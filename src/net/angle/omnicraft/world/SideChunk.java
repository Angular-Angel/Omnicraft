/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

/**
 *
 * @author angle
 */
public abstract class SideChunk implements SideContainer {

    public final Chunk container;
    
    public final int x, y, z;
    
    public SideChunk(Chunk container, int x, int y, int z) {
        this.container = container;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLength();
    }
}
