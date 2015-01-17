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

import java.util.List;

/**
 * This interface represents the stack.
 * <p>スタックを表すインターフェースである.
 * 
 * @author MORIGUCHI, Yuichiro 2005/03/03
 */
public interface Stack2<E> {
	
	/**
	 * pushs the object.
	 * <p>オブジェクトをプッシュする.
	 * 
	 * @param o  the object to be pushed
	 * @return   the pushed object
	 */
	public E push(E o);
	
	/**
	 * pops the object from this stack,
	 * the popped object is removed from this stack.
	 * <p>オブジェクトをポップする.
	 * ポップされたオブジェクトは削除される.
	 * 
	 * @return  the popped object
	 */
	public E pop();
	
	/**
	 * gets the object at the top of this stack,
	 * the object is not removed from this.
	 * <p>スタックの先頭にあるオブジェクトを得る.
	 * 
	 * @return  the top object of this stack
	 */
	public E peek();
	
	/**
	 * gets the object which was pushed.
	 * <p>スタックの底からn番目にあるオブジェクトを取得する.
	 * 
	 * @return  the distance from the bottom of this stack
	 */
	public E get(int n);
	
	/**
	 * returns true if this stack is empty.
	 * <p>スタックが空のときtrueを得る.
	 */
	public boolean isEmpty();
	
	/**
	 * pops n objects from this stack and removes the objects
	 * from this stack.
	 * <p>スタックからn個のオブジェクトをポップし、削除する.
	 * 
	 * @param n
	 */
	public void pop(int n);
	
	/**
	 * returns the size of this stack.
	 * <p>サイズを返す.
	 * 
	 * @return  the size of this stack
	 */
	public int size();
	
	/**
	 * clears all elements in this stack.
	 * <p>スタックにある要素をクリアする.
	 */
	public void clear();
	
	/**
	 * returns true if the given object is in this stack.
	 * <p>与えられたオブジェクトがスタックにあるときtrueを得る.
	 * 
	 * @param o  the object
	 * @return  true if the given object is in this stack
	 */
	public boolean contains(E o);
	
	/**
	 * converts this stack to list,
	 * the end of the list is top of this stack.
	 * <p>このスタックの内容をリストに変換します。
	 * リストの末尾がスタックの先頭になります。
	 */
	public List<E> toList();
	
	/**
	 * add the given stack to top of this stack.
	 * <p>スタックの先頭に与えられたスタックを追加します。
	 */
	public void addAll(Stack2<E> s);
	
	/**
	 * add the given element to top of this stack.
	 * <p>スタックの先頭に与えられた要素を追加します。
	 */
	public boolean add(E o);

}
