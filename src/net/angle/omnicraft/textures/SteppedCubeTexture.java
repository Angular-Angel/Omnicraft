/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.world.Chunk;

/**
 *
 * @author angle
 */
public class SteppedCubeTexture extends CubeTexture {
    public final int height;
    
    public SteppedCubeTexture(Texture2D top, Texture2D bottom, Texture2D front, Texture2D back, Texture2D left, Texture2D right) {
        this(15, top, bottom, front, back, left, right);
    }

    public SteppedCubeTexture(int height, Texture2D top, Texture2D bottom, Texture2D front, Texture2D back, Texture2D left, Texture2D right) {
        super(top, bottom, front, back, left, right);
        this.height = height;
    }

    @Override
    public void draw(Chunk chunk, int blockx, int blocky, int blockz) {
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        drawFlatTexture(top, OFFSET, OFFSET - ((16 - height)/16f), -OFFSET, -1, 0, 1);
        
        //Draw bottom:
        drawFlatTexture(bottom, OFFSET, -OFFSET, OFFSET, -1, 0, -1);
        
        //Draw front
        drawFlatTexture(front, OFFSET, OFFSET - (16 - height)/16f, OFFSET, -1, -height/16f, 0);
        
        //Draw back
        drawFlatTexture(back, -OFFSET, OFFSET - (16 - height)/16f, -OFFSET, 1, -height/16f, 0);
        
        //Draw left
        drawFlatTexture(left, -OFFSET,  OFFSET - (16 - height)/16f, OFFSET, 0, -height/16f, -1);
        
        //Draw right
        drawFlatTexture(right, OFFSET,  OFFSET - (16 - height)/16f, -OFFSET, 0, -height/16f, 1);
    }
    
}
