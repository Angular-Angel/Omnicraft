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
public class MixtureComponent {
    
    private final Substance substance;
    private final float fraction;
    
    public MixtureComponent(Substance substance, float fraction) {
        this.substance = substance;
        this.fraction = fraction;
    }
    
    /**
     * @return the fraction
     */
    public float getFraction() {
        return fraction;
    }
}
