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
public class AmmoBonus extends Bonus { 
    
    public static final String MODEL_PATH = "Models/ArmyBoxComplete/ArmyBoxComplete.j3o";
    public static final String SPATIAL_NAME = "ammo";

    public AmmoBonus(AssetManager assetManager, int amount, Vector3f localTranslation) {
        super(assetManager, amount, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup ammo spatial properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(MODEL_PATH));
        this.getSpatial().setName(SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(0.5f);
        this.getSpatial().addControl(new RigidBodyControl(2f));
        this.getSpatial().addControl(new BonusControl(this.getAmount()));
    }
    
}
