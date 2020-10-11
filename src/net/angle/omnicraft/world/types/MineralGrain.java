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
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class MineralGrain extends SoilComponent {
    private final Mineraloid mineraloid;
    private final float grainSize;
    
    public MineralGrain(Mineraloid mineraloid, float grainSize) {
        this.mineraloid = mineraloid;
        this.grainSize = grainSize;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        Color color = this.mineraloid.getPixelColor(random, context);
        
        return color;
    }
}
