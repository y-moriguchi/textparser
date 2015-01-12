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
 * Useful functions for intervals.
 * 区間に関する便利な関数である.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class Intervals {
	
	//
	private Intervals() {}

	/**
	 * creates a new interval with the given supremum and no infima.
	 * 与えられた上限をもち、下限を持たない区間を作成する.
	 * 
	 * @param sup    the object defines the supremum
	 * @param supOpen true is the supremum is open (exclusive)
	 * @return  the inteval with the extrema
	 */
	public static
	Interval newInfimumlessInterval(
			Object sup, boolean supOpen) {
		return new Interval(
				Limit.MINIMUM, true, sup, supOpen);
	}

	/**
	 * creates a new interval with the given infimum and no suprema.
	 * 与えられた下限をもち、上限を持たない区間を作成する.
	 * 
	 * @param inf    the object defines the infimum
	 * @param infOpen true is the infimum is open (exclusive)
	 * @return  the inteval with the extrema
	 */
	public static 
	Interval newSupremumlessInterval(
			Object inf, boolean infOpen) {
		return new Interval(
				inf, infOpen, Limit.MAXIMUM, true);
	}

	/**
	 * creates a new closed interval with the given extrema.
	 * 与えられた上限・下限の閉区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new closed inteval with the extrema
	 */
	public static 
	Interval newClosedInterval(Object inf, Object sup) {
		return Interval.newInstance(inf, false, sup, false);
	}

	/**
	 * creates a new open interval with the given extrema.
	 * 与えられた上限・下限の開区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new open inteval with the extrema
	 */
	public static 
	Interval newOpenInterval(Object inf, Object sup) {
		return Interval.newInstance(inf, true, sup, true);
	}

	/**
	 * creates a new left open interval with the given extrema.
	 * 与えられた上限・下限の左開区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new left open inteval with the extrema
	 */
	public static 
	Interval newLeftOpenInterval(Object inf, Object sup) {
		return Interval.newInstance(inf, true, sup, false);
	}

	/**
	 * creates a new right open interval with the given extrema.
	 * 与えられた上限・下限の右開区間を作成する.
	 * 
	 * @param inf  the infimum of the interval
	 * @param sup  the supremum of the interval
	 * @return  the new right open inteval with the extrema
	 */
	public static 
	Interval newRightOpenInterval(Object inf, Object sup) {
		return Interval.newInstance(inf, false, sup, true);
	}

	/**
	 * creates a new closed interval with the given supremum and
	 * no infima.
	 * 与えられた上限をもち、下限を持たない閉区間を作成する.
	 * 
	 * @param sup    the object defines the supremum
	 * @return  the closed inteval with the extrema
	 */
	public static 
	Interval newClosedInfimumlessInterval(Object sup) {
		return newInfimumlessInterval(sup, false);
	}

	/**
	 * creates a new open interval with the given supremum and no infima.
	 * 与えられた上限をもち、下限を持たない開区間を作成する.
	 * 
	 * @param sup    the object defines the supremum
	 * @return  the open inteval with the extrema
	 */
	public static 
	Interval newOpenInfimumlessInterval(Object sup) {
		return newInfimumlessInterval(sup, true);
	}

	/**
	 * creates a new closed interval with the given infimum and
	 * no suprema.
	 * 与えられた下限をもち、上限を持たない閉区間を作成する.
	 * 
	 * @param inf    the object defines the infimum
	 * @return  the closed inteval with the extrema
	 */
	public static 
	Interval newClosedSupremumlessInterval(Object inf) {
		return newSupremumlessInterval(inf, false);
	}

	/**
	 * creates a new open interval with the given infimum and no suprema.
	 * 与えられた下限をもち、上限を持たない開区間を作成する.
	 * 
	 * @param inf    the object defines the infimum
	 * @return  the open inteval with the extrema
	 */
	public static 
	Interval newOpenSupremumlessInterval(Object inf) {
		return newSupremumlessInterval(inf, true);
	}

}
