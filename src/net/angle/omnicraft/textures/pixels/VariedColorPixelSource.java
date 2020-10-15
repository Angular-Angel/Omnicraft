/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures.pixels;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class VariedColorPixelSource implements PixelSource {

    private Color mainColor;
    private PixelVariation variation;
    
    public VariedColorPixelSource(Color mainColor, int redVar, int greenVar, int blueVar) {
        this(mainColor, new int[] {redVar, greenVar, blueVar});
    }
    
    public VariedColorPixelSource(Color mainColor, int variation) {
        this.mainColor = mainColor;
        this.variation = new GreyVariation(variation);
    }
    
    public VariedColorPixelSource(Color mainColor, int[] variation) {
        this.mainColor = mainColor;
        this.variation = new ColoredVariation(variation);
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return variation.varyPixel(mainColor, random);
    }
}
