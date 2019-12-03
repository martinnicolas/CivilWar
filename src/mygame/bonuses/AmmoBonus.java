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
    public static final float LOCAL_SCALE = 0.5f;
    public static final float MASS = 2f;

    public AmmoBonus(AssetManager assetManager, int amount, Vector3f localTranslation) {
        super(assetManager, amount, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup ammo spatial properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(AmmoBonus.MODEL_PATH));
        this.getSpatial().setName(AmmoBonus.SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(AmmoBonus.LOCAL_SCALE);
        this.getSpatial().addControl(new RigidBodyControl(AmmoBonus.MASS));
        this.getSpatial().addControl(new BonusControl(this.getAmount()));
    }
    
}
