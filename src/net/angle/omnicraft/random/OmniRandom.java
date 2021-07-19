/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.random;

import java.util.Random;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class OmniRandom extends Random {
    public int getIntBetween(int min, int max) {
        if (max == 0 && min == 0)
            return 0;
        else
            return min + this.nextInt(max - min);
    }
    
    public float getFloatBetween(float min, float max) {
        return min + (this.nextFloat() * (max - min));
    }
    
    public int getBoundedInt(int bound) {
        if (bound > 0)
            return this.getIntBetween(0, bound);
        else
            return this.getIntBetween(bound, 0);
    }
    
    public float getBoundedFloat(float bound) {
        if (bound > 0)
            return this.getFloatBetween(0, bound);
        else
            return this.getFloatBetween(bound, 0);
    }
    
    public float fand(float a, float b) {
        return a + b - a * b;
    }

    public float startingInterp(float interpolator){
        return interpolator * interpolator;
    }

    public float endingInterp(float interpolator){
        return 1 - startingInterp(1 - interpolator);
    }
    
    public float mix(float start, float end, float interpolator) {
        return start + (end - start) * interpolator;
    }

    public float smoothInterp(float interpolator){
        float start = startingInterp(interpolator);
        float end = endingInterp(interpolator);
        return mix(start, end, interpolator);
    }
}
