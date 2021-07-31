#version 140

uniform float u_texel_length;

in vec2 v_tex_coord;

out vec3 out_color; //The color of a pixel/fragment.

void main() {
    out_color = vec3(0.25, 0.25, 0.1);
}