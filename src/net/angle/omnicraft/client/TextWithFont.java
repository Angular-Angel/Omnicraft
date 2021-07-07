/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.angle.omnicraft.client;

import com.samrj.devil.gui.DUI;
import com.samrj.devil.gui.DUIDrawer;
import com.samrj.devil.gui.Text;
import com.samrj.devil.gui.Font;

/**
 *
 * @author angle
 */
public class TextWithFont extends Text {
    
    private Font font;
    
    public TextWithFont(String text, Font font) {
        super(text);
        this.font = font;
    }
    
    @Override
    protected void updateSize()
    {
        width = font.getWidth(getText());
        height = font.getHeight();
    }
    
    @Override
    protected void render(DUIDrawer drawer) {
        Font current = DUI.font();
        if (font != current)
            DUI.setFont(font);
        super.render(drawer);
        if (font != current)
            DUI.setFont(current);
    }
}
