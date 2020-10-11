/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.angle.omnicraft.pixel.PixelSource;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class SoilType implements PixelSource {
    private final List<SoilFraction> components;
    
    public SoilType(SoilFraction... components) {
        this.components = new ArrayList();
        
        this.components.addAll(Arrays.asList(components));
    }
    
    /*public Image generateImage() {
        Image image = new Image();
        image.
    }*/

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return this.components.get(0).getPixelColor(random, context);
    }
}
