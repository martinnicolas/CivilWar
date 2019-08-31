/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 * @author martin
 */
public class HelpScreen extends AbstractAppState implements ScreenController {
    
    private Nifty nifty;
    private Screen screen;
    private Main app;
    private AppStateManager stateManager;
    private AudioNode audioHelp;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        this.setAudioHelp(new AudioNode(this.app.getAssetManager(), "Sounds/Music/ambientmain_0.ogg", AudioData.DataType.Stream));
        this.getAudioHelp().setLooping(true);  // activate continuous playing
        this.getAudioHelp().setPositional(false);   
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.setStateManager(stateManager);
        this.setApp((Main) app);
    }

    @Override
    public void onStartScreen() {
        this.getAudioHelp().play(); // play continuously!
    }

    @Override
    public void onEndScreen() {
        this.getAudioHelp().stop();
    }
    
    /**
     * Volver al menu principal
     */
    public void volver() {
        this.getStateManager().detach(this);
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(this.getStateManager(), this.getApp());
        this.nifty.fromXml("Interface/start_screen.xml", "start_screen", startScreen);
    }

    public Main getApp() {
        return app;
    }

    public void setApp(Main app) {
        this.app = app;
    }

    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public AudioNode getAudioHelp() {
        return audioHelp;
    }

    public void setAudioHelp(AudioNode audioHelp) {
        this.audioHelp = audioHelp;
    }
    
}
