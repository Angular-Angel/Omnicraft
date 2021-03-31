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
public abstract class BlockChunk extends Positionable implements BlockContainer {
    
    public final BlockChunkContainer container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    
    public BlockChunk(BlockChunkContainer container, int x, int y, int z) {
        super(x, y, z);
        this.container = container;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLengthOfContainedChunks();
    }
}
