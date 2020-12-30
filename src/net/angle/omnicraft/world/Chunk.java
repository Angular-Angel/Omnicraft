/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 *
 * @author angle
 */
public abstract class Chunk implements BlockContainer {
    
    public final ChunkContainer container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    public final int x, y, z;
    
    public Chunk(ChunkContainer container, int x, int y, int z) {
        this.container = container;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLengthOfContainedChunks();
    }
    
    public void draw() {
        int edgeLength = getEdgeLength();
        glPushMatrix();
        glTranslatef(x, y, z);
        for (int blockx = 0; blockx < edgeLength; blockx++) {
            glTranslatef(1, 0, 0);
            glPushMatrix();
            for (int blocky = 0; blocky < edgeLength; blocky++) {
                glTranslatef(0, 1, 0);
                glPushMatrix();
                for (int blockz = 0; blockz < edgeLength; blockz++) {
                    glTranslatef(0, 0, 1);
                    getBlock(blockx, blocky, blockz).draw(this, blockx, blocky, blockz);
                }
                glPopMatrix();
            }
            glPopMatrix();
        }
        glPopMatrix();
    }
}
