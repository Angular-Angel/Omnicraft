/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.world.Chunk;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class CubeTexture implements BlockTexture {
    public final Texture2D top, bottom, front, back, left, right;
    
    public CubeTexture(Texture2D top, Texture2D bottom, Texture2D front, Texture2D back, Texture2D left, Texture2D right) {
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public void drawTop() {
        drawFlatTexture(top, OFFSET, OFFSET, -OFFSET, -1, 0, 1);
    }
    
    @Override
    public void drawBottom() {
        drawFlatTexture(bottom, OFFSET, -OFFSET, OFFSET, -1, 0, -1);
    }
    
    @Override
    public void drawFront() {
        drawFlatTexture(front, OFFSET, OFFSET, OFFSET, -1, -1, 0);
    }
    
    @Override
    public void drawBack() {
        drawFlatTexture(back, -OFFSET, OFFSET, -OFFSET, 1, -1, 0);
    }
    
    @Override
    public void drawLeftSide() {
        drawFlatTexture(left, -OFFSET, OFFSET, OFFSET, 0, -1, -1);
    }
    
    @Override
    public void drawRightSide() {
        drawFlatTexture(right, OFFSET, OFFSET, -OFFSET, 0, -1, 1);
    }
    
    @Override
    public void draw(Chunk chunk, int blockx, int blocky, int blockz) {
        
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        if (topIsVisible(chunk, blockx, blocky, blockz))
            drawTop();
        
        //Draw bottom:
        if (bottomIsVisible(chunk, blockx, blocky, blockz))
            drawBottom();
        
        //Draw front
        if (frontIsVisible(chunk, blockx, blocky, blockz))
            drawFront();
        
        //Draw back
        if (backIsVisible(chunk, blockx, blocky, blockz))
            drawBack();
        
        //Draw left
        if (leftSideIsVisible(chunk, blockx, blocky, blockz))
            drawLeftSide();
        
        //Draw right
        if (rightSideIsVisible(chunk, blockx, blocky, blockz))
            drawRightSide();
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
