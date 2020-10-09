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
public abstract class PixelSource {
    
    public int checkBand(int value) {
        if (value < 0)
            return 0;
        if (value > 255)
            return 255;
        else
            return value;
    }
    
    public abstract Color getPixelColor(OmniRandom random, PixelSource context);
}
