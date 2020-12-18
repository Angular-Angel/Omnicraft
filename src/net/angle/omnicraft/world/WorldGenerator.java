/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.FBO;
import com.samrj.devil.gl.Texture2D;
import java.awt.Color;
import net.angle.omnicraft.random.OmniRandom;
import net.angle.omnicraft.textures.ArtisanalDirtTexture;
import net.angle.omnicraft.textures.PaletteLayeredTextureSource;
import net.angle.omnicraft.textures.TextureSource;
import net.angle.omnicraft.textures.TextureSource.ColorVariationCallback;
import net.angle.omnicraft.textures.pixels.ColoredVariation;
import net.angle.omnicraft.textures.pixels.VariedColorPixelSource;
import net.angle.omnicraft.world.blocks.CubeShape;
import net.angle.omnicraft.world.blocks.HomogenousBlock;
import net.angle.omnicraft.world.types.Fluid;
import net.angle.omnicraft.world.types.Mineraloid;
import net.angle.omnicraft.world.types.MixtureComponent;
import net.angle.omnicraft.world.types.Mixture;
import static org.lwjgl.opengl.ARBFramebufferObject.GL_COLOR_ATTACHMENT0;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class WorldGenerator {
    
    public static void generateSubstances(World world) {
        world.addSubstance(new Mineraloid("Pebbles", new VariedColorPixelSource(Color.darkGray, 60), 240));
        world.addSubstance(new Mineraloid("Sand", new VariedColorPixelSource(new Color(200, 150, 80), 30), 80));
        world.addSubstance(new Mineraloid("Silt", new VariedColorPixelSource(new Color(120, 75, 0), 10), 40));
        world.addSubstance(new Mineraloid("Clay", new VariedColorPixelSource(new Color(120, 120, 130), -25), 20));
        world.addSubstance(new Mineraloid("Compost", new VariedColorPixelSource(new Color(80, 40, 15), -20), 130));
        world.addSubstance(new Fluid("Water", new VariedColorPixelSource(new Color(0, 0, 200, 20), -20)));
        world.addSubstance(new Fluid("Air", new VariedColorPixelSource(new Color(20, 0, 50, 25), -50)));
        world.addSubstance(new Mixture("Gravel", new MixtureComponent(world.substances.get("Pebbles"), 100.0f)));
        world.addSubstance(new Mixture("Desert Sand", new MixtureComponent(world.substances.get("Sand"), 100.0f)));
        
        generateDirtType(world, "Dirt", 6, 6, 3, 0.15f, 0.15f, 0.45f);
    }
    
    public static Mixture generateDirtType(World world, String name, int redVar, int greenVar, int blueVar, float downChance, float upChance, float randomChance) {
        Mixture dirt = new Mixture(name, new MixtureComponent(world.substances.get("Water"), 25.0f), 
                new MixtureComponent(world.substances.get("Air"), 25.0f), new MixtureComponent(world.substances.get("Silt"), 18.0f),
                new MixtureComponent(world.substances.get("Sand"), 15.0f), new MixtureComponent(world.substances.get("Clay"), 9.0f), 
                new MixtureComponent(world.substances.get("Compost"), 5.0f), new MixtureComponent(world.substances.get("Pebbles"), 3.0f));
        
        ColoredVariation upVariation = new ColoredVariation(redVar, greenVar, blueVar);
        ColoredVariation downVariation = new ColoredVariation(-redVar, -greenVar, -blueVar);
        
        ColorVariationCallback upvarCallback = TextureSource.ColorVariationCallback.createVariationCallback(upChance, upVariation);
                
        ColorVariationCallback downvarCallback = TextureSource.ColorVariationCallback.createVariationCallback(downChance, downVariation);
                
        ColorVariationCallback randomCallback = TextureSource.ColorVariationCallback.createRandomizationCallback(randomChance, dirt);
        
        PaletteLayeredTextureSource paletteLayeredTextureSource = new PaletteLayeredTextureSource(dirt, 6, upvarCallback, downvarCallback, randomCallback);
        
        ArtisanalDirtTexture artisanalDirtTexture = new ArtisanalDirtTexture(dirt, 6);
        
        paletteLayeredTextureSource.setPixelCallbacks.add((x, y, tex, color, random) -> {
            if (random.nextFloat() <= 0.05) {
                if (random.nextFloat() <= 0.5 && y != 0) {
                    tex[x][y-1] = color;
                } else if (y != tex[0].length - 1){
                    tex[x][y+1] = color;
                }
            }
            
            return tex;
        });
        
        world.addSubstance(dirt);
        
        return dirt;
    }
    
    public static void generateBlocks(World world) {
        world.blocks.add(new HomogenousBlock(world.substances.get("Dirt"), new CubeShape(), new OmniRandom()));
        world.blocks.add(new HomogenousBlock(world.substances.get("Gravel"), new CubeShape(), new OmniRandom()));
        world.blocks.add(new HomogenousBlock(world.substances.get("Desert Sand"), new CubeShape(), new OmniRandom()));
    }
    
    public static void generateChunks(World world) {
        world.chunks.add(new OctreeChunk(world.blocks.get(0)));
        world.chunks.get(0).setBlock(0, 0, 0, world.blocks.get(1));
        world.chunks.get(0).setBlock(0, 0, 1, world.blocks.get(2));
        world.chunks.add(new OctreeChunk(world.blocks.get(0), 16, 0, 0));
    }
    
    public static World generateWorld() {
        World world = new World();
        
        generateSubstances(world);
        
        generateBlocks(world);

        generateChunks(world);
        
        FBO fbo = DGL.genFBO();
        
        Texture2D tex = DGL.genTex2D();
        
        DGL.bindFBO(fbo);
        
        fbo.texture2D(tex, GL_COLOR_ATTACHMENT0);
        
        DGL.bindFBO(null);
        
        DGL.delete(tex, fbo);
        
        return world;
    }
}
