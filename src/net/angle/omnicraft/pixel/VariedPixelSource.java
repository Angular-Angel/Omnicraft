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
public class VariedPixelSource extends PixelSource {

    private Color mainColor;
    
    private int[] variation;
    
    public VariedPixelSource(Color mainColor, int redVar, int greenVar, int blueVar) {
        this(mainColor, new int[] {redVar, greenVar, blueVar});
    }
    
    public VariedPixelSource(Color mainColor, int[] variation) {
        this.mainColor = mainColor;
        this.variation = variation;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        int redVar = this.variation[0];
        int greenVar = this.variation[1];
        int blueVar = this.variation[2];
        
        int red = checkBand(this.mainColor.getRed() + random.getBoundedInt(redVar));
        int green = checkBand(this.mainColor.getGreen() + random.getBoundedInt(greenVar));
        int blue = checkBand(this.mainColor.getBlue() + random.getBoundedInt(blueVar));
        return new Color(red, green, blue);
    }
}
