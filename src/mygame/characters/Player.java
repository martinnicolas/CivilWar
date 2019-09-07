/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Sphere;
import mygame.Main;
import mygame.controls.PlayerControl;
import mygame.controls.PlayerHUDControl;
import mygame.levels.Level;

/**
 *
 * @author martin
 */
public class Player implements ActionListener{

    private Vector3f walkDirection = new Vector3f();
    private Main app;
    private Level level;
    private Node localRootNode;
    private AssetManager assetManager;
    private Node playerNode;
    private AudioNode pickedAmmoAudio, pickedHealthAudio, jumpAudio, walkAudio, shootAudio, emptyGunAudio;
    private boolean left = false, right = false, up = false, down = false;
    public static final String SPATIAL_NAME = "player";
    //String constants for Action Listener keys
    private static final String LEFT_KEY = "Left";
    private static final String RIGHT_KEY = "Right";
    private static final String UP_KEY = "Up";
    private static final String DOWN_KEY = "Down";
    private static final String JUMP_KEY = "Jump";
    private static final String PAUSE_KEY = "Pause";
    private static final String SHOOT_KEY = "Shoot";

    public Player(Level level) {
        this.level = level;
        this.app = level.getApp();
        this.localRootNode = level.getLocalRootNode();
        this.assetManager = level.getAssetManager();
        this.setUpProperties();
        this.setUpPlayerGun();
        this.setUpCrossHairs();
        this.setUpAudio();
        this.setUpKeys();
    }

    /**
     * Setup player properties
     */
    private void setUpProperties() {
        // We set up collision detection for the player by creating
        // a capsule collision shape and a Player Control which extends 
        // CharacterControl.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 3.0f, 1);
        this.setPlayerNode(new Node(SPATIAL_NAME));
        this.getPlayerNode().addControl(new GhostControl(capsuleShape));
        this.getPlayerNode().addControl(new PlayerControl(capsuleShape));
        this.getPlayerNode().addControl(new PlayerHUDControl(this.getApp(), this));
        // We re-use the flyby camera for rotation, while positioning is handled by physics
        this.getApp().getFlyByCamera().setEnabled(true);
        this.getApp().getFlyByCamera().setMoveSpeed(50);
        this.getWalkDirection().set(0, 0, 0);
    }
    
