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
public class SoilFraction extends PixelSource {
    
    private SoilComponent component;
    private float fraction;
    
    public SoilFraction(SoilComponent component, float fraction) {
        this.component = component;
        this.fraction = fraction;
    }
    
    /**
     * @return the fraction
     */
    public float getFraction() {
        return fraction;
    }

    /**
     * @param fraction the fraction to set
     */
    public void setFraction(float fraction) {
        this.fraction = fraction;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return component.getPixelColor(random, context);
    }
}
