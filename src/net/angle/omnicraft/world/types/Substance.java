/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import net.angle.omnicraft.graphics.ColorSource;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Substance implements ColorSource {
    public final String name;
    
    public Substance(String name) {
        this.name = name;
    }
}
