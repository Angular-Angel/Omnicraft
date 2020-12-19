/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.world.Chunk;
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
public interface BlockTexture {
    
    default void drawFlatTexture(Texture2D texture, float startx, float starty, float startz, float xoff, float yoff, float zoff) {
        glTranslatef(startx, starty, startz);
        texture.bind();
        glBegin(GL_QUADS);
        
        //draw top left corner of texture
        glTexCoord2f(1.0f, 1.0f); glVertex3f(0, 0, 0);
        
        //draw top right corner of texture
        if (xoff == 0) {
            glTexCoord2f(0.0f, 1.0f); glVertex3f(0, 0, zoff);
        } else {
            glTexCoord2f(0.0f, 1.0f); glVertex3f(xoff, 0, 0);
        }
        
        //draw bottom right corner of texture
        glTexCoord2f(0.0f, 0.0f); glVertex3f(xoff, yoff, zoff);
        
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
    
    public void draw(Chunk chunk, int blockx, int blocky, int blockz);
    
    public void delete();
}
