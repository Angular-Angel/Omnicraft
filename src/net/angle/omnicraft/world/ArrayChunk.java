/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block;

/**
 * This class represents a cube of blocks in the map. 
 * I am probably going to have to redo it repeatedly, but, oh well.
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class ArrayChunk extends Chunk {
    //incredibly lazy and lame representation, but also very easy.
    private Block[][][] blocks;
    
    public ArrayChunk(Region region, Block block) {
        this(region, block, 16, 0, 0, 0);
    }
    
    public ArrayChunk(Region region, Block block, int size, int x, int y, int z) {
        super(region, size, x, y, z);
        blocks = new Block[16][16][16];
        for (int blockx = 0; blockx < size; blockx++) {
            for (int blocky = 0; blocky < size; blocky++) {
                for (int blockz = 0; blockz < size; blockz++) {
                    blocks[blockx][blocky][blockz] = block;
                }
            }
        }
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        return blocks[blockx][blocky][blockz];
    }
    
    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        blocks[blockx][blocky][blockz] = block;
    }
}
