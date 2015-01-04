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
package net.morilib.natalia.db.parser;

import java.util.EnumSet;

import net.morilib.natalia.core.parser.ParserException;
import net.morilib.natalia.core.parser.Quadro;

public class FrameTransition implements Transition {

	//
	static final Transition I = new FrameTransition();

	@Override
	public PS transit(Quadro q, PS state) {
		switch(state) {
		case FRAME_INIT:
			q.mark(this).turnEast();
			return PS.FRAME_LINE_INIT;
		case FRAME_LINE_INIT:
		case FRAME_LINE_WALL:
		case FRAME_LINE_JUNCTION:
			return FrameLineTransition.I.transit(q, state);
		case FRAME_LINE_END:
			if(q.isMarked(this)) {
				q.mark(null);
				return PS.FRAME_END;
			} else if(q.isDirectionEast()) {
				throw new ParserException();
			} else {
				return PS.FRAME_LINE_INIT;
			}
		default:
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.FRAME_INIT,
				PS.FRAME_LINE_INIT,
				PS.FRAME_LINE_WALL,
				PS.FRAME_LINE_JUNCTION,
				PS.FRAME_LINE_END,
				PS.FRAME_END);
		return r;
	}

}
