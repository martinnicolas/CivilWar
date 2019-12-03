/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.bonuses;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import mygame.controls.BonusControl;

/**
 *
 * @author martin
 */
public class HealthBonus extends Bonus {
    
    public static final String MODEL_PATH = "Models/MedKit_mk2/MedKit_mk2.j3o";
    public static final String SPATIAL_NAME = "health";
    public static final float LOCAL_SCALE = 1f;
    public static final float MASS = 2f;

    public HealthBonus(AssetManager assetManager, int amount, Vector3f localTranslation) {
        super(assetManager, amount, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup health spatial properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(HealthBonus.MODEL_PATH));
        this.getSpatial().setName(HealthBonus.SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(HealthBonus.LOCAL_SCALE);
        this.getSpatial().addControl(new RigidBodyControl(HealthBonus.MASS));
        this.getSpatial().addControl(new BonusControl(this.getAmount()));
    }
    
}
