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
package net.morilib.automata.nfa;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import net.morilib.natalia.range.Interval;

/*package*/ final class RangeTree implements Iterable<Interval> {
	
	//
	private RangeTree root;
	private RangeTree left, right;
	private Interval value;
	
	
	private static class Itr implements Iterator<Interval> {
		
		private RangeTree next;
		
		private Itr(RangeTree ptr) {
			next = first(ptr);
		}
		
		public boolean hasNext() {
			return next != null;
		}

		public Interval next() {
			Interval res = next.value;
			
			if(next == null) {
				throw new NoSuchElementException();
			} else if(next.right != null) {
				next = first(next.right);
			} else {
				RangeTree t = next;
				
				next = next.root;
				while(next != null && t == next.right) {
					t = next;
					next = next.root;
				}
			}
			return res;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	
	public RangeTree(Interval val) {
		value = val;
	}
	
	private RangeTree(Interval val, RangeTree p) {
		value = val;
		root  = p;
	}
	
	private static RangeTree first(RangeTree r) {
		RangeTree l = r;
		
		if(l == null) {
			return null;
		} else {
			while(l.left != null) {
				l = l.left;
			}
			return l;
		}
	}
	
	private static Interval _find0(Interval o, RangeTree e) {
		if(e == null) {
			return null;
		} else {
			int cm = e.value.compareTo(o);
			
			if(cm > 0) {
				return _find0(o, e.right);
			} else if(cm < 0) {
				return _find0(o, e.left);
			} else {
				return e.value;
			}
		}
	}
	
	private static void _add2(Interval i, RangeTree e) {
		if(!i.isEmpty()) {
			int cmp = i.compareTo(e.value);
			
			if(cmp < 0) {
				if(e.left == null) {
					e.left = new RangeTree(i, e);
				} else {
					_add1(i, e.left);
				}
			} else if(cmp > 0) {
				if(e.right == null) {
					e.right = new RangeTree(i, e);
				} else {
					_add1(i, e.right);
				}
			}
		}
	}
	
	private static void _add1(Interval o, RangeTree e) {
		if(e.value.in(o)) {
			Set<Interval> s = e.value.complement(o).intervals();
			
			for(Interval i : s) {
				_add2(i, e);
			}
		} else if(o.in(e.value)) {
			Set<Interval> s = o.complement(e.value).intervals();
			
			e.value = o;
			for(Interval i : s) {
				_add2(i, e);
			}
		} else if(o.independentOf(e.value)) {
			_add2(o, e);
		} else {
			Interval is = e.value.meetInterval(o);
			SortedSet<Interval> s1 = e.value.complementIntervals(is);
			SortedSet<Interval> s2 = o.complementIntervals(is);
			
			e.value = is;
			for(Interval i : s1) {
				_add2(i, e);
			}
			for(Interval i : s2) {
				_add2(i, e);
			}
		}
		
		
		
	}
	
	
	public Interval find(Interval o) {
		return _find0(o, this);
	}


	public void delete(Interval o) {
		throw new UnsupportedOperationException();
	}


	public void insert(Interval o) {
		_add1(o, this);
	}

	public Iterator<Interval> iterator() {
		return new Itr(this);
	}
	
}
