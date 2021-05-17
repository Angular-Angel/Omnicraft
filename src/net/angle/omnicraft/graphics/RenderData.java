/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.math.Vec3;
import java.awt.Color;
import java.util.ArrayList;
import net.angle.omnicraft.world.World;

/**
 *
 * @author angle
 */
public class RenderData {
    
    public final Vec3[] palette;
    
    public RenderData() {
        ArrayList<Vec3> palette = new ArrayList<>();
        for (int i = 0; i < World.PALETTE_SIZE; i++) {
            palette.add(new Vec3(0, 0, 0));
        }
        
        this.palette = palette.toArray(new Vec3[0]);
    }
    
    public RenderData(Color... colors) {
        ArrayList<Vec3> palette = new ArrayList<>();
        for (Color color : colors) {
            palette.add(new Vec3(color.getRed(), color.getGreen(), color.getBlue()));
        }
        
        this.palette = palette.toArray(new Vec3[0]);
    }
    
}
