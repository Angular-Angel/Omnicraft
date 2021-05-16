#version 140

uniform mat4 u_projection_matrix; //Supplied once per frame.
uniform mat4 u_view_matrix;

in vec3 in_pos; //Supplied by the vertex buffer.
in vec2 in_tex_coord;
in int in_block_palette_index;
in int in_side_palette_index;
in vec3 in_random;

flat out int i_block_palette_index;
flat out int i_side_palette_index;
out vec2 v_tex_coord;
flat out float f_random;
out float f_distance;

float random (in vec3 st) {
    return fract(sin(dot(st.xyz,
                         vec3(234.66, 32.4, 564.7)))
                 * 95.234567);
}

void main()
{
    i_block_palette_index = in_block_palette_index;
    v_tex_coord = in_tex_coord;
    f_random = random(in_random);

    //Position should be a vec4 because matrix transformations require homogeneous coordinates.
    //So this ends up being vec4(x, y, z, 1.0)
    vec4 pos = vec4(in_pos, 1.0);
    
    //gl_Position is one of the few built-in variables in GLSL.
    gl_Position = u_projection_matrix*u_view_matrix*pos;

    f_distance = length(gl_Position);
}
