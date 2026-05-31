/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.levels;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.Ray;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import de.lessvoid.nifty.Nifty;
import java.util.List;
import com.mygame.bonuses.AmmoBonus;
import com.mygame.bonuses.HealthBonus;
import com.mygame.characters.Player;
import com.mygame.controls.BonusControl;
import com.mygame.controls.PlayerControl;
import com.mygame.controls.PlayerHUDControl;
import com.mygame.screens.GameOverScreen;
import com.mygame.screens.NiftyScreenHelper;
import com.mygame.screens.PauseScreen;

/**
 *
 * @author martin
 */
public abstract class Level extends AbstractAppState {
    
    private SimpleApplication app;
    private Player player;
    private AudioNode audioNode;
    private RigidBodyControl control;
    private Node levelNode;
    private int loadingStep;
    private int loadingSteps;
    private boolean gameOverStarted;
    //For pause screen
    private Nifty nifty;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.setApp((SimpleApplication) app);
        this.setLevelNode(new Node("level_node"));
        this.getLevelNode().setCullHint(Spatial.CullHint.Always);
        this.getApp().getRootNode().attachChild(this.getLevelNode());
        this.setEnabled(false);
    }
    
    /**
     * Load the next Level resource chunk.
     * @return true when the Level is fully loaded.
     */
    public abstract boolean loadLevel();
    
    /**
     * Start the Level once every resource is ready.
     */
    public void startLevel() {
        this.getLevelNode().setCullHint(Spatial.CullHint.Inherit);
        if (this.getPlayer() != null && this.getPlayer().getPlayerNode().getControl(PlayerHUDControl.class) == null) {
            this.getPlayer().getPlayerNode().addControl(new PlayerHUDControl(this.getApp(), this.getPlayer()));
        }
        this.setEnabled(true);
        if (this.getAudioNode() != null) {
            this.getAudioNode().play();
        }
    }
    
    /**
     * End the game and starts GameOverScreen
     */
    public void gameOver() {
        if (this.isGameOverStarted()) { return; }
        this.setGameOverStarted(true);
        this.disableAllLevelElements();
        this.showGameOverScreen();
    }
    
    /**
     * Pause the game.
     */
    public void pause() {
        this.disableAllLevelElements();
        this.showPauseScreen();
    }
    
    /**
     * Disable all elementos of current level
     */
    private void disableAllLevelElements() {
        if (this.getAudioNode() != null) {
            this.getAudioNode().stop();
        }
        this.getApp().getFlyByCamera().setEnabled(false);
        this.getApp().getInputManager().setCursorVisible(false);
        this.setEnabled(false);
        this.disableAllControls();
        this.disablePhysics();
    }
    
    /**
     * Resume game.
     */
    public void resume() {
        this.closePauseScreen();
        this.setEnabled(true);
        if (this.getAudioNode() != null) {
            this.getAudioNode().play();
        }
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
        NiftyScreenHelper.loadDefaults(this.getNifty());
        NiftyScreenHelper.registerMenuSounds(this.getNifty());
        PauseScreen pauseScreen = new PauseScreen(this);
        pauseScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/pause_screen.xml", "pause_screen", pauseScreen);
        this.getApp().getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    /**
     * Show game over screen
     */
    private void showGameOverScreen() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(this.getApp().getAssetManager(),
                                                          this.getApp().getInputManager(),
                                                          this.getApp().getAudioRenderer(),
                                                          this.getApp().getGuiViewPort());
        this.setNifty(niftyDisplay.getNifty());
        NiftyScreenHelper.loadDefaults(this.getNifty());
        NiftyScreenHelper.registerMenuSounds(this.getNifty());
        GameOverScreen gameOverScreen = new GameOverScreen();
        gameOverScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/game_over_screen.xml", "game_over_screen", gameOverScreen);
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
        this.setControlsEnabled(this.getLevelNode(), true);
    }
    
    /**
     * Disable all controls when Level is paused
     */
    private void disableAllControls() {
        this.setControlsEnabled(this.getLevelNode(), false);
    }
    
    /**
     * Enable or disable controls recursively.
     * @param spatial
     * @param enabled
     */
    protected void setControlsEnabled(Spatial spatial, boolean enabled) {
        for (int i = 0; i < spatial.getNumControls(); i++) {
            Control spatialControl = spatial.getControl(i);
            switch (spatialControl) {
                case PhysicsControl physicsControl -> physicsControl.setEnabled(enabled);
                case AbstractControl abstractControl -> abstractControl.setEnabled(enabled);
                default -> {
                }
            }
        }
        if (spatial instanceof Node node) {
            List<Spatial> children = node.getChildren();
            for (Spatial child : children) {
                this.setControlsEnabled(child, enabled);
            }
        }
    }
    
    /**
     * Remove Level settings
     */
    public void removeSettings() {
        if (this.getAudioNode() != null) {
            this.getAudioNode().stop();
            this.getAudioNode().removeFromParent();
        }
        ViewPort viewPort = this.getApp().getViewPort();
        for (SceneProcessor processor: viewPort.getProcessors()) {
            if (processor instanceof FilterPostProcessor filterPostProcessor) {
                filterPostProcessor.removeAllFilters();
            }
            viewPort.removeProcessor(processor);
        }
        LightList lightsList = this.getApp().getRootNode().getWorldLightList();
        for (Light light : lightsList) {
            this.getApp().getRootNode().removeLight(light);
        }
        BulletAppState bulletAppState = this.getApp().getStateManager().getState(BulletAppState.class);
        if (bulletAppState != null && this.getLevelNode() != null) {
            this.removePhysicsAndBonusControls(this.getLevelNode(), bulletAppState);
        }
        if (this.getPlayer() != null) {
            PlayerHUDControl playerHUDControl = this.getPlayer().getPlayerNode().getControl(PlayerHUDControl.class);
            if (playerHUDControl != null) {
                playerHUDControl.getNifty().exit();
                this.getPlayer().getPlayerNode().removeControl(PlayerHUDControl.class);
            }
            this.getPlayer().getPlayerNode().removeControl(PlayerControl.class);
            this.getApp().getInputManager().removeListener(this.getPlayer());
        }
        if (bulletAppState != null) {
            this.getApp().getStateManager().detach(bulletAppState);
        }
        if (this.getLevelNode() != null) {
            this.getLevelNode().removeFromParent();
        }
    }
    
    /**
     * Remove physics and bonus controls recursively.
     * @param spatial
     * @param bulletAppState
     */
    private void removePhysicsAndBonusControls(Spatial spatial, BulletAppState bulletAppState) {
        bulletAppState.getPhysicsSpace().removeAll(spatial);
        String spatialName = spatial.getName();
        if (AmmoBonus.SPATIAL_NAME.equals(spatialName) || HealthBonus.SPATIAL_NAME.equals(spatialName)) {
            spatial.removeControl(BonusControl.class);
        }
        if (spatial instanceof Node node) {
            List<Spatial> children = node.getChildren();
            for (Spatial child : children) {
                this.removePhysicsAndBonusControls(child, bulletAppState);
            }
        }
    }
    
    /**
     * Setup the audio level
     */
    public abstract void setUpAudio();

    /**
     * Handle a shooting raycast before the generic scene raycast.
     * @param ray shooting ray
     * @return true when the hit was consumed by the level
     */
    public abstract boolean handleShootingCollision(Ray ray);
            
    /**
    * Get the assets loading progress for level
    * 
    * @return assets loading progress percentage
    */
    public float getLoadingProgress() {
        if (this.getLoadingSteps() == 0) {
            return 0f;
        }
        return Math.min(1f, (float) this.getLoadingStep() / (float) this.getLoadingSteps());
    }
    
    public SimpleApplication getApp() {
        return this.app;
    };
    
    public void setApp(SimpleApplication app) {
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

    public Node getLevelNode() {
        return levelNode;
    }

    public void setLevelNode(Node levelNode) {
        this.levelNode = levelNode;
    }

    public int getLoadingStep() {
        return loadingStep;
    }

    public void setLoadingStep(int loadingStep) {
        this.loadingStep = loadingStep;
    }

    public int getLoadingSteps() {
        return loadingSteps;
    }

    public void setLoadingSteps(int loadingSteps) {
        this.loadingSteps = loadingSteps;
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

    public boolean isGameOverStarted() {
        return gameOverStarted;
    }

    public void setGameOverStarted(boolean gameOverStarted) {
        this.gameOverStarted = gameOverStarted;
    }
    
}
