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
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public interface BlockTexture {
    
    public final float OFFSET = 0.5f;
    
    default void drawFlatTexture(Texture2D texture, float startx, float starty, float startz, float xoff, float yoff, float zoff) {
        glPushMatrix();
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
        glPopMatrix();
    }
    
    public default boolean playerIsAbove(int blocky) {
        return Player.player.position.y > blocky + 1 + OFFSET;
    }
    
    public default boolean playerIsBelow(int blocky) {
        return Player.player.position.y < blocky + OFFSET;
    }
    
    public default boolean playerIsInFront(int blockz) {
        return Player.player.position.z > blockz + 1 + OFFSET;
    }
    
    public default boolean playerIsBehind(int blockz) {
        return Player.player.position.z < blockz + OFFSET;
    }
    
    public default boolean playerIsToTheLeft(int blockx) {
        return Player.player.position.x < blockx + OFFSET;
    }
    
    public default boolean playerIsToTheRight(int blockx) {
        return Player.player.position.x > blockx + 1 + OFFSET;
    }
    
    public default boolean isVisibleThrough(Block adjacentBlock) {
        return adjacentBlock == null || adjacentBlock.isTransparent();
    }
    
    public default boolean topIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsAbove(blocky + chunk.y) && isVisibleThrough(chunk.getBlock(blockx, blocky + 1, blockz));
    }
    
    public default boolean bottomIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsBelow(blocky + chunk.y) && isVisibleThrough(chunk.getBlock(blockx, blocky - 1, blockz));
    }
    
    public default boolean frontIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsInFront(blockz + chunk.z) && isVisibleThrough(chunk.getBlock(blockx, blocky, blockz + 1));
    }
    
    public default boolean backIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsBehind(blockz + chunk.z) && isVisibleThrough(chunk.getBlock(blockx, blocky, blockz - 1));
    }
    
    public default boolean leftSideIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsToTheLeft(blockx + chunk.x) && isVisibleThrough(chunk.getBlock(blockx - 1, blocky, blockz));
    }
    
    public default boolean rightSideIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsToTheRight(blockx + chunk.x) && isVisibleThrough(chunk.getBlock(blockx + 1, blocky, blockz));
    }
    
    public default void draw(Chunk chunk, int blockx, int blocky, int blockz) {
        
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
    
    public void delete();

    public void drawTop();

    public void drawBottom();

    public void drawFront();

    public void drawBack();

    public void drawLeftSide();

    public void drawRightSide();
}
