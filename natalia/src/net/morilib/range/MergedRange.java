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

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a merged range.<br>
 * このクラスは複数の区間からなる和集合を表す.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class MergedRange extends Range {
	
	//
	/*package*/ SortedSet<Interval> sets;
	
	//
	/*package*/ MergedRange() {
		this.sets = new TreeSet<Interval>();
	}
	
	//
	private MergedRange(SortedSet<Interval> sets) {
		this.sets = sets;
	}
	
	//
	/*package*/ MergedRange(Range t) {
		if(t instanceof MergedRange) {
			this.sets = new TreeSet<Interval>(
					((MergedRange)t).sets);
		} else {
			this.sets = new TreeSet<Interval>();
			sets.add((Interval)t);
		}
		//addAll(t.intervals());
	}
	
	//
	/*package*/ void addAll(Collection<Interval> intervals) {
		for(Interval o : intervals) {
			add(o);
		}
	}
	
	// 20080524 add
	//
	private SortedSet<Interval> optimizeInterval(
			SortedSet<Interval> s) {
		SortedSet<Interval> r = new TreeSet<Interval>();
		Interval  k = null;
		
		for(Interval j : s) {
			if(k == null) {
				k = j;
			} else if(k.contactSupremumBound(j)) {
				if(k.isSupremumOpen() && j.isInfimumClosed()) {
					k = k.cloneInterval(
							Interval.FROM_INFIMUM, j);
				} else if(k.isSupremumClosed() && j.isInfimumOpen()) {
					k = k.cloneInterval(
							Interval.FROM_INFIMUM, j);
				} else {
					r.add(k);
					k = j;
				}
			} else {
				r.add(k);
				k = j;
			}
		}
		
		if(k != null) {
			r.add(k);
		}
		return r;
	}
	// 20080524 add end
	
	//
	/*package*/ void add(Interval t) {
		SortedSet<Interval> r = new TreeSet<Interval>();
		Interval  k = null, l = t;
		
		for(Interval j : sets) {
			if(j.independentOf(l)) {
				r.add((k == null) ? j : k);
				k = null;
			} else {
				l = k = l.coveredInterval(j);
			}
		}
		
		r.add(l);
		sets.clear();
		sets = optimizeInterval(r);
	}

	/**
	 * returns true if the specified object is below the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限より小さいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the infimum
	 * @see net.morilib.range.Range#isInfimumAbove(java.lang.Object)
	 */
	/*package*/ boolean isInfimumAbove(Object o) {
		for(Range m : sets) {
			if(!m.isInfimumAbove(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the specified object is above the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限より大きいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the infimum
	 * @see net.morilib.range.Range#isInfimumBelow(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBelow(Object o) {
		for(Range m : sets) {
			if(m.isInfimumBelow(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の下限に等しいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the infimum
	 * @see net.morilib.range.Range#isInfimumEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isInfimumEqualTo(Object o) {
		return !isInfimumAbove(o) && !isInfimumBelow(o);
	}

	/**
	 * returns true if the specified object is below the supremum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限より小さいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the supremum
	 * @see net.morilib.range.Range#isSupremumAbove(java.lang.Object)
	 */
	/*package*/ boolean isSupremumAbove(Object o) {
		for(Range m : sets) {
			if(m.isSupremumAbove(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the specified object is above the supremum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限より大きいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the supremum
	 * @see net.morilib.range.Range#isSupremumBelow(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBelow(Object o) {
		for(Range m : sets) {
			if(!m.isSupremumBelow(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * of this range.<br>
	 * <p>指定されたオブジェクトがこの範囲の上限に等しいときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the supremum
	 * @see net.morilib.range.Range#isSupremumEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isSupremumEqualTo(Object o) {
		return !isSupremumAbove(o) && !isSupremumBelow(o);
	}

	/**
	 * returns true if the specified object is below the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点より小さいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the infimum
	 * @see net.morilib.range.Range#isInfimumBoundAbove(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBoundAbove(Object o) {
		for(Range m : sets) {
			if(!m.isInfimumBoundAbove(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the specified object is above the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点より大きいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the infimum
	 * @see net.morilib.range.Range#isInfimumBoundBelow(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBoundBelow(Object o) {
		for(Range m : sets) {
			if(m.isInfimumBoundBelow(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の下限の境界点に等しいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the infimum
	 * @see net.morilib.range.Range#isInfimumBoundEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isInfimumBoundEqualTo(Object o) {
		return !isInfimumBoundAbove(o) && !isInfimumBoundBelow(o);
	}

	/**
	 * returns true if the specified object is below the supremum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点より小さいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object below the supremum
	 * @see net.morilib.range.Range#isSupremumBoundAbove(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBoundAbove(Object o) {
		for(Range m : sets) {
			if(m.isSupremumBoundAbove(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the specified object is above the supremum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点より大きいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object above the supremum
	 * @see net.morilib.range.Range#isSupremumBoundBelow(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBoundBelow(Object o) {
		for(Range m : sets) {
			if(!m.isSupremumBoundBelow(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the specified object is equal to the infimum
	 * bound of this range.<br>
	 * 指定されたオブジェクトがこの範囲の上限の境界点に等しいとき
	 * trueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return  true is the object equals the supremum
	 * @see net.morilib.range.Range#isSupremumBoundEqualTo(java.lang.Object)
	 */
	/*package*/ boolean isSupremumBoundEqualTo(Object o) {
		return !isSupremumBoundAbove(o) && !isSupremumBoundBelow(o);
	}

	/**
	 * returns true if all points of the specified range is below
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the infimum
	 * @see net.morilib.range.Range#isInfimumBoundAboveAllClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isInfimumBoundAboveAllClosureOf(Range r) {
		for(Range m : sets) {
			if(!m.isInfimumBoundAboveAllClosureOf(r)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if all points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the infimum
	 * @see net.morilib.range.Range#isInfimumBoundBelowAllClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isInfimumBoundBelowAllClosureOf(Range r) {
		for(Range m : sets) {
			if(m.isInfimumBoundBelowAllClosureOf(r)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if any points of the specified range is below
	 * the infimum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the infimum
	 * @see net.morilib.range.Range#isInfimumBoundAboveAnyClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isInfimumBoundAboveAnyClosureOf(Range r) {
		for(Range m : sets) {
			if(!m.isInfimumBoundAboveAnyClosureOf(r)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if any points of the specified range is above
	 * the infimum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の下限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the infimum
	 * @see net.morilib.range.Range#isInfimumBoundBelowAnyClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isInfimumBoundBelowAnyClosureOf(Range r) {
		for(Range m : sets) {
			if(m.isInfimumBoundBelowAnyClosureOf(r)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the infimum bound of this range contacts
	 * the supremum bound of the specified range.<br>
	 * この範囲の下限の境界点が与えられた範囲の上限の境界点と一致する
	 * (接触する)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum contacts the supremum the range
	 * @see net.morilib.range.Range#contactInfimumBound(net.morilib.range.Range)
	 */
	/*package*/ boolean contactInfimumBound(Range r) {
		return (!isInfimumBoundAboveAllClosureOf(r) &&
				!isInfimumBoundBelowAnyClosureOf(r));
	}

	/**
	 * returns true if the infimum bound of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の下限の境界点が与えられた範囲の下限の境界点と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is the same as one
	 * @see net.morilib.range.Range#commonInfimumBoundTo(net.morilib.range.Range)
	 */
	/*package*/ boolean commonInfimumBoundTo(Range r) {
		return (!isInfimumBoundAboveAnyClosureOf(r) &&
				!isInfimumBoundBelowAllClosureOf(r));
	}

	/**
	 * returns true if all points of the specified range is below
	 * the supremum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range below the supremum
	 * @see net.morilib.range.Range#isSupremumBoundAboveAllClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isSupremumBoundAboveAllClosureOf(Range r) {
		for(Range m : sets) {
			if(m.isSupremumBoundAboveAllClosureOf(r)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if all points of the specified range is above
	 * the supremum bound of this range.<br>
	 * 指定された範囲の全ての点がこの範囲の上限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is all points of the range above the supremum
	 * @see net.morilib.range.Range#isSupremumBoundBelowAllClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isSupremumBoundBelowAllClosureOf(Range r) {
		for(Range m : sets) {
			if(!m.isSupremumBoundBelowAllClosureOf(r)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if any points of the specified range is below
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限の境界点より小さいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range below the supremum
	 * @see net.morilib.range.Range#isSupremumBoundAboveAnyClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isSupremumBoundAboveAnyClosureOf(Range r) {
		for(Range m : sets) {
			if(m.isSupremumBoundAboveAnyClosureOf(r)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if any points of the specified range is above
	 * the supremum bound of this range.<br>
	 * 指定された範囲のある点がこの範囲の上限の境界点より大きいところに
	 * 存在するときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true is any points of the range above the supremum
	 * @see net.morilib.range.Range#isSupremumBoundBelowAnyClosureOf(net.morilib.range.Range)
	 */
	/*package*/ boolean isSupremumBoundBelowAnyClosureOf(Range r) {
		for(Range m : sets) {
			if(!m.isSupremumBoundBelowAnyClosureOf(r)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the supremum bound of this range contacts
	 * the infimum bound of the specified range.<br>
	 * この範囲の上限の境界点が与えられた範囲の下限の境界点と一致する
	 * (接触する)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum contacts the infimum the range
	 * @see net.morilib.range.Range#contactSupremumBound(net.morilib.range.Range)
	 */
	/*package*/ boolean contactSupremumBound(Range r) {
		return (!isSupremumBoundAboveAnyClosureOf(r) &&
				!isSupremumBoundBelowAllClosureOf(r));
	}

	/**
	 * returns true if the supremum bound of this range is the same
	 * as one of the specified range.<br>
	 * この範囲の上限の境界点が与えられた範囲の上限の境界点と一致する
	 * (共通である)ときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is the same as one
	 * @see net.morilib.range.Range#commonSupremumBoundTo(net.morilib.range.Range)
	 */
	/*package*/ boolean commonSupremumBoundTo(Range r) {
		return (!isSupremumBoundAboveAllClosureOf(r) &&
				!isSupremumBoundBelowAnyClosureOf(r));
	}

	/**
	 * Returns true if this set contains the given object.<br>
	 * この集合が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is contained in this
	 * @see net.morilib.range.Range#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		for(Range m : sets) {
			if(m.contains(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this set is in the specified set.<br>
	 * この集合が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 * @see net.morilib.range.Range#in(net.morilib.range.Range)
	 */
	public boolean in(Range t) {
		for(Range r : sets) {
			if(!r.in(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if this set is independent of
	 * the specified set.<br>
	 * この集合が与えられた集合と独立しているときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is independent of the topology t
	 * @see net.morilib.range.Range#independentOf(net.morilib.range.Range)
	 */
	public boolean independentOf(Range t) {
		for(Range r : sets) {
			if(!r.independentOf(t)) {
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
	 * @see net.morilib.range.Range#interiorIndependentOf(net.morilib.range.Range)
	 */
	public boolean interiorIndependentOf(Range t) {
		for(Range r : sets) {
			if(!r.interiorIndependentOf(t)) {
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
	 * @see net.morilib.range.Range#isEmpty()
	 */
	public boolean isEmpty() {
		for(Range r : sets) {
			if(!r.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * return true if the smallest edge of this range is finite.
	 * <p>この範囲の小さい側の端点が有限のときにtrueを得る.
	 * 
	 * @return  true if the smallest edge of this range is finite
	 * @see net.morilib.range.Range#isInfimumFinite()
	 */
	public boolean isInfimumFinite() {
		for(Range r : sets) {
			if(!r.isInfimumFinite()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * return true if the largest edge of this range is finite.
	 * <p>この範囲の大きい側の端点が有限のときにtrueを得る.
	 * 
	 * @return  true if the largest edge of this range is finite
	 * @see net.morilib.range.Range#isSupremumFinite()
	 */
	public boolean isSupremumFinite() {
		for(Range r : sets) {
			if(!r.isSupremumFinite()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the smallest edge of this range is open.
	 * <p>この範囲の小さい側の端点が開いているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isInfimumClosed()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the smallest edge of this range is open
	 * @see net.morilib.range.Range#isInfimumOpen()
	 */
	public boolean isInfimumOpen() {
		return getInfimumRange().isInfimumOpen();
	}

	/**
	 * returns true if the largest edge of this range is open.
	 * <p>この範囲の大きい側の端点が開いているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isSupremumClosed()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the largest edge of this range is open
	 * @see net.morilib.range.Range#isSupremumOpen()
	 */
	public boolean isSupremumOpen() {
		return getSupremumRange().isSupremumOpen();
	}

	/**
	 * returns true if the smallest edge of this range is closed.
	 * <p>この範囲の小さい側の端点が閉じているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isInfimumOpen()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the smallest edge of this range is closed
	 * @see net.morilib.range.Range#isInfimumClosed()
	 */
	public boolean isInfimumClosed() {
		return getInfimumRange().isInfimumClosed();
	}

	/**
	 * returns true if the largest edge of this range is closed.
	 * <p>この範囲の大きい側の端点が閉じているときにtrueを得る.<br />
	 * このメソッドがtrueのとき、{@link #isSupremumOpen()}が
	 * falseであるとは限らない(例えば、端点が無限大のとき).
	 * 
	 * @return  true if the largest edge of this range is closed
	 * @see net.morilib.range.Range#isSupremumClosed()
	 */
	public boolean isSupremumClosed() {
		return getSupremumRange().isSupremumClosed();
	}

	/**
	 * Returns the boundary set of this topological set.<br>
	 * この範囲のすべての境界点を含む集合を得る.
	 * 
	 * @see net.morilib.range.Range#bound()
	 */
	public Range bound() {
		RangeAdder res = new RangeAdder();
		
		for(Interval b : sets) {
			for(Object s : b.boundElements()) {
				if(!isNeighborhoodOf(s)) {
					res.add(Interval.newPoint(s));
				}
			}
		}
		return res.toRange();
	}

	/**
	 * Returns true if the given object is neighborhood of
	 * this set.<br>
	 * この集合の近傍、つまり与えられたオブジェクトをこの集合の開集合に
	 * 含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is neighborhood of this
	 * @see net.morilib.range.Range#isNeighborhoodOf(java.lang.Object)
	 */
	public boolean isNeighborhoodOf(Object o) {
		for(Range t : sets) {
			if(t.isNeighborhoodOf(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the closure of this set contains the given
	 * object.<br>
	 * この集合の閉包が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is neighborhood of this
	 * @see net.morilib.range.Range#containsClosure(java.lang.Object)
	 */
	public boolean containsClosure(Object o) {
		for(Range t : sets) {
			if(t.containsClosure(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this set contains the given object.<br>
	 * この集合の境界が与えられたオブジェクトを含むときtrueを得る.
	 * 
	 * @param o  the object to be tested
	 * @return   true if the object is contained in this
	 * @see net.morilib.range.Range#containsBound(java.lang.Object)
	 */
	public boolean containsBound(Object o) {
		for(Range t : sets) {
			if(t.containsBound(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the bound of this set is a subset of the
	 * specified set.<br>
	 * この集合の境界が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if the bound of this set is a subset
	 * @see net.morilib.range.Range#boundsIn(net.morilib.range.Range)
	 */
	public boolean boundsIn(Range t) {
		for(Range s : sets) {
			if(!s.boundsIn(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the closure of this set is in
	 * the specified set.<br>
	 * この集合の閉包が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 * @see net.morilib.range.Range#closureContains(net.morilib.range.Range)
	 */
	public boolean closureContains(Range t) {
		for(Range s : sets) {
			if(s.closureContains(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the interior of this set is in
	 * the specified set.<br>
	 * この集合の内部が与えられた集合の部分集合のときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return   true if this is in the topology t
	 * @see net.morilib.range.Range#interiorContains(net.morilib.range.Range)
	 */
	public boolean interiorContains(Range t) {
		for(Range s : sets) {
			if(s.interiorContains(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if the set contains all points of
	 * the specified set.<br>
	 * この集合が与えられた集合の点を全て含む(与えられた集合がこの集合の
	 * 部分集合である)ときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if this contains all points of the specified set
	 * @see net.morilib.range.Range#containsAll(net.morilib.range.Range)
	 */
	public boolean containsAll(Range t) {
		for(Range s : sets) {
			if(s.containsAll(t)) {
				return true;
			}
		}
		return false;
		//return t.in(this);
	}

	/**
	 * returns the bounds of this range by SortedSet.<br>
	 * この範囲の境界点のSortedSetを得る. 
	 * 
	 * @return  the SortedSet which includes all bounds of this range
	 * @see net.morilib.range.Range#boundElements()
	 */
	public SortedSet<?> boundElements() {
		SortedSet<Object> res = new TreeSet<Object>();
		
		for(Range s : sets) {
			res.addAll(s.boundElements());
		}
		return InternalUtils.sortedUnmodifiable(res);
	}

	/**
	 * returns true if the bound of this set is independent of
	 * the specified set.<br>
	 * この集合の境界が与えられた集合と独立であるときtrueを得る.
	 * 
	 * @param t  the topology to be tested
	 * @return  true if the bound of this set is independent
	 * @see net.morilib.range.Range#boundsIndependentOf(net.morilib.range.Range)
	 */
	public boolean boundsIndependentOf(Range t) {
		for(Range s : sets) {
			if(!s.boundsIndependentOf(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if this range is a open set.<br>
	 * この範囲が開集合であればtrueを得る.
	 * 
	 * @return  true if this range is open
	 * @see net.morilib.range.Range#isOpen()
	 */
	public boolean isOpen() {
		for(Range s : sets) {
			if(!s.isOpen()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if this range is a closed set.<br>
	 * この範囲が閉集合であればtrueを得る.
	 * 
	 * @return  true if this range is closed
	 * @see net.morilib.range.Range#isClosed()
	 */
	public boolean isClosed() {
		for(Range s : sets) {
			if(!s.isClosed()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * returns true if the infimum bound of this range is contained
	 * in the specified range.<br>
	 * 与えられた範囲がこの範囲の下限の境界点を含むときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the infimum bound is contained in the range 
	 * @see net.morilib.range.Range#infimumBoundIn(net.morilib.range.Range)
	 */
	public boolean infimumBoundIn(Range r) {
		return getInfimumRange().infimumBoundIn(r);
	}

	/**
	 * returns true if the supremum bound of this range is contained
	 * in the specified range.<br>
	 * 与えられた範囲がこの範囲の上限の境界点を含むときtrueを得る.
	 * 
	 * @param r  the range to be tested
	 * @return  true if the supremum bound is contained in the range 
	 * @see net.morilib.range.Range#supremumBoundIn(net.morilib.range.Range)
	 */
	public boolean supremumBoundIn(Range r) {
		return getSupremumRange().supremumBoundIn(r);
	}

	/**
	 * returns the closure of this range.<br>
	 * この範囲の閉包を返す.
	 * 
	 * @return  the closure
	 * @see net.morilib.range.Range#closure()
	 */
	public Range closure() {
		SortedSet<Interval> s = new TreeSet<Interval>();
		
		for(Interval t : sets) {
			s.add(t.closureInterval());
		}
		return new MergedRange(s);
	}

	/**
	 * returns the interior of this range.<br>
	 * この範囲の内部を返す.
	 * 
	 * @return  the interior
	 * @see net.morilib.range.Range#interior()
	 */
	public Range interior() {
		SortedSet<Interval> s = new TreeSet<Interval>();
		
		for(Interval t : sets) {
			s.add(t.interiorInterval());
		}
		return new MergedRange(s);
	}

	/**
	 * Returns the intervals which consists this range.<br>
	 * この範囲を構成する全ての区間を得る.
	 * 
	 * @return  the intervals which consists this range
	 * @see net.morilib.range.Range#intervals()
	 */
	public SortedSet<Interval> intervals() {
		return InternalUtils.sortedUnmodifiable(sets);
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.Range#isFinite()
	 */
	@Override
	public boolean isFinite() {
		for(Interval s : sets) {
			if(!s.isFinite()) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.Range#getJavaSetIfFinite()
	 */
	@Override
	public SortedSet<?> getJavaSetIfFinite() {
		SortedSet<Object> r = new TreeSet<Object>();

		for(Interval s : sets) {
			if(!s.isFinite()) {
				return null;
			}
			r.addAll(s.getJavaSetIfFinite());
		}
		return r;
	}

	/**
	 * returns the hash code of this object.
	 * <p>このオブジェクトのハッシュ値を得る.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return sets.hashCode();
	}

	/**
	 * returns the string representation of this object.
	 * <p>このオブジェクトの文字列表現を得る.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return InternalUtils.toString(sets.iterator(), "+");
	}

}
