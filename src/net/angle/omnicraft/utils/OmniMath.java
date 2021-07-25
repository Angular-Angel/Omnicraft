/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.utils;

/**
 *
 * @author angle
 */
public class OmniMath {
    public static float fand(float a, float b) {
        return a + b - a * b;
    }

    public static float startingInterp(float interpolator) {
        return interpolator * interpolator;
    }

    public static float endingInterp(float interpolator) {
        return 1 - startingInterp(1 - interpolator);
    }
    
    public static float mix(float start, float end, float interpolator) {
        return start + (end - start) * interpolator;
    }
    
    public static int mix(int start, int end, float interpolator) {
        return (int) (start + (end - start) * interpolator);
    }

    public static float smoothInterp(float interpolator){
        float start = startingInterp(interpolator);
        float end = endingInterp(interpolator);
        return mix(start, end, interpolator);
    }
}
