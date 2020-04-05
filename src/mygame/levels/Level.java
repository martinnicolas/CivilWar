/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.levels;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import de.lessvoid.nifty.Nifty;
import java.util.List;
import mygame.Main;
import mygame.characters.Player;
import mygame.controls.PlayerControl;
import mygame.controls.PlayerHUDControl;
import mygame.screens.PauseScreen;

/**
 *
 * @author martin
 */
public abstract class Level extends AbstractAppState {
    
    private Main app;
    private AppStateManager stateManager;
    private Player player;
    private AudioNode audioNode;
    private Node rootNode;
    private AssetManager assetManager;
    private RigidBodyControl control;
    //For pause screen
    private Nifty nifty;
    
    /**
     * Pause game. Rewrite specific implementation in every class if it's necesary
     */
    public void pause() {
        this.getAudioNode().stop();
        this.getApp().getFlyByCamera().setEnabled(false);
        this.getApp().getInputManager().setCursorVisible(false);
        this.setEnabled(false);
        this.disableAllControls();
        this.disablePhysics();
        this.showPauseScreen();
    }
    
    /**
     * Resume game. Rewrite specific implementation in every class if it's necesary
     */
    public void resume() {
        this.closePauseScreen();
        this.setEnabled(true);
        this.getAudioNode().play();
        this.getApp().getFlyByCamera().setEnabled(true);
        this.enableAllControls();
        this.enablePhysics();
    }    
    
    /**
     * Show pause screen
     */
    private void showPauseScreen() {        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(this.getApp().getAssetManager(),
                                                          this.getApp().getInputManager(),
                                                          this.getApp().getAudioRenderer(),
                                                          this.getApp().getGuiViewPort());
        this.setNifty(niftyDisplay.getNifty());
        PauseScreen pauseScreen = new PauseScreen(this);
        pauseScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/pause_screen.xml", "pause_screen", pauseScreen);
        this.getApp().getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    /**
     * Close pause screen
     */
    private void closePauseScreen() {
        this.getApp().getStateManager().detach(this.getApp().getStateManager().getState(PauseScreen.class));
        this.getNifty().exit();
    }
    
    /**
     * Disable physics
     */
    private void disablePhysics() {
       this.getApp().getStateManager().getState(BulletAppState.class).setEnabled(false); 
    }
    
    /**
     * Enable physics
     */
    private void enablePhysics() {
       this.getApp().getStateManager().getState(BulletAppState.class).setEnabled(true);
    }
    
    /**
     * Enable all controls when Level resumes
     */
    private void enableAllControls() {
        List<Spatial> spatials = this.getApp().getRootNode().getChildren();
        for (Spatial spatial : spatials) {
            for (int i = 0; i < spatial.getNumControls(); i++) {
                Control spatialControl = spatial.getControl(i);
                if (spatialControl instanceof PhysicsControl) {
                    PhysicsControl physicsControl = (PhysicsControl) spatialControl;
                    physicsControl.setEnabled(true);
                } else {
                    AbstractControl abstractControl = (AbstractControl) spatialControl;
                    abstractControl.setEnabled(true);
                }
            }
        }
    }
    
    /**
     * Disable all controls when Level is paused
     */
    private void disableAllControls() {
        List<Spatial> spatials = this.getApp().getRootNode().getChildren();
        for (Spatial spatial : spatials) {
            for (int i = 0; i < spatial.getNumControls(); i++) {
                Control spatialControl = spatial.getControl(i);
                if (spatialControl instanceof PhysicsControl) {
                    PhysicsControl physicsControl = (PhysicsControl) spatialControl;
                    physicsControl.setEnabled(false);
                } else {
                    AbstractControl abstractControl = (AbstractControl) spatialControl;
                    abstractControl.setEnabled(false);
                }
            }
        }        
    }
    
    /**
     * Remove Level settings
     */
    public void removeSettings() {
        this.getPlayer().getPlayerNode().getControl(PlayerHUDControl.class).getNifty().exit();
        this.getPlayer().getPlayerNode().removeControl(PlayerHUDControl.class);
        this.getPlayer().getPlayerNode().removeControl(PlayerControl.class);
        BulletAppState bulletAppState = this.getApp().getStateManager().getState(BulletAppState.class);
        this.getApp().getStateManager().detach(bulletAppState);
        this.getApp().getInputManager().removeListener(this.getPlayer());
    }
    
    public abstract void setUpAudio();
    
    public Main getApp() {
        return this.app;
    };
    
    public void setApp(Main app) {
        this.app = app;
    };
    
    public Player getPlayer() {
        return this.player;
    };
    
    public void setPlayer(Player player) {
        this.player = player;
    };
    
    public RigidBodyControl getControl() {
        return control;
    }

    public void setControl(RigidBodyControl control) {
        this.control = control;
    }
    
    public AudioNode getAudioNode() {
        return audioNode;
    }

    public void setAudioNode(AudioNode audioNode) {
        this.audioNode = audioNode;
    }

    public Nifty getNifty() {
        return nifty;
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }
    
}