    /**
     * Setup player gun
     */
    private void setUpPlayerGun() {
        Spatial spatial = this.getAssetManager().loadModel("Models/AK-47/AK-47.j3o");
        spatial.setName("AK47");
        spatial.setLocalScale(0.5f);
        spatial.setLocalTranslation(-1f,-1f, 2f);
        spatial.setLocalRotation(new Quaternion(0f, -1f, 0f, 1f));
        //Create camera node for gun spatial
        CameraNode camNode = new CameraNode("camera_node", this.getApp().getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        camNode.attachChild(spatial);
        this.getLocalRootNode().attachChild(camNode);
    }

    /**
     * Setup crosshairs for aim
     */
    public void setUpCrossHairs() {
        BitmapFont guiFont = this.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText crossHairs = new BitmapText(guiFont, false);
        crossHairs.setName("cross_hairs");
        crossHairs.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        crossHairs.setText("+"); // crosshairs
        crossHairs.setLocalTranslation( // center
                this.getApp().getCamera().getWidth() / 2 - crossHairs.getLineWidth() / 2,
                this.getApp().getCamera().getHeight() / 2 + crossHairs.getLineHeight() / 2,
                0
        );
        this.getApp().getGuiNode().attachChild(crossHairs);
    }
    
    /**
     * Remove crosshairs
     */
    public void removeCrossHairs() {
        this.getApp().getGuiNode().detachChildNamed("cross_hairs");
    }

    /**
     * Settup audio
     */
    private void setUpAudio() {
        //Attach audio for jump action
        this.setJumpAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/jumppp11.ogg", DataType.Buffer));
        this.getJumpAudio().setPositional(false);
        this.getJumpAudio().setLooping(false);
        this.getJumpAudio().setVolume(20);
        this.getLocalRootNode().attachChild(this.getJumpAudio());
        //Attach audio for walk action
        this.setWalkAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/sfx_step_grass_l.ogg", DataType.Buffer));
        this.getWalkAudio().setPositional(false);
        this.getWalkAudio().setLooping(true);
        this.getWalkAudio().setVolume(2);
        this.getLocalRootNode().attachChild(this.getWalkAudio());
        //Attach audio for shoot action
        this.setShootAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/shots/pistol.wav", DataType.Buffer));
        this.getShootAudio().setPositional(false);
        this.getShootAudio().setLooping(false);
        this.getShootAudio().setVolume(2);
        this.getLocalRootNode().attachChild(this.getShootAudio());
        //Attach audio for empty gun
        this.setEmptyGunAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/shots/Gun_Cock.wav", DataType.Buffer));
        this.getEmptyGunAudio().setPositional(false);
        this.getEmptyGunAudio().setLooping(false);
        this.getEmptyGunAudio().setVolume(2);
        this.getLocalRootNode().attachChild(this.getEmptyGunAudio());
        //Attach audio for picked ammo action
        this.setPickedAmmoAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/bonus/picked_ammo.wav", DataType.Buffer));
        this.getPickedAmmoAudio().setPositional(false);
        this.getPickedAmmoAudio().setLooping(false);
        this.getPickedAmmoAudio().setVolume(2);
        this.getLocalRootNode().attachChild(this.getPickedAmmoAudio());
        //Attach audio for picked health action
        this.setPickedHealthAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/bonus/picked_health.wav", DataType.Buffer));
        this.getPickedHealthAudio().setPositional(false);
        this.getPickedHealthAudio().setLooping(false);
        this.getPickedHealthAudio().setVolume(2);
        this.getLocalRootNode().attachChild(this.getPickedHealthAudio());
    }

    /**
     * Settup keys
     */
    private void setUpKeys() {
        this.getApp().getInputManager().addMapping(Player.LEFT_KEY, new KeyTrigger(KeyInput.KEY_A));
        this.getApp().getInputManager().addMapping(Player.RIGHT_KEY, new KeyTrigger(KeyInput.KEY_D));
        this.getApp().getInputManager().addMapping(Player.UP_KEY, new KeyTrigger(KeyInput.KEY_W));
        this.getApp().getInputManager().addMapping(Player.DOWN_KEY, new KeyTrigger(KeyInput.KEY_S));
        this.getApp().getInputManager().addMapping(Player.JUMP_KEY, new KeyTrigger(KeyInput.KEY_SPACE));
        this.getApp().getInputManager().addMapping(Player.PAUSE_KEY, new KeyTrigger(KeyInput.KEY_P));
        this.getApp().getInputManager().addMapping(Player.SHOOT_KEY, new KeyTrigger(KeyInput.KEY_F));
        this.getApp().getInputManager().addListener(this, Player.LEFT_KEY);
        this.getApp().getInputManager().addListener(this, Player.RIGHT_KEY);
        this.getApp().getInputManager().addListener(this, Player.UP_KEY);
        this.getApp().getInputManager().addListener(this, Player.DOWN_KEY);
        this.getApp().getInputManager().addListener(this, Player.JUMP_KEY);
        this.getApp().getInputManager().addListener(this, Player.PAUSE_KEY);
        this.getApp().getInputManager().addListener(this, Player.SHOOT_KEY);
    }
    
    /**
     * Set initial location for player. Also sets camera looking to the center of the scene
     *
     * @param initialLocation Vector3f for players location
     */
    public void setInitialLocation(Vector3f initialLocation) {
        this.getControl().setPhysicsLocation(initialLocation);
        this.getApp().getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!name.equals(Player.PAUSE_KEY)) {
            if (this.getLevel().isEnabled()) {
                switch (name) {
                    case Player.LEFT_KEY:
                        this.setLeft(isPressed);
                        break;
                    case Player.RIGHT_KEY:
                        this.setRight(isPressed);
                        break;
                    case Player.UP_KEY:
                        this.setUp(isPressed);
                        break;
                    case Player.DOWN_KEY:
                        this.setDown(isPressed);
                        break;
                    case Player.JUMP_KEY:
                        if (isPressed) {
                            this.getControl().jump();
                            this.getJumpAudio().playInstance();
                        }
                        break;
                    case Player.SHOOT_KEY:
                        if (isPressed) {
                            if (this.getControl().haveEnoughAmmoes()) {
                                this.getShootAudio().playInstance();
                                this.getControl().discountAmmoes();
                                this.checkForShootingCollisions();
                            } else {
                                this.getEmptyGunAudio().playInstance();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            if (isPressed && this.getLevel().isEnabled()) {
                this.getLevel().pause();
            } else if (isPressed && !this.getLevel().isEnabled()) {
                this.getLevel().resume();
            }
        }
    }

    /**
     * Checks for shooting collisions
     */
    private void checkForShootingCollisions() {
        CollisionResults results = new CollisionResults();
        // Aim the ray from cam loc to cam direction
        Ray ray = new Ray(this.getApp().getCamera().getLocation(), this.getApp().getCamera().getDirection());
        // Collect intersections between Ray and Shootables in results list.
        // DO NOT check collision with the root node, or else ALL collisions will hit the
        // skybox! Always make a separate node for objects you want to collide with.
        this.getLevel().getLocalRootNode().collideWith(ray, results);
        if (results.size() > 0) {
            // The closest collision point is what was truly hit:
            CollisionResult closest = results.getClosestCollision();
            // Let's interact - we mark the hit
            Sphere sphere = new Sphere(10, 10, 0.02f);
            Geometry shootsMark = new Geometry("shoots_mark", sphere);
            Material markMaterial = new Material(this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            markMaterial.setColor("Color", ColorRGBA.Black);
            shootsMark.setMaterial(markMaterial);
            shootsMark.setLocalTranslation(closest.getContactPoint());
            shootsMark.addControl(new RigidBodyControl(1f));            
            this.getLevel().getLocalRootNode().attachChild(shootsMark);
            this.getLevel().getStateManager().getState(BulletAppState.class).getPhysicsSpace().addAll(shootsMark);
        }
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Main getApp() {
        return app;
    }

    public void setApp(Main app) {
        this.app = app;
    }

    public Node getLocalRootNode() {
        return localRootNode;
    }

    public void setLocalRootNode(Node localRootNode) {
        this.localRootNode = localRootNode;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Node getPlayerNode() {
        return playerNode;
    }

    public void setPlayerNode(Node playerNode) {
        this.playerNode = playerNode;
    }

    public PlayerControl getControl() {
        return this.getPlayerNode().getControl(PlayerControl.class);
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public AudioNode getJumpAudio() {
        return jumpAudio;
    }

    public void setJumpAudio(AudioNode jumpAudio) {
        this.jumpAudio = jumpAudio;
    }
    
    public AudioNode getPickedAmmoAudio() {
        return pickedAmmoAudio;
    }

    public void setPickedAmmoAudio(AudioNode pickedAmmoAudio) {
        this.pickedAmmoAudio = pickedAmmoAudio;
    }
    
    public AudioNode getPickedHealthAudio() {
        return pickedHealthAudio;
    }

    public void setPickedHealthAudio(AudioNode pickedHealthAudio) {
        this.pickedHealthAudio = pickedHealthAudio;
    }

    public AudioNode getWalkAudio() {
        return walkAudio;
    }

    public void setWalkAudio(AudioNode walkAudio) {
        this.walkAudio = walkAudio;
    }

    public AudioNode getShootAudio() {
        return shootAudio;
    }

    public void setShootAudio(AudioNode shootAudio) {
        this.shootAudio = shootAudio;
    }

    public AudioNode getEmptyGunAudio() {
        return emptyGunAudio;
    }

    public void setEmptyGunAudio(AudioNode emptyGunAudio) {
        this.emptyGunAudio = emptyGunAudio;
    }

}
