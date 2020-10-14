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
public class HomogenousChunk extends Chunk {
    
    public final Block block;
    
    public HomogenousChunk(Block block) {
        super(16, 0, 0, 0);
        this.block = block;
    }
    
    public HomogenousChunk(Block block, int size, int x, int y, int z) {
        super(size, x, y, z);
        this.block = block;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (blockx >= 0 && blockx < size && blocky >= 0 && blocky < size && 
            blockz >= 0 && blockz < size) {
            return block;
        } else {
            throw new IndexOutOfBoundsException("Asked for block at " + blockx + ", " + blocky + ", " + blockz + " in chunk of size " + size);
        }
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
