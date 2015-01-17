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

import java.util.Set;
import java.util.SortedSet;


/**
 * This is an abstract class which implements &quot;range&quot;,
 * a topology with sum and overlap operation, supremum and infimum.<br>
 * 
 * Range(範囲: 区間の和集合からなる集合)を表す基底クラスである.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public abstract class Range {
	
	/**
	 * The empty range.<br>
	 * 空集合である.
	 */
	public static final Interval O = new Interval(
			Limit.MAXIMUM, true, Limit.MINIMUM, true);
	
	/**
	 * The universal range of all objects.<br>
	 * 全オブジェクトを含む集合である.
	 */
	public static final Interval U = new Interval(
			Limit.MINIMUM, true, Limit.MAXIMUM, true);

	/**
	 * creates a new range with the specified objects.<br>
	 * 引数で与えられた点を含む範囲を作成する.
	 * 
	 * @param s  a collection of points
	 * @return  a new range with the objects
	 */
	public static Range newPoints(Set<?> s) {
		if(s.isEmpty()) {
			return O;
		} else {
			RangeAdder a = new RangeAdder();
			
			for(Object t : s) {
				a.add(Interval.newPoint(t));
			}
			return a.toRange();
		}
	}

	/**
	 * creates a new range with the specified objects.<br>
	 * 引数で与えられた点を含む範囲を作成する.
	 * 
	 * @param v1  a point
	 * @param v2  another point
	 * @return  a new range with the objects
	 */
	public static Range
	newPointPair(Object v1, Object v2) {
		return Ranges.sum(
				Interval.newPoint(v1), Interval.newPoint(v2));
	}

	/**
	 * Returns the boundary set of this topological set.<br>
	 * この範囲のすべての境界点を含む集合を得る.
	 */
	public abstract Range bound();
	
	/**
	 * returns true if this range is a open set.<br>
	 * この範囲が開集合であればtrueを得る.
	 * 
	 * @return  true if this range is open
	 */
	public abstract boolean isOpen();
	
	/**
	 * returns true if this range is a closed set.<br>
	 * この範囲が閉集合であればtrueを得る.
	 * 
	 * @return  true if this range is closed
	 */
	public abstract boolean isClosed();
	
	/**
	 * returns the bounds of this range by SortedSet.<br>
	 * この範囲の境界点のSortedSetを得る. 
	 * 
	 * @return  the SortedSet which includes all bounds of this range
	 */
	public abstract SortedSet<?> boundElements();

	/**
	 * returns true if the specified object is below the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点より小さいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundAbove(Object o);

	/**
	 * returns true if the specified object is above the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点より大きいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundBelow(Object o);

	/**
	 * returns true if the specified object is equal to the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点に等しいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundEqualTo(Object o);

	/**
	 * returns true if the specified object is below the supremum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点より小さいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundAbove(Object o);

	/**
	 * returns true if the specified object is above the supremum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点より大きいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundBelow(Object o);

	/**
	 * returns true if the specified object is equal to the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点に等しいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundEqualTo(Object o);
	
	/**
	 * returns true if any points of the specified range is below
	 * the infimum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundAboveAnyClosureOf(
			Range r);
	
	/**
	 * returns true if any points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundBelowAnyClosureOf(
			Range r);

	/**
	 * returns true if any points of the specified range is below
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundAboveAnyClosureOf(
			Range r);

	/**
	 * returns true if any points of the specified range is above
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundBelowAnyClosureOf(
			Range r);
	
	/**
	 * returns true if all points of the specified range is below
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundAboveAllClosureOf(
			Range r);
	
	/**
	 * returns true if all points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the infimum
	 */
	/*package*/ abstract boolean isInfimumBoundBelowAllClosureOf(
			Range r);

	/**
	 * returns true if all points of the specified range is below
	 * the supremum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundAboveAllClosureOf(
			Range r);

	/**
	 * returns true if all points of the specified range is above
	 * the supremum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the supremum
	 */
	/*package*/ abstract boolean isSupremumBoundBelowAllClosureOf(
			Range r);
	
	/**
	 * returns true if the infimum bound of this range contacts
	 * the supremum bound of the specified range.<br>
	 * この範囲の下限の境界点が与えられた範囲の上限の境界点と一致する
	 * (接触する)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum contacts the supremum the range
	 */
	/*package*/ abstract boolean contactInfimumBound(Range r);

	/**
	 * returns true if the supremum bound of this range contacts
	 * the infimum bound of the specified range.<br>
	 * この範囲の上限の境界点が与えられた範囲の下限の境界点と一致する
	 * (接触する)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum contacts the infimum the range
	 */
	/*package*/ abstract boolean contactSupremumBound(Range r);
	
	/**
	 * returns true if the infimum bound of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の下限の境界点が与えられた範囲の下限の境界点と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is the same as one
	 */
	/*package*/ abstract boolean commonInfimumBoundTo(Range r);

	/**
	 * returns true if the supremum bound of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の上限の境界点が与えられた範囲の上限の境界点と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is the same as one
	 */
	/*package*/ abstract boolean commonSupremumBoundTo(Range r);
	
	/**
	 * returns true if the specified object is below the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限より小さいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the infimum
	 */
	/*package*/ abstract boolean isInfimumAbove(Object o);
	
	/**
	 * returns true if the specified object is above the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限より大きいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the infimum
	 */
	/*package*/ abstract boolean isInfimumBelow(Object o);
	
	/**
	 * returns true if the specified object is equal to the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限に等しいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the infimum
	 */
	/*package*/ abstract boolean isInfimumEqualTo(Object o);
	
	/**
	 * returns true if the specified object is below the supremum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限より小さいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the supremum
	 */
	/*package*/ abstract boolean isSupremumAbove(Object o);
	
	/**
	 * returns true if the specified object is above the supremum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限より大きいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the supremum
	 */
	/*package*/ abstract boolean isSupremumBelow(Object o);
	
	/**
	 * returns true if the specified object is equal to the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限に等しいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the supremum
	 */
	/*package*/ abstract boolean isSupremumEqualTo(Object o);
	
	/**
	 * returns true if the infimum bound of this range is contained
	 * in the specified range.<br>
	 * 与えられた範囲がこの範囲の下限の境界点を含むときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is contained in the range 
	 */
	public abstract boolean infimumBoundIn(Range r);
	
	/**
	 * returns true if the supremum bound of this range is contained
	 * in the specified range.<br>
	 * 与えられた範囲がこの範囲の上限の境界点を含むときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is contained in the range 
	 */
	public abstract boolean supremumBoundIn(Range r);
	
	/**
	 * Returns the intervals which consists this range.<br>
	 * この範囲を構成する全ての区間を得る.
	 * 
	 * @return  the intervals which consists this range
	 */
	public abstract SortedSet<Interval> intervals();
	
	/**
	 * returns the closure of this range.<br>
	 * この範囲の閉包を返す.
	 * 
	 * @return  the closure
	 */
	public abstract Range closure();
	
	/**
	 * returns the interior of this range.<br>
	 * この範囲の内部を返す.
	 * 
	 * @return  the interior
	 */
	public abstract Range interior();
	
	/**
	 * returns true if the smallest edge of this range is open.
	 * <p>この範囲の小さい側の端点が開いているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isInfimumClosed()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the smallest edge of this range is open
	 */
	public abstract boolean isInfimumOpen();
	
	/**
	 * returns true if the smallest edge of this range is closed.
	 * <p>この範囲の小さい側の端点が閉じているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isInfimumOpen()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the smallest edge of this range is closed
	 */
	public abstract boolean isInfimumClosed();
	
	/**
	 * return true if the smallest edge of this range is finite.
	 * <p>この範囲の小さい側の端点が有限のときにtrueを得る.
	 * 
	 * @return  true if the smallest edge of this range is finite
	 */
	public abstract boolean isInfimumFinite();
	
	/**
	 * returns true if the largest edge of this range is open.
	 * <p>この範囲の大きい側の端点が開いているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isSupremumClosed()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the largest edge of this range is open
	 */
	public abstract boolean isSupremumOpen();
	
	/**
	 * returns true if the largest edge of this range is closed.
	 * <p>この範囲の大きい側の端点が閉じているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isSupremumOpen()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the largest edge of this range is closed
	 */
	public abstract boolean isSupremumClosed();
	
	/**
	 * return true if the largest edge of this range is finite.
	 * <p>この範囲の大きい側の端点が有限のときにtrueを得る.
	 * 
	 * @return  true if the largest edge of this range is finite
	 */
	public abstract boolean isSupremumFinite();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public abstract int hashCode();

	/**
	 * returns true if all points of the specified range is below
	 * the infimum of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the infimum
	 */
	/*package*/ boolean isInfimumAboveAllOf(Range r) {
		return (isInfimumBoundAboveAllClosureOf(r) ||
				(contactInfimumBound(r) &&
						(r.isSupremumOpen() || isInfimumOpen())));
	}

	/**
	 * returns true if all points of the specified range is above
	 * the infimum of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the infimum
	 */
	/*package*/ boolean isInfimumBelowAllOf(Range r) {
		return (isInfimumBoundBelowAllClosureOf(r) ||
				(commonInfimumBoundTo(r) && r.isInfimumOpen()));
	}

	/**
	 * returns true if any points of the specified range is below
	 * the infimum of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the infimum
	 */
	/*package*/ boolean isInfimumAboveAnyOf(Range r) {
		return (isInfimumFinite() &&
				(isInfimumBoundAboveAnyClosureOf(r) ||
						(commonInfimumBoundTo(r) &&
								(r.isInfimumClosed() &&
										isInfimumOpen()))));
	}

	/**
	 * returns true if any points of the specified range is above
	 * the infimum of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the infimum
	 */
	/*package*/ boolean isInfimumBelowAnyOf(Range r) {
		return isInfimumBoundBelowAnyClosureOf(r);
	}

	/**
	 * returns true if the infimum of this range contacts
	 * the supremum of the specified range.<br>
	 * この範囲の下限が与えられた範囲の上限と一致する(接触する)ときtrueを
	 * 得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum contacts the supremum the range
	 */
	/*package*/ boolean contactInfimum(Range r) {
		return (contactInfimumBound(r) &&
				(r.isSupremumClosed() && isInfimumClosed()));
	}

	/**
	 * returns true if the infimum of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の下限が与えられた範囲の下限と一致する(共通である)とき
	 * trueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is the same as one
	 */
	/*package*/ boolean commonInfimumTo(Range r) {
		return (commonInfimumBoundTo(r) &&
				(r.isInfimumClosed() && isInfimumClosed()));
	}

	/**
	 * returns true if all points of the specified range is below
	 * the supremum of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the supremum
	 */
	/*package*/ boolean isSupremumAboveAllOf(Range r) {
		return (isSupremumBoundAboveAllClosureOf(r) ||
				(commonSupremumBoundTo(r) && r.isSupremumOpen()));
	}

	/**
	 * returns true if all points of the specified range is above
	 * the supremum of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the supremum
	 */
	/*package*/ boolean isSupremumBelowAllOf(Range r) {
		return (isSupremumBoundBelowAllClosureOf(r) ||
				(contactSupremumBound(r) &&
						(r.isInfimumOpen() || isSupremumOpen())));
	}

	/**
	 * returns true if any points of the specified range is below
	 * the infimum of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the infimum
	 */
	/*package*/ boolean isSupremumAboveAnyOf(Range r) {
		return isSupremumBoundAboveAnyClosureOf(r);
	}

	/**
	 * returns true if any points of the specified range is above
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the supremum
	 */
	/*package*/ boolean isSupremumBelowAnyOf(Range r) {
		return (isSupremumFinite() &&
				(isSupremumBoundBelowAnyClosureOf(r) ||
						(commonSupremumBoundTo(r) &&
								(r.isSupremumClosed() &&
										isSupremumOpen()))));
	}

	/**
	 * returns true if the supremum bound of this range contacts
	 * the infimum of the specified range.<br>
	 * この範囲の上限が与えられた範囲の下限と一致する(接触する)ときtrueを
	 * 得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum contacts the infimum the range
	 */
	/*package*/ boolean contactSupremum(Range r) {
		return (contactSupremumBound(r) &&
				(r.isSupremumClosed() && isInfimumClosed()));
	}

	/**
	 * returns true if the supremum of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の上限が与えられた範囲の上限と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is the same as one
	 */
	/*package*/ boolean commonSupremumTo(Range r) {
		return (commonSupremumBoundTo(r) &&
				(r.isSupremumClosed() && isSupremumClosed()));
	}

	/**
	 * gets the relative complementary of this set in the specified
	 * set.<br>
	 * この集合の与えられた集合の差集合を得る.
	 * 
	 * @param r  the universal interval
	 * @return  the complementary interval
	 */
	public Range complement(Range r) {
		if(isEmpty() || r.isEmpty()) {
			return this;
		} else {
			RangeAdder bld  = new RangeAdder();
			
			for(Interval o : r.intervals()) {
				Interval.limit(o, this, bld);
			}
			return bld.toRange();
		}
	}

	/**
	 * returns the interval which includes the smallest edge of this
	 * object.
	 * <p>このオブジェクトの小さい側の端点を含む区間を得る.
	 */
	public Range getInfimumRange() {
		Range res = null;
		
		for(Range t : intervals()) {
			if(res == null ||
					res.isInfimumBoundAboveAnyClosureOf(t)) {
				res = t;
			}
		}
		return res;
	}

	/**
	 * returns the interval which includes the largest edge of this
	 * object.
	 * <p>このオブジェクトの大きい側の端点を含む区間を得る.
	 */
	public Range getSupremumRange() {
		Range res = null;
		
		for(Range t : intervals()) {
			if(res == null ||
					res.isSupremumBoundBelowAnyClosureOf(t)) {
				res = t;
			}
		}
		return res;
	}

	/**
	 * gets the join of this and the specified Range.<br>
	 * <p>この範囲と引数の範囲の和集合(join)を得る.
	 * 
	 * @param t  the Range
	 * @return  the join of this and the argument
	 */
	public Range join(Range t) {
		Range r = t;
		
		if(r.isEmpty()) {
			return this;
		} else if(isEmpty()) {
			return r;
		} else {
			MergedRange u = new MergedRange(this);
			
			u.addAll(t.intervals());
			return u;
		}
	}

	/**
	 * gets the meet of this and the specified Range.<br>
	 * <p>この範囲と引数の範囲の共通部分(meet)を得る.
	 * 
	 * @param t  the Range
	 * @return  the meet of this and the argument
	 */
	public Range meet(Range t) {
		Range r = t;
		
		if(isEmpty() || r.isEmpty()) {
			return O;
		} else {
			RangeAdder bld  = new RangeAdder();
			
			for(Interval o : r.intervals()) {
				Interval.meet(o, this, bld);
			}
			return bld.toRange();
		}
	}

	/**
	 * Returns true if the closure of this set is independent of
	 * the specified set.<br>
	 * この集合の閉包が与えられた集合と独立しているときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is independent of the topology t
	 * @see net.morilib.range.Topology#closureIndependentOf(net.morilib.range.Topology)
	 */
	public boolean closureIndependentOf(Range t) {
		return independentOf(t) && boundsIndependentOf(t);
	}

	/**
	 * returns true if the set contains some points of
	 * the specified set.<br>
	 * この集合が与えられた集合の点のどれかを含むときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if this contains some points of the specified set
	 * @see net.morilib.range.Topology#containsAny(net.morilib.range.Topology)
	 */
	public boolean containsAny(Range t) {
		return !independentOf(t);
	}

	/**
	 * Returns true if this set contains the given object.<br>
	 * この集合が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is contained in this
	 */
	public abstract boolean contains(Object o);
	
	/**
	 * Returns true if the given object is neighborhood of
	 * this set.<br>
	 * この集合の近傍、つまり与えられたオブジェクトをこの集合の開集合に
	 * 含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is neighborhood of this
	 */
	public abstract boolean isNeighborhoodOf(Object o);
	
	/**
	 * Returns true if the closure of this set contains the given
	 * object.<br>
	 * この集合の閉包が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is neighborhood of this
	 */
	public abstract boolean containsClosure(Object o);
	
	/**
	 * Returns true if this set contains the given object.<br>
	 * この集合の境界が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is contained in this
	 */
	public abstract boolean containsBound(Object o);
	
	/**
	 * Returns true if this is empty.<br>
	 * この集合が空集合のときtrueを得る.
	 * 
	 * @return  true if this is empty
	 */
	public abstract boolean isEmpty();
	
	/**
	 * Returns true if this set is in the specified set.<br>
	 * この集合が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 */
	public abstract boolean in(Range t);
	
	/**
	 * Returns true if this set is independent of
	 * the specified set.<br>
	 * この集合が与えられた集合と独立しているときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is independent of the topology t
	 */
	public abstract boolean independentOf(Range t);
	
	/**
	 * Returns true if the interior of this set is independent of
	 * the specified set.<br>
	 * この集合の内部が与えられた集合と独立しているときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is independent of the topology t
	 */
	public abstract boolean interiorIndependentOf(Range t);
	
	/**
	 * Returns true if the closure of this set is in
	 * the specified set.<br>
	 * この集合の閉包が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 */
	public abstract boolean closureContains(Range t);
	
	/**
	 * Returns true if the interior of this set is in
	 * the specified set.<br>
	 * この集合の内部が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 */
	public abstract boolean interiorContains(Range t);
	
	/**
	 * returns true if the set contains all points of
	 * the specified set.<br>
	 * この集合が与えられた集合の点を全て含む(与えられた集合がこの集合の
	 * 部分集合である)ときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if this contains all points of the specified set
	 */
	public abstract boolean containsAll(Range t);
	
	/**
	 * returns true if the bound of this set is a subset of the
	 * specified set.<br>
	 * この集合の境界が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if the bound of this set is a subset
	 */
	public abstract boolean boundsIn(Range t);
	
	/**
	 * returns true if the bound of this set is independent of
	 * the specified set.<br>
	 * この集合の境界が与えられた集合と独立であるときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if the bound of this set is independent
	 */
	public abstract boolean boundsIndependentOf(Range t);

	/**
	 * returns true if this set is finite.<br>
	 * この集合が有限のときtrueを得る.
	 * 
	 * @return true if this set is finite
	 */
	public abstract boolean isFinite();

	/**
	 * returns the Java collection set if this set is finite.<br>
	 * if this set has infinite cardinality,
	 * this method returns null.<br>
	 * この集合が有限のときJavaコレクションのSetを返す.<br>
	 * 集合が無限の濃度を持つときはnullを返す.
	 * 
	 * @return java.util.Set if this set is finite
	 */
	public abstract SortedSet<?> getJavaSetIfFinite();

	/**
	 * returns true if this set is universe.<br>
	 * この集合が全体を表すときtrueを返す.
	 * 
	 * @return true if this set is universe
	 */
	public boolean isUniverse() {
		return !isSupremumFinite() && !isInfimumFinite();
	}

	/**
	 * returns true if this object equals to the given object.
	 * <p>このオブジェクトが与えられたオブジェクトと等しいときtrueを得る.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof Range) {
			//return containsAll((Range)o) && in((Range)o);
			return in((Range)o) && ((Range)o).in(this);
		}
		return false;
	}

}
