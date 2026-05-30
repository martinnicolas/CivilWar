/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.enemies;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.scene.Spatial;
import com.mygame.controls.EnemyAnimationControl;
import com.mygame.controls.EnemyControl;

/**
 *
 * @author martin
 */
public abstract class Enemy {

    private AssetManager assetManager;
    private Spatial spatial;
    private float energy;
    private float damage;
    
    public Enemy(AssetManager assetManager, float energy, float damage) {
        this.energy = energy;
        this.damage = damage;
        this.assetManager = assetManager;
    }
    
    /**
    * Kill this enemy and leave its spatial in the scene as an inert body.
    * @param physicsSpace physics space where the enemy controls are registered
    */
    public void kill(PhysicsSpace physicsSpace) {
        if (!this.isAlive()) {
            return;
        }

        EnemyAnimationControl animationControl = this.getSpatial().getControl(EnemyAnimationControl.class);
        if (animationControl != null) {
            animationControl.die();
        }

        this.getSpatial().removeControl(EnemyControl.class);

        PhysicsControl physicsControl = this.getSpatial().getControl(PhysicsControl.class);
        if (physicsControl != null) {
            physicsControl.setEnabled(false);
            if (physicsSpace != null) {
                physicsSpace.remove(physicsControl);
            }
            this.getSpatial().removeControl(physicsControl);
        }
    }

    public boolean isAlive() {
        return this.getSpatial() != null && this.getSpatial().getControl(EnemyControl.class) != null;
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }
    
    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }   
}
