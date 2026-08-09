/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.controls;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;

/**
 *
 * @author martin
 */
public class PlayerControl extends CharacterControl {
    
    //Player settings for the game    
    private static final int MAX_AMMOS = 100;
    private static final float MAX_HEALTH = 100;
    private int ammoes = MAX_AMMOS;
    private float health = MAX_HEALTH;
    private int waterCollectibles = 0;
    private int healthCollectibles = 0;
    private int fuelCollectibles = 0;
    private float remainingTime = 0; 
    //Settings for CharacterControl
    private static final float STEP_HEIGHT = 0.5f;
    private static final float JUMP_SPEED = 15;
    private static final float FALL_SPEED = 30;
    private static final float GRAVITY = 30;
    
    public PlayerControl(CapsuleCollisionShape capsuleShape) {
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        super(capsuleShape, STEP_HEIGHT);
        super.setJumpSpeed(JUMP_SPEED);
        super.setFallSpeed(FALL_SPEED);
        super.setGravity(GRAVITY);
    }
    
    public void plusAmmoes(int plusAmmoes) {
        this.setAmmoes(this.getAmmoes() + plusAmmoes);
    }

    public void plusHealth(int plusHealth) {
        this.setHealth(this.getHealth() + plusHealth);
    }
    
    public void plusWaterCollectible() {
        this.setWaterCollectibles(this.getWaterCollectibles() + 1);
    }

    public void plusHealthCollectible() {
        this.setHealthCollectibles(this.getHealthCollectibles() + 1);
    }
    
    public void plusFuelCollectible() {
        this.setFuelCollectibles(this.getFuelCollectibles() + 1);
    }
    
    public int amountOfCollectiblesCollected() {
        return this.getFuelCollectibles() + this.getWaterCollectibles() + this.getHealthCollectibles();
    }
    
    public boolean haveEnoughAmmoes() {
        return this.getAmmoes() > 0;
    }

    public void discountAmmoes() {
        this.setAmmoes(this.getAmmoes() - 1);
    }
    
    public boolean haveEnoughHealth() {
        return this.getHealth() > 0;
    }

    public void discountHealth(float discount) {
        this.setHealth(this.getHealth() - discount);
    }

    public int getAmmoes() {
        return ammoes;
    }

    public void setAmmoes(int ammoes) {
        this.ammoes = ammoes;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getWaterCollectibles() {
        return waterCollectibles;
    }

    public void setWaterCollectibles(int waterCollectibles) {
        this.waterCollectibles = waterCollectibles;
    }

    public int getHealthCollectibles() {
        return healthCollectibles;
    }

    public void setHealthCollectibles(int healthCollectibles) {
        this.healthCollectibles = healthCollectibles;
    }

    public int getFuelCollectibles() {
        return fuelCollectibles;
    }

    public void setFuelCollectibles(int fuelCollectibles) {
        this.fuelCollectibles = fuelCollectibles;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(float remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    public void decrementRemainingTime(float tpf) {
        this.setRemainingTime(this.getRemainingTime() - tpf);
    }
    
    public boolean enoughRemainingTime() {
        return this.getRemainingTime() > 0;
    }
}
