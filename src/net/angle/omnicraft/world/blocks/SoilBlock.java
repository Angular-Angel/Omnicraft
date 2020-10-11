/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Image;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.math.Util;
import java.awt.Color;
import net.angle.omnicraft.pixel.CubeTexture;
import net.angle.omnicraft.pixel.PixelSource;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.world.types.SoilType;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.ARBTextureFilterAnisotropic.*;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class SoilBlock implements PixelSource {
    private final SoilType soilType;
    private final CubeTexture texture;
    
    public Texture2D generateTexture(OmniRandom random) {
        Color[][] tex = new Color[16][16];
        
        for (Color[] line : tex) {
            for (int i = 0; i < 16; i++) {
                line[i] = this.soilType.getPixelColor(random, this);
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
    
    public SoilBlock(SoilType soilType) {
        this.soilType = soilType;
        
        OmniRandom random = new OmniRandom();
        
        this.texture = new CubeTexture(generateTexture(random), generateTexture(random),
                                       generateTexture(random), generateTexture(random), 
                                       generateTexture(random), generateTexture(random));
    }
    
    public void draw() {
        texture.draw();
    }
    
    public void delete() {
        this.texture.delete();
    }

    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return this.soilType.getPixelColor(new OmniRandom(), this);
    }
}
