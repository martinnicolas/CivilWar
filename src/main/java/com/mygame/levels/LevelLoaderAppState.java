/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.mygame.screens.LoadingScreen;

/**
 *
 * @author martin
 */
public class LevelLoaderAppState extends AbstractAppState {

    private final LoadingScreen loadingScreen;
    private final Level level;
    private AppStateManager loaderStateManager;
    private boolean loadingScreenRendered;
    private boolean loadingStarted;
    private boolean loadingFinished;

    public LevelLoaderAppState(LoadingScreen loadingScreen, Level level) {
        this.loadingScreen = loadingScreen;
        this.level = level;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.setLoaderStateManager(stateManager);
        this.getLoaderStateManager().attach(this.getLevel());
    }

    @Override
    public void update(float tpf) {
        if (this.isLoadingFinished()) {
            return;
        }
        if (!this.getLevel().isInitialized()) {
            return;
        }
        if (!this.isLoadingScreenRendered()) {
            this.setLoadingScreenRendered(true);
            return;
        }
        if (!this.isLoadingStarted()) {
            this.setLoadingStarted(true);
        }
        if (this.getLevel().loadLevel()) {
            this.getLoadingScreen().setProgress(this.getLevel().getLoadingProgress());
            this.setLoadingFinished(true);
            this.getLoaderStateManager().detach(this);
            this.getLoadingScreen().finishLoading(this.getLevel());
        } else {
            this.getLoadingScreen().setProgress(this.getLevel().getLoadingProgress());
        }
    }

    public LoadingScreen getLoadingScreen() {
        return loadingScreen;
    }

    public Level getLevel() {
        return level;
    }

    public AppStateManager getLoaderStateManager() {
        return loaderStateManager;
    }

    public void setLoaderStateManager(AppStateManager loaderStateManager) {
        this.loaderStateManager = loaderStateManager;
    }

    public boolean isLoadingScreenRendered() {
        return loadingScreenRendered;
    }

    public void setLoadingScreenRendered(boolean loadingScreenRendered) {
        this.loadingScreenRendered = loadingScreenRendered;
    }

    public boolean isLoadingStarted() {
        return loadingStarted;
    }

    public void setLoadingStarted(boolean loadingStarted) {
        this.loadingStarted = loadingStarted;
    }

    public boolean isLoadingFinished() {
        return loadingFinished;
    }

    public void setLoadingFinished(boolean loadingFinished) {
        this.loadingFinished = loadingFinished;
    }

}
