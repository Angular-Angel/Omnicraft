/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3i;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.angle.omnicraft.world.blocks.Block;

/**
 *
 * @author angle
 */
public class Region implements ChunkContainer {
    public final World world;
    //These describe this regions size in blocks, and the sizes of it's chunks.
    public final Vec3i coords;
    
    //The chunks within this region
    public Chunk[][][] chunks;
    
    public Region(World world) {
        this(world, null, 0, 0, 0);
    }
    
    public Region(World world, Block block, int x, int y, int z) {
        this.world = world;
        this.coords = new Vec3i(x, y, z);
        
        chunks = new Chunk[world.chunkEdgeLengthOfRegion][world.chunkEdgeLengthOfRegion][world.chunkEdgeLengthOfRegion];
        
        for (int i = 0; i < world.chunkEdgeLengthOfRegion; i++) {
            for (int j = 0; j < world.chunkEdgeLengthOfRegion; j++) {
                for (int k = 0; k < world.chunkEdgeLengthOfRegion; k++) {
                    chunks[i][j][k] = new OctreeChunk(this, block, i * world.blockEdgeLengthOfChunk, j * world.blockEdgeLengthOfChunk, k * world.blockEdgeLengthOfChunk);
                }
            }
        }
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (!containsBlockCoordinates(blockx, blocky, blockz))
            return null;
        return getChunkOfBlock(blockx, blocky, blockz).getBlock(blockx % world.blockEdgeLengthOfChunk, blocky % world.blockEdgeLengthOfChunk, blockz % world.blockEdgeLengthOfChunk);
    }
    
    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        getChunkOfBlock(blockx, blocky, blockz).setBlock(blockx % world.blockEdgeLengthOfChunk, blocky % world.blockEdgeLengthOfChunk, blockz % world.blockEdgeLengthOfChunk, block);
    }

    @Override
    public void setAllBlocks(Block block) {
        for (int i = 0; i < world.chunkEdgeLengthOfRegion; i++) {
            for (int j = 0; j < world.chunkEdgeLengthOfRegion; j++) {
                for (int k = 0; k < world.chunkEdgeLengthOfRegion; k++) {
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
        return new Vec3i(blockx / world.blockEdgeLengthOfChunk, blocky / world.blockEdgeLengthOfChunk, blockz / world.blockEdgeLengthOfChunk);
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
    public int getEdgeLength() {
        return world.blockEdgeLengthOfChunk * world.chunkEdgeLengthOfRegion;
    }

    @Override
    public int getEdgeLengthOfContainedChunks() {
        return world.blockEdgeLengthOfChunk;
    }

    @Override
    public Vec3i getCoordinates() {
        return new Vec3i(coords);
    }

    @Override
    public List<Chunk> getChunks() {
        return Arrays.stream(chunks)
        .flatMap(Arrays::stream)
        .flatMap(Arrays::stream)
        .collect(Collectors.toList());
    }
}
