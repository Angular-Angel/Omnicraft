#version 140

uniform sampler2DRect u_palette;

in vec2 v_tex_coord;
flat in int i_palette_index;
flat in float f_random;

out vec3 out_color; //The color of a pixel/fragment.

// 2D Random
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(234.66, 32.4)))
                 * 983.234567 * f_random);
}

void main() {
    vec2 st = floor(v_tex_coord * 16);
    
    int palette_size = textureSize(u_palette).x;
    // Use the random function, times the palette_size and then floored, 
    //and then use that to select a color from the palette and return that color.
    out_color = texelFetch(u_palette, ivec2(int(floor(random(st) * palette_size)), i_palette_index)).rgb;
}
