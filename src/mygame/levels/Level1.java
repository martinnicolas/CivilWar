/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.List;
import mygame.enemies.SoldierEnemy;
import mygame.Main;
import mygame.bonuses.AmmoBonus;
import mygame.bonuses.HealthBonus;
import mygame.characters.Player;

/**
 *
 * @author martin
 */
public class Level1 extends Level implements PhysicsCollisionListener{

    //Temporary vectors used on each frame. They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f(), camLeft = new Vector3f();
    //Scene path for Level 1
    private static final String SCENE_PATH = "Scenes/Level1.j3o";
    //Player initial location for Level 1.
    private static final Vector3f PLAYER_INITIAL_LOCATION = new Vector3f(300, 10, 325);
    //Sun light direction
    private static final Vector3f LIGHT_DIRECTION = new Vector3f(0.5f, -0.5f, -0.5f);
    //Shadow map size
    private static final int SHADOWMAP_SIZE = 4096;
    //Shadow size
    private static final int SHADOW_SIZE = 4;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.setApp((Main) app);
        this.setStateManager(stateManager);
        this.setRootNode(this.getApp().getRootNode());
        this.setAssetManager(this.getApp().getAssetManager());
        this.setLocalRootNode(new Node("Level 1"));
        this.getRootNode().attachChild(this.getLocalRootNode());

        //Setup audio effects for Level 1
        this.setUpAudio();

        //Setup Physics
        BulletAppState bulletAppState = new BulletAppState();
        this.getStateManager().attach(bulletAppState);

        // We load the scene from the zip file and adjust its size.
        //assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = this.getAssetManager().loadModel(Level1.SCENE_PATH);
        sceneModel.setLocalScale(2f);
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        // We attach the scene to the rootnode and the physics space,
        // to make it appear in the game world.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        this.setControl(new RigidBodyControl(sceneShape, 0));
        sceneModel.addControl(this.getControl());
        this.getLocalRootNode().attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().addAll(sceneModel);
        
        //Setup lights and shadows
        this.setUpLightsAndShadows();
        
        //Load player
        this.setPlayer(new Player(this));
        this.getPlayer().setInitialLocation(Level1.PLAYER_INITIAL_LOCATION);
        this.getLocalRootNode().attachChild(this.getPlayer().getPlayerNode());
        bulletAppState.getPhysicsSpace().addAll(this.getPlayer().getPlayerNode());

        //Load some enemy
        SoldierEnemy soldierEnemy = new SoldierEnemy(this.getAssetManager(), 100, 10);
        this.getLocalRootNode().attachChild(soldierEnemy.getSpatial());
        bulletAppState.getPhysicsSpace().addAll(soldierEnemy.getSpatial());
        
        //Load some ammo bonus
        AmmoBonus ammoBonus = new AmmoBonus(this.getAssetManager(), 10, new Vector3f(-29, 10, 26));
        this.getLocalRootNode().attachChild(ammoBonus.getSpatial());
        bulletAppState.getPhysicsSpace().addAll(ammoBonus.getSpatial());
        
        //Load some health bonus
        HealthBonus healthBonus = new HealthBonus(this.getAssetManager(), 10, new Vector3f(-29, 10, 27));
        this.getLocalRootNode().attachChild(healthBonus.getSpatial());
        bulletAppState.getPhysicsSpace().addAll(healthBonus.getSpatial());
        
