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
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public class Region extends Positionable implements BlockContainer, SideContainer {
    public final World world;
    //These describe this regions size in blocks, and the sizes of it's chunks.
    
    //The chunks within this region
    public Chunk[][][] chunks;
    
    public Region(World world) {
        this(world, null, null, 0, 0, 0);
    }
    
    public Region(World world, Block block, Side side, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
        
        chunks = new Chunk[world.chunkEdgeLengthOfRegion][world.chunkEdgeLengthOfRegion][world.chunkEdgeLengthOfRegion];
        
        for (int i = 0; i < world.chunkEdgeLengthOfRegion; i++) {
            for (int j = 0; j < world.chunkEdgeLengthOfRegion; j++) {
                for (int k = 0; k < world.chunkEdgeLengthOfRegion; k++) {
                    chunks[i][j][k] = new Chunk(this, block, side, i * world.blockEdgeLengthOfChunk, j * world.blockEdgeLengthOfChunk, k * world.blockEdgeLengthOfChunk);
                }
            }
        }
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        if (!containsCoordinates(blockx, blocky, blockz))
            return null;
        return getChunkFromVoxelCoords(blockx, blocky, blockz).getBlock(blockx % world.blockEdgeLengthOfChunk, blocky % world.blockEdgeLengthOfChunk, blockz % world.blockEdgeLengthOfChunk);
    }
    
    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        getChunkFromVoxelCoords(blockx, blocky, blockz).setBlock(blockx % world.blockEdgeLengthOfChunk, blocky % world.blockEdgeLengthOfChunk, blockz % world.blockEdgeLengthOfChunk, block);
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
    
    public Chunk getChunk(int chunkx, int chunky, int chunkz) {
        return chunks[chunkx][chunky][chunkz];
    }

    public void setChunk(int chunkx, int chunky, int chunkz, Chunk chunk) {
        chunks[chunkx][chunky][chunkz] = chunk;
    }
    
    public Vec3i getChunkCoordsFromVoxelCoords(int x, int y, int z) {
        return new Vec3i(x / world.blockEdgeLengthOfChunk, y / world.blockEdgeLengthOfChunk, z / world.blockEdgeLengthOfChunk);
    }
    
    public Chunk getChunkFromVoxelCoords(int x, int y, int z) {
        Vec3i chunkCoords = getChunkCoordsFromVoxelCoords(x, y, z);
        
        return chunks[chunkCoords.x][chunkCoords.y][chunkCoords.z];
    }
    
    public void setChunkFromVoxelCoords(int x, int y, int z, Chunk chunk) {
        
        Vec3i chunkCoords = getChunkCoordsFromVoxelCoords(x, y, z);
        
        chunks[chunkCoords.x][chunkCoords.y][chunkCoords.z] = chunk;
    }

    @Override
    public int getEdgeLength() {
        return world.blockEdgeLengthOfChunk * world.chunkEdgeLengthOfRegion;
    }

    public int getEdgeLengthOfContainedChunks() {
        return world.blockEdgeLengthOfChunk;
    }

    public List<Chunk> getChunks() {
        return Arrays.stream(chunks)
        .flatMap(Arrays::stream)
        .flatMap(Arrays::stream)
        .collect(Collectors.toList());
    }

    @Override
    public Side getSide(Block.BlockFace face, int sidex, int sidey, int sidez) {
        return getChunkFromVoxelCoords(sidex, sidey, sidez).getSide(face, sidex % world.blockEdgeLengthOfChunk, sidey % world.blockEdgeLengthOfChunk, sidez % world.blockEdgeLengthOfChunk);
    }

    @Override
    public void setSide(Block.BlockFace face, int sidex, int sidey, int sidez, Side side) {
        getChunkFromVoxelCoords(sidex, sidey, sidez).setSide(face, sidex % world.blockEdgeLengthOfChunk, sidey % world.blockEdgeLengthOfChunk, sidez % world.blockEdgeLengthOfChunk, side);
    }
}
