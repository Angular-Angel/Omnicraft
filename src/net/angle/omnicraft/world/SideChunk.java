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
    
    public SideChunk(Chunk container) {
        this.container = container;
    }
    
    @Override
    public int getEdgeLength() {
        return container.getEdgeLength();
    }

    @Override
    public int getXVoxelOffset() {
        return container.getXVoxelOffset();
    }

    @Override
    public int getYVoxelOffset() {
        return container.getYVoxelOffset();
    }

    @Override
    public int getZVoxelOffset() {
        return container.getZVoxelOffset();
    }
}
