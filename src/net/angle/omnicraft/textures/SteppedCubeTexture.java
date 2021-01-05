/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.Texture2D;
import net.angle.omnicraft.client.Player;
import static net.angle.omnicraft.textures.BlockTexture.OFFSET;
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
    public boolean playerIsAbove(int blocky) {
        return Player.player.position.y > blocky + height/16f + OFFSET;
    }
    
    @Override
    public boolean topIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsAbove(blocky + chunk.y);
    }
    
    @Override
    public boolean bottomIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return playerIsBelow(blocky + chunk.y);
    }
    
    @Override
    public void drawTop() {
        drawFlatTexture(top, OFFSET, OFFSET - ((16 - height)/16f), -OFFSET, -1, 0, 1);
    }
    
    //drawBottom is inherited unchanged from CubeTexture.
    
    @Override
    public void drawFront() {
        drawFlatTexture(front, OFFSET, OFFSET - (16 - height)/16f, OFFSET, -1, -height/16f, 0);
    }
    
    @Override
    public void drawBack() {
        drawFlatTexture(back, -OFFSET, OFFSET - (16 - height)/16f, -OFFSET, 1, -height/16f, 0);
    }
    
    @Override
    public void drawLeftSide() {
        drawFlatTexture(left, -OFFSET,  OFFSET - (16 - height)/16f, OFFSET, 0, -height/16f, -1);
    }
    
    @Override
    public void drawRightSide() {
        drawFlatTexture(right, OFFSET,  OFFSET - (16 - height)/16f, -OFFSET, 0, -height/16f, 1);
    }
    
}
