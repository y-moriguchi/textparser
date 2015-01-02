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

public class DrawGridTransition implements Transition {

	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case DRAW_GRID_INIT:
			q.turnEast();
			return ParserState.TFRAME_INIT;
		case TFRAME_END:
			if(q.isDirectionWest()) {
				return ParserState.DRAW_GRID_RETURN;
			} else {
				return ParserState.TFRAME_INIT;
			}
		case DRAW_GRID_RETURN:
			if(q.getScratch().isFrameCorner()) {
				if(q.isDirectionNorth()) {
					return ParserState.DRAW_GRID_END;
				} else {
					q.turnRight().forward();
				}
			} else if(q.getScratch().isFrame()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		default:
			return TraverseFrameTransition.INSTANCE.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.DRAW_BORDER_INIT,
				ParserState.DRAW_GRID_INIT,
				ParserState.DRAW_GRID_RETURN,
				ParserState.DRAW_GRID_END);
		r.addAll(TraverseFrameTransition.INSTANCE.getStates());
		return r;
	}

}
