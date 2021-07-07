/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.Font;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;
import com.samrj.devil.math.Vec2;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

/**
 *
 * @author angle
 */
public class TitleScreen extends Screen {
    
    private Window TitleWindow;
    
    private Font normalFont;
    private Font titleFont;

    public TitleScreen(DebugClient client) {
        super(client);
        
        normalFont = DUI.font();
        try {
            Font.FontProperties titleProperties = new Font.FontProperties();
            titleProperties.bitmapWidth = 1600;
            titleProperties.bitmapHeight = 1800;
            titleProperties.height = 100;
            titleProperties.first = 65;
            titleProperties.count = 57;
            titleFont = new Font(new FileInputStream("resources/Helvetica-Normal.ttf"), titleProperties);
        } catch (IOException ex) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TitleWindow = new Window();
        TitleWindow.setTitleBarVisible(false);
        
        Text title = new TextWithFont("OMNICRAFT", titleFont);
        TitleWindow.setContent(title);
        TitleWindow.setSizeFromContent();
        TitleWindow.setPosAlignToViewport(new Vec2(0.5f, 1));
        
        DUI.show(TitleWindow);
    }

    @Override
    public void mouseMoved(float x, float y) {
    }

    @Override
    public void key(int key, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) Game.stop();
    }

    @Override
    public void resize(int width, int height) {
        TitleWindow.setWidth(width - 2);
        TitleWindow.setHeight(height - 2);
        TitleWindow.setPosCenterViewport();
    }

    @Override
    public void step(float dt) {
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        DUI.render();
    }

    @Override
    public void destroy(Boolean crashed) {
    }
    
}
