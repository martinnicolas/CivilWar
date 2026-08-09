/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.effects;

import com.jme3.math.Vector3f;
import com.mygame.levels.Level;
import java.util.List;

/**
 *
 * @author martin
 */
public class ExplosionSequence {

    private static final float EXPLOSION_INTERVAL = 1f;
    private static final float GAME_OVER_DELAY = 2.0f;

    private final Level level;
    private final List<Vector3f> explosionPositions;

    private float explosionTimer;
    private float gameOverTimer;
    private int nextExplosion;
    private boolean explosionsFinished;

    public ExplosionSequence(Level level, List<Vector3f> explosionPositions) {
        this.level = level;
        this.explosionPositions = explosionPositions;
    }

    public void update(float tpf) {

        if (!explosionsFinished) {

            explosionTimer += tpf;

            if (explosionTimer >= EXPLOSION_INTERVAL) {

                explosionTimer = 0f;

                ExplosionFactory.create(level, explosionPositions.get(nextExplosion));

                nextExplosion++;

                if (nextExplosion >= explosionPositions.size()) {
                    explosionsFinished = true;
                }
            }

            return;
        }

        gameOverTimer += tpf;
    }

    public boolean isFinished() {
        return explosionsFinished && gameOverTimer >= GAME_OVER_DELAY;
    }
}
