#version 140

uniform sampler2DRect u_palette;
uniform int u_palette_size;

in vec2 v_tex_coord;
flat in int i_palette_index;

out vec3 out_color; //The color of a pixel/fragment.

// 2D Random
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(234.66, 32.4)))
                 * 98765.234567);
}

void main() {
    vec2 st = floor(v_tex_coord * 16);

    // Use the random function, times the palette_size and then floored, 
    //and then use that to select a color from the palette and return that color.
    out_color = texelFetch(u_palette, ivec2(int(floor(random(st) * u_palette_size)), i_palette_index)).rgb;
}
