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
public class ArrayBlockChunk extends BlockChunk {
    //incredibly lazy and lame representation, but also very easy.
    private Block[][][] blocks;
    
    public ArrayBlockChunk(BlockChunkContainer container, Block block) {
        this(container, block, 0, 0, 0);
    }
    
    public ArrayBlockChunk(BlockChunkContainer container, Block block, int x, int y, int z) {
        super(container, x, y, z);
        blocks = new Block[16][16][16];
        setAllBlocks(block);
    }
    
    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        
        if (!containsBlockCoordinates(blockx, blocky, blockz)) {
            return container.getBlock(blockx + x, blocky + y, blockz + z);
        }
        return blocks[blockx][blocky][blockz];
    }
    
    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        blocks[blockx][blocky][blockz] = block;
    }

    @Override
    public void setAllBlocks(Block block) {
        for (int blockx = 0; blockx < getEdgeLength(); blockx++) {
            for (int blocky = 0; blocky < getEdgeLength(); blocky++) {
                for (int blockz = 0; blockz < getEdgeLength(); blockz++) {
                    blocks[blockx][blocky][blockz] = block;
                }
            }
        }
    }
}
