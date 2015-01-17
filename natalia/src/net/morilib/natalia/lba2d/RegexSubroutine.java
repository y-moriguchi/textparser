/*
 * Copyright 2015 Yuichiro Moriguchi
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
package net.morilib.natalia.lba2d;

import java.util.HashSet;
import java.util.Set;

import net.morilib.automata.DFA;
import net.morilib.automata.DFAState;
import net.morilib.automata.NFA;
import net.morilib.automata.NFAState;
import net.morilib.automata.dfa.ConvertedRangeDFA;
import net.morilib.automata.nfa.RegexParseException;
import net.morilib.regex.dfa.RegexParser;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class RegexSubroutine<S, P> implements Transition<S, P> {

	/**
	 * 
	 *
	 * @author Yuichiro MORIGUCHI
	 */
	public enum Direction {
		EAST, SOUTH, WEST, NORTH;

		Direction getOpposite() {
			switch(this) {
			case EAST:   return WEST;
			case SOUTH:  return NORTH;
			case WEST:   return EAST;
			case NORTH:  return SOUTH;
			default:     throw new IllegalStateException();
			}
		}

	}

	//
	private DFA<Object, NFAState, Integer> dfa;
	private P begin, accept, reject;
	private Direction direction;

	/**
	 * 
	 * @param regex
	 * @param direction
	 * @param accept
	 * @param reject
	 * @throws RegexParseException
	 */
	public RegexSubroutine(String regex, Direction direction,
			P accept, P reject) throws RegexParseException {
		NFA<Object, NFAState, Integer> nfa;

		nfa = RegexParser.parse(regex);
		this.dfa = ConvertedRangeDFA.convertDFA(nfa);
		this.accept = accept;
		this.reject = reject;
		this.direction = direction;
	}

	private void move(Quadro<S> q, Direction d) {
		switch(d) {
		case EAST:
			q.moveEast();
			return;
		case SOUTH:
			q.moveSouth();
			return;
		case WEST:
			q.moveWest();
			return;
		case NORTH:
			q.moveNorth();
			return;
		default:     throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.lba2d.Transition#transit(net.morilib.natalia.lba2d.Quadro, java.lang.Object)
	 */
	@Override
	public P transit(Quadro<S> q, P state) {
		DFAState<Object, NFAState, Integer> s;
		boolean acc = false;

		if(!begin.equals(state)) {
			throw new IllegalStateException();
		} else if(q.get().isBound()) {
			return reject;
		}
		s = dfa.getInitialState();
		while(true) {
			s = s.go(q.get().getChar());
			acc = s.isAccepted();
			move(q, direction);
			if(q.get().isBound()) {
				return acc ? accept : reject;
			} else if(s.isDead()) {
				return acc ? accept : reject;
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.lba2d.Transition#getStates()
	 */
	@Override
	public Set<P> getStates() {
		Set<P> s = new HashSet<P>();

		s.add(begin);
		s.add(accept);
		s.add(reject);
		return s;
	}

}
