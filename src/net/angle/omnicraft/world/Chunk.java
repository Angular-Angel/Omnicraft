/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public class Chunk extends AbstractChunk implements BlockContainer, SideContainer {
    public BlockChunk blockChunk;
    public SideChunk sideChunk;

    public Chunk(ChunkContainer container, Block block, int x, int y, int z) {
        super(container, x, y, z);
        
        blockChunk = new OctreeChunk(container, block, x, y, z);
    }

    @Override
    public Block getBlock(int blockx, int blocky, int blockz) {
        return blockChunk.getBlock(blockx, blocky, blockz);
    }

    @Override
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        blockChunk.setBlock(blockx, blocky, blockz, block);
    }

    @Override
    public Side getSide(Block.BlockFace face, int blockx, int blocky, int blockz) {
        return sideChunk.getSide(face, blockx, blocky, blockz);
    }

    @Override
    public void setSide(Block.BlockFace face, int blockx, int blocky, int blockz, Side side) {
        sideChunk.setSide(face, blockx, blocky, blockz, side);
    }
}
