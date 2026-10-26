/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.effects;

import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.mygame.levels.Level;

/**
 *
 * @author martin
 */
public class FireFactory {

    public static void create(Level level, Vector3f explosionPosition) {
        Vector3f firePosition = findGroundPosition(level, explosionPosition);

        for (int i = 0; i < 8; i++) {
            float angle = FastMath.TWO_PI * i / 8f;
            float radius = 2.0f;

            float x = FastMath.cos(angle) * radius;
            float z = FastMath.sin(angle) * radius;

            Vector3f position = firePosition.add(x, 0, z);

            createFireEmitter(level, position);
        }
    }
    
    private static void createFireEmitter(Level level, Vector3f firePosition) {
        if (firePosition == null) { return; }
        
        ParticleEmitter fire = new ParticleEmitter("Fire", Type.Triangle, 100);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/flame.png"));
        fire.setMaterial(material);
        fire.setImagesX(2);
        fire.setImagesY(2);
        fire.setStartColor(new ColorRGBA(1f, 0.8f, 0.1f, 1f));
        fire.setEndColor(new ColorRGBA(1f, 0.1f, 0.0f, 0f));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.3f);
        fire.setGravity(0, 0.5f, 0);
        fire.setLowLife(0.8f);
        fire.setHighLife(1.5f);
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0.5f, 2f, 0.5f));
        fire.getParticleInfluencer().setVelocityVariation(0.8f);
        fire.setParticlesPerSec(15);
        fire.setLocalTranslation(firePosition);
        level.getApp().getRootNode().attachChild(fire);
    }
    
    private static Vector3f findGroundPosition(Level level, Vector3f position) {
        Ray ray = new Ray(position.add(0, 100, 0), Vector3f.UNIT_Y.negate());

        CollisionResults results = new CollisionResults();

        level.getApp().getRootNode().collideWith(ray, results);

        if (results.size() == 0) { return null; }

        return results.getClosestCollision().getContactPoint().clone();
    }
}
