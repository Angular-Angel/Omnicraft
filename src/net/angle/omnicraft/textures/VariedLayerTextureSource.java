/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.textures.pixels.PixelVariation;

/**
 *
 * @author angle
 */
public class VariedLayerTextureSource extends LayeredTextureSource {
    
    public final PixelVariation lineVariation;
    
    public final float chanceToVaryColor, chanceToRandomizeColor;
    
    public VariedLayerTextureSource(PixelSource pixelSource, PixelVariation lineVariation, float chanceToVaryColor, float chanceToRandomizeColor) {
        super(pixelSource);
        this.lineVariation = lineVariation;
        this.chanceToVaryColor = chanceToVaryColor;
        this.chanceToRandomizeColor = chanceToRandomizeColor;
    }
    
    @Override
    public Color varyLineColor(int x, int y, Color currentLineColor, OmniRandom random) {
        if (random.nextFloat() <= chanceToRandomizeColor)
            return this.getPixelColor(random, this);
        else if (random.nextFloat() <= chanceToVaryColor)
            return lineVariation.varyPixel(currentLineColor, random);
        else return currentLineColor;
    }
    
}
