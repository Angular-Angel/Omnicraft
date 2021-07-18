/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.gui.Align;
import com.samrj.devil.gui.Button;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.Font;
import com.samrj.devil.gui.LayoutRows;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author angle
 */
public class TitleScreen extends MenuScreen {
    
    private Font titleFont;
    
    public TitleScreen(DebugClient client) {
        super(client);
    }
    
    @Override
    protected void createTitleWindow() {
        try {
            Font.FontProperties titleProperties = new Font.FontProperties();
            titleProperties.bitmapWidth = 1600;
            titleProperties.bitmapHeight = 1800;
            titleProperties.height = 100;
            titleProperties.first = 65;
            titleProperties.count = 57;

            titleFont = new Font(new FileInputStream("resources/Helvetica-Normal.ttf"), titleProperties);

            titleWindow = new Window();
            titleWindow.setTitleBarVisible(false);

            Text title = new TextWithFont("OMNICRAFT", titleFont);
            titleWindow.setContent(title);

            DUI.show(titleWindow);
        } catch (IOException ex) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void createMenuWindow() {
        menuWindow = new Window();
        menuWindow.setTitleBarVisible(false);
        
        LayoutRows rows = new LayoutRows();
        menuWindow.setContent(rows);
        
        Button button = new Button("Local Game");
        rows.add(button);
        
        button.setCallback((t) -> {
            if (client.screen == this)
                client.changeScreen(new WorldSelectScreen(client));
        });
        
        button = new Button("Network Game");
        rows.add(button);
        
        DUI.show(menuWindow);
    }

    @Override
    public void resize(int width, int height) {
        titleWindow.setSizeFromContent();
        titleWindow.setPosAlignToViewport(Align.N.vector());
        
        menuWindow.setSizeFromContent();
        menuWindow.setPosAlignToViewport(Align.C.vector());
    }

    @Override
    public void destroy(Boolean crashed) {
        super.destroy(crashed);
        titleFont.destroy();
    }
    
}
