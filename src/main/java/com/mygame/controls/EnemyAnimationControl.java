/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.controls;

import com.jme3.anim.AnimComposer;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author martin
 */
public class EnemyAnimationControl extends AbstractControl {

    private AnimComposer composer;
    private boolean initialized = false;
    private boolean dead = false;

    @Override
    protected void controlUpdate(float tpf) {

        if (!initialized) {
            setUpAnimation();
            initialized = true;
        }
    }

    private void setUpAnimation() {

        composer = findAnimComposer(spatial);

        if (composer == null) {
            System.out.println("AnimComposer not found!");
            return;
        }

        attack();
    }

    public void attack() {

        if (dead || composer == null) {
            return;
        }

        composer.setCurrentAction("Run");
    }

    public void die() {

        if (!initialized) {
            setUpAnimation();
            initialized = true;
        }

        if (dead || composer == null) {
            return;
        }

        dead = true;

        composer.setCurrentAction("Die", AnimComposer.DEFAULT_LAYER, false);
    }

    private AnimComposer findAnimComposer(Spatial spatial) {

        AnimComposer composer = spatial.getControl(AnimComposer.class);

        if (composer != null) {
            return composer;
        }

        if (spatial instanceof Node node) {


            for (Spatial child : node.getChildren()) {

                composer = findAnimComposer(child);

                if (composer != null) {
                    return composer;
                }
            }
        }

        return null;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
