/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

/**
 *
 * @author angle
 */
public class Side {
    
    public final String name;
    
    public boolean obscures;
    
    public Side(String name) {
        this(name, false);
    }
    
    public Side(String name, boolean obscures) {
        this.name = name;
        this.obscures = obscures;
    }
}
