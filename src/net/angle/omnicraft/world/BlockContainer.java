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
public interface BlockContainer {
    
    /**
     * Gets the number of blocks in an edge (length, width, or height) for this block container.
     * @return
     */
    public int getEdgeLength();
    
    public default boolean containsBlockCoordinates(int blockx, int blocky, int blockz) {
        return blockx >= 0 && blockx < getEdgeLength() && blocky >= 0 && blocky < getEdgeLength() && 
            blockz >= 0 && blockz < getEdgeLength();
    }
    
    public abstract Block getBlock(int blockx, int blocky, int blockz);
    public abstract void setBlock(int blockx, int blocky, int blockz, Block block);
    public abstract void setAllBlocks(Block block);
}
