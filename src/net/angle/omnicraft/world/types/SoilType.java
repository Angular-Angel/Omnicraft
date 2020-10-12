/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.types;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Image;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.math.Util;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.angle.omnicraft.pixel.PixelSource;
import net.angle.omnicraft.random.OmniRandom;
import static org.lwjgl.opengl.ARBTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class SoilType implements PixelSource {
    private final List<SoilFraction> components;
    
    public SoilType(SoilFraction... components) {
        this.components = new ArrayList();
        
        this.components.addAll(Arrays.asList(components));
    }
    
    public Texture2D generateTexture(OmniRandom random) {
        Color[][] tex = new Color[16][16];
        
        for (Color[] line : tex) {
            for (int i = 0; i < 16; i++) {
                line[i] = this.getPixelColor(random, this);
            }
        }
        
        Image image = DGL.genImage(16, 16, 3, Util.PrimType.BYTE);
        image.shade((x, y, band) -> {
            if (band == 0)
                return tex[x][y].getRed();
            if (band == 1)
                return tex[x][y].getGreen();
            if (band == 2)
                return tex[x][y].getBlue();
            else
                System.out.println("Asked for Band: " + band);
                return 0;
        });

        Texture2D texture = DGL.genTex2D();
        texture.bind();
        texture.parami(GL_TEXTURE_WRAP_S, GL_REPEAT);
        texture.parami(GL_TEXTURE_WRAP_T, GL_REPEAT);
        texture.parami(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        texture.parami(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        texture.image(image);
        texture.generateMipmap();
        texture.paramf(GL_TEXTURE_MAX_ANISOTROPY, 8);
        texture.unbind();
        DGL.delete(image);
        return texture;
    }
    
    /*public Image generateImage() {
        Image image = new Image();
        image.
    }*/

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return this.components.get(0).getPixelColor(random, context);
    }
}
