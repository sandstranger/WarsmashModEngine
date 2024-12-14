#version 100

precision mediump float;

    uniform sampler2D u_boneMap;
    uniform float u_vectorSize;
    uniform float u_rowSize;
    mat4 fetchMatrix(float column, float row) {
      column *= u_vectorSize * 4.0;
      row *= u_rowSize;
      // Add in half texel to sample in the middle of the texel.
      // Otherwise, since the sample is directly on the boundry, small floating point errors can cause the sample to get the wrong pixel.
      // This is mostly noticable with NPOT textures, which the bone maps are.
      column += 0.5 * u_vectorSize;
      row += 0.5 * u_rowSize;
      return mat4(texture2D(u_boneMap, vec2(column, row)),
                  texture2D(u_boneMap, vec2(column + u_vectorSize, row)),
                  texture2D(u_boneMap, vec2(column + u_vectorSize * 2.0, row)),
                  texture2D(u_boneMap, vec2(column + u_vectorSize * 3.0, row)));
    }
    uniform mat4 u_VP;
    uniform mat4 u_MV;
    uniform vec3 u_eyePos;
    uniform sampler2D u_lightTexture;
    uniform float u_lightTextureHeight;
    uniform float u_layerAlpha;
    uniform bool u_hasBones;
        attribute vec3 a_position;
    attribute vec3 a_normal;
    attribute vec2 a_uv;
    attribute vec4 a_tangent;
    
    
#define SKIN
#ifdef SKIN
attribute vec4 a_bones;
attribute vec4 a_weights;
void transformSkin(inout vec3 position, inout vec3 normal, inout vec3 tangent, inout vec3 binormal) {
  mat4 bone = mat4(0);
  bone += fetchMatrix(a_bones[0], 0.0) * a_weights[0];
  bone += fetchMatrix(a_bones[1], 0.0) * a_weights[1];
  bone += fetchMatrix(a_bones[2], 0.0) * a_weights[2];
  bone += fetchMatrix(a_bones[3], 0.0) * a_weights[3];
  mat3 rotation = mat3(bone);
  position = vec3(bone * vec4(position, 1.0));
  normal = rotation * normal;
  tangent = rotation * tangent;
  binormal = rotation * binormal;
}
#else
attribute vec4 a_bones;
#ifdef EXTENDED_BONES
attribute vec4 a_extendedBones;
#endif
attribute float a_boneNumber;
mat4 getVertexGroupMatrix() {
  mat4 bone;
  // For the broken models out there, since the game supports this.
  if (a_boneNumber > 0.0) {
    for (int i = 0; i < 4; i++) {
      if (a_bones[i] > 0.0) {
        bone += fetchMatrix(a_bones[i] - 1.0, 0.0);
      }
    }
    #ifdef EXTENDED_BONES
      for (int i = 0; i < 4; i++) {
        if (a_extendedBones[i] > 0.0) {
          bone += fetchMatrix(a_extendedBones[i] - 1.0, 0.0);
        }
      }
    #endif
  }
  return bone / a_boneNumber;
}
void transformVertexGroups(inout vec3 position, inout vec3 normal) {
  mat4 bone = getVertexGroupMatrix();
  mat3 rotation = mat3(bone);
  position = vec3(bone * vec4(position, 1.0));
  normal = normalize(rotation * normal);
}
void transformVertexGroupsHD(inout vec3 position, inout vec3 normal, inout vec3 tangent, inout vec3 binormal) {
  mat4 bone = getVertexGroupMatrix();
  mat3 rotation = mat3(bone);
  position = vec3(bone * vec4(position, 1.0));
  normal = normalize(rotation * normal);
  tangent = normalize(rotation * tangent);
  binormal = normalize(rotation * binormal);
}
#endif    
    
