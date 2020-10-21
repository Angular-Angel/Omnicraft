/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelVariation;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Fluid extends Substance {

    private final PixelVariation variation;
    
    public Fluid(String name, PixelVariation variation) {
        super(name);
        this.variation = variation;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return variation.varyPixel(context.getPixelColor(random, context), random);
    }
}
