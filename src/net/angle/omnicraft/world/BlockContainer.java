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
public abstract class BlockContainer {
    
    public final int size;
    
    public BlockContainer(int size) {
        this.size = size;
    }
    
    public boolean containsBlockCoordinates(int blockx, int blocky, int blockz) {
        return blockx >= 0 && blockx < size && blocky >= 0 && blocky < size && 
            blockz >= 0 && blockz < size;
    }
    
    public abstract Block getBlock(int blockx, int blocky, int blockz);
    public abstract void setBlock(int blockx, int blocky, int blockz, Block block);
    public abstract void setAllBlocks(Block block);
}
