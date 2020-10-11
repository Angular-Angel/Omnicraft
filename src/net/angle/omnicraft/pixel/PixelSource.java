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
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public interface PixelSource {
    
    public static int checkBand(int value) {
        if (value < 0)
            return 0;
        if (value > 255)
            return 255;
        else
            return value;
    }
    
    public Color getPixelColor(OmniRandom random, PixelSource context);
}
