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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <i>USEful Implements</i> for sets.<br>
 * 集合に関する便利な関数である.
 * 
 * @author MORIGUCHI, Yuichiro 2005/09/21
 */
final class InternalUtils {

	//
	private InternalUtils() {}

	/**
	 * An empty array of Object.
	 * <p>空のObject型配列である.
	 */
	public static final Object[] OBJECT_EMPTY = new Object[0];

	/**
	 * Returns true if the given objects are equal.<br>
	 * <p>与えられた引数が等しいときにtrueを得る.
	 * 
	 * @return  true if obj1 equals obj2, otherwise false
	 */
	public static /*inline*/ boolean equals(Object obj1, Object obj2) {
		return (obj1 == null) ? obj2 == null : obj1.equals(obj2);
	}

	/**
	 * compares the given objects each other with the given Comparator.
	 * <p>与えられたComparatorを使用してオブジェクトを比較する.
	 * 
	 * @param c  a comparator
	 * @see java.lang.Comparable
	 * @see java.util.Comparator
	 */
	public static<T> int compare(T o, T p, Comparator<T> c) {
		//return (c == null) ? compare(o, p) : c.compare(o, p);
		return c.compare(o, p);
	}
	
	/**
	 * returns true if the first argument is between the second
	 * argument and the third; the given comparator is used for compare.
	 * <p>第1の引数が第2の引数と第3の引数の間にあればtrueとする.
	 * 比較には引数のComparatorを使用する.
	 * 
	 * @param src   the first argument
	 * @param from  the second argument
	 * @param to    the third argument
	 * @param comp  the comparator to be used
	 */
	public static<T> boolean between(
			T src, T from, T to, Comparator<T> comp) {
		return (compare(src, from, comp) >= 0 &&
				compare(src, to,   comp) <= 0);
	}

	/**
	 * a empty list iterator.
	 * <p>空のListIteratorである.
	 */
	public static final ListIterator<Object> NULL =
		new ListIterator<Object>() {

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return false;
		}

		public Object next() {
			throw new NoSuchElementException();
		}

		public int nextIndex() {
			return 0;
		}

		public int previousIndex() {
			return -1;
		}

		public boolean hasPrevious() {
			return false;
		}

		public Object previous() {
			throw new NoSuchElementException();
		}

		public void add(Object o) {
			throw new UnsupportedOperationException();
		}

