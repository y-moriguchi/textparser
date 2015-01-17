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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a BufferedIterator which can keep one element.
 * <p>1つの要素を保存するBufferedIteratorである.
 * 
 * @author MORIGUCHI, Yuichiro 2005/05/29
 */
public class WrappedLookaheadIterator<E> implements LookaheadIterator<E> {
	
	//
	private static final Object END = new Object();
	
	//
	private Iterator<E> iterator;
	private Object now = null;
	
	/**
	 * creates a new instance.
	 * <p>インスタンスを生成する.
	 * 
	 * @param iter  an iterator to be wrapped
	 */
	public WrappedLookaheadIterator(Iterator<E> iter) {
		if(iter == null) {
			throw new NullPointerException();
		}
		iterator = iter;
		lookahead();
	}
	
	/**
	 * creates a new instance.
	 * <p>インスタンスを生成する.
	 * 
	 * @param col  a collection, whose iterator is wrapped
	 */
	public WrappedLookaheadIterator(Iterable<E> col) {
		if(col == null) {
			throw new NullPointerException();
		}
		iterator = col.iterator();
		lookahead();
	}
	
	//
	/*package*/ void lookahead() {
		now = iterator.hasNext() ? iterator.next() : END;
	}

	/**
	 * gets the element pointed by this iterator.
	 * <p>Iteratorの指し示す現在の要素を得る.
	 */
	@SuppressWarnings("unchecked")
	public E peek() {
		if(now == END) {
			throw new NoSuchElementException();
		} else {
			return (E)now;
		}
	}

	/**
	 * returns true if this iterator has a next element.
	 * <p>Iteratorが次の要素を持つときにtrueを得る.
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return now != END;
	}

	/**
	 * gets the value pointed by this iterator and forwards the pointer of
	 * this.
	 * <p>Iteratorにより示されている値を得て、ポインタを進める.
	 * 
	 * @see java.util.Iterator#next()
	 */
	@SuppressWarnings("unchecked")
	public E next() {
		Object res = now;
		
		if(now == END) {
			throw new NoSuchElementException();
		} else {
			lookahead();
			return (E)res;
		}
	}

	/**
	 * removes the element which is pointed by this iterator.
	 * <p>Iteratorが指し示す要素を削除する.
	 * このメソッドはサポートされていない.
	 * 
	 * @see java.util.Iterator#remove()
	 * @throws UnsupportedOperationException
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
