/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.graphics.Camera3D;
import com.samrj.devil.graphics.Camera3DController;
import com.samrj.devil.math.Util;
import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.world.World;
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
    
    public static final float CAMERA_NEAR_Z = 0.125f;
    public static final float CAMERA_FAR_Z = 1024.0f;
    public static final float CAMERA_FOV = Util.toRadians(90.0f);
    public static final float MOVE_SPEED = 20.0f;
    
    public final Vec3 position;
    public final Vec3i regionPosition;
    
    public final World world;
    
    public final Camera3D camera;
    private final Camera3DController cameraController;
    private float prevMouseX;
    private float prevMouseY;
    
    public Player(World world) {
        this(world, CAMERA_NEAR_Z, CAMERA_FAR_Z, CAMERA_FOV);
    }
    
    public Player(World world, float camera_near_z, float camera_far_z, float camera_fov) {
        this.world = world;
        world.player = this;
        camera = new Camera3D(camera_near_z, camera_far_z, camera_fov, 1.0f);

        Vec2i resolution = Game.getResolution();
        camera.setFOV(resolution.x, resolution.y, camera_fov);
        
        cameraController = new Camera3DController(camera);
        position = new Vec3(32, 20, 32);
        regionPosition = new Vec3i(0, 0, 0);
        
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
    
    public void updateRegionPosition() {
            if (position.x > world.getBlockEdgeLengthOfRegion() * (regionPosition.x + 1)) {
                regionPosition.x += 1;
            }
            if (position.x < world.getBlockEdgeLengthOfRegion() * (regionPosition.x)) {
                regionPosition.x -= 1;
            }
            if (position.y > world.getBlockEdgeLengthOfRegion() * (regionPosition.y + 1)) {
                regionPosition.y += 1;
            }
            if (position.y < world.getBlockEdgeLengthOfRegion() * (regionPosition.y)) {
                regionPosition.y -= 1;
            }
            if (position.z > world.getBlockEdgeLengthOfRegion() * (regionPosition.z + 1)) {
                regionPosition.z += 1;
            }
            if (position.z < world.getBlockEdgeLengthOfRegion() * (regionPosition.z)) {
                regionPosition.z -= 1;
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
        
        updateRegionPosition();

        cameraController.target.set(position);
        cameraController.update();
        
//        Region spawnRegion = world.getSpawnRegion();
//        Vec3i coords = spawnRegion.raycast(position, camera.forward, 10);
//        System.out.println(camera.forward);
//        System.out.println(coords);
//        if (coords != null)
//            System.out.println(spawnRegion.getBlock(coords).name);
    }
}