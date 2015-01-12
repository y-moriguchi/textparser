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
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.morilib.automata.NFAState;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public final class NFAAlternative<T, A, B>
extends CompoundNFA<T, A, B> {

	//
	/*package*/ final NFAState beginState = new IntStateObject(this, 0);
	/*package*/ final NFAState endState   = new IntStateObject(this, 1);

	//
	private NFAAlternative() {
		// do nothing
	}

	/**
	 * 
	 * @param col
	 */
	public static<T, A, B> NFAAlternative<T, A, B> newInstance(
			Collection<NFAObject<T, A, B>> col) {
		NFAAlternative<T, A, B> res = new NFAAlternative<T, A, B>();

		res.nfas = new ArrayList<NFAObject<T, A, B>>(col);
		return res;
	}

	/**
	 * 
	 * @param arr
	 */
	public static<T, A, B> NFAAlternative<T, A, B> newInstance(
			NFAObject<T, A, B>[] arr) {
		NFAAlternative<T, A, B> res = new NFAAlternative<T, A, B>();

		res.nfas = Arrays.asList(arr);
		return res;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 */
	public static<T, A, B> NFAAlternative<T, A, B> newInstance(
			NFAObject<T, A, B> a, NFAObject<T, A, B> b) {
		NFAAlternative<T, A, B> res = new NFAAlternative<T, A, B>();

		res.nfas = new ArrayList<NFAObject<T, A, B>>();
		res.nfas.add(a);
		res.nfas.add(b);
		return res;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected NFAAlternative<T, A, B> clone() {
		return newInstance(getNFADeepCopy());
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
		if(beginState.equals(state)) {
			for(int i = 0; i < nfas.size(); i++) {
				nfas.get(i).addInitialStates(res);
			}
		} else {
			for(int i = 0; i < nfas.size(); i++) {
				if(nfas.get(i).isState(state)) {
					nfas.get(i).addStatesEpsilon(res, state);

					if(nfas.get(i).isFinal(state)) {
						res.add(endState);
					}
					return;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isState(java.lang.Object)
	 */
	public boolean isState(NFAState o) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(o)) {
				return true;
			}
		}
		return beginState.equals(o) || endState.equals(o);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#isInitialState(java.lang.Object)
	 */
	public boolean isInitialState(NFAState state) {
		return beginState.equals(state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#isAccepted(java.lang.Object)
	 */
	public boolean isFinal(NFAState state) {
		return endState.equals(state);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getAcceptedStates()
	 */
	public Set<NFAState> getAcceptedStates() {
		return Collections.singleton(endState);
	}

}
