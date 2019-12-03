/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.weapons;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author martin
 */
public abstract class Weapon {
    
    private AssetManager assetManager;
    private Spatial spatial;
    private Vector3f localTranslation;
    
    public Weapon(AssetManager assetManager, Vector3f localTranslation) {
        this.assetManager = assetManager;
        this.localTranslation = localTranslation;
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

    public Vector3f getLocalTranslation() {
        return localTranslation;
    }

    public void setLocalTranslation(Vector3f localTranslation) {
        this.localTranslation = localTranslation;
    }
    
}
