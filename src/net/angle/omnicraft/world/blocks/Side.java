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
public abstract class Side extends Renderable {
    
    public Side(String name, int id, RenderData renderData) {
        super(name, id, renderData);
    }
}
