/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.collectibles;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.mygame.controls.CollectibleControl;

/**
 *
 * @author martin
 */
public class WaterCollectible extends Collectible {
    
    public static final String MODEL_PATH = "Models/water/metal_jerrycan_green.j3o";
    public static final String SPATIAL_NAME = "water_collectible";
    public static final float LOCAL_SCALE = 3f;
    public static final float MASS = 2f;
    public static final int DEFAULT_AMOUNT = 10;

    public WaterCollectible(AssetManager assetManager, Vector3f localTranslation) {
        super(assetManager, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup water collectible spatial properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(WaterCollectible.MODEL_PATH));
        this.getSpatial().setName(WaterCollectible.SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(WaterCollectible.LOCAL_SCALE);
        this.getSpatial().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.getSpatial().addControl(new RigidBodyControl(WaterCollectible.MASS));
        this.getSpatial().addControl(new CollectibleControl());
    }
    
}
