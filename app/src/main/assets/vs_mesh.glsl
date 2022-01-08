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
    /// TODO la normale deve cambiare con l'animazione
    vNormal = normalize(vec3(uMMatrix * vec4(aNormal, 0.0)));
    ///


    vColor = aColor;
    vTexCoords = aTexCoords;

    vec4 bindPos = uBindShapeMatrix * aPosition;

    vec4 totalLocalPos = vec4(0.0);

    mat4 jointTransform = uJointTransforms[int(aJointIndices[0])];
    vec4 posePosition = jointTransform * bindPos;
    totalLocalPos += posePosition * aWeights[0];

    jointTransform = uJointTransforms[int(aJointIndices[1])];
    posePosition = jointTransform * bindPos;
    totalLocalPos += posePosition * aWeights[1];

    jointTransform = uJointTransforms[int(aJointIndices[2])];
    posePosition = jointTransform * bindPos;
    totalLocalPos += posePosition * aWeights[2];

    vPosition = uMMatrix * totalLocalPos;

    gl_Position = uMVPMatrix * totalLocalPos;
}
