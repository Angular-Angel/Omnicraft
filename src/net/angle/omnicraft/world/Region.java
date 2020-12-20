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
public class Region {
    public final World world;
    //These describe this regions size in chunks, and the sizes of it's chunks.
    public final int size, chunkSize;
    
    //This regions neighbors
    private Region up, down, north, east, south, west;
    
    //The chunks within this region
    public Chunk[][][] chunks;
    
    public Region(World world) {
        this(world, null, 256, 16);
    }
    
    public Region(World world, Block block, int size, int chunkSize) {
        this.world = world;
        this.size = size;
        this.chunkSize = chunkSize;
        
        int arraySize = size / chunkSize;
        
        chunks = new Chunk[arraySize][arraySize][arraySize];
        
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                for (int k = 0; k < arraySize; k++) {
                    chunks[i][j][k] = new OctreeChunk(this, block, chunkSize, i * chunkSize, j * chunkSize, k * chunkSize);
                }
            }
        }
    }
    
    public boolean containsCoordinates(int blockx, int blocky, int blockz) {
        
        return blockx >= 0 && blockx < size && blocky >= 0 && blocky < size && 
            blockz >= 0 && blockz < size;
    }
    
    public Chunk getChunk(int chunkx, int chunky, int chunkz) {
        return chunks[chunkx][chunky][chunkz];
    }
    
    public Chunk getChunkOfBlock(int blockx, int blocky, int blockz) {
        int chunkx = blockx / chunkSize, chunky = blocky / chunkSize, chunkz = blockz / chunkSize;
        
        return chunks[chunkx][chunky][chunkz];
    }
    
    public Block getBlock(int blockx, int blocky, int blockz) {
        return getChunkOfBlock(blockx, blocky, blockz).getBlock(blockx % chunkSize, blocky % chunkSize, blockz % chunkSize);
    }
    
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        getChunkOfBlock(blockx, blocky, blockz).setBlock(blockx % chunkSize, blocky % chunkSize, blockz % chunkSize, block);
    }
}
