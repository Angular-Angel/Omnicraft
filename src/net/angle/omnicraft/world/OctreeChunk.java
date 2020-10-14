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
public class OctreeChunk extends Chunk {
    
    private OctreeChunk parent;
    
    private Chunk[][][] children;
    
    public OctreeChunk() {
        this(null, 16, 0, 0, 0);
    }
    
    public OctreeChunk(Block block) {
        this(block, 16, 0, 0, 0);
    }
    
    public OctreeChunk(Block block, int x, int y, int z) {
        this(block, 16, x, y, z);
    }

    public OctreeChunk(Block block, int size, int x, int y, int z) {
        super(size, x, y, z);
        if (size <= 1) throw new IllegalArgumentException("Attempting to create OctreeChunk with size of " + size + "!");
        children = new Chunk[2][2][2];
        for (int octantx = 0; octantx < 2; octantx++) {
            for (int octanty = 0; octanty < 2; octanty++) {
                for (int octantz = 0; octantz < 2; octantz++) {
                    children[octantx][octanty][octantz] = new HomogenousChunk(block, this, size/2, octantx * size/2, octanty * size/2, octantz * size/2);
                }
            }
        }
    }
    
    //It's gross that both of these use almost the same boilerplate, but I can't see any way to condense them, so. :/
    
    public void setOctant(int chunkx, int chunky, int chunkz, Chunk chunk) {
        int octantx = 0;
        int octanty = 0;
        int octantz = 0;
        
        if (chunkx >= size/2) {
            octantx = 1;
        }
        
        if (chunky >= size/2) {
            octanty = 1;
        }
        
        if (chunkz >= size/2) {
            octantz = 1;
        }
        
        children[octantx][octanty][octantz] = chunk;
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        int octantx = 0;
        int octanty = 0;
        int octantz = 0;
        
        if (blockx >= size/2) {
            octantx = 1;
            blockx = blockx - size/2;
        }
        
        if (blocky >= size/2) {
            octanty = 1;
            blocky = blocky - size/2;
        }
        
        if (blockz >= size/2) {
            octantz = 1;
            blockz = blockz - size/2;
        }
        
        return children[octantx][octanty][octantz].getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        int octantx = 0;
        int octanty = 0;
        int octantz = 0;
        
        if (blockx >= size/2) {
            octantx = 1;
            blockx = blockx - size/2;
        }
        
        if (blocky >= size/2) {
            octanty = 1;
            blocky = blocky - size/2;
        }
        
        if (blockz >= size/2) {
            octantz = 1;
            blockz = blockz - size/2;
        }
        
        children[octantx][octanty][octantz].setBlock(blockx, blocky, blockz, block);
    }
    
}
