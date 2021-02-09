/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import net.angle.omnicraft.client.DebugClient;
import net.angle.omnicraft.graphics.RenderData;
import net.angle.omnicraft.world.Chunk;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Block {
    
    public final String name;
    public RenderData renderData;
    public int id;
    
    public Block(String name) {
        this.name = name;
    }
    
    public abstract boolean isTransparent();
    
    public boolean isVisibleThrough(Block adjacentBlock) {
        return adjacentBlock == null || adjacentBlock.isTransparent();
    }
    
    public boolean topIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky + 1, blockz));
    }
    
    public boolean bottomIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky - 1, blockz));
    }
    
    public boolean frontIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky, blockz + 1));
    }
    
    public boolean backIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky, blockz - 1));
    }
    
    public boolean leftSideIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx - 1, blocky, blockz));
    }
    
    public boolean rightSideIsVisible(Chunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx + 1, blocky, blockz));
    }
    
    public void bufferTop(DebugClient client, float x, float y, float z) {
        bufferFlatVertices(client, x, y, z - 1, -1, 0, 1);
    }
    
    public void bufferBottom(DebugClient client, float x, float y, float z) {
        bufferFlatVertices(client, x, y - 1, z, -1, 0, -1);
    }
    
    public void bufferFront(DebugClient client, float x, float y, float z) {
        bufferFlatVertices(client, x, y, z, -1, -1, 0);
    }
    
    public void bufferBack(DebugClient client, float x, float y, float z) {
        bufferFlatVertices(client, x - 1, y, z - 1, 1, -1, 0);
    }
    
    public void bufferLeftSide(DebugClient client, float x, float y, float z) {
        bufferFlatVertices(client, x - 1, y, z, 0, -1, -1);
    }
    
    public void bufferRightSide(DebugClient client, float x, float y, float z) {
        bufferFlatVertices(client, x, y, z - 1, 0, -1, 1);
    }
    
    public void bufferVertices(DebugClient client, Chunk chunk, int blockx, int blocky, int blockz) {
        
        //sides are all drawn as though you are standing next to the block facing it, 
        //or in front looking down or up, for the top and bottom.
        
        //Draw top:
        if (topIsVisible(chunk, blockx, blocky, blockz))
            bufferTop(client, chunk.x + blockx, chunk.y + blocky, chunk.z + blockz);
        
        //Draw bottom:
        if (bottomIsVisible(chunk, blockx, blocky, blockz))
            bufferBottom(client, chunk.x + blockx, chunk.y + blocky, chunk.z + blockz);
        
        //Draw front
        if (frontIsVisible(chunk, blockx, blocky, blockz))
            bufferFront(client, chunk.x + blockx, chunk.y + blocky, chunk.z + blockz);
        
        //Draw back
        if (backIsVisible(chunk, blockx, blocky, blockz))
            bufferBack(client, chunk.x + blockx, chunk.y + blocky, chunk.z + blockz);
        
        //Draw left
        if (leftSideIsVisible(chunk, blockx, blocky, blockz))
            bufferLeftSide(client, chunk.x + blockx, chunk.y + blocky, chunk.z + blockz);
        
        //Draw right
        if (rightSideIsVisible(chunk, blockx, blocky, blockz))
            bufferRightSide(client, chunk.x + blockx, chunk.y + blocky, chunk.z + blockz);
    }
    
    public void bufferFlatVertices(DebugClient client, float startx, float starty, float startz, float xoff, float yoff, float zoff) {

        //Build a square out of two triangles.

        Vec3 topLeft, topRight, bottomLeft, bottomRight;

        topLeft = new Vec3(0, 0, 0);

        if (xoff == 0) {
            topRight = new Vec3(0, 0, zoff);
        } else {
            topRight = new Vec3(xoff, 0, 0);
        }

        bottomRight = new Vec3(xoff, yoff, zoff);

        if (yoff == 0) {
            bottomLeft = new Vec3(0, 0, zoff);
        } else{
            bottomLeft = new Vec3(0, yoff, 0);
        }
        
        //adjust positions for where our starts are.
        topLeft.add(new Vec3(startx, starty, startz));
        topRight.add(new Vec3(startx, starty, startz));
        bottomLeft.add(new Vec3(startx, starty, startz));
        bottomRight.add(new Vec3(startx, starty, startz));

        //add first trangle, starting at top left corner, then top right, then bottom right
        client.vPos.set(topLeft); client.vTexCoord.set(0.0f, 0.0f); client.palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(topRight); client.vTexCoord.set(1.0f, 0.0f); client.palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(bottomRight); client.vTexCoord.set(1.0f, 1.0f); client.palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        client.vPos.set(topLeft); client.vTexCoord.set(0.0f, 0.0f); client.palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(bottomRight); client.vTexCoord.set(1.0f, 1.0f); client.palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(bottomLeft); client.vTexCoord.set(0.0f, 1.0f); client.palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
    }
}
