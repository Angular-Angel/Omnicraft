/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Fluid extends HomogenousSubstance {
    
    private final int opacity;
    
    public Fluid(String name, int opacity) {
        super(name);
        this.opacity = opacity;
    }
}
