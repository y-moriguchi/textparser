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

import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implimentation of stack by LinkedList.
 * <p>LinkedListを使用したスタックの実装である.
 * 
 * @author MORIGUCHI, Yuichiro 2005/03/03
 */
class LinkedListStack<E> implements Stack2<E> {
	
	//
	/*package*/ List<E> list;
	
	/**
	 * constructs a new ListStack.
	 * <p>新しくスタックを生成する.
	 */
	public LinkedListStack() {
		list = new LinkedList<E>();
	}
	
	/**
	 * constructs a new ListStack and initializes the given collection.
	 * <p>新しくスタックを作成し、与えられたコレクションで初期化する.
	 * 
	 * @param col  a collection
	 */
	public LinkedListStack(Collection<E> col) {
		list = new LinkedList<E>();
		
		Iterator<E> i = col.iterator();
		while(i.hasNext()) {
			list.add(i.next());
		}
	}
	
	/**
	 * pushs the object.
	 * <p>オブジェクトをプッシュする.
	 * 
	 * @param o  the object to be pushed
	 * @return   the pushed object
	 */
	public E push(E o) {
		list.add(o);
		return o;
	}

	/**
	 * pops the object from this stack,
	 * the popped object is removed from this stack.
	 * <p>オブジェクトをポップする.
	 * ポップされたオブジェクトは削除される.
	 * 
	 * @return  the popped object
	 */
	public E pop() {
		if(list.size() <= 0) {
			throw new EmptyStackException();
		}
		return list.remove(list.size() - 1);
	}

	/**
	 * gets the top object of this stack,
	 * the object is not removed from this.
	 * <p>スタックの先頭にあるオブジェクトを得る.
	 * 
	 * @return  the top object of this stack
	 */
	public E peek() {
		if(list.size() <= 0) {
			throw new EmptyStackException();
		}
		return list.get(list.size() - 1);
	}

	/**
	 * returns true if this stack is empty.
	 * <p>スタックが空のときtrueを得る.
	 */
	public boolean isEmpty() {
		return (list.size() <= 0);
	}

	/**
	 * pops n objects from this stack and removes the objects
	 * from this stack.
	 * <p>スタックからn個のオブジェクトをポップし、削除する.
	 * 
	 * @param n
	 */
	public void pop(int n) {
		if(list.size() < n) {
			throw new IndexOutOfBoundsException();
		}
		
		for(; n > 0; n--) {
			pop();
		}
	}

	/**
	 * returns the size of this stack.
	 * <p>サイズを返す.
	 * 
	 * @return  the size of this stack
	 */
	public int size() {
		return list.size();
	}

	/**
	 * clears all elements in this stack.
	 * <p>スタックにある要素をクリアする.
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * returns true if the given object is in this stack.
	 * <p>与えられたオブジェクトがスタックにあるときtrueを得る.
	 * 
	 * @param o  the object
	 * @return  true if the given object is in this stack
	 */
	public boolean contains(E o) {
		return list.contains(o);
	}

	/**
	 * gets the object which was pushed.
	 * <p>n+1番目にプッシュしたオブジェクトを取得する.
	 * 
	 * @return  the distance from the top of this stack
	 */
	public E get(int n) {
		if(list.size() <= n) {
			throw new IndexOutOfBoundsException();
		}
		return list.get(n);
	}
	
	/**
	 * converts this stack to list,
	 * the end of the list is top of this stack.
	 * <p>このスタックの内容をリストに変換します。
	 * リストの末尾がスタックの先頭になります。
	 * 
	 * @see net.morilib.util.Stack2#toList()
	 */
	public List<E> toList() {
		return Collections.unmodifiableList(list);
	}
	
	/**
	 * add the given element to top of this stack.
	 * <p>スタックの先頭に与えられた要素を追加します。
	 * 
	 * @see net.morilib.util.Stack2#add(java.lang.Object)
	 */
	public boolean add(E o) {
		return list.add(o);
	}
	
	/**
	 * add the given stack to top of this stack.
	 * <p>スタックの先頭に与えられたスタックを追加します。
	 * 
	 * @see net.morilib.util.Stack2#addAll(net.morilib.util.Stack2)
	 */
	public void addAll(Stack2<E> s) {
		list.addAll(s.toList());
	}
	
	/**
	 * returns the string representation.
	 * <p>このオブジェクトの文字列表現を取得する.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		Iterator<E> itr = list.iterator();
		StringBuffer buf = new StringBuffer();
		
		while(itr.hasNext()) {
			buf.append(itr.next()).append(" ");
		}
		return buf.toString();
	}

}
