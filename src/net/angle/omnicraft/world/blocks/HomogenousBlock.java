/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import net.angle.omnicraft.graphics.RenderData;
import net.angle.omnicraft.random.OmniRandom;
import static net.angle.omnicraft.world.World.PALETTE_SIZE;
import net.angle.omnicraft.world.types.Substance;

/**
 * Instances of this class can represent any block made of a single homogenous substance.
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class HomogenousBlock extends Block {
    private final Substance substance;
    private final BlockShape blockShape;
    
    public HomogenousBlock(String name, int id, Substance substance, BlockShape blockShape, OmniRandom random) {
        super(name, id, new RenderData(substance.getPalette(PALETTE_SIZE, random)));
        
        this.substance = substance;
        this.blockShape = blockShape;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }
}
