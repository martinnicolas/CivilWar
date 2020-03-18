/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.screens;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author martin
 */
public class LoadingScreen extends AbstractScreen implements ScreenController {
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.setNifty(nifty);
        this.setScreen(screen);
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
}
