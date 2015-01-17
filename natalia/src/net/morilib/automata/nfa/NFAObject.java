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

import net.morilib.automata.NFA;
import net.morilib.automata.NFAEdges;
import net.morilib.automata.NFAState;
import net.morilib.automata.TextBound;
import net.morilib.range.Interval;
import net.morilib.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public abstract class NFAObject<T, A, B>
implements NFA<T, A, B>, Cloneable {

	//
	/*package*/ static final class IntStateObject implements NFAState {

		//
		private Object fa;
		private int    internal;

		//
		public IntStateObject(Object fa, int internal) {
			this.fa = fa;
			this.internal = internal;
		}

		//
		public boolean equals(Object o) {
			if(o instanceof IntStateObject) {
				IntStateObject n = (IntStateObject)o;

				return fa.equals(n.fa) && n.internal == internal;
			}
			return false;
		}

		//
		public int hashCode() {
			return (fa.hashCode() << 8) | (internal & 0xff);
		}

		//
		public String toString() {
			return (fa.toString() + ":" + Integer.toString(internal));
		}

	}

	//
	/*package*/ class Edges implements NFAEdges<T> {

		//
		/*package*/ NFAState state;

		//
		/*package*/ Edges(NFAState state) {
			this.state = state;
		}

		//
		public Set<NFAState> goNext(T alphabet) {
			HashSet<NFAState> res = new HashSet<NFAState>();

			addStates(res, state, alphabet);
			return res;
		}

		/* (non-Javadoc)
		 * @see net.morilib.automata.nfa.NFAEdges#goNext(int)
		 */
		@Override
		public Set<NFAState> goNext(int alphabet) {
			return goNext(Integer.valueOf(alphabet));
		}

		/* (non-Javadoc)
		 * @see net.morilib.automata.nfa.NFAEdges#goNext(char)
		 */
		@Override
		public Set<NFAState> goNext(char alphabet) {
			return goNext(Integer.valueOf((int)alphabet));
		}

		//
		public Set<NFAState> goNextEpsilon() {
			HashSet<NFAState> res = new HashSet<NFAState>();

			addStatesEpsilon(res, state);
			return res;
		}

		//
		public Set<? extends Range> nextAlphabets() {
			return NFAObject.this.nextAlphabets(state);
		}

		//
		public boolean isNextEpsilon() {
			return isNextEpsilon();
		}

	}

	//
	/*package*/ NFAObject() {
		// do nothing
	}

	/**
	 * 
	 * @param res
	 * @param state
	 * @param a
	 */
	/*package*/ abstract void addStates(
			Set<NFAState> res, NFAState state, T a);

	/**
	 * 
	 * @param res
	 * @param state
	 * @param a
	 */
	/*package*/ abstract void addStates(
			Set<NFAState> res, NFAState state, Range a);

	/**
	 * 
	 * @param res
	 * @param state
	 * @param b
	 */
	/*package*/ abstract void addStatesBound(
			Set<NFAState> res, NFAState state, EnumSet<TextBound> b);

	/**
	 * 
	 * @param res
	 */
	/*package*/ abstract void addInitialStates(Set<NFAState> res);

	/**
	 * 
	 * @param res
	 * @param state
	 */
	/*package*/ abstract void addStatesEpsilon(
			Set<NFAState> res, NFAState state);

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected abstract NFAObject<T, A, B> clone();

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getInitialStates()
	 */
	public Set<NFAState> getInitialStates() {
		Set<NFAState> res = new HashSet<NFAState>();

		addInitialStates(res);
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getStates(java.lang.Object, java.lang.Object)
	 */
	public Set<NFAState> getStates(
			NFAState state, T alphabet) {
		Set<NFAState> res = new HashSet<NFAState>();

		addStates(res, state, alphabet);
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getStates(java.util.Set, net.morilib.range.Range)
	 */
	public Set<NFAState> getStates(NFAState state, Range rng) {
		Set<NFAState> res = new HashSet<NFAState>();

		addStates(res, state, rng);
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getStates(java.util.Set, java.util.EnumSet)
	 */
	public Set<NFAState> getStates(
			NFAState state, EnumSet<TextBound> b) {
		Set<NFAState> res = new HashSet<NFAState>();

		addStatesBound(res, state, b);
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getStatesEpsilon(java.lang.Object)
	 */
	public Set<NFAState> getStatesEpsilon(NFAState state) {
		Set<NFAState> res = new HashSet<NFAState>();

		addStatesEpsilon(res, state);
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getStatesEpsilon(net.morilib.automata.nfa.NFAState, java.util.EnumSet)
	 */
	public Set<NFAState> getStatesBound(
			NFAState state, EnumSet<TextBound> b) {
		Set<NFAState> res = new HashSet<NFAState>();

		addStatesBound(res, state, b);
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param states
	 * @return
	 */
	public boolean isFinalAny(Set<NFAState> states) {
		for(NFAState s : states) {
			if(isFinal(s)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getEdges()
	 */
	public NFAEdges<T> getEdges(NFAState state) {
		return new Edges(state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets(java.util.Set)
	 */
	public Iterable<Interval> nextAlphabets(Set<NFAState> states) {
		RangeTree rt = null;

		for(NFAState s : states) {
			for(Interval i : nextAlphabets(s)) {
				if(rt == null) {
					rt = new RangeTree(i);
				} else {
					rt.insert(i);
				}
			}
		}
		return (rt == null) ? new HashSet<Interval>() : rt;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#nextAlphabets(java.util.Set)
	 */
	public Iterable<T> nextDiscreteAlphabets(Set<NFAState> states) {
		Set<T> rt = new HashSet<T>();

		for(NFAState s : states) {
			for(T i : nextDiscreteAlphabets(s))  rt.add(i);
		}
		return rt;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isAccepted(net.morilib.automata.nfa.NFAState)
	 */
	@Override
	public boolean isAccepted(NFAState state) {
		return !getAccept(state).isEmpty();
	}

}
