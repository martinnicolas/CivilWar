/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.effects;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.mygame.levels.Level;

/**
 *
 * @author martin
 */
public class ExplosionFactory {
    private static final int COUNT_FACTOR = 1;
    private static final float COUNT_FACTOR_F = 1f;

    private static final boolean POINT_SPRITE = true;
    private static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;
    
    public static void create(Level level, Vector3f position) {
        Node explosionEffect = new Node("explosionFX");
        explosionEffect.setLocalScale(2f);
        explosionEffect.setLocalTranslation(position);
        ParticleEmitter flame = createFlame(level);
        explosionEffect.attachChild(flame);
        ParticleEmitter flash = createFlash(level);
        explosionEffect.attachChild(flash);
        ParticleEmitter spark = createSpark(level);
        explosionEffect.attachChild(spark);
        ParticleEmitter roundspark = createRoundSpark(level);
        explosionEffect.attachChild(roundspark);
        ParticleEmitter smoketrail = createSmokeTrail(level);
        explosionEffect.attachChild(smoketrail);
        ParticleEmitter debris = createDebris(level);
        explosionEffect.attachChild(debris);
        ParticleEmitter shockwave = createShockwave(level);
        explosionEffect.attachChild(shockwave);
        AudioNode explosionAudio = createAudio(level);
        explosionEffect.attachChild(explosionAudio);
        level.getApp().getRenderManager().preloadScene(explosionEffect);
        level.getApp().getRootNode().attachChild(explosionEffect);
        flash.emitAllParticles();       // explosión blanca inicial
        flame.emitAllParticles();       // bola fuego
        debris.emitAllParticles();      // restos
        spark.emitAllParticles();       // chispas
        smoketrail.emitAllParticles();  // humo
        roundspark.emitAllParticles();  // residuos
        shockwave.emitAllParticles();   // onda final
        explosionAudio.playInstance();
    }
    
