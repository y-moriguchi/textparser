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
import java.util.HashSet;
import java.util.Set;

import net.morilib.automata.NFAState;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/22
 */
public final class NFAParenthesis<T, A, B> extends NFAClosure<T, A, B> {

	//
	private B tag;

	//
	public NFAParenthesis(NFAObject<T, A, B> wrapee, B tag) {
		super(wrapee, false, false);
		this.tag = tag;
	}

	/**
	 * 
	 * @param wrapee
	 * @param nullable
	 */
	public static<T, A, B> NFAParenthesis<T, A, B> newInstance(
			NFAObject<T, A, B> wrapee, B tag) {
		return new NFAParenthesis<T, A, B>(wrapee, tag);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected NFAParenthesis<T, A, B> clone() {
		return newInstance(wrapee.clone(), tag);
	}

	/*
	 * (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAWrapper#getMatchTag(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTag(NFAState state) {
		Set<B> res = new HashSet<B>();

		res.add(tag);
		if(wrapee.isState(state)) {
			res.addAll(wrapee.getMatchTag(state));
		}
		return Collections.unmodifiableSet(res);
	}

	/*
	 * (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAWrapper#getMatchTagEnd(net.morilib.automata.nfa.NFAState)
	 */
	public Set<B> getMatchTagEnd(NFAState state) {
		Set<B> res = new HashSet<B>();

		if(wrapee.isState(state)) {
			if(wrapee.isFinal(state)) {
				res.add(tag);
			}
			res.addAll(wrapee.getMatchTagEnd(state));
		}
		return Collections.unmodifiableSet(res);
	}

}
