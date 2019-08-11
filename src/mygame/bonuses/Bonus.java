/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.bonuses;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author martin
 */
public abstract class Bonus {
    
    private AssetManager assetManager;
    private Spatial spatial;
    private int amount;
    
    public Bonus(AssetManager assetManager, int amount) {
        this.setAmount(amount);
        this.setAssetManager(assetManager);
    }
    
    public abstract void setUpProperties();

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }
    
}
