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
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
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
     * Comienza el Level 1
     */
    public void salir() {
        this.nifty.exit();
        this.app.stop();
    }
    
}
