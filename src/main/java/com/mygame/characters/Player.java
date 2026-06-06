/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.characters;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.mygame.bonuses.AmmoBonus;
import com.mygame.bonuses.HealthBonus;
import com.mygame.controls.BonusControl;
import com.mygame.controls.EnemyControl;
import com.mygame.controls.PlayerControl;
import com.mygame.controls.PlayerHUDControl;
import com.mygame.levels.Level;
import com.mygame.weapons.AK47Weapon;

/**
 *
 * @author martin
 */
public class Player implements ActionListener {

    private Vector3f walkDirection = Vector3f.ZERO;
    private SimpleApplication app;
    private Level level;
    private Node playerNode;
    private AudioNode pickedAmmoAudio, pickedHealthAudio, jumpAudio, screamAudio, walkAudio, shootAudio, emptyWeaponAudio;
    private boolean left = false, right = false, up = false, down = false;
    private long lastDamageTime = 0L;
    public static final String SPATIAL_NAME = "player";
    private static final long DAMAGE_COOLDOWN_MS = 1000L;
    //Player capsule dimensions
    private static final float RADIUS = 1.5f;
    private static final float HEIGTH = 3.0f;
    private static final int AXIS = 1;
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
        this.setUpProperties();
        this.setUpPlayerWeapon();
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
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(Player.RADIUS, Player.HEIGTH, Player.AXIS);
        this.setPlayerNode(new Node(Player.SPATIAL_NAME));
        this.getPlayerNode().addControl(new GhostControl(capsuleShape));
        this.getPlayerNode().addControl(new PlayerControl(capsuleShape));
        // We re-use the flyby camera for rotation, while positioning is handled by physics
        this.getApp().getFlyByCamera().setEnabled(true);
        this.getApp().getFlyByCamera().setMoveSpeed(50);
        this.getWalkDirection().set(Vector3f.ZERO);
    }
    
    /**
     * Setup player weapon
     */
    private void setUpPlayerWeapon() {
        AK47Weapon ak47 = new AK47Weapon(this.getApp().getAssetManager(), new Vector3f(-1f, -1.1f, 2f));
        ak47.getSpatial().setLocalRotation(new Quaternion(0f, -1f, 0f, 1f));        
        //Create camera node for weapon spatial
        CameraNode cameraNode = new CameraNode("camera_node", this.getApp().getCamera());
        cameraNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        cameraNode.attachChild(ak47.getSpatial());
        this.getLevel().getLevelNode().attachChild(cameraNode);
    }

    /**
     * Settup audio
     */
    private void setUpAudio() {
        //Attach audio for jump action
        this.setJumpAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/jumppp11.ogg", DataType.Buffer));
        this.getJumpAudio().setName("player_jump_audio");
        this.getJumpAudio().setPositional(false);
        this.getJumpAudio().setLooping(false);
        this.getJumpAudio().setVolume(20);
        this.getApp().getRootNode().attachChild(this.getJumpAudio());
        //Attach audio for scream action
        this.setScreamAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/aww/aw02.ogg", DataType.Buffer));
        this.getScreamAudio().setName("player_jump_audio");
        this.getScreamAudio().setPositional(false);
        this.getScreamAudio().setLooping(false);
        this.getScreamAudio().setVolume(20);
        this.getApp().getRootNode().attachChild(this.getScreamAudio());
        //Attach audio for walk action
        this.setWalkAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/sfx_step_grass_l.ogg", DataType.Buffer));
        this.getWalkAudio().setName("player_walk_audio");
        this.getWalkAudio().setPositional(false);
        this.getWalkAudio().setLooping(true);
        this.getWalkAudio().setVolume(2);
        this.getApp().getRootNode().attachChild(this.getWalkAudio());
        //Attach audio for shoot action
        this.setShootAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/shots/pistol.wav", DataType.Buffer));
        this.getShootAudio().setName("player_shoot_audio");
        this.getShootAudio().setPositional(false);
        this.getShootAudio().setLooping(false);
        this.getShootAudio().setVolume(2);
        this.getApp().getRootNode().attachChild(this.getShootAudio());
        //Attach audio for empty weapon
        this.setEmptyWeaponAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/shots/Gun_Cock.wav", DataType.Buffer));
        this.getEmptyWeaponAudio().setName("player_empty_weapon_audio");
        this.getEmptyWeaponAudio().setPositional(false);
        this.getEmptyWeaponAudio().setLooping(false);
        this.getEmptyWeaponAudio().setVolume(2);
        this.getApp().getRootNode().attachChild(this.getEmptyWeaponAudio());
        //Attach audio for picked ammo action
        this.setPickedAmmoAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/bonus/picked_ammo.wav", DataType.Buffer));
        this.getPickedAmmoAudio().setName("player_picked_ammo_audio");
        this.getPickedAmmoAudio().setPositional(false);
        this.getPickedAmmoAudio().setLooping(false);
        this.getPickedAmmoAudio().setVolume(2);
        this.getApp().getRootNode().attachChild(this.getPickedAmmoAudio());
        //Attach audio for picked health action
        this.setPickedHealthAudio(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/bonus/picked_health.wav", DataType.Buffer));
        this.getPickedHealthAudio().setName("player_picked_health_audio");
        this.getPickedHealthAudio().setPositional(false);
        this.getPickedHealthAudio().setLooping(false);
        this.getPickedHealthAudio().setVolume(2);
        this.getApp().getRootNode().attachChild(this.getPickedHealthAudio());
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
        this.getControl().setWalkDirection(Vector3f.ZERO);
        this.getApp().getCamera().lookAt(this.getControl().getWalkDirection(), Vector3f.UNIT_Y);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!name.equals(Player.PAUSE_KEY)) {
            if (this.getLevel().isEnabled()) {
                switch (name) {
                    case Player.LEFT_KEY -> this.setLeft(isPressed);
                    case Player.RIGHT_KEY -> this.setRight(isPressed);
                    case Player.UP_KEY -> this.setUp(isPressed);
                    case Player.DOWN_KEY -> this.setDown(isPressed);
                    case Player.JUMP_KEY -> {
                        if (isPressed) {
                            this.getControl().jump();
                            this.getJumpAudio().playInstance();
                        }
                    }
                    case Player.SHOOT_KEY -> {
                        if (isPressed) {
                            if (this.getControl().haveEnoughAmmoes()) {
                                this.getShootAudio().playInstance();
                                this.getControl().discountAmmoes();
                                this.checkForShootingCollisions();
                            } else {
                                this.getEmptyWeaponAudio().playInstance();
                            }
                        }
                    }
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
     * Plus picked bonus. Receive a picked Spatial Bonus and plus its amount to the stored
     * 
     * @param bonusSpatial 
     */
    public void plusPickedBonus(Spatial bonusSpatial) {
        BonusControl bonusControl = bonusSpatial.getControl(BonusControl.class);
        switch (bonusSpatial.getName()) {
            case AmmoBonus.SPATIAL_NAME -> {
                this.getControl().plusAmmoes(bonusControl.getBonusAmount());
                this.getPickedAmmoAudio().playInstance();
            }
            case HealthBonus.SPATIAL_NAME -> {
                this.getControl().plusHealth(bonusControl.getBonusAmount());
                this.getPickedHealthAudio().playInstance();
            }
        }
    }
    
    /**
     * Load player damage
     * @param enemySpatial
     */
    public void loadDamage(Spatial enemySpatial) {
        long now = System.currentTimeMillis();
        
        if (now - this.getLastDamageTime() <= Player.DAMAGE_COOLDOWN_MS) { return; }

        if (this.getControl().haveEnoughHealth()) {
            EnemyControl enemyControl = enemySpatial.getControl(EnemyControl.class);
            this.setLastDamageTime(now);
            this.getControl().discountHealth(enemyControl.getDamage());
            this.getScreamAudio().playInstance();
            this.getPlayerNode().getControl(PlayerHUDControl.class).showDamageFlash();
        }
    }  

    /**
     * Checks for shooting collisions
     */
    private void checkForShootingCollisions() {
        Ray ray = new Ray(this.getApp().getCamera().getLocation(), this.getApp().getCamera().getDirection());
        this.getLevel().handleShootingCollision(ray);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public SimpleApplication getApp() {
        return app;
    }

    public void setApp(SimpleApplication app) {
        this.app = app;
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

    public long getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }
    
    public AudioNode getScreamAudio() {
        return screamAudio;
    }

    public void setScreamAudio(AudioNode jumpAudio) {
        this.screamAudio = jumpAudio;
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

    public AudioNode getEmptyWeaponAudio() {
        return emptyWeaponAudio;
    }

    public void setEmptyWeaponAudio(AudioNode emptyWeaponAudio) {
        this.emptyWeaponAudio = emptyWeaponAudio;
    }

}
