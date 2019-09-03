/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.app.Application;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import mygame.Main;
import mygame.characters.Player;
import mygame.screens.HUDScreen;

/**
 *
 * @author martin
 */
public class PlayerHUDControl extends AbstractControl {
    
    private Main app;
    private Player player;
    private Nifty nifty;
    
    public PlayerHUDControl(Main app, Player player) {
        this.app = app;
        this.player = player;
        this.showHUDScreen();
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.updateHUD();
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
        HUDScreen hudScreen = new HUDScreen();
        this.getNifty().fromXml("Interface/hud_screen.xml", "hud_screen", hudScreen);
        this.getApp().getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    /**
     * Update HUD indicators
     */
    private void updateHUD() {
        Screen screen = this.getNifty().getScreen("hud_screen");
        Label ammoText = (Label) screen.findNiftyControl("ammo_text", Label.class);
        Label ammo = (Label) screen.findNiftyControl("ammo", Label.class);
        if (!this.getPlayer().getControl().haveEnoughAmmoes()) {
            ammoText.setColor(new Color("#FF0000"));
            ammo.setColor(new Color("#FF0000"));
        }
        else
            if (!"#FFF".equals(ammoText.getColor().getColorString())) {
                ammoText.setColor(new Color("#FFF"));
                ammo.setColor(new Color("#FFF"));
            }
        ammoText.setText(Integer.toString(this.getPlayer().getControl().getAmmoes()));
        Label healthText = (Label) screen.findNiftyControl("health_text", Label.class);
        healthText.setText(Integer.toString(Math.round(this.getPlayer().getControl().getHealth())));
    }

    public Main getApp() {
        return app;
    }

    public void setApp(Main app) {
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
