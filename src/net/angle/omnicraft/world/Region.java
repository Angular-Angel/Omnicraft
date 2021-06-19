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
        
        chunks = new Chunk[world.chunkEdgeLengthOfRegion][world.chunkEdgeLengthOfRegion][world.chunkEdgeLengthOfRegion];
    }
    
    public void generateChunks() {
        generateChunks(world.block_ids.get(0), world.side_ids.get(0));
    }
    
    public void generateChunks(Block block, Side side) {
        for (int i = 0; i < world.chunkEdgeLengthOfRegion; i++) {
            for (int j = 0; j < world.chunkEdgeLengthOfRegion; j++) {
                for (int k = 0; k < world.chunkEdgeLengthOfRegion; k++) {
                    chunks[i][j][k] = new Chunk(this, block, side, i, j, k);
                }
            }
        }
    }
    
    public boolean containsChunkCoordinates(int x, int y, int z) {
        return x >= 0 && x < world.chunkEdgeLengthOfRegion && y >= 0 && y < world.chunkEdgeLengthOfRegion && 
            z >= 0 && z < world.chunkEdgeLengthOfRegion;
    }
    
    public void update(float dt) {
    }
    
    public Vec3i raycast(Vec3 origin, Vec3 direction, int radius) {
        
        int curx = (int) Math.floor(origin.x);
        int cury = (int) Math.floor(origin.y);
        int curz = (int) Math.floor(origin.z);
        
        float dx = Math.abs(direction.x);
        float dy = Math.abs(direction.y);
        float dz = Math.abs(direction.z);
        
        int stepX = (int) Math.signum(dx);
        int stepY = (int) Math.signum(dy);
        int stepZ = (int) Math.signum(dz);
        
        float tMaxX = 0;
        float tMaxY = 0;
        float tMaxZ = 0;
        
        // The change in t when taking a step (always positive).
        float tDeltaX = stepX/dx;
        float tDeltaY = stepY/dy;
        float tDeltaZ = stepZ/dz;
        
        if (dx == 0 && dy == 0 && dz == 0)
            throw new IllegalArgumentException("Raycast in zero direction!");
        
        while (/* ray's length is less than search radius*/
               tMaxX <= radius && tMaxY <= radius && tMaxZ <= radius) {
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
            Block block = getBlock(curx, cury, curz);
            if (block != null && block.id != 0)
                return new Vec3i(curx, cury, curz);
        }
        
        return null;
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        Chunk chunk = getChunkFromVoxelCoords(blockx, blocky, blockz);
        if (chunk == null)
            return null;
        return chunk.getBlock(blockx % world.blockEdgeLengthOfChunk, blocky % world.blockEdgeLengthOfChunk, blockz % world.blockEdgeLengthOfChunk);
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
    
    public Chunk generateChunk(int chunkx, int chunky, int chunkz) {
        if (!containsChunkCoordinates(chunkx, chunky, chunkz)) {
            int regionx = getX();
            int regiony = getY();
            int regionz = getZ();
            while (chunkx < 0) {
                chunkx += world.chunkEdgeLengthOfRegion;
                regionx--;
            }
            while (chunkx >= world.chunkEdgeLengthOfRegion) {
                chunkx -= world.chunkEdgeLengthOfRegion;
                regionx++;
            }
            while (chunky < 0) {
                chunky += world.chunkEdgeLengthOfRegion;
                regiony--;
            }
            while (chunky >= world.chunkEdgeLengthOfRegion) {
                chunky -= world.chunkEdgeLengthOfRegion;
                regiony++;
            }
            while (chunkz < 0) {
                chunkz += world.chunkEdgeLengthOfRegion;
                regionz--;
            }
            while (chunkz >= world.chunkEdgeLengthOfRegion) {
                chunkz -= world.chunkEdgeLengthOfRegion;
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
                chunkx += world.chunkEdgeLengthOfRegion;
                regionx--;
            }
            while (chunkx >= world.chunkEdgeLengthOfRegion) {
                chunkx -= world.chunkEdgeLengthOfRegion;
                regionx++;
            }
            while (chunky < 0) {
                chunky += world.chunkEdgeLengthOfRegion;
                regiony--;
            }
            while (chunky >= world.chunkEdgeLengthOfRegion) {
                chunky -= world.chunkEdgeLengthOfRegion;
                regiony++;
            }
            while (chunkz < 0) {
                chunkz += world.chunkEdgeLengthOfRegion;
                regionz--;
            }
            while (chunkz >= world.chunkEdgeLengthOfRegion) {
                chunkz -= world.chunkEdgeLengthOfRegion;
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
        if (voxelx < 0) voxelx -= world.blockEdgeLengthOfChunk;
        if (voxely < 0) voxely -= world.blockEdgeLengthOfChunk;
        if (voxelz < 0) voxelz -= world.blockEdgeLengthOfChunk;
        return new Vec3i(voxelx / world.blockEdgeLengthOfChunk, voxely / world.blockEdgeLengthOfChunk, voxelz / world.blockEdgeLengthOfChunk);
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
        return world.chunkEdgeLengthOfRegion;
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

    @Override
    public int getXVoxelOffset() {
        return getX() * getEdgeLength();
    }

    @Override
    public int getYVoxelOffset() {
        return getX() * getEdgeLength();
    }

    @Override
    public int getZVoxelOffset() {
        return getX() * getEdgeLength();
    }
}
