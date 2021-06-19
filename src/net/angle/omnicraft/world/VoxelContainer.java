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
public interface VoxelContainer {
    public int getEdgeLength();
    
    public default boolean containsCoordinates(int x, int y, int z) {
        return x >= 0 && x < getEdgeLength() && y >= 0 && y < getEdgeLength() && 
            z >= 0 && z < getEdgeLength();
    }
    
    public int getXVoxelOffset();
    public int getYVoxelOffset();
    public int getZVoxelOffset();
}
