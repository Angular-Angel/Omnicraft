/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class MixtureComponent implements PixelSource {
    
    private final Substance substance;
    private final float fraction;
    
    public MixtureComponent(Substance substance, float fraction) {
        this.substance = substance;
        this.fraction = fraction;
    }
    
    /**
     * @return the fraction
     */
    public float getFraction() {
        return fraction;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return substance.getPixelColor(random, context);
    }
}
