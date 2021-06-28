/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gl.VertexStream;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.Font;
import com.samrj.devil.gui.LayoutColumns;
import com.samrj.devil.gui.LayoutRows;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.angle.omnicraft.graphics.OutlineStreamManager;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.WorldGenerator;
import net.angle.omnicraft.world.blocks.Block;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class DebugClient implements Client {
    
    private Vec2i resolution;
    private ShaderProgram blockShader;
    private ShaderProgram outlineShader;
    
    private Player player;
    private World world;
    
    private Window debugWindow;
    private Text fpsNum;
    
    private Window waila;
    private Text blockName;
    
    private OutlineStreamManager blockOutline;
    
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
    
    public void buildDebugWindow() {
        debugWindow = new Window();
        debugWindow.setTitle("Debug Window");
        LayoutRows rows = new LayoutRows();
        debugWindow.setContent(rows);
        
        LayoutColumns columns = new LayoutColumns();
        rows.add(columns);
        
        
        Text fps = new Text("FPS: ");
        columns.add(fps);
        
        fpsNum = new Text("");
        columns.add(fpsNum);
    }
    
    public void buildWAILA() {
        waila = new Window();
        waila.setTitleBarVisible(false);
        
        waila.setWidth(300f);
        waila.setHeight(75f);
        waila.setPosAlignToViewport(new Vec2(0.5f, 1));
        
        LayoutColumns columns = new LayoutColumns();
        waila.setContent(columns);
        
        blockName = new Text("");
        columns.add(blockName);
    }
    
    public void beginGame() {
        try {
            world = new World(new WorldGenerator());
            
            player = new Player(world);
            
            resolution = Game.getResolution();
            
            //This method loads shader.vert and shader.frag, as the vertex and
            //fragment shaders respectively.
            blockShader = DGL.loadProgram("resources/block_shader");
            outlineShader = DGL.loadProgram("resources/outline_shader");
            
            //VertexBuffer is a static block of vertices, allocated once.
            //Could use VertexStream if we wanted something more dynamic.
            
            Game.getMouse().setGrabbed(true);
            
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClearDepth(1.0);
            
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);
            
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            
            glfwMaximizeWindow(Game.getWindow());
            
            world.prepare_block_palette();
            world.prepare_side_palette();
            
            DUI.setFont(new Font(new FileInputStream("resources/Helvetica-Normal.ttf")));
            
            buildDebugWindow();
            buildWAILA();
            
            blockOutline = new OutlineStreamManager();
            blockOutline.begin();
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
        player.mouseMoved(x, y);
    }
    
    @Override
    public void key(int key, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) Game.stop();
        if (key == GLFW_KEY_F3 && action == GLFW_PRESS) {
            if (debugWindow.isVisible())
                DUI.hide(debugWindow);
            else
                DUI.show(debugWindow);
        }
    }
    
    @Override
    public void resize(int width, int height) {
        resolution.set(width, height);
        waila.setPosAlignToViewport(new Vec2(0.5f, 1));
        
        //Camera's aspect ratio may change if window is resized.
        player.camera.setFOV(resolution.x, resolution.y, player.CAMERA_FOV);
    }
    
    @Override
    public void step(float dt) {
        player.update(dt);
        world.update(dt);
        fpsNum.setText("" + 1000000000l/Game.getLastFrameNano());
        Block block = player.pickBlock(25);
        if (block != null) {
            blockName.setText(block.name);
            DUI.show(waila);
            blockOutline.streamBlockOutline(player.pickedCoord);
        } else {
            DUI.hide(waila);
        }
    }
    
    @Override
    public void render() {
        DGL.useProgram(blockShader);
        blockShader.uniformMat4("u_projection_matrix", player.camera.projMat);
        blockShader.uniformMat4("u_view_matrix", player.camera.viewMat);
        
        world.prepareShader(blockShader);
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        world.draw();
        
        DUI.render();
        if (waila.isVisible()) {
            DGL.useProgram(outlineShader);
            outlineShader.uniformMat4("u_projection_matrix", player.camera.projMat);
            outlineShader.uniformMat4("u_view_matrix", player.camera.viewMat);
            blockOutline.draw();
        }
    }
    
    @Override
    public void destroy(Boolean crashed) {
        world.delete();
        blockOutline.clearVertices();
        
        DGL.delete(blockShader, outlineShader);
        DUI.font().destroy();
        
        if (crashed) DGL.setDebugLeakTracking(false);

        DUI.destroy();
        DGL.destroy();
    }
}