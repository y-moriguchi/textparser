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
package net.morilib.natalia.core.parser;

import java.util.EnumSet;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class FrameSearchTransition implements Transition {

	//
	static final Transition INSTANCE = new FrameSearchTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.core.parser.ParserState)
	 */
	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case FSEARCH_INIT:
			if(q.get().isJunction()) {
				q.moveEast();
				return ParserState.FSEARCH_FIND1;
			} else if(q.get().isBound()) {
				q.crlf();
			} else {
				q.moveEast();
			}
			return state;
		case FSEARCH_FIND1:
			if(q.get().isWall() || q.get().isJunction()) {
				q.moveEast();
				return ParserState.FSEARCH_FIND2;
			} else {
				return ParserState.FSEARCH_INIT;
			}
		case FSEARCH_FIND2:
			if(q.get().isWall() || q.get().isJunction()) {
				q.moveWest().moveWest();
				return ParserState.FMAIN_INIT;
			} else {
				q.moveWest();
				return ParserState.FSEARCH_INIT;
			}
		case FMAIN_END:
			return ParserState.FSEARCH_END;
		default:
			return FrameMainTransition.INSTANCE.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.FSEARCH_INIT,
				ParserState.FSEARCH_FIND1,
				ParserState.FSEARCH_FIND2,
				ParserState.FSEARCH_END);
		r.addAll(FrameMainTransition.INSTANCE.getStates());
		return r;
	}

}
