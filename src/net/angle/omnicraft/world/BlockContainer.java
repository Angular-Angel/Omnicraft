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
    
    public int getBlockEdgeLegth();
    
    public default boolean containsBlockCoordinates(int blockx, int blocky, int blockz) {
        return blockx >= 0 && blockx < getBlockEdgeLegth() && blocky >= 0 && blocky < getBlockEdgeLegth() && 
            blockz >= 0 && blockz < getBlockEdgeLegth();
    }
    
    public abstract Block getBlock(int blockx, int blocky, int blockz);
    public abstract void setBlock(int blockx, int blocky, int blockz, Block block);
    public abstract void setAllBlocks(Block block);
}