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
 * An interface represents section.
 * <p>「区間」を抽象化したインターフェースです。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2010/04/18
 */
public interface Section<T> {
	
	/**
	 * returns 1 if the given object is more
	 * than the maximum of this section,
	 * returns -1 if the object is less
	 * than the minimum of this section,
	 * returns 0 if the object is in this section.
	 * <p>与えられたオブジェクトがこの区間の最大より大きいときに1を、
	 * 最小より小さいときに-1を、この区間内にあるときに0を返します。
	 * 
	 * @param o value to be tested
	 */
	public int side(T o);
	
}
