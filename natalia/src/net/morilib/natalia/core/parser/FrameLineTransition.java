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

public class FrameLineTransition implements Transition {

	//
	static final Transition INSTANCE = new FrameLineTransition();

	public ParserState transit(Quadro q, ParserState state) {
		Scratch s;

		switch(state) {
		case FRAME_LINE_INIT:
			if(!q.get().isJunction()) {
				throw new ParserException();
			}

			s = Scratch.FRAME_CORNER.add(Scratch.BORDER_CROSSING);
			if(q.isDirectionWest()) {
				s = s.add(Scratch.VERTICAL_FRAME);
			}
			q.setScratch(s).forward();
			return ParserState.FRAME_LINE_WALL;
		case FRAME_LINE_WALL:
			s = q.getScratch();
			if(q.isDirectionWest()) {
				q.setScratch(s.add(Scratch.FRAME.add(
						Scratch.LONGITUDINAL_BORDER)));
			} else if(q.isDirectionLatitudinal()) {
				q.setScratch(s.add(Scratch.VERTICAL_FRAME));
			} else {
				q.setScratch(s.add(Scratch.FRAME));
			}

			if(q.get().isJunction()) {
				q.forward();
				return ParserState.FRAME_LINE_JUNCTION;
			} else if(q.get().isWall()) {
				q.forward();
			} else {
				throw new ParserException();
			}
			return state;
		case FRAME_LINE_JUNCTION:
			if(q.get().isWall()) {
				return ParserState.FRAME_LINE_WALL;
			} else if(q.get().isJunction()) {
				s = q.getScratch();
				if(q.isDirectionNorth() || q.isDirectionSouth()) {
					q.setScratch(s.add(Scratch.VERTICAL_FRAME));
				} else {
					q.setScratch(s.add(Scratch.FRAME));
				}
				q.forward();
			} else {
				q.back().turnRight();
				return ParserState.FRAME_LINE_END;
			}
			return state;
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

		r = EnumSet.of(ParserState.FRAME_LINE_INIT,
				ParserState.FRAME_LINE_WALL,
				ParserState.FRAME_LINE_JUNCTION,
				ParserState.FRAME_LINE_END);
		return r;
	}

}
