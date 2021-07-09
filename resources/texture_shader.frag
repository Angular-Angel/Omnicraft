#version 140

uniform sampler2D u_texture; //Only has red channel.

in vec2 v_tex_coord;

out vec4 out_color; //The color of a pixel/fragment.

void main()
{
    out_color = texture(u_texture, v_tex_coord);
}
