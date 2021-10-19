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

import java.util.ArrayList;
import java.util.List;

public final class VertexSkinData {
	
	public final List<Integer> jointIds = new ArrayList<>(3);
	public final List<Float> weights = new ArrayList<>(3);
	
	public void addJointEffect(int jointId, float weight){
		for(int i=0;i<weights.size();i++){
			if(weight > weights.get(i)){
				jointIds.add(i, jointId);
				weights.add(i, weight);
				return;
			}
		}
		jointIds.add(jointId);
		weights.add(weight);
	}
	
	public void limitJointNumber(int max){
		if(jointIds.size() > max){
			float[] topWeights = new float[max];
			float total = saveTopWeights(topWeights);
			refillWeightList(topWeights, total);
			removeExcessJointIds(max);
		}else if(jointIds.size() < max){
			fillEmptyWeights(max);
		}
	}

	private void fillEmptyWeights(int max){
		while(jointIds.size() < max){
			jointIds.add(0);
			weights.add(0f);
		}
	}
	
	private float saveTopWeights(float[] topWeightsArray){
		float total = 0;
		for(int i=0;i<topWeightsArray.length;i++){
			topWeightsArray[i] = weights.get(i);
			total += topWeightsArray[i];
		}
		return total;
	}
	
	private void refillWeightList(float[] topWeights, float total){
		weights.clear();
		for(int i=0;i<topWeights.length;i++){
			weights.add(Math.min(topWeights[i]/total, 1));
		}
	}
	
	private void removeExcessJointIds(int max){
		while(jointIds.size() > max){
			jointIds.remove(jointIds.size()-1);
		}
	}

	@Override
	public String toString() {
		return "VertexSkinData{" +
				"jointIds=" + jointIds +
				", weights=" + weights +
				'}';
	}
}
