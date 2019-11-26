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
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import mygame.levels.Level1;

/**
 *
 * @author martin
 */
public class StartScreen extends AbstractAppState implements ScreenController {
    
    private Nifty nifty;
    private Screen screen;
    private Main app;
    private AppStateManager stateManager;
    private Element popUpElement;
    private AudioNode audioMenu;
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        this.setPopUpElement(this.nifty.createPopup("popupExit"));
        this.setAudioMenu(new AudioNode(this.app.getAssetManager(), "Sounds/Music/ambientmain_0.ogg", AudioData.DataType.Stream));
        this.getAudioMenu().setLooping(true);  // activate continuous playing
        this.getAudioMenu().setPositional(false);   
    }

    @Override
    public void onStartScreen() {
        this.getAudioMenu().play(); // play continuously!
    }

    @Override
    public void onEndScreen() {
        this.getAudioMenu().stop();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.setStateManager(stateManager);
        this.setApp((Main)app);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Start Level 1
     */
    public void jugar() {
        this.nifty.exit();
        this.getStateManager().attach(new Level1());
    }
    
    /**
     * Show Help Screen
     */
    public void ayuda() {
        this.getStateManager().detach(this);
        HelpScreen helpScreen = new HelpScreen();
        helpScreen.initialize(this.getStateManager(), this.getApp());
        this.nifty.fromXml("Interface/help_screen.xml", "help_screen", helpScreen);
    }
    
    /**
     * Show About Screen
     */
    public void acerca() {
        this.getStateManager().detach(this);
        AboutScreen aboutScreen = new AboutScreen();
        aboutScreen.initialize(this.getStateManager(), this.getApp());
        this.nifty.fromXml("Interface/about_screen.xml", "about_screen", aboutScreen);
    }
    
    /**
     * Show confirmation popUp for exit action
     */
    public void salir() {
        this.nifty.showPopup(this.nifty.getCurrentScreen(), this.getPopUpElement().getId(), null);
    }
    
    /**
     * Confirm exit action
     */
    public void aceptar() {
        this.nifty.exit();
        this.getApp().stop();
    }

    /**
     * Close popUp
     */    
    public void cancelar() { 
        this.nifty.closePopup(this.popUpElement.getId());
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

    public Element getPopUpElement() {
        return popUpElement;
    }

    public void setPopUpElement(Element popUpElement) {
        this.popUpElement = popUpElement;
    }

    public AudioNode getAudioMenu() {
        return audioMenu;
    }

    public void setAudioMenu(AudioNode audioMenu) {
        this.audioMenu = audioMenu;
    }
    
}
