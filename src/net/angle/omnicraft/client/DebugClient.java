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
import static org.lwjgl.opengl.GL11C.*;


public class DebugClient implements Client {
    
    private static final float CAMERA_NEAR_Z = 0.125f;
    private static final float CAMERA_FAR_Z = 1024.0f;
    private static final float CAMERA_FOV = Util.toRadians(90.0f);
    public static final float DRAW_OFFSET = 0.5f;
    
    private Vec2i resolution;
    private ShaderProgram shader;
    private VertexBuffer buffer;
    private Player player;
    private World world;
    
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
            shader = DGL.loadProgram("resources/dirt_shader");
            
            //VertexBuffer is a static block of vertices, allocated once.
            //Could use VertexStream if we wanted something more dynamic.
            buffer = DGL.genVertexBuffer(36, -1);
            
            //Set up the variable names used by the vertex shader. Each vertex can
            //have multiple kinds of data: floats, vectors, or matrices.
            Vec3 vPos = buffer.vec3("in_pos");
            Vec2 vTexCoord = buffer.vec2("in_tex_coord");
            
            buffer.begin();
            
            bufferFlatVertices(buffer, vPos, vTexCoord, DRAW_OFFSET, DRAW_OFFSET, -DRAW_OFFSET, -1, 0, 1);
            bufferFlatVertices(buffer, vPos, vTexCoord, DRAW_OFFSET, -DRAW_OFFSET, DRAW_OFFSET, -1, 0, -1);
            bufferFlatVertices(buffer, vPos, vTexCoord, DRAW_OFFSET, DRAW_OFFSET, DRAW_OFFSET, -1, -1, 0);
            bufferFlatVertices(buffer, vPos, vTexCoord, -DRAW_OFFSET, DRAW_OFFSET, -DRAW_OFFSET, 1, -1, 0);
            bufferFlatVertices(buffer, vPos, vTexCoord, -DRAW_OFFSET, DRAW_OFFSET, DRAW_OFFSET, 0, -1, -1);
            bufferFlatVertices(buffer, vPos, vTexCoord, DRAW_OFFSET, DRAW_OFFSET, -DRAW_OFFSET, 0, -1, 1);
            
            buffer.end();
            
            Game.getMouse().setGrabbed(true);
            
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClearDepth(1.0);
            
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);

            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
            
            glfwMaximizeWindow(Game.getWindow());
            
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
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        DGL.draw(buffer, GL_TRIANGLES);
    }
    
    public void bufferFlatVertices(VertexBuffer buffer, Vec3 vPos, Vec2 vTexCoord, float startx, float starty, float startz, float xoff, float yoff, float zoff) {

        //Build a square out of two triangles.

        Vec3 topLeft, topRight, bottomLeft, bottomRight;

        topLeft = new Vec3(0, 0, 0);

        if (xoff == 0) {
            topRight = new Vec3(0, 0, zoff);
        } else {
            topRight = new Vec3(xoff, 0, 0);
        }

        bottomRight = new Vec3(xoff, yoff, zoff);

        if (yoff == 0) {
            bottomLeft = new Vec3(0, 0, zoff);
        } else{
            bottomLeft = new Vec3(0, yoff, 0);
        }
        
        //adjust positions for where our starts are.
        topLeft.add(new Vec3(startx, starty, startz));
        topRight.add(new Vec3(startx, starty, startz));
        bottomLeft.add(new Vec3(startx, starty, startz));
        bottomRight.add(new Vec3(startx, starty, startz));

        //add first trangle, starting at top left corner, then top right, then bottom right
        vPos.set(topLeft); vTexCoord.set(0.0f, 0.0f); buffer.vertex();
        vPos.set(topRight); vTexCoord.set(1.0f, 0.0f); buffer.vertex();
        vPos.set(bottomRight); vTexCoord.set(1.0f, 1.0f); buffer.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        vPos.set(topLeft); vTexCoord.set(0.0f, 0.0f); buffer.vertex();
        vPos.set(bottomRight); vTexCoord.set(1.0f, 1.0f); buffer.vertex();
        vPos.set(bottomLeft); vTexCoord.set(0.0f, 1.0f); buffer.vertex();
    }

    @Override
    public void destroy(Boolean crashed) {
        
        DGL.delete(shader, buffer);
        
        if (crashed) DGL.setDebugLeakTracking(false);

        DGL.destroy();
    }
}