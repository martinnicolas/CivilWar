/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import com.mygame.Main;

/**
 *
 * @author martin
 */
public class StartScreen extends AbstractScreen implements ScreenController {
    
    private Element popUpExit;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.setApp((Main)app);
        this.setAudioNode(new AudioNode(this.getApp().getAssetManager(), "Sounds/Music/ambientmain_0.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setName("audio_start_screen");
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
        this.setPopUpExit(this.getNifty().createPopup("popupExit"));
    }

    @Override
    public void onStartScreen() {
        this.getAudioNode().play(); // play continuously!
    }

    @Override
    public void onEndScreen() {
        this.getAudioNode().stop();
    }
    
    /**
     * Start Level 1
     */
    public void play() {
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/loading_screen.xml", "loading_screen", loadingScreen);
    }
    
    /**
     * Show Help Screen
     */
    public void showHelp() {
        this.getApp().getStateManager().detach(this);
        HelpScreen helpScreen = new HelpScreen();
        helpScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/help_screen.xml", "help_screen", helpScreen);
    }
    
    /**
     * Show About Screen
     */
    public void showAbout() {
        this.getApp().getStateManager().detach(this);
        AboutScreen aboutScreen = new AboutScreen();
        aboutScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/about_screen.xml", "about_screen", aboutScreen);
    }
    
    /**
     * Show confirmation popUp for exit action
     */
    public void showExitConfirmation() {
        this.getNifty().showPopup(this.getNifty().getCurrentScreen(), this.getPopUpExit().getId(), null);
    }
    
    /**
     * Confirm exit action
     */
    public void confirmExit() {
        this.getNifty().exit();
        this.getApp().stop();
    }

    /**
     * Close popUp
     */    
    public void cancelExit() { 
        this.getNifty().closePopup(this.getPopUpExit().getId());
    }

    public Element getPopUpExit() {
        return popUpExit;
    }

    public void setPopUpExit(Element popUpExit) {
        this.popUpExit = popUpExit;
    }
    
}
