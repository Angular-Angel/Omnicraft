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
public abstract class Datum {
    
    public final String name;
    public final int id;
    
    public Datum(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
