/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.math.Vec3;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author angle
 */
public class RenderData {
    
    public final Vec3[] palette;
    public final int palette_size;
    
    public RenderData(Color... colors) {
        ArrayList<Vec3> palette = new ArrayList<>();
        for (Color color : colors) {
            palette.add(new Vec3(color.getRed(), color.getGreen(), color.getBlue()));
        }
        
        palette_size = palette.size();
        
        while (palette.size() < 20) {
            palette.add(new Vec3());
        }
        
        this.palette = palette.toArray(new Vec3[0]);
    }
    
    public void prepareShader(ShaderProgram shader) {
        shader.uniformVec3v("u_palette", palette);
        shader.uniform1i("u_palette_size", palette_size);
    }
    
}
