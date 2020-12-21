/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.blocks.Emptiness;
import net.angle.omnicraft.world.types.Substance;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class World {
    public final Map<String, Substance> substances;
    public final Map<String, Block> blockTypes;
    public final List<Chunk> loadedChunks;
    public final Region spawnRegion;
    
    public World() {
        this(new Emptiness());
    }
    
    public World(Block block) {
        substances = new HashMap<>();
        blockTypes = new HashMap<>();
        loadedChunks = new ArrayList<>();
        addBlockType(block);
        spawnRegion = new Region(this, block, 64, 16);
    }
    
    public void addSubstance(Substance substance) {
        substances.put(substance.name, substance);
    }
    
    public void addBlockType(Block block) {
        blockTypes.put(block.name, block);
    }
    
    public void loadRegion(Region region) {
        for (Chunk[][] chunklayer : region.chunks) {
            for (Chunk[] chunkRow : chunklayer) {
                loadedChunks.addAll(Arrays.asList(chunkRow));
            }
        }
    }
    
    public void draw() {
        for (Chunk chunk : loadedChunks) {
            chunk.draw();
        }
    }
    
    public void delete() {
        for (Block block : blockTypes.values())
            block.delete();
    }
}
