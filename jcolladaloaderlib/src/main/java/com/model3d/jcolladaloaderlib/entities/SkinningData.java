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

import java.util.List;

public class SkinningData {

	private final String id;
    private final float[] bindShapeMatrix;
	public final List<String> jointOrder;
	public final List<VertexSkinData> verticesSkinData;
	private final float[] inverseBindMatrix;

	/**
	 *  @param skinId
	 * @param bindShapeMatrix bind_shape_matrix or IDENTITY_MATRIX
	 * @param jointOrder
	 * @param verticesSkinData
	 * @param inverseBindMatrix optional value
	 */
	public SkinningData(String skinId, float[] bindShapeMatrix, List<String> jointOrder, List<VertexSkinData> verticesSkinData, float[] inverseBindMatrix){
		this.id = skinId;
	    this.bindShapeMatrix = bindShapeMatrix;
		this.jointOrder = jointOrder;
		this.verticesSkinData = verticesSkinData;
		this.inverseBindMatrix = inverseBindMatrix;
	}


	public float[] getBindShapeMatrix() {
		return bindShapeMatrix;
	}

	public float[] getInverseBindMatrix() {
		return inverseBindMatrix;
	}

	public String getId() {
		return this.id;
	}
}
