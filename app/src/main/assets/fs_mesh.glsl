#version 100

precision highp float;


uniform vec3 uViewPos;
uniform sampler2D uTexture;
uniform int uIsTextured;
uniform int uIsColored;
varying vec4 vPosition;
varying vec3 vNormal;
varying vec4 vColor;
varying vec2 vTexCoords;
const vec3 lightDirection = vec3(1.0,0.0,0.0);
const vec3 lightAmbientColor = vec3(0.2,0.2,0.2);
const vec3 lightDiffuseColor = vec3(0.7,0.7,0.7);
const vec3 lightSpecularColor = vec3(1.0,1.0,1.0);


float diffuseLighting(vec3 normal, vec3 lightDir){
    return max(dot(normal, lightDir), 0.0);
}

float specularLighting(vec3 normal, vec3 lightDir, vec3 viewDir){
    vec3 reflectDir = reflect(-lightDir, normal);
    return pow(max(dot(viewDir, reflectDir), 0.0), 2.0);
}

vec4 calcLight(vec3 normal, vec3 viewDir){
    vec3 lightDir = normalize(-lightDirection);
    float diff = diffuseLighting(normal, lightDir);
    float spec = specularLighting(normal, lightDir, viewDir);
    vec4 color;
    if (uIsTextured == 1){
        color = texture2D(uTexture,vTexCoords);
    }
    else if (uIsColored == 1){
        color = vColor;
    }
    else {
        color = vec4(1.0,1.0,1.0,1.0);
    }
    vec4 ambient = vec4(lightAmbientColor, 1.0) * color;
    vec4 diffuse = vec4(lightDiffuseColor * diff, 1.0) * color;
    vec4 specular = vec4(lightSpecularColor * spec, 1.0) * vec4(0.5,0.5,0.5,1.0);
    return ambient + (diffuse + specular);
}

void main() {
    vec3 normal = normalize(vNormal);
    vec3 viewDir = normalize(uViewPos - vec3(vPosition));
    gl_FragColor = calcLight(normal, viewDir);
}

