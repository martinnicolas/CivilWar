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
import java.util.Iterator;

/**
 *
 * @author martin
 */
public class TerrainTrackControl extends AbstractControl {
    
    private final Ray ray = new Ray(Vector3f.ZERO.clone(), new Vector3f(0,-1,0));
    private final Vector3f up = new Vector3f(0, 50 , 0);
    private final CollisionResults results = new CollisionResults();
    private final float offset = 0.1745207f;
    private Spatial terrain;
    
    public TerrainTrackControl() {
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        terrain = spatial.getParent();
        if (terrain != null) {
            ray.setOrigin(spatial.getWorldTranslation().add(up));
            ray.setLimit(100);
            results.clear();
            terrain.collideWith(ray, results);
            for (Iterator<CollisionResult> it = results.iterator();it.hasNext();) {
                CollisionResult collisionResult = it.next();
                if (isTerrain(collisionResult.getGeometry())) {
                    Vector3f loc = collisionResult.getContactPoint();
                    spatial.setLocalTranslation(spatial.getLocalTranslation().setY(loc.getY() - offset));
                    return;
                }
            }
        }
    }
    
    private boolean isTerrain(Spatial spat) {
        while(true) {
            if (spat == null) {
                return false;
            } else if (spat.getName().contains("terrain")) {
                return true;
            }
            spat = spat.getParent();
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
}
