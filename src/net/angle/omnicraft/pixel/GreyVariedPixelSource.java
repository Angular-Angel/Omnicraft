/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.pixel;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public class GreyVariedPixelSource extends PixelSource {

    private Color mainColor;
    
    private int variation;
    
    public GreyVariedPixelSource(Color mainColor, int variation) {
        this.mainColor = mainColor;
        this.variation = variation;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        int variation = random.getBoundedInt(this.variation);
        
        int red = checkBand(this.mainColor.getRed() + variation);
        int green = checkBand(this.mainColor.getGreen() + variation);
        int blue = checkBand(this.mainColor.getBlue() + variation);
        
        return new Color(red, green, blue);
    }
    
}
