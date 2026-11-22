package com.mygame.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.mygame.effects.ExplosionSequence;
import com.mygame.levels.Level;
import java.util.List;
import com.jme3.math.Vector3f;

/** Starts and updates the level's explosion sequence when its timer expires. */
public class ExplosionSequenceControl extends AbstractControl {

    private final Level level;
    private final List<Vector3f> explosionPositions;
    private ExplosionSequence sequence;

    public ExplosionSequenceControl(Level level, List<Vector3f> explosionPositions) {
        this.level = level;
        this.explosionPositions = explosionPositions;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (sequence == null) {
            if (level.getPlayer() == null || level.getPlayer().getControl().enoughRemainingTime()) {
                return;
            }
            sequence = new ExplosionSequence(level, explosionPositions);
        }

        sequence.update(tpf);
        if (sequence.isFinished()) {
            setEnabled(false);
            level.gameOver();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return new ExplosionSequenceControl(level, explosionPositions);
    }
}
