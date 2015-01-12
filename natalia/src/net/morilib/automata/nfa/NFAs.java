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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.morilib.automata.NFA;
import net.morilib.automata.NFAState;
import net.morilib.automata.TextBound;
import net.morilib.natalia.range.CharSets;
import net.morilib.natalia.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/06/17
 */
public final class NFAs {

	//
	private NFAs() {}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static<A, B> NFA<Integer, A, B> build(String str) {
		List<NFAObject<Integer, A, B>> arr =
			new ArrayList<NFAObject<Integer, A, B>>();

		for(int i = 0; i < str.length(); i++) {
			arr.add(SingleObjectNFA.<Integer, A, B>newInstance(
					new Integer(str.charAt(i))));
		}
		return NFAConcatenation.newInstance(arr);
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static<T, A, B> NFA<T, A, B> build(T[] array) {
		List<NFAObject<T, A, B>> arr =
			new ArrayList<NFAObject<T, A, B>>();

		for(T t : array) {
			arr.add(SingleObjectNFA.<T, A, B>newInstance(t));
		}
		return NFAConcatenation.newInstance(arr);
	}

	/**
	 * 
	 * @param col
	 * @return
	 */
	public static<T, A, B> NFA<T, A, B> build(Collection<T> col) {
		List<NFAObject<T, A, B>> arr =
			new ArrayList<NFAObject<T, A, B>>();

		for(T t : col) {
			arr.add(SingleObjectNFA.<T, A, B>newInstance(t));
		}
		return NFAConcatenation.newInstance(arr);
	}

	/**
	 * 
	 * @param res
	 * @param nfa
	 */
	public static<T, A, B> Set<NFAState> getEpsilonReachable(
			NFA<T, A, B> nfa,
			Set<NFAState> states) {
		Set<NFAState>    res = new HashSet<NFAState>(states);
		Stack2<NFAState> stk = new LinkedListStack<NFAState>(states);

		while(!stk.isEmpty()) {
			NFAState t = stk.pop();

			Set<NFAState> e = nfa.getStatesEpsilon(t);
			for(NFAState u : e) {
				if(!res.contains(u)) {
					res.add(u);
					stk.push(u);
				}
			}
		}
		return res;
	}

	/**
	 * 
	 * @param <B>
	 * @param nfa
	 * @param states
	 * @return
	 */
	public static<B> Set<B> getMatchTag(
			NFA<?, ?, B> nfa,
			Set<NFAState> states) {
		Set<B> res = new HashSet<B>();

		for(NFAState s : states) {
			res.addAll(nfa.getMatchTag(s));
		}
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param <B>
	 * @param nfa
	 * @param states
	 * @return
	 */
	public static<B> Set<B> getMatchTagEnd(
			NFA<?, ?, B> nfa,
			Set<NFAState> states) {
		Set<B> res = new HashSet<B>();

		for(NFAState s : states) {
			res.addAll(nfa.getMatchTagEnd(s));
		}
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param <A>
	 * @param nfa
	 * @param states
	 * @return
	 */
	public static<A> Set<A> getAccept(
			NFA<?, A, ?> nfa,
			Set<NFAState> states) {
		Set<A> res = new HashSet<A>();

		for(NFAState s : states) {
			res.addAll(nfa.getAccept(s));
		}
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param nfa
	 * @param states
	 * @return
	 */
	public static boolean isAccepted(NFA<?, ?, ?> nfa,
			Set<NFAState> states) {
		for(NFAState s : states) {
			if(nfa.isAccepted(s))  return true;
		}
		return false;
	}

	/**
	 * 
	 * @param nfa
	 * @param states
	 * @return
	 */
	public static boolean isInitialState(NFA<?, ?, ?> nfa,
			Set<NFAState> states) {
		for(NFAState s : states) {
			if(nfa.isInitialState(s))  return true;
		}
		return false;
	}

	/**
	 * 
	 * @param <T>
	 * @param <A>
	 * @param <B>
	 * @param nfa
	 * @param state
	 * @param alphabet
	 * @return
	 */
	public static<T, A> Set<NFAState> getStates(
			NFA<T, A, ?> nfa,
			Set<NFAState> state, T alphabet) {
		Set<NFAState> res = new HashSet<NFAState>();

		if(nfa instanceof NFAObject) {
			NFAObject<T, A, ?> nfo = (NFAObject<T, A, ?>)nfa;

			for(NFAState s : state) {
				nfo.addStates(res, s, alphabet);
			}
		} else {
			for(NFAState s : state) {
				res.addAll(nfa.getStates(s, alphabet));
			}
		}
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param <T>
	 * @param <A>
	 * @param <B>
	 * @param nfa
	 * @param state
	 * @param rng
	 * @return
	 */
	public static<T, A> Set<NFAState> getStates(
			NFA<T, A, ?> nfa,
			Set<NFAState> state, Range rng) {
		Set<NFAState> res = new HashSet<NFAState>();

		if(nfa instanceof NFAObject) {
			NFAObject<T, A, ?> nfo = (NFAObject<T, A, ?>)nfa;

			for(NFAState s : state) {
				nfo.addStates(res, s, rng);
			}
		} else {
			for(NFAState s : state) {
				res.addAll(nfa.getStates(s, rng));
			}
		}
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param <T>
	 * @param <A>
	 * @param <B>
	 * @param nfa
	 * @param state
	 * @param b
	 * @return
	 */
	public static<T, A> Set<NFAState> getStatesBound(
			NFA<T, A, ?> nfa,
			Set<NFAState> state, EnumSet<TextBound> b) {
		Set<NFAState> res = new HashSet<NFAState>();

		if(nfa instanceof NFAObject) {
			NFAObject<T, A, ?> nfo = (NFAObject<T, A, ?>)nfa;

			for(NFAState s : state) {
				nfo.addStatesBound(res, s, b);
			}
		} else {
			for(NFAState s : state) {
				res.addAll(nfa.getStates(s, b));
			}
		}
		return Collections.unmodifiableSet(res);
	}

	/**
	 * 
	 * @param nfa
	 * @param n
	 * @param m
	 * @return
	 */
	public static<T, A, B> NFAObject<T, A, B> newRepetitionNM(
			NFAObject<T, A, B> nfa, int n, int m) {
		List<NFAObject<T, A, B>> l;

		if(n > m && m >= 0)  throw new IllegalArgumentException();
		l = new ArrayList<NFAObject<T, A, B>>();
		for(int i = 0; i < n; i++) {
			l.add(nfa.clone());
		}

		if(m < 0) {
			l.add(NFARepetition.newInstance(nfa.clone(), true));
		} else {
			for(int i = n; i < m; i++) {
				l.add(NFAOptional.newInstance(nfa.clone()));
			}
		}
		return NFAConcatenation.newInstance(l);
	}

	/**
	 * 
	 * @return
	 */
	public static<T, A, B> NFAObject<T, A, B> newComplexLetter() {
		List<NFAObject<T, A, B>> l;
		NFAObject<T, A, B> o;

		l = new ArrayList<NFAObject<T, A, B>>();
		o = SingleSetNFA.newInstance(CharSets.parse("\\P{M}"));
		l.add(o);
		o = SingleSetNFA.newInstance(CharSets.parse("\\p{M}"));
		l.add(NFARepetition.newInstance(o, true));
		return NFAConcatenation.newInstance(l);
	}

}
