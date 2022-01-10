#version 100

precision highp float;


uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec4 aColor;
attribute vec2 aTexCoords;
varying vec4 vColor;
varying vec4 vPosition;
varying vec3 vNormal;
varying vec2 vTexCoords;
const int MAX_JOINTS = 60;
attribute vec3 aJointIndices;
attribute vec3 aWeights;
uniform mat4 uBindShapeMatrix;
uniform mat4 uJointTransforms[MAX_JOINTS];



void main() {

    vColor = aColor;
    vTexCoords = aTexCoords;

    vec4 bindPos = uBindShapeMatrix * aPosition;
    vec3 bindNorm = mat3(uBindShapeMatrix) * aNormal;

    vec4 totalLocalPos = vec4(0.0);
    vec3 totalLocalNorm = vec3(0.0);

    for (int i = 0; i < 3; i++){
        totalLocalPos += uJointTransforms[int(aJointIndices[i])] * bindPos * aWeights[i];
        totalLocalNorm += mat3(uJointTransforms[int(aJointIndices[i])]) * bindNorm * aWeights[i];
    }

    vPosition = uMMatrix * totalLocalPos;
    vNormal = normalize(mat3(uMMatrix) * totalLocalNorm);

    gl_Position = uMVPMatrix * totalLocalPos;
}
