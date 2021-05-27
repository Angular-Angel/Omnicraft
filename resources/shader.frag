#version 140

uniform sampler2DRect u_block_palette;
uniform sampler2DRect u_side_palette;

in vec2 v_tex_coord;
flat in int i_block_palette_index;
flat in int i_side_palette_index;
flat in float f_random;
in float f_distance;

out vec3 out_color; //The color of a pixel/fragment.

// 2D Random
float random (vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(234.66, 32.4)))
                 * 983.234567 * f_random);
}

float noise (vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    // Smooth Interpolation

    // Cubic Hermine Curve.  Same as SmoothStep()
    vec2 u = f*f*(3.0-2.0*f);
    // u = smoothstep(0.,1.,f);

    // Mix 4 coorners percentages
    return mix(a, b, u.x) +
            (c - a)* u.y * (1.0 - u.x) +
            (d - b) * u.x * u.y;
}

vec3 getBlockPaletteColor(int index, int palette_length) {
    return texelFetch(u_block_palette, ivec2(index, palette_length - i_block_palette_index)).rgb;
}


vec3 getSidePaletteColor(int index, int palette_length) {
    return texelFetch(u_side_palette, ivec2(index, palette_length - i_side_palette_index)).rgb;
}

// Use the random function, times the palette_size and then floored, 
// and then use that to select a color from the palette and return that color.
vec3 getTexel(vec2 texel_coords, int palette_size, int palette_length) {
    return getBlockPaletteColor(int(floor(random(texel_coords) * palette_size)), palette_length).rgb;
}

void main() {
    
    //We use 15.9999 here instead of 16 to prevent having a sliver where it hits 16 on the far edges, and giving us lines.
    vec2 texel_position = floor(v_tex_coord * 15.9999);

    ivec2 block_texture_size = textureSize(u_block_palette);

    int block_palette_size = block_texture_size.x - 1;
    int block_palette_length = block_texture_size.y - 1;
    
    out_color = getTexel(texel_position, block_palette_size, block_palette_length);

    vec2 texel_above = vec2(texel_position.x, texel_position.y - 1);
    vec2 texel_below = vec2(texel_position.x, texel_position.y + 1);

    vec2 texel_left = vec2(texel_position.x - 1, texel_position.y);
    vec2 texel_right = vec2(texel_position.x + 1, texel_position.y);

    vec3 color_above = getTexel(texel_above, block_palette_size, block_palette_length);
    vec3 color_below = getTexel(texel_below, block_palette_size, block_palette_length);

    vec3 color_left = getTexel(texel_left, block_palette_size, block_palette_length);
    vec3 color_right = getTexel(texel_right, block_palette_size, block_palette_length);

    vec3 blur_color = mix(mix(color_above, color_below, 0.5), mix(color_left, color_right, 0.5), 0.5);

    //make things darker the farther away they are, 
    float distance = f_distance / 75;
    float blur_factor = 1.0 / exp((distance) * (distance));

    out_color = mix(blur_color, out_color, 0.4);

    out_color = mix(getBlockPaletteColor(block_palette_size, block_palette_length), out_color, blur_factor);
    if (i_side_palette_index != 0) {
    
        ivec2 side_texture_size = textureSize(u_side_palette);

        int side_palette_size = side_texture_size.x - 1;
        int side_palette_length = side_texture_size.y - 1;
        float n = noise(texel_position / 2);
        out_color = mix(out_color, getSidePaletteColor(1, side_palette_length), pow(n, 5));
        return;
    }
}