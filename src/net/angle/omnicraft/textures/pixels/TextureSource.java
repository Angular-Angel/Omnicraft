/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures.pixels;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Image;
import com.samrj.devil.gl.Texture2D;
import com.samrj.devil.math.Util;
import java.awt.Color;
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
public abstract class TextureSource implements PixelSource {
    public Texture2D generateTexture(OmniRandom random) {
        return generateTexture(16, 16, random);
    }
    
    public Texture2D generateTexture(int width, int height, OmniRandom random) {
        
        //Pretty sure this and the following loop can be folded into a single 
        //statement, but I don;t know how to do it right now.
        Color[][] tex = new Color[width][height];
        
        for (Color[] line : tex) {
            for (int i = 0; i < height; i++) {
                line[i] = this.getPixelColor(random, this);
            }
        }
        
        /*//This chunk of code is for testing position of texture rendering.
        tex[0][0] = Color.red; //Top Left
        tex[width - 1][0] = Color.blue; //Top Right
        tex[width - 1][height - 1] = Color.green; // Bottom Right
        tex[0][height - 1] = Color.yellow; //Bottom Left*/
        
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
}
