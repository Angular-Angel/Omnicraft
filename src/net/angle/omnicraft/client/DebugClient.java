/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.graphics.Camera3D;
import com.samrj.devil.math.Util;
import com.samrj.devil.math.Vec2i;
import java.awt.Color;
import java.io.IOException;
import net.angle.omnicraft.pixel.GreyVariedPixelSource;
import net.angle.omnicraft.world.types.MineralGrain;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.SoilBlock;
import net.angle.omnicraft.world.types.SoilFraction;
import net.angle.omnicraft.world.types.SoilType;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryStack;

/**
 *
 * @author angle
 */
public class DebugClient {
    
    float     rtri;                       // Angle For The Triangle
    float     rquad;                      // Angle For The Quad
    
    private Camera3D camera;
    private SoilBlock block;
    private float cameraAngle;
    private Player player;
    
    private class InitCallback implements Game.InitCallback {

        @Override
        public void init() throws IOException {
            /*glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            
            Vec2i resolution = Game.getResolution();
            
            gluPerspective(45.0f, (float)resolution.x / (float)resolution.y, 0.1f ,100.0f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            
            glShadeModel(GL_SMOOTH);                        // Enables Smooth Shading
            glClearDepth(1.0f);                         // Depth Buffer Setup
            glEnable(GL_DEPTH_TEST);                        // Enables Depth Testing
            glDepthFunc(GL_LEQUAL);                         // The Type Of Depth Test To Do
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);          // Really Nice Perspective Calculations*/
            
            DGL.init();
 
            camera = new Camera3D(0.1f, 100.0f, Util.toRadians(90.0f), 1.0f);
            player = new Player(camera);
            
            block = new SoilBlock(new SoilType(new SoilFraction(new MineralGrain(new Mineraloid(new GreyVariedPixelSource(Color.darkGray, 60)), 1.0f), 100.0f)));

            glEnable(GL_TEXTURE_2D);
            
            glShadeModel(GL_SMOOTH);                        // Enables Smooth Shading
            glClearDepth(1.0f);                         // Depth Buffer Setup
            glEnable(GL_DEPTH_TEST);                        // Enables Depth Testing
            glDepthFunc(GL_LEQUAL);                         // The Type Of Depth Test To Do
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);          // Really Nice Perspective Calculations*/
            
            Game.getMouse().setGrabbed(true);
            Game.setVsync(true);
        }
        
    }
    
    private class StepCallback implements Game.StepCallback {
        
        @Override
        public void step(float dt) {
            player.update(dt);
            if (Game.getKeyboard().isKeyDown(GLFW_KEY_ESCAPE)){
                Game.stop();
            }
            
            //cameraAngle += dt;
            //camera.dir.setRotation(new Vec3(0.0f, 1.0f, 0.0f), cameraAngle);
            //camera.dir.rotate(new Vec3(1.0f, 0.0f, 0.0f), -Util.toRadians(10.0f));

            float cameraX = 5.0f*(float)Math.sin(cameraAngle);
            float cameraZ = 5.0f*(float)Math.cos(cameraAngle);
            //camera.pos.set(cameraX, 1.0f, cameraZ);
        }
        
    }
    
    private class RenderCallback implements Runnable {
        
        @Override
        public void run() {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            Vec2i resolution = Game.getResolution();
            camera.setFOV(resolution.x, resolution.y, Util.toRadians(90.0f));

            //I need to add matrix toArray functions to make this easier. -Sam
            try (MemoryStack stack = MemoryStack.stackPush())
            {
                glMatrixMode(GL_PROJECTION);
                glLoadMatrixf(camera.projMat.mallocFloat(stack));
                glMatrixMode(GL_MODELVIEW);
                glLoadMatrixf(camera.viewMat.mallocFloat(stack));
            }
            
            block.draw();
            
            /*glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            drawPyramid();
            
            drawCube();
            
            rtri+=0.2f;                     // Increase The Rotation Variable For The Triangle ( NEW )
            rquad-=0.15f;                       // Decrease The Rotation Variable For The Quad     ( NEW )*/
        }
        
    }
    
    public static void gluPerspective(float fovy, float aspect, float near, float far) {
        float bottom = -near * (float) Math.tan(fovy / 2);
        float top = -bottom;
        float left = aspect * bottom;
        float right = -left;
        glFrustum(left, right, bottom, top, near, far);
    }
    
    public DebugClient() {}
    
    public void run() {
        Game.setTitle("Omnicraft");
        Game.setDebug(true);
        
        Game.onInit(new InitCallback());
        
        Game.onStep(new StepCallback());
        
        Game.onRender(new RenderCallback());
        
        Game.onDestroy(crashed ->
            {
                block.delete();
                DGL.destroy();
            });
        
        Game.run();
    }
    
    public static void main(String args[]) {
        DebugClient client = new DebugClient();
        client.run();
    }
}
