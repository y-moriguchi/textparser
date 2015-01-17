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
package net.morilib.automata;

import java.util.Set;

import net.morilib.range.Interval;

/**
 * 
 *
 *
 * @author MORIGUCHI, Yuichiro 2009
 */
public interface DFAState<T, A, B> extends NFAState {

	/**
	 * 
	 * @param alphabet
	 * @return
	 */
	public DFAState<T, A, B> go(T alphabet);

	/**
	 * 
	 * @param x
	 * @return
	 */
	public DFAState<T, A, B> goInt(int x);

	/**
	 * 
	 * @param x
	 * @return
	 */
	public DFAState<T, A, B> goChar(char x);

	/**
	 * 
	 * @param bound
	 * @return
	 */
	public DFAState<T, A, B> goBound(TextBound bound);

	/**
	 * 
	 * @param o
	 * @return
	 */
	public boolean isInitialState();

	/**
	 * 
	 * @param o
	 * @return
	 */
	public Set<A> getAccepted();

	/**
	 * 
	 * @return
	 */
	public boolean isDead();

	/**
	 * 
	 * @return
	 */
	public boolean isAccepted();

	/**
	 * 
	 * @return
	 */
	public Set<T> getAlphabets();

	/**
	 * 
	 * @return
	 */
	public Iterable<Interval> getAlphabetRanges();

	/**
	 * 
	 * @param alphabet
	 * @return
	 */
	public Object getLabel(T alphabet);

	/**
	 * 
	 * @param x
	 * @return
	 */
	public Object getLabelInt(int x);

	/**
	 * 
	 * @param x
	 * @return
	 */
	public Object getLabelChar(char x);

}
