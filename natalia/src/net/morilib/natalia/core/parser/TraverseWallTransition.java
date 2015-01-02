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
 */
public class TraverseWallTransition implements Transition {

	//
	static final Transition INSTANCE = new TraverseWallTransition();

	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case TWALL_INIT:
			q.setScratch(q.getScratch().add(Scratch.BORDER_CROSSING));
			q.mark(this).turnRight().forward();
			return ParserState.TWALL_FORWARD;
		case TWALL_FORWARD:
			if(q.getScratch().isFrame()) {
				q.setScratch(q.getScratch().add(
						Scratch.BORDER_CROSSING));
				q.turnLeft().turnLeft().forward();
				return ParserState.TWALL_BACK;
			} else if(q.get().isJunction()) {
				return ParserState.DRAW_BORDER_INIT;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			} else {
				if(q.isDirectionLatitudinal()) {
					q.setScratch(q.getScratch().add(
							Scratch.LATITUDINAL_BORDER));
				} else {
					q.setScratch(q.getScratch().add(
							Scratch.LONGITUDINAL_BORDER));
				}
				q.forward();
			}
			return state;
		case DRAW_BORDER_END:
			q.forward();
			return ParserState.TWALL_FORWARD;
		case TWALL_BACK:
			if(q.isMarked(this)) {
				q.mark(null).turnRight();
				return ParserState.TWALL_END;
			} else {
				q.forward();
			}
			return state;
		default:
			return DrawBorderTransition.INSTANCE.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.TWALL_INIT,
				ParserState.TWALL_FORWARD,
				ParserState.TWALL_BACK,
				ParserState.TWALL_END);
		r.addAll(DrawBorderTransition.INSTANCE.getStates());
		return r;
	}

}
