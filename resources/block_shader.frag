#version 140

uniform sampler2DRect u_block_palette;
uniform sampler2DRect u_side_palette;
uniform float u_texel_length;

in vec2 v_tex_coord;
flat in int i_block_palette_index;
flat in int i_side_palette_index;
flat in float f_random;
in float f_distance;

out vec3 out_color; //The color of a pixel/fragment.

// 2D Random
float randomV2ToF (vec2 st) {
    return fract(sin(dot(st,
                         vec2(234.66, 32.4)))
                 * 983.234567 * f_random);
}

vec2 randomV2toV2 (vec2 st) {
    return vec2(randomV2ToF(st * 0.87), randomV2ToF(st * 1.43));
}

float fand(float a, float b) {
    return a + b - a * b;
}

float startingInterp(float interpolator){
    return interpolator * interpolator;
}

float endingInterp(float interpolator){
    return 1 - startingInterp(1 - interpolator);
}

float smoothInterp(float interpolator){
    float start = startingInterp(interpolator);
    float end = endingInterp(interpolator);
    return mix(start, end, interpolator);
}

//valueNoise!
float valueNoise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    float bottomLeft = randomV2ToF(i);
    float bottomRight = randomV2ToF(i + vec2(1.0, 0.0));
    float topLeft = randomV2ToF(i + vec2(0.0, 1.0));
    float topRight = randomV2ToF(i + vec2(1.0, 1.0));
    
    // Smooth Interpolation
    float interpolatorX = smoothInterp(f.x);
    float interpolatorY = smoothInterp(f.y);

    float upperCells = mix(topLeft, topRight, interpolatorX);
    float lowerCells = mix(bottomLeft, bottomRight, interpolatorX);

    float noise = mix(lowerCells, upperCells, interpolatorY);
    return noise;
}

//Perlin Noise! Doesn't work properly right now.
float perlinNoise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    vec2 bottomLeftDir = randomV2toV2(i) * 2 - 1;
    vec2 bottomRightDir = randomV2toV2(i + vec2(1.0, 0.0)) * 2 - 1;
    vec2 topLeftDir = randomV2toV2(i + vec2(0.0, 1.0)) * 2 - 1;
    vec2 topRightDir = randomV2toV2(i + vec2(1.0, 1.0)) * 2 - 1;

    float bottomLeftFunc = dot(bottomLeftDir, f);
    float bottomRightFunc = dot(bottomRightDir, f - vec2(1, 0));
    float topLeftFunc = dot(topLeftDir, f - vec2(0, 1));
    float topRightFunc = dot(topRightDir, f - vec2(1, 1));
    
    // Smooth Interpolation
    float interpolatorX = smoothInterp(f.x);
    float interpolatorY = smoothInterp(f.y);

    float upperCells = mix(topLeftFunc, topRightFunc, interpolatorX);
    float lowerCells = mix(bottomLeftFunc, bottomRightFunc, interpolatorX);

    float noise = mix(lowerCells, upperCells, interpolatorY);
    return noise;
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
    return getBlockPaletteColor(int(floor(randomV2ToF(texel_coords) * palette_size)), palette_length).rgb;
}

void main() {
    vec2 texel_position = floor(v_tex_coord * u_texel_length);

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
        float n = valueNoise(texel_position / 2);
        n = fand(n, 0.35);
        out_color = mix(out_color, getSidePaletteColor(1, side_palette_length), pow(n, 5));
        return;
    }
}