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
public class ColoredVariation implements PixelVariation {

    private final int[] variation;
    
    public ColoredVariation(int redVar, int greenVar, int blueVar) {
        this(new int[] {redVar, greenVar, blueVar});
    }
    
    public ColoredVariation(int[] variation) {
        this.variation = variation;
    }
    
    @Override
    public Color varyPixel(Color pixel, OmniRandom random) {
        int red = checkBand(pixel.getRed() + random.getBoundedInt(this.variation[0]));
        int green = checkBand(pixel.getGreen() + random.getBoundedInt(this.variation[1]));
        int blue = checkBand(pixel.getBlue() + random.getBoundedInt(this.variation[2]));
        
        return new Color(red, green, blue, pixel.getAlpha());
    }
    
}
