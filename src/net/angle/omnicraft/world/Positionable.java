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
public abstract class Positionable {
    
    //These describe this objects x, y, and z coordinates relative to it's containing object.
    public final Vec3i position;
    
    public Positionable(int x, int y, int z) {
        this(new Vec3i(x, y, z));
    }
    
    public Positionable(Vec3i position) {
        this.position = position;
    }
    
    public Vec3i getCoordinates() {
        return new Vec3i(position);
    }
}
