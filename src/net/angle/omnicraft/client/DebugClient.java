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
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.ArrayChunk;
import net.angle.omnicraft.world.Chunk;
import net.angle.omnicraft.world.HomogenousChunk;
import net.angle.omnicraft.world.OctreeChunk;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.types.MineralGrain;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.SoilFraction;
import net.angle.omnicraft.world.types.SoilType;
import net.angle.omnicraft.world.blocks.SoilBlock;
import net.angle.omnicraft.world.blocks.SteppedCubeShape;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryStack;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class DebugClient {
    
    private Camera3D camera;
    private Player player;
    private World world;
    
    private class InitCallback implements Game.InitCallback {

        @Override
        public void init() throws IOException {
            
            DGL.init();
 
            camera = new Camera3D(0.1f, 100.0f, Util.toRadians(90.0f), 1.0f);
            
            Vec2i resolution = Game.getResolution();
            camera.setFOV(resolution.x, resolution.y, Util.toRadians(90.0f));
            
            player = new Player(camera);
            
            world = new World();
            
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
        }
        
    }
    
    private class RenderCallback implements Runnable {
        
        @Override
        public void run() {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //I need to add matrix toArray functions to make this easier. -Sam
            try (MemoryStack stack = MemoryStack.stackPush())
            {
                glMatrixMode(GL_PROJECTION);
                glLoadMatrixf(camera.projMat.mallocFloat(stack));
                glMatrixMode(GL_MODELVIEW);
                glLoadMatrixf(camera.viewMat.mallocFloat(stack));
            }
            
            world.draw();
        }
        
    }
    
    public void run() {
        Game.setTitle("Omnicraft");
        Game.setDebug(true);
        
        Game.onInit(new InitCallback());
        
        Game.onStep(new StepCallback());
        
        Game.onRender(new RenderCallback());
        
        Game.onDestroy(crashed ->
            {
                world.delete();
                DGL.destroy();
            });
        
        Game.run();
    }
    
    public static void main(String args[]) {
        DebugClient client = new DebugClient();
        client.run();
    }
}
