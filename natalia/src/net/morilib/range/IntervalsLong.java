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

/**
 * Useful functions for intervals of long.
 * long型の区間に関する便利な関数である.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public class IntervalsLong {
	
	//
	private static Long newLong(long i) {
		return new Long(i);
	}

	/**
	 * creates a new interval.<br>
	 * 区間を新規に作成する.
	 * 
	 * @param inf    first object defines the edge of the interval
	 * @param infOpen true if infimum is open (exclusive)
	 * @param sup    second object defines the edge of the interval
	 * @param supOpen true is supremum is open (exclusive)
	 * @return  a new interval
	 */
	public static Interval newInstance(
			long inf, boolean infOpen, long sup, boolean supOpen) {
		return Interval.newInstance(
				newLong(inf), infOpen,
				newLong(sup), supOpen);
	}

	/**
	 * creates a new interval which only includes the specified
	 * point.<br>
	 * 引数のオブジェクトのみを含む区間を新規に作成する.
	 * 
	 * @param x  a point which the interval includes
	 * @return  a new interval includes only the specified point
	 */
	public static Interval newPoint(long x) {
		return new Interval(
				newLong(x), false, newLong(x), false);
	}

	/**
	 * creates a new closed interval with the given supremum and
	 * no infima.
	 * 与えられた上限をもち、下限を持たない閉区間を作成する.
	 * 
	 * @param sup    the object defines the supremum
	 * @return  the closed inteval with the extrema
	 */
	public static Interval newClosedInfimumlessInterval(
			long sup) {
		return Intervals.newInfimumlessInterval(newLong(sup), false);
	}

	/**
	 * creates a new open interval with the given supremum and no infima.
	 * 与えられた上限をもち、下限を持たない開区間を作成する.
	 * 
	 * @param sup    the object defines the supremum
	 * @return  the open inteval with the extrema
	 */
	public static Interval newOpenInfimumlessInterval(
			long sup) {
		return Intervals.newInfimumlessInterval(newLong(sup), true);
	}

	/**
	 * creates a new closed interval with the given infimum and
	 * no suprema.
	 * 与えられた下限をもち、上限を持たない閉区間を作成する.
	 * 
	 * @param inf    the object defines the infimum
	 * @return  the closed inteval with the extrema
	 */
	public static Interval newClosedSupremumlessInterval(
			long inf) {
		return Intervals.newSupremumlessInterval(newLong(inf), false);
	}

	/**
	 * creates a new open interval with the given infimum and no suprema.
	 * 与えられた下限をもち、上限を持たない開区間を作成する.
	 * 
	 * @param inf    the object defines the infimum
	 * @return  the open inteval with the extrema
	 */
	public static Interval newOpenSupremumlessInterval(
			long inf) {
		return Intervals.newSupremumlessInterval(newLong(inf), true);
	}

	/**
	 * creates a new closed interval with the given extrema.
	 * 与えられた上限・下限の閉区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new closed inteval with the extrema
	 */
	public static Interval newClosedInterval(
			long inf, long sup) {
		return newInstance(inf, false, sup, false);
	}

	/**
	 * creates a new open interval with the given extrema.
	 * 与えられた上限・下限の開区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new open inteval with the extrema
	 */
	public static Interval newOpenInterval(
			long inf, long sup) {
		return newInstance(inf, true, sup, true);
	}

	/**
	 * creates a new left open interval with the given extrema.
	 * 与えられた上限・下限の左開区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new left open inteval with the extrema
	 */
	public static Interval newLeftOpenInterval(
			long inf, long sup) {
		return newInstance(inf, true, sup, false);
	}

	/**
	 * creates a new right open interval with the given extrema.
	 * 与えられた上限・下限の右開区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new right open inteval with the extrema
	 */
	public static Interval newRightOpenInterval(
			long inf, long sup) {
		return newInstance(inf, false, sup, true);
	}

}
