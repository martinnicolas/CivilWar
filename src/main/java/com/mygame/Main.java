package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.mygame.screens.NiftyScreenHelper;
import com.mygame.screens.StartScreen;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Main Class of the Game.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    
    public static void main(String[] args) {
        try {
            Main application = new Main();
            AppSettings settings = new AppSettings(true);
            settings.setFrameRate(60); // set to less than or equal screen refresh rate
            settings.setVSync(true);   // prevents page tearing
            settings.setFrequency(60); // set to screen refresh rate
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            settings.setResolution(screenSize.width, screenSize.height);
            settings.setFullscreen(false);
            settings.setSamples(2);    // anti-aliasing
            settings.setTitle("CivilWar"); // branding: window name
            settings.setIcons(new BufferedImage[]{ImageIO.read(new File("assets/Interface/images/logo.png"))});
            application.setShowSettings(false); // or don't display splashscreen
            application.setDisplayFps(false);
            application.setDisplayStatView(false);
            application.setSettings(settings);
            application.start();
        } catch (IOException exception) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Icon missing.", exception);
        }
    }

    @Override
    public void simpleInitApp() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        NiftyScreenHelper.loadDefaults(nifty);
        NiftyScreenHelper.registerMenuSounds(nifty);
        StartScreen startScreen = new StartScreen();
        startScreen.initialize(stateManager, this);
        nifty.fromXml("Interface/start_screen.xml", "start_screen", startScreen);
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
    }    
}
