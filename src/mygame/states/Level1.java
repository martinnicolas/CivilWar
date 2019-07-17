/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;
import mygame.caracters.Player;

/**
 *
 * @author martin
 */
public class Level1 extends Level {

    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f(), camLeft = new Vector3f();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.setApp((Main) app);
        this.setRootNode(this.getApp().getRootNode());
        this.setAssetManager(this.getApp().getAssetManager());
        this.setLocalRootNode(new Node("Level 1"));
        this.getRootNode().attachChild(this.getLocalRootNode());

        this.setAudioNode(new AudioNode(this.getAssetManager(), "Sounds/Effects/Outdoor_Ambiance.ogg", false));
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getLocalRootNode().attachChild(this.getAudioNode());
        this.getAudioNode().play();// play continuously!

        /**
         * Set up Physics
         */
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        this.getApp().getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        this.getApp().getFlyByCamera().setMoveSpeed(100);

        // We load the scene from the zip file and adjust its size.
        //assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = this.getApp().getAssetManager().loadModel("Scenes/Level1.j3o");
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
        bulletAppState.getPhysicsSpace().add(this.getPlayer().getControl());
        bulletAppState.getPhysicsSpace().add(this.getControl());
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

}
