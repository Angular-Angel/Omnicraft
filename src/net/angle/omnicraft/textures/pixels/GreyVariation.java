/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures.pixels;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import static net.angle.omnicraft.textures.pixels.PixelVariation.checkBand;

/**
 *
 * @author angle
 */
public class GreyVariation implements PixelVariation {
    
    private final int variation;
    
    public GreyVariation(int variation) {
        this.variation = variation;
    }

    @Override
    public Color varyPixel(Color pixel, OmniRandom random) {
        int variation = random.getBoundedInt(this.variation);
        
        int red = checkBand(pixel.getRed() + variation);
        int green = checkBand(pixel.getGreen() + variation);
        int blue = checkBand(pixel.getBlue() + variation);
        
        return new Color(red, green, blue);
    }
}
