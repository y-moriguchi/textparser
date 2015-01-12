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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import net.morilib.automata.NFA;
import net.morilib.automata.NFAState;

/**
 * 
 *
 *
 * @author MORIGUCHI, Yuichiro 2013/01/05
 */
public final class GenericNFAMatcher<T, A> {

	//
	private NFA<T, A, ?> nfa;
	private Set<A>    result = null;
	private List<T>   matched = null;

	/**
	 * 
	 * @param nfa
	 */
	public GenericNFAMatcher(NFA<T, A, ?> nfa) {
		this.nfa = nfa;
	}

	//
	private Set<NFAState> getInitialStates() {
		return NFAs.getEpsilonReachable(nfa, nfa.getInitialStates());
	}

	/**
	 * 
	 * @param ts
	 * @return
	 */
	public boolean match(Iterator<T> ts) {
		List<T> matches = new ArrayList<T>();
		Set<NFAState> s = getInitialStates();
		Set<NFAState> t = s;
		T a;

		while(ts.hasNext()) {
			a = ts.next();
			matches.add(a);
			t = NFAs.getStates(nfa, s, a);
			s = NFAs.getEpsilonReachable(nfa, t);
			if(s.isEmpty())  return false;
		}

		if(nfa.isFinalAny(s)) {
			matched = matches;
			result  = NFAs.getAccept(nfa, s);
			return true;
		} else {
			matched = null;
			result  = null;
			return false;
		}
	}

	/**
	 * 
	 * @param ts
	 * @return
	 */
	public boolean match(Iterable<T> ts) {
		return match(ts.iterator());
	}

	public boolean match(final String s) {
		return match(new Iterator<T>() {

			private int l = 0;

			@Override
			public boolean hasNext() {
				return l < s.length();
			}

			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				if(l >= s.length()) {
					throw new NoSuchElementException();
				}
				return (T)Integer.valueOf(s.charAt(l++));
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		});
	}

	/**
	 * 
	 * @return
	 */
	public Set<A> getResult() {
		// result is already unmodifiable
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public List<T> getMatched() {
		return (matched != null) ?
				new ArrayList<T>(matched) : null;
	}

}
