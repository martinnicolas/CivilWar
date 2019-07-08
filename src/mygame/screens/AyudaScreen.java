/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 * @author martin
 */
public class AyudaScreen extends AbstractAppState implements ScreenController {
    
    private Nifty nifty;
    private Screen screen;
    private Element element;
    private Parameters parameters;    
    private Main app;
    private AppStateManager stateManager;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.stateManager = stateManager;
        this.app = (Main)app;
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    /**
     * Volver al menu principal
     */
    public void volver() {
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(stateManager, app);
        this.nifty.fromXml("Interface/start_screen.xml", "start_screen", startScreen);
    }
    
}