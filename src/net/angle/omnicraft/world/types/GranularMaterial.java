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
public class GranularMaterial extends SoilComponent {
    private final Substance substance;
    
    public GranularMaterial(Substance substance) {
        this.substance = substance;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        Color color = this.substance.getPixelColor(random, context);
        
        return color;
    }
}
