/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.FBO;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.Font;
import com.samrj.devil.gui.LayoutColumns;
import com.samrj.devil.gui.LayoutRows;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;
import com.samrj.devil.math.Mat4;
import com.samrj.devil.math.Util;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.angle.omnicraft.graphics.BlockBufferManager;
import net.angle.omnicraft.graphics.OutlineStreamManager;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.WorldGenerator;
import net.angle.omnicraft.world.blocks.Block;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL14C.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL30C.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30C.GL_DEPTH_ATTACHMENT;

/**
 *
 * @author angle
 */
public class GameScreen extends Screen {
    private final Player player;
    private final World world;
    
    private Window debugWindow;
    private Text fpsNum;
    private Text posDisplay;
    private Text dirDisplay;
    
    private Window waila;
    private Text blockName;
    
    private final OutlineStreamManager blockOutline;
    private final BlockBufferManager wailaBlockDisplay;
    private FBO wailaBlockBuffer;
    private Mat4 wailaBlockView;
    
    private Texture2D wailaPreviewTexture;
    private Texture2D wailaDepthTexture;
    
    private void buildDebugWindow() {
        debugWindow = new Window();
        debugWindow.setTitle("Debug Window");
        debugWindow.setWidth(500);
        LayoutRows rows = new LayoutRows();
        debugWindow.setContent(rows);
        
        LayoutColumns columns = new LayoutColumns();
        rows.add(columns);
        
        Text fps = new Text("FPS: ");
        columns.add(fps);
        
        fpsNum = new Text("");
        columns.add(fpsNum);
        
        columns = new LayoutColumns();
        rows.add(columns);
        
        Text pos = new Text("POS: ");
        columns.add(pos);
        
        posDisplay = new Text("");
        columns.add(posDisplay);
        
        columns = new LayoutColumns();
        rows.add(columns);
        
        Text dir = new Text("DIR: ");
        columns.add(dir);
        
        dirDisplay = new Text("");
        columns.add(dirDisplay);
        
    }
    
    private void buildWAILA() {
        waila = new Window();
        waila.setTitleBarVisible(false);
        
        waila.setWidth(300f);
        waila.setHeight(75f);
        waila.setPosAlignToViewport(new Vec2(0.5f, 1));
        
        LayoutColumns columns = new LayoutColumns();
        waila.setContent(columns);
        
        blockName = new Text("");
        columns.add(blockName);
        
        wailaBlockView = Mat4.translation(new Vec3(1, -1, -3)) 
                .rotate(new Vec3(0, 1, 0), Util.toRadians(20.0f))
                .rotate(new Vec3(1, 0, 0), Util.toRadians(10.0f));
        
        wailaBlockBuffer = DGL.genFBO();
        
        DGL.bindFBO(wailaBlockBuffer);
        
        wailaPreviewTexture = DGL.genTex2D();
        wailaPreviewTexture.image(500, 500, GL_RGB8);
        wailaBlockBuffer.texture2D(wailaPreviewTexture, GL_COLOR_ATTACHMENT0);
        
        wailaDepthTexture = DGL.genTex2D();
        wailaDepthTexture.image(500, 500, GL_DEPTH_COMPONENT16);
        wailaBlockBuffer.texture2D(wailaDepthTexture, GL_DEPTH_ATTACHMENT);
    }
    
    public void toggeDebugScreen() {
        if (debugWindow.isVisible())
            DUI.hide(debugWindow);
        else
            DUI.show(debugWindow);
    }
    
    public GameScreen(DebugClient client) {
        super(client);
        
        world = new World(new WorldGenerator());
            
        player = new Player(world);

        //VertexBuffer is a static block of vertices, allocated once.
        //Could use VertexStream if we wanted something more dynamic.

        Game.getMouse().setGrabbed(true);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1.0);

        glfwMaximizeWindow(Game.getWindow());

        world.prepare_block_palette();
        world.prepare_side_palette();

        blockOutline = new OutlineStreamManager();
        blockOutline.begin();

        buildDebugWindow();
        buildWAILA();

