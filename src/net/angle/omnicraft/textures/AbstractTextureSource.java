/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Image;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.math.Util;
import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.pixels.PixelSource;
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
 */
public abstract class AbstractTextureSource implements TextureSource {
    private PixelSource pixelSource;
    
    public AbstractTextureSource(PixelSource pixelSource) {
        this.pixelSource = pixelSource;
    }
    
    public void setPixelSource(PixelSource pixelSource) {
        this.pixelSource = pixelSource;
    }
    
    public abstract Color[][] getPixelColors(int width, int height, OmniRandom random);
    
    public Image generateImage(int width, int height, Color[][] tex) {
        Image image = DGL.genImage(width, height, 3, Util.PrimType.BYTE);
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
        
        return image;
    }
    
    public void configureTexture(Image image, Texture2D texture) {
        texture.bind();
        texture.parami(GL_TEXTURE_WRAP_S, GL_REPEAT);
        texture.parami(GL_TEXTURE_WRAP_T, GL_REPEAT);
        texture.parami(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        texture.parami(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        texture.image(image);
        DGL.delete(image);
        texture.generateMipmap();
        texture.paramf(GL_TEXTURE_MAX_ANISOTROPY, 8);
        texture.unbind();
    }

    @Override
    public Texture2D generateTexture(OmniRandom random) {
        return generateTexture(16, 16, random);
    }
    
    @Override
    public Texture2D generateTexture(int width, int height, OmniRandom random) {
        Color[][] tex = getPixelColors(width, height, random);
        
        /*//This chunk of code is for testing position of texture rendering.
        tex[0][0] = Color.red; //Top Left
        tex[width - 1][0] = Color.blue; //Top Right
        tex[width - 1][height - 1] = Color.green; // Bottom Right
        tex[0][height - 1] = Color.yellow; //Bottom Left*/
        
        Image image = generateImage(width, height, tex);

        Texture2D texture = DGL.genTex2D();
        
        configureTexture(image, texture);
        
        return texture;
    }
    
    @Override
    public Color getPixelColor(OmniRandom random, PixelSource context) {
        return pixelSource.getPixelColor(random, context);
    }
}