    private static ParticleEmitter createFlame(Level level) {
        ParticleEmitter flame = new ParticleEmitter("Flame", EMITTER_TYPE, 32 * COUNT_FACTOR);
        flame.setSelectRandomImage(true);
        flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (1f / COUNT_FACTOR_F)));
        flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        flame.setStartSize(4f);
        flame.setEndSize(8f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0, -5, 0);
        flame.setLowLife(.6f);
        flame.setHighLife(.8f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
        flame.getParticleInfluencer().setVelocityVariation(1f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/flame.png"));
        material.setBoolean("PointSprite", POINT_SPRITE);
        flame.setMaterial(material);

        return flame;
    }

    private static ParticleEmitter createFlash(Level level) {
        ParticleEmitter flash = new ParticleEmitter("Flash", EMITTER_TYPE, 24 * COUNT_FACTOR);
        flash.setSelectRandomImage(true);
        flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f / COUNT_FACTOR_F));
        flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        flash.setStartSize(.1f);
        flash.setEndSize(3.0f);
        flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
        flash.setParticlesPerSec(0);
        flash.setGravity(0, 0, 0);
        flash.setLowLife(.2f);
        flash.setHighLife(.2f);
        flash.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
        flash.getParticleInfluencer().setVelocityVariation(1);
        flash.setImagesX(2);
        flash.setImagesY(2);
        Material mat = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/flash.png"));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        flash.setMaterial(mat);
        
        return flash;
    }

    private static ParticleEmitter createRoundSpark(Level level) {
        ParticleEmitter roundspark = new ParticleEmitter("RoundSpark", EMITTER_TYPE, 20 * COUNT_FACTOR);
        roundspark.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, (float) (1.0 / COUNT_FACTOR_F)));
        roundspark.setEndColor(new ColorRGBA(0, 0, 0, 0.5f / COUNT_FACTOR_F));
        roundspark.setStartSize(1.2f);
        roundspark.setEndSize(1.8f);
        roundspark.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
        roundspark.setParticlesPerSec(0);
        roundspark.setGravity(0, -.5f, 0);
        roundspark.setLowLife(1.8f);
        roundspark.setHighLife(2f);
        roundspark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
        roundspark.getParticleInfluencer().setVelocityVariation(.5f);
        roundspark.setImagesX(1);
        roundspark.setImagesY(1);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/roundspark.png"));
        material.setBoolean("PointSprite", POINT_SPRITE);
        roundspark.setMaterial(material);
        
        return roundspark;
    }

    private static ParticleEmitter createSpark(Level level) {
        ParticleEmitter spark = new ParticleEmitter("Spark", Type.Triangle, 30 * COUNT_FACTOR);
        spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1.0f / COUNT_FACTOR_F));
        spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        spark.setStartSize(.5f);
        spark.setEndSize(.5f);
        spark.setFacingVelocity(true);
        spark.setParticlesPerSec(0);
        spark.setGravity(0, 5, 0);
        spark.setLowLife(1.1f);
        spark.setHighLife(1.5f);
        spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
        spark.getParticleInfluencer().setVelocityVariation(1);
        spark.setImagesX(1);
        spark.setImagesY(1);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/spark.png"));
        spark.setMaterial(material);
        
        return spark;
    }

    private static ParticleEmitter createSmokeTrail(Level level) {
        ParticleEmitter smoketrail = new ParticleEmitter("SmokeTrail", Type.Triangle, 22 * COUNT_FACTOR);
        smoketrail.setStartColor(new ColorRGBA(0.15f, 0.15f, 0.15f, 0.8f));
        smoketrail.setEndColor(new ColorRGBA(0.05f, 0.05f, 0.05f, 0f));
        smoketrail.setStartSize(.2f);
        smoketrail.setEndSize(1f);

        // smoketrail.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        smoketrail.setFacingVelocity(true);
        smoketrail.setParticlesPerSec(0);
        smoketrail.setGravity(0, 1, 0);
        smoketrail.setLowLife(2f);
        smoketrail.setHighLife(3f);
        smoketrail.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 12, 0));
        smoketrail.getParticleInfluencer().setVelocityVariation(1);
        smoketrail.setImagesX(1);
        smoketrail.setImagesY(3);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/smoketrail.png"));
        smoketrail.setMaterial(material);

        return smoketrail;
    }

    private static ParticleEmitter createDebris(Level level) {
        ParticleEmitter debris = new ParticleEmitter("Debris", Type.Triangle, 15);
        debris.setSelectRandomImage(true);
        debris.setRandomAngle(true);
        debris.setRotateSpeed(FastMath.TWO_PI * 4);
        debris.setStartColor(new ColorRGBA(1f, 0.59f, 0.28f, 1.0f / COUNT_FACTOR_F));
        debris.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f, 0f));
        debris.setStartSize(.2f);
        debris.setEndSize(.2f);

        // debris.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
        debris.setParticlesPerSec(0);
        debris.setGravity(0, 12f, 0);
        debris.setLowLife(1.4f);
        debris.setHighLife(1.5f);
        debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 15, 0));
        debris.getParticleInfluencer().setVelocityVariation(.60f);
        debris.setImagesX(3);
        debris.setImagesY(3);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/Debris.png"));
        debris.setMaterial(material);
        
        return debris;
    }

    private static ParticleEmitter createShockwave(Level level) {
        ParticleEmitter shockwave = new ParticleEmitter("Shockwave", Type.Triangle, 1 * COUNT_FACTOR);
        shockwave.setFaceNormal(Vector3f.UNIT_Y);
        shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, .8f / COUNT_FACTOR_F));
        shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

        shockwave.setStartSize(0f);
        shockwave.setEndSize(12f);

        shockwave.setParticlesPerSec(0);
        shockwave.setGravity(0, 0, 0);
        shockwave.setLowLife(0.5f);
        shockwave.setHighLife(0.5f);
        shockwave.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        shockwave.getParticleInfluencer().setVelocityVariation(0f);
        shockwave.setImagesX(1);
        shockwave.setImagesY(1);
        Material material = new Material(level.getApp().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", level.getApp().getAssetManager().loadTexture("Effects/Explosion/shockwave.png"));
        shockwave.setMaterial(material);
        
        return shockwave;
    }
   
    private static AudioNode createAudio(Level level) {
        AudioNode explosionAudio = new AudioNode(level.getApp().getAssetManager(), "Sounds/Effects/explosions/explosion07.wav", DataType.Buffer);
        explosionAudio.setPositional(true);
        explosionAudio.setLooping(false);
        explosionAudio.setVolume(30);
        
        return explosionAudio;
    }
}
