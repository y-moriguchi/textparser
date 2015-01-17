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

import java.util.SortedSet;

/**
 * Useful functions for intervals of int.
 * int型の区間に関する便利な関数である.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public class IntervalsInt {

	//
	private static Integer newInt(int i) {
		return new Integer(i);
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
			int inf, boolean infOpen, int sup, boolean supOpen) {
		return Interval.newInstance(
				newInt(inf), infOpen,
				newInt(sup), supOpen);
	}

	/**
	 * creates a new interval which only includes the specified
	 * point.<br>
	 * 引数のオブジェクトのみを含む区間を新規に作成する.
	 * 
	 * @param x  a point which the interval includes
	 * @return  a new interval includes only the specified point
	 */
	public static Interval newPoint(int x) {
		return new Interval(
				newInt(x), false, newInt(x), false);
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
			int sup) {
		return Intervals.newInfimumlessInterval(newInt(sup), false);
	}

	/**
	 * creates a new open interval with the given supremum and no infima.
	 * 与えられた上限をもち、下限を持たない開区間を作成する.
	 * 
	 * @param sup    the object defines the supremum
	 * @return  the open inteval with the extrema
	 */
	public static Interval newOpenInfimumlessInterval(
			int sup) {
		return Intervals.newInfimumlessInterval(newInt(sup), true);
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
			int inf) {
		return Intervals.newSupremumlessInterval(newInt(inf), false);
	}

	/**
	 * creates a new open interval with the given infimum and no suprema.
	 * 与えられた下限をもち、上限を持たない開区間を作成する.
	 * 
	 * @param inf    the object defines the infimum
	 * @return  the open inteval with the extrema
	 */
	public static Interval newOpenSupremumlessInterval(
			int inf) {
		return Intervals.newSupremumlessInterval(newInt(inf), true);
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
			int inf, int sup) {
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
			int inf, int sup) {
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
			int inf, int sup) {
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
			int inf, int sup) {
		return newInstance(inf, false, sup, true);
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public static long cardinality(Range r) {
		if(r.isEmpty()) {
			return 0;
		} else {
			SortedSet<Interval> s = r.intervals();
			int x = 0;

			for(Interval i : s) {
				long a;

				a  = (Integer)i.getSupremumBound();
				a -= (Integer)i.getInfimumBound();
				if(i.isSupremumOpen()) {
					a--;
				}
				if(i.isInfimumOpen()) {
					a--;
				}
				x += a + 1;
			}
			return x;
		}
	}

	/**
	 * 
	 * @param r
	 * @param val
	 * @return
	 */
	public static long indexOf(Range r, int val) {
		if(!r.contains(val)) {
			return -1;
		} else {
			SortedSet<Interval> s = r.intervals();
			long x = 0;

			for(Interval i : s) {
				int a;

				if(i.contains(val)) {
					a  = val;
					a -= (Integer)i.getInfimumBound();
					if(i.isInfimumOpen()) {
						a--;
					}
					return x + a;
				} else {
					a  = (Integer)i.getSupremumBound();
					a -= (Integer)i.getInfimumBound();
					if(i.isSupremumOpen()) {
						a--;
					}
					if(i.isInfimumOpen()) {
						a--;
					}
					x += a;
				}
			}
			return -1;
		}
	}

	/**
	 * 
	 * @param r
	 * @param index
	 * @return
	 */
	public static int intAt(Range r, long index) {
		SortedSet<Interval> s = r.intervals();
		long x = 0;

		if(index < 0 || index >= cardinality(r)) {
			throw new IndexOutOfBoundsException("" + index);
		}
		for(Interval i : s) {
			int a;

			a  = (Integer)i.getSupremumBound();
			a -= (Integer)i.getInfimumBound() - 1;
			if(i.isSupremumOpen()) {
				a--;
			}
			if(i.isInfimumOpen()) {
				a--;
			}
			if(index < x + a) {
				int b;
				
				b = (Integer)i.getInfimumBound();
				if(i.isInfimumOpen()) {
					b++;
				}
				return b + (int)(index - x);
			} else {
				x += a;
			}
		}
		return -1;
		
	}

}
