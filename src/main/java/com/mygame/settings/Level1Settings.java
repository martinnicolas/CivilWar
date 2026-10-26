/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.settings;

import com.jme3.math.Vector3f;
import java.util.List;

/**
 * Settings for Level 1 logic.
 * 
 * @author martin
 */
public final class Level1Settings {

    private static final float MAX_TIME_TO_FINISH_THE_LEVEL = 300f; //300f (4 minutes)
    
    private static final List<Vector3f> ZOMBIE_SPAWN_POSITIONS = List.of(
        new Vector3f(-110, 3, -311),
        new Vector3f(-80, 3, -353),
        new Vector3f(-70, 3, -326),
        new Vector3f(-23, 3, -307),
        new Vector3f(7, 3, -300),
        new Vector3f(48, 3, -313),
        new Vector3f(87, 3, -315),
        new Vector3f(89, 3, -270),
        new Vector3f(-30, 10, 150),
        new Vector3f(50, 10, -550),
        new Vector3f(20, 10, -10),
        new Vector3f(10, 10, -50),
        new Vector3f(50, 10, -30),
        new Vector3f(20, 10, -20),
        new Vector3f(20, 10, -25),
        new Vector3f(20, 10, -50),
        new Vector3f(0, 10, 0),
        new Vector3f(0, 10, 50),
        new Vector3f(10, 10, 50),
        new Vector3f(10, 10, 70),
        new Vector3f(-80, 10, -80),
        new Vector3f(-60, 10, 60),
        new Vector3f(60, 10, -60),
        new Vector3f(80, 10, 80),
        new Vector3f(-100, 10, 0),
        new Vector3f(100, 10, 0),
        new Vector3f(0, 10, -10),
        new Vector3f(0, 10, 10),
        new Vector3f(-40, 10, 20),
        new Vector3f(40, 10, -20)
    );
    
    private static final List<Vector3f> AMMO_BONUS_SPAWN_POSITIONS = List.of(
        new Vector3f(-28, 10, 26),
        new Vector3f(40, 10, 80),
        new Vector3f(-12, 40, -88)
    );
    
    private static final List<Vector3f> HEALTH_BONUS_SPAWN_POSITIONS = List.of(
        new Vector3f(-28, 10, 45),
        new Vector3f(40, 10, 88),
        new Vector3f(-13, 40, -88)
    );
    
    private static final List<Vector3f> WATER_COLLECTIBLE_SPAWN_POSITIONS = List.of(
        new Vector3f(-28, 10, 48),
        new Vector3f(-54, 3, -59),
        new Vector3f(-14, 40, -88),
        new Vector3f(23, 17, -112),
        new Vector3f(23, 1, -112)
    );    
    
    private static final List<Vector3f> HEALTH_COLLECTIBLE_SPAWN_POSITIONS = List.of(
        new Vector3f(-28, 10, 47),
        new Vector3f(-53, 3, -59),
        new Vector3f(-16, 40, -88),
        new Vector3f(9, 17, -112),
        new Vector3f(8, 1, -111)
    );

    private static final List<Vector3f> FUEL_COLLECTIBLE_SPAWN_POSITIONS = List.of(
        new Vector3f(-29, 10, 47),
        new Vector3f(-52, 3, -59),
        new Vector3f(-17, 40, -88),
        new Vector3f(13, 17, -112),
        new Vector3f(7, 1, -97)
    );
    
    private static final List<Vector3f> EXPLOSION_POSITIONS = List.of(
        new Vector3f(7, 3, -82),
        new Vector3f(-38, 3, -96),
        new Vector3f(-59, 3, -147),
        new Vector3f(-18, 4, -153),
        new Vector3f(19, 4, -143),
        new Vector3f(28, 3, -95)
    );

    private Level1Settings() {
    }

    public static float getMaxTimeToFinishTheLevel() {
        return Level1Settings.MAX_TIME_TO_FINISH_THE_LEVEL;
    }

    public static List<Vector3f> getZombieSpawnPositions() {
        return Level1Settings.ZOMBIE_SPAWN_POSITIONS;
    }

    public static List<Vector3f> getAmmoBonusSpawnPositions() {
        return Level1Settings.AMMO_BONUS_SPAWN_POSITIONS;
    }

    public static List<Vector3f> getHealthBonusSpawnPositions() {
        return Level1Settings.HEALTH_BONUS_SPAWN_POSITIONS;
    }
    
    public static List<Vector3f> getWaterCollectibleSpawnPositions() {
        return Level1Settings.WATER_COLLECTIBLE_SPAWN_POSITIONS;
    }

    public static List<Vector3f> getHealthCollectibleSpawnPositions() {
        return Level1Settings.HEALTH_COLLECTIBLE_SPAWN_POSITIONS;
    }

    public static List<Vector3f> getFuelCollectibleSpawnPositions() {
        return Level1Settings.FUEL_COLLECTIBLE_SPAWN_POSITIONS;
    }
    
    public static List<Vector3f> getExplosionPositions() {
        return Level1Settings.EXPLOSION_POSITIONS;
    }
}
