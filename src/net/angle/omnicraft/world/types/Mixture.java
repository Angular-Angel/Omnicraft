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
import net.angle.omnicraft.utils.OmniRandom;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Mixture extends Substance {
    private final List<MixtureComponent> components;
    
    public Mixture(String name, MixtureComponent... components) {
        super(name);
        
        this.components = new ArrayList();
        this.components.addAll(Arrays.asList(components));
    }
    
    public MixtureComponent pickComponent(OmniRandom random) {
        int index = 0;
        for (int roll = random.getBoundedInt(100); roll > 0; index++) {
            if (index >= this.components.size())
                index = 0;
            roll -= this.components.get(index).fraction;
        }
        
        if (index >= this.components.size())
                index = 0;
        
        return this.components.get(index);
    }

    @Override
    public Color getColor(OmniRandom random) {
        
        Color color = new Color(0, 0, 0);
        
        int alpha = 0;
        //Sample a bunch of the stuff this Mixture is made of and return a color based on the combination.
        while (alpha < 255) {
            Color newColor = pickComponent(random).substance.getColor(random);
            
            int newAlpha = Math.min(newColor.getAlpha(), 255 - alpha);
            
            int red = (int) Math.min(color.getRed() + (newColor.getRed() * (newAlpha/255.0f)), 255);
            int green = (int) Math.min(color.getGreen() + (newColor.getGreen() * (newAlpha/255.0f)), 255);
            int blue = (int) Math.min(color.getBlue() + (newColor.getBlue() * (newAlpha/255.0f)), 255);
            alpha = Math.min(alpha + newAlpha, 255);
            
            color = new Color(red, green, blue);
        }
        
        return color;
    }
}
