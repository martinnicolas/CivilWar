/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.controls;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.mygame.characters.Player;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author martin
 */
public class PlayerDamageControl extends AbstractControl {
    
    private static final float DEFAULT_TIME_TO_SHOW_DAMAGE = 0.20f;
    private static final long DAMAGE_COOLDOWN_MS = 1000L;
    private SimpleApplication app;
    private Player player;
    private float damageFlashTimer = 0f;
    private long lastDamageTime = 0L;
    
    public PlayerDamageControl(SimpleApplication app, Player player) {
        this.app = app;
        this.player = player;
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.updateDamageFlash(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    /**
     * Set damageFlashTimer to show damage flash
     */
    public void showDamageFlash() {
        this.setDamageFlashTimer(PlayerDamageControl.DEFAULT_TIME_TO_SHOW_DAMAGE);
    }
    
    /**
     * Return true if user can receive damage otherwise returns false
     * 
     * @return 
     */
    public boolean canReceiveDamage() {
        long now = System.currentTimeMillis();
        return (now - this.getLastDamageTime()) >= PlayerDamageControl.DAMAGE_COOLDOWN_MS;
    }
    
    /**
     * Update damageFlashTimer and hide damage flash if timer is zero
     */
    private void updateDamageFlash(float tpf) {
        PlayerHUDControl hudControl = this.player.getPlayerNode().getControl(PlayerHUDControl.class);
        if (hudControl == null || hudControl.getNifty() == null) { return; }

        Screen screen = hudControl.getNifty().getScreen("hud_screen");

        if (screen == null) { return; }

        Element damageFlash = screen.findElementById("damage_flash");

        if (damageFlash == null) { return; }

        if (this.getDamageFlashTimer() > 0f) {
            damageFlash.show();
            this.setDamageFlashTimer(this.getDamageFlashTimer() - tpf);
        }
        else {
            damageFlash.hide();
        }
    }
    
    public SimpleApplication getApp() {
        return app;
    }

    public void setApp(SimpleApplication app) {
        this.app = app;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getDamageFlashTimer() {
        return damageFlashTimer;
    }

    public void setDamageFlashTimer(float damageFlashTimer) {
        this.damageFlashTimer = damageFlashTimer;
    }
    
    public long getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }
}
