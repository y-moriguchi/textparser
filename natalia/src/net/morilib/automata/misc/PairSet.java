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
package net.morilib.automata.misc;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2010/09/12
 */
public class PairSet<E> extends AbstractSet<E> {

	//
	private E e1, e2;

	//
	static boolean _equals(Object obj1, Object obj2) {
		return (obj1 == null) ? obj2 == null : obj1.equals(obj2);
	}

	/**
	 * 
	 * @param e1
	 * @param e2
	 */
	public PairSet(E e1, E e2) {
		if(_equals(e1, e2)) {
			throw new IllegalArgumentException();
		}
		this.e1 = e1;
		this.e2 = e2;
	}

	/**
	 * 
	 * @param <E>
	 * @param element1
	 * @param element2
	 * @return
	 */
	public static<E> Set<E> newPair(E element1, E element2) {
		if(_equals(element1, element2)) {
			return Collections.singleton(element1);
		}
		return new PairSet<E>(element1, element2);
	}

	/**
	 * 
	 * @return
	 */
	public E get1() {
		return e1;
	}

	/**
	 * 
	 * @return
	 */
	public E get2() {
		return e2;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public E getOpposite(Object e) {
		if(_equals(e1, e)) {
			return e2;
		} else if(_equals(e2, e)) {
			return e1;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isPair(Object a, Object b) {
		return ((_equals(e1, a) && _equals(e2, b)) ||
				(_equals(e2, a) && _equals(e1, b)));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {

			private int cnt = 0;

			public boolean hasNext() {
				return cnt < 2;
			}

			public E next() {
				E res;

				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				res = (cnt == 0) ? e1 : e2;
				cnt++;
				return res;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return _equals(o, e1) || _equals(o, e2);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return 2;
	}

}
