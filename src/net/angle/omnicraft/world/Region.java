/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.world.blocks.Block;

/**
 *
 * @author angle
 */
public class Region implements ChunkContainer {
    public final World world;
    //These describe this regions size in blocks, and the sizes of it's chunks.
    public final int chunkEdgeLength, edgeLength;
    
    //This regions neighbors
    private Region up, down, north, east, south, west;
    
    //The chunks within this region
    public Chunk[][][] chunks;
    
    public Region(World world) {
        this(world, null, 256, 16);
    }
    
    public Region(World world, Block block, int edgeLength, int chunkEdgeLength) {
        this.world = world;
        this.edgeLength = edgeLength;
        this.chunkEdgeLength = chunkEdgeLength;
        
        int arraySize = edgeLength / chunkEdgeLength;
        
        chunks = new Chunk[arraySize][arraySize][arraySize];
        
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                for (int k = 0; k < arraySize; k++) {
                    chunks[i][j][k] = new OctreeChunk(this, block, chunkEdgeLength, i * chunkEdgeLength, j * chunkEdgeLength, k * chunkEdgeLength);
                }
            }
        }
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (!containsBlockCoordinates(blockx, blocky, blockz))
            return null;
        return getChunkOfBlock(blockx, blocky, blockz).getBlock(blockx % chunkEdgeLength, blocky % chunkEdgeLength, blockz % chunkEdgeLength);
    }
    
    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        getChunkOfBlock(blockx, blocky, blockz).setBlock(blockx % chunkEdgeLength, blocky % chunkEdgeLength, blockz % chunkEdgeLength, block);
    }

    @Override
    public void setAllBlocks(Block block) {
        int arraySize = edgeLength / chunkEdgeLength;
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                for (int k = 0; k < arraySize; k++) {
                    chunks[i][j][k].setAllBlocks(block);
                }
            }
        }
    }
    
    @Override
    public Chunk getChunk(int chunkx, int chunky, int chunkz) {
        return chunks[chunkx][chunky][chunkz];
    }

    @Override
    public void setChunk(int chunkx, int chunky, int chunkz, Chunk chunk) {
        chunks[chunkx][chunky][chunkz] = chunk;
    }
    
    @Override
    public Vec3i getChunkCoordinatesOfBlock(int blockx, int blocky, int blockz) {
        return new Vec3i(blockx / chunkEdgeLength, blocky / chunkEdgeLength, blockz / chunkEdgeLength);
    }
    
    @Override
    public Chunk getChunkOfBlock(int blockx, int blocky, int blockz) {
        Vec3i chunkCoords = getChunkCoordinatesOfBlock(blockx, blocky, blockz);
        
        return chunks[chunkCoords.x][chunkCoords.y][chunkCoords.z];
    }

    @Override
    public void setChunkOfBlock(int blockx, int blocky, int blockz, Chunk chunk) {
        
        Vec3i chunkCoords = getChunkCoordinatesOfBlock(blockx, blocky, blockz);
        
        chunks[chunkCoords.x][chunkCoords.y][chunkCoords.z] = chunk;
    }

    @Override
    public int getBlockEdgeLegth() {
        return edgeLength;
    }
}
