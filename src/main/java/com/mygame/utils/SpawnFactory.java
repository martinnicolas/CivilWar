/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.utils;

import com.jme3.math.Vector3f;
import com.mygame.bonuses.AmmoBonus;
import com.mygame.bonuses.HealthBonus;
import com.mygame.enemies.ZombieEnemy;
import com.mygame.levels.Level;
import java.util.ArrayList;
import java.util.List;

/**
 * Spawn factory to spawn spawnable objects on the game (Enemy, Bonus)
 * 
 * @author martin
 */
public final class SpawnFactory {
    /**
     * Spawn objects in the level using the provided settings.
     * @param <T> spawn object type
     * @param spawnClass class to spawn
     * @param level level that owns the spawned objects
     * @param settings spawn settings
     * @return spawned objects
     */
    public static <T extends Spawnable> List<T> spawn(Class<T> spawnClass, Level level, SpawnSettings settings) {
        List<T> spawnedObjects = new ArrayList<>();

        for (Vector3f position : getSpawnPositions(spawnClass, settings)) {
            spawnedObjects.add(create(spawnClass, level, position));
        }

        return spawnedObjects;
    }

    /**
     * Get the spawnable objects positions to spawn 
     * 
     * @param spawnClass Spawnable to spawn
     * @param settings Level that implements SpawnSettings
     * @return 
     */
    private static List<Vector3f> getSpawnPositions(Class<? extends Spawnable> spawnClass, SpawnSettings settings) {
        if (spawnClass.equals(ZombieEnemy.class)) {
            return settings.getZombieSpawnPositions();
        }
        if (spawnClass.equals(AmmoBonus.class)) {
            return settings.getAmmoBonusSpawnPositions();
        }
        if (spawnClass.equals(HealthBonus.class)) {
            return settings.getHealthBonusSpawnPositions();
        }
        throw new IllegalArgumentException("Unsupported spawn class: " + spawnClass.getName());
    }

    /**
     * Factory to create Spawneable object according to class in some position
     * 
     * @param <T>
     * @param spawnClass
     * @param level
     * @param position
     * @return 
     */
    private static <T extends Spawnable> T create(Class<T> spawnClass, Level level, Vector3f position) {
        if (spawnClass.equals(ZombieEnemy.class)) {
            ZombieEnemy zombieEnemy = new ZombieEnemy(level.getApp().getAssetManager(), ZombieEnemy.DEFAULT_ENERGY, ZombieEnemy.DEFAULT_DAMAGE, position);
            return spawnClass.cast(zombieEnemy);
        }
        if (spawnClass.equals(AmmoBonus.class)) {
            return spawnClass.cast(new AmmoBonus(level.getApp().getAssetManager(), AmmoBonus.DEFAULT_AMOUNT, position));
        }
        if (spawnClass.equals(HealthBonus.class)) {
            return spawnClass.cast(new HealthBonus(level.getApp().getAssetManager(), HealthBonus.DEFAULT_AMOUNT, position));
        }
        throw new IllegalArgumentException("Unsupported spawn class: " + spawnClass.getName());
    }
}
