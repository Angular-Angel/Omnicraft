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
import net.angle.omnicraft.world.Chunk;
import net.angle.omnicraft.world.Region;
import net.angle.omnicraft.world.World;
import net.angle.omnicraft.world.blocks.Block;
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
    public static final float MOVE_SPEED = 10.0f;
    
    private final Vec3 position;
    private final Vec3i regionPosition;
    
    public final World world;
    
    public final Camera3D camera;
    private final Camera3DController cameraController;
    private float prevMouseX;
    private float prevMouseY;
    
    public Region getRegion() {
        return world.regions.get(regionPosition.toString());
    }
    
    public Player(World world) {
        this(world, CAMERA_NEAR_Z, CAMERA_FAR_Z, CAMERA_FOV);
    }
    
    public Player(World world, float camera_near_z, float camera_far_z, float camera_fov) {
        this.world = world;
        camera = new Camera3D(camera_near_z, camera_far_z, camera_fov, 1.0f);

        Vec2i resolution = Game.getResolution();
        camera.setFOV(resolution.x, resolution.y, camera_fov);
        
        cameraController = new Camera3DController(camera);
        position = new Vec3(world.getRealEdgeLengthOfRegion() / 2, 17, world.getRealEdgeLengthOfRegion() / 2);
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
            if (position.x >= world.getRealEdgeLengthOfRegion() * (regionPosition.x + 1)) {
                regionPosition.x += 1;
            }
            if (position.x < world.getRealEdgeLengthOfRegion() * regionPosition.x) {
                regionPosition.x -= 1;
            }
            if (position.y >= world.getRealEdgeLengthOfRegion() * (regionPosition.y + 1)) {
                regionPosition.y += 1;
            }
            if (position.y < world.getRealEdgeLengthOfRegion() * regionPosition.y) {
                regionPosition.y -= 1;
            }
            if (position.z >= world.getRealEdgeLengthOfRegion() * (regionPosition.z + 1)) {
                regionPosition.z += 1;
            }
            if (position.z < world.getRealEdgeLengthOfRegion() * regionPosition.z) {
                regionPosition.z -= 1;
            }
    }
    
    public Vec3 getBlockAdjustedPosition() {
        return new Vec3(position).div(World.EDGE_LENGTH_OF_BLOCK);
    }
    
    public Vec3i getChunkCoords() {
        Vec3 blockAdjustedPosition = getBlockAdjustedPosition();
        Vec3i relativePosition = new Vec3i((int) blockAdjustedPosition.x, (int) blockAdjustedPosition.y, (int) blockAdjustedPosition.z);
        
        relativePosition.x -= world.getBlockEdgeLengthOfRegion() * regionPosition.x;
        relativePosition.y -= world.getBlockEdgeLengthOfRegion() * regionPosition.y;
        relativePosition.z -= world.getBlockEdgeLengthOfRegion() * regionPosition.z;
        
        return getRegion().getChunkCoordsFromVoxelCoords(relativePosition.x, relativePosition.y, relativePosition.z);
    }
    
    public void generateNeededChunks() {
        Region region = getRegion();
        if (region == null)
            return;
        Vec3i chunkCoords = getChunkCoords();
        for (int i = -World.GENERATION_DISTANCE; i <= World.GENERATION_DISTANCE; i++) {
            for (int j = -World.GENERATION_DISTANCE; j <= World.GENERATION_DISTANCE; j++) {
                for (int k = -World.GENERATION_DISTANCE; k <= World.GENERATION_DISTANCE; k++) {
                    Chunk chunk = region.getChunk(chunkCoords.x + i, chunkCoords.y + j, chunkCoords.z + k);
                    if (chunk == null)
                        region.generateChunk(chunkCoords.x + i, chunkCoords.y + j, chunkCoords.z + k);
                }
            }
        }
    }
    
    public void loadChunks() {
        Region region = getRegion();
        if (region == null)
            return;
        Vec3i chunkCoords = getChunkCoords();
        for (int i = -World.RENDER_DISTANCE; i <= World.RENDER_DISTANCE; i++) {
            for (int j = -World.RENDER_DISTANCE; j <= World.RENDER_DISTANCE; j++) {
                for (int k = -World.RENDER_DISTANCE; k <= World.RENDER_DISTANCE; k++) {
                    world.loadChunk(region.getChunk(chunkCoords.x + i, chunkCoords.y + j, chunkCoords.z + k));
                }
            }
        }
    }
    
    public void move(float dt) {
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
            updateRegionPosition();
        }
    }
    
    public Block pickBlock(int range) {
        Region region = getRegion();
        Vec3i coord = region.raycast(getBlockAdjustedPosition(), new Vec3(camera.forward).mult(World.EDGE_LENGTH_OF_BLOCK), range);
        if (coord != null)
            return region.getBlock(coord);
        else return null;
    }
    
    public void update(float dt) {
        
        move(dt);
        
        generateNeededChunks();
        
        loadChunks();
        
        cameraController.target.set(position);
        cameraController.update();
    }
}