/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.graphics;

/**
 *
 * @author angle
 */
public abstract class VertexManager {
    
    public boolean loaded;
    public boolean drawing;
    
    protected abstract void uploadVertices();
    
    protected abstract void deleteVertices();
    
    protected abstract void drawVertices();
    
    public void begin() {
        loaded = true;
        drawing = true;
    }
    
    public void upload() {
        if (loaded && drawing)
            uploadVertices();
    }
    
    public void clearVertices() {
        if (loaded)
            deleteVertices();
        loaded = false;
    }
    
    public void draw() {
        if (loaded && drawing)
            drawVertices();
    }
    
}
