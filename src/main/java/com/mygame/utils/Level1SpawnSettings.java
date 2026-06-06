/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.utils;

import com.jme3.math.Vector3f;
import java.util.List;

/**
 * Interface to set zombie enemies, ammo bonus and health bonus positions for Level 1
 * 
 * @author martin
 */
public interface Level1SpawnSettings {
    
    List<Vector3f> ZOMBIE_SPAWN_POSITIONS = List.of(
        new Vector3f(-320, 10, -320),
        new Vector3f(320, 10, -320),
        new Vector3f(-320, 10, 320),
        new Vector3f(320, 10, 320),
        new Vector3f(0, 10, -450),
        new Vector3f(0, 10, 450),
        new Vector3f(-450, 10, 0),
        new Vector3f(450, 10, 0),
        new Vector3f(-500, 10, 500),
        new Vector3f(500, 10, -500),
        new Vector3f(200, 10, -100),
        new Vector3f(100, 10, -50),
        new Vector3f(50, 10, -300),
        new Vector3f(20, 10, -20),
        new Vector3f(20, 10, -200),
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
        new Vector3f(0, 10, -100),
        new Vector3f(0, 10, 100),
        new Vector3f(-40, 10, 20),
        new Vector3f(40, 10, -20)
    );
    
    List<Vector3f> AMMO_BONUS_SPAWN_POSITIONS = List.of(
        new Vector3f(-28, 10, 26),
        new Vector3f(40, 10, 80),
        new Vector3f(-90, 10, -40)
    );
    
    List<Vector3f> HEALTH_BONUS_SPAWN_POSITIONS = List.of(
        new Vector3f(-28, 10, 45),
        new Vector3f(40, 10, 88),
        new Vector3f(-90, 10, -60)
    );
}