		public void set(Object o) {
			throw new UnsupportedOperationException();
		}

	};

	/**
	 * gets an empty iterator.
	 * <p>空のiteratorを得る。
	 * 
	 * @return an empty iterator
	 */
	@SuppressWarnings("unchecked")
	public static<E> ListIterator<E> emptyIterator() {
		return (ListIterator<E>)NULL;
	}

	/**
	 * a empty sorted set.
	 * <p>空のSortedSetである.
	 */
	public static class EmptySSet implements SortedSet<Object> {

		public Object first() {
			throw new NoSuchElementException();
		}

		public Object last() {
			throw new NoSuchElementException();
		}

		public Comparator<Object> comparator() {
			return null;
		}

		public SortedSet<Object> headSet(Object toElement) {
			return this;
		}

		public SortedSet<Object> tailSet(Object fromElement) {
			return this;
		}

		public SortedSet<Object> subSet(
				Object fromElement, Object toElement) {
			return this;
		}

		public int size() {
			return 0;
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public boolean isEmpty() {
			return true;
		}

		public Object[] toArray() {
			return OBJECT_EMPTY;
		}

		public boolean add(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean contains(Object o) {
			return false;
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public boolean containsAll(Collection<?> c) {
			return c.isEmpty();
		}

		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public Iterator<Object> iterator() {
			return emptyIterator();
		}

		public<T> T[] toArray(T[] a) {
			return a;
		}

		public boolean equals(Object o) {
			if(o instanceof SortedSet<?>) {
				return ((Collection<?>)o).isEmpty();
			}
			return false;
		}

		public int hashCode() {
			// 
			return 0;
		}

		public String toString() {
			return "[]";
		}

	};

	//
	private static SortedSet<Object> EMPTY = new EmptySSet();

	/**
	 * returns an empty SortedSet.
	 * <p>空のSortedSetを得ます。
	 */
	@SuppressWarnings("unchecked")
	public static<T> SortedSet<T> emptySortedSet() {
		return (SortedSet<T>)EMPTY;
	}

	//
	private static class Unmodify<E> implements Iterator<E> {

		//
		Iterator<E> iter;

		//
		Unmodify(Iterator<E> iter) {
			this.iter = iter;
		}

		//
		public void remove() {
			throw new UnsupportedOperationException();
		}

		//
		public boolean hasNext() {
			return iter.hasNext();
		}

		//
		public E next() {
			return iter.next();
		}

	};

	/**
	 * gets the unmodifiable iterator of the given iterator.
	 * <p>引数のIteratorを変更不可にしたものを得る.
	 * 
	 * @param i  the iterator
	 * @return  the unmodifiable iterator
	 */
	public static<E> Iterator<E> unmodifiable(Iterator<E> i) {
		return new Unmodify<E>(i);
	}

	//
	private static class Unmodify1<E> implements SortedSet<E> {

		//
		SortedSet<E> wrapee;

		//
		Unmodify1(SortedSet<E> w) {
			wrapee = w;
		}

		//
		public E first() {
			return wrapee.first();
		}

		//
		public E last() {
			return wrapee.last();
		}

		//
		public Comparator<? super E> comparator() {
			return wrapee.comparator();
		}

		//
		public SortedSet<E> headSet(E toElement) {
			return wrapee.headSet(toElement);
		}

		//
		public SortedSet<E> tailSet(E fromElement) {
			return wrapee.tailSet(fromElement);
		}

		//
		public SortedSet<E> subSet(E fromElement, E toElement) {
			return new Unmodify1<E>(
					wrapee.subSet(fromElement, toElement));
		}

		//
		public int size() {
			return wrapee.size();
		}

		//
		public void clear() {
			throw new UnsupportedOperationException();
		}

		//
		public boolean isEmpty() {
			return wrapee.isEmpty();
		}

		//
		public Object[] toArray() {
			return wrapee.toArray();
		}

		//
		public boolean add(Object o) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean contains(Object o) {
			return wrapee.contains(o);
		}

		//
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean addAll(Collection<? extends E> c) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean containsAll(Collection<?> c) {
			return wrapee.containsAll(c);
		}

		//
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		//
		public Iterator<E> iterator() {
			return unmodifiable(wrapee.iterator());
		}

		//
		public<T> T[] toArray(T[] a) {
			return wrapee.toArray(a);
		}

		public boolean equals(Object o) {
			return wrapee.equals(o);
		}

		public int hashCode() {
			return wrapee.hashCode();
		}

		public String toString() {
			return wrapee.toString();
		}

	};

	//
	private static class Single<E> implements Iterator<E> {

		//
		private E obj;

		//
		Single(E o) {
			obj = o;
		}

		//
		public void remove() {
			throw new UnsupportedOperationException();
		}

		//
		public boolean hasNext() {
			return obj != null;
		}

		//
		public E next() {
			E o = obj;

			if(o == null) {
				throw new NoSuchElementException();
			}
			obj = null;
			return o;
		}

	};

	/**
	 * gets the singleton iterator which has only the given object.
	 * <p>引数のオブジェクトのみ所有するIteratorを得る.
	 * 
	 * @param o  the object
	 * @return  the singleton iterator
	 */
	public static<E> Iterator<E> singleton(E o) {
		return new Single<E>(o);
	}

	//
	private static class Singleton<E> implements SortedSet<E> {

		//
		private E             value;
		private Comparator<E> comp;

		//
		Singleton(E o, Comparator<E> c) {
			value = o;
			comp  = c;
		}

		//
		public E first() {
			return value;
		}

		//
		public E last() {
			return value;
		}

		//
		public Comparator<? super E> comparator() {
			return comp;
		}

		//
		@SuppressWarnings("unchecked")
		public SortedSet<E> headSet(E e) {
			return (compare(value, e, comp) <= 0) ?
					this : (SortedSet<E>)EMPTY;
		}

		//
		@SuppressWarnings("unchecked")
		public SortedSet<E> tailSet(E e) {
			return (compare(value, e, comp) >= 0) ?
					this : (SortedSet<E>)EMPTY;
		}

		//
		@SuppressWarnings("unchecked")
		public SortedSet<E> subSet(E from, E to) {
			return between(value, from, to, comp) ?
					this : (SortedSet<E>)EMPTY;
		}

		//
		public int size() {
			return 1;
		}

		//
		public void clear() {
			throw new UnsupportedOperationException();
		}

		//
		public boolean isEmpty() {
			return false;
		}

		//
		public Object[] toArray() {
			return new Object[] { value };
		}

		//
		public boolean add(Object o) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean contains(Object o) {
			return InternalUtils.equals(value, o);
		}

		//
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean addAll(Collection<? extends E> c) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean containsAll(Collection<?> c) {
			return c.isEmpty() || (c.size() == 1 && c.contains(value));
		}

		//
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		//
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		//
		public Iterator<E> iterator() {
			return singleton(value);
		}

		//
		@SuppressWarnings("unchecked")
		public<T> T[] toArray(T[] a) {
			try {
				a[0] = (T)value;
			} catch(ClassCastException e) {
				throw new ArrayStoreException();
			}
			return a;
		}

		public boolean equals(Object o) {
			if(o instanceof Collection<?>) {
				Collection<?> s = (Collection<?>)o;
				return s.size() == 1 && s.contains(value);
			}
			return false;
		}

		public int hashCode() {
			return 37 * (value.hashCode() + 17);
		}

		public String toString() {
			return "[" + value.toString() + "]";
		}

	};

	/**
	 * gets the unmodifiable set of the given set.
	 * <p>引数の集合を変更不可にしたものを得る.
	 * 
	 * @param wrapee  the set to be wrapped
	 * @return  the unmodifiable set
	 */
	public static<E> Set<E> unmodifiable(Set<E> wrapee) {
		return (wrapee == null) ?
				null : Collections.unmodifiableSet(wrapee);
	}

	/**
	 * gets the unmodifiable sorted set of the given sorted set.
	 * <p>引数のSortedSetを変更不可にしたものを得る.
	 * 
	 * @param wrapee  the sorted set to be wrapped
	 * @return  the unmodifiable sorted set
	 */
	public static<E> SortedSet<E> sortedUnmodifiable(
			SortedSet<E> wrapee) {
		return (wrapee == null) ? null : new Unmodify1<E>(wrapee);
	}

	/**
	 * gets the singleton sorted set which has only the given object.
	 * <p>引数のオブジェクトのみもつSortedSetを得る.
	 * 
	 * @param o  the object
	 * @return  the singleton sorted set
	 */
	public static<E> SortedSet<E> sortedSingleton(E o) {
		return new Singleton<E>(o, null);
	}

	/**
	 * gets the singleton sorted set which has only the given object.
	 * <p>引数のオブジェクトのみもつSortedSetを得る.
	 * 
	 * @param o  the object
	 * @return  the singleton sorted set
	 */
	public static<E> SortedSet<E> sortedSingleton(
			E o, Comparator<E> comp) {
		return new Singleton<E>(o, comp);
	}

	/**
	 * returns true if the given sets is independent each other.
	 * <p>引数のオブジェクトが互いに独立であるときtrueを得る.
	 * 
	 * @param a  the set
	 * @param b  another set
	 * @return  true if the given sets is independent each other
	 */
	public static<E> boolean isIndependent(Set<E> a, Set<E> b) {
		int diff = a.size() - b.size();
		Set<E> l = (diff > 0) ? a : b;
		Set<E> s = (diff > 0) ? b : a;

		for(E o : s) {
			if(l.contains(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param <E>
	 * @param iterator
	 * @return
	 */
	public static<E> Set<E> toSet(Iterator<E> iterator) {
		Set<E> r = new HashSet<E>();

		while(iterator.hasNext()) {
			r.add(iterator.next());
		}
		return r;
	}

	/**
	 * 
	 * @param <E>
	 * @param iterator
	 * @return
	 */
	public static<E> SortedSet<E> toSortedSet(Iterator<E> iterator) {
		SortedSet<E> r = new TreeSet<E>();

		while(iterator.hasNext()) {
			r.add(iterator.next());
		}
		return r;
	}

	/**
	 * returns the string representation of the given iterator.
	 * <p>引数のIteratorの文字列表現を得る.
	 * 
	 * @param i  the iterator
	 * @param delim  the delimiter which separates the elements
	 * @return  the string representation of the array
	 */
	public static<E> String toString(Iterator<E> i, String delim) {
		StringBuffer buf = new StringBuffer();
		String d2 = "";

		while(i.hasNext()) {
			buf.append(d2);
			buf.append(i.next() + "");
			d2 = delim;
		}
		return buf.toString();		
	}

}
