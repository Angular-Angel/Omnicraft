/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import java.awt.Color;
import net.angle.omnicraft.textures.pixels.PixelSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.types.Mixture;

/**
 * This block represents not just dirt, but also gravel, sand, clay, silt, mud, and any mixture thereof.
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class HomogenousBlock extends Block implements PixelSource {
    private final Mixture soilType;
    private final BlockShape blockShape;
    
    public HomogenousBlock(Mixture soilType, BlockShape blockShape, OmniRandom random) {
        super(blockShape.generateBlockTexture(soilType.textureSource, random));
        
        this.soilType = soilType;
        this.blockShape = blockShape;
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return this.soilType.getPixelColor(new OmniRandom(), this);
    }
}
