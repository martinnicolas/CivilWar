/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.enemies;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author martin
 */
public abstract class Enemy {

    private AssetManager assetManager;
    private Spatial spatial;
    private int energy;
    private int damage;
    
    public Enemy(AssetManager assetManager, int energy, int damage) {
        this.setEnergy(energy);
        this.setDamage(damage);
        this.setAssetManager(assetManager);
    }
    
    public abstract void setUpProperties();
    
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

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    
}
