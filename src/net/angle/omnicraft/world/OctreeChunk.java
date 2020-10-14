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
        children = new Chunk[2][2][2];
        for (int octantx = 0; octantx < 2; octantx++) {
            for (int octanty = 0; octanty < 2; octanty++) {
                for (int octantz = 0; octantz < 2; octantz++) {
                    children[octantx][octanty][octantz] = new HomogenousChunk(block, this, size/2, octantx * size/2, octanty * size/2, octantz * size/2);
                }
            }
        }
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        int octantx = 0;
        int octanty = 0;
        int octantz = 0;
        
        if (x >= size/2) {
            octantx = 1;
            x = x - size/2;
        }
        
        if (y >= size/2) {
            octanty = 1;
            y = y - size/2;
        }
        
        if (z >= size/2) {
            octantz = 1;
            z = z - size/2;
        }
        
        return children[octantx][octanty][octantz].getBlock(x, y, z);
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        int octantx = 0;
        int octanty = 0;
        int octantz = 0;
        
        if (x >= size/2) {
            octantx = 1;
            x = x - size/2;
        }
        
        if (y >= size/2) {
            octanty = 1;
            y = y - size/2;
        }
        
        if (z >= size/2) {
            octantz = 1;
            z = z - size/2;
        }
        
        children[octantx][octanty][octantz].setBlock(block, x, y, z);
    }
    
}