vec3 TBN(vec3 vector, vec3 tangent, vec3 binormal, vec3 normal) {
  return vec3(dot(vector, tangent), dot(vector, binormal), dot(vector, normal));
}
    
    
    varying vec2 v_uv;
    varying float v_layerAlpha;
    varying vec4 v_lightDir;
    varying vec4 v_lightDir2;
    varying vec4 v_lightDir3;
    varying vec4 v_lightDir4;
    varying vec4 v_lightDir5;
    varying vec4 v_lightDir6;
    varying vec4 v_lightDir7;
    varying vec4 v_lightDir8;
    varying vec3 v_eyeVec;
    varying vec3 v_normal;
    
    
    void main() {
      vec3 position = a_position;
      vec3 normal = a_normal;
      vec3 tangent = a_tangent.xyz;
      
      
      tangent = normalize(tangent - dot(tangent, normal) * normal);
      
      vec3 binormal = cross(normal, tangent) * a_tangent.w;
      
      if (u_hasBones) {
        #ifdef SKIN
          transformSkin(position, normal, tangent, binormal);
        #else
          transformVertexGroupsHD(position, normal, tangent, binormal);
        #endif
      }
      
      vec3 position_mv = vec3(u_MV * vec4(position, 1));
      
      mat3 mv = mat3(u_MV);
      vec3 t = normalize(mv * tangent);
      vec3 b = normalize(mv * binormal);
      vec3 n = normalize(mv * normal);
      
      v_eyeVec = normalize(TBN(normalize(mv * u_eyePos - position_mv), t, b, n));
      
      float rowPos = (0.5) / u_lightTextureHeight;
      vec4 lightPosition = texture2D(u_lightTexture, vec2(0.125, rowPos));
      vec4 lightExtra = texture2D(u_lightTexture, vec2(0.375, rowPos));
      vec3 u_lightPos = mv * lightPosition.xyz;
      vec3 lightDir;
      if(lightExtra.x > 0.5) {
          // Sunlight ('directional')
      	   lightDir = normalize(u_lightPos);
          v_lightDir = vec4(normalize(TBN(lightDir, t, b, n)), 1.0);
      } else {
          // Point light ('omnidirectional')
          vec3 delta = u_lightPos - position_mv;
          lightDir = normalize(delta);
            float dist = length(delta) / 64.0 + 1.0;
          v_lightDir = vec4(normalize(TBN(lightDir, t, b, n)), 1.0/pow(dist, 2.0));
      }
      
      if( u_lightTextureHeight > 1.5 ) {
          float rowPos = (1.5) / u_lightTextureHeight;
          vec4 lightPosition2 = texture2D(u_lightTexture, vec2(0.125, rowPos));
          vec4 lightExtra2 = texture2D(u_lightTexture, vec2(0.375, rowPos));
          vec3 u_lightPos2 = mv * lightPosition2.xyz;
          vec3 lightDir2;
          if(lightExtra2.x > 0.5) {
              // Sunlight ('directional')
          	   lightDir2 = normalize(u_lightPos2);
              v_lightDir2 = vec4(normalize(TBN(lightDir2, t, b, n)), 1.0);
          } else {
              // Point light ('omnidirectional')
              vec3 delta = u_lightPos2 - position_mv;
              lightDir2 = normalize(delta);
                float dist = length(delta) / 64.0 + 1.0;
              v_lightDir2 = vec4(normalize(TBN(lightDir2, t, b, n)), 1.0/pow(dist, 2.0));
          }
          if( u_lightTextureHeight > 2.5 ) {
              float rowPos = (2.5) / u_lightTextureHeight;
              vec4 lightPosition3 = texture2D(u_lightTexture, vec2(0.125, rowPos));
              vec4 lightExtra3 = texture2D(u_lightTexture, vec2(0.375, rowPos));
              vec3 u_lightPos3 = mv * lightPosition3.xyz;
              vec3 lightDir3;
              if(lightExtra3.x > 0.5) {
                  // Sunlight ('directional')
              	   lightDir3 = normalize(u_lightPos3);
                  v_lightDir3 = vec4(normalize(TBN(lightDir3, t, b, n)), 1.0);
              } else {
                  // Point light ('omnidirectional')
                  vec3 delta = u_lightPos3 - position_mv;
                  lightDir3 = normalize(delta);
                    float dist = length(delta) / 64.0 + 1.0;
                  v_lightDir3 = vec4(normalize(TBN(lightDir3, t, b, n)), 1.0/pow(dist, 2.0));
              }
              if( u_lightTextureHeight > 3.5 ) {
                  float rowPos = (3.5) / u_lightTextureHeight;
                  vec4 lightPosition4 = texture2D(u_lightTexture, vec2(0.125, rowPos));
                  vec4 lightExtra4 = texture2D(u_lightTexture, vec2(0.375, rowPos));
                  vec3 u_lightPos4 = mv * lightPosition4.xyz;
                  vec3 lightDir4;
                  if(lightExtra4.x > 0.5) {
                      // Sunlight ('directional')
                  	   lightDir4 = normalize(u_lightPos4);
                      v_lightDir4 = vec4(normalize(TBN(lightDir4, t, b, n)), 1.0);
                  } else {
                      // Point light ('omnidirectional')
                      vec3 delta = u_lightPos4 - position_mv;
                      lightDir4 = normalize(delta);
                        float dist = length(delta) / 64.0 + 1.0;
                      v_lightDir4 = vec4(normalize(TBN(lightDir4, t, b, n)), 1.0/pow(dist, 2.0));
                  }
                  if( u_lightTextureHeight > 4.5 ) {
                      float rowPos = (4.5) / u_lightTextureHeight;
                      vec4 lightPosition5 = texture2D(u_lightTexture, vec2(0.125, rowPos));
                      vec4 lightExtra5 = texture2D(u_lightTexture, vec2(0.375, rowPos));
                      vec3 u_lightPos5 = mv * lightPosition5.xyz;
                      vec3 lightDir5;
                      if(lightExtra5.x > 0.5) {
                          // Sunlight ('directional')
                      	   lightDir5 = normalize(u_lightPos5);
                          v_lightDir5 = vec4(normalize(TBN(lightDir5, t, b, n)), 1.0);
                      } else {
                          // Point light ('omnidirectional')
                          vec3 delta = u_lightPos5 - position_mv;
                          lightDir5 = normalize(delta);
                            float dist = length(delta) / 64.0 + 1.0;
                          v_lightDir5 = vec4(normalize(TBN(lightDir5, t, b, n)), 1.0/pow(dist, 2.0));
                      }
                      if( u_lightTextureHeight > 5.5 ) {
                          float rowPos = (5.5) / u_lightTextureHeight;
                          vec4 lightPosition6 = texture2D(u_lightTexture, vec2(0.125, rowPos));
                          vec4 lightExtra6 = texture2D(u_lightTexture, vec2(0.375, rowPos));
                          vec3 u_lightPos6 = mv * lightPosition6.xyz;
                          vec3 lightDir6;
                          if(lightExtra6.x > 0.5) {
                              // Sunlight ('directional')
                          	   lightDir6 = normalize(u_lightPos6);
                              v_lightDir6 = vec4(normalize(TBN(lightDir6, t, b, n)), 1.0);
                          } else {
                              // Point light ('omnidirectional')
                              vec3 delta = u_lightPos6 - position_mv;
                              lightDir6 = normalize(delta);
                                float dist = length(delta) / 64.0 + 1.0;
                              v_lightDir6 = vec4(normalize(TBN(lightDir6, t, b, n)), 1.0/pow(dist, 2.0));
                          }
                          if( u_lightTextureHeight > 6.5 ) {
                              float rowPos = (6.5) / u_lightTextureHeight;
                              vec4 lightPosition7 = texture2D(u_lightTexture, vec2(0.125, rowPos));
                              vec4 lightExtra7 = texture2D(u_lightTexture, vec2(0.375, rowPos));
                              vec3 u_lightPos7 = mv * lightPosition7.xyz;
                              vec3 lightDir7;
                              if(lightExtra7.x > 0.5) {
                                  // Sunlight ('directional')
                              	   lightDir7 = normalize(u_lightPos7);
                                  v_lightDir7 = vec4(normalize(TBN(lightDir7, t, b, n)), 1.0);
                              } else {
                                  // Point light ('omnidirectional')
                                  vec3 delta = u_lightPos7 - position_mv;
                                  lightDir7 = normalize(delta);
                                    float dist = length(delta) / 64.0 + 1.0;
                                  v_lightDir7 = vec4(normalize(TBN(lightDir7, t, b, n)), 1.0/pow(dist, 2.0));
                              }
                              if( u_lightTextureHeight > 7.5 ) {
                                  float rowPos = (7.5) / u_lightTextureHeight;
                                  vec4 lightPosition8 = texture2D(u_lightTexture, vec2(0.125, rowPos));
                                  vec4 lightExtra8 = texture2D(u_lightTexture, vec2(0.375, rowPos));
                                  vec3 u_lightPos8 = mv * lightPosition8.xyz;
                                  vec3 lightDir8;
                                  if(lightExtra8.x > 0.5) {
                                      // Sunlight ('directional')
                                  	   lightDir8 = normalize(u_lightPos8);
                                      v_lightDir8 = vec4(normalize(TBN(lightDir8, t, b, n)), 1.0);
                                  } else {
                                      // Point light ('omnidirectional')
                                      vec3 delta = u_lightPos8 - position_mv;
                                      lightDir8 = normalize(delta);
                                        float dist = length(delta) / 64.0 + 1.0;
                                      v_lightDir8 = vec4(normalize(TBN(lightDir8, t, b, n)), 1.0/pow(dist, 2.0));
                                  }
                              } else {
                                  v_lightDir8 = vec4(0.0);
                                  
                              }
                          } else {
                              v_lightDir7 = vec4(0.0);
                              
                          }
                      } else {
                          v_lightDir6 = vec4(0.0);
                          
                      }
                  } else {
                      v_lightDir5 = vec4(0.0);
                      
                  }
              } else {
                  v_lightDir4 = vec4(0.0);
                  
              }
          } else {
              v_lightDir3 = vec4(0.0);
              
          }
      } else {
          v_lightDir2 = vec4(0.0);
          
      }
      
      v_uv = a_uv;
      v_layerAlpha = u_layerAlpha;
      
      v_normal = normal;
      // v_lightDirWorld = normalize(lightDir);
      
      gl_Position = u_VP * vec4(position, 1.0);
    }