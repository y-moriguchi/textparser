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
import net.morilib.natalia.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/21
 */
public abstract class SingleAlphabetNFA<T, A, B>
extends NFAObject<T, A, B> {
	
	//
	/*package*/ final NFAState beginState = new IntStateObject(this, 0);
	/*package*/ final NFAState endState   = new IntStateObject(this, 1);
	
	/**
	 * 
	 * @param alphabet
	 * @return
	 */
	public abstract boolean contains(T alphabet);
	
	/**
	 * 
	 * @return
	 */
	public abstract Range getObject();

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#getStates(java.lang.Object, java.lang.Object)
	 */
	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, T alphabet) {
		if(state.equals(beginState) && contains(alphabet)) {
			res.add(endState);
		}
	}

	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, Range a) {
		if(state.equals(beginState) && a.in(getObject())) {
			res.add(endState);
		}
	}

	/*package*/ void addStatesBound(
			Set<NFAState> res, NFAState state,
			EnumSet<TextBound> b) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#getInitialStates()
	 */
	/*package*/ void addInitialStates(Set<NFAState> res) {
		res.add(beginState);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFAObject#addStatesEpsilon(java.util.Set, java.lang.Object)
	 */
	/*package*/ void addStatesEpsilon(
			Set<NFAState> res, NFAState state) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isState(java.lang.Object)
	 */
	public boolean isState(NFAState o) {
		if(o instanceof IntStateObject) {
			IntStateObject s = (IntStateObject)o;
			
			return s.equals(beginState) || s.equals(endState);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isInitialState(java.lang.Object)
	 */
	public boolean isInitialState(NFAState o) {
		return beginState.equals(o);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isAccepted(java.lang.Object)
	 */
	public boolean isFinal(NFAState o) {
		return endState.equals(o);
	}

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
