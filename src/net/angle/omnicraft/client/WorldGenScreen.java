/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.gui.Window;

/**
 *
 * @author angle
 */
public class WorldGenScreen extends MenuScreen {

    public WorldGenScreen(DebugClient client) {
        super(client);
    }
    
    @Override
    protected void createTitleWindow() {
        createTitleWindow("Generate World");
    }

    @Override
    protected void createMenuWindow() {
        menuWindow = new Window();
        menuWindow.setTitleBarVisible(false);
    }
    
}
