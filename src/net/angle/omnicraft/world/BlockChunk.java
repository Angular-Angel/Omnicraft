/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

/**
 *
 * @author angle
 */
public abstract class BlockChunk implements BlockContainer {
    
    public final Chunk container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    
    public BlockChunk(Chunk container) {
        this.container = container;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLength();
    }

    @Override
    public int getXVoxelOffset() {
        return container.getXVoxelOffset();
    }

    @Override
    public int getYVoxelOffset() {
        return container.getYVoxelOffset();
    }

    @Override
    public int getZVoxelOffset() {
        return container.getZVoxelOffset();
    }
}
