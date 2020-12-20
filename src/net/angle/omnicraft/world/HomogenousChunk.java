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
    
    public HomogenousChunk(Region region, Block block) {
        super(region, 16, 0, 0, 0);
        this.block = block;
        this.parent = null;
    }
    
    public HomogenousChunk(Region region, Block block, OctreeChunk parent, int size, int x, int y, int z) {
        super(region, size, x, y, z);
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
        if (!containsCoordinates(blockx, blocky, blockz)) {
            //If the coordinates are outside this block, then we should throw an error.
            throw new IndexOutOfBoundsException("Asked for block at " + blockx + ", " + blocky + ", " + blockz + " in chunk of size " + size);
        }
        
        if (this.block == block) {
            //trying to set our block to what it already is - no change needed!
            return;
        }
        if (size == 1) {
            this.block = block;
            return;
        }
        
        if (parent == null)
            throw new IllegalStateException("Attempting to change a single block of multiblock HomogenousChunk with no Parent!");
        
        OctreeChunk replacement = new OctreeChunk(region, this.block, size, x, y, z);
        replacement.setBlock(blockx, blocky, blockz, block);
        parent.setOctant(x, y, z, replacement);
        
    }
    
}
