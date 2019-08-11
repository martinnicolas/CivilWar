/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author martin
 */
public class TerrainTrackControl extends AbstractControl {
    
    private final Ray ray = new Ray(Vector3f.ZERO.clone(), new Vector3f(0, -1, 0));
    private final Vector3f up = new Vector3f(0, 50, 0);
    private final CollisionResults results = new CollisionResults();
    private final float offset = 0.5f;
    private Spatial terrain;

    @Override
    protected void controlUpdate(float tpf) {
        terrain = this.getSpatial().getParent();
        if (terrain != null) {
            ray.setOrigin(this.getSpatial().getWorldTranslation().add(up));
            ray.setLimit(100);
            results.clear();
            terrain.collideWith(ray, results);
            for (CollisionResult collisionResult : results) {
                if (this.isTerrain(collisionResult.getGeometry())) {
                    Vector3f location = collisionResult.getContactPoint();
                    this.getSpatial().setLocalTranslation(this.getSpatial().getLocalTranslation().setY(location.getY() + offset));
                    return;
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    private boolean isTerrain(Spatial spatial) {
        while (true) {
            if (spatial == null) {
                return false;
            } else if ("Scene".equals(spatial.getName())) {
                return true;
            }
            spatial = spatial.getParent();
        }
    }
    
}
