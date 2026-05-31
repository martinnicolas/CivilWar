/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mygame.enemies.ZombieEnemy;
import com.mygame.bonuses.AmmoBonus;
import com.mygame.bonuses.HealthBonus;
import com.mygame.characters.Player;

/**
 *
 * @author martin
 */
public class Level1 extends Level implements PhysicsCollisionListener {

    //Temporary vectors used on each frame. They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f(), camLeft = new Vector3f();
    //Scene path for Level 1
    private static final String SCENE_PATH = "Scenes/Level1.j3o";
    //Player initial location for Level 1.
    private static final Vector3f PLAYER_INITIAL_LOCATION = new Vector3f(300, 10, 325);
    // Visual sun position in the skybox. The sun is painted on arid2_rt.
    private static final Vector3f SUN_POSITION_DIRECTION = new Vector3f(-15.0f, 16.35f, -25.9f);
    private static final ColorRGBA SUN_LIGHT_COLOR = new ColorRGBA(1.0f, 0.74f, 0.48f, 1.0f);
    private static final float SUN_LIGHT_INTENSITY = 0.65f;
    private static final float SUN_SCATTERING_DENSITY = 0.35f;
    //Shadow map size
    private static final int SHADOWMAP_SIZE = 2048;
    //Shadow size
    private static final int SHADOW_SIZE = 2;
    //Loading steps
    private static final int LOADING_STEPS = 8;
    //Lights
    private DirectionalLight directionalLight;
    private BulletAppState bulletAppState;
    private final Map<Spatial, ZombieEnemy> enemiesBySpatial = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.setLoadingSteps(Level1.LOADING_STEPS);
    }
    
    @Override
    public boolean loadLevel() {
        switch (this.getLoadingStep()) {
            case 0 -> this.setUpAudio();
            case 1 -> {
                this.setBulletAppState(new BulletAppState());
                this.getApp().getStateManager().attach(this.getBulletAppState());
            }
            case 2 -> this.loadScene();
            case 3 -> this.loadPlayer();
            case 4 -> this.loadEnemies();
            case 5 -> this.loadAmmoBonus();
            case 6 -> this.loadHealthBonus();
            case 7 -> {
                this.setUpLights();
                this.setUpShadowProcessors();
                this.getBulletAppState().getPhysicsSpace().addCollisionListener(this);
            }
        }
        this.setLoadingStep(this.getLoadingStep() + 1);
        return this.getLoadingStep() >= this.getLoadingSteps();
    }

    @Override
    public void update(float tpf) {
        if (this.getPlayer() == null) {
            return;
        }

        camDir.set(this.getApp().getCamera().getDirection()).multLocal(0.3f);
        camLeft.set(this.getApp().getCamera().getLeft()).multLocal(0.2f);
        this.getPlayer().getWalkDirection().set(0, 0, 0);
        if (this.getPlayer().isLeft()) {
            this.getPlayer().getWalkDirection().addLocal(camLeft);
        }
        if (this.getPlayer().isRight()) {
            this.getPlayer().getWalkDirection().addLocal(camLeft.negate());
        }
        if (this.getPlayer().isUp()) {
            this.getPlayer().getWalkDirection().addLocal(camDir);
        }
        if (this.getPlayer().isDown()) {
            this.getPlayer().getWalkDirection().addLocal(camDir.negate());
        }
        this.getPlayer().getControl().setWalkDirection(this.getPlayer().getWalkDirection());
        this.getApp().getCamera().setLocation(this.getPlayer().getControl().getPhysicsLocation());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        this.removeSettings();
    }
    
    /**
     * Setup lights
     */
    private void setUpLights() {
        this.setDirectionalLight(new DirectionalLight());
        this.getDirectionalLight().setDirection(Level1.SUN_POSITION_DIRECTION.normalize().negate());
        this.getDirectionalLight().setColor(Level1.SUN_LIGHT_COLOR.mult(Level1.SUN_LIGHT_INTENSITY));
        this.getApp().getRootNode().addLight(this.getDirectionalLight());        
    }
    
    /**
     * Load scene model and collision.
     */
    private void loadScene() {
        Spatial sceneModel = this.getApp().getAssetManager().loadModel(Level1.SCENE_PATH);
        sceneModel.setLocalScale(2f);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        this.setControl(new RigidBodyControl(sceneShape, 0));
        sceneModel.addControl(this.getControl());
        this.getLevelNode().attachChild(sceneModel);
        this.getBulletAppState().getPhysicsSpace().addAll(sceneModel);
    }
    
    /**
     * Load player.
     */
    private void loadPlayer() {
        this.setPlayer(new Player(this));
        this.getPlayer().setInitialLocation(Level1.PLAYER_INITIAL_LOCATION);
        this.getLevelNode().attachChild(this.getPlayer().getPlayerNode());
        this.getBulletAppState().getPhysicsSpace().addAll(this.getPlayer().getPlayerNode());
    }
    
    /**
     * Load level enemies.
     */
    private void loadEnemies() {
        Vector3f[] enemiesPositions = {
            new Vector3f(-320, 10, -320),
            new Vector3f(320, 10, -320),
            new Vector3f(-320, 10, 320),
            new Vector3f(320, 10, 320),
            new Vector3f(0, 10, -450),
            new Vector3f(0, 10, 450),
            new Vector3f(-450, 10, 0),
            new Vector3f(450, 10, 0),
            new Vector3f(-500, 10, 500),
            new Vector3f(500, 10, -500),
            new Vector3f(200, 10, -100),
            new Vector3f(100, 10, -50),
            new Vector3f(50, 10, -300),
            new Vector3f(20, 10, -20),
            new Vector3f(20, 10, -200),
            new Vector3f(20, 10, -50),
            new Vector3f(0, 10, 0),
            new Vector3f(0, 10, 50),
            new Vector3f(10, 10, 50),
            new Vector3f(10, 10, 70),
        };

        for (Vector3f position : enemiesPositions) {
            ZombieEnemy zombieEnemy = new ZombieEnemy(this.getApp().getAssetManager(), 100, 10);
            zombieEnemy.getSpatial().setLocalTranslation(position);
            this.getEnemiesBySpatial().put(zombieEnemy.getSpatial(), zombieEnemy);
            this.getLevelNode().attachChild(zombieEnemy.getSpatial());
            this.getBulletAppState().getPhysicsSpace().addAll(zombieEnemy.getSpatial());
        }
    }
    
    /**
     * Load ammo bonus.
     */
    private void loadAmmoBonus() {
        Vector3f[] ammoBonusesPositions = {
            new Vector3f(-28, 10, 26),
            new Vector3f(40, 10, 80),
            new Vector3f(-90, 10, -40),
        };
        
        for (Vector3f position : ammoBonusesPositions) {
            AmmoBonus ammoBonus = new AmmoBonus(this.getApp().getAssetManager(), 10, position);
            this.getLevelNode().attachChild(ammoBonus.getSpatial());
            this.getBulletAppState().getPhysicsSpace().addAll(ammoBonus.getSpatial());   
        }
    }
    
    /**
     * Load health bonus.
     */
    private void loadHealthBonus() {
        Vector3f[] healthBonusesPositions = {
            new Vector3f(-28, 10, 45),
            new Vector3f(40, 10, 88),
            new Vector3f(-90, 10, -60),
        };

        for (Vector3f position : healthBonusesPositions) {
          HealthBonus healthBonus = new HealthBonus(this.getApp().getAssetManager(), 10, position);
          this.getLevelNode().attachChild(healthBonus.getSpatial());
          this.getBulletAppState().getPhysicsSpace().addAll(healthBonus.getSpatial());
        }
    }
    
    /**
     * Setup shadow processors
     */
    private void setUpShadowProcessors() {
        //Add ambient light
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.08f));
        this.getApp().getRootNode().addLight(ambient);
        //Add some shadows
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(this.getApp().getAssetManager(), Level1.SHADOWMAP_SIZE, Level1.SHADOW_SIZE);
        dlsr.setLight(this.getDirectionalLight());
        this.getApp().getViewPort().addProcessor(dlsr);
        //Add light scattering
        FilterPostProcessor fpp = new FilterPostProcessor(this.getApp().getAssetManager());
        LightScatteringFilter sunlight = new LightScatteringFilter(Level1.SUN_POSITION_DIRECTION.normalize().mult(3000f));
        sunlight.setLightDensity(Level1.SUN_SCATTERING_DENSITY);
        //Add bonus bloom
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBloomIntensity(2f);
        //Add Filter Post Processor
        fpp.addFilter(sunlight);
        fpp.addFilter(bloom);
        this.getApp().getViewPort().addProcessor(fpp);
    }

    @Override
    public void setUpAudio() {
        this.setAudioNode(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/Outdoor_Ambiance.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setName("audio_level1");
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getApp().getRootNode().attachChild(this.getAudioNode());
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        //Check for collisions with ammo
        this.checkPlayerCollisionsWithBonus(event, AmmoBonus.SPATIAL_NAME);
        //Check for collisions with health
        this.checkPlayerCollisionsWithBonus(event, HealthBonus.SPATIAL_NAME);
        //Check for player collisions with enemy
        this.checkPlayerCollisionsWithEnemy(event, Player.SPATIAL_NAME);
    }
    
    /**
     * Checks for player collisions with some bonus kind
     * 
     * @param event
     * @param bonusName 
     */
    private void checkPlayerCollisionsWithBonus(PhysicsCollisionEvent event, String bonusName) {
        Spatial nodeA = event.getNodeA();
        Spatial nodeB = event.getNodeB();
        Spatial bonus = null;

        if (hasName(nodeA, bonusName)) {
            bonus = nodeA;
        } else if (hasName(nodeB, bonusName)) {
            bonus = nodeB;
        }

        if (bonus == null) { 
            return; 
        }

        if (hasName(nodeA, Player.SPATIAL_NAME) || hasName(nodeB, Player.SPATIAL_NAME)) {
            pickBonus(bonus);
        }
    }

    @Override
    public boolean handleShootingCollision(Ray ray) {
        Vector3f rayFrom = ray.getOrigin();
        Vector3f rayTo = ray.getDirection().normalize().multLocal(1000f).addLocal(rayFrom);
        List<PhysicsRayTestResult> results = this.getBulletAppState().getPhysicsSpace().rayTest(rayFrom, rayTo);
        ZombieEnemy closestEnemy = null;
        float closestHitFraction = Float.MAX_VALUE;

        for (PhysicsRayTestResult result : results) {
            if (!(result.getCollisionObject() instanceof GhostControl)) {
                continue;
            }

            GhostControl ghostControl = (GhostControl) result.getCollisionObject();
            ZombieEnemy enemy = this.getEnemiesBySpatial().get(ghostControl.getSpatial());
            if (enemy == null) {
                continue;
            }

            if (!enemy.isAlive()) {
                continue;
            }

            if (result.getHitFraction() < closestHitFraction) {
                closestHitFraction = result.getHitFraction();
                closestEnemy = enemy;
            }
        }

        if (closestEnemy == null) {
            return false;
        }

        closestEnemy.kill(this.getBulletAppState().getPhysicsSpace());
        return true;
    }
    
    /**
     * Check player collision with enemies
     * 
     * @param event
     * @param playerName 
     */
    private void checkPlayerCollisionsWithEnemy(PhysicsCollisionEvent event, String playerName) {
        Spatial nodeA = event.getNodeA();
        Spatial nodeB = event.getNodeB();
        Spatial enemy = null;

        if (hasName(nodeA, ZombieEnemy.SPATIAL_NAME)) {
            enemy = nodeA;
        } else if (hasName(nodeB, ZombieEnemy.SPATIAL_NAME)) {
            enemy = nodeB;
        }

        if (enemy == null) {
            return;
        }

        if (hasName(nodeA, Player.SPATIAL_NAME) || hasName(nodeB, Player.SPATIAL_NAME)) {
            this.getPlayer().loadDamage(enemy);
            if (!this.getPlayer().getControl().haveEnoughHealth()) {
                this.gameOver();
            }
        }
    }
    
    /**
     * Pick bonus spatial and remove it from scene
     * 
     * @param spatial 
     */
    private void pickBonus(Spatial bonusSpatial) {
        if (bonusSpatial == null) { return; }

        RigidBodyControl spatialControl = bonusSpatial.getControl(RigidBodyControl.class);
        if (spatialControl != null && spatialControl.isEnabled()) {
            this.getPlayer().plusPickedBonus(bonusSpatial);
            spatialControl.setEnabled(false);
            this.getBulletAppState().getPhysicsSpace().remove(spatialControl);
            bonusSpatial.removeControl(spatialControl);
            bonusSpatial.removeFromParent();
            bonusSpatial.setLocalScale(0.0f);
        }
    }
    
    private boolean hasName(Spatial spatial, String name) {
        return spatial != null && name.equals(spatial.getName());
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }

    public Map<Spatial, ZombieEnemy> getEnemiesBySpatial() {
        return enemiesBySpatial;
    }
}
