/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.FBO;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.gui.Align;
import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.LayoutColumns;
import com.samrj.devil.gui.LayoutRows;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Window;
import com.samrj.devil.math.Mat4;
import com.samrj.devil.math.Util;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.angle.omnicraft.graphics.BlockBufferManager;
import net.angle.omnicraft.graphics.OutlineStreamManager;
import net.angle.omnicraft.graphics.SkyboxBufferManager;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.WorldGenerator;
import net.angle.omnicraft.world.blocks.Block;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11C.*;
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
    private Text lookDisplay;
    
    private Window waila;
    private Text blockName;
    
    private SkyboxBufferManager skybox;
    
    private OutlineStreamManager blockOutline;
    private OutlineStreamManager chunkOutline;
    private BlockBufferManager wailaBlockDisplay;
    private FBO wailaBlockBuffer;
    private Mat4 wailaBlockView;
    private UITexture wailaPreviewForm;
    
    private Texture2D wailaPreviewTexture;
    private Texture2D wailaDepthTexture;
    
    public ShaderProgram skyboxShader;
    
    private static final int previewWidth = 150, previewHeight = 150;
    
    public GameScreen(DebugClient client) {
        super(client);
        
        world = new World(new WorldGenerator());
            
        player = new Player(world);
    }
    

    @Override
    public void init() {
        try {
            skyboxShader = DGL.loadProgram("resources/skybox_shader");
        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        Game.getMouse().setGrabbed(true);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1.0);

        world.prepare_block_palette();
        world.prepare_side_palette();

        blockOutline = new OutlineStreamManager();
        blockOutline.begin();
        
        chunkOutline = new OutlineStreamManager();
        chunkOutline.begin();

        buildDebugWindow();
        buildWAILA();

        wailaBlockDisplay = new BlockBufferManager();
        
        skybox = new SkyboxBufferManager();
        
        Game.getMouse().setGrabbed(true);
    }
    
    private void buildDebugWindow() {
        debugWindow = new Window();
        debugWindow.setTitle("Debug Window");
        LayoutRows rows = new LayoutRows();
        debugWindow.setContent(rows);
        
        LayoutColumns columns = new LayoutColumns();
        rows.add(columns);
        
        Text text = new Text("FPS: ");
        columns.add(text);
        
        fpsNum = new Text("");
        columns.add(fpsNum);
        
        columns = new LayoutColumns();
        rows.add(columns);
        
        text = new Text("POS: ");
        columns.add(text);
        
        posDisplay = new Text("");
        columns.add(posDisplay);
        
        columns = new LayoutColumns();
        rows.add(columns);
        
        text = new Text("DIR: ");
        columns.add(text);
        
        dirDisplay = new Text("");
        columns.add(dirDisplay);
        
        columns = new LayoutColumns();
        rows.add(columns);
        
        text = new Text("LOOK: ");
        columns.add(text);
        
        lookDisplay = new Text("");
        columns.add(lookDisplay);
        
    }
    
    private void buildWAILA() {
        waila = new Window();
        waila.setTitleBarVisible(false);
        
        LayoutColumns columns = new LayoutColumns();
        waila.setContent(columns);
        
        wailaBlockView = Mat4.translation(new Vec3(-4.4f, -2.35f, -5));
        
        wailaBlockBuffer = DGL.genFBO();
        
        DGL.bindFBO(wailaBlockBuffer);
        
        wailaPreviewTexture = DGL.genTex2D();
        wailaPreviewTexture.image(previewWidth, previewHeight, GL_RGB8);
        wailaBlockBuffer.texture2D(wailaPreviewTexture, GL_COLOR_ATTACHMENT0);
        
        wailaDepthTexture = DGL.genTex2D();
        wailaDepthTexture.image(previewWidth, previewHeight, GL_DEPTH_COMPONENT16);
        wailaBlockBuffer.texture2D(wailaDepthTexture, GL_DEPTH_ATTACHMENT);
        
        wailaPreviewForm = new UITexture(wailaPreviewTexture, client.textureShader, previewWidth, previewHeight);
        columns.add(wailaPreviewForm);
        
        blockName = new Text("");
        columns.add(blockName);
        columns.setAllAlignments(Align.C.vector());
        
        waila.setSizeFromContent();
        waila.setPosAlignToViewport(Align.N.vector());
    }
    
    public void toggeDebugScreen() {
        if (debugWindow.isVisible())
            DUI.hide(debugWindow);
        else
            DUI.show(debugWindow);
    }
    
    @Override
    public void mouseMoved(float x, float y) {
        player.mouseMoved(x, y);
    }

    @Override
    public void mouseButton(int button, int action, int mods) {
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
        waila.setPosAlignToViewport(Align.N.vector());
        
        //Camera's aspect ratio may change if window is resized.
        player.camera.setFOV(width, height, Player.CAMERA_FOV);
    }
    
    private void updateDebugWindow() {
        if (!debugWindow.isVisible())
            return;
        fpsNum.setText("" + 1000000000l/Game.getLastFrameNano());
        posDisplay.setText(player.getApproximateVoxelPosition().toString());
        dirDisplay.setText(player.camera.forward.toString());
        if (player.pickedCoord != null)
            lookDisplay.setText(player.pickedCoord.toString());
        else
            lookDisplay.setText("None");
        debugWindow.setSizeFromContent();
        
        chunkOutline.streamBlockOutline(player.getChunkCoords().mult(16), 16);
    }
    
    private void updateWaila() {
        Block block = player.pickBlock(25);
        if (block != null) {
            blockOutline.streamBlockOutline(player.pickedCoord);
            
            blockName.setText(block.name);
            
            waila.setSizeFromContent();
            waila.setPosAlignToViewport(Align.N.vector());
            DUI.show(waila);
            
            wailaBlockDisplay.clearVertices();
            wailaBlockDisplay.begin(36, -1);
            Vec3 drawStart = new Vec3(-World.EDGE_LENGTH_OF_BLOCK/2.0f, -World.EDGE_LENGTH_OF_BLOCK/2.0f, -World.EDGE_LENGTH_OF_BLOCK/2.0f);
            Vec2 dimensions = new Vec2(1, 1);
            wailaBlockDisplay.bufferFace(block, world.side_ids.get(0), Block.BlockFace.front, dimensions, drawStart);
            wailaBlockDisplay.bufferFace(block, world.side_ids.get(0), Block.BlockFace.back, dimensions, drawStart);
            wailaBlockDisplay.bufferFace(block, world.side_ids.get(0), Block.BlockFace.left, dimensions, drawStart);
            wailaBlockDisplay.bufferFace(block, world.side_ids.get(0), Block.BlockFace.right, dimensions, drawStart);
            wailaBlockDisplay.bufferFace(block, world.side_ids.get(0), Block.BlockFace.top, dimensions, drawStart);
            wailaBlockDisplay.bufferFace(block, world.side_ids.get(0), Block.BlockFace.bottom, dimensions, drawStart);
            wailaBlockDisplay.upload();
        } else {
            DUI.hide(waila);
        }
    }
    
    private void bufferSkybox() {
        skybox.clearVertices();
        wailaBlockDisplay.begin(36, -1);
        
        wailaBlockDisplay.upload();
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
        
        skybox.draw();
        
        world.draw();
        
        if (waila.isVisible()) {
            DGL.useProgram(client.outlineShader);
            client.outlineShader.uniformMat4("u_projection_matrix", player.camera.projMat);
            client.outlineShader.uniformMat4("u_view_matrix", player.camera.viewMat);
            blockOutline.draw();
            
            DGL.bindFBO(wailaBlockBuffer);
            
            DGL.useProgram(client.blockShader);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            client.blockShader.uniformMat4("u_projection_matrix", 
                    Mat4.perspective(Util.toRadians(90.0f), client.resolution.y/(float) client.resolution.x, 0.5f, 16));
            client.blockShader.uniformMat4("u_view_matrix", wailaBlockView);
            wailaBlockDisplay.draw();
            
            DGL.bindFBO(null);
        }
        
        if (debugWindow.isVisible()) {
            DGL.useProgram(client.outlineShader);
            client.outlineShader.uniformMat4("u_projection_matrix", player.camera.projMat);
            client.outlineShader.uniformMat4("u_view_matrix", player.camera.viewMat);
            chunkOutline.draw();
        }
        
        DUI.render();
    }
    
    @Override
    public void destroy(Boolean crashed) {
        world.delete();
        blockOutline.clearVertices();
        wailaBlockDisplay.clearVertices();
        wailaPreviewForm.destroy();
        
        DGL.delete(wailaDepthTexture, wailaPreviewTexture, wailaBlockBuffer);
    }
}
