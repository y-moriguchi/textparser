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
package net.morilib.automata.misc;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.morilib.range.Section;

public final class TreeSectionMap
<T extends Comparable<T> & Section<K>, K, V>
implements SimpleMap<K, V> {

	//
	private TreeSectionMap<T, K, V> left, right, root;
	private T key;
	private V value;
//	private int height = 0;
	//private Comparator<T> cmper = null;

	//
	private static class Itr
	<T extends Comparable<T> & Section<K>, K, V> implements Iterator<T> {

		private TreeSectionMap<T, K, V> next;

		private Itr(TreeSectionMap<T, K, V> ptr) {
			next = first(ptr);
		}

		public boolean hasNext() {
			return next != null;
		}

		public T next() {
			T res = next.key;

			if(next == null) {
				throw new NoSuchElementException();
			} else if(next.right != null) {
				next = first(next.right);
			} else {
				TreeSectionMap<T, K, V> t = next;

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

	private static class ItrE
	<T extends Comparable<T> & Section<K>, K, V>
	implements Iterator<Tuple2<T, V>> {

		private TreeSectionMap<T, K, V> next;

		private ItrE(TreeSectionMap<T, K, V> ptr) {
			next = first(ptr);
		}

		public boolean hasNext() {
			return next != null;
		}

		public Tuple2<T, V> next() {
			T res = next.key;
			V rev = next.value;

			if(next == null) {
				throw new NoSuchElementException();
			} else if(next.right != null) {
				next = first(next.right);
			} else {
				TreeSectionMap<T, K, V> t = next;

				next = next.root;
				while(next != null && t == next.right) {
					t = next;
					next = next.root;
				}
			}
			return new Tuple2<T, V>(res, rev);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private class ItrbK implements Iterable<T> {

		public Iterator<T> iterator() {
			return new Itr<T, K, V>(TreeSectionMap.this);
		}

	}

	private class ItrbE implements Iterable<Tuple2<T, V>> {

		public Iterator<Tuple2<T, V>> iterator() {
			return new ItrE<T, K, V>(TreeSectionMap.this);
		}

	}

	private final ItrbK ITR_K = new ItrbK();
	private final ItrbE ITR_E = new ItrbE();

	/**
	 * 
	 * @param key
	 * @param val
	 */
	public TreeSectionMap(T key, V val) {
		this.key   = key;
		this.value = val;
		this.root  = null;
	}

	//
	private TreeSectionMap(
			T key, TreeSectionMap<T, K, V> root, V val) {
		this.key   = key;
		this.value = val;
		this.root  = root;
		//this.cmper = root.cmper;
	}

	private static<T extends Comparable<T> & Section<K>, K, V>
	TreeSectionMap<T, K, V> _find0(K o, TreeSectionMap<T, K, V> e) {
		if(e == null) {
			return null;
		} else {
			int cm = e.key.side(o);

			if(cm > 0) {
				return _find0(o, e.right);
			} else if(cm < 0) {
				return _find0(o, e.left);
			} else {
				return e;
			}
		}
	}

	private static<T extends Comparable<T> & Section<K>, K, V>
	TreeSectionMap<T, K, V> _find1(T o, TreeSectionMap<T, K, V> e) {
		if(e == null) {
			return null;
		} else {
			int cm = e.key.compareTo(o);

			if(cm > 0) {
				return _find1(o, e.right);
			} else if(cm < 0) {
				return _find1(o, e.left);
			} else {
				return e;
			}
		}
	}

//	private void rotateLeft() {
//		T tmp;
//		TreeSectionMap<T, K, V> tt;
//
//		if(left == null) {
//			tmp = right.key;
//			tt = right;
//			right = right.right;
//			tt.key = key;
//			tt.right = tt.left;
//			key = tmp;
//			left = tt;
//		} else {
//			// swap key
//			tmp = left.key;
//			left.key = key;
//			key = tmp;
//
//			// rotate tree
//			tt = left.left;
//			left.left = left.right;
//			left.right = right;
//			right = left;
//			left = tt;
//		}
//
//		// height
//		height -= 2;
//	}

//	private void rotateRight() {
//		T tmp;
//		TreeSectionMap<T, K, V> tt;
//
//		if(right == null) {
//			tmp = left.key;
//			tt = left;
//			left = left.left;
//			tt.key = key;
//			tt.left = tt.right;
//			key = tmp;
//			right = tt;
//		} else {
//			// swap key
//			tmp = right.key;
//			right.key = key;
//			key = tmp;
//
//			// rotate tree
//			tt = right.right;
//			right.right = right.left;
//			right.left = left;
//			left = right;
//			right = tt;
//		}
//
//		// height
//		height += 2;
//	}

//	private void doubleRotateLeft() {
//		right.rotateRight();
//		rotateLeft();
//	}


//	private void doubleRotateRight() {
//		left.rotateLeft();
//		rotateRight();
//	}

	private static<T extends Comparable<T> & Section<K>, K, V>
	TreeSectionMap<T, K, V> first(TreeSectionMap<T, K, V> r) {
		TreeSectionMap<T, K, V> l = r;

		if(l == null) {
			return null;
		} else {
			while(l.left != null) {
				l = l.left;
			}
			return l;
		}
	}

//	private static int abs(int x) {
//		return (x < 0) ? -x : x;
//	}

	private static<T extends Comparable<T> & Section<K>, K, V>
	T _add1(T o, TreeSectionMap<T, K, V> e, V val) {
		int cm = e.key.compareTo(o);
		T res;

		if(cm > 0) {
			if(e.left == null) {
				e.left  = new TreeSectionMap<T, K, V>(o, e, val);
//				e.height--;
				res = null;
			} else {
				res = _add1(o, e.left, val);
			}
		} else if(cm < 0) {
			if(e.right == null) {
				e.right = new TreeSectionMap<T, K, V>(o, e, val);
//				e.height++;
				res = null;
			} else {
				res = _add1(o, e.right, val);
			}
		} else {
			e.value = val;
			return e.key;
		}

		// adjust tree
//		int hl = (e.left  == null) ? 0 : e.left.height;
//		int hr = (e.right == null) ? 0 : e.right.height;
//		if(abs(hr) + abs(hl) >= 2) {
//			if(hr > 0) {
//				e.rotateLeft();
//			} else if(hr < 0) {
//				e.doubleRotateLeft();
//			}
//		} else if(abs(hl) + abs(hr) <= 2) {
//			if(hl < 0) {
//				e.rotateRight();
//			} else if(hl > 0) {
//				e.doubleRotateRight();
//			}
//		}
		return res;
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	public T find(T o) {
		TreeSectionMap<T, K, V> res = _find1(o, this);

		return (res == null) ? null : res.key;
	}

	/**
	 * 
	 * @param o
	 */
	public void delete(T o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param o
	 * @param val
	 */
	public void insert(T o, V val) {
		_add1(o, this, val);
	}

	/**
	 * 
	 * @return
	 */
	public Iterable<T> keys() {
		return ITR_K;
	}

	/**
	 * 
	 * @return
	 */
	public Iterable<Tuple2<T, V>> entries() {
		return ITR_E;
	}

	/* (non-Javadoc)
	 * @see net.morilib.util.SimpleMap#map(java.lang.Object)
	 */
	public V map(K key) {
		TreeSectionMap<T, K, V> res = _find0(key, this);

		return (res == null) ? null : res.value;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public V map(T key) {
		TreeSectionMap<T, K, V> res = _find1(key, this);

		return (res == null) ? null : res.value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		String dlm = "";

		buf.append("{");
		for(Tuple2<T, V> k : ITR_E) {
			buf.append(dlm);
			buf.append(k.getA() + "");
			buf.append("=");
			buf.append(k.getB() + "");
			dlm = ",";
		}
		buf.append("}");
		return buf.toString();
	}

}
