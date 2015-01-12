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
public final class NFAAccept<T, A, B> extends NFAWrapper<T, A, B> {

	//
	private A acc;

	//
	private NFAAccept(NFAObject<T, A, B> wrapee, A acc) {
		this.wrapee = wrapee;
		this.acc = acc;
	}

	/**
	 * 
	 * @param wrapee
	 * @param nullable
	 */
	public static<T, A, B> NFAAccept<T, A, B> newInstance(
			NFAObject<T, A, B> wrapee, A acc) {
		return new NFAAccept<T, A, B>(wrapee, acc);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected NFAAccept<T, A, B> clone() {
		return newInstance(wrapee.clone(), acc);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAWrapper#getAccept(net.morilib.automata.nfa.NFAState)
	 */
	public Set<A> getAccept(NFAState state) {
		Set<A> res = new HashSet<A>();

		if(wrapee.isFinal(state)) {
			res.add(acc);
			//res.addAll(wrapee.getAccept(state));
		}
		return Collections.unmodifiableSet(res);
	}

}
