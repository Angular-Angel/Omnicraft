/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.client.DebugClient;

/**
 *
 * @author angle
 */
public abstract class Chunk implements BlockContainer {
    
    public final ChunkContainer container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    public final int x, y, z;
    
    public Chunk(ChunkContainer container, int x, int y, int z) {
        this.container = container;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLengthOfContainedChunks();
    }
    
    public void bufferBlocks(DebugClient client) {
        for (int blockx = 0; blockx < getEdgeLength(); blockx++) {
            for (int blocky = 0; blocky < getEdgeLength(); blocky++) {
                for (int blockz = 0; blockz < getEdgeLength(); blockz++) {
                    getBlock(blockx, blocky, blockz).bufferVertices(client, this, blockx, blocky, blockz);
                }
            }
        }
    }
}