        //Add collision listener
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
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
        this.removeSettings();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        this.getRootNode().detachChild(this.getLocalRootNode());
        super.cleanup();
    }
    
    /**
     * Setup lights and shadows
     */
    private void setUpLightsAndShadows() {
        //Add some light
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(Level1.LIGHT_DIRECTION.normalizeLocal());
        this.getLocalRootNode().addLight(directionalLight);
        //Add some shadows
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(this.getAssetManager(), Level1.SHADOWMAP_SIZE, Level1.SHADOW_SIZE);
        dlsr.setLight(directionalLight);
        this.getApp().getViewPort().addProcessor(dlsr);
    }
    
    /**
     * Setup audio
     */
    @Override
    public void setUpAudio() {
        //Setup ambient audio
        this.setAudioNode(new AudioNode(this.getAssetManager(), "Sounds/Effects/Outdoor_Ambiance.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getLocalRootNode().attachChild(this.getAudioNode());
        this.getAudioNode().play();// play continuously!
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        //Check for collisions with ammo
        this.checkPlayerCollisionsWithBonus(event, AmmoBonus.SPATIAL_NAME);
        //Check for collisions with health
        this.checkPlayerCollisionsWithBonus(event, HealthBonus.SPATIAL_NAME);
        //Check for shooting collisions with enemy
        this.checkShootingCollisionsWithEnemy(event, SoldierEnemy.SPATIAL_NAME);
        //Check for player collisions with enemy
        this.checkPlayerCollisionsWithEnemy(event, Player.SPATIAL_NAME);
        //Restore controls for alive spatials
        this.restoreControlForAlives();
    }
    
    /**
     * Checks for player collisions with some bonus kind
     * 
     * @param event
     * @param bonusName 
     */
    private void checkPlayerCollisionsWithBonus(PhysicsCollisionEvent event, String bonusName) {
        if (bonusName.equals(event.getNodeA().getName()) || bonusName.equals(event.getNodeB().getName())) {
            if (event.getNodeA().getName().equals(Player.SPATIAL_NAME)) {
                this.pickBonus(event.getNodeB());
            } else {
                if (event.getNodeB().getName().equals(Player.SPATIAL_NAME)) {
                    this.pickBonus(event.getNodeA());
                }
            }
        }
    }
    
    /**
     * Checks shooting collisions with enemies
     * 
     * @param event
     * @param enemyName 
     */
    private void checkShootingCollisionsWithEnemy(PhysicsCollisionEvent event, String enemyName) {
        if (enemyName.equals(event.getNodeA().getName()) || enemyName.equals(event.getNodeB().getName())) {
            if (event.getNodeA().getName().equals("shoots_mark")) {
                this.removeShootingMarks(event.getNodeA());
                this.removeDeadEnemy(event.getNodeB());
            } else {
                if (event.getNodeB().getName().equals("shoots_mark")) {
                    this.removeShootingMarks(event.getNodeB());
                    this.removeDeadEnemy(event.getNodeA());
                }
            }
        }
    }
    
    /**
     * Check player collision with enemies
     * 
     * @param event
     * @param playerName 
     */
    private void checkPlayerCollisionsWithEnemy(PhysicsCollisionEvent event, String playerName) {
        if (playerName.equals(event.getNodeA().getName()) || playerName.equals(event.getNodeB().getName())) {
            if (event.getNodeA().getName().equals(SoldierEnemy.SPATIAL_NAME)) {
                this.getPlayer().loadDamage();
            } else {
                if (event.getNodeB().getName().equals(SoldierEnemy.SPATIAL_NAME)) {
                    this.getPlayer().loadDamage();
                }
            }
        }
    }
    
    /**
     * Pick bonus spatial and remove it from scene
     * 
     * @param spatial 
     */
    private void pickBonus(Spatial bonusSpatial) {
        RigidBodyControl spatialControl = bonusSpatial.getControl(RigidBodyControl.class);
        if(spatialControl != null && spatialControl.isEnabled()) {
            this.getPlayer().plusPickedBonus(bonusSpatial);
            spatialControl.setEnabled(false);
            bonusSpatial.removeFromParent();
            bonusSpatial.setLocalScale(0.0f);
        }
    }
    
    /**
     * Remove shooting marks from scene
     * 
     * @param spatial 
     */
    private void removeShootingMarks(Spatial spatial) {
        RigidBodyControl spatialControl = spatial.getControl(RigidBodyControl.class);
        if(spatialControl != null && spatialControl.isEnabled()) {
            spatialControl.setEnabled(false);
            spatial.removeFromParent();
            spatial.setLocalScale(0.0f);
        }
    }
    
    /**
     * Remove dead enemy
     * 
     * @param spatial 
     */
    private void removeDeadEnemy(Spatial spatial) {
        RigidBodyControl spatialControl = spatial.getControl(RigidBodyControl.class);
        if(spatialControl != null && spatialControl.isEnabled()) {
            spatialControl.setEnabled(false);
            spatial.removeFromParent();
            spatial.setLocalScale(0.0f);
        }
    }  

    private void restoreControlForAlives() {
        //enable all controls
        List<Spatial> spatials = this.getLocalRootNode().getChildren();
        for (Spatial spatial : spatials) {
            for (int i = 0; i < spatial.getNumControls(); i++) {
                Control spatialControl = spatial.getControl(i);
                if (spatialControl instanceof PhysicsControl) {
                    PhysicsControl physicsControl = (PhysicsControl) spatialControl;
                    physicsControl.setEnabled(true);
                }
            }
        }
    }

}
