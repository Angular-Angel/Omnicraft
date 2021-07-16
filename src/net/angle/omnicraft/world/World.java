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
import com.samrj.devil.math.Vec3i;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.EmptyBlock;
import net.angle.omnicraft.world.blocks.EmptySide;
import net.angle.omnicraft.world.blocks.Side;
import net.angle.omnicraft.world.types.Emptiness;
import net.angle.omnicraft.world.types.Substance;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class World {
    
    public static final int PALETTE_SIZE = 40;
    public static final int GENERATION_DISTANCE = 7;
    public static final int RENDER_DISTANCE = 6;
    public static final int CHUNK_EDGE_LENGTH_OF_REGION = 16;
    public static final int BLOCK_EDGE_LENGTH_OF_CHUNK = 16;
    public static final float EDGE_LENGTH_OF_BLOCK = 0.5f;
    
    //Stored by name
    public final Map<String, Substance> substances;
    public final Map<String, Block> blockTypes;
    public final Map<String, Side> sideTypes;
    
    //Stored by coordinates
    public final Map<String, Region> regions;
    
    public final List<Chunk> loadedChunks;
    
    public final List<Substance> substance_ids;
    public final List<Block> block_ids;
    public final List<Side> side_ids;
    
    public final WorldGenerator worldGenerator;
    
    private TextureRectangle block_palette;
    private TextureRectangle side_palette;
    
    public World(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
        substances = new HashMap<>();
        blockTypes = new HashMap<>();
        sideTypes = new HashMap<>();
        regions = new HashMap<>();
        loadedChunks = new ArrayList<>();
        substance_ids = new ArrayList<>();
        block_ids = new ArrayList<>();
        side_ids = new ArrayList<>();
        addSubstance(new Emptiness());
        addBlockType(new EmptyBlock());
        addSideType(new EmptySide());
        worldGenerator.generateSubstances(this);
        worldGenerator.generateBlocks(this);
        worldGenerator.generateSpawnRegion(this);
    }
    
    public Region CheckRegion(int regionx, int regiony, int regionz) {
        return checkRegion(new Vec3i(regionx, regiony, regionz));
    }
    
    public Region checkRegion(Vec3i coord) {
        Region region = regions.get(coord.toString());
        if (region == null) {
            region = generateNewRegion(coord);
        }
        return region;
    }
    
    public void update(float dt) {
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
    
    public Region generateNewRegion(Vec3i coord) {
        return generateNewRegion(coord.x, coord.y, coord.z);
    }

    public Region generateNewRegion(int x, int y, int z) {
        Region region = new Region(this, x, y, z);
        addRegion(region);
        return region;
    }
    
    public Chunk generateChunkTerrain(Chunk chunk) {
        worldGenerator.generateChunk(chunk);
        reloadChunk(chunk);
        reloadAdjacentChunks(chunk);
        return chunk;
    }
    
    public void addRegion(Region region) {
        String coordstring = region.getCoordinates().toString();
        regions.put(coordstring, region);
    }
    
    public boolean loadChunk(Chunk chunk) {
        if (chunk != null && !chunk.vertexManager.loaded) {
            chunk.buffer();
            loadedChunks.add(chunk);
            return true;
        }
        return false;
    }
    
    public void unloadChunk(Chunk chunk) {
        if (chunk.vertexManager.loaded) {
            chunk.vertexManager.clearVertices();
            loadedChunks.remove(chunk);
        }
    }
    
    public void reloadChunk(Chunk chunk) {
        if (chunk != null && chunk.vertexManager.loaded) {
            unloadChunk(chunk);
            loadChunk(chunk);
        }
    }
    
    public void reloadAdjacentChunks(Chunk chunk) {
        reloadChunk(chunk.region.getChunk(chunk.getX() + 1, chunk.getY(), chunk.getZ()));
        reloadChunk(chunk.region.getChunk(chunk.getX() - 1, chunk.getY(), chunk.getZ()));
        reloadChunk(chunk.region.getChunk(chunk.getX(), chunk.getY() + 1, chunk.getZ()));
        reloadChunk(chunk.region.getChunk(chunk.getX(), chunk.getY() - 1, chunk.getZ()));
        reloadChunk(chunk.region.getChunk(chunk.getX(), chunk.getY(), chunk.getZ() + 1));
        reloadChunk(chunk.region.getChunk(chunk.getX(), chunk.getY(), chunk.getZ() - 1));
    }
    
    public void loadRegion(Region region) {
        List<Chunk> chunks = region.getChunks();
        chunks.forEach(chunk -> {
            loadChunk(chunk);
        });
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
        block_palette.image(image);
        DGL.delete(image);
        
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
        side_palette.image(image);
        DGL.delete(image);
        
        side_palette.bind(GL_TEXTURE1);
    }
    
    public void prepareShader(ShaderProgram shader) {
        shader.uniform1i("u_block_palette", 0);
        shader.uniform1i("u_side_palette", 1);
        
        //We use subtract a small number here instead of a full one to prevent 
        //having a sliver where it hits the full on the far edges, and giving us lines.
        shader.uniform1f("u_texel_length", (16.0f * EDGE_LENGTH_OF_BLOCK) - 0.00001f);
    }
    
    public int getBlockEdgeLengthOfRegion() {
        return BLOCK_EDGE_LENGTH_OF_CHUNK * CHUNK_EDGE_LENGTH_OF_REGION;
    }
    
    public float getRealEdgeLengthOfRegion() {
        return getBlockEdgeLengthOfRegion() * EDGE_LENGTH_OF_BLOCK;
    }
    
    public void draw() {
        loadedChunks.forEach(chunk -> {
            chunk.vertexManager.draw();
        });
    }
    
    public void delete() {
        for (Chunk chunk : loadedChunks) {
            chunk.vertexManager.clearVertices();
        }
        DGL.delete(block_palette);
        DGL.delete(side_palette);
    }
    
    public Block getBlock(int blockx, int blocky, int blockz) {
        int regionx = 0, regiony = 0, regionz = 0;
        while (blockx < 0) {
            blockx += getBlockEdgeLengthOfRegion();
            regionx--;
        }
        while (blockx >= getBlockEdgeLengthOfRegion()) {
            blockx -= getBlockEdgeLengthOfRegion();
            regionx++;
        }
        while (blocky < 0) {
            blocky += getBlockEdgeLengthOfRegion();
            regiony--;
        }
        while (blocky >= getBlockEdgeLengthOfRegion()) {
            blocky -= getBlockEdgeLengthOfRegion();
            regiony++;
        }
        while (blockz < 0) {
            blockz += getBlockEdgeLengthOfRegion();
            regionz--;
        }
        while (blockz >= getBlockEdgeLengthOfRegion()) {
            blockz -= getBlockEdgeLengthOfRegion();
            regionz++;
        }
        
        int chunkx = 0, chunky = 0, chunkz = 0;
        while (blockx >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            blockx -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkx++;
        }
        while (blocky >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            blocky -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunky++;
        }
        while (blockz >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            blockz -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkz++;
        }
        
        return CheckRegion(regionx, regiony, regionz).getChunk(chunkx, chunky, chunkz).getBlock(blockx, blocky, blockz);
    }
    
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        int regionx = 0, regiony = 0, regionz = 0;
        while (blockx < 0) {
            blockx += getBlockEdgeLengthOfRegion();
            regionx--;
        }
        while (blockx >= getBlockEdgeLengthOfRegion()) {
            blockx -= getBlockEdgeLengthOfRegion();
            regionx++;
        }
        while (blocky < 0) {
            blocky += getBlockEdgeLengthOfRegion();
            regiony--;
        }
        while (blocky >= getBlockEdgeLengthOfRegion()) {
            blocky -= getBlockEdgeLengthOfRegion();
            regiony++;
        }
        while (blockz < 0) {
            blockz += getBlockEdgeLengthOfRegion();
            regionz--;
        }
        while (blockz >= getBlockEdgeLengthOfRegion()) {
            blockz -= getBlockEdgeLengthOfRegion();
            regionz++;
        }
        
        int chunkx = 0, chunky = 0, chunkz = 0;
        while (blockx >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            blockx -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkx++;
        }
        while (blocky >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            blocky -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunky++;
        }
        while (blockz >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            blockz -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkz++;
        }
        
        CheckRegion(regionx, regiony, regionz).getChunk(chunkx, chunky, chunkz).setBlock(blockx, blocky, blockz, block);
    }
    
    public Side getSide(Block.BlockFace face, int sidex, int sidey, int sidez) {
        int regionx = 0, regiony = 0, regionz = 0;
        while (sidex < 0) {
            sidex += getBlockEdgeLengthOfRegion();
            regionx--;
        }
        while (sidex >= getBlockEdgeLengthOfRegion()) {
            sidex -= getBlockEdgeLengthOfRegion();
            regionx++;
        }
        while (sidey < 0) {
            sidey += getBlockEdgeLengthOfRegion();
            regiony--;
        }
        while (sidey >= getBlockEdgeLengthOfRegion()) {
            sidey -= getBlockEdgeLengthOfRegion();
            regiony++;
        }
        while (sidez < 0) {
            sidez += getBlockEdgeLengthOfRegion();
            regionz--;
        }
        while (sidez >= getBlockEdgeLengthOfRegion()) {
            sidez -= getBlockEdgeLengthOfRegion();
            regionz++;
        }
        
        int chunkx = 0, chunky = 0, chunkz = 0;
        while (sidex >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            sidex -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkx++;
        }
        while (sidey >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            sidey -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunky++;
        }
        while (sidez >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            sidez -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkz++;
        }
        
        return CheckRegion(regionx, regiony, regionz).getChunk(chunkx, chunky, chunkz).getSide(face, sidex, sidey, sidez);
    }
    
    public void setSide(Block.BlockFace face, int sidex, int sidey, int sidez, Side side) {
        int regionx = 0, regiony = 0, regionz = 0;
        while (sidex < 0) {
            sidex += getBlockEdgeLengthOfRegion();
            regionx--;
        }
        while (sidex >= getBlockEdgeLengthOfRegion()) {
            sidex -= getBlockEdgeLengthOfRegion();
            regionx++;
        }
        while (sidey < 0) {
            sidey += getBlockEdgeLengthOfRegion();
            regiony--;
        }
        while (sidey >= getBlockEdgeLengthOfRegion()) {
            sidey -= getBlockEdgeLengthOfRegion();
            regiony++;
        }
        while (sidez < 0) {
            sidez += getBlockEdgeLengthOfRegion();
            regionz--;
        }
        while (sidez >= getBlockEdgeLengthOfRegion()) {
            sidez -= getBlockEdgeLengthOfRegion();
            regionz++;
        }
        
        int chunkx = 0, chunky = 0, chunkz = 0;
        while (sidex >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            sidex -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkx++;
        }
        while (sidey >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            sidey -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunky++;
        }
        while (sidez >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            sidez -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkz++;
        }
        
        CheckRegion(regionx, regiony, regionz).getChunk(chunkx, chunky, chunkz).setSide(face, sidex, sidey, sidez, side);
    }
}
