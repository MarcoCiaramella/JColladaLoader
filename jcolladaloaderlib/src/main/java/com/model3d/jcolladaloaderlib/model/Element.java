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

package com.model3d.jcolladaloaderlib.model;

import com.model3d.jcolladaloaderlib.util.io.IOUtils;

import java.nio.IntBuffer;
import java.util.List;

public class Element {

    public static class Builder {

        // polygon
        private String id;
        private List<Integer> indices;

        // materials
        private String materialId;

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public String getId() {
            return this.id;
        }


        public Builder indices(List<Integer> indices) {
            this.indices = indices;
            return this;
        }

        public Builder materialId(String materialId) {
            this.materialId = materialId;
            return this;
        }

        public String getMaterialId() {
            return this.materialId;
        }

        public Element build() {
            return new Element(id, indices, materialId);
        }



    }

    // polygon
    private final String id;
    private final List<Integer> indicesArray;
    private IntBuffer indexBuffer;

    // material
    private String materialId;
    private Material material;

    public Element(String id, List<Integer> indexBuffer, String material) {
        this.id = id;
        this.indicesArray = indexBuffer;
        this.materialId = material;
    }

    public Element(String id, IntBuffer indexBuffer, String material) {
        this.id = id;
        this.indicesArray = null;
        this.indexBuffer = indexBuffer;
        this.materialId = material;
    }

    public String getId() {
        return this.id;
    }

    public List<Integer> getIndices() {
        return this.indicesArray;
    }


    public IntBuffer getIndexBuffer() {
        if (indexBuffer == null) {
            this.indexBuffer = IOUtils.createIntBuffer(indicesArray.size());
            this.indexBuffer.position(0);
            for (int i = 0; i < indicesArray.size(); i++) {
                this.indexBuffer.put(indicesArray.get(i));
            }
        }
        return indexBuffer;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return "Element{" +
                "id='" + id + '\'' +
                ", indices="+(indicesArray != null? indicesArray.size(): null)+
                ", indexBuffer="+indexBuffer+
                ", material=" + material +
                '}';
    }
}
