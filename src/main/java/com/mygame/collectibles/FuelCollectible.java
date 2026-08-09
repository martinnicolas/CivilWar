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
public class FuelCollectible extends Collectible {
    
    public static final String MODEL_PATH = "Models/fuel/fuel_2.j3o";
    public static final String SPATIAL_NAME = "fuel_collectible";
    public static final float LOCAL_SCALE = 1f;
    public static final float MASS = 2f;
    public static final int DEFAULT_AMOUNT = 10;

    public FuelCollectible(AssetManager assetManager, Vector3f localTranslation) {
        super(assetManager, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup water collectible spatial properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(FuelCollectible.MODEL_PATH));
        this.getSpatial().setName(FuelCollectible.SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(FuelCollectible.LOCAL_SCALE);
        this.getSpatial().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.getSpatial().addControl(new RigidBodyControl(FuelCollectible.MASS));
        this.getSpatial().addControl(new CollectibleControl());
    }
    
}
