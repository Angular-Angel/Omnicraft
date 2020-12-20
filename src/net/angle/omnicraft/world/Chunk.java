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
public abstract class Chunk extends BlockContainer {
    
    public final ChunkContainer container;
    //These describe this chunks size, and it's x, y, and z coordinates within it's region. 
    //These coords are specific to the chunk, with no regard for the wider world.
    public final int x, y, z;
    
    public Chunk(ChunkContainer container, int size, int x, int y, int z) {
        super(size);
        this.container = container;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void draw() {
        glPushMatrix();
        glTranslatef(x, y, z);
        for (int blockx = 0; blockx < size; blockx++) {
            glTranslatef(1, 0, 0);
            glPushMatrix();
            for (int blocky = 0; blocky < size; blocky++) {
                glTranslatef(0, 1, 0);
                glPushMatrix();
                for (int blockz = 0; blockz < size; blockz++) {
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
