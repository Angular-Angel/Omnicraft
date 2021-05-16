/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gl.VertexBuffer;
import com.samrj.devil.gl.VertexBuilder;
import com.samrj.devil.math.Util;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.WorldGenerator;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class DebugClient implements Client {
    
    private static final float CAMERA_NEAR_Z = 0.125f;
    private static final float CAMERA_FAR_Z = 1024.0f;
    private static final float CAMERA_FOV = Util.toRadians(90.0f);
    
    private Vec2i resolution;
    private ShaderProgram shader;
    private Player player;
    private World world;
    
    public VertexBuffer buffer;
    public Vec3 vPos;
    public Vec2 vTexCoord;
    public VertexBuilder.IntAttribute block_palette_index;
    public Vec3 vRandom;
    
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
    
    @Override
    public void init() {
        try {
            
            player = new Player(CAMERA_NEAR_Z, CAMERA_FAR_Z, CAMERA_FOV);
            
            world = WorldGenerator.generateWorld();
            
            resolution = Game.getResolution();
            
            //This method loads shader.vert and shader.frag, as the vertex and
            //fragment shaders respectively.
            shader = DGL.loadProgram("resources/shader");
            
            //VertexBuffer is a static block of vertices, allocated once.
            //Could use VertexStream if we wanted something more dynamic.
            buffer = DGL.genVertexBuffer(7200000, -1);
            
            bufferVertices();
            
            Game.getMouse().setGrabbed(true);
            
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClearDepth(1.0);
            
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);

            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            
            glfwMaximizeWindow(Game.getWindow());
            
            world.prepare_palette();
            
            DGL.useProgram(shader);
        } catch (IOException ex) {
            Logger.getLogger(DebugClient.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    }

    @Override
    public void resize(int width, int height) {
        resolution.set(width, height);
        
        //Camera's aspect ratio may change if window is resized.
        player.camera.setFOV(resolution.x, resolution.y, CAMERA_FOV);
    }

    @Override
    public void step(float dt) {
        player.update(dt);
    }

    @Override
    public void render() {
        shader.uniformMat4("u_projection_matrix", player.camera.projMat);
        shader.uniformMat4("u_view_matrix", player.camera.viewMat);
        
        world.prepareShader(shader);
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        DGL.draw(buffer, GL_TRIANGLES);
    }
    
    public void bufferVertices() {
         //Set up the variable names used by the vertex shader. Each vertex can
        //have multiple kinds of data: floats, vectors, or matrices.
        vPos = buffer.vec3("in_pos");
        vTexCoord = buffer.vec2("in_tex_coord");
        block_palette_index = buffer.aint("in_block_palette_index");
        vRandom = buffer.vec3("in_random");

        buffer.begin();
        
        world.bufferOptimizedMesh(this);

        buffer.end();
    }

    @Override
    public void destroy(Boolean crashed) {
        
        world.delete();
        
        DGL.delete(shader, buffer);
        
        if (crashed) DGL.setDebugLeakTracking(false);

        DGL.destroy();
    }
}