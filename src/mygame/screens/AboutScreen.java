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
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 * @author martin
 */
public class AboutScreen extends AbstractScreen implements ScreenController {

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.setApp((Main) app);
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
     * Volver al menu principal
     */
    public void volver() {
        this.getApp().getStateManager().detach(this);
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(this.getApp().getStateManager(), this.getApp());
        this.getNifty().fromXml("Interface/start_screen.xml", "start_screen", startScreen);
    }
    
}
