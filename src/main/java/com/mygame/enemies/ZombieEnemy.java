/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.enemies;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.mygame.controls.EnemyAnimationControl;
import com.mygame.controls.EnemyControl;
import com.mygame.controls.TerrainTrackControl;

/**
 *
 * @author martin
 */
public class ZombieEnemy extends Enemy {
    
    public static final String MODEL_PATH = "Models/Zombie/zombie3.j3o";
    public static final String SPATIAL_NAME = "enemy";
    public static final float DEFAULT_ENERGY = 100;
    public static final float DEFAULT_DAMAGE = 10;
    private static final float COLLISION_RADIUS = 1.0f;
    private static final float COLLISION_HEIGHT = 3.0f;
    public static final float LOCAL_SCALE = 2.0f;

    public ZombieEnemy(AssetManager assetManager, float energy, float damage, Vector3f localTranslation) {
        super(assetManager, energy, damage, localTranslation);
        this.setUpProperties();
    }
    
    /**
     * Setup soldier properties
     */
    private void setUpProperties() {
        this.setSpatial(this.getAssetManager().loadModel(ZombieEnemy.MODEL_PATH));
        this.getSpatial().setName(ZombieEnemy.SPATIAL_NAME);
        this.getSpatial().setLocalTranslation(this.getLocalTranslation());
        this.getSpatial().setLocalScale(LOCAL_SCALE);
        this.getSpatial().addControl(new GhostControl(new CapsuleCollisionShape(ZombieEnemy.COLLISION_RADIUS, ZombieEnemy.COLLISION_HEIGHT)));
        this.getSpatial().addControl(new EnemyControl(this.getEnergy(), this.getDamage()));
        this.getSpatial().addControl(new TerrainTrackControl());
        this.getSpatial().addControl(new EnemyAnimationControl());
    }    
    
}
