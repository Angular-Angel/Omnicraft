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
public class Splatter extends Side {

    public Splatter(String name, int id, RenderData renderData) {
        super(name, id, renderData);
    }

    @Override
    public boolean isTransparent() {
        return false;
    }
    
}
