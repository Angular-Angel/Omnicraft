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
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.WorldGenerator;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class DebugClient implements Client {
    
    private Player player;
    private World world;
    
    public static void main(String args[]) {
        DebugClient client = new DebugClient();
        client.run();
    }

    @Override
    public void preInit() {
        Game.setTitle("Omnicraft");
        Game.setDebug(true);
    }

    @Override
    public void init() {
        player = new Player();

        world = WorldGenerator.generateWorld();

        glEnable(GL_TEXTURE_2D);

        glShadeModel(GL_SMOOTH);                        // Enables Smooth Shading
        glClearDepth(1.0f);                         // Depth Buffer Setup
        glEnable(GL_DEPTH_TEST);                        // Enables Depth Testing
        glDepthFunc(GL_LEQUAL);                         // The Type Of Depth Test To Do
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);          // Really Nice Perspective Calculations*/

        Game.getMouse().setGrabbed(true);
        Game.setVsync(true);
    }

    @Override
    public void mouseMoved(float x, float y) {
        player.handleMouseInput(x, y);
    }

    @Override
    public void key(int key, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) Game.stop();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void step(float dt) {
        player.update(dt);
    }

    @Override
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        player.loadMatrixes();

        world.draw();
    }

    @Override
    public void destroy(Boolean crashed) {
        world.delete();
        world = null;

        if (crashed) DGL.setDebugLeakTracking(false);

        DGL.destroy();
    }
}
