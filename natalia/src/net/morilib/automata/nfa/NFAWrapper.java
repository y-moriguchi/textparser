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
import java.util.HashSet;
import java.util.Set;

import net.morilib.automata.NFAState;
import net.morilib.automata.TextBound;
import net.morilib.natalia.range.Interval;
import net.morilib.natalia.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public abstract class NFAWrapper<T, A, B> extends NFAObject<T, A, B> {

	/**
	 * 
	 */
	protected NFAObject<T, A, B> wrapee;

	//
	void addStates(
			Set<NFAState> res, NFAState state, T alphabet) {
		if(wrapee.isState(state)) {
			wrapee.addStates(res, state, alphabet);
		}
	}

	//
	void addStates(
			Set<NFAState> res, NFAState state, Range a) {
		if(wrapee.isState(state)) {
			wrapee.addStates(res, state, a);
		}
	}

	//
	void addStatesBound(
			Set<NFAState> res, NFAState state,
			EnumSet<TextBound> a) {
		if(wrapee.isState(state)) {
			wrapee.addStatesBound(res, state, a);
		}
	}

	//
	void addInitialStates(Set<NFAState> res) {
		wrapee.addInitialStates(res);
	}

	//
	void addStatesEpsilon(Set<NFAState> res, NFAState state) {
		wrapee.addStatesEpsilon(res, state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isState(java.lang.Object)
	 */
	public boolean isState(NFAState o) {
		if(o instanceof IntStateObject) {
			return wrapee.isState(o);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isInitialState(java.lang.Object)
	 */
	public boolean isInitialState(NFAState o) {
		return wrapee.isInitialState(o);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isFinal(net.morilib.automata.nfa.NFAState)
	 */
	public boolean isFinal(NFAState o) {
		return wrapee.isFinal(o);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets()
	 */
	public Set<Interval> nextAlphabets(NFAState state) {
		return wrapee.nextAlphabets(state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets()
	 */
	public Set<T> nextDiscreteAlphabets(NFAState state) {
		return wrapee.nextDiscreteAlphabets(state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getAcceptedStates()
	 */
	public Set<NFAState> getAcceptedStates() {
		return wrapee.getAcceptedStates();
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getMatchTag(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTag(NFAState state) {
		Set<B> res = new HashSet<B>();

		if(wrapee.isState(state)) {
			res.addAll(wrapee.getMatchTag(state));
		}
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getMatchTagEnd(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTagEnd(NFAState state) {
		Set<B> res = new HashSet<B>();

		if(wrapee.isState(state)) {
			res.addAll(wrapee.getMatchTagEnd(state));
		}
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getAccepted(net.morilib.automata.nfa.NFAState)
	 */
	public Set<A> getAccept(NFAState state) {
		Set<A> res = new HashSet<A>();

		if(wrapee.isState(state)) {
			res.addAll(wrapee.getAccept(state));
		}
		return Collections.unmodifiableSet(res);
	}

}
