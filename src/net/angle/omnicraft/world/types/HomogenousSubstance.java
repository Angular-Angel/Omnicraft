/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import net.angle.omnicraft.textures.TextureSource;
import net.angle.omnicraft.textures.pixels.PixelSource;

/**
 *
 * @author angle
 */
public abstract class HomogenousSubstance extends Substance {

    protected final PixelSource pixelSource;
    
    public HomogenousSubstance(String name, PixelSource pixelSource) {
        this(name, null, pixelSource);
    }
    
    public HomogenousSubstance(String name, TextureSource textureSource, PixelSource pixelSource) {
        super(name, textureSource);
        this.pixelSource = pixelSource;
    }
    
}
