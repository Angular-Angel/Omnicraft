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
public interface SideContainer extends VoxelContainer {
    
    /**
     * Gets the number of blocks in an edge (length, width, or height) for this side container.
     * @return
     */
    public int getEdgeLength();
    
    public Side getSide(BlockFace face, int sidex, int sidey, int sidez);
    
    public default Side getSide(BlockFace face, Vec3i coord) {
        return getSide(face, coord.x, coord.y, coord.z);
    }
    
    public void setSide(BlockFace face, int sidex, int sidey, int sidez, Side side);
    
    public default void setSide(BlockFace face, Vec3i coord, Side side) {
        setSide(face, coord.x, coord.y, coord.z, side);
    }
    
    public default void setAllSides(BlockFace face, Side side) {
        for (int sidex = 0; sidex < getEdgeLength(); sidex++) {
            for (int sidey = 0; sidey < getEdgeLength(); sidey++) {
                for (int sidez = 0; sidez < getEdgeLength(); sidez++) {
                    setSide(face, sidex, sidey, sidez, side);
                }
            }
        }
    }

    public default void setAllSides(Side side) {
        for (BlockFace face : BlockFace.values()) {
            setAllSides(face, side);
        }
    }
}
