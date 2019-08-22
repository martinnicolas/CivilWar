/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.states;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.List;
import mygame.Main;
import mygame.characters.Player;

/**
 *
 * @author martin
 */
public abstract class Level extends AbstractAppState {
    
    private Main app;
    private AppStateManager stateManager;
    private Player player;
    private AudioNode audioNode;
    private Node localRootNode;
    private Node rootNode;
    private AssetManager assetManager;
    private RigidBodyControl control;
    private BitmapText pauseText;
    
    /**
     * Pause game. Rewrite specific implementation in every class if it's necesary
     */
    public void pause() {
        this.setEnabled(false);
        this.getApp().getFlyByCamera().setEnabled(false);
        //Write text on the screen
        BitmapFont guiFont = this.getApp().getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        this.setPauseText(new BitmapText(guiFont, false));
        this.getPauseText().setSize(guiFont.getCharSet().getRenderedSize());
        this.getPauseText().setText("PAUSE");
        this.getPauseText().setColor(ColorRGBA.Red);
        this.getPauseText().setLocalTranslation(10, 750, 0);
        this.getApp().getGuiNode().attachChild(this.getPauseText());
        this.getAudioNode().stop();
        this.getLocalRootNode().detachChild(this.getAudioNode());
        this.setAudioNode(new AudioNode(this.getApp().getAssetManager(), "Sounds/Music/ambientmain_0.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getLocalRootNode().attachChild(this.getAudioNode());
        this.getAudioNode().play();// play continuously!
        this.disableAllControls();
    }
    
    /**
     * Resume game. Rewrite specific implementation in every class if it's necesary
     */
    public void resume() {
        this.setEnabled(true);
        this.getApp().getFlyByCamera().setEnabled(true);
        this.getApp().getInputManager().setCursorVisible(false);
        this.getApp().getGuiNode().detachChild(this.getPauseText());
        this.getAudioNode().stop();
        this.getLocalRootNode().detachChild(this.getAudioNode());
        this.setAudioNode(new AudioNode(this.getApp().getAssetManager(), "Sounds/Effects/Outdoor_Ambiance.ogg", AudioData.DataType.Stream));
        this.getAudioNode().setLooping(true);  // activate continuous playing
        this.getAudioNode().setPositional(false);
        this.getLocalRootNode().attachChild(this.getAudioNode());
        this.getAudioNode().play();// play continuously!
        this.enableAllControls();
    }    
    
    /**
     * Enable all controls
     */
    private void enableAllControls() {
        List<Spatial> spatials = this.getLocalRootNode().getChildren();
        for (Spatial spatial : spatials) {
            for (int i = 0; i < spatial.getNumControls(); i++) {
                Control spatialControl = spatial.getControl(i);
                if (spatialControl instanceof PhysicsControl) {
                    PhysicsControl physicsControl = (PhysicsControl) spatialControl;
                    physicsControl.setEnabled(true);
                } else {
                    AbstractControl abstractControl = (AbstractControl) spatialControl;
                    abstractControl.setEnabled(true);
                }
            }
        }
    }
    
    /**
     * Disable all controls
     */
    private void disableAllControls() {
        List<Spatial> spatials = this.getLocalRootNode().getChildren();
        for (Spatial spatial : spatials) {
            for (int i = 0; i < spatial.getNumControls(); i++) {
                Control spatialControl = spatial.getControl(i);
                if (spatialControl instanceof PhysicsControl) {
                    PhysicsControl physicsControl = (PhysicsControl) spatialControl;
                    physicsControl.setEnabled(false);
                } else {
                    AbstractControl abstractControl = (AbstractControl) spatialControl;
                    abstractControl.setEnabled(false);
                }
            }
        }        
    }

    public BitmapText getPauseText() {
        return pauseText;
    }

    public void setPauseText(BitmapText pauseText) {
        this.pauseText = pauseText;
    }
    
    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }
    
    public Main getApp() {
        return this.app;
    };
    
    public void setApp(Main app) {
        this.app = app;
    };
    
    public Player getPlayer() {
        return this.player;
    };
    
    public void setPlayer(Player player) {
        this.player = player;
    };
    
    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public RigidBodyControl getControl() {
        return control;
    }

    public void setControl(RigidBodyControl control) {
        this.control = control;
    }
    
    public Node getLocalRootNode() {
        return localRootNode;
    }

    public void setLocalRootNode(Node localRootNode) {
        this.localRootNode = localRootNode;
    }
    
    public AudioNode getAudioNode() {
        return audioNode;
    }

    public void setAudioNode(AudioNode audioNode) {
        this.audioNode = audioNode;
    }
    
}
