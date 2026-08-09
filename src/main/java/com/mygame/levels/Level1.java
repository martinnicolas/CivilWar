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
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Torus;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mygame.enemies.ZombieEnemy;
import com.mygame.bonuses.AmmoBonus;
import com.mygame.bonuses.HealthBonus;
import com.mygame.characters.Player;
import com.mygame.collectibles.FuelCollectible;
import com.mygame.collectibles.HealthCollectible;
import com.mygame.controls.EnemyControl;
import com.mygame.collectibles.WaterCollectible;
import com.mygame.effects.ExplosionSequence;
import com.mygame.settings.Level1Settings;
import com.mygame.utils.Spawnable;
import com.mygame.utils.SpawnFactory;
import com.mygame.utils.SpawnSettings;

/**
 *
 * @author martin
 */
public class Level1 extends Level implements PhysicsCollisionListener, SpawnSettings {

    //Temporary vectors used on each frame. They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f(), camLeft = new Vector3f();
    //Scene path for Level 1
    private static final String SCENE_PATH = "Scenes/Level1.j3o";
    //Player initial location for Level 1.
    private static final Vector3f PLAYER_INITIAL_LOCATION = new Vector3f(300, 10, 325);
    //Player exit location for Level1
    private static final Vector3f PLAYER_EXIT_LOCATION = new Vector3f(-93, 3, -395);
    //Player exit location properties
    private static final float EXIT_RADIUS = 4f;
    private static final float EXIT_RADIUS_SQUARED = EXIT_RADIUS * EXIT_RADIUS;
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
    private static final int LOADING_STEPS = 11;
    //Lights
    private DirectionalLight directionalLight;
    private BulletAppState bulletAppState;
    private final Map<Spatial, ZombieEnemy> enemiesBySpatial = new HashMap<>();
    //Explosion sequence
    private ExplosionSequence explosionSequence;
    
