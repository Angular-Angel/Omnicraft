/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.pixel;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Texture2D;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class CubeTexture extends BlockTexture {
    public Texture2D top, bottom, front, back, left, right;
    
    public CubeTexture(Texture2D top, Texture2D bottom, Texture2D front, Texture2D back, Texture2D left, Texture2D right) {
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
    }
    
    public void draw() {
        
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        drawFlatTexture(top, 0.5f, 0.5f, -0.5f, -1, 0, 1);
        
        //Draw bottom:
        drawFlatTexture(bottom, 0.5f, -0.5f, 0.5f, -1, 0, -1);
        
        //Draw front
        drawFlatTexture(front, 0.5f, 0.5f, 0.5f, -1, -1, 0);
        
        //Draw back
        drawFlatTexture(back, -0.5f, 0.5f, -0.5f, 1, -1, 0);
        
        //Draw left
        drawFlatTexture(left, -0.5f,  0.5f, 0.5f, 0, -1, -1);
        
        //Draw right
        drawFlatTexture(right, 0.5f,  0.5f, -0.5f, 0, -1, 1);
    }

    public void delete() {
        DGL.delete(top);
        DGL.delete(bottom);
        DGL.delete(front);
        DGL.delete(back);
        DGL.delete(left);
        DGL.delete(right);
    }
}
