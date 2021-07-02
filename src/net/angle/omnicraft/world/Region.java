/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import jdk.nashorn.internal.objects.Global;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public class Region extends VoxelPositionable implements BlockContainer, SideContainer {
    public final World world;
    //These describe this regions size in blocks, and the sizes of it's chunks.
    
    //The chunks within this region
    public Chunk[][][] chunks;
    
    public Region(World world) {
        this(world, 0, 0, 0);
    }
    
    public Region(World world, int x, int y, int z) {
        super(x, y, z);
        this.world = world;
        
        chunks = new Chunk[World.CHUNK_EDGE_LENGTH_OF_REGION][World.CHUNK_EDGE_LENGTH_OF_REGION][World.CHUNK_EDGE_LENGTH_OF_REGION];
    }
    
    public void generateChunks() {
        generateChunks(world.block_ids.get(0), world.side_ids.get(0));
    }
    
    public void generateChunks(Block block, Side side) {
        for (int i = 0; i < World.CHUNK_EDGE_LENGTH_OF_REGION; i++) {
            for (int j = 0; j < World.CHUNK_EDGE_LENGTH_OF_REGION; j++) {
                for (int k = 0; k < World.CHUNK_EDGE_LENGTH_OF_REGION; k++) {
                    chunks[i][j][k] = new Chunk(this, block, side, i, j, k);
                }
            }
        }
    }
    
    public boolean containsChunkCoordinates(int x, int y, int z) {
        return x >= 0 && x < World.CHUNK_EDGE_LENGTH_OF_REGION && y >= 0 && y < World.CHUNK_EDGE_LENGTH_OF_REGION && 
            z >= 0 && z < World.CHUNK_EDGE_LENGTH_OF_REGION;
    }
    
    public void update(float dt) {
    }
    
    public float intbound(float start, float delta) {
        if (delta < 0) {
            return intbound(-start, -delta);
        } else {
            start = (start % 1 + 1) % 1;
            return (1 - start) / delta;
        }
    }
    
    public Vec3i raycast(Vec3 origin, Vec3 direction, int radius) {
        float curx = origin.x;
        float cury = origin.y;
        float curz = origin.z;
        
        float dx = direction.x;
        float dy = direction.y;
        float dz = direction.z;
        
        if (dx < 0.05f && dx > -0.05f)
            dx = 0;
        
        if (dy < 0.05f && dy > -0.05f)
            dy = 0;
        
        if (dz < 0.05f && dz > -0.05f)
            dz = 0;
        
        int stepX = (int) Math.signum(dx);
        int stepY = (int) Math.signum(dy);
        int stepZ = (int) Math.signum(dz);
        
        int steps = 0;
        
        float tMaxX = dx != 0 ? intbound(origin.x, dx) : Float.POSITIVE_INFINITY;
        float tMaxY = dy != 0 ? intbound(origin.y, dy) : Float.POSITIVE_INFINITY;
        float tMaxZ = dz != 0 ? intbound(origin.z, dz) : Float.POSITIVE_INFINITY;
        
        // The change in t when taking a step (always positive).
        
        float tDeltaX = dx != 0 ? stepX/dx : 0;
        float tDeltaY = dy != 0 ? stepY/dy : 0;
        float tDeltaZ = dz != 0 ? stepZ/dz : 0;
        
        if (dx == 0 && dy == 0 && dz == 0)
            throw new IllegalArgumentException("Raycast in zero direction!");
        
        while (steps < radius) {
            if(tMaxX < tMaxY) {
                if(tMaxX < tMaxZ) {
                    curx += stepX;
                    tMaxX += tDeltaX;
                } else {
                    curz += stepZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if(tMaxY < tMaxZ) {
                    cury += stepY;
                    tMaxY += tDeltaY;
                } else {
                    curz += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }
            steps++;
            Vec3i coord = new Vec3i((int) Math.floor(curx), (int) Math.floor(cury), (int) Math.floor(curz));
            Block block = getBlock(coord);
            if (block != null && block.isDrawable())
                return coord;
        }
        
        return null;
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        Chunk chunk = getChunkFromVoxelCoords(blockx, blocky, blockz);
        if (chunk == null)
            return null;
        return chunk.getBlock(blockx % World.BLOCK_EDGE_LENGTH_OF_CHUNK, blocky % World.BLOCK_EDGE_LENGTH_OF_CHUNK, blockz % World.BLOCK_EDGE_LENGTH_OF_CHUNK);
    }
    
    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        getChunkFromVoxelCoords(blockx, blocky, blockz).setBlock(blockx % World.BLOCK_EDGE_LENGTH_OF_CHUNK, blocky % World.BLOCK_EDGE_LENGTH_OF_CHUNK, blockz % World.BLOCK_EDGE_LENGTH_OF_CHUNK, block);
    }

    @Override
    public void setAllBlocks(Block block) {
        for (int i = 0; i < World.CHUNK_EDGE_LENGTH_OF_REGION; i++) {
            for (int j = 0; j < World.CHUNK_EDGE_LENGTH_OF_REGION; j++) {
                for (int k = 0; k < World.CHUNK_EDGE_LENGTH_OF_REGION; k++) {
                    chunks[i][j][k].setAllBlocks(block);
                }
            }
        }
    }
    
    public Chunk generateChunk(int chunkx, int chunky, int chunkz) {
        if (!containsChunkCoordinates(chunkx, chunky, chunkz)) {
            int regionx = getX();
            int regiony = getY();
            int regionz = getZ();
            while (chunkx < 0) {
                chunkx += World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionx--;
            }
            while (chunkx >= World.CHUNK_EDGE_LENGTH_OF_REGION) {
                chunkx -= World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionx++;
            }
            while (chunky < 0) {
                chunky += World.CHUNK_EDGE_LENGTH_OF_REGION;
                regiony--;
            }
            while (chunky >= World.CHUNK_EDGE_LENGTH_OF_REGION) {
                chunky -= World.CHUNK_EDGE_LENGTH_OF_REGION;
                regiony++;
            }
            while (chunkz < 0) {
                chunkz += World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionz--;
            }
            while (chunkz >= World.CHUNK_EDGE_LENGTH_OF_REGION) {
                chunkz -= World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionz++;
            }
            Region region = world.regions.get(new Vec3i(regionx, regiony, regionz).toString());
            if (region == null)
                region = world.generateNewRegion(regionx, regiony, regionz);
            return region.generateChunk(chunkx, chunky, chunkz);
        }
        Chunk chunk = new Chunk(this, chunkx, chunky, chunkz);
        chunks[chunkx][chunky][chunkz] = chunk;
        return world.generateChunkTerrain(chunk);
    }
    
    public Chunk getChunk(Vec3i coord) {
        return getChunk(coord.x, coord.y, coord.z);
    }
    
    public Chunk getChunk(int chunkx, int chunky, int chunkz) {
        if (!containsChunkCoordinates(chunkx, chunky, chunkz)) {
            int regionx = getX();
            int regiony = getY();
            int regionz = getZ();
            while (chunkx < 0) {
                chunkx += World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionx--;
            }
            while (chunkx >= World.CHUNK_EDGE_LENGTH_OF_REGION) {
                chunkx -= World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionx++;
            }
            while (chunky < 0) {
                chunky += World.CHUNK_EDGE_LENGTH_OF_REGION;
                regiony--;
            }
            while (chunky >= World.CHUNK_EDGE_LENGTH_OF_REGION) {
                chunky -= World.CHUNK_EDGE_LENGTH_OF_REGION;
                regiony++;
            }
            while (chunkz < 0) {
                chunkz += World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionz--;
            }
            while (chunkz >= World.CHUNK_EDGE_LENGTH_OF_REGION) {
                chunkz -= World.CHUNK_EDGE_LENGTH_OF_REGION;
                regionz++;
            }
            Region region = world.regions.get(new Vec3i(regionx, regiony, regionz).toString());
            if (region == null)
                region = world.generateNewRegion(regionx, regiony, regionz);
            return region.getChunk(chunkx, chunky, chunkz);
        }
        return chunks[chunkx][chunky][chunkz];
    }

    public void setChunk(int chunkx, int chunky, int chunkz, Chunk chunk) {
        chunks[chunkx][chunky][chunkz] = chunk;
    }
    
    public Vec3i getChunkCoordsFromVoxelCoords(int voxelx, int voxely, int voxelz) {
        //Fixing a rounding problem
        if (voxelx < 0) voxelx -= World.BLOCK_EDGE_LENGTH_OF_CHUNK;
        if (voxely < 0) voxely -= World.BLOCK_EDGE_LENGTH_OF_CHUNK;
        if (voxelz < 0) voxelz -= World.BLOCK_EDGE_LENGTH_OF_CHUNK;
        return new Vec3i(voxelx / World.BLOCK_EDGE_LENGTH_OF_CHUNK, voxely / World.BLOCK_EDGE_LENGTH_OF_CHUNK, voxelz / World.BLOCK_EDGE_LENGTH_OF_CHUNK);
    }
    
    public Chunk getChunkFromVoxelCoords(Vec3i coord) {
        return getChunkFromVoxelCoords(coord.x, coord.y, coord.z);
    }
    
    public Chunk getChunkFromVoxelCoords(int voxelx, int voxely, int voxelz) {
        Vec3i chunkCoords = getChunkCoordsFromVoxelCoords(voxelx, voxely, voxelz);
        
        return getChunk(chunkCoords);
    }
    
    public void setChunkFromVoxelCoords(int voxelx, int voxely, int voxelz, Chunk chunk) {
        
        Vec3i chunkCoords = getChunkCoordsFromVoxelCoords(voxelx, voxely, voxelz);
        
        chunks[chunkCoords.x][chunkCoords.y][chunkCoords.z] = chunk;
    }

    @Override
    public int getEdgeLength() {
        return world.getBlockEdgeLengthOfRegion();
    }
    
    public int getEdgeLengthInChunks() {
        return World.CHUNK_EDGE_LENGTH_OF_REGION;
    }

    public int getEdgeLengthOfContainedChunks() {
        return World.BLOCK_EDGE_LENGTH_OF_CHUNK;
    }

    public List<Chunk> getChunks() {
        return Arrays.stream(chunks)
        .flatMap(Arrays::stream)
        .flatMap(Arrays::stream)
        .collect(Collectors.toList());
    }

    @Override
    public Side getSide(Block.BlockFace face, int sidex, int sidey, int sidez) {
        return getChunkFromVoxelCoords(sidex, sidey, sidez).getSide(face, sidex % World.BLOCK_EDGE_LENGTH_OF_CHUNK, sidey % World.BLOCK_EDGE_LENGTH_OF_CHUNK, sidez % World.BLOCK_EDGE_LENGTH_OF_CHUNK);
    }

    @Override
    public void setSide(Block.BlockFace face, int sidex, int sidey, int sidez, Side side) {
        getChunkFromVoxelCoords(sidex, sidey, sidez).setSide(face, sidex % World.BLOCK_EDGE_LENGTH_OF_CHUNK, sidey % World.BLOCK_EDGE_LENGTH_OF_CHUNK, sidez % World.BLOCK_EDGE_LENGTH_OF_CHUNK, side);
    }

    @Override
    public int getXVoxelOffset() {
        return getX() * getEdgeLength();
    }

    @Override
    public int getYVoxelOffset() {
        return getY() * getEdgeLength();
    }

    @Override
    public int getZVoxelOffset() {
        return getZ() * getEdgeLength();
    }
    
    public int getXChunkOffset() {
        return getX() * getEdgeLengthInChunks();
    }

    public int getYChunkOffset() {
        return getY() * getEdgeLengthInChunks();
    }

    public int getZChunkOffset() {
        return getZ() * getEdgeLengthInChunks();
    }
}
