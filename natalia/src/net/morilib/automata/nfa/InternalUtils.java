/*
 * Copyright 2015 Yuichiro Moriguchi
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
package net.morilib.automata.nfa;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
final class InternalUtils {

	private InternalUtils() {}

	/**
	 * Returns true if the given objects are equal.<br>
	 * <p>与えられた引数が等しいときにtrueを得る.
	 * 
	 * @return  true if obj1 equals obj2, otherwise false
	 */
	public static /*inline*/ boolean equals(Object obj1, Object obj2) {
		return (obj1 == null) ? obj2 == null : obj1.equals(obj2);
	}

}
