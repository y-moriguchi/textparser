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

import java.util.Set;

import net.morilib.automata.NFAState;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public final class StringNFAMatcher<A> {

	//
	private NFAObject<Object, A, ?> nfa;
	private Set<A>    result = null;
	private String    matched = null;

	/**
	 * 
	 * @param nfa
	 */
	public StringNFAMatcher(NFAObject<Object, A, ?> nfa) {
		this.nfa = nfa;
	}

	//
	private Set<NFAState> getInitialStates() {
		return NFAs.getEpsilonReachable(nfa, nfa.getInitialStates());
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public boolean match(String str) {
		StringBuilder matches = new StringBuilder();
		Set<NFAState> s = getInitialStates();
		Set<NFAState> t = s;
		int i = 0;

		while(i < str.length()) {
			int a = str.charAt(i);

			matches.append((char)a);
			t = NFAs.getStates(nfa, s, a);
			s = NFAs.getEpsilonReachable(nfa, t);
			if(s.isEmpty()) {
				return false;
			} else {
				i++;
			}
		}

		if(nfa.isFinalAny(s)) {
			matched = matches.toString();
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
	public String getMatched() {
		return matched;
	}

}
