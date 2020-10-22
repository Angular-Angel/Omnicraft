/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.textures.pixels;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Image;
import com.samrj.devil.gl.Texture2D;
import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import static net.angle.omnicraft.textures.pixels.TextureSource.configureTexture;
import static net.angle.omnicraft.textures.pixels.TextureSource.generateImage;

/**
 *
 * @author angle
 */
public class MixedTextureSource implements TextureSource {
    private PixelSource pixelSource;
    
    public MixedTextureSource(PixelSource pixelSource) {
        this.pixelSource = pixelSource;
    }
    
    @Override
    public Texture2D generateTexture(OmniRandom random) {
        return generateTexture(16, 16, random);
    }
    
    public Color[][] getPixelColors(int width, int height, OmniRandom random) {
        //Pretty sure this and the following loop can be folded into a single
        //statement, but I don;t know how to do it right now.
        Color[][] tex = new Color[width][height];
        
        for (Color[] line : tex) {
            for (int i = 0; i < line.length; i++) {
                line[i] = this.getPixelColor(random, this);
            }
        }
        
        return tex;
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
