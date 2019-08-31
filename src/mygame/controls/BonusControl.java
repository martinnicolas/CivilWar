/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author martin
 */
public class BonusControl extends AbstractControl {
    
    private int plusBonus;
    
    public BonusControl(int amount) {
        this.plusBonus = amount;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        this.getSpatial().rotate(0, tpf, 0);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }

    public int getPlusBonus() {
        return plusBonus;
    }

    public void setPlusBonus(int plusBonus) {
        this.plusBonus = plusBonus;
    }
    
}