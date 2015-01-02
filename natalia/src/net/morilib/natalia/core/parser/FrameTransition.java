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

public class FrameTransition implements Transition {

	//
	static final Transition INSTANCE = new FrameTransition();

	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case FRAME_INIT:
			q.mark(this).turnEast();
			return ParserState.FRAME_LINE_INIT;
		case FRAME_LINE_INIT:
		case FRAME_LINE_WALL:
		case FRAME_LINE_JUNCTION:
			return FrameLineTransition.INSTANCE.transit(q, state);
		case FRAME_LINE_END:
			if(q.isMarked(this)) {
				q.mark(null);
				return ParserState.FRAME_END;
			} else if(q.isDirectionEast()) {
				throw new ParserException();
			} else {
				return ParserState.FRAME_LINE_INIT;
			}
		default:
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.FRAME_INIT,
				ParserState.FRAME_LINE_INIT,
				ParserState.FRAME_LINE_WALL,
				ParserState.FRAME_LINE_JUNCTION,
				ParserState.FRAME_LINE_END,
				ParserState.FRAME_END);
		return r;
	}

}
