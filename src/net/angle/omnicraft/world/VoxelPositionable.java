/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.math.Vec3i;

/**
 *
 * @author angle
 */
public abstract class VoxelPositionable {
    
    //These describe this objects x, y, and z coordinates relative to it's containing object.
    private final Vec3i position;
    
    public VoxelPositionable(int x, int y, int z) {
        this(new Vec3i(x, y, z));
    }
    
    public int getX() {
        return position.x;
    }
    
    public int getY() {
        return position.y;
    }
    
    public int getZ() {
        return position.z;
    }
    
    public VoxelPositionable(Vec3i position) {
        this.position = position;
    }
    
    public Vec3i getCoordinates() {
        return new Vec3i(position);
    }
    
    public int axialDist(VoxelPositionable positionable) {
        Vec3i other = positionable.getCoordinates();
        return Math.max(Math.abs(other.x - position.x), Math.max(Math.abs(other.y - position.y), Math.abs(other.z - position.z)));
    }
}
