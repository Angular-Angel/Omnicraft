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
public class Side extends Renderable {
    
    public final boolean obscures;
    
    public RenderData renderData;
    
    public Side(String name, int id, RenderData renderData) {
        this(name, id, false, renderData);
    }
    
    public Side(String name, int id, boolean obscures, RenderData renderData) {
        super(name, id, renderData);
        this.obscures = obscures;
    }
}
