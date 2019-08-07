/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.collectibles;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;

/**
 *
 * @author martin
 */
public class Ammo {    
    
    private int bonus;
    
    public Ammo(int bonus) {
        this.setBonus(bonus);
        this.setUpProperties();
    }
    
    /**
     * Setup properties
     */
    private void setUpProperties() {
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
    
}
