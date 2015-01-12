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

/**
 * 
 *
 *
 * @author MORIGUCHI, Yuichiro 2006
 */
public final class NFAOptional<T, A, B> extends NFAClosure<T, A, B> {

	//
	private NFAOptional(NFAObject<T, A, B> wrapee) {
		super(wrapee, true, false);
	}

	/**
	 * 
	 * @param wrapee
	 * @param nullable
	 */
	public static<T, A, B> NFAOptional<T, A, B> newInstance(
			NFAObject<T, A, B> wrapee) {
		return new NFAOptional<T, A, B>(wrapee);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.nfa.NFAObject#clone()
	 */
	protected NFAOptional<T, A, B> clone() {
		return newInstance(wrapee.clone());
	}

}
