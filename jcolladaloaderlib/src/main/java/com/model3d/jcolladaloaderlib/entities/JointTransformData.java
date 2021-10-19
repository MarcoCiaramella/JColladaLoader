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

/**
 * This contains the data for a transformation of one joint, at a certain time
 * in an animation. It has the name of the joint that it refers to, and the
 * local transform of the joint in the pose position.
 * 
 * @author andresoviedo
 *
 */
public class JointTransformData {

	public final String jointId;

	public final float[] matrix;
	public final Float[] location;
	public final Float[] rotation;
	public final Float[] scale;

	private JointTransformData(String jointId, float[] matrix, Float[] location, Float[] rotation, Float[] scale) {
		this.jointId = jointId;
		this.matrix = matrix;
		this.location = location;
		this.rotation = rotation;
		this.scale = scale;
	}

	public static JointTransformData ofMatrix(String jointId, float[] matrix) {
		return new JointTransformData(jointId, matrix, null, null, null);
	}

	public static JointTransformData ofLocation(String jointId, Float[] location) {
		return new JointTransformData(jointId, null, location, null, null);
	}

	public static JointTransformData ofRotation(String jointId, Float[] rotation) {
		return new JointTransformData(jointId, null, null, rotation, null);
	}

	public static JointTransformData ofScale(String jointId, Float[] scale) {
		return new JointTransformData(jointId, null, null, null, scale);
	}
}
