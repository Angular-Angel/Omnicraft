/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.game.Game;
import com.samrj.devil.gl.DGL;
import com.samrj.devil.gui.DUI;
import org.lwjgl.system.APIUtil;

/**
 *
 * @author angle
 */
public interface Client {
    public default void run() {
        try
        {
            this.preInit();

            Game.onInit(() -> {
                DGL.init();
                DUI.init();
                
                this.init();
                
                Game.onResize(this::resize);
                Game.onMouseMoved(this::mouseMoved);
                Game.onKey(this::key);
                Game.onStep(this::step);
                Game.onRender(this::render);
            });
            
            Game.onDestroy(this::destroy);

            Game.run();
            
        } catch (Throwable t) {
            t.printStackTrace();
            APIUtil.DEBUG_STREAM.close(); //Prevent LWJGL leak message spam.
            System.exit(-1);
        }
    }

    public void preInit();

    public void init();

    public void mouseMoved(float x, float y);

    public void key(int key, int action, int mods);

    public void resize(int width, int height);

    public void step(float dt);

    public void render();

    public void destroy(Boolean crashed);
}
