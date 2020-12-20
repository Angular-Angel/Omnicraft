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
public class OctreeChunk extends Chunk {
    
    private Chunk[][][] children;
    
    public OctreeChunk(Region region) {
        this(region, null, 16, 0, 0, 0);
    }
    
    public OctreeChunk(Region region, Block block) {
        this(region, block, 16, 0, 0, 0);
    }
    
    public OctreeChunk(Region region, Block block, int x, int y, int z) {
        this(region, block, 16, x, y, z);
    }

    public OctreeChunk(Region region, Block block, int size, int x, int y, int z) {
        super(region, size, x, y, z);
        if (size <= 1) throw new IllegalArgumentException("Attempting to create OctreeChunk with size of " + size + "!");
        children = new Chunk[2][2][2];
        for (int octantx = 0; octantx < 2; octantx++) {
            for (int octanty = 0; octanty < 2; octanty++) {
                for (int octantz = 0; octantz < 2; octantz++) {
                    children[octantx][octanty][octantz] = new HomogenousChunk(region, block, this, size/2, octantx * size/2, octanty * size/2, octantz * size/2);
                }
            }
        }
    }
    
    public Vec3i getOctant(int chunkx, int chunky, int chunkz) {
        Vec3i octant = new Vec3i(0, 0, 0);
        
        if (chunkx >= size/2) {
            octant.x = 1;
        }
        
        if (chunky >= size/2) {
            octant.y = 1;
        }
        
        if (chunkz >= size/2) {
            octant.z = 1;
        }
        
        return octant;
    }
    
    public void setOctant(int chunkx, int chunky, int chunkz, Chunk chunk) {
        Vec3i octant = getOctant(chunkx, chunky, chunkz);
        
        children[octant.x][octant.y][octant.z] = chunk;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        
        if (!containsCoordinates(blockx, blocky, blockz))
            return null;
        
        Vec3i octant = getOctant(blockx, blocky, blockz);
        
        if (octant.x == 1) {
            blockx = blockx - size/2;
        }
        
        if (octant.y == 1) {
            blocky = blocky - size/2;
        }
        
        if (octant.z == 1) {
            blockz = blockz - size/2;
        }
        
        return children[octant.x][octant.y][octant.z].getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        Vec3i octant = getOctant(blockx, blocky, blockz);
        
        if (octant.x == 1) {
            blockx = blockx - size/2;
        }
        
        if (octant.y == 1) {
            blocky = blocky - size/2;
        }
        
        if (octant.z == 1) {
            blockz = blockz - size/2;
        }
        
        children[octant.x][octant.y][octant.z].setBlock(blockx, blocky, blockz, block);
    }
    
}
