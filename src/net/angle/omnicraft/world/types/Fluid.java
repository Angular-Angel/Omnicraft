/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public class Fluid implements PixelSource {

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        Color color = context.getPixelColor(random, context);
        return color;
    }
    
}
