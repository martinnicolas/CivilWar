/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import mygame.levels.Level;

/**
 *
 * @author martin
 */
public class PauseScreen extends AbstractScreen implements ScreenController{
    
    private Nifty nifty;
    private Level level;
    private Element popUpExit;
    private Element popUpHelpScreen;
    
    
    public PauseScreen(Level level) {
        this.level = level;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.setApp((Main) app);
        this.setStateManager(stateManager);
        this.setAudioNode(new AudioNode(this.getApp().getAssetManager(), "Sounds/Music/ambientmain_0.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getApp().getRootNode().attachChild(this.getAudioNode());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        this.getApp().getRootNode().detachChild(this.getAudioNode());
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.setNifty(nifty);
        this.setScreen(screen);
        this.setPopUpExit(this.getNifty().createPopup("popUpExit"));
        this.setPopUpHelpScreen(this.getNifty().createPopup("popUpHelpScreen"));
    }

    @Override
    public void onStartScreen() {
        this.getAudioNode().play();
    }

    @Override
    public void onEndScreen() {
        this.getAudioNode().stop();
    }
    
    /**
     * Shows popup confirmation screen
     */
    public void salir() {
        this.getNifty().showPopup(this.getNifty().getCurrentScreen(), this.getPopUpExit().getId(), null);
    }
    
    /**
     * Exit current Level and init start screen
     */
    public void aceptar() {
        this.getStateManager().detach(this.getLevel());
        this.getNifty().exit();
        this.getStateManager().detach(this);
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(this.getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/start_screen.xml", "start_screen", startScreen);
    }
    
    /**
     * Cancel and return to pause menu screen
     */
    public void cancelar() {
        this.nifty.closePopup(this.getPopUpExit().getId());
    }
    
    /**
     * Shows a help popup screen
     */
    public void ayuda() {
        this.nifty.showPopup(this.nifty.getCurrentScreen(), this.getPopUpHelpScreen().getId(), null);
    }
    
    /**
     * Close the help popup screen
     */
    public void volver() {
        this.nifty.closePopup(this.getPopUpHelpScreen().getId());
    }

    /**
     * Get the actual Level instance
     * @return the actual level instance
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Set the actual level instance
     * @param level 
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Get the popUpExit
     * @return popUpExit
     */
    public Element getPopUpExit() {
        return popUpExit;
    }

    /**
     * Sets the popUpExit
     * @param popUpExit 
     */
    public void setPopUpExit(Element popUpExit) {
        this.popUpExit = popUpExit;
    }

    /**
     * Get the popUpHelpScreen
     * @return 
     */
    public Element getPopUpHelpScreen() {
        return popUpHelpScreen;
    }

    /**
     * Sets popUpHelpScreen
     * @param popUpHelpScreen 
     */
    public void setPopUpHelpScreen(Element popUpHelpScreen) {
        this.popUpHelpScreen = popUpHelpScreen;
    }
    
}
