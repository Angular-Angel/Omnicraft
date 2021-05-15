/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.world.blocks.Block.BlockFace;
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public interface SideContainer {
    
    /**
     * Gets the number of blocks in an edge (length, width, or height) for this side container.
     * @return
     */
    public int getEdgeLength();
    
    public Side getSide(BlockFace face, int blockx, int blocky, int blockz);
    
    public default Side getSide(BlockFace face, Vec3i coord) {
        return getSide(face, coord.x, coord.y, coord.z);
    }
    
    public void setSide(BlockFace face, int blockx, int blocky, int blockz, Side side);
    
    public default void setSide(BlockFace face, Vec3i coord, Side side) {
        setSide(face, coord.x, coord.y, coord.z, side);
    }
    
    public default void setAllSides(BlockFace face, Side side) {
        for (int blockx = 0; blockx < getEdgeLength(); blockx++) {
                for (int blocky = 0; blocky < getEdgeLength(); blocky++) {
                    for (int blockz = 0; blockz < getEdgeLength(); blockz++) {
                        setSide(face, blockx, blocky, blockz, side);
                    }
                }
            }
        }
    }
