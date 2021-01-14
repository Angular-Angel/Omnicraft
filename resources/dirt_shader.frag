in vec2 v_tex_coord;

out vec3 out_color; //The color of a pixel/fragment.

// 2D Random
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(234.66, 32.4)))
                 * 98765.234567);
}

void main() {
    vec2 st = floor(v_tex_coord.xy * 16);

    // Use the noise function
    float n = random(st);

    out_color = new vec3(n);
}
