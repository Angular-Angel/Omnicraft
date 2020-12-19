/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 *
 * @author angle
 */
public abstract class Chunk {
    
    public final World world;
    //These describe this blocks size, and it's x, y, and z coordinates
    public final int size, x, y, z;
    
    public Chunk(World world, int size, int x, int y, int z) {
        this.world = world;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean containsCoordinates(int blockx, int blocky, int blockz) {
        return blockx >= 0 && blockx < size && blocky >= 0 && blocky < size && 
            blockz >= 0 && blockz < size;
    }
    
    public abstract Block getBlock(int blockx, int blocky, int blockz);
    public abstract void setBlock(int blockx, int blocky, int blockz, Block block);
    
    public void draw() {
        glTranslatef(x, y, z);
        for (int blockx = 0; blockx < size; blockx++) {
            glTranslatef(1, 0, 0);
            for (int blocky = 0; blocky < size; blocky++) {
                glTranslatef(0, 1, 0);
                for (int blockz = 0; blockz < size; blockz++) {
                    glTranslatef(0, 0, 1);
                    getBlock(blockx, blocky, blockz).draw(this, blockx, blocky, blockz);
                }
                glTranslatef(0, 0, -size);
            }
            glTranslatef(0, -size, 0);
        }
        glTranslatef(-size, 0, 0);
        glTranslatef(-x, -y, -z);
    }
}
