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
    
    public HomogenousChunk(ChunkContainer container, Block block) {
        super(container, 0, 0, 0);
        this.block = block;
    }
    
    public HomogenousChunk(ChunkContainer container, Block block, int size, int x, int y, int z) {
        super(container, x, y, z);
        this.block = block;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (containsBlockCoordinates(blockx, blocky, blockz)) {
            return block;
        } else {
            return container.getBlock(blockx + x, blocky + y, blockz + z);
        }
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        if (!containsBlockCoordinates(blockx, blocky, blockz)) {
            //If the coordinates are outside this block, then we should throw an error.
            throw new IndexOutOfBoundsException("Asked for block at " + blockx + ", " + blocky + ", " + blockz + " in chunk of side length " + getEdgeLength());
        }
        
        if (this.block == block) {
            //trying to set our block to what it already is - no change needed!
            return;
        }
        if (getEdgeLength() == 1) {
            this.block = block;
            return;
        }
        
        if (container == null)
            throw new IllegalStateException("Attempting to change a single block of multiblock HomogenousChunk with no Parent!");
        
        OctreeChunk replacement = new OctreeChunk(container, this.block, x, y, z);
        replacement.setBlock(blockx, blocky, blockz, block);
        container.setChunkOfBlock(x, y, z, replacement);
        
    }

    @Override
    public void setAllBlocks(Block block) {
        this.block = block;
    }
    
}
