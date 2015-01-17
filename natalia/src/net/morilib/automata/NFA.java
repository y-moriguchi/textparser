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

import java.util.EnumSet;
import java.util.Set;

import net.morilib.automata.TextBound;
import net.morilib.range.Interval;
import net.morilib.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/21
 */
public interface NFA<T, A, B> {

	/**
	 * 
	 * @param o
	 * @return
	 */
	public boolean isState(NFAState o);

	/**
	 * 
	 * @param state
	 * @param alphabet
	 * @return
	 */
	public Set<NFAState> getStates(NFAState state, T alphabet);

	/**
	 * 
	 * @param state
	 * @param rng
	 * @return
	 */
	public Set<NFAState> getStates(NFAState state, Range rng);

	/**
	 * 
	 * @param state
	 * @param bound
	 * @return
	 */
	public Set<NFAState> getStates(
			NFAState state, EnumSet<TextBound> bound);

	/**
	 * 
	 * @param state
	 * @return
	 */
	public Set<NFAState> getStatesEpsilon(NFAState state);

	/**
	 * 
	 * @param state
	 * @return
	 */
	public Set<NFAState> getStatesBound(
			NFAState state, EnumSet<TextBound> bound);

	/**
	 * 
	 */
	public Set<NFAState> getInitialStates();

	/**
	 * 
	 * @param o
	 * @return
	 */
	public boolean isInitialState(NFAState o);

	/**
	 * 
	 * @param state
	 * @return
	 */
	public boolean isFinal(NFAState state);

	/**
	 * 
	 * @param states
	 * @return
	 */
	public boolean isFinalAny(Set<NFAState> states);

	/**
	 * 
	 * @return
	 */
	public NFAEdges<T> getEdges(NFAState state);

	/**
	 * 
	 * @return
	 */
	public Set<Interval> nextAlphabets(NFAState state);

	/**
	 * 
	 * @param states
	 * @return
	 */
	public Iterable<Interval> nextAlphabets(Set<NFAState> states);

	/**
	 * 
	 * @return
	 */
	public Set<T> nextDiscreteAlphabets(NFAState state);

	/**
	 * 
	 * @param states
	 * @return
	 */
	public Iterable<T> nextDiscreteAlphabets(Set<NFAState> states);

	/**
	 * 
	 * @return
	 */
	public Set<NFAState> getAcceptedStates();

	/**
	 * 
	 * @param state
	 * @return
	 */
	public Set<B> getMatchTag(NFAState state);

	/**
	 * 
	 * @param state
	 * @return
	 */
	public Set<B> getMatchTagEnd(NFAState state);

	/**
	 * 
	 * @param state
	 * @return
	 */
	public Set<A> getAccept(NFAState state);

	/**
	 * 
	 * @param state
	 * @return
	 */
	public boolean isAccepted(NFAState state);

}
