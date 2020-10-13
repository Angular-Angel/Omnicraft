/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import java.awt.Color;
import net.angle.omnicraft.pixel.CubeTexture;
import net.angle.omnicraft.pixel.PixelSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.types.SoilType;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class SoilBlock extends Block implements PixelSource {
    private final SoilType soilType;
    
    public SoilBlock(SoilType soilType, OmniRandom random) {
        super(new CubeTexture(soilType.generateTexture(random), soilType.generateTexture(random),
                              soilType.generateTexture(random), soilType.generateTexture(random), 
                              soilType.generateTexture(random), soilType.generateTexture(random)));
        
        this.soilType = soilType;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return this.soilType.getPixelColor(new OmniRandom(), this);
    }
}
