/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import com.samrj.devil.math.Vec2i;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.client.DebugClient;
import net.angle.omnicraft.graphics.RenderData;
import net.angle.omnicraft.world.BlockChunk;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Block {
    
    public final String name;
    public RenderData renderData;
    public int id;
    
    public enum BlockFace {
        
        top(0), bottom(1), front(2), back(3), left(4), right(5);
        
        public final int id;
        
        private BlockFace(int id) {
            this.id = id;
        }
        
        public Vec3i getStartingPosition(BlockChunk chunk) {
            switch(this) {
                case top: return new Vec3i(chunk.getEdgeLength() - 1, chunk.getEdgeLength() - 1, 0);
                case bottom: return new Vec3i(0, 0, 0);
                case front: return new Vec3i(0, 0, chunk.getEdgeLength() - 1);
                case back: return new Vec3i(chunk.getEdgeLength() - 1, 0, 0);
                case left: return new Vec3i(0, 0, 0);
                case right: return new Vec3i(chunk.getEdgeLength() - 1, 0, chunk.getEdgeLength() - 1);
            }
            return new Vec3i(-1, -1, -1);
        }
        
        public Vec3 getDrawStart(int x, int y, int z) {
            switch(this) {
                case top: return new Vec3(x + 1, y + 1, z);
                case bottom: return new Vec3(x, y, z);
                case front: return new Vec3(x, y, z + 1);
                case back: return new Vec3(x + 1, y, z);
                case left: return new Vec3(x, y, z);
                case right: return new Vec3(x + 1, y, z + 1);
            }
            return new Vec3(x, y, z);
        }
        
        public Vec3i moveAcross(Vec3i coord) {
            switch(this) {
                case top:
                    coord.z += 1;
                    break;
                case bottom:
                    coord.z += 1;
                    break;
                case front:
                    coord.x += 1;
                    break;
                case back:
                    coord.x -= 1;
                    break;
                case left:
                    coord.z += 1;
                    break;
                case right:
                    coord.z -= 1;
                    break;
            }
            
            return coord;
        }
        
        public Vec3i moveDown(Vec3i coord) {
            switch(this) {
                case top:
                    coord.x -= 1;
                    break;
                case bottom:
                    coord.x += 1;
                    break;
                case front: 
                case back:
                case left:
                case right:
                    coord.y += 1;
                    break;
            }
            
            return coord;
        }
        
        public Vec3i moveIn(Vec3i coord) {
            switch(this) {
                case top:
                    coord.y -= 1;
                    break;
                case bottom:
                    coord.y += 1;
                    break;
                case front:
                    coord.z -= 1;
                    break;
                case back:
                    coord.z += 1;
                    break;
                case left:
                    coord.x += 1;
                    break;
                case right:
                    coord.x -= 1;
                    break;
            }
            
            return coord;
        }
        
        public boolean continueAcross(Vec3i coord, BlockChunk chunk) {
            switch(this) {
                case top: return coord.z < chunk.getEdgeLength() - 1;
                case bottom: return coord.z < chunk.getEdgeLength() - 1;
                case front: return coord.x < chunk.getEdgeLength() - 1;
                case back: return coord.x > 0;
                case left: return coord.z < chunk.getEdgeLength() - 1;
                case right: return coord.z > 0;
            }
            return false;
        }
        
        public boolean continueDown(Vec3i coord, BlockChunk chunk) {
            switch(this) {
                case top: return coord.x > 0;
                case bottom: return coord.x < chunk.getEdgeLength() - 1;
                case front:
                case back:
                case left:
                case right: return coord.y < chunk.getEdgeLength() - 1;
            }
            return false;
        }
        
        public Vec3i orientFace(Vec2i dimensions) {
            Vec3i direction = new Vec3i();
            switch(this) {
                case top:
                    direction.z = dimensions.x;
                    direction.x = -dimensions.y;
                    break;
                case bottom:
                    direction.z = dimensions.x;
                    direction.x = dimensions.y;
                    break;
                case front:
                    direction.x = dimensions.x;
                    direction.y = dimensions.y;
                    break;
                case back:
                    direction.x = -dimensions.x;
                    direction.y = dimensions.y;
                    break;
                case left:
                    direction.z = dimensions.x;
                    direction.y = dimensions.y;
                    break;
                case right:
                    direction.z = -dimensions.x;
                    direction.y = dimensions.y;
                    break;
            }
            
            return direction;
        }
    }
    
    public Block(String name) {
        this.name = name;
    }
    
    public abstract boolean isTransparent();
    
    public boolean isVisibleThrough(Block adjacentBlock) {
        return adjacentBlock == null || adjacentBlock.isTransparent();
    }
    
    public boolean faceIsVisible(BlockFace face, BlockChunk chunk, Vec3i coord) {
        return faceIsVisible(face, chunk, coord.x, coord.y, coord.z);
    }
    
    public boolean faceIsVisible(BlockFace face, BlockChunk chunk, int blockx, int blocky, int blockz) {
        switch(face) {
            case top: return topIsVisible(chunk, blockx, blocky, blockz);
            case bottom: return bottomIsVisible(chunk, blockx, blocky, blockz);
            case front: return frontIsVisible(chunk, blockx, blocky, blockz);
            case back: return backIsVisible(chunk, blockx, blocky, blockz);
            case left: return leftSideIsVisible(chunk, blockx, blocky, blockz);
            case right: return rightSideIsVisible(chunk, blockx, blocky, blockz);
            default: throw new IllegalArgumentException("Checking visibility of face: " + face);
        }
    }
    
    public boolean topIsVisible(BlockChunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky + 1, blockz));
    }
    
    public boolean bottomIsVisible(BlockChunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky - 1, blockz));
    }
    
    public boolean frontIsVisible(BlockChunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky, blockz + 1));
    }
    
    public boolean backIsVisible(BlockChunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx, blocky, blockz - 1));
    }
    
    public boolean leftSideIsVisible(BlockChunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx - 1, blocky, blockz));
    }
    
    public boolean rightSideIsVisible(BlockChunk chunk, int blockx, int blocky, int blockz) {
        return isVisibleThrough(chunk.getBlock(blockx + 1, blocky, blockz));
    }
    
    public void bufferFlatVertices(DebugClient client, float startx, float starty, float startz, float xoff, float yoff, float zoff) {

        //Build a square out of two triangles.

        Vec3 topLeft, topRight, bottomLeft, bottomRight;
        
        int width, height;

        topLeft = new Vec3(0, 0, 0);

        if (xoff == 0) {
            topRight = new Vec3(0, 0, zoff);
            width = (int) zoff;
        } else {
            topRight = new Vec3(xoff, 0, 0);
            width = (int) xoff;
        }

        bottomRight = new Vec3(xoff, yoff, zoff);

        if (yoff == 0) {
            bottomLeft = new Vec3(0, 0, zoff);
            height = (int) zoff;
        } else{
            bottomLeft = new Vec3(0, yoff, 0);
            height = (int) yoff;
        }
        
        //adjust positions for where our starts are.
        topLeft.add(new Vec3(startx, starty, startz));
        topRight.add(new Vec3(startx, starty, startz));
        bottomLeft.add(new Vec3(startx, starty, startz));
        bottomRight.add(new Vec3(startx, starty, startz));

        //add first trangle, starting at top left corner, then top right, then bottom right
        client.vPos.set(topLeft); client.vTexCoord.set(0.0f, 0.0f); client.block_palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(topRight); client.vTexCoord.set(width, 0.0f); client.block_palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(bottomRight); client.vTexCoord.set(width, height); client.block_palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();

        //add second triangle, starting at top left corner, then bottom right, then bottom left
        client.vPos.set(topLeft); client.vTexCoord.set(0.0f, 0.0f); client.block_palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(bottomRight); client.vTexCoord.set(width, height); client.block_palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
        client.vPos.set(bottomLeft); client.vTexCoord.set(0.0f, height); client.block_palette_index.x = id; client.vRandom.set(topRight); client.buffer.vertex();
    }
}
