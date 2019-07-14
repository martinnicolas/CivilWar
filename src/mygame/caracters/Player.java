/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.caracters;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import mygame.Main;

/**
 *
 * @author martin
 */
public class Player implements ActionListener {
    
    private Vector3f walkDirection = new Vector3f();
    private Main app;
    private CharacterControl control;
    boolean left = false, right = false, up = false, down = false;
    
    public Player(Main app){
        this.app = app;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Left")) {
            this.setLeft(isPressed);
        } else if (name.equals("Right")) {
            this.setRight(isPressed);
        } else if (name.equals("Up")) {
            this.setUp(isPressed);
        } else if (name.equals("Down")) {
            this.setDown(isPressed);
        } else if (name.equals("Jump")) {
            if (isPressed) { this.getControl().jump(); }
        }
    }
    
    /**
     * Init player properties
     */
    public void initialize(){
        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        this.setControl(new CharacterControl(capsuleShape, 0.05f));
        this.getControl().setJumpSpeed(20);
        this.getControl().setFallSpeed(30);
        this.getControl().setGravity(30);
        this.getControl().setPhysicsLocation(new Vector3f(10, 20, 10));        
        this.getWalkDirection().set(0, 0, 0);
        this.setUpKeys();
    }
        
    private void setUpKeys() {
        this.getApp().getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.getApp().getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.getApp().getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        this.getApp().getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        this.getApp().getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        this.getApp().getInputManager().addListener(this, "Left");
        this.getApp().getInputManager().addListener(this, "Left_arrow");
        this.getApp().getInputManager().addListener(this, "Right");
        this.getApp().getInputManager().addListener(this, "Up");
        this.getApp().getInputManager().addListener(this, "Down");
        this.getApp().getInputManager().addListener(this, "Jump");
    }
    
    public Main getApp() {
        return app;
    }

    public void setApp(Main app) {
        this.app = app;
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
    
}
