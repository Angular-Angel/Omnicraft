#version 140

uniform mat3 u_matrix; //Supplied once per frame.

in vec3 in_pos; //Supplied by the vertex buffer.
in vec2 in_tex_coord;

out vec2 v_tex_coord;

void main()
{
    v_tex_coord = in_tex_coord;
    
    //gl_Position is one of the few built-in variables in GLSL.
    gl_Position = vec4(u_matrix*in_pos, 1.0);
}
