/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.angle.omnicraft.world.blocks.Block;
import net.angle.omnicraft.world.types.Substance;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class World {
    public final Map<String, Substance> substances;
    public final List<Block> blocks;
    public final List<Chunk> chunks;
    
    public World() {
        substances = new HashMap<>();
        blocks = new ArrayList<>();
        chunks = new ArrayList<>();
    }
    
    public void draw() {
        for (Chunk chunk : chunks) {
            chunk.draw();
        }
    }
    
    public void delete() {
        for (Block block : blocks)
            block.delete();
    }
}
