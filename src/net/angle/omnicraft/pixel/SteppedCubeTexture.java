/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.pixel;

import com.samrj.devil.gl.Texture2D;

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
    public void draw() {
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        drawFlatTexture(top, 0.5f, 0.5f - ((16 - height)/16f), -0.5f, -1, 0, 1);
        
        //Draw bottom:
        drawFlatTexture(bottom, 0.5f, -0.5f, 0.5f, -1, 0, -1);
        
        //Draw front
        drawFlatTexture(front, 0.5f, 0.5f - (16 - height)/16f, 0.5f, -1, -height/16f, 0);
        
        //Draw back
        drawFlatTexture(back, -0.5f, 0.5f - (16 - height)/16f, -0.5f, 1, -height/16f, 0);
        
        //Draw left
        drawFlatTexture(left, -0.5f,  0.5f - (16 - height)/16f, 0.5f, 0, -height/16f, -1);
        
        //Draw right
        drawFlatTexture(right, 0.5f,  0.5f - (16 - height)/16f, -0.5f, 0, -height/16f, 1);
    }
    
}
