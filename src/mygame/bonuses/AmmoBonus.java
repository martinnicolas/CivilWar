/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.bonuses;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import mygame.controls.AmmoControl;

/**
 *
 * @author martin
 */
public class AmmoBonus extends Bonus { 
    
    public static final String MODEL_PATH = "Models/barrel01/barrel01.j3o";
    public static final String SPATIAL_NAME = "ammo";

    public AmmoBonus(AssetManager assetManager, int amount) {
        super(assetManager, amount);
    }

    @Override
    public void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(MODEL_PATH));
        this.getSpatial().setName(SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(500, 0, 700);
        this.getSpatial().setLocalScale(3f);
        this.getSpatial().addControl(new RigidBodyControl(2f));
        this.getSpatial().addControl(new AmmoControl());
    }
    
}
