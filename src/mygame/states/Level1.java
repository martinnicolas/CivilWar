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
public class Level1 extends Level {

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

        //Setup audio effects for Level 1
        this.setUpAudio();

        //Setup Physics
        BulletAppState bulletAppState = new BulletAppState();
        this.getStateManager().attach(bulletAppState);

        // We load the scene from the zip file and adjust its size.
        //assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = this.getAssetManager().loadModel("Scenes/Level1.j3o");
        sceneModel.setLocalScale(2f);
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        this.setControl(new RigidBodyControl(sceneShape, 0));
        sceneModel.addControl(this.getControl());
        this.getLocalRootNode().attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().addAll(sceneModel);
        //Add some light to localRootNode
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal());
        this.getLocalRootNode().addLight(directionalLight);
        
        //Load player
        this.setPlayer(new Player(this));
        this.getPlayer().setInitialLocation(Level1.PLAYER_INITIAL_LOCATION);
        this.getLocalRootNode().attachChild(this.getPlayer().getPlayerNode());
        bulletAppState.getPhysicsSpace().addAll(this.getPlayer().getPlayerNode());

        //Load model for enemy
        Spatial enemy = this.getAssetManager().loadModel("Models/D05150293DS/D0515029.j3o");
        enemy.setName("enemy");
        enemy.setLocalTranslation(600, 0, 700);
        enemy.setLocalScale(0.8f);
        enemy.addControl(new RigidBodyControl(0));
        this.getLocalRootNode().attachChild(enemy);
        bulletAppState.getPhysicsSpace().addAll(enemy);

        //Load model for ammo
        Spatial ammo = this.getAssetManager().loadModel("Models/barrel01/barrel01.j3o");
        ammo.setName("ammo");
        ammo.setLocalTranslation(500, 0, 700);
        ammo.setLocalScale(3f);
        ammo.addControl(new RigidBodyControl(2f));
        this.getLocalRootNode().attachChild(ammo);
        bulletAppState.getPhysicsSpace().addAll(ammo);
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
