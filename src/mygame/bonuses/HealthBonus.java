/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.bonuses;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import mygame.controls.BonusControl;

/**
 *
 * @author martin
 */
public class HealthBonus extends Bonus {
    
    public final String MODEL_PATH = "Models/MedKit_mk2/MedKit_mk2.j3o";
    public static final String SPATIAL_NAME = "health";

    public HealthBonus(AssetManager assetManager, int amount) {
        super(assetManager, amount);
        this.setUpProperties();
    }
    
    /**
     * Setup health properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(MODEL_PATH));
        this.getSpatial().setName(SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(-29, 10, 27);
        this.getSpatial().setLocalScale(1f);
        this.getSpatial().addControl(new RigidBodyControl(2f));        
        this.getSpatial().addControl(new BonusControl(this.getAmount()));
    }
    
}
