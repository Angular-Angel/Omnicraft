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
    
    private Block block;
    private final OctreeChunk parent;
    
    public HomogenousChunk(Block block) {
        super(16, 0, 0, 0);
        this.block = block;
        this.parent = null;
    }
    
    public HomogenousChunk(Block block, OctreeChunk parent, int size, int x, int y, int z) {
        super(size, x, y, z);
        System.out.println("HomogenousChunk, Size: " + size + ", Position: " + x + ", " + y + ", " + z);
        this.block = block;
        this.parent = parent;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (containsCoordinates(blockx, blocky, blockz)) {
            return block;
        } else {
            throw new IndexOutOfBoundsException("Asked for block at " + blockx + ", " + blocky + ", " + blockz + " in chunk of size " + size);
        }
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (size == 1)
            if (containsCoordinates(blockx, blocky, blockz)) {
                this.block = block;
                return;
            } else {
                throw new IndexOutOfBoundsException("Asked for block at " + blockx + ", " + blocky + ", " + blockz + " in chunk of size " + size);
            }
        
        if (parent == null)
            throw new IllegalStateException("Attempting to set single block of HomogenousChunk with no Parent!");
        
        OctreeChunk replacement = new OctreeChunk(this.block, size, x, y, z);
        replacement.setBlock(blockx, blocky, blockz, block);
        parent.setOctant(x, y, z, replacement);
        
    }
    
}
