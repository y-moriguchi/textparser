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

import net.morilib.natalia.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/07/13
 */
public interface NFAEdges<T> {

	/**
	 * 
	 * @param alphabet
	 * @return
	 */
	public Set<NFAState> goNext(T alphabet);

	/**
	 * 
	 * @param alphabet
	 * @return
	 */
	public Set<NFAState> goNext(int alphabet);

	/**
	 * 
	 * @param alphabet
	 * @return
	 */
	public Set<NFAState> goNext(char alphabet);

	/**
	 * 
	 * @return
	 */
	public Set<NFAState> goNextEpsilon();

	/**
	 * 
	 * @return
	 */
	public Set<? extends Range> nextAlphabets();

	/**
	 * 
	 * @return
	 */
	public boolean isNextEpsilon();

}
