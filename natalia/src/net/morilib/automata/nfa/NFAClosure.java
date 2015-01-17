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
import net.morilib.range.Interval;
import net.morilib.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public abstract class NFAClosure<T, A, B> extends NFAObject<T, A, B> {

	//
	final NFAState beginState = new IntStateObject(this, 0);
	final NFAState endState   = new IntStateObject(this, 1);

	//
	protected NFAObject<T, A, B> wrapee;
	protected boolean nullable;
	protected boolean repeatable;

	//
	protected NFAClosure(
			NFAObject<T, A, B> wrapee,
			boolean nullable,
			boolean repeatable) {
		this.wrapee = wrapee;
		this.nullable = nullable;
		this.repeatable = repeatable;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isRepeatable() {
		return repeatable;
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#getStates(java.lang.Object, java.lang.Object)
	 */
	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, T alphabet) {
		if(wrapee.isState(state)) {
			wrapee.addStates(res, state, alphabet);
		}
	}

	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, Range a) {
		if(wrapee.isState(state)) {
			wrapee.addStates(res, state, a);
		}
	}

	/*package*/ void addStatesBound(
			Set<NFAState> res, NFAState state,
			EnumSet<TextBound> a) {
		if(wrapee.isState(state)) {
			wrapee.addStatesBound(res, state, a);
		}
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#getInitialStates()
	 */
	/*package*/ void addInitialStates(Set<NFAState> res) {
		res.add(beginState);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#addStatesEpsilon(java.util.Set, java.lang.Object)
	 */
	/*package*/ void addStatesEpsilon(
			Set<NFAState> res, NFAState state) {
		if(beginState.equals(state)) {
			if(nullable) {
				res.add(endState);
			}

			wrapee.addInitialStates(res);
			//res.addAll(wrapee.getAllStates(init, alphabet));
		} else if(wrapee.isFinal(state)) {
			res.addAll(wrapee.getStatesEpsilon(state));

			res.add(endState);
			if(repeatable) {
				wrapee.addInitialStates(res);
			}
			//res.addAll(wrapee.getAllStates(init, alphabet));
		} else if(wrapee.isState(state)) {
			res.addAll(wrapee.getStatesEpsilon(state));
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isState(java.lang.Object)
	 */
	public boolean isState(NFAState o) {
		if(o instanceof IntStateObject) {
			IntStateObject s = (IntStateObject)o;

			return (s.equals(beginState) ||
					s.equals(endState)   || wrapee.isState(o));
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
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets()
	 */
	public Set<Interval> nextAlphabets(NFAState state) {
		if(wrapee.isState(state)) {
			return wrapee.nextAlphabets(state);
		} else {
			return Collections.emptySet();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets()
	 */
	public Set<T> nextDiscreteAlphabets(NFAState state) {
		if(wrapee.isState(state)) {
			return wrapee.nextDiscreteAlphabets(state);
		} else {
			return Collections.emptySet();
		}
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
