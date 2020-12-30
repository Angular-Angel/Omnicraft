/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block;
import com.samrj.devil.math.Vec3i;

/**
 *
 * @author angle
 */
public class OctreeChunk extends Chunk implements ChunkContainer {
    
    private Chunk[][][] children;
    
    public OctreeChunk(ChunkContainer container) {
        this(container, null, 0, 0, 0);
    }
    
    public OctreeChunk(ChunkContainer container, Block block) {
        this(container, block, 0, 0, 0);
    }

    public OctreeChunk(ChunkContainer container, Block block, int x, int y, int z) {
        super(container, x, y, z);
        int edgeLength = getBlockEdgeLegth();
        if (getBlockEdgeLegth() <= 1) throw new IllegalArgumentException("Attempting to create OctreeChunk with size of " + edgeLength + "!");
        children = new Chunk[2][2][2];
        for (int octantx = 0; octantx < 2; octantx++) {
            for (int octanty = 0; octanty < 2; octanty++) {
                for (int octantz = 0; octantz < 2; octantz++) {
                    children[octantx][octanty][octantz] = new HomogenousChunk(this, block, edgeLength/2, octantx * edgeLength/2, octanty * edgeLength/2, octantz * edgeLength/2);
                }
            }
        }
    }
    
    @Override
    public Vec3i getChunkCoordinatesOfBlock(int chunkx, int chunky, int chunkz) {
        Vec3i octant = new Vec3i(0, 0, 0);
        
        if (chunkx >= getBlockEdgeLegth()/2) {
            octant.x = 1;
        }
        
        if (chunky >= getBlockEdgeLegth()/2) {
            octant.y = 1;
        }
        
        if (chunkz >= getBlockEdgeLegth()/2) {
            octant.z = 1;
        }
        
        return octant;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        
        if (!containsBlockCoordinates(blockx, blocky, blockz)) {
            return container.getBlock(blockx + x, blocky + y, blockz + z);
        }
        
        Vec3i octant = getChunkCoordinatesOfBlock(blockx, blocky, blockz);
        
        if (octant.x == 1) {
            blockx = blockx - getBlockEdgeLegth()/2;
        }
        
        if (octant.y == 1) {
            blocky = blocky - getBlockEdgeLegth()/2;
        }
        
        if (octant.z == 1) {
            blockz = blockz - getBlockEdgeLegth()/2;
        }
        
        return children[octant.x][octant.y][octant.z].getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        Vec3i octant = getChunkCoordinatesOfBlock(blockx, blocky, blockz);
        
        if (octant.x == 1) {
            blockx = blockx - getBlockEdgeLegth()/2;
        }
        
        if (octant.y == 1) {
            blocky = blocky - getBlockEdgeLegth()/2;
        }
        
        if (octant.z == 1) {
            blockz = blockz - getBlockEdgeLegth()/2;
        }
        
        children[octant.x][octant.y][octant.z].setBlock(blockx, blocky, blockz, block);
    }

    @Override
    public void setAllBlocks(Block block) {
        for (int octantx = 0; octantx < 2; octantx++) {
            for (int octanty = 0; octanty < 2; octanty++) {
                for (int octantz = 0; octantz < 2; octantz++) {
                    children[octantx][octanty][octantz].setAllBlocks(block);
                }
            }
        }
    }

    @Override
    public Chunk getChunk(int chunkx, int chunky, int chunkz) {
        return children[chunkx][chunky][chunkz];
    }

    @Override
    public void setChunk(int chunkx, int chunky, int chunkz, Chunk chunk) {
        children[chunkx][chunky][chunkz] = chunk;
    }

    @Override
    public Chunk getChunkOfBlock(int chunkx, int chunky, int chunkz) {
        Vec3i octant = new Vec3i(0, 0, 0);
        
        return getChunk(octant.x, octant.y, octant.z);
    }

    @Override
    public void setChunkOfBlock(int blockx, int blocky, int blockz, Chunk chunk) {
        Vec3i octant = getChunkCoordinatesOfBlock(blockx, blocky, blockz);
        
        setChunk(octant.x, octant.y, octant.z, chunk);
    }

    @Override
    public int getChunkEdgeLength() {
        return getBlockEdgeLegth()/2;
    }
    
}
