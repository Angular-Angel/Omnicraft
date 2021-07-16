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
import com.samrj.devil.math.Vec3;
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
    public static final int BLOCK_EDGE_LENGTH_OF_CHUNK = 16;
    public static final float EDGE_LENGTH_OF_BLOCK = 0.5f;
    
    //Stored by name
    public final Map<String, Substance> substances;
    public final Map<String, Block> blockTypes;
    public final Map<String, Side> sideTypes;
    
    //Stored by coordinates
    public final Map<String, Chunk> chunks;
    
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
        chunks = new HashMap<>();
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

    public Chunk getChunk(int chunkx, int chunky, int chunkz) {
        return getChunk(new Vec3i(chunkx, chunky, chunkz));
    }
    
    public Chunk getChunk(Vec3i coords) {
        return chunks.get(coords.toString());
    }
    
    public Chunk checkChunk(int chunkx, int chunky, int chunkz) {
        return checkChunk(new Vec3i(chunkx, chunky, chunkz));
    }
    
    public Chunk checkChunk(Vec3i coord) {
        Chunk chunk = getChunk(coord);
        if (chunk == null) {
            chunk = generateChunk(coord);
        }
        return chunk;
    }
    
    public float getRealEdgeLengthOfChunk() {
        return EDGE_LENGTH_OF_BLOCK * BLOCK_EDGE_LENGTH_OF_CHUNK;
    }
    
    public Vec3i getChunkCoordsFromVoxelCoords(int voxelx, int voxely, int voxelz) {
        //Fixing a rounding problem
        if (voxelx < 0) voxelx -= BLOCK_EDGE_LENGTH_OF_CHUNK;
        if (voxely < 0) voxely -= BLOCK_EDGE_LENGTH_OF_CHUNK;
        if (voxelz < 0) voxelz -= BLOCK_EDGE_LENGTH_OF_CHUNK;
        return new Vec3i(voxelx / BLOCK_EDGE_LENGTH_OF_CHUNK, voxely / BLOCK_EDGE_LENGTH_OF_CHUNK, voxelz / BLOCK_EDGE_LENGTH_OF_CHUNK);
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
    
    public Chunk generateNewChunk(Vec3i coord) {
        return generateNewChunk(coord.x, coord.y, coord.z);
    }

    public Chunk generateNewChunk(int x, int y, int z) {
        Chunk chunk = new Chunk(this, x, y, z);
        addChunk(chunk);
        return chunk;
    }
    
    public Chunk generateChunkTerrain(Chunk chunk) {
        worldGenerator.generateChunk(chunk);
        reloadChunk(chunk);
        reloadAdjacentChunks(chunk);
        return chunk;
    }
    
    public Chunk generateChunk(Vec3i coords) {
        return generateChunk(coords.x, coords.y, coords.z);
    }
    
    public Chunk generateChunk(int x, int y, int z) {
        return generateChunkTerrain(generateNewChunk(x, y, z));
    }
    
    public void addChunk(Chunk chunk) {
        String coordstring = chunk.getCoordinates().toString();
        chunks.put(coordstring, chunk);
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
        reloadChunk(getChunk(chunk.getX() + 1, chunk.getY(), chunk.getZ()));
        reloadChunk(getChunk(chunk.getX() - 1, chunk.getY(), chunk.getZ()));
        reloadChunk(getChunk(chunk.getX(), chunk.getY() + 1, chunk.getZ()));
        reloadChunk(getChunk(chunk.getX(), chunk.getY() - 1, chunk.getZ()));
        reloadChunk(getChunk(chunk.getX(), chunk.getY(), chunk.getZ() + 1));
        reloadChunk(getChunk(chunk.getX(), chunk.getY(), chunk.getZ() - 1));
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
    
    public Block getBlock(Vec3i coord) {
        return getBlock(coord.x, coord.y, coord.z);
    }
    
    public int[] getChunkAndVoxelCoords(int voxelx, int voxely, int voxelz) {
        int chunkx = 0, chunky = 0, chunkz = 0;
        while (voxelx < 0) {
            voxelx += BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkx--;
        }
        while (voxelx >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            voxelx -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkx++;
        }
        while (voxely < 0) {
            voxely += BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunky--;
        }
        while (voxely >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            voxely -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunky++;
        }
        while (voxelz < 0) {
            voxelz += BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkz--;
        }
        while (voxelz >= BLOCK_EDGE_LENGTH_OF_CHUNK) {
            voxelz -= BLOCK_EDGE_LENGTH_OF_CHUNK;
            chunkz++;
        }
        
        return new int[] {chunkx, chunky, chunkz, voxelx, voxely, voxelz};
    }
    
    public Block getBlock(int blockx, int blocky, int blockz) {
        int[] coords = getChunkAndVoxelCoords(blockx, blocky, blockz);
        
        Chunk chunk = getChunk(coords[0], coords[1], coords[2]);
        if (chunk == null) 
            return block_ids.get(0);
        else
            return chunk.getBlock(coords[3], coords[4], coords[5]);
    }
    
    public void setBlock(int blockx, int blocky, int blockz, Block block) {
        int[] coords = getChunkAndVoxelCoords(blockx, blocky, blockz);
        
        checkChunk(coords[0], coords[1], coords[2]).setBlock(coords[3], coords[4], coords[5], block);
    }
    
    public Side getSide(Block.BlockFace face, int sidex, int sidey, int sidez) {
        int[] coords = getChunkAndVoxelCoords(sidex, sidey, sidez);
        
        Chunk chunk = getChunk(coords[0], coords[1], coords[2]);
        if (chunk == null) 
            return side_ids.get(0);
        else
            return chunk.getSide(face, coords[3], coords[4], coords[5]);
    }
    
    public void setSide(Block.BlockFace face, int sidex, int sidey, int sidez, Side side) {int[] coords = getChunkAndVoxelCoords(sidex, sidey, sidez);
        
        checkChunk(coords[0], coords[1], coords[2]).setSide(face, coords[3], coords[4], coords[5], side);
    }
    
    public float intbound(float start, float delta) {
        if (delta < 0) {
            return intbound(-start, -delta);
        } else {
            start = (start % 1 + 1) % 1;
            return (1 - start) / delta;
        }
    }
    
    public Vec3i raycast(Vec3 origin, Vec3 direction, int radius) {
        float curx = origin.x;
        float cury = origin.y;
        float curz = origin.z;
        
        float dx = direction.x;
        float dy = direction.y;
        float dz = direction.z;
        
        if (dx < 0.05f && dx > -0.05f)
            dx = 0;
        
        if (dy < 0.05f && dy > -0.05f)
            dy = 0;
        
        if (dz < 0.05f && dz > -0.05f)
            dz = 0;
        
        int stepX = (int) Math.signum(dx);
        int stepY = (int) Math.signum(dy);
        int stepZ = (int) Math.signum(dz);
        
        int steps = 0;
        
        float tMaxX = dx != 0 ? intbound(origin.x, dx) : Float.POSITIVE_INFINITY;
        float tMaxY = dy != 0 ? intbound(origin.y, dy) : Float.POSITIVE_INFINITY;
        float tMaxZ = dz != 0 ? intbound(origin.z, dz) : Float.POSITIVE_INFINITY;
        
        // The change in t when taking a step (always positive).
        
        float tDeltaX = dx != 0 ? stepX/dx : 0;
        float tDeltaY = dy != 0 ? stepY/dy : 0;
        float tDeltaZ = dz != 0 ? stepZ/dz : 0;
        
        if (dx == 0 && dy == 0 && dz == 0)
            throw new IllegalArgumentException("Raycast in zero direction!");
        
        while (steps < radius) {
            if(tMaxX < tMaxY) {
                if(tMaxX < tMaxZ) {
                    curx += stepX;
                    tMaxX += tDeltaX;
                } else {
                    curz += stepZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if(tMaxY < tMaxZ) {
                    cury += stepY;
                    tMaxY += tDeltaY;
                } else {
                    curz += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }
            steps++;
            Vec3i coord = new Vec3i((int) Math.floor(curx), (int) Math.floor(cury), (int) Math.floor(curz));
            Block block = getBlock(coord);
            if (block != null && block.isDrawable())
                return coord;
        }
        
        return null;
    }
}
