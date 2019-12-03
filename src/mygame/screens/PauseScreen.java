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
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
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
public class PauseScreen extends AbstractAppState implements ScreenController{
    
    private Nifty nifty;
    private Level level;
    private Element popUpExit;
    private Element popUpHelpScreen;
    private AppStateManager stateManager;
    private Main app;
    private AudioNode pauseAudioNode;
    private Node localRootNode;
    private Node rootNode;
    
    public PauseScreen(Level level) {
        this.level = level;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.popUpExit = this.nifty.createPopup("popUpExit");
        this.popUpHelpScreen = this.nifty.createPopup("popUpHelpScreen");
    }

    @Override
    public void onStartScreen() {
        this.getPauseAudioNode().play();
    }

    @Override
    public void onEndScreen() {
        this.getPauseAudioNode().stop();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.setApp((Main) app);
        this.setStateManager(stateManager);
        this.setRootNode(this.getApp().getRootNode());
        this.setLocalRootNode(new Node("PauseScreen"));
        this.getRootNode().attachChild(this.getLocalRootNode());
        //Setup pause audio
        this.setUpPauseAudio();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        this.getRootNode().detachChild(this.getLocalRootNode());
        super.cleanup();
    }
    
    public void salir() {
        this.nifty.showPopup(this.nifty.getCurrentScreen(), this.popUpExit.getId(), null);
    }
    
    public void aceptar() {
        this.getStateManager().detach(this.getLevel());
        this.nifty.exit();
        this.getStateManager().detach(this);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(this.getApp().getAssetManager(),
                                                          this.getApp().getInputManager(),
                                                          this.getApp().getAudioRenderer(),
                                                          this.getApp().getGuiViewPort());
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(this.getLevel().getStateManager(), this.getApp());
        this.nifty.fromXml("Interface/start_screen.xml", "start_screen", startScreen);
        // attach the nifty display to the gui view port as a processor
        this.getApp().getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    public void cancelar() {
        this.nifty.closePopup(this.popUpExit.getId());
    }
    
    public void ayuda() {
        this.nifty.showPopup(this.nifty.getCurrentScreen(), this.popUpHelpScreen.getId(), null);
    }
    
    public void volver() {
        this.nifty.closePopup(this.popUpHelpScreen.getId());
    }
    
    /**
     * Setup pause audio
     */
    public void setUpPauseAudio() {
        this.setPauseAudioNode(new AudioNode(this.getApp().getAssetManager(), "Sounds/Music/ambientmain_0.ogg", AudioData.DataType.Stream));
        this.getPauseAudioNode().setLooping(true);  // activate continuous playing
        this.getPauseAudioNode().setPositional(false);
        this.getLocalRootNode().attachChild(this.getPauseAudioNode());
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public Node getLocalRootNode() {
        return localRootNode;
    }

    public void setLocalRootNode(Node localRootNode) {
        this.localRootNode = localRootNode;
    }
    
    public AudioNode getPauseAudioNode() {
        return pauseAudioNode;
    }

    public void setPauseAudioNode(AudioNode pauseAudioNode) {
        this.pauseAudioNode = pauseAudioNode;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    
    public void setStateManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }
    
    public AppStateManager getStateManager() {
        return this.stateManager;
    }
    
    public void setApp(Main app) {
        this.app = app;
    }
    
    public Main getApp() {
        return this.app;
    }
    
}
