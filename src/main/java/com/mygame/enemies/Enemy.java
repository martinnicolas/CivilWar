/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.enemies;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.mygame.controls.EnemyAnimationControl;

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
    * Kill the enemy by running die animation
    */
    public void kill() {
        if (!this.isAlive()) {
            return;
        }

        this.setEnergy(0);
        EnemyAnimationControl animationControl = this.getSpatial().getControl(EnemyAnimationControl.class);
        if (animationControl != null) {
            animationControl.die();
        }
    }

    public boolean isAlive() {
        return this.getEnergy() > 0;
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
