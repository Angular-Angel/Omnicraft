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
}
