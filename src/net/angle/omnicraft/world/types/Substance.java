/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import net.angle.omnicraft.pixel.PixelSource;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public class Substance extends PixelSource{
    private PixelSource pixelSource;
    
    public Substance(PixelSource pixelSource) {
        this.pixelSource = pixelSource;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return this.pixelSource.getPixelColor(random, context);
    }
}
