// Basic Texture Shader

#type vertex
#version 330 core

layout(location = 0) in vec3 a_Position;
layout(location = 1) in vec4 a_Color;
layout(location = 2) in vec2 a_TextureCoordinate;
layout(location = 3) in float a_TextureIndex;
layout(location = 4) in float a_TilingFactor;

uniform mat4 u_ViewProjection;

out vec4 v_Color;
out vec2 v_TextureCoordinate;
out float v_TextureIndex;
out float v_TilingFactor;

void main() {
	v_Color = a_Color;
	v_TextureCoordinate = a_TextureCoordinate;
	v_TextureIndex = a_TextureIndex;
	v_TilingFactor = a_TilingFactor;
	gl_Position = u_ViewProjection * vec4(a_Position, 1.0);
}


#type fragment
#version 330 core

layout(location = 0) out vec4 color;

in vec4 v_Color;
in vec2 v_TextureCoordinate;
in float v_TextureIndex;
in float v_TilingFactor;

uniform sampler2D u_Textures[16];

void main() {
	vec4 textureColor = v_Color;
	switch (int(v_TextureIndex)) {
		case  0: textureColor *= texture(u_Textures[ 0], v_TextureCoordinate * v_TilingFactor); break;
		case  1: textureColor *= texture(u_Textures[ 1], v_TextureCoordinate * v_TilingFactor); break;
		case  2: textureColor *= texture(u_Textures[ 2], v_TextureCoordinate * v_TilingFactor); break;
		case  3: textureColor *= texture(u_Textures[ 3], v_TextureCoordinate * v_TilingFactor); break;
		case  5: textureColor *= texture(u_Textures[ 5], v_TextureCoordinate * v_TilingFactor); break;
		case  6: textureColor *= texture(u_Textures[ 6], v_TextureCoordinate * v_TilingFactor); break;
		case  7: textureColor *= texture(u_Textures[ 7], v_TextureCoordinate * v_TilingFactor); break;
		case  8: textureColor *= texture(u_Textures[ 8], v_TextureCoordinate * v_TilingFactor); break;
		case  9: textureColor *= texture(u_Textures[ 9], v_TextureCoordinate * v_TilingFactor); break;
		case 10: textureColor *= texture(u_Textures[10], v_TextureCoordinate * v_TilingFactor); break;
		case 11: textureColor *= texture(u_Textures[11], v_TextureCoordinate * v_TilingFactor); break;
		case 12: textureColor *= texture(u_Textures[12], v_TextureCoordinate * v_TilingFactor); break;
		case 13: textureColor *= texture(u_Textures[13], v_TextureCoordinate * v_TilingFactor); break;
		case 14: textureColor *= texture(u_Textures[14], v_TextureCoordinate * v_TilingFactor); break;
		case 15: textureColor *= texture(u_Textures[15], v_TextureCoordinate * v_TilingFactor); break;
	}
	color = textureColor;
}
