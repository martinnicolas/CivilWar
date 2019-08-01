/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.characters;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import mygame.Main;
import mygame.states.Level;

/**
 *
 * @author martin
 */
public class Player implements ActionListener {

    private Vector3f walkDirection = new Vector3f();
    private Main app;
    private Level level;
    private Node rootNode;
    private AssetManager assetManager;
    private CharacterControl control;
    private AudioNode jumpAudio, walkAudio, shootAudio, emptyGunAudio;
    private boolean left = false, right = false, up = false, down = false;
    //Player settings for the game
    private static final int MAX_AMMOS = 100;
    private static final int MAX_HEALTH = 100;
    private int ammoes = MAX_AMMOS;
    private int health = MAX_HEALTH;
    //Variables for HUD texts
    private BitmapText ammoesText;
    private BitmapText healthText;

    public Player(Level level) {
        this.setLevel(level);
        this.setApp(level.getApp());
        this.setRootNode(level.getRootNode());
        this.setAssetManager(level.getAssetManager());
        this.setUpProperties();
        this.setUpAudio();
        this.setUpKeys();
    }

    /**
     * Settup player properties
     */
    private void setUpProperties() {
        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        this.setControl(new CharacterControl(capsuleShape, 0.05f));
        this.getControl().setJumpSpeed(20);
        this.getControl().setFallSpeed(30);
        this.getControl().setGravity(30);
        this.getWalkDirection().set(0, 0, 0);
        this.initCrossHairs();
        this.initHUD();
    }

    /**
     * Settup crosshairs for aim
     */
    protected void initCrossHairs() {
        BitmapFont guiFont = this.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText crossHairs = new BitmapText(guiFont, false);
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
     * Init HUD
     */
    private void initHUD() {
        //Write text for ammoes
        BitmapFont guiFont = this.getApp().getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        this.setAmmoesText(new BitmapText(guiFont, false));
        this.getAmmoesText().setSize(guiFont.getCharSet().getRenderedSize());
        this.getAmmoesText().setText("Ammo:     "+Integer.toString(MAX_AMMOS));
        this.getAmmoesText().setLocalTranslation(this.getApp().getCamera().getWidth() - 150, 750, 0);
        this.getApp().getGuiNode().attachChild(this.getAmmoesText());
        //Write text for health
        this.setHealthText(new BitmapText(guiFont, false));
        this.getHealthText().setSize(guiFont.getCharSet().getRenderedSize());
        this.getHealthText().setText("Health:     "+Integer.toString(MAX_HEALTH));
        this.getHealthText().setLocalTranslation(this.getApp().getCamera().getWidth() - 150, 725, 0);
        this.getApp().getGuiNode().attachChild(this.getHealthText());
    }
    
    /**
     * Update HUD
     */
    private void updateHUD() {
        //Update ammoes text
        if (!this.haveEnoughAmmoes())
            this.getAmmoesText().setColor(ColorRGBA.Red);
        this.getAmmoesText().setText("Ammo:     "+Integer.toString(this.getAmmoes()));
        //Update health text
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
        this.getRootNode().attachChild(this.getJumpAudio());
        //Attach audio for walk action
        this.setWalkAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/sfx_step_grass_l.ogg", DataType.Buffer));
        this.getWalkAudio().setPositional(false);
        this.getWalkAudio().setLooping(true);
        this.getWalkAudio().setVolume(2);
        this.getRootNode().attachChild(this.getWalkAudio());
        //Attach audio for shoot action
        this.setShootAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/shots/pistol.wav", DataType.Buffer));
        this.getShootAudio().setPositional(false);
        this.getShootAudio().setLooping(false);
        this.getShootAudio().setVolume(2);
        this.getRootNode().attachChild(this.getShootAudio());
        //Attach audio for empty gun
        this.setEmptyGunAudio(new AudioNode(this.getAssetManager(), "Sounds/Effects/shots/Gun_Cock.wav", DataType.Buffer));
        this.getEmptyGunAudio().setPositional(false);
        this.getEmptyGunAudio().setLooping(false);
        this.getEmptyGunAudio().setVolume(2);
        this.getRootNode().attachChild(this.getEmptyGunAudio());
    }

    /**
     * Settup keys
     */
    private void setUpKeys() {
        this.getApp().getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.getApp().getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.getApp().getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        this.getApp().getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        this.getApp().getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        this.getApp().getInputManager().addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        this.getApp().getInputManager().addMapping("Shoot", new KeyTrigger(KeyInput.KEY_F));
        this.getApp().getInputManager().addListener(this, "Left");
        this.getApp().getInputManager().addListener(this, "Right");
        this.getApp().getInputManager().addListener(this, "Up");
        this.getApp().getInputManager().addListener(this, "Down");
        this.getApp().getInputManager().addListener(this, "Jump");
        this.getApp().getInputManager().addListener(this, "Pause");
        this.getApp().getInputManager().addListener(this, "Shoot");
    }

    /**
     * Set player initial location.
     *
     * @param initialLocation Vector3f for players location
     */
    public void setInitialLocation(Vector3f initialLocation) {
        this.getControl().setPhysicsLocation(initialLocation);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!name.equals("Pause")) {
            if (this.getLevel().isEnabled()) {
                switch (name) {
                    case "Left":
                        this.setLeft(isPressed);
                        break;
                    case "Right":
                        this.setRight(isPressed);
                        break;
                    case "Up":
                        this.setUp(isPressed);
                        break;
                    case "Down":
                        this.setDown(isPressed);
                        break;
                    case "Jump":
                        if (isPressed) {
                            this.getControl().jump();
                            this.getJumpAudio().playInstance();
                        }
                        break;
                    case "Shoot":                        
                        if (isPressed) {
                            if (this.haveEnoughAmmoes()) {
                                this.getShootAudio().playInstance();
                                this.discountAmmoes();
                                this.updateHUD();
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
            Sphere sphere = new Sphere(3, 3, 0.1f);
            Geometry shootsMark = new Geometry("shoots_mark", sphere);
            Material markMaterial = new Material(this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            markMaterial.setColor("Color", ColorRGBA.Black);
            shootsMark.setMaterial(markMaterial);
            shootsMark.setLocalTranslation(closest.getContactPoint());
            this.getLevel().getLocalRootNode().attachChild(shootsMark);
        } 
    }

    public BitmapText getAmmoesText() {
        return ammoesText;
    }

    public void setAmmoesText(BitmapText ammoesText) {
        this.ammoesText = ammoesText;
    }

    public BitmapText getHealthText() {
        return healthText;
    }

    public void setHealthText(BitmapText healthText) {
        this.healthText = healthText;
    }
    
    private boolean haveEnoughAmmoes() {
        return this.getAmmoes() > 0;
    }
    
    private void discountAmmoes() {
        this.setAmmoes(this.getAmmoes() - 1);
    }

    public int getAmmoes() {
        return ammoes;
    }

    public void setAmmoes(int ammoes) {
        this.ammoes = ammoes;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
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
    
    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public CharacterControl getControl() {
        return control;
    }

    public void setControl(CharacterControl control) {
        this.control = control;
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
