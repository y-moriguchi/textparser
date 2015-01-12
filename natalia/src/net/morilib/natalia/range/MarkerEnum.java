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
package net.morilib.natalia.range;

/**
 * This is the class of the marker objects.
 * <p>「マーカオブジェクト」を示すクラスである.
 * 
 * @author MORIGUCHI, Yuichiro 2005/05/15
 */
/*package*/ class MarkerEnum {
	
	//
	private String explain;
	
	/**
	 * creates a marker.
	 * <p>マーカオブジェクトを生成する.
	 * 
	 * @param explain  a descprition
	 */
	public MarkerEnum(String explain) {
		this.explain = explain;
	}
	
	/**
	 * gets the descprition of this instance.
	 * <p>マーカの説明を得る.
	 */
	public String toString() {
		return explain;
	}

}
