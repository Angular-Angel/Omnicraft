/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block;

/**
 *
 * @author angle
 */
public abstract class Chunk {
    
    //These describe this blocks size, and it's x, y, and z coordinates
    public final int size, x, y, z;
    
    public Chunk(int size, int x, int y, int z) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        
    }
    
    public abstract Block getBlock(int blockx, int blocky, int blockz);
    public abstract void setBlock(int blockx, int blocky, int blockz, Block block);
    public abstract void draw();
}
