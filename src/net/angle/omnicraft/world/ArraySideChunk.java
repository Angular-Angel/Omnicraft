/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world;

import net.angle.omnicraft.world.blocks.Block.BlockFace;
import net.angle.omnicraft.world.blocks.Side;

/**
 *
 * @author angle
 */
public class ArraySideChunk extends SideChunk {
    
    //incredibly lazy and lame representation, but also very easy.
    private Side[][][][] sides;

    public ArraySideChunk(SideChunkContainer container, Side side, int x, int y, int z) {
        super(container, x, y, z);
        sides = new Side[16][16][16][6];
        setAllSides(side);
    }

    @Override
    public Side getSide(BlockFace face, int sidex, int sidey, int sidez) {
        
        if (!containsCoordinates(sidex, sidey, sidez)) {
            return container.getSide(face, sidex + x, sidey + y, sidez + z);
        }
        return sides[sidex][sidey][sidez][face.id];
    }

    @Override
    public void setSide(BlockFace face, int sidex, int sidey, int sidez, Side side) {
        sides[sidex][sidey][sidez][face.id] = side;
    }
    
}
