/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
public class Level1 extends AbstractAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private final Node localRootNode = new Node("Level 1");    
    private final InputManager inputManager;
    private AudioNode audioNode;
    private Main app;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    Spatial modelo;
    Player player;
    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    Vector3f camDir = new Vector3f();
    Vector3f camLeft = new Vector3f();
    
    public Level1(SimpleApplication app) {
        this.app = (Main)app;
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        rootNode.attachChild(localRootNode);
        
        this.audioNode = new AudioNode(assetManager, "Sounds/Effects/Outdoor_Ambiance.ogg", false);
        this.audioNode.setLooping(true);  // activate continuous playing
        this.audioNode.setPositional(false);   
        localRootNode.attachChild(audioNode);
        this.audioNode.play();// play continuously!
        
        /** Set up Physics */
        this.bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 
        // We re-use the flyby camera for rotation, while positioning is handled by physics
        this.app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        this.app.getFlyByCamera().setMoveSpeed(100);
        
        // We load the scene from the zip file and adjust its size.
        //assetManager.registerLocator("town.zip", ZipLocator.class);
        this.modelo = assetManager.loadModel("Scenes/Level1.j3o");
        this.modelo.setLocalScale(2f);
 
        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) this.modelo);
        landscape = new RigidBodyControl(sceneShape, 0);
        this.modelo.addControl(landscape);
 
        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        localRootNode.attachChild(this.modelo);
        this.player = new Player(this.app);
        this.player.initialize();
        bulletAppState.getPhysicsSpace().add(this.player.getControl());
        bulletAppState.getPhysicsSpace().add(landscape);
        
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "Pause");
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        camDir.set(this.app.getCam().getDirection()).multLocal(0.6f);
        camLeft.set(this.app.getCam().getLeft()).multLocal(0.4f);        
        this.player.getWalkDirection().set(0,0,0);
        if (this.player.isLeft()) {
            this.player.getWalkDirection().addLocal(camLeft);
        }
        if (this.player.isRight()) {
            this.player.getWalkDirection().addLocal(camLeft.negate());
        }
        if (this.player.isUp()) {
            this.player.getWalkDirection().addLocal(camDir);
        }
        if (this.player.isDown()) {
            this.player.getWalkDirection().addLocal(camDir.negate());
        }
        this.player.getControl().setWalkDirection(player.getWalkDirection());        
        this.app.getCam().setLocation(player.getControl().getPhysicsLocation());
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        rootNode.detachChild(localRootNode);
        super.cleanup();
    }
    
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("Pause") && !isPressed) {
                setEnabled(!isEnabled());
            }
        }
         
    };
    
}
