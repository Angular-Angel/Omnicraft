/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.world.blocks;

import com.samrj.devil.math.Vec2;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.graphics.RenderData;
import net.angle.omnicraft.world.BlockChunk;
import net.angle.omnicraft.world.BlockContainer;
import net.angle.omnicraft.world.VoxelContainer;

/**
 *
 * @author angle
 * @license https://gitlab.com/AngularAngel/omnicraft/-/blob/master/LICENSE
 */
public abstract class Block extends Renderable {

    public Block(String name, int id, RenderData renderData) {
        super(name, id, renderData);
    }
    
    public enum BlockFace {
        
        top(0), bottom(1), front(2), back(3), left(4), right(5);
        
        public final int id;
        
        private BlockFace(int id) {
            this.id = id;
        }
        
        public Vec3i getStartingPosition(VoxelContainer container) {
            switch(this) {
                case top: return new Vec3i(container.getEdgeLength() - 1, container.getEdgeLength() - 1, 0);
                case bottom: return new Vec3i(0, 0, 0);
                case front: return new Vec3i(0, 0, container.getEdgeLength() - 1);
                case back: return new Vec3i(container.getEdgeLength() - 1, 0, 0);
                case left: return new Vec3i(0, 0, 0);
                case right: return new Vec3i(container.getEdgeLength() - 1, 0, container.getEdgeLength() - 1);
            }
            return new Vec3i(-1, -1, -1);
        }
        
        public Vec3 getDrawStart(Vec3 drawStart) {
            return getDrawStart(drawStart.x, drawStart.y, drawStart.z);
        }
        
        public Vec3 getDrawStart(float x, float y, float z) {
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
        
        public boolean continueAcross(Vec3i coord, VoxelContainer container) {
            switch(this) {
                case top: return coord.z < container.getEdgeLength() - 1;
                case bottom: return coord.z < container.getEdgeLength() - 1;
                case front: return coord.x < container.getEdgeLength() - 1;
                case back: return coord.x > 0;
                case left: return coord.z < container.getEdgeLength() - 1;
                case right: return coord.z > 0;
            }
            return false;
        }
        
        public boolean continueDown(Vec3i coord, VoxelContainer container) {
            switch(this) {
                case top: return coord.x > 0;
                case bottom: return coord.x < container.getEdgeLength() - 1;
                case front:
                case back:
                case left:
                case right: return coord.y < container.getEdgeLength() - 1;
            }
            return false;
        }
        
        public Vec3 orientFace(Vec2 dimensions) {
            Vec3 direction = new Vec3();
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
    
    public boolean isVisibleThrough(Block adjacentBlock) {
        return adjacentBlock == null || adjacentBlock.isTransparent();
    }
    
    public boolean faceIsVisible(BlockFace face, BlockContainer container, Vec3i coord) {
        return faceIsVisible(face, container, coord.x, coord.y, coord.z);
    }
    
    public boolean faceIsVisible(BlockFace face, BlockContainer container, int blockx, int blocky, int blockz) {
        switch(face) {
            case top: return topIsVisible(container, blockx, blocky, blockz);
            case bottom: return bottomIsVisible(container, blockx, blocky, blockz);
            case front: return frontIsVisible(container, blockx, blocky, blockz);
            case back: return backIsVisible(container, blockx, blocky, blockz);
            case left: return leftSideIsVisible(container, blockx, blocky, blockz);
            case right: return rightSideIsVisible(container, blockx, blocky, blockz);
            default: throw new IllegalArgumentException("Checking visibility of face: " + face);
        }
    }
    
    public boolean topIsVisible(BlockContainer container, int blockx, int blocky, int blockz) {
        return isVisibleThrough(container.getBlock(blockx, blocky + 1, blockz));
    }
    
    public boolean bottomIsVisible(BlockContainer container, int blockx, int blocky, int blockz) {
        return isVisibleThrough(container.getBlock(blockx, blocky - 1, blockz));
    }
    
    public boolean frontIsVisible(BlockContainer container, int blockx, int blocky, int blockz) {
        return isVisibleThrough(container.getBlock(blockx, blocky, blockz + 1));
    }
    
    public boolean backIsVisible(BlockContainer container, int blockx, int blocky, int blockz) {
        return isVisibleThrough(container.getBlock(blockx, blocky, blockz - 1));
    }
    
    public boolean leftSideIsVisible(BlockContainer container, int blockx, int blocky, int blockz) {
        return isVisibleThrough(container.getBlock(blockx - 1, blocky, blockz));
    }
    
    public boolean rightSideIsVisible(BlockContainer container, int blockx, int blocky, int blockz) {
        return isVisibleThrough(container.getBlock(blockx + 1, blocky, blockz));
    }
}