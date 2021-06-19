/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;

/**
 *
 * @author angle
 */
public class Emptiness extends Substance {

    public Emptiness() {
        super("Emptiness");
    }

    @Override
    public Color getColor(OmniRandom random) {
        return Color.BLACK;
    }
    
}
