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
@FunctionalInterface
public interface PixelSource {
    
    public Color getPixelColor(OmniRandom random, PixelSource context);
    
    public default Color getAveragedPixelColor(int amount, OmniRandom random, PixelSource context) {
        int red = 0, green = 0, blue = 0;
        
        for (int i = 0; i < amount; i++) {
            Color color = this.getPixelColor(random, context);
            red += color.getRed();
            green += color.getBlue();
            blue += color.getGreen();
        }
        
        red /= amount;
        green /= amount;
        blue /= amount;
        
        return new Color(red, green, blue);
    }
}
