/*
MIT License

Copyright (c) 2020 The 3Deers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.model3d.jcolladaloaderlib.entities;

public class Vertex {

    private static final int NO_INDEX = -1;

    private int vertexIndex = 0;
    private float[] position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private int colorIndex = NO_INDEX;

    private VertexSkinData weightsData;

    public Vertex(int vertexIndex) {
        this.vertexIndex = vertexIndex;
    }

    @Deprecated
    public Vertex(float[] position) {
        this.position = position;
    }

    public int getVertexIndex() {
        return vertexIndex;
    }

    public VertexSkinData getWeightsData() {
        return weightsData;
    }

    public void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    public void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    public float[] getPosition() {
        return position;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public void setWeightsData(VertexSkinData weightsData) {
        this.weightsData = weightsData;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (vertexIndex != vertex.vertexIndex) return false;
        if (textureIndex != vertex.textureIndex) return false;
        return normalIndex == vertex.normalIndex;
    }

    @Override
    public int hashCode() {
        int result = vertexIndex;
        result = 31 * result + textureIndex;
        result = 31 * result + normalIndex;
        return result;
    }
}
