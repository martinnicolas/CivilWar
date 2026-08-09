/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.utils;

import com.jme3.math.Vector3f;
import java.util.List;

/**
 *
 * @author martin
 */
public interface SpawnSettings {

    /**
     * Get the zombie spawn positions.
     * @return zombie spawn positions
     */
    List<Vector3f> getZombieSpawnPositions();
    
    /**
     * Get the ammo bonus spawn positions.
     * @return ammo bonus spawn positions
     */
    List<Vector3f> getAmmoBonusSpawnPositions();
    
    /**
     * Get the health bonus spawn positions.
     * @return health bonus spawn positions
     */
    List<Vector3f> getHealthBonusSpawnPositions();
    
    /**
     * Get the water collectible spawn positions.
     * @return water collectible spawn positions
     */
    List<Vector3f> getWaterCollectibleSpawnPositions();
    
    /**
     * Get the health collectible spawn positions.
     * @return health collectible spawn positions
     */
    List<Vector3f> getHealthCollectibleSpawnPositions();
    
    
    /**
     * Get the health collectible spawn positions.
     * @return health collectible spawn positions
     */
    List<Vector3f> getFuelCollectibleSpawnPositions();
}
