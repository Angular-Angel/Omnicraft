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
public interface Chunk {
    public Block getBlock(int x, int y, int z);
    public void setBlock(int x, int y, int z, Block block);
    public void draw();
}
