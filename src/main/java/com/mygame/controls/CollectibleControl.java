/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.controls;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author martin
 */
public class CollectibleControl extends AbstractControl {

    private static final float PULSE_SPEED = 1.4f;
    private static final float MIN_GLOW_INTENSITY = 0.16f;
    private static final float MAX_GLOW_INTENSITY = 0.32f;
    private float time = 0f;

    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        float pulse = (FastMath.sin(time * PULSE_SPEED) + 1f) / 2f;
        float glowIntensity = MIN_GLOW_INTENSITY + (pulse * (MAX_GLOW_INTENSITY - MIN_GLOW_INTENSITY));
        this.applyGlow(spatial, glowIntensity);
    }

    private void applyGlow(Spatial spatial, float intensity) {

        if (spatial instanceof Geometry geometry) {
            Material material = geometry.getMaterial();
            
            if (material.getMaterialDef().getMaterialParam("Emissive") != null) {
                material.setColor("Emissive", ColorRGBA.Orange.mult(intensity));
            }
        }

        if (spatial instanceof Node node) {
            for (Spatial child : node.getChildren()) {
                applyGlow(child, intensity);
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
