/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

import com.samrj.devil.gl.DGL;
import com.samrj.devil.gl.VertexStream;
import com.samrj.devil.math.Vec3;
import com.samrj.devil.math.Vec3i;
import net.angle.omnicraft.world.World;
import static org.lwjgl.opengl.GL11.GL_LINES;

/**
 *
 * @author angle
 */
public class OutlineStreamManager extends VertexManager {
    
    private VertexStream blockOutline;
    private Vec3 blockOutlineVPos;
    
    @Override
    public void begin() {
        blockOutline = DGL.genVertexStream(8, 24);
        blockOutlineVPos = blockOutline.vec3("in_pos");
        blockOutline.begin();
        super.begin();
    }
    
    public void streamBlockOutline(Vec3i coord) {
        //starting corner
        float startx = coord.x * World.EDGE_LENGTH_OF_BLOCK;
        float starty = coord.y * World.EDGE_LENGTH_OF_BLOCK;
        float startz = coord.z * World.EDGE_LENGTH_OF_BLOCK;
        
        //end corner
        float endx = startx + World.EDGE_LENGTH_OF_BLOCK;
        float endy = starty + World.EDGE_LENGTH_OF_BLOCK;
        float endz = startz + World.EDGE_LENGTH_OF_BLOCK;
        
        blockOutlineVPos.set(new Vec3(startx, starty, startz)); blockOutline.vertex();
        
        blockOutlineVPos.set(new Vec3(endx, starty, startz)); blockOutline.vertex();
        blockOutlineVPos.set(new Vec3(startx, endy, startz)); blockOutline.vertex();
        blockOutlineVPos.set(new Vec3(startx, starty, endz)); blockOutline.vertex();
        
        blockOutlineVPos.set(new Vec3(endx, endy, startz)); blockOutline.vertex();
        blockOutlineVPos.set(new Vec3(startx, endy, endz)); blockOutline.vertex();
        blockOutlineVPos.set(new Vec3(endx, starty, endz)); blockOutline.vertex();
        
        blockOutlineVPos.set(new Vec3(endx, endy, endz)); blockOutline.vertex();
        
        blockOutline.index(0);
        blockOutline.index(1);
        
        blockOutline.index(0);
        blockOutline.index(2);
        
        blockOutline.index(0);
        blockOutline.index(3);
        
        blockOutline.index(1);
        blockOutline.index(4);
        
        blockOutline.index(1);
        blockOutline.index(6);
        
        blockOutline.index(2);
        blockOutline.index(4);
        
        blockOutline.index(2);
        blockOutline.index(5);
        
        blockOutline.index(3);
        blockOutline.index(5);
        
        blockOutline.index(3);
        blockOutline.index(6);
        
        blockOutline.index(4);
        blockOutline.index(7);
        
        blockOutline.index(5);
        blockOutline.index(7);
        
        blockOutline.index(6);
        blockOutline.index(7);
        
        upload();
    }

    @Override
    protected void uploadVertices() {
        blockOutline.upload();
    }

    @Override
    protected void deleteVertices() {
        DGL.delete(blockOutline);
    }

    @Override
    protected void drawVertices() {
        DGL.draw(blockOutline, GL_LINES);
    }
}
