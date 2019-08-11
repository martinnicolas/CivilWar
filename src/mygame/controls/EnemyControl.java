/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import mygame.characters.Player;

/**
 *
 * @author martin
 */
public class EnemyControl extends AbstractControl {
    
    private final Quaternion lookRotation = new Quaternion();

    @Override
    protected void controlUpdate(float tpf) {
        Spatial playerSpatial = this.getSpatial().getParent().getChild(Player.SPATIAL_NAME);
        Vector3f aim = playerSpatial.getWorldTranslation();
        Vector3f dist = aim.subtract(this.getSpatial().getWorldTranslation());
        if (dist.length() >= 1) {
            dist.normalizeLocal();
            lookRotation.lookAt(dist, Vector3f.UNIT_Y);
            this.getSpatial().setLocalRotation(lookRotation);
            this.getSpatial().move(dist.multLocal(0.1f + tpf));
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
}
