/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.mygame.characters.Player;

/**
 *
 * @author martin
 */
public class EnemyControl extends AbstractControl {
    
    private static final float ATTACK_DISTANCE = 2.2f;
    private float energy;
    private float damage;    
    private final Quaternion lookRotation = new Quaternion();
    
    public EnemyControl(float energy, float damage) {
        this.energy = energy;
        this.damage = damage;
    }

    @Override
    protected void controlUpdate(float tpf) {
        Spatial playerSpatial = this.getSpatial().getParent().getChild(Player.SPATIAL_NAME);
        Vector3f playerLocaltion = playerSpatial.getWorldTranslation();
        Vector3f distance = playerLocaltion.subtract(this.getSpatial().getWorldTranslation());
        if (distance.length() >= ATTACK_DISTANCE) {
            distance.normalizeLocal();
            lookRotation.lookAt(distance, Vector3f.UNIT_Y);
            this.getSpatial().setLocalRotation(lookRotation);
            this.getSpatial().move(distance.multLocal(0.1f + tpf));
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
    
}
