/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.states;

import com.jme3.app.state.AbstractAppState;
import mygame.Main;
import mygame.caracters.Player;

/**
 *
 * @author martin
 */
public abstract class Level extends AbstractAppState {
    
    
    public abstract Main getApp();
    
    public abstract void setApp(Main app);
    
    public abstract Player getPlayer();
    
    public abstract void setPlayer(Player player);
    
    public abstract void pause();
    
    public abstract void resume();
    
}
