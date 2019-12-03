/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.weapons;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;

/**
 *
 * @author martin
 */
public class AK47Weapon extends Weapon {
    
    public static final String MODEL_PATH = "Models/AK-47/AK-47.j3o";
    public static final String SPATIAL_NAME = "AK47";
    public static final float LOCAL_SCALE = 0.5f;

    public AK47Weapon(AssetManager assetManager, Vector3f localTranslation) {
        super(assetManager, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup AK47 spatial properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(AK47Weapon.MODEL_PATH));
        this.getSpatial().setName(AK47Weapon.SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(AK47Weapon.LOCAL_SCALE);
        this.getSpatial().setShadowMode(RenderQueue.ShadowMode.Receive);
    }
    
}
