/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.Image;
import com.samrj.devil.gl.ShaderProgram;
import com.samrj.devil.gl.TextureRectangle;
import com.samrj.devil.math.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.angle.omnicraft.client.DebugClient;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Emptiness;
import net.angle.omnicraft.world.types.Substance;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class World {
    
    public static final int PALETTE_SIZE = 40;
    
    public final int blockEdgeLengthOfChunk, chunkEdgeLengthOfRegion;
    
    public final Map<String, Substance> substances;
    public final Map<String, Block> blockTypes;
    public final Map<String, ChunkContainer> regions;
    public final List<Chunk> loadedChunks;
    public final List<Block> block_ids;
    
    private TextureRectangle palettes;
    
    public World() {
        this(new Emptiness());
    }
    
    public World(Block block) {
        this(block, 16, 16);
    }
    
    public World(Block block, int chunkEdgeLengthOfRegion, int blockEdgeLengthOfChunk) {
        substances = new HashMap<>();
        blockTypes = new HashMap<>();
        regions = new HashMap<>();
        loadedChunks = new ArrayList<>();
        block_ids = new ArrayList<>();
        addBlockType(block);
        this.chunkEdgeLengthOfRegion = chunkEdgeLengthOfRegion;
        this.blockEdgeLengthOfChunk = blockEdgeLengthOfChunk;
        addRegion(new Region(this, block, 0, 0, 0));
    }
    
    public void addSubstance(Substance substance) {
        substances.put(substance.name, substance);
    }
    
    public void addBlockType(Block block) {
        blockTypes.put(block.name, block);
        if (block.renderData != null) {
            block.id = block_ids.size();
            block_ids.add(block);
        }
    }
    
    public void addRegion(ChunkContainer region) {
        region.getCoordinates().toString();
        String coordstring = region.getCoordinates().toString();
        regions.put(coordstring, region);
    }
    
    public ChunkContainer getSpawnRegion() {
        return regions.get("(0, 0, 0)");
    }
    
    public void loadRegion(ChunkContainer region) {
        loadedChunks.addAll(region.getChunks());
    }
    
    public void prepare_palette() {
        palettes = DGL.genTexRect();
        Image image = DGL.genImage(PALETTE_SIZE, block_ids.size(), 3, Util.PrimType.BYTE);
        image.shade((int x, int y, int band) -> {
            if (band == 0)
                return block_ids.get(y).renderData.palette[x].x;
            if (band == 1)
                return block_ids.get(y).renderData.palette[x].y;
            if (band == 2)
                return block_ids.get(y).renderData.palette[x].z;
            else
                throw new IllegalArgumentException("Asked for Band: " + band);
        });
        
        palettes.bind();
        palettes.parami(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        palettes.parami(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        palettes.image(image);
        DGL.delete(image);
        palettes.unbind();
        
        palettes.bind(GL_TEXTURE0);
    }
    
    public void prepareShader(ShaderProgram shader) {
        
        shader.uniform1i("u_palette", 0);
    }
    
    public void bufferOptimizedMesh(DebugClient client) {
        loadedChunks.forEach(chunk -> {
            chunk.bufferOptimizedMesh(client);
        });
    }
    
    public void delete() {
        DGL.delete(palettes);
    }
}
