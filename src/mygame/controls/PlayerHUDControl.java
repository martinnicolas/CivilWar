/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import mygame.Main;
import mygame.characters.Player;

/**
 *
 * @author martin
 */
public class PlayerHUDControl extends AbstractControl {
    
    //Variables for HUD texts
    private BitmapText ammoesText;
    private BitmapText healthText;
    private Main app;
    private Player player;
    
    public PlayerHUDControl(Application app, Player player) {
        this.setApp((Main) app);
        this.setPlayer(player);
        this.initHUD();
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.updateHUD();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    /**
     * Init HUD
     */
    private void initHUD() {
        //Write text for ammoes
        BitmapFont guiFont = this.getApp().getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        this.setAmmoesText(new BitmapText(guiFont, false));
        this.getAmmoesText().setSize(guiFont.getCharSet().getRenderedSize());
        this.getAmmoesText().setText("Ammo:     " + Integer.toString(this.getPlayer().getControl().getAmmoes()));
        this.getAmmoesText().setLocalTranslation(this.getApp().getCamera().getWidth() - 150, 750, 0);
        this.getApp().getGuiNode().attachChild(this.getAmmoesText());
        //Write text for health
        this.setHealthText(new BitmapText(guiFont, false));
        this.getHealthText().setSize(guiFont.getCharSet().getRenderedSize());
        this.getHealthText().setText("Health:     " + Integer.toString(Math.round(this.getPlayer().getControl().getHealth())));
        this.getHealthText().setLocalTranslation(this.getApp().getCamera().getWidth() - 150, 725, 0);
        this.getApp().getGuiNode().attachChild(this.getHealthText());
    }
    
    /**
     * Update HUD
     */
    private void updateHUD() {
        if (!this.getPlayer().getControl().haveEnoughAmmoes()) {
            this.getAmmoesText().setColor(ColorRGBA.Red);
        } else {
            this.getAmmoesText().setColor(ColorRGBA.White);
        }
        this.getAmmoesText().setText("Ammo:     " + Integer.toString(this.getPlayer().getControl().getAmmoes()));
        this.getHealthText().setText("Health:     " + Integer.toString(Math.round(this.getPlayer().getControl().getHealth())));
    }
    
    public BitmapText getAmmoesText() {
        return ammoesText;
    }

    public void setAmmoesText(BitmapText ammoesText) {
        this.ammoesText = ammoesText;
    }

    public BitmapText getHealthText() {
        return healthText;
    }

    public void setHealthText(BitmapText healthText) {
        this.healthText = healthText;
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
    
}
