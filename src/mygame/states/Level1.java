/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;
import mygame.characters.Player;

/**
 *
 * @author martin
 */
public class Level1 extends Level implements PhysicsCollisionListener {

    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f(), camLeft = new Vector3f();
    //Player initial location for Level 1.
    private static final Vector3f PLAYER_INITIAL_LOCATION = new Vector3f(600, 20, 650);

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

        //Set up audio effects for Level 1
        this.setUpAudio();

        //Set up Physics
        BulletAppState bulletAppState = new BulletAppState();
        this.getStateManager().attach(bulletAppState);

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        this.getApp().getFlyByCamera().setMoveSpeed(100);

        // We load the scene from the zip file and adjust its size.
        //assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = this.getAssetManager().loadModel("Scenes/Level1.j3o");
        sceneModel.setLocalScale(2f);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        this.setControl(new RigidBodyControl(sceneShape, 0));
        sceneModel.addControl(this.getControl());

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        this.getLocalRootNode().attachChild(sceneModel);
        this.setPlayer(new Player(this));
        this.getPlayer().setInitialLocation(Level1.PLAYER_INITIAL_LOCATION);
        bulletAppState.getPhysicsSpace().add(this.getPlayer().getControl());
        bulletAppState.getPhysicsSpace().add(this.getPlayer().getPlayerGeometry());        
        bulletAppState.getPhysicsSpace().add(this.getControl());

        //Load model for jeep
        Spatial jeep1 = this.getAssetManager().loadModel("Models/jeep1/jeep1.j3o");
        jeep1.setName("jeep1");
        jeep1.setLocalTranslation(600, 0, 700);
        jeep1.setLocalScale(7f);
        jeep1.addControl(new RigidBodyControl(10));
        this.getLocalRootNode().attachChild(jeep1);
        bulletAppState.getPhysicsSpace().add(jeep1);

        //Load model for barrel
        Spatial barrel = this.getAssetManager().loadModel("Models/barrel01/barrel01.j3o");
        barrel.setName("barril");
        barrel.setLocalTranslation(500, 0, 700);
        barrel.setLocalScale(3f);
        barrel.addControl(new RigidBodyControl(10));
        this.getLocalRootNode().attachChild(barrel);
        bulletAppState.getPhysicsSpace().add(barrel);

        //Add some light
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal());
        this.getRootNode().addLight(directionalLight);

        //Add collision listener
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        camDir.set(this.getApp().getCamera().getDirection()).multLocal(0.6f);
        camLeft.set(this.getApp().getCamera().getLeft()).multLocal(0.4f);
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
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        this.getRootNode().detachChild(this.getLocalRootNode());
        super.cleanup();
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        //Discard collisions with Scene
        if (!event.getNodeA().getName().equals("Scene") && !event.getNodeB().getName().equals("Scene")) {
            //Check collisions for player with ammo
            this.checkCollisionsWithAmmo(event);
            //Check collisions for player with health
            this.checkCollisionWithHealth(event);
            //Check for shooting collisions with enemy
            this.checkShootingCollisionWithEnemy(event);
        }
    }

    /**
     * Check for picking an ammo bonus
     *
     * @param event
     */
    private void checkCollisionsWithAmmo(PhysicsCollisionEvent event) {
        if ("player".equals(event.getNodeA().getName()) || "player".equals(event.getNodeB().getName())) {
            if ("barril".equals(event.getNodeA().getName()) || "barril".equals(event.getNodeB().getName())) {
                if ("barril".equals(event.getNodeA().getName())) {
                    this.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(event.getNodeA());
                    this.getLocalRootNode().detachChild(event.getNodeA());
                    this.getPlayer().plusAmmoes(10);
                }
                else {
                    this.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(event.getNodeB());
                    this.getLocalRootNode().detachChild(event.getNodeB());
                    this.getPlayer().plusAmmoes(10);
                } 
            }
        }
    }

    /**
     * Check for picking a health bonus
     *
     * @param event
     */
    private void checkCollisionWithHealth(PhysicsCollisionEvent event) {
         if ("player".equals(event.getNodeA().getName()) || "player".equals(event.getNodeB().getName())) {
            if ("jeep1".equals(event.getNodeA().getName()) || "jeep1".equals(event.getNodeB().getName())) {
                if ("jeep1".equals(event.getNodeA().getName())) {
                    this.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(event.getNodeA());
                    this.getLocalRootNode().detachChild(event.getNodeA());
                    this.getPlayer().plusHealth(10);
                }
                else {
                    this.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(event.getNodeB());
                    this.getLocalRootNode().detachChild(event.getNodeB());                    
                    this.getPlayer().plusHealth(10);
                }
            }
        }
    }
    
    /**
     * Check for shooting an enemy
     * 
     * @param event 
     */
    private void checkShootingCollisionWithEnemy(PhysicsCollisionEvent event) {
        if ("shoots_mark".equals(event.getNodeA().getName()) || "shoots_mark".equals(event.getNodeB().getName())) {
            if ("enemy".equals(event.getNodeA().getName()) || "enemy".equals(event.getNodeB().getName())) {
                System.out.println("---- You hit the enemy!----");
            }
        }
    }

    /**
     * Settup audio
     */
    private void setUpAudio() {
        //Setup ambient audio
        this.setAudioNode(new AudioNode(this.getAssetManager(), "Sounds/Effects/Outdoor_Ambiance.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getLocalRootNode().attachChild(this.getAudioNode());
        this.getAudioNode().play();// play continuously!
    }

}
