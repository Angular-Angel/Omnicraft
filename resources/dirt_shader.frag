#version 140

uniform sampler2DRect u_palette;

in vec2 v_tex_coord;
flat in int i_palette_index;
flat in float f_random;
in float f_distance;

out vec3 out_color; //The color of a pixel/fragment.

// 2D Random
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(234.66, 32.4)))
                 * 983.234567 * f_random);
}

void main() {
    //We use 15.9999 here instead of 16 to prevent having a sliver where it hits 16 on the far edges, and giving us lines.
    vec2 texel_position = floor(v_tex_coord * 15.9999);

    ivec2 texture_size = textureSize(u_palette);

    int palette_size = texture_size.x;
    int palette_length = texture_size.y - 1;

    // Use the random function, times the palette_size and then floored, 
    //and then use that to select a color from the palette and return that color.
    out_color = texelFetch(u_palette, ivec2(int(floor(random(texel_position) * palette_size)), palette_length - i_palette_index)).rgb;
    
    //Blur things the farther away they are, to reduce obnoxious static.
    float blur_factor = 1.0 / exp((f_distance * 2) * (f_distance * 2));

    vec2 texel_above = vec2(texel_position.x, texel_position.y - 1);
    vec2 texel_below = vec2(texel_position.x, texel_position.y + 1);

    vec2 texel_left = vec2(texel_position.x - 1, texel_position.y);
    vec2 texel_right = vec2(texel_position.x + 1, texel_position.y);

    vec3 color_above = texelFetch(u_palette, ivec2(int(floor(random(texel_above) * palette_size)), palette_length - i_palette_index)).rgb;
    vec3 color_below = texelFetch(u_palette, ivec2(int(floor(random(texel_below) * palette_size)), palette_length - i_palette_index)).rgb;

    vec3 color_left = texelFetch(u_palette, ivec2(int(floor(random(texel_left) * palette_size)), palette_length - i_palette_index)).rgb;
    vec3 color_right = texelFetch(u_palette, ivec2(int(floor(random(texel_right) * palette_size)), palette_length - i_palette_index)).rgb;

    vec3 blur_color = mix(mix(color_above, color_below, 0.5), mix(color_left, color_right, 0.5), 0.5);

    out_color = mix(blur_color, out_color, blur_factor);
    
}