    public Level1() {
        this.setMaxTimeToFinish(Level1Settings.getMaxTimeToFinishTheLevel());
    }

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
            case 7 -> this.loadWaterCollectible();
            case 8 -> this.loadHealthCollectible();
            case 9 -> this.loadFuelCollectible();
            case 10 -> {
                this.setUpLights();
                this.setUpShadowProcessors();
                this.getBulletAppState().getPhysicsSpace().addCollisionListener(this);
            }
        }
        this.setLoadingStep(this.getLoadingStep() + 1);
        return this.getLoadingStep() >= this.getLoadingSteps();
    }
    
    /**
     * Load player.
     */
    @Override
    public void loadPlayer() {
        this.setPlayer(new Player(this));
        this.getPlayer().setInitialLocation(Level1.PLAYER_INITIAL_LOCATION);
        this.getLevelNode().attachChild(this.getPlayer().getPlayerNode());
        this.getBulletAppState().getPhysicsSpace().addAll(this.getPlayer().getPlayerNode());
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
        
        if (this.getPlayer().getControl().enoughRemainingTime()) {
            this.getPlayer().getControl().decrementRemainingTime(tpf);
        }
        
        if (!this.getPlayer().getControl().enoughRemainingTime() && explosionSequence == null) {
            explosionSequence = new ExplosionSequence(this, Level1Settings.getExplosionPositions());
        }
        
        if (explosionSequence != null) { 
            explosionSequence.update(tpf);
            if (explosionSequence.isFinished()) { this.gameOver(); }
        }
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
     * Load level enemies.
     */
    private void loadEnemies() {
        for (ZombieEnemy zombieEnemy : SpawnFactory.spawn(ZombieEnemy.class, this, this)) {
            this.attachSpawned(zombieEnemy);
            this.getEnemiesBySpatial().put(zombieEnemy.getSpatial(), zombieEnemy);
        }
    }
    
    /**
     * Load ammo bonus.
     */
    private void loadAmmoBonus() {
        for (AmmoBonus ammoBonus : SpawnFactory.spawn(AmmoBonus.class, this, this)) {
            this.attachSpawned(ammoBonus);
        }
    }
    
    /**
     * Load health bonus.
     */
    private void loadHealthBonus() {
        for (HealthBonus healthBonus : SpawnFactory.spawn(HealthBonus.class, this, this)) {
            this.attachSpawned(healthBonus);
        }
    }
    
    /**
     * Load water collectible.
     */
    private void loadWaterCollectible() {
        for (WaterCollectible waterCollectible : SpawnFactory.spawn(WaterCollectible.class, this, this)) {
            this.attachSpawned(waterCollectible);
        }
    }
    
    /**
     * Load health collectible.
     */
    private void loadHealthCollectible() {
        for (HealthCollectible healthCollectible : SpawnFactory.spawn(HealthCollectible.class, this, this)) {
            this.attachSpawned(healthCollectible);
        }
    }
    
    /**
     * Load health collectible.
     */
    private void loadFuelCollectible() {
        for (FuelCollectible fuelCollectible : SpawnFactory.spawn(FuelCollectible.class, this, this)) {
            this.attachSpawned(fuelCollectible);
        }
    }

    private void attachSpawned(Spawnable spawnable) {
        this.getLevelNode().attachChild(spawnable.getSpatial());
        this.getBulletAppState().getPhysicsSpace().addAll(spawnable.getSpatial());
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
        //Check for collisions with water collectible
        this.checkPlayerCollisionsWithCollectible(event, WaterCollectible.SPATIAL_NAME);
        //Check for collisions with health collectible
        this.checkPlayerCollisionsWithCollectible(event, HealthCollectible.SPATIAL_NAME);
        //Check for collisions with fuel collectible
        this.checkPlayerCollisionsWithCollectible(event, FuelCollectible.SPATIAL_NAME);
        //Check for player collisions with enemy
        this.checkPlayerCollisionsWithEnemy(event);
        
        //check player exit reached
        if (this.getPlayer().getControl().amountOfCollectiblesCollected() == this.getMaxAmountOfCollectibles()) {
            this.checkPlayerExit();
        }
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
            this.pickBonus(bonus);
        }
    }
    
     /**
     * Checks for player collisions with some bonus kind
     * 
     * @param event
     * @param bonusName 
     */
    private void checkPlayerCollisionsWithCollectible(PhysicsCollisionEvent event, String collectibleName) {
        Spatial nodeA = event.getNodeA();
        Spatial nodeB = event.getNodeB();
        Spatial collectible = null;

        if (hasName(nodeA, collectibleName)) {
            collectible = nodeA;
        } else if (hasName(nodeB, collectibleName)) {
            collectible = nodeB;
        }

        if (collectible == null) { 
            return; 
        }

        if (hasName(nodeA, Player.SPATIAL_NAME) || hasName(nodeB, Player.SPATIAL_NAME)) {
            this.pickCollectible(collectible);
            
            if (this.getPlayer().getControl().amountOfCollectiblesCollected() == this.getMaxAmountOfCollectibles()) {
                this.markPlayerExit();
            }
        }
    }
    
    private void markPlayerExit() {
        Torus torus = new Torus(64, 32, 0.12f, 4f);
        Geometry ring = new Geometry("ExitRing", torus);

        // Acostarlo sobre el suelo
        ring.rotate(FastMath.HALF_PI, 0f, 0f);

        // Apenas elevado para evitar z-fighting
        ring.setLocalTranslation(PLAYER_EXIT_LOCATION.x, 0, PLAYER_EXIT_LOCATION.z);

        Material material = new Material(this.getApp().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Diffuse", ColorRGBA.Cyan);
        material.setColor("Ambient", ColorRGBA.Cyan.mult(0.3f));
        material.setColor("GlowColor", ColorRGBA.Cyan.mult(3f));

        ring.setMaterial(material);
        this.getApp().getRootNode().attachChild(ring);
    }

    @Override
    public void handleShootingCollision(Ray ray) {
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
            return;
        }

        closestEnemy.loadDamage(this.getPlayer().getWeapon().getDamage());

        if (closestEnemy.isAlive()) { return; }
        
        closestEnemy.die();

        closestEnemy.getSpatial().removeControl(EnemyControl.class);
        PhysicsControl physicsControl = closestEnemy.getSpatial().getControl(PhysicsControl.class);
        if (physicsControl != null) {
            physicsControl.setEnabled(false);
            this.getBulletAppState().getPhysicsSpace().remove(physicsControl);
            closestEnemy.getSpatial().removeControl(physicsControl);
        }
    }
    
    @Override
    public List<Vector3f> getZombieSpawnPositions() {
        return Level1Settings.getZombieSpawnPositions();
    }
    
    @Override
    public List<Vector3f> getAmmoBonusSpawnPositions() {
        return Level1Settings.getAmmoBonusSpawnPositions();
    }
    
    @Override
    public List<Vector3f> getHealthBonusSpawnPositions() {
        return Level1Settings.getHealthBonusSpawnPositions();
    }   
    
    @Override
    public List<Vector3f> getWaterCollectibleSpawnPositions() {
        return Level1Settings.getWaterCollectibleSpawnPositions();
    }
    
    @Override
    public List<Vector3f> getHealthCollectibleSpawnPositions() {
        return Level1Settings.getHealthCollectibleSpawnPositions();
    }
    
    @Override
    public List<Vector3f> getFuelCollectibleSpawnPositions() {
        return Level1Settings.getFuelCollectibleSpawnPositions();
    }
    
    @Override
    public int getMaxSizeOfWaterCollectibles() {
        return Level1Settings.getWaterCollectibleSpawnPositions().size();
    }
    
    @Override
    public int getMaxSizeOfHealthCollectibles() {
        return Level1Settings.getHealthCollectibleSpawnPositions().size();
    }
    
    @Override
    public int getMaxSizeOfFuelCollectibles() {
        return Level1Settings.getFuelCollectibleSpawnPositions().size();
    }
    
    @Override
    public List<Vector3f> getExplosionPositions() {
        return Level1Settings.getExplosionPositions();
    }

    /**
     * Check player collision with enemies
     * 
     * @param event
     * @param playerName 
     */
    private void checkPlayerCollisionsWithEnemy(PhysicsCollisionEvent event) {
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
    
    private void checkPlayerExit() {
        Vector3f playerPosition = this.getPlayer().getControl().getSpatial().getLocalTranslation();

        float dx = playerPosition.x - PLAYER_EXIT_LOCATION.x;
        float dz = playerPosition.z - PLAYER_EXIT_LOCATION.z;

        float distanceSquared = dx * dx + dz * dz;

        if (distanceSquared <= EXIT_RADIUS_SQUARED) {
            this.finishLevel();
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
    
    /**
     * Pick element spatial and remove it from scene
     * 
     * @param spatial 
     */
    private void pickCollectible(Spatial collectibleSpatial) {
        if (collectibleSpatial == null) { return; }

        RigidBodyControl spatialControl = collectibleSpatial.getControl(RigidBodyControl.class);
        if (spatialControl != null && spatialControl.isEnabled()) {
            this.getPlayer().plusPickedCollectible(collectibleSpatial);
            spatialControl.setEnabled(false);
            this.getBulletAppState().getPhysicsSpace().remove(spatialControl);
            collectibleSpatial.removeControl(spatialControl);
            collectibleSpatial.removeFromParent();
            collectibleSpatial.setLocalScale(0.0f);
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
