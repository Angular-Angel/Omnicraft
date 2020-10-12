/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * This class represents a cube of blocks in the map. 
 * I am probably going to have to redo it repeatedly, but, oh well.
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public class Chunk {
    //incredibly lazy and lame representation, but also very easy.
    //Will probably change later.
    private Block[][][] blocks;
    
    public Chunk(Block block) {
        blocks = new Block[16][16][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    blocks[x][y][z] = block;
                    
                }
            }
        }
    }
    
    public void draw() {
        for (int x = 0; x < 16; x++) {
            glTranslatef(1, 0, 0);
            for (int y = 0; y < 16; y++) {
                glTranslatef(0, 1, 0);
                for (int z = 0; z < 16; z++) {
                    glTranslatef(0, 0, 1);
                    blocks[x][y][z].draw();
                }
                glTranslatef(0, 0, -16);
            }
            glTranslatef(0, -16, 0);
        }
        glTranslatef(-16, 0, 0);
    }
}
