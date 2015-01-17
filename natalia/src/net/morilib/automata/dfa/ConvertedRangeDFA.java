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
package net.morilib.automata.dfa;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import net.morilib.automata.AlphabetsNotDiscreteException;
import net.morilib.automata.DFA;
import net.morilib.automata.DFAState;
import net.morilib.automata.NFA;
import net.morilib.automata.NFAState;
import net.morilib.automata.TextBound;
import net.morilib.automata.misc.ArrayListStack;
import net.morilib.automata.misc.Stack2;
import net.morilib.automata.misc.TreeSectionMap;
import net.morilib.automata.nfa.NFAs;
import net.morilib.range.Interval;

/**
 * 
 *
 *
 * @author MORIGUCHI, Yuichiro 2006
 */
public class ConvertedRangeDFA<T, O, B> implements DFA<T, O, B> {

	//
	private Map<Object, TreeSectionMap<Interval, Object, State>> graph =
		new IdentityHashMap
		<Object, TreeSectionMap<Interval, Object, State>>();
	private Map<Object, EnumMap<TextBound, State>> boundgraph =
		new IdentityHashMap<Object, EnumMap<TextBound, State>>();

	//
	private Map<Object, Set<O>> accept =
		new IdentityHashMap<Object, Set<O>>();
	private Map<Object, Set<B>> tags =
		new IdentityHashMap<Object, Set<B>>();
	private Map<Object, State> states = new HashMap<Object, State>();
	private State initial;

	//
	private class State implements DFAState<T, O, B> {

		private Object st;

		private State(Object st) {
			this.st = st;
		}

		public DFAState<T, O, B> go(T alphabet) {
			TreeSectionMap<Interval, Object, State> ts;
			State s2;

			if((ts = graph.get(st)) == null) {
				return DFAs.deadState();
			} else if((s2 = ts.map(alphabet)) == null) {
				return DFAs.deadState();
			}
			return s2;
		}

		public DFAState<T, O, B> goBound(TextBound bound) {
			EnumMap<TextBound, State> ts;
			State s2;

			if((ts = boundgraph.get(st)) == null) {
				return DFAs.deadState();
			} else if((s2 = ts.get(bound)) == null) {
				return DFAs.deadState();
			}
			return s2;
		}

		public Set<O> getAccepted() {
			Set<O> res = accept.get(st);

			return (res == null) ? Collections.<O>emptySet() : res;
		}

		public boolean isInitialState() {
			// compare by identity
			return initial == st;
		}

		public boolean isDead() {
			return graph.get(st) == null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public DFAState<T, O, B> goInt(int x) {
			return go((T)Integer.valueOf(x));
		}

		@SuppressWarnings("unchecked")
		@Override
		public DFAState<T, O, B> goChar(char x) {
			return go((T)Integer.valueOf((int)x));
		}

		@Override
		public boolean isAccepted() {
			Set<O> res = accept.get(st);

			return (res != null) && !res.isEmpty();
		}

		@Override
		public Set<T> getAlphabets() {
			throw new AlphabetsNotDiscreteException();
		}

		@Override
		public Iterable<Interval> getAlphabetRanges() {
			TreeSectionMap<Interval, Object, State> ts;

			if((ts = graph.get(st)) == null) {
				return Collections.emptySet();
			} else {
				return ts.keys();
			}
		}

		@Override
		public Object getLabel(T alphabet) {
			return null;
		}

		@Override
		public Object getLabelInt(int x) {
			return null;
		}

		@Override
		public Object getLabelChar(char x) {
			return null;
		}

	}

	//
	private State newstate(Object nst) {
		State s;

		if((s = states.get(nst)) == null) {
			s = new State(nst);
			states.put(nst, s);
		}
		return s;
	}

	//
	private void addTrans(Object st, Interval i, Object nst) {
		TreeSectionMap<Interval, Object, State> edges;
		State n = newstate(nst);

		edges = graph.get(st);
		if(edges == null) {
			edges = new TreeSectionMap<Interval, Object, State>(i, n);
			graph.put(st, edges);
		} else {
			edges.insert(i, n);
		}
	}

	//
	private void addTransBound(Object st, TextBound b, Object nst) {
		EnumMap<TextBound, State> edges;
		State n = newstate(nst);

		edges = boundgraph.get(st);
		if(edges == null) {
			edges = new EnumMap<TextBound, State>(TextBound.class);
			boundgraph.put(st, edges);
		}
		edges.put(b, n);
	}

	//
	private void setInitialState(Object o) {
		initial = newstate(o);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.dfa.DFA#getInitialState()
	 */
	public DFAState<T, O, B> getInitialState() {
		return initial;
	}

	/**
	 * 
	 * @param nfa
	 * @return
	 */
	public static<T, A, B> DFA<T, A, B> convertDFA(NFA<T, A, B> nfa) {
		Map<Set<NFAState>, Set<NFAState>>  dstates =
			new HashMap<Set<NFAState>, Set<NFAState>>();
		Stack2<Set<NFAState>> stk = new ArrayListStack<Set<NFAState>>();
		ConvertedRangeDFA<T, A, B> res = new ConvertedRangeDFA<T, A, B>();
		Set<NFAState> init =
			NFAs.getEpsilonReachable(nfa, nfa.getInitialStates());

		res.setInitialState(init);
		stk.push(init);
		while(!stk.isEmpty()) {
			Set<NFAState> t = stk.pop();

			dstates.put   (t, t);
			res.tags.put  (t, NFAs.getMatchTag(nfa, t));
			res.accept.put(t, NFAs.getAccept(nfa, t));

			// all alphabets
			for(Interval r : nfa.nextAlphabets(t)) {
				Set<NFAState> u = NFAs.getEpsilonReachable(
						nfa, NFAs.getStates(nfa, t, r));
				Set<NFAState> z;

				if((z = dstates.get(u)) == null) {
					dstates.put(u, u);
					stk.push(u);
					z = u;
				}
				res.addTrans(t, r, z);
			}

			// all bounds
			for(TextBound b : TextBound.ALL) {
				EnumSet<TextBound> bs = EnumSet.of(b);
				Set<NFAState> u = NFAs.getEpsilonReachable(
						nfa, NFAs.getStatesBound(nfa, t, bs));
				Set<NFAState> z;

				if(!u.isEmpty()) {
					if((z = dstates.get(u)) == null) {
						dstates.put(u, u);
						stk.push(u);
						z = u;
					}
					res.addTransBound(t, b, z);
				}
			}
		}
		return res;
	}

}
