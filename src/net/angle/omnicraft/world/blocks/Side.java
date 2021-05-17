/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import net.angle.omnicraft.graphics.RenderData;

/**
 *
 * @author angle
 */
public class Side extends Datum {
    
    public final boolean obscures;
    
    public RenderData renderData;
    
    public Side(String name, int id) {
        this(name, id, false);
    }
    
    public Side(String name, int id, boolean obscures) {
        super(name, id);
        this.obscures = obscures;
    }
}
