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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.morilib.automata.NFAState;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public final class NFAConcatenation<T, A, B>
extends CompoundNFA<T, A, B> {
	
	private NFAConcatenation() {
		// do nothing
	}
	
	/**
	 * 
	 * @param col
	 */
	public static<T, A, B> NFAConcatenation<T, A, B> newInstance(
			List<NFAObject<T, A, B>> col) {
		NFAConcatenation<T, A, B> res = new NFAConcatenation<T, A, B>();
		
		if(col.size() < 2) {
			throw new IllegalArgumentException();
		}
		res.nfas = new ArrayList<NFAObject<T, A, B>>(col);
		return res;
	}
	
	/**
	 * 
	 * @param arr
	 */
	public static<T, A, B> NFAConcatenation<T, A, B> newInstance(
			NFAObject<T, A, B>[] arr) {
		NFAConcatenation<T, A, B> res = new NFAConcatenation<T, A, B>();
		
		if(arr.length < 2) {
			throw new IllegalArgumentException();
		}
		res.nfas = Arrays.asList(arr);
		return res;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 */
	public static<T, A, B> NFAConcatenation<T, A, B> newInstance(
			NFAObject<T, A, B> a, NFAObject<T, A, B> b) {
		NFAConcatenation<T, A, B> res = new NFAConcatenation<T, A, B>();
		
		res.nfas = new ArrayList<NFAObject<T, A, B>>();
		res.nfas.add(a);
		res.nfas.add(b);
		return res;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected NFAConcatenation<T, A, B> clone() {
		return newInstance(getNFADeepCopy());
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#getInitialStates()
	 */
	/*package*/ void addInitialStates(Set<NFAState> res) {
		nfas.get(0).addInitialStates(res);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFAObject#addStatesEpsilon(java.util.Set, java.lang.Object)
	 */
	/*package*/ void addStatesEpsilon(
			Set<NFAState> res, NFAState state) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				nfas.get(i).addStatesEpsilon(res, state);
				
				if(i < nfas.size() - 1 &&
						nfas.get(i).isFinal(state)) {
					nfas.get(i + 1).addInitialStates(res);
				}
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isState(java.lang.Object)
	 */
	public boolean isState(NFAState o) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(o)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isInitialState(java.lang.Object)
	 */
	public boolean isInitialState(NFAState o) {
		return nfas.get(0).isInitialState(o);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isAccepted(java.lang.Object)
	 */
	public boolean isFinal(NFAState o) {
		return nfas.get(nfas.size() - 1).isFinal(o);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{" + nfas + "}";
	}

	public Set<NFAState> getAcceptedStates() {
		return nfas.get(nfas.size() - 1).getAcceptedStates();
	}

}
