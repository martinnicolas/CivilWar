/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.screens;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import com.mygame.Main;
import com.mygame.levels.Level;
import com.mygame.levels.Level1;
import com.mygame.levels.LevelLoaderAppState;

/**
 *
 * @author martin
 */
public class LoadingScreen extends AbstractScreen implements ScreenController {
    
    private boolean loadingStarted;
    private Level loadedLevel;
    private Element progressBar;
    private Element progressText;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.setApp((Main) app);
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.setNifty(nifty);
        this.setScreen(screen);
        this.setProgressBar(screen.findElementById("progress_bar"));
        this.setProgressText(screen.findElementById("loading_percent"));
        this.setProgress(0f);
    }

    @Override
    public void onStartScreen() {
        if (!this.isLoadingStarted()) {
            this.setLoadingStarted(true);
            this.load(new Level1());
        }
    }

    @Override
    public void onEndScreen() {
        if (this.getLoadedLevel() != null) {
            this.getLoadedLevel().startLevel();
            this.setLoadedLevel(null);
        }
        this.getApp().getStateManager().detach(this);
    }
    
    /**
     * Creates an AppState that loads the Level while this screen is visible.
     * @param level
     */
    private void load(Level level) {
        this.getApp().getStateManager().attach(new LevelLoaderAppState(this, level));
    }

    /**
     * Close the loading screen and start the loaded Level once Nifty ends it.
     * @param level loaded Level
     */
    public void finishLoading(Level level) {
        this.setLoadedLevel(level);
        this.close();
    }
    
    /**
     * Close loading screen.
     */
    public void close() {
        this.getNifty().exit();
    }
    
    /**
     * Update loading progress bar.
     * @param progress value between 0 and 1
     */
    public void setProgress(float progress) {
        int percent = Math.round(Math.max(0f, Math.min(1f, progress)) * 100f);
        if (this.getProgressBar() != null) {
            this.getProgressBar().setConstraintWidth(new SizeValue(percent + "%"));
            this.getProgressBar().getParent().layoutElements();
        }
        if (this.getProgressText() != null) {
            this.getProgressText().getRenderer(TextRenderer.class).setText(percent + "%");
        }
    }
    
    public boolean isLoadingStarted() {
        return loadingStarted;
    }

    public void setLoadingStarted(boolean loadingStarted) {
        this.loadingStarted = loadingStarted;
    }

    public Level getLoadedLevel() {
        return loadedLevel;
    }

    public void setLoadedLevel(Level loadedLevel) {
        this.loadedLevel = loadedLevel;
    }

    public Element getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(Element progressBar) {
        this.progressBar = progressBar;
    }

    public Element getProgressText() {
        return progressText;
    }

    public void setProgressText(Element progressText) {
        this.progressText = progressText;
    }
    
}
