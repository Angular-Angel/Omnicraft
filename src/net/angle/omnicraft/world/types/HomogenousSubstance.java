/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import net.angle.omnicraft.graphics.ColorSource;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public abstract class HomogenousSubstance extends Substance {
    
    public final ColorSource colorSource;
    
    public HomogenousSubstance(String name, ColorSource colorSource) {
        super(name);
        this.colorSource = colorSource;
    }
    
    @Override
    public Color getColor(OmniRandom random) {
        return colorSource.getColor(random);
    }
}
