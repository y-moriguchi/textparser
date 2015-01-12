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
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
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
public abstract class CompoundNFA<T, A, B> extends NFAObject<T, A, B> {

	/**
	 * 
	 */
	protected List<NFAObject<T, A, B>> nfas;

	/**
	 * 
	 * @return
	 */
	protected List<NFAObject<T, A, B>> getNFADeepCopy() {
		List<NFAObject<T, A, B>> l;

		l = new ArrayList<NFAObject<T, A, B>>();
		for(NFAObject<T, A, B> o : nfas) {
			l.add(o.clone());
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFAObject#addStates(java.util.Set, java.lang.Object, java.lang.Object)
	 */
	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, T alphabet) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				nfas.get(i).addStates(res, state, alphabet);
				return;
			}
		}
	}

	/*package*/ void addStates(
			Set<NFAState> res, NFAState state, Range a) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				nfas.get(i).addStates(res, state, a);
				return;
			}
		}
	}

	/*package*/ void addStatesBound(
			Set<NFAState> res, NFAState state,
			EnumSet<TextBound> a) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				nfas.get(i).addStatesBound(res, state, a);
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#nextAlphabets(java.lang.Object)
	 */
	public Set<Interval> nextAlphabets(NFAState state) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				return nfas.get(i).nextAlphabets(state);
			}
		}
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#nextAlphabets(java.lang.Object)
	 */
	public Set<T> nextDiscreteAlphabets(NFAState state) {
		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				return nfas.get(i).nextDiscreteAlphabets(state);
			}
		}
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getMatchTag(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTag(NFAState state) {
		Set<B> res = new HashSet<B>();

		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				res.addAll(nfas.get(i).getMatchTag(state));
				//break;
			}
		}
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getMatchTag(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTagEnd(NFAState state) {
		Set<B> res = new HashSet<B>();

		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				res.addAll(nfas.get(i).getMatchTagEnd(state));
				//break;
			}
		}
		return Collections.unmodifiableSet(res);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFA#getAccepted(net.morilib.automata.nfa.NFAState)
	 */
	public Set<A> getAccept(NFAState state) {
		Set<A> res = new HashSet<A>();

		for(int i = 0; i < nfas.size(); i++) {
			if(nfas.get(i).isState(state)) {
				res.addAll(nfas.get(i).getAccept(state));
				//break;
			}
		}
		return Collections.unmodifiableSet(res);
	}

}
