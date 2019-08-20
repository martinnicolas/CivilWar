/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.enemies;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import mygame.controls.EnemyControl;
import mygame.controls.TerrainTrackControl;

/**
 *
 * @author martin
 */
public class SoldierEnemy extends Enemy {
    
    public final String MODEL_PATH = "Models/D05150293DS/D0515029.j3o";
    public static final String SPATIAL_NAME = "enemy";

    public SoldierEnemy(AssetManager assetManager, int energy, int damage) {
        super(assetManager, energy, damage);
    }

    @Override
    public void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(MODEL_PATH));
        this.getSpatial().setName(SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(-28, 0, -26);
        this.getSpatial().setLocalScale(0.5f);
        this.getSpatial().addControl(new RigidBodyControl(0));
        //this.getSpatial().addControl(new EnemyControl());
        //this.getSpatial().addControl(new TerrainTrackControl());
    }
    
}
