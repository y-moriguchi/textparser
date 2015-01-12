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

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents an interval with two comparable objects.<br>
 * 2つの比較可能(Comparable)なオブジェクトによる区間を表すクラスである.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class Interval extends Range
implements Comparable<Interval>, Section<Object> {
	
	//
	/*package*/ static class InfimumMarker extends MarkerEnum {

		//
		private InfimumMarker(String explain) {
			super(explain);
		}
		
	};

	//
	/*package*/ static class SupremumMarker extends MarkerEnum {

		//
		private SupremumMarker(String explain) {
			super(explain);
		}
		
	};

	/**
	 * This marker represents &quot;to the supremum of
	 * this interval&quot;.<br>
	 * 「この区間の上限まで」を表すマーカである.
	 */
	public static final SupremumMarker TO_SUPREMUM =
		new SupremumMarker("to supremum");
	
	/**
	 * This marker represents &quot;below the infimum of
	 * this interval&quot;.<br>
	 * 「この区間の下限より下」を表すマーカである.
	 */
	public static final SupremumMarker BELOW_INFIMUM = 
		new SupremumMarker("below infimum");
	
	/**
	 * This marker represents &quot;from the supremum of
	 * this interval&quot;.<br>
	 * 「この区間の下限から」を表すマーカである.
	 */
	public static final InfimumMarker FROM_INFIMUM = 
		new InfimumMarker("from infimum");

	/**
	 * This marker represents &quot;above the supremum of
	 * this interval&quot;.<br>
	 * 「この区間の上限より上」を表すマーカである.
	 */
	public static final InfimumMarker ABOVE_SUPREMUM =  
		new InfimumMarker("above supremum");
	
	//
	private Object   sup, inf;
	private int left, right;
	
	//
	/*package*/ Interval(Object inf, Object sup, int l, int r) {
		if(Limit.compareBound(inf, sup) > 0) {
			throw new IllegalArgumentException();
		}
		this.inf = inf;
		this.sup = sup;
		this.left  = l;
		this.right = r;
	}
	
	//
	/*package*/ Interval(
			Object inf, boolean infOpen, Object sup, boolean supOpen) {
		this.inf   = inf;
		this.sup   = sup;
		this.left  = (infOpen ?  1 : 0);
		this.right = (supOpen ? -1 : 0);
	}
	
	//
	/*package*/ int compareInfimum(Object o, boolean open) {
		return compareInfimum(this, o, open);
	}
	
	//
	/*package*/ int compareSupremum(Object o, boolean open) {
		return compareInfimum(o, open, this);
	}

	/**
	 * 与えられたIntervalとRangeの共通部分を得る.
	 * 
	 * @param i   the interval
	 * @param r   the range
	 * @param bld the RangeAdder
	 */
	/*package*/ static void meet(
			Interval i, Range r, RangeAdder bld) {
		for(Interval t : r.intervals()) {
			bld.addInterval(i.meetInterval(t));
		}
	}
	
	/**
	 * 与えられたIntervalのRangeによる補集合を得る.
	 * 
	 * @param i   the interval
	 * @param r   the range
	 * @param bld the RangeAdder
	 */
	/*package*/ static void limit(
			Interval i, Range r, RangeAdder bld) {
		for(Interval t : r.intervals()) {
			bld.addAll(i.complementIntervals(t));
		}
	}
	
	//
	/*package*/ static
	int compareInfimum(
			Interval b1, Object o2, boolean b2) {
		int r = Limit.compareBound(b1.getInfimumBound(), o2);
		
		return (r != 0) ? r : (
				(b1.isInfimumOpen() && !b2) ? 1 : (
						(b1.isInfimumClosed() && b2) ? -1 : 0));
	}
	
	//
	/*package*/ static
	int compareInfimum(
			Object o1, boolean b1, Interval b2) {
		int r = Limit.compareBound(o1, b2.getSupremumBound());
		
		return (r != 0) ? r : (
				(b1 && b2.isSupremumClosed()) ? 1 : (
						(!b1 && b2.isSupremumOpen()) ? -1 : 0));
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
	public static
	Interval newInstance(
			Object inf, boolean infOpen, Object sup, boolean supOpen) {
		if(inf == null || sup == null) {
			throw new NullPointerException();
		} else {
			int c = Limit.compareBound(inf, sup);
			
			if(c > 0 || (c == 0 && (infOpen || supOpen))) {
				throw new IllegalArgumentException(inf + ">" + sup);
			}
		}
		return new Interval(inf, infOpen, sup, supOpen);
	}
	
	/**
	 * creates a new interval.<br>
	 * 区間を新規に作成する.
	 * 
	 * @param dinf    first object defines the edge of the interval
	 * @param dsup    second object defines the edge of the interval
	 * @return  a new interval
	 */
	public static
	Interval newInstance(Interval dinf, Interval dsup) {
		return newInstance(
				dinf.getInfimumBound(),  dinf.isInfimumOpen(),
				dsup.getSupremumBound(), dsup.isSupremumOpen());
	}
	
	/**
	 * creates a new interval which only includes the specified
	 * point.<br>
	 * 引数のオブジェクトのみを含む区間を新規に作成する.
	 * 
	 * @param o  a point which the interval includes
	 * @return  a new interval includes only the specified point
	 */
	public static
	Interval newPoint(Object o) {
		return new Interval(o, false, o, false);
	}
	
	/**
	 * returns true if the specified intervals are independent
	 * each other.<br>
	 * 2つの区間が互いに独立であればtrueを返す。
	 * 
	 * @param src  an interval to test
	 * @param dst  another interval to test
	 * @return  true if the two intervals are independent
	 */
	public static
	boolean independentOf(Interval src, Interval dst) {
		return (src.isInfimumAboveAllOf(dst) ||
				src.isSupremumBelowAllOf(dst));
	}
	
	/**
	 * returns true if interior of the specified intervals are
	 * independent each other.<br>
	 * 2つの区間の内部が互いに独立であればtrueを返す。
	 * 
	 * @param src  an interval to test
	 * @param dst  another interval to test
	 * @return  true if the interiors are independent
	 */
	public static
	boolean interiorIndependentOf(
			Interval src, Interval dst) {
		return (!src.isInfimumBelowAnyOf(dst) ||
				!src.isSupremumAboveAnyOf(dst));
	}
	
	/**
	 * compares the infimums of the given intervals each other.<br>
	 * 2つの区間の下限を比較する. 順序は次のように定義される:<br>
	 * <ol>
	 * <li>空集合はその他の順序より低い順序を持つ.</li>
	 * <li>有限でない下限は空でない集合でもっとも低い順序である.</li>
	 * <li>有限の下限を持つときは下限の順序にて順序を決める.</li>
	 * </ol>
	 * 
	 * @param t  an interval to be compared
	 * @param i  another interval to be compared
	 * @return   &lt;0, 0, &gt;0 as the first interval is less than,
	 *  equal to, more than the second interval
	 */
	public static
	int compareInfimum(Interval t, Interval i) {
		if(t.isEmpty()) {                  // t is empty set
			return i.isEmpty() ? 0 : -1;
		} else if(i.isEmpty()) {           // i is empty set
			return 1;
		} else if(!t.isInfimumFinite()) {  // infimum of t is infinite
			return i.isInfimumFinite() ? -1 : 0;
		} else if(!i.isInfimumFinite()) {  // infimum of i is infinite
			return 1;
		} else {
			int c = (t.isInfimumBoundAboveAnyClosureOf (i) ?  1 : (
					 t.isInfimumBoundBelowAllClosureOf (i) ? -1 : 0));
			boolean c3 = t.isInfimumOpen()    && i.isInfimumClosed();
			boolean c4 = t.isInfimumClosed()  && i.isInfimumOpen();
			
			return (c != 0) ? c : (c3 ? 1 : (c4 ? -1 : 0));
		}
	}
	
	/**
	 * compares the supremums of the given intervals each other.<br>
	 * 2つの区間の上限を比較する. 順序は次のように定義される:<br>
	 * <ol>
	 * <li>空集合はその他の順序より低い順序を持つ.</li>
	 * <li>有限でない上限は空でない集合でもっとも高い順序である.</li>
	 * <li>有限の上限を持つときは上限の順序にて順序を決める.</li>
	 * </ol>
	 * 
	 * @param t  an interval to be compared
	 * @param i  another interval to be compared
	 * @return   &lt;0, 0, &gt;0 as the first interval is less than,
	 *  equal to, more than the second interval
	 */
	public static
	int compareSupremum(Interval t, Interval i) {
		if(t.isEmpty()) {                   // t is empty set
			return i.isEmpty() ? 0 : -1;
		} else if(i.isEmpty()) {            // i is empty set
			return 1;
		} else if(!t.isSupremumFinite()) {  // supremum of t is infinite
			return i.isSupremumFinite() ? 1 : 0;
		} else if(!i.isSupremumFinite()) {  // supermum of i is infinite
			return -1;
		} else {
			int d = (t.isSupremumBoundAboveAllClosureOf(i) ?  1 : (
					 t.isSupremumBoundBelowAnyClosureOf(i) ? -1 : 0));
			boolean d3 = t.isSupremumOpen()   && i.isSupremumClosed();
			boolean d4 = t.isSupremumClosed() && i.isSupremumOpen();
			
			return (d != 0) ? d : (d3 ? -1 : (d4 ? 1 : 0));
		}
	}
	
	/**
	 * compares the supremums of the given intervals each other.<br>
	 * 2つの区間を比較する. 順序は次のように定義される:<br>
	 * <ol>
	 * <li>空集合はその他の順序より低い順序を持つ.</li>
	 * <li>有限でない下限は空でない集合でもっとも低い順序である.</li>
	 * <li>有限の下限を持つときは下限の順序にて順序を決める.</li>
	 * <li>下限の順序が等しいときは.上限の順序にて決める.</li>
	 * <li>有限でない上限は空でない集合でもっとも高い順序である.</li>
	 * <li>有限の上限を持つときは上限の順序にて順序を決める.</li>
	 * </ol>
	 * 
	 * @param t  an interval to be compared
	 * @param i  another interval to be compared
	 * @return   &lt;0, 0, &gt;0 as the first interval is less than,
	 *  equal to, more than the second interval
	 */
	public static
	int compare(Interval t, Interval i) {
		int inf = compareInfimum(t, i);
		return (inf != 0) ? inf : compareSupremum(t, i);
	}
	
	/**
	 * creates a new interval.<br>
	 * この区間を複製する.
	 * 
	 * @param inf    first object defines the edge of the interval
	 * @param infOpen true if infimum is open (exclusive)
	 * @param sup    second object defines the edge of the interval
	 * @param supOpen true is supremum is open (exclusive)
	 * @return  a new interval
	 */
	public Interval cloneInterval(
			Object inf, boolean infOpen, Object sup, boolean supOpen) {
		return newInstance(inf, infOpen, sup, supOpen);
	}

	/**
	 * duplicates this interval.<br>
	 * この区間を複製する.
	 * 
	 * @return a clone of this interval
	 */
	public Interval cloneInterval() {
		return cloneInterval(
				getInfimumBound(),  isInfimumOpen(),
				getSupremumBound(), isSupremumOpen());
	}

	/**
	 * creates a new interval from the point indicated by infimum
	 * markers to the supremum of the specified interval.<br>
	 * InfimumMarkerで指定された点を下限、引数で与えられた区間の上限を
	 * 上限とする新しい区間を生成する.
	 * 
	 * @param dinf  infimum marker
	 * @param dsup  object defines the edge of the interval
	 * @return  a new interval
	 */
	public Interval cloneInterval(
			InfimumMarker dinf, Interval dsup) {
		Object       rinf, rsup;
		boolean iOpn, sOpn;
		
		// infimum
		if(dinf == FROM_INFIMUM) {
			rinf = getInfimumBound();
			iOpn = isInfimumOpen();
		} else if(dinf == ABOVE_SUPREMUM) {
			rinf = getSupremumBound();
			iOpn = !isSupremumOpen();
		} else {
			throw new IllegalArgumentException(dinf.toString());
		}
		
		// supremum
		rsup = dsup.getSupremumBound();
		sOpn = dsup.isSupremumOpen();
		return cloneInterval(rinf, iOpn, rsup, sOpn);
	}

	/**
	 * creates a new interval from the infimum of the specified interval
	 * to the point indicated by supremum markers.<br>
	 * SupremumMarkerで指定された点を上限、引数で与えられた区間の下限を
	 * 下限とする新しい区間を生成する.
	 * 
	 * @param dinf  object defines the edge of the interval
	 * @param dsup  the supremum marker
	 * @return  a new interval
	 */
	public Interval cloneInterval(
			Interval dinf, SupremumMarker dsup) {
		Object       rinf, rsup;
		boolean iOpn, sOpn;
		
		// infimum
		rinf = dinf.getInfimumBound();
		iOpn = dinf.isInfimumOpen();
		
		// supremum
		if(dsup == TO_SUPREMUM) {
			rsup = getSupremumBound();
			sOpn = isSupremumOpen();
		} else if(dsup == BELOW_INFIMUM) {
			rsup = getInfimumBound();
			sOpn = !isInfimumOpen();
		} else {
			throw new IllegalArgumentException(dsup.toString());
		}
		return cloneInterval(rinf, iOpn, rsup, sOpn);
	}

	/**
	 * returns the closure of this interval.<br>
	 * この区間の閉包(この区間を含む最小の閉区間)を返す.
	 * 
	 * @return  the closure of this interval.
	 */
	public Range closure() {
		return closureInterval();
	}

	/**
	 * returns the interior of this interval.<br>
	 * この区間の内部(この区間に含まれる最大の開区間)を返す.
	 * 
	 * @return  the interior of this interval.
	 */
	public Range interior() {
		return interiorInterval();
	}

	/**
	 * returns the closure of this interval.<br>
	 * この区間の閉包(この区間を含む最小の閉区間)を返す.
	 * 
	 * @return  the closure of this interval.
	 */
	public Interval closureInterval() {
		return Intervals.newClosedInterval(inf, sup);
	}

	/**
	 * returns the interior of this interval.<br>
	 * この区間の内部(この区間に含まれる最大の開区間)を返す.
	 * 
	 * @return  the interior of this interval.
	 */
	public Interval interiorInterval() {
		return Intervals.newOpenInterval(inf, sup);
	}

	/**
	 * returns the smallest interval which covered this and
	 * the specified interval.<br>
	 * この区間と与えられた区間を覆う最小の区間を得る.
	 * 
	 * @param iv  the interval to be covered
	 * @return  the smallest interval which covered the intervals
	 */
	public Interval coveredInterval(Interval iv) {
		if(containsAll(iv)) {
			return this;
		} else if(in(iv)) {
			return iv;
		} else if(iv.isInfimumAbove(getInfimumBound())) {
			// iv.belowSupremum(getSupremumBound())) {
			return iv.cloneInterval(this, TO_SUPREMUM);
		} else {
			// iv.aboveSupremum(getSupremumBound())) {
			return iv.cloneInterval(FROM_INFIMUM, this);
		}
	}

	/**
	 * returns the interval hidden by the smallest upper half plane
	 * which covered the specified interval.<br>
	 * この区間から与えられた区間を覆う上半開区間を除いた区間を得る.
	 * 
	 * @param iv  the interval to hide
	 * @return  the interval which is hidden
	 */
	public Interval hideUpperHalfIntervalOf(Interval iv) {
		if(iv.isInfimumAboveAllOf(this)) {
			return this;
		} else if(!iv.isInfimumAboveAnyOf(this)) {
			return O;
		} else {
			return iv.cloneInterval(this, BELOW_INFIMUM);
		}
	}

	/**
	 * returns the interval hidden by the smallest lower half plane
	 * which covered the specified interval.<br>
	 * この区間から与えられた区間を覆う下半開区間を除いた区間を得る.
	 * 
	 * @param iv  the interval to hide
	 * @return  the interval which is hidden
	 */
	public Interval hideLowerHalfIntervalOf(Interval iv) {
		if(iv.isSupremumBelowAllOf(this)) {
			return this;
		} else if(!iv.isSupremumBelowAnyOf(this)) {
			return O;
		} else {
			return iv.cloneInterval(ABOVE_SUPREMUM, this);
		}
	}

	/**
	 * returns the smallest lower half plane which covered this.<br>
	 * この区間を覆う最小の下半開区間を得る.
	 * 
	 * @return the smallest lower half interval covered this
	 */
	public Interval lowerHalfInterval() {
		return Intervals.newInfimumlessInterval(
				getSupremumBound(), isSupremumOpen());
	}

	/**
	 * returns the smallest upper half plane which covered this.<br>
	 * この区間を覆う最小の上半開区間を得る.
	 * 
	 * @return the smallest upper half interval covered this
	 */
	public Interval upperHalfInterval() {
		return Intervals.newSupremumlessInterval(
				getInfimumBound(), isInfimumOpen());
	}

	/**
	 * returns the infimum bound.<br>
	 * 下限の境界点を得る.
	 * 
	 * @return  the infimum bound
	 */
	public Object getInfimumBound() {
		return inf;
	}

	/**
	 * returns the supremum bound.<br>
	 * 上限の境界点を得る.
	 * 
	 * @return  the supremum bound
	 */
	public Object getSupremumBound() {
		return sup;
	}
	
	/**
	 * returns the intersection of this and the specified interval.<br>
	 * この区間と与えられた区間の共通部分を得る.
	 * 
	 * @param j  the interval
	 * @return  the intersection
	 */
	public Interval meetInterval(Interval j) {
		if(independentOf(j)) {
			// intervals do not overlap
			return O;
		} else if(isInfimumAboveAnyOf(j)) {
			return isSupremumAboveAllOf(j) ? newInstance(this, j) : this;
		} else {
			return isSupremumAboveAllOf(j) ? j : newInstance(j, this);
		}
	}
	
	/**
	 * returns the relative complementary interval of this in
	 * the specified interval.<br>
	 * 与えられた区間を全体区間とするこの集合の差集合を得る.
	 * 
	 * @param u  the universal interval
	 * @return  the complementary interval
	 */
	public SortedSet<Interval> complementIntervals(Interval u) {
		SortedSet<Interval> iv = new TreeSet<Interval>();
		
		if(independentOf(u)) {
			// intervals do not overlap
			iv.add(this);
		} else {
			// the infimum of j is larger than i
			if(isInfimumBelowAllOf(u)) {
				iv.add(hideUpperHalfIntervalOf(u));
			}
			
			// the supremum of j is smaller than i
			if(isSupremumAboveAllOf(u)) {
				iv.add(hideLowerHalfIntervalOf(u));
			}
		}
		return iv;
	}

	/**
	 * returns true if the specified object is above the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点より大きいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundAbove(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBoundAbove(Object o) {
		return Limit.compareBound(o, getInfimumBound()) < 0;
	}

	/**
	 * returns true if the specified object is below the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点より小さいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundBelow(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBoundBelow(Object o) {
		return Limit.compareBound(o, getInfimumBound()) > 0;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点に等しいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBoundEqualTo(Object o) {
		return Limit.compareBound(o, getInfimumBound()) == 0;
	}

	/**
	 * returns true if the specified object is above the supremum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点より小さいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundAbove(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBoundAbove(Object o) {
		return Limit.compareBound(o, getSupremumBound()) < 0;
	}

	/**
	 * returns true if the specified object is below the supremum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点より大きいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundBelow(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBoundBelow(Object o) {
		return Limit.compareBound(o, getSupremumBound()) > 0;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点に等しいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBoundEqualTo(Object o) {
		return Limit.compareBound(o, getSupremumBound()) == 0;
	}

	/**
	 * returns true if the specified object is above the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限より小さいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @see net.morilib.natalia.range.Range#isInfimumAbove(java.lang.Object)
	 */
	/*package*/ boolean isInfimumAbove(Object o) {
		int r = Limit.compareBound(o, getInfimumBound());
		return (r < 0) || (r == 0 && isInfimumOpen());
	}

	/**
	 * returns true if the specified object is below the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限より大きいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBelow(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBelow(Object o) {
		return Limit.compareBound(o, getInfimumBound()) > 0;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限に等しいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isInfimumEqualTo(Object o) {
		return (isInfimumClosed() &&
				Limit.compareBound(o, getInfimumBound()) == 0);
	}

	/**
	 * returns true if the specified object is below the supremum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限より小さいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumAbove(java.lang.Object)
	 */
	/*package*/ boolean isSupremumAbove(Object o) {
		return Limit.compareBound(o, getSupremumBound()) < 0;
	}

	/**
	 * returns true if the specified object is above the supremum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限より大きいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBelow(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBelow(Object o) {
		int r = Limit.compareBound(o, getSupremumBound());
		return (r > 0) || (r == 0 && isSupremumOpen());
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限に等しいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isSupremumEqualTo(Object o) {
		return (!isSupremumOpen() &&
				Limit.compareBound(o, getSupremumBound()) == 0);
	}

	/**
	 * returns true if all points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundBelowAllClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isInfimumBoundBelowAllClosureOf(Range r) {
		return r.isInfimumBoundAbove(getInfimumBound());
	}

	/**
	 * returns true if all points of the specified range is below
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundAboveAllClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isInfimumBoundAboveAllClosureOf(Range r) {
		return r.isSupremumBoundBelow(getInfimumBound());
	}

	/**
	 * returns true if any points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundBelowAnyClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isInfimumBoundBelowAnyClosureOf(Range r) {
		return r.isSupremumBoundAbove(getInfimumBound());
	}

	/**
	 * returns true if any points of the specified range is below
	 * the infimum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the infimum
	 * @see net.morilib.natalia.range.Range#isInfimumBoundAboveAnyClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isInfimumBoundAboveAnyClosureOf(Range r) {
		return r.isInfimumBoundBelow(getInfimumBound());
	}

	/**
	 * returns true if the infimum bound of this range contacts
	 * the supremum bound of the specified range.<br>
	 * この範囲の下限の境界点が与えられた範囲の上限の境界点と一致する
	 * (接触する)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum contacts the supremum the range
	 * @see net.morilib.natalia.range.Range#contactInfimumBound(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean contactInfimumBound(Range r) {
		return r.isSupremumBoundEqualTo(getInfimumBound());
	}

	/**
	 * returns true if the infimum bound of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の下限の境界点が与えられた範囲の下限の境界点と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is the same as one
	 * @see net.morilib.natalia.range.Range#commonInfimumBoundTo(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean commonInfimumBoundTo(Range r) {
		return r.isInfimumBoundEqualTo(getInfimumBound());
	}

	/**
	 * returns true if all points of the specified range is below
	 * the supremum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundAboveAllClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isSupremumBoundAboveAllClosureOf(Range r) {
		return r.isSupremumBoundBelow(getSupremumBound());
	}

	/**
	 * returns true if all points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the infimum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundBelowAllClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isSupremumBoundBelowAllClosureOf(Range r) {
		return r.isInfimumBoundAbove(getSupremumBound());
	}

	/**
	 * returns true if any points of the specified range is below
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundAboveAnyClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isSupremumBoundAboveAnyClosureOf(Range r) {
		return r.isInfimumBoundBelow(getSupremumBound());
	}

	/**
	 * returns true if any points of the specified range is above
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the supremum
	 * @see net.morilib.natalia.range.Range#isSupremumBoundBelowAnyClosureOf(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean isSupremumBoundBelowAnyClosureOf(Range r) {
		return r.isSupremumBoundAbove(getSupremumBound());
	}

	/**
	 * returns true if the supremum bound of this range contacts
	 * the infimum bound of the specified range.<br>
	 * この範囲の上限の境界点が与えられた範囲の下限の境界点と一致する
	 * (接触する)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum contacts the infimum the range
	 * @see net.morilib.natalia.range.Range#contactSupremumBound(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean contactSupremumBound(Range r) {
		return r.isInfimumBoundEqualTo(getSupremumBound());
	}

	/**
	 * returns true if the supremum bound of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の上限の境界点が与えられた範囲の上限の境界点と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is the same as one
	 * @see net.morilib.natalia.range.Range#commonSupremumBoundTo(net.morilib.natalia.range.Range)
	 */
	/*package*/ boolean commonSupremumBoundTo(Range r) {
		return r.isSupremumBoundEqualTo(getSupremumBound());
	}

	/**
	 * return true if the smallest edge of this range is finite.
	 * <p>この範囲の小さい側の端点が有限のときにtrueを得る.
	 * 
	 * @return  true if the smallest edge of this range is finite
	 * @see net.morilib.natalia.range.Range#isInfimumFinite()
	 */
	public boolean isInfimumFinite() {
		return (getInfimumBound() != Limit.MINIMUM &&
				getInfimumBound() != Limit.MAXIMUM);
	}

	/**
	 * return true if the largest edge of this range is finite.
	 * <p>この範囲の大きい側の端点が有限のときにtrueを得る.
	 * 
	 * @return  true if the largest edge of this range is finite
	 * @see net.morilib.natalia.range.Range#isSupremumFinite()
	 */
	public boolean isSupremumFinite() {
		return (getSupremumBound() != Limit.MINIMUM &&
				getSupremumBound() != Limit.MAXIMUM);
	}

	/**
	 * returns true if the smallest edge of this range is open.
	 * <p>この範囲の小さい側の端点が開いているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isInfimumClosed()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the smallest edge of this range is open
	 * @see net.morilib.natalia.range.Range#isInfimumOpen()
	 */
	public boolean isInfimumOpen() {
		return !isInfimumFinite() || left != 0;
	}

	/**
	 * returns true if the largest edge of this range is open.
	 * <p>この範囲の大きい側の端点が開いているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isSupremumClosed()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the largest edge of this range is open
	 * @see net.morilib.natalia.range.Range#isSupremumOpen()
	 */
	public boolean isSupremumOpen() {
		return !isSupremumFinite() || right != 0;
	}

	/**
	 * returns true if the smallest edge of this range is closed.
	 * <p>この範囲の小さい側の端点が閉じているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isInfimumOpen()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the smallest edge of this range is closed
	 * @see net.morilib.natalia.range.Range#isInfimumClosed()
	 */
	public boolean isInfimumClosed() {
		return !isInfimumFinite() || left == 0;
	}

	/**
	 * returns true if the largest edge of this range is closed.
	 * <p>この範囲の大きい側の端点が閉じているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isSupremumOpen()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the largest edge of this range is closed
	 * @see net.morilib.natalia.range.Range#isSupremumClosed()
	 */
	public boolean isSupremumClosed() {
		return !isSupremumFinite() || right == 0;
	}

	/**
	 * returns true if this range is a open set.<br>
	 * この範囲が開集合であればtrueを得る.
	 * 
	 * @return  true if this range is open
	 * @see net.morilib.natalia.range.Range#isOpen()
	 */
	public boolean isOpen() {
		return isInfimumOpen() && isSupremumOpen();
	}

	/**
	 * returns true if this range is a closed set.<br>
	 * この範囲が閉集合であればtrueを得る.
	 * 
	 * @return  true if this range is closed
	 * @see net.morilib.natalia.range.Range#isClosed()
	 */
	public boolean isClosed() {
		return isInfimumClosed() && isSupremumClosed();
	}

	/**
	 * Returns true if this set contains the given object.<br>
	 * この集合が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is contained in this
	 * @see net.morilib.natalia.range.Range#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return !isInfimumAbove(o) && !isSupremumBelow(o);
	}

	/**
	 * Returns true if the given object is neighborhood of
	 * this set.<br>
	 * この集合の近傍、つまり与えられたオブジェクトをこの集合の開集合に
	 * 含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is neighborhood of this
	 * @see net.morilib.natalia.range.Range#isNeighborhoodOf(java.lang.Object)
	 */
	public boolean isNeighborhoodOf(Object o) {
		return isInfimumBoundBelow(o) && isSupremumBoundAbove(o);
	}

	/**
	 * Returns true if the closure of this set contains the given
	 * object.<br>
	 * この集合の閉包が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is neighborhood of this
	 * @see net.morilib.natalia.range.Range#containsClosure(java.lang.Object)
	 */
	public boolean containsClosure(Object o) {
		return !isInfimumBoundAbove(o) && !isSupremumBoundBelow(o);
	}

	/**
	 * Returns true if this set contains the given object.<br>
	 * この集合の境界が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is contained in this
	 * @see net.morilib.natalia.range.Range#containsBound(java.lang.Object)
	 */
	public boolean containsBound(Object o) {
		return isInfimumBoundEqualTo(o) || isSupremumBoundEqualTo(o);
	}

	/**
	 * Returns true if the closure of this set is in
	 * the specified set.<br>
	 * この集合の閉包が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 * @see net.morilib.natalia.range.Range#closureContains(net.morilib.natalia.range.Range)
	 */
	public boolean closureContains(Range t) {
		Range r = (Range)t;
		return (!isInfimumBoundAboveAnyClosureOf(r) &&
				!isSupremumBoundBelowAnyClosureOf(r));
	}

	/**
	 * Returns true if the interior of this set is in
	 * the specified set.<br>
	 * この集合の内部が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 * @see net.morilib.natalia.range.Range#interiorContains(net.morilib.natalia.range.Range)
	 */
	public boolean interiorContains(Range t) {
		Range r = (Range)t;
		return isInfimumBelowAllOf(r) && isSupremumAboveAllOf(r);
	}

	/**
	 * returns true if the set contains all points of
	 * the specified set.<br>
	 * この集合が与えられた集合の点を全て含む(与えられた集合がこの集合の
	 * 部分集合である)ときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if this contains all points of the specified set
	 * @see net.morilib.natalia.range.Range#containsAll(net.morilib.natalia.range.Range)
	 */
	public boolean containsAll(Range t) {
		Range r = (Range)t;
		return !isInfimumAboveAnyOf(r) && !isSupremumBelowAnyOf(r);
		//return aboveInfimumAll(r) && belowSupremumAll(r);
	}

	/**
	 * returns true if the set contains some points of
	 * the specified set.<br>
	 * この集合が与えられた集合の点のどれかを含むときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if this contains some points of the specified set
	 * @see net.morilib.natalia.range.Range#containsAny(net.morilib.natalia.range.Range)
	 */
	public boolean containsAny(Range t) {
		Range r = (Range)t;
		return !isInfimumAboveAllOf(r) && !isSupremumBelowAllOf(r);
		//return aboveInfimumAny(r) && belowSupremumAny(r);
	}

	/**
	 * Returns the intervals which consists this range.<br>
	 * この範囲を構成する全ての区間を得る.
	 * 
	 * @return  the intervals which consists this range
	 * @see net.morilib.natalia.range.Range#intervals()
	 */
	public SortedSet<Interval> intervals() {
		return InternalUtils.sortedSingleton(this);
	}

	/**
	 * Returns the boundary set of this topological set.<br>
	 * この範囲のすべての境界点を含む集合を得る.
	 * 
	 * @see net.morilib.natalia.range.Range#bound()
	 */
	public Range bound() {
		return Range.newPoints(boundElements());
	}

	/**
	 * returns the bounds of this range by SortedSet.<br>
	 * この範囲の境界点のSortedSetを得る. 
	 * 
	 * @return  the SortedSet which includes all bounds of this range
	 * @see net.morilib.natalia.range.Range#boundElements()
	 */
	public SortedSet<?> boundElements() {
		if(isInfimumFinite() && isSupremumFinite()) {
			SortedSet<Object> s = new TreeSet<Object>();
			
			s.add(getInfimumBound());
			s.add(getSupremumBound());
			return s;
		} else if(isInfimumFinite()) {
			return InternalUtils.sortedSingleton(getInfimumBound());
		} else if(isSupremumFinite()) {
			return InternalUtils.sortedSingleton(getSupremumBound());
		} else {
			return InternalUtils.emptySortedSet();
		}
	}

	/**
	 * returns true if the infimum bound of this range is contained
	 * in the specified range.<br>
	 * 与えられた範囲がこの範囲の下限の境界点を含むときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is contained in the range 
	 * @see net.morilib.natalia.range.Range#infimumBoundIn(net.morilib.natalia.range.Range)
	 */
	public boolean infimumBoundIn(Range r) {
		return r.contains(getInfimumBound());
	}

	/**
	 * returns true if the supremum bound of this range is contained
	 * in the specified range.<br>
	 * 与えられた範囲がこの範囲の上限の境界点を含むときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is contained in the range 
	 * @see net.morilib.natalia.range.Range#supremumBoundIn(net.morilib.natalia.range.Range)
	 */
	public boolean supremumBoundIn(Range r) {
		return r.contains(getSupremumBound());
	}

	/**
	 * returns true if the bound of this set is a subset of the
	 * specified set.<br>
	 * この集合の境界が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if the bound of this set is a subset
	 * @see net.morilib.natalia.range.Range#boundsIn(net.morilib.natalia.range.Range)
	 */
	public boolean boundsIn(Range t) {
		return (t.contains(getInfimumBound ()) &&
				t.contains(getSupremumBound()));
	}

	/**
	 * returns true if the bound of this set is independent of
	 * the specified set.<br>
	 * この集合の境界が与えられた集合と独立であるときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if the bound of this set is independent
	 * @see net.morilib.natalia.range.Range#boundsIndependentOf(net.morilib.natalia.range.Range)
	 */
	public boolean boundsIndependentOf(Range t) {
		return (!t.contains(getInfimumBound ()) &&
				!t.contains(getSupremumBound()));
	}

	/**
	 * Returns true if this set is in the specified set.<br>
	 * この集合が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 * @see net.morilib.natalia.range.Range#in(net.morilib.natalia.range.Range)
	 */
	public boolean in(Range t) {
		for(Interval j : ((Range)t).intervals()) {
			if(Ranges.inCoveredUpperHalfPlane(this, j) &&
					Ranges.inCoveredLowerHalfPlane(this, j)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this set is independent of
	 * the specified set.<br>
	 * この集合が与えられた集合と独立しているときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is independent of the topology t
	 * @see net.morilib.natalia.range.Range#independentOf(net.morilib.natalia.range.Range)
	 */
	public boolean independentOf(Range t) {
		for(Interval j : ((Range)t).intervals()) {
			if(!independentOf(this, j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the interior of this set is independent of
	 * the specified set.<br>
	 * この集合の内部が与えられた集合と独立しているときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is independent of the topology t
	 * @see net.morilib.natalia.range.Range#interiorIndependentOf(net.morilib.natalia.range.Range)
	 */
	public boolean interiorIndependentOf(Range t) {
		for(Interval j : ((Range)t).intervals()) {
			if(!interiorIndependentOf(this, j)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if this is empty.<br>
	 * この集合が空集合のときtrueを得る.
	 * 
	 * @return  true if this is empty
	 * @see net.morilib.natalia.range.Range#isEmpty()
	 */
	public boolean isEmpty() {
		return (getInfimumBound()  == Limit.MAXIMUM &&
				getSupremumBound() == Limit.MINIMUM);
	}

	/**
	 * compares this instance to the given object.
	 * <p>このインスタンスを与えられたオブジェクトと比較する.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Interval o) {
		if(o instanceof Interval) {
			return compare(this, o);
		} else {
			throw new ClassCastException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.Range#isFinite()
	 */
	@Override
	public boolean isFinite() {
		return sup.equals(inf) && left == right && left == 0;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.Range#getJavaSetIfFinite()
	 */
	@Override
	public SortedSet<?> getJavaSetIfFinite() {
		return isFinite() ? InternalUtils.sortedSingleton(sup) : null;
	}

	/* (non-Javadoc)
	 * @see net.morilib.util.Section#side(java.lang.Object)
	 */
	public int side(Object o) {
		if(isInfimumAbove(o)) {
			return -1;
		} else if(isSupremumBelow(o)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * returns the hash code of this object.
	 * <p>このオブジェクトのハッシュ値を得る.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int r = 17;
		r = 37 * r + inf.hashCode();
		r = 37 * r + sup.hashCode();
		r = 37 * r + left;
		r = 37 * r + right;
		return r;
	}

	/**
	 * returns the string representation of this object.
	 * <p>このオブジェクトの文字列表現を得る.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		if(getInfimumBound() == Limit.MAXIMUM) {
			return "Empty Interval";
		}
		buf.append(isInfimumOpen() ? "(" : "[");
		buf.append((inf == Limit.MINIMUM) ? "-oo" : inf);
		buf.append(",");
		buf.append((sup == Limit.MAXIMUM) ? "+oo" : sup);
		buf.append(isSupremumOpen() ? ")" : "]");
		return buf.toString();		
	}

}
