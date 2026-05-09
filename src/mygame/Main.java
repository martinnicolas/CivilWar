package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.awt.Dimension;
import java.awt.Toolkit;
import mygame.screens.StartScreen;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60); // set to less than or equal screen refresh rate
        settings.setVSync(true);   // prevents page tearing
        settings.setFrequency(60); // set to screen refresh rate
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        settings.setResolution(screenSize.width, screenSize.height);
        settings.setFullscreen(false);
        settings.setSamples(2);    // anti-aliasing
        settings.setTitle("CivilWar"); // branding: window name
        /*try {
            // Branding: window icon
            cfg.setIcons(new BufferedImage[]{ImageIO.read(new File("assets/Interface/icon.gif"))});
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Icon missing.", ex);
        }*/
        // branding: load splashscreen from assets
        //cfg.setSettingsDialogImage("Interface/MySplashscreen.png"); 
        app.setShowSettings(false); // or don't display splashscreen
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();        
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(stateManager, this);
        nifty.fromXml("Interface/start_screen.xml", "start_screen", startScreen);
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
}
