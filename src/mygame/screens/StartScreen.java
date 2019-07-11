/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import mygame.states.Level1;

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
        this.popUpElement = this.nifty.createPopup("popupExit");
    }

    @Override
    public void onStartScreen() {
        this.audioMenu = new AudioNode(this.app.getAssetManager(), "Sounds/Music/ambientmain_0.ogg", false);
        this.audioMenu.setLooping(true);  // activate continuous playing
        this.audioMenu.setPositional(false);   
        this.audioMenu.play();// play continuously!
    }

    @Override
    public void onEndScreen() {
        this.audioMenu.stop();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.stateManager = stateManager;
        this.app = (Main)app;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Comienza el Level 1
     */
    public void jugar() {
        this.nifty.exit();
        stateManager.attach(new Level1(this.app));
    }
    
    /**
     * Muestra pantalla de ayuda
     */
    public void ayuda() {
        AyudaScreen ayudaScreen = new AyudaScreen();
        ayudaScreen.initialize(stateManager, app);
        this.nifty.fromXml("Interface/ayuda_screen.xml", "ayuda_screen", ayudaScreen);
    }
    
    /**
     * Mostrar popUp de confirmacion
     */
    public void salir() {
        this.nifty.showPopup(this.nifty.getCurrentScreen(), this.popUpElement.getId(), null);
    }
    
    /**
     * Sale del juego
     */
    public void aceptar() {
        this.nifty.exit();
        this.app.stop();
    }

    /**
     * Cierra el popup
     */    
    public void cancelar() { 
        this.nifty.closePopup(this.popUpElement.getId());
    }
    
    
}
