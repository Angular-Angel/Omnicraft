/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

/**
 * I'm going to have to rewrite this if I ever want transparent minerals... 
 * Oh well, I'll deal with it then.
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Mineraloid extends HomogenousSubstance {
    
    private final int grainSize;
    
    public Mineraloid(String name, int grainSize) {
        super(name);
        this.grainSize = grainSize;
    }
}
