/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.pixel;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Texture2D;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class CubeTexture {
    private Texture2D top, bottom, front, back, left, right;
    
    public CubeTexture(Texture2D top, Texture2D bottom, Texture2D front, Texture2D back, Texture2D left, Texture2D right) {
        this.top = top;
        this.bottom = bottom;
        this.front = front;
        this.back = back;
        this.left = left;
        this.right = right;
    }
    
    public void drawSide(Texture2D texture, float startx, float starty, float startz, float xoff, float yoff, float zoff) {
        glTranslatef(startx, starty, startz);
        texture.bind();
        glBegin(GL_QUADS);
        
        //draw top left corner of texture
        glTexCoord2f(0.0f, 0.0f); glVertex3f(0, 0, 0);
        
        //draw top right corner of texture
        if (xoff == 0) {
            glTexCoord2f(0.0f, 1.0f); glVertex3f(0, 0, zoff);
        } else {
            glTexCoord2f(0.0f, 1.0f); glVertex3f(xoff, 0, 0);
        }
        
        //draw bottom right corner of texture
        glTexCoord2f(1.0f, 1.0f); glVertex3f(xoff, yoff, zoff);
        
        //draw bottom left corner of texture
        if (yoff == 0) {
            glTexCoord2f(1.0f, 0.0f); glVertex3f(0, 0, zoff);
        } else{
            glTexCoord2f(1.0f, 0.0f); glVertex3f(0, yoff, 0);
        }
        
        glEnd();
        texture.unbind();
        glTranslatef(-startx, -starty, -startz);
    }
    
    public void draw() {
        
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        drawSide(top, 0.5f, 0.5f, -0.5f, -1, 0, 1);
        
        //Draw bottom:
        drawSide(bottom, 0.5f, -0.5f, 0.5f, -1, 0, -1);
        
        //Draw front
        drawSide(front, 0.5f, 0.5f, 0.5f, -1, -1, 0);
        
        //Draw back
        drawSide(back, -0.5f, 0.5f, -0.5f, 1, -1, 0);
        
        //Draw left
        drawSide(left, -0.5f,  0.5f, 0.5f, 0, -1, -1);
        
        //Draw right
        drawSide(right, 0.5f,  0.5f, -0.5f, 0, -1, 1);
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
