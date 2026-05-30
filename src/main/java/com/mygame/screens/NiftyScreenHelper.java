/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygame.screens;

import de.lessvoid.nifty.Nifty;

/**
 *
 * @author martin
 */
public final class NiftyScreenHelper {

    private static final String DEFAULT_CONTROLS = "nifty-default-controls.xml";
    private static final String DEFAULT_STYLES = "nifty-default-styles.xml";
    private static final String MENU_HOVER_SOUND_ID = "menu_hover";
    private static final String MENU_HOVER_SOUND_PATH = "Sounds/Effects/menu_hover.wav";

    private NiftyScreenHelper() {
    }

    public static void loadDefaults(Nifty nifty) {
        nifty.loadControlFile(NiftyScreenHelper.DEFAULT_CONTROLS);
        nifty.loadStyleFile(NiftyScreenHelper.DEFAULT_STYLES);
    }

    public static void registerMenuSounds(Nifty nifty) {
        nifty.registerSound(NiftyScreenHelper.MENU_HOVER_SOUND_ID, NiftyScreenHelper.MENU_HOVER_SOUND_PATH);
    }

}
