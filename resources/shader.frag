uniform sampler2D u_texture;

in vec2 v_tex_coord;

out vec3 out_color; //The color of a pixel/fragment.

void main()
{
    out_color = texture(u_texture, v_tex_coord).rgb;
}