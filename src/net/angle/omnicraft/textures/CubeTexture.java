/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.world.Chunk;
import net.angle.omnicraft.world.blocks.Block;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class CubeTexture implements BlockTexture {
    public final Texture2D top, bottom, front, back, left, right;
    
    public final float OFFSET = 0.5f;
    
    public CubeTexture(Texture2D top, Texture2D bottom, Texture2D front, Texture2D back, Texture2D left, Texture2D right) {
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public void draw(Chunk chunk, int blockx, int blocky, int blockz) {
        
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        Block adjacentBlock = chunk.getBlock(blockx, blocky + 1, blockz);
        if (adjacentBlock == null || adjacentBlock.isTransparent())
            drawFlatTexture(top, OFFSET, OFFSET, -OFFSET, -1, 0, 1);
        
        //Draw bottom:
        adjacentBlock = chunk.getBlock(blockx, blocky - 1, blockz);
        if (adjacentBlock == null || adjacentBlock.isTransparent())
            drawFlatTexture(bottom, OFFSET, -OFFSET, OFFSET, -1, 0, -1);
        
        //Draw front
        adjacentBlock = chunk.getBlock(blockx, blocky, blockz + 1);
        if (adjacentBlock == null || adjacentBlock.isTransparent())
            drawFlatTexture(front, OFFSET, OFFSET, OFFSET, -1, -1, 0);
        
        //Draw back
        adjacentBlock = chunk.getBlock(blockx, blocky, blockz - 1);
        if (adjacentBlock == null || adjacentBlock.isTransparent())
            drawFlatTexture(back, -OFFSET, OFFSET, -OFFSET, 1, -1, 0);
        
        //Draw left
        adjacentBlock = chunk.getBlock(blockx - 1, blocky, blockz);
        if (adjacentBlock == null || adjacentBlock.isTransparent())
            drawFlatTexture(left, -OFFSET, OFFSET, OFFSET, 0, -1, -1);
        
        //Draw right
        adjacentBlock = chunk.getBlock(blockx + 1, blocky, blockz);
        if (adjacentBlock == null || adjacentBlock.isTransparent())
            drawFlatTexture(right, OFFSET, OFFSET, -OFFSET, 0, -1, 1);
    }

    @Override
    public void delete() {
        DGL.delete(top);
        DGL.delete(bottom);
        DGL.delete(front);
        DGL.delete(back);
        DGL.delete(left);
        DGL.delete(right);
    }
}
