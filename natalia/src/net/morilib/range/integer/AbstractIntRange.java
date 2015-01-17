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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import net.morilib.range.IntervalsInt;
import net.morilib.range.Range;
import net.morilib.range.RangeAdder;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2011/10/09
 */
public abstract class AbstractIntRange implements IntRange {

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static IntRange makeRange(SortedSet<IntInterval> s) {
		switch(s.size()) {
		case 0:   return O;
		case 1:   return s.first();
		default:  return new IntMergedRange(s);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#isContained(net.morilib.range.integer.IntRange)
	 */
	public boolean isContained(IntRange r) {
		return r.containsAll(this);
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#isContained(net.morilib.range.integer.IntRange)
	 */
	public boolean isOverlapped(IntRange r) {
		return r.containsAny(this);
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#join(net.morilib.range.integer.IntRange)
	 */
	public IntRange join(IntRange r) {
		SortedSet<IntInterval> s = new TreeSet<IntInterval>();
		Iterator<IntInterval> i = intervals().iterator();
		Iterator<IntInterval> j = r.intervals().iterator();
		IntInterval t = null, a, b, c;

		if(cardinality() == 0) {
			return r;
		} else if(r.cardinality() == 0) {
			return this;
		} else {
			a = i.hasNext() ? i.next() : null;
			b = j.hasNext() ? j.next() : null;
			while(a != null || b != null) {
				if(b == null) {
					c = a;
					a = i.hasNext() ? i.next() : null;
				} else if(a == null) {
					c = b;
					b = j.hasNext() ? j.next() : null;
				} else if(b == null || a.compareTo(b) < 0) {
					c = a;
					a = i.hasNext() ? i.next() : null;
				} else if(a == null || b.compareTo(a) < 0) {
					c = b;
					b = j.hasNext() ? j.next() : null;
				} else {
					c = a;
					a = i.hasNext() ? i.next() : null;
					b = j.hasNext() ? j.next() : null;
				}

				if(t == null) {
					t = c;
				} else if(t.containsAll(c)) {
					// do nothing
				} else if(!t.containsAny(c) && t.right + 1 != c.left) {
					// independent
					s.add(t);
					t = c;
				} else {
					t = new IntInterval(t.left, c.right);
				}
			}
		}
		s.add(t);
		return makeRange(s);
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#meet(net.morilib.range.integer.IntRange)
	 */
	public IntRange meet(IntRange r) {
		SortedSet<IntInterval> s = new TreeSet<IntInterval>();
		Iterator<IntInterval> i = intervals().iterator();
		Iterator<IntInterval> j = r.intervals().iterator();
		IntInterval t = null, a, b, c;

		if(cardinality() == 0 || r.cardinality() == 0) {
			return O;
		} else {
			a = i.hasNext() ? i.next() : null;
			b = j.hasNext() ? j.next() : null;
			while(a != null || b != null) {
				if(b == null) {
					c = a;
					a = i.hasNext() ? i.next() : null;
				} else if(a == null) {
					c = b;
					b = j.hasNext() ? j.next() : null;
				} else if(b == null || a.compareTo(b) < 0) {
					c = a;
					a = i.hasNext() ? i.next() : null;
				} else if(a == null || b.compareTo(a) < 0) {
					c = b;
					b = j.hasNext() ? j.next() : null;
				} else {
					c = a;
					a = i.hasNext() ? i.next() : null;
					b = j.hasNext() ? j.next() : null;
				}

				if(t == null) {
					t = c;
				} else if(t.containsAll(c)) {
					s.add(c);
				} else if(!t.containsAny(c)) {
					// independent
					t = c;
				} else {
					s.add(new IntInterval(c.left, t.right));
					t = c;
				}
			}
		}
		return makeRange(s);
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#difference(net.morilib.range.integer.IntRange)
	 */
	public IntRange difference(IntRange r) {
		return meet(r.complement());
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#xor(net.morilib.range.integer.IntRange)
	 */
	public IntRange xor(IntRange r) {
		return join(r).difference(meet(r));
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#complement()
	 */
	public IntRange complement() {
		SortedSet<IntInterval> s = new TreeSet<IntInterval>();
		int c = 0;

		if(cardinality() == 0) {
			return UINT;
		} else {
			for(IntInterval i : intervals()) {
				if(i.left > 0) {
					s.add(new IntInterval(c, i.left - 1));
				}
				c = (i.right == Integer.MAX_VALUE) ?
						Integer.MIN_VALUE : i.right + 1;
			}

			if(c != Integer.MIN_VALUE) {
				s.add(new IntInterval(c, Integer.MAX_VALUE));
			}
			return makeRange(s);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.range.integer.IntRange#wrappingRange()
	 */
	public Range wrappingRange() {
		RangeAdder ra = new RangeAdder();

		for(IntInterval i : intervals()) {
			ra.add(IntervalsInt.newClosedInterval(i.left, i.right));
		}
		return ra.toRange();
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Integer> iterator() {
		final Iterator<IntInterval> itr = intervals().iterator();
		final int[] c = new int[1];
		final IntInterval[] i = new IntInterval[1];

		if(itr.hasNext()) {
			i[0] = itr.next();
			c[0] = i[0].left;
			return new Iterator<Integer>() {

				public boolean hasNext() {
					return i[0] != null;
				}

				public Integer next() {
					int r = c[0];

					if(!hasNext()) {
						throw new NoSuchElementException();
					} else if(c[0] < i[0].right) {
						c[0]++;
					} else if(itr.hasNext()) {
						i[0] = itr.next();
						c[0] = i[0].left;
					} else {
						i[0] = null;
					}
					return r;
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		} else {
			return new Iterator<Integer>() {

				public boolean hasNext() {
					return false;
				}

				public Integer next() {
					throw new NoSuchElementException();
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int r = Hashes.INIT;

		for(IntInterval i : intervals()) {
			r = Hashes.A * (r + i.hashCode());
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof IntRange) {
			return intervals().equals(((IntRange)o).intervals());
		}
		return false;
	}

}
