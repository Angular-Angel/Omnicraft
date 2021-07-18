/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.gui.Button;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.LayoutColumns;
import com.samrj.devil.gui.LayoutRows;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;

/**
 *
 * @author angle
 */
public class WorldSelectScreen extends MenuScreen {

    public WorldSelectScreen(DebugClient client) {
        super(client);
    }
    
    @Override
    protected void createTitleWindow() {
        createTitleWindow("Select World");
    }
    
    @Override
    protected void createMenuWindow() {
        menuWindow = new Window();
        menuWindow.setTitleBarVisible(false);
        
        LayoutRows rows = new LayoutRows();
        menuWindow.setContent(rows);
        
        LayoutColumns columns = new LayoutColumns();
        rows.add(columns);
        
        Button button = new Button("Play");
        columns.add(button);
        
        button.setCallback((t) -> {
            if (client.screen == this)
                client.changeScreen(new GameScreen(client));
        });
        
        button = new Button("New");
        columns.add(button);
        
        button.setCallback((t) -> {
            if (client.screen == this)
                client.changeScreen(new WorldGenScreen(client));
        });
        
        DUI.show(menuWindow);
    }
    
}
