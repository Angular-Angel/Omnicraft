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
    
    public Vec3i pickedCoord;
    
    public final World world;
    
    public final Camera3D camera;
    private final Camera3DController cameraController;
    private float prevMouseX;
    private float prevMouseY;
    
    private int chunkGenX = -World.GENERATION_DISTANCE, chunkGenY = -World.GENERATION_DISTANCE, chunkGenZ = -World.GENERATION_DISTANCE;
    private int chunkRenderX = -World.RENDER_DISTANCE, chunkRenderY = -World.RENDER_DISTANCE, chunkRenderZ = -World.RENDER_DISTANCE;
    private int unloadChunkIndex = 0;
    
    public Player(World world) {
        this(world, CAMERA_NEAR_Z, CAMERA_FAR_Z, CAMERA_FOV);
    }
    
    public Player(World world, float camera_near_z, float camera_far_z, float camera_fov) {
        this.world = world;
        camera = new Camera3D(camera_near_z, camera_far_z, camera_fov, 1.0f);

        Vec2i resolution = Game.getResolution();
        camera.setFOV(resolution.x, resolution.y, camera_fov);
        
        cameraController = new Camera3DController(camera);
        position = new Vec3(world.getRealEdgeLengthOfChunk() * 8, 17, world.getRealEdgeLengthOfChunk() * 8);
        
        Vec2 mousePos = Game.getMouse().getPos();
        prevMouseX = mousePos.x; prevMouseY = mousePos.y;
    }
    
    public Chunk getChunk() {
        return world.getChunk(getChunkCoords());
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
    
    public Vec3 getApproximateVoxelPosition() {
        return new Vec3(position).div(World.EDGE_LENGTH_OF_BLOCK);
    }
    
    public Vec3i getVoxelPosition() {
        Vec3 pos = getApproximateVoxelPosition();
        return new Vec3i((int) pos.x, (int) pos.y, (int) pos.z);
    }
    
    public Vec3i getChunkCoords() {
        Vec3i voxelPosition = getVoxelPosition();
        return world.getChunkCoordsFromVoxelCoords(voxelPosition.x, voxelPosition.y, voxelPosition.z);
    }
    
    public void generateNeededChunks() {
        int generatedChunks = 0;
        Vec3i chunkCoords = getChunkCoords();
        for (; chunkGenX <= World.GENERATION_DISTANCE; chunkGenX++) {
            for (; chunkGenY <= World.GENERATION_DISTANCE; chunkGenY++) {
                for (; chunkGenZ <= World.GENERATION_DISTANCE; chunkGenZ++) {
                    Chunk chunk = world.getChunk(chunkCoords.x + chunkGenX, chunkCoords.y + chunkGenY, chunkCoords.z + chunkGenZ);
                    if (chunk == null) {
                        world.generateChunk(chunkCoords.x + chunkGenX, chunkCoords.y + chunkGenY, chunkCoords.z + chunkGenZ);
                        generatedChunks++;
                        if (generatedChunks >= 5)
                            return;
                    }
                }
                chunkGenZ = -World.GENERATION_DISTANCE;
            }
            chunkGenY = -World.GENERATION_DISTANCE;
        }
        chunkGenX = -World.GENERATION_DISTANCE;
    }
    
    public void loadChunks() {
        int renderedChunks = 0;
        Vec3i chunkCoords = getChunkCoords();
        for (; chunkRenderX <= World.RENDER_DISTANCE; chunkRenderX++) {
            for (; chunkRenderY <= World.RENDER_DISTANCE; chunkRenderY++) {
                for (; chunkRenderZ <= World.RENDER_DISTANCE; chunkRenderZ++) {
                    if (world.loadChunk(world.getChunk(chunkCoords.x + chunkRenderX, chunkCoords.y + chunkRenderY, chunkCoords.z + chunkRenderZ))) {
                        renderedChunks++;
                        if (renderedChunks >= 10)
                            return;
                    }
                }
                chunkRenderZ = -World.RENDER_DISTANCE;
            }
            chunkRenderY = -World.RENDER_DISTANCE;
        }
        chunkRenderX = -World.RENDER_DISTANCE;
    }
    
    public void unloadChunks() {
        Chunk ownChunk = getChunk();
        if (ownChunk == null) return;
        for (int i = 0; i < 10; i++) {
            if (world.loadedChunks.isEmpty())
                return;
            if (unloadChunkIndex >= world.loadedChunks.size())
                unloadChunkIndex = 0;
            Chunk chunk = world.loadedChunks.get(unloadChunkIndex);
            if (ownChunk.axialDist(chunk) > World.RENDER_DISTANCE + 2)
                world.unloadChunk(chunk);
            else {
                unloadChunkIndex++;
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
        }
    }
    
    public Block pickBlock(int range) {
        pickedCoord = world.raycast(getApproximateVoxelPosition(), new Vec3(camera.forward).mult(World.EDGE_LENGTH_OF_BLOCK), range);
        if (pickedCoord != null)
            return world.getBlock(pickedCoord);
        else return null;
    }
    
    public void update(float dt) {
        move(dt);
        
        generateNeededChunks();
        
        loadChunks();
        unloadChunks();
        
        cameraController.target.set(position);
        cameraController.update();
    }
}