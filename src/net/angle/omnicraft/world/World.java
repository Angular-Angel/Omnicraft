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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.angle.omnicraft.client.DebugClient;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Emptiness;
import net.angle.omnicraft.world.blocks.Nothingness;
import net.angle.omnicraft.world.blocks.Side;
import net.angle.omnicraft.world.types.Substance;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;

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
    public final Map<String, Side> sideTypes;
    public final Map<String, Region> regions;
    public final List<Chunk> loadedChunks;
    public final List<Block> block_ids;
    public final List<Side> side_ids;
    
    private TextureRectangle block_palette;
    private TextureRectangle side_palette;
    
    public World() {
        this(new Emptiness(), new Nothingness());
    }
    
    public World(Block block, Side side) {
        this(block, side, 16, 16);
    }
    
    public World(Block block, Side side, int chunkEdgeLengthOfRegion, int blockEdgeLengthOfChunk) {
        substances = new HashMap<>();
        blockTypes = new HashMap<>();
        sideTypes = new HashMap<>();
        regions = new HashMap<>();
        loadedChunks = new ArrayList<>();
        block_ids = new ArrayList<>();
        side_ids = new ArrayList<>();
        addBlockType(block);
        addSideType(side);
        this.chunkEdgeLengthOfRegion = chunkEdgeLengthOfRegion;
        this.blockEdgeLengthOfChunk = blockEdgeLengthOfChunk;
        addRegion(new Region(this, block, side, 0, 0, 0));
    }
    
    public void addSubstance(Substance substance) {
        substances.put(substance.name, substance);
    }
    
    public int get_next_block_id() {
        return block_ids.size();
    }
    
    public void addBlockType(Block block) {
        if (block.id != get_next_block_id())
            throw new IllegalArgumentException("Block has incorrect ID!");
        blockTypes.put(block.name, block);
        block_ids.add(block);
    }
    
    public int get_next_side_id() {
        return side_ids.size();
    }
    
    public void addSideType(Side side) {
        if (side.id != get_next_side_id())
            throw new IllegalArgumentException("Side has incorrect ID!");
        sideTypes.put(side.name, side);
        side_ids.add(side);
    }
    
    public void addRegion(Region region) {
        String coordstring = region.getCoordinates().toString();
        regions.put(coordstring, region);
    }
    
    public Region getSpawnRegion() {
        return regions.get("(0, 0, 0)");
    }
    
    public void loadRegion(Region region) {
        loadedChunks.addAll(region.getChunks());
    }
    
    public void prepare_block_palette() {
        block_palette = DGL.genTexRect();
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
        
        block_palette.bind();
        block_palette.parami(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        block_palette.parami(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        block_palette.image(image);
        DGL.delete(image);
        block_palette.unbind();
        
        block_palette.bind(GL_TEXTURE0);
    }
    
    public void prepare_side_palette() {
        side_palette = DGL.genTexRect();
        Image image = DGL.genImage(PALETTE_SIZE, side_ids.size(), 3, Util.PrimType.BYTE);
        image.shade((int x, int y, int band) -> {
            if (band == 0)
                return side_ids.get(y).renderData.palette[x].x;
            if (band == 1)
                return side_ids.get(y).renderData.palette[x].y;
            if (band == 2)
                return side_ids.get(y).renderData.palette[x].z;
            else
                throw new IllegalArgumentException("Asked for Band: " + band);
        });
        
        side_palette.bind();
        side_palette.parami(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        side_palette.parami(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        side_palette.image(image);
        DGL.delete(image);
        side_palette.unbind();
        
        side_palette.bind(GL_TEXTURE1);
    }
    
    public void prepareShader(ShaderProgram shader) {
        
        shader.uniform1i("u_block_palette", 0);
        shader.uniform1i("u_side_palette", 1);
    }
    
    public void bufferOptimizedMesh(DebugClient client) {
        loadedChunks.forEach(chunk -> {
            chunk.bufferOptimizedMesh(client);
        });
    }
    
    public void delete() {
        DGL.delete(block_palette);
        DGL.delete(side_palette);
    }
}
