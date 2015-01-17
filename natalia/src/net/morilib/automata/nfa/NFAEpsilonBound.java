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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import net.morilib.automata.NFAState;
import net.morilib.automata.TextBound;
import net.morilib.range.Interval;
import net.morilib.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/06/04
 */
public final class NFAEpsilonBound<T, A, B>
extends NFAObject<T, A, B> {

	//
	/*package*/ final NFAState beginState = new IntStateObject(this, 0);
	/*package*/ final NFAState endState   = new IntStateObject(this, 1);
	private TextBound aBound = null;

	//
	private NFAEpsilonBound(TextBound bound) {
		this.aBound = bound;
	}

	/**
	 * 
	 * @return
	 */
	public static<T, A, B> NFAEpsilonBound<T, A, B> newInstance() {
		return new NFAEpsilonBound<T, A, B>(null);
	}

	/**
	 * 
	 * @param bound
	 * @return
	 */
	public static<T, A, B> NFAEpsilonBound<T, A, B> newInstance(
			TextBound bound) {
		return new NFAEpsilonBound<T, A, B>(bound);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected NFAEpsilonBound<T, A, B> clone() {
		return newInstance(aBound);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#addStates(java.util.Set, java.lang.Object, java.lang.Object)
	 */
	/*package*/ void addStates(Set<NFAState> res, NFAState state, T a) {
		// do nothing
	}

	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, Range a) {
		// do nothing
	}

	/*package*/ void addStatesBound(
			Set<NFAState> res, NFAState state,
			EnumSet<TextBound> a) {
		if(a.contains(aBound) && state.equals(beginState)) {
			res.add(endState);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#addInitialStates(java.util.Set)
	 */
	/*package*/ void addInitialStates(Set<NFAState> res) {
		res.add(beginState);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#addStatesEpsilon(java.util.Set, java.lang.Object)
	 */
	/*package*/ void addStatesEpsilon(
			Set<NFAState> res, NFAState state) {
		if(aBound == null && state.equals(beginState)) {
			res.add(endState);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isState(java.lang.Object)
	 */
	public boolean isState(NFAState o) {
		if(o instanceof IntStateObject) {
			IntStateObject s = (IntStateObject)o;

			return s.equals(beginState) || s.equals(endState);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isInitialState(java.lang.Object)
	 */
	public boolean isInitialState(NFAState o) {
		return beginState.equals(o);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isAccepted(java.lang.Object)
	 */
	public boolean isFinal(NFAState state) {
		return endState.equals(state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets(java.lang.Object)
	 */
	public Set<Interval> nextAlphabets(NFAState state) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets(java.lang.Object)
	 */
	public Set<T> nextDiscreteAlphabets(NFAState state) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getAcceptedStates()
	 */
	public Set<NFAState> getAcceptedStates() {
		return Collections.singleton(endState);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getMatchTag(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTag(NFAState state) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getMatchTag(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTagEnd(NFAState state) {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getAccepted(net.morilib.automata.nfa.NFAState)
	 */
	public Set<A> getAccept(NFAState state) {
		return Collections.emptySet();
	}

}
