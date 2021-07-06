/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.Font;
import com.samrj.devil.math.Vec2i;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;


public class DebugClient implements Client {
    
    public Vec2i resolution;
    public ShaderProgram blockShader;
    public ShaderProgram outlineShader;
    public ShaderProgram textureShader;
    
    private Screen screen;
    
    @Override
    public void preInit() {
        Game.setDebug(true);
            
        //OpenGL context should be forward-compatible, i.e. one where all
        //functionality deprecated in the requested version of OpenGL is
        //removed. In the core profile, immediate mode OpenGL is deprecated.
        Game.hint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        Game.hint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        //OpenGL 3.2 gives good access to most modern features, and is
        //supported by most hardware.
        Game.hint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        Game.hint(GLFW_CONTEXT_VERSION_MINOR, 2);
        Game.hint(GLFW_SAMPLES, 4);

        Game.setFullscreen(false);
        Game.setVsync(true);
        Game.setTitle("Omnicraft");
    }
    
    public void beginGame() {
        try {
            
            resolution = Game.getResolution();
            
            //This method loads shader.vert and shader.frag, as the vertex and
            //fragment shaders respectively.
            blockShader = DGL.loadProgram("resources/block_shader");
            outlineShader = DGL.loadProgram("resources/outline_shader");
            textureShader = DGL.loadProgram("resources/texture_shader");
            
            //VertexBuffer is a static block of vertices, allocated once.
            //Could use VertexStream if we wanted something more dynamic.
            
            Game.getMouse().setGrabbed(true);
            
            DUI.setFont(new Font(new FileInputStream("resources/Helvetica-Normal.ttf")));
            
            screen = new GameScreen(this);
        } catch (IOException ex) {
            Logger.getLogger(DebugClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void init() {
        beginGame();
    }
    
    public static void main(String args[]) {
        Client client = new DebugClient();
        client.run();
    }
    
    @Override
    public void mouseMoved(float x, float y) {
        screen.mouseMoved(x, y);
    }
    
    @Override
    public void key(int key, int action, int mods) {
        screen.key(key, action, mods);
    }
    
    @Override
    public void resize(int width, int height) {
        resolution.set(width, height);
        screen.resize(width, height);
    }
    
    @Override
    public void step(float dt) {
        screen.step(dt);
    }
    
    @Override
    public void render() {
        screen.render();
    }
    
    @Override
    public void destroy(Boolean crashed) {
        screen.destroy(crashed);
        
        DGL.delete(blockShader, outlineShader);
        DUI.font().destroy();
        
        if (crashed) DGL.setDebugLeakTracking(false);

        DUI.destroy();
        DGL.destroy();
    }
}