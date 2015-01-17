/*
 * Copyright 2009 Yuichiro Moriguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.morilib.range;


public final class Limit {
	
	/**
	 * The mark means the maximum of all objects.<br>
	 * <p>「最大のオブジェクト」を示すマーカである.
	 */
	public static final Object MAXIMUM = new MarkerEnum("maximum");
	
	/**
	 * The mark means the minimum of all objects.<br>
	 * <p>「最小のオブジェクト」を示すマーカである.
	 */
	public static final Object MINIMUM = new MarkerEnum("MINIMUM");
	
	//
	private Limit() {}

	
	/**
	 * compares the given objects;
	 * returns 1 if the first object is larger than the second object,
	 * returns 0 if the first is equal to the second,
	 * and returns -1 if the first is smaller than the second.
	 * <p>引数を比較する.
	 * 第1の引数が第2の引数より大きい、等しい、小さいときそれぞれ
	 * 1, 0, -1を返す.
	 * 
	 * @see java.lang.Comparable
	 */
	@SuppressWarnings("unchecked")
	public static int compareBound(Object o, Object p) {
		if(o == null || o == MINIMUM) {
			return (p == null || p == MINIMUM) ? 0 : -1;
		} else if(o == MAXIMUM) {
			return (p == MAXIMUM) ? 0 : 1;
		} else if(p == null || p == MINIMUM) {
			return 1;
		} else if(p == MAXIMUM) {
			return -1;
		} else {
			return ((Comparable<Object>)o).compareTo(p);
		}
	}
	
}
