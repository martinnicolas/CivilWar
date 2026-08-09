/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.controls;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import com.mygame.characters.Player;
import com.mygame.screens.HUDScreen;
import com.mygame.screens.NiftyScreenHelper;
import de.lessvoid.nifty.elements.Element;

/**
 *
 * @author martin
 */
public class PlayerHUDControl extends AbstractControl {
    
    public static String DEFAULT_FONT_COLOR = "#FFF";
    public static String EMPTY_FONT_COLOR = "#FF0000";
    public static String TIMER_FORMAT = "00    %02d    %02d";
    public static String FUEL_COLLECTIBLES_STATUS_FORMAT = "%01d/%01d";
    public static String WATER_COLLECTIBLES_STATUS_FORMAT = "%01d/%01d";
    public static String HEALTH_COLLECTIBLES_STATUS_FORMAT = "%01d/%01d";
    
    private SimpleApplication app;
    private Player player;
    private Nifty nifty;
    private float damageFlashTimer = 0f;
    
    public PlayerHUDControl(SimpleApplication app, Player player) {
        this.app = app;
        this.player = player;
        this.showHUDScreen();
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.updateHUD();
        this.updateTimer(tpf);
        this.updateDamageFlash(tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    /**
     * Show HUD Screen
     */
    private void showHUDScreen() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(this.getApp().getAssetManager(),
                                                          this.getApp().getInputManager(),
                                                          this.getApp().getAudioRenderer(),
                                                          this.getApp().getGuiViewPort());
        this.setNifty(niftyDisplay.getNifty());
        NiftyScreenHelper.loadDefaults(this.getNifty());
        HUDScreen hudScreen = new HUDScreen();
        this.getNifty().fromXml("Interface/hud_screen.xml", "hud_screen", hudScreen);
        this.getApp().getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    /**
     * Update HUD indicators
     */
    private void updateHUD() {
        Screen hudScreen = this.getNifty().getScreen("hud_screen");
        
        if (hudScreen == null) { return; }

        this.updateBonusInidicators(hudScreen);
        this.updateCollectiblesIndicators(hudScreen);
    }
    
    private void updateBonusInidicators(Screen hudScreen) {
        Label ammoText = (Label) hudScreen.findNiftyControl("ammo_text", Label.class);
        Label ammoCounter = (Label) hudScreen.findNiftyControl("ammo_counter", Label.class);
        Label healthText = (Label) hudScreen.findNiftyControl("health_text", Label.class);
        Label healthCounter = (Label) hudScreen.findNiftyControl("health_counter", Label.class);
        
        if (ammoText == null || ammoCounter == null || healthText == null || healthCounter == null) { return; }
        
        if (!this.getPlayer().getControl().haveEnoughAmmoes()) {
            this.setNotEnoughTextColor(ammoText, ammoCounter);
        }
        else {
            this.setEnoughTextColor(ammoText, ammoCounter);
        }
        
        if (!this.getPlayer().getControl().haveEnoughHealth()) {
            this.setNotEnoughTextColor(healthText, healthCounter);
        }
        else {
            this.setEnoughTextColor(healthText, healthCounter);
        }

        ammoCounter.setText(Integer.toString(this.getPlayer().getControl().getAmmoes()));        
        healthCounter.setText(Integer.toString(Math.round(this.getPlayer().getControl().getHealth())));
    }
    
    private void setNotEnoughTextColor(Label ammoText, Label ammoCounter) {
        ammoText.setColor(new Color(PlayerHUDControl.EMPTY_FONT_COLOR));
        ammoCounter.setColor(new Color(PlayerHUDControl.EMPTY_FONT_COLOR));
    }
    
    private void setEnoughTextColor(Label ammoText, Label ammoCounter) {
        Color ammoTextColor = ammoText.getColor();

        if (ammoTextColor == null || PlayerHUDControl.DEFAULT_FONT_COLOR.equals(ammoTextColor.getColorString())) { return; }
        
        ammoText.setColor(new Color(PlayerHUDControl.DEFAULT_FONT_COLOR));
        ammoCounter.setColor(new Color(PlayerHUDControl.DEFAULT_FONT_COLOR));
    }
    
    private void updateCollectiblesIndicators(Screen hudScreen) {
        Label fuelCollectibleAmount = (Label) hudScreen.findNiftyControl("fuel_text", Label.class);
        Label waterCollectibleAmount = (Label) hudScreen.findNiftyControl("water_text", Label.class);
        Label healthCollectibleAmount = (Label) hudScreen.findNiftyControl("medical_text", Label.class);
        
        if (fuelCollectibleAmount == null || healthCollectibleAmount == null || waterCollectibleAmount == null) { return; }
        
        int currentHealthCollectiblesCollected = this.getPlayer().getControl().getHealthCollectibles();
        int currentWaterCollectiblesCollected = this.getPlayer().getControl().getWaterCollectibles();
        int currentFuelCollectiblesCollected = this.getPlayer().getControl().getFuelCollectibles();
        
        int maxAmountOfHealthCollectibles = this.getPlayer().getLevel().getMaxSizeOfHealthCollectibles();
        int maxAmountOfWaterCollectibles = this.getPlayer().getLevel().getMaxSizeOfWaterCollectibles();
        int maxAmountOfFuelCollectibles = this.getPlayer().getLevel().getMaxSizeOfFuelCollectibles();

        String currentFuelCollectibleStatus = String.format(PlayerHUDControl.FUEL_COLLECTIBLES_STATUS_FORMAT, currentFuelCollectiblesCollected, maxAmountOfFuelCollectibles);
        fuelCollectibleAmount.setText(currentFuelCollectibleStatus);   
        String currentWaterCollectibleStatus = String.format(PlayerHUDControl.WATER_COLLECTIBLES_STATUS_FORMAT, currentWaterCollectiblesCollected, maxAmountOfWaterCollectibles);
        waterCollectibleAmount.setText(currentWaterCollectibleStatus);
        String currentHealthCollectibleStatus = String.format(PlayerHUDControl.HEALTH_COLLECTIBLES_STATUS_FORMAT, currentHealthCollectiblesCollected, maxAmountOfHealthCollectibles);
        healthCollectibleAmount.setText(currentHealthCollectibleStatus);
    }
    
    private void updateTimer(float tpf) {
        Screen screen = this.getNifty().getScreen("hud_screen");
        
        if (screen == null) { return; }
        
        Label timerText = (Label) screen.findNiftyControl("timer_text", Label.class);
        
        if (timerText == null) { return; }
        
        int minutes = (int) this.getPlayer().getControl().getRemainingTime() / 60;
        int seconds = (int) this.getPlayer().getControl().getRemainingTime() % 60;

        String timer = String.format(PlayerHUDControl.TIMER_FORMAT, minutes, seconds);
        timerText.setText(timer);
    }
    
    private void updateDamageFlash(float tpf) {
        Screen screen = this.getNifty().getScreen("hud_screen");

        if (screen == null) { return; }

        Element damageFlash = screen.findElementById("damage_flash");

        if (damageFlash == null) { return; }

        if (this.damageFlashTimer > 0f) {
            damageFlash.show();
            this.damageFlashTimer -= tpf;
        }
        else {
            damageFlash.hide();
        }
    }
    
    public void showDamageFlash() {
        this.damageFlashTimer = 0.20f;
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

    public Nifty getNifty() {
        return nifty;
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }
}
