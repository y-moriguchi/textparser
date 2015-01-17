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

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2011/10/09
 */
public class IntInterval extends AbstractIntRange
implements Comparable<IntInterval>, java.io.Serializable {

	//
	/*package*/ int left, right;

	/**
	 * 
	 * @param left
	 * @param right
	 */
	public IntInterval(int left, int right) {
		if(left > right) {
			throw new IllegalArgumentException();
		}
		this.left  = left;
		this.right = right;
	}

	//
	/*package*/ IntInterval(boolean f) {
		this.left  = Integer.MAX_VALUE;
		this.right = Integer.MIN_VALUE;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#contains(int)
	 */
	public boolean contains(int x) {
		return left <= x && x <= right;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#contains(net.morilib.range.integer.IntRange)
	 */
	public boolean containsAll(IntRange r) {
		return left <= r.minimum() && r.maximum() <= right;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#containsAny(net.morilib.range.integer.IntRange)
	 */
	public boolean containsAny(IntRange r) {
		for(IntInterval i : r.intervals()) {
			if(i.left <= right && i.right >= left) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#isIndependent(net.morilib.range.integer.IntRange)
	 */
	public boolean isIndependent(IntRange r) {
		for(IntInterval i : r.intervals()) {
			if(i.right <= left || i.left >= right) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#minimum()
	 */
	public int minimum() {
		return left;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#maximum()
	 */
	public int maximum() {
		return right;
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#intervals()
	 */
	public Collection<IntInterval> intervals() {
		if(left > right) {
			return Collections.emptySet();
		} else {
			return Collections.singleton(this);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#cardinality()
	 */
	public long cardinality() {
		return (left > right) ? 0 : (long)right - (long)left + 1l;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntInterval o) {
		if(left < o.left) {
			return -1;
		} else if(left > o.left) {
			return 1;
		} else if(right < o.right) {
			return -1;
		} else if(right > o.right) {
			return 1;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int r = Hashes.INIT;

		r = Hashes.A * (r + left);
		r = Hashes.A * (r + right);
		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof IntInterval) {
			IntInterval i = (IntInterval)o;

			return left == i.left && right == i.right;
		}
		return false;
	}

}
