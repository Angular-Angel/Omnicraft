/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.game.Mouse;
import com.samrj.devil.graphics.Camera3D;
import com.samrj.devil.graphics.Camera3DController;
import com.samrj.devil.math.Util;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import org.lwjgl.system.MemoryStack;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Player {
    
    private static final float CAMERA_NEAR_Z = 0.1f;
    private static final float CAMERA_FAR_Z = 100.0f;
    private static final float CAMERA_FOV = Util.toRadians(90.0f);
    private static final float MOVE_SPEED = 5.0f;
    
    private final Vec3 position;
    
    public final Camera3D camera;
    private final Camera3DController cameraController;
    float prevMouseX;
    float prevMouseY;
    
    public Player() {
        this(CAMERA_NEAR_Z, CAMERA_FAR_Z, CAMERA_FOV);
    }
    
    public Player(float camera_near_z, float camera_far_z, float camera_fov) {
        camera = new Camera3D(camera_near_z, camera_far_z, camera_fov, 1.0f);

        Vec2i resolution = Game.getResolution();
        camera.setFOV(resolution.x, resolution.y, camera_fov);
        
        cameraController = new Camera3DController(camera);
        position = new Vec3(0, 1, 2);
        
        Vec2 mousePos = Game.getMouse().getPos();
        prevMouseX = mousePos.x; prevMouseY = mousePos.y;
    }
    
    public void mouseMoved(float x, float y) {
        float dx = x - prevMouseX;
        float dy = y - prevMouseY;
        cameraController.mouseDelta(dx, dy);
        prevMouseX = x; prevMouseY = y;
    }
    
    public void loadMatrixes() {
        //I need to add matrix toArray functions to make this easier. -Sam
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            glMatrixMode(GL_PROJECTION);
            glLoadMatrixf(camera.projMat.mallocFloat(stack));
            glMatrixMode(GL_MODELVIEW);
            glLoadMatrixf(camera.viewMat.mallocFloat(stack));
        }
    }
    
    public void update(float dt) {
        
        float forwards = 0, rightwards = 0;
        
        boolean moving = false;
        
        if (Game.getKeyboard().isKeyDown(GLFW_KEY_W)){
            forwards = 1;
            moving = true;
        } else if (Game.getKeyboard().isKeyDown(GLFW_KEY_S)){
            forwards = -1;
            moving = true;
        }

        if (Game.getKeyboard().isKeyDown(GLFW_KEY_A)){
            rightwards = -1;
            moving = true;
        }else if (Game.getKeyboard().isKeyDown(GLFW_KEY_D)){
            rightwards = 1;
            moving = true;
        }
        
        float camSin = (float)Math.sin(cameraController.getYaw());
        float camCos = (float)Math.cos(cameraController.getYaw());
        Vec3 flatForward = new Vec3(-camSin, 0.0f, -camCos);
        Vec3 flatRight   = new Vec3(camCos, 0.0f, -camSin);
        Vec3 direction = Vec3.mult(flatRight, rightwards);
        direction.madd(flatForward, forwards);
        
        if (Game.getKeyboard().isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            direction.y = -1;
            moving = true;
        }

        if (Game.getKeyboard().isKeyDown(GLFW_KEY_SPACE)){
            direction.y = 1;
            moving = true;
        }
        
        if (moving) {
            direction.normalize();
            direction.mult(MOVE_SPEED * dt);
            position.add(direction);
        }

        cameraController.target.set(position);
        cameraController.update();
    }
}