        wailaBlockDisplay = new BlockBufferManager();
    }
    
    @Override
    public void mouseMoved(float x, float y) {
        player.mouseMoved(x, y);
    }
    
    @Override
    public void key(int key, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) Game.stop();
        if (key == GLFW_KEY_F3 && action == GLFW_PRESS) {
            toggeDebugScreen();
        }
    }
    
    @Override
    public void resize(int width, int height) {
        waila.setPosAlignToViewport(new Vec2(0.5f, 1));
        
        //Camera's aspect ratio may change if window is resized.
        player.camera.setFOV(width, height, Player.CAMERA_FOV);
    }
    
    private void updateDebugWindow() {
        fpsNum.setText("" + 1000000000l/Game.getLastFrameNano());
        posDisplay.setText(player.getPositionInRegion().toString());
        dirDisplay.setText(player.camera.forward.toString());
    }
    
    private void updateWaila() {
        Block block = player.pickBlock(25);
        if (block != null) {
            blockName.setText(block.name);
            DUI.show(waila);
            blockOutline.streamBlockOutline(player.getRegion(), player.pickedCoord);
            wailaBlockDisplay.clearVertices();
            wailaBlockDisplay.begin(36, -1);
            Vec3 drawStart = new Vec3(-World.EDGE_LENGTH_OF_BLOCK/2.0f, -World.EDGE_LENGTH_OF_BLOCK/2.0f, -World.EDGE_LENGTH_OF_BLOCK/2.0f);
            Vec2 dimensions = new Vec2(1, 1);
            wailaBlockDisplay.BufferFace(block, world.side_ids.get(0), Block.BlockFace.front, dimensions, drawStart);
            wailaBlockDisplay.BufferFace(block, world.side_ids.get(0), Block.BlockFace.back, dimensions, drawStart);
            wailaBlockDisplay.BufferFace(block, world.side_ids.get(0), Block.BlockFace.left, dimensions, drawStart);
            wailaBlockDisplay.BufferFace(block, world.side_ids.get(0), Block.BlockFace.right, dimensions, drawStart);
            wailaBlockDisplay.BufferFace(block, world.side_ids.get(0), Block.BlockFace.top, dimensions, drawStart);
            wailaBlockDisplay.BufferFace(block, world.side_ids.get(0), Block.BlockFace.bottom, dimensions, drawStart);
            wailaBlockDisplay.upload();
        } else {
            DUI.hide(waila);
        }
    }
    
    @Override
    public void step(float dt) {
        player.update(dt);
        world.update(dt);
        updateDebugWindow();
        updateWaila();
    }
    
    @Override
    public void render() {
        
        DGL.bindFBO(null);
            
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1.0);
            
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        
        DGL.useProgram(client.blockShader);
        client.blockShader.uniformMat4("u_projection_matrix", player.camera.projMat);
        client.blockShader.uniformMat4("u_view_matrix", player.camera.viewMat);
        
        world.prepareShader(client.blockShader);
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        world.draw();
        
        DUI.render();
        if (waila.isVisible()) {
            
            DGL.useProgram(client.outlineShader);
            client.outlineShader.uniformMat4("u_projection_matrix", player.camera.projMat);
            client.outlineShader.uniformMat4("u_view_matrix", player.camera.viewMat);
            blockOutline.draw();
            
            DGL.bindFBO(wailaBlockBuffer);
            
            DGL.useProgram(client.blockShader);
            client.blockShader.uniformMat4("u_projection_matrix", 
                    Mat4.perspective(Util.toRadians(90.0f), 
                            client.resolution.y/(float) client.resolution.x, 0.5f, 16));
            client.blockShader.uniformMat4("u_view_matrix", wailaBlockView);
            wailaBlockDisplay.draw();
        }
    }
    
    @Override
    public void destroy(Boolean crashed) {
        world.delete();
        blockOutline.clearVertices();
        wailaBlockDisplay.clearVertices();
        
        DGL.delete(wailaDepthTexture, wailaPreviewTexture, wailaBlockBuffer);
    }
}
