/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gui.Align;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

/**
 *
 * @author angle
 */
public abstract class MenuScreen extends Screen {
    
    protected Window titleWindow;
    protected Window menuWindow;

    public MenuScreen(DebugClient client) {
        super(client);
    }
    
    protected void createTitleWindow(String string) {
        titleWindow = new Window();
        titleWindow.setTitleBarVisible(false);
        
        Text title = new Text(string);
        titleWindow.setContent(title);
        
        DUI.show(titleWindow);
    }
    
    
    protected abstract void createTitleWindow();
    
    protected abstract void createMenuWindow();

    @Override
    public void init() {
        createTitleWindow();
        createMenuWindow();
        
        resize(client.resolution.x, client.resolution.y);
    }

    @Override
    public void mouseMoved(float x, float y) {
        DUI.mouseMoved(x, y);
    }

    @Override
    public void mouseButton(int button, int action, int mods) {
        DUI.mouseButton(button, action, mods);
    }

    @Override
    public void key(int key, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) Game.stop();
    }

    @Override
    public void resize(int width, int height) {
        titleWindow.setSizeFromContent();
        titleWindow.setPosAlignToViewport(Align.N.vector());
        
        menuWindow.setSizeFromContent();
        menuWindow.setPosAlignToViewport(Align.S.vector());
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
        DUI.hide(titleWindow);
        DUI.hide(menuWindow);
    }
    
}
