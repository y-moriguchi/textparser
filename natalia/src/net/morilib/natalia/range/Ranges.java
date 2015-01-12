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
 * Useful methods and constants for the Range library.
 * <p>Rangeライブラリのための有用なメソッドと定数である.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class Ranges {
	
	//
	private Ranges() {}
	
	//
	static int compareInfimum(
			Object o1, boolean b1, Object o2, boolean b2) {
		int r = Limit.compareBound(o1, o2);
		
		return (r != 0) ? r : (
				(b1 && !b2) ? 1 : ((!b1 && b2) ? -1 : 0));
	}
	
	//
	static int compareSupremum(
			Object o1, boolean b1, Object o2, boolean b2) {
		int r = Limit.compareBound(o1, o2);
		
		return (r != 0) ? r : (
				(b1 && !b2) ? -1 : ((!b1 && b2) ? 1 : 0));
	}
	
	//
	static
	boolean inCoveredLowerHalfPlane(Range src, Range d) {
		if(d.isSupremumClosed() || src.isSupremumOpen()) {
			return !src.isSupremumBoundAboveAllClosureOf(d);
		} else {
			return src.isSupremumBoundBelowAllClosureOf(d);
		}
	}
	
	//
	static
	boolean inCoveredUpperHalfPlane(Range src, Range d) {
		if(d.isInfimumClosed() || src.isInfimumOpen()) {
			return !src.isInfimumBoundBelowAllClosureOf(d);
		} else {
			return d.isInfimumBoundAboveAllClosureOf(src);
		}
	}

	/**
	 * calculates the sum set of the given {@link Range} objects.
	 * <p>与えられた{@link Range}の和集合を計算する.
	 */
	public static
	Range sum(Range r1, Range r2) {
		return r1.join(r2);
	}
	
	/**
	 * Returns the overlap of the given ranges.
	 * <p>与えられた{@link Range}の共通部分を計算する.
	 */
	public static
	Range overlap(Range r1, Range r2) {
		return r1.meet(r2);
	}
	
	/**
	 * returns true if the given object is in the interval between
	 * the both edges of {@link Range}.
	 * <p>与えられたオブジェクトが{@link Range}オブジェクトの端点で構成
	 * される区間にあるときtrueを得る.
	 * 
	 * @param t  a {@link Range}
	 * @param o  an object to be tested
	 */
	public static
	boolean inBounds(Range t, Object o) {
		return !t.isInfimumAbove(o) && !t.isSupremumBelow(o);
	}

}
