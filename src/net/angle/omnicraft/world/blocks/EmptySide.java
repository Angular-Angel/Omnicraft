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
public class EmptySide extends Side {
    
    public EmptySide() {
        super("Nothingness", 0, new RenderData());
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean isDrawable() {
        return false;
    }
    
}