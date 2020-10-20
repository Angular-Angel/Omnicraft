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
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.random.OmniRandom;

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
            roll -= this.components.get(index).getFraction();
        }
        
        if (index >= this.components.size())
                index = 0;
        
        return this.components.get(index);
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return pickComponent(random).getPixelColor(random, context);
    }
}
