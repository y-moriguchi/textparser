/*
 * Copyright 2009-2010 Yuichiro Moriguchi
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
package net.morilib.range.integer;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2011/10/09
 */
public class IntMergedRange extends AbstractIntRange
implements java.io.Serializable {

	//
	/*package*/ SortedSet<IntInterval> intervals;

	//
	/*package*/ IntMergedRange() {
		this.intervals = new TreeSet<IntInterval>();
	}

	//
	/*package*/ IntMergedRange(SortedSet<IntInterval> s) {
		this.intervals = s;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#contains(int)
	 */
	public boolean contains(int x) {
		for(IntInterval i : intervals) {
			if(i.contains(x)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#containsAll(net.morilib.range.integer.IntRange)
	 */
	public boolean containsAll(IntRange r) {
		outer: for(IntInterval j : r.intervals()) {
			for(IntInterval i : intervals) {
				if(j.isContained(i)) {
					continue outer;
				}
			}
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#containsAny(net.morilib.range.integer.IntRange)
	 */
	public boolean containsAny(IntRange r) {
		for(IntInterval i : intervals) {
			for(IntInterval j : r.intervals()) {
				if(i.containsAny(j)) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#isIndependent(net.morilib.range.integer.IntRange)
	 */
	public boolean isIndependent(IntRange r) {
		for(IntInterval i : intervals) {
			for(IntInterval j : r.intervals()) {
				if(i.isIndependent(j)) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#minimum()
	 */
	public int minimum() {
		return intervals.first().left;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#maximum()
	 */
	public int maximum() {
		return intervals.last().right;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#intervals()
	 */
	public Collection<IntInterval> intervals() {
		return Collections.unmodifiableSortedSet(intervals);
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#cardinality()
	 */
	public long cardinality() {
		long c = 0;

		for(IntInterval i : intervals) {
			c += i.cardinality();
		}
		return c;
	}

}
