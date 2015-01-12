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
import java.util.Set;

import net.morilib.automata.NFAState;
import net.morilib.natalia.range.Interval;
import net.morilib.natalia.range.Range;

/**
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2006/05/27
 */
public final class SingleObjectNFA<T, A, B>
extends SingleAlphabetNFA<T, A, B> {
	
	//
	private T object;
	
	//
	private SingleObjectNFA(T object) {
		this.object = object;
	}
	
	/**
	 * 
	 * @param object
	 */
	public static<T, A, B> SingleObjectNFA<T, A, B> newInstance(
			T object) {
		return new SingleObjectNFA<T, A, B>(object);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected SingleObjectNFA<T, A, B> clone() {
		return newInstance(object);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.SingleAlphabetNFA#equalsAlphabet(java.lang.Object)
	 */
	public boolean contains(T alphabet) {
		return InternalUtils.equals(object, alphabet);
	}

	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.SingleAlphabetNFA#getObject()
	 */
	public Range getObject() {
		return Interval.newPoint(object);
	}
	
	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#nextAlphabets()
	 */
	public Set<Interval> nextAlphabets(NFAState state) {
		if(beginState.equals(state)) {
			return getObject().intervals();
		} else {
			return Collections.emptySet();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.usei.automata.nfa.NFA#nextAlphabets()
	 */
	public Set<T> nextDiscreteAlphabets(NFAState state) {
		if(beginState.equals(state)) {
			return Collections.singleton(object);
		} else {
			return Collections.emptySet();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return object + "";
	}

}
