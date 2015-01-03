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

import net.morilib.natalia.core.parser.Quadro;
import net.morilib.natalia.core.parser.Scratch;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class DrawLongitudinalFrameTransition implements Transition {

	//
	static final Transition I = new DrawLongitudinalFrameTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.db.parser.PS)
	 */
	@Override
	public PS transit(Quadro q, PS state) {
		switch(state) {
		case DRAW_LONG_FRAME_INIT:
			q.mark(this);
			return PS.DRAW_LONG_FRAME1;
		case DRAW_LONG_FRAME1:
			if(q.get().isBound()) {
				q.moveEast();
				return PS.DRAW_LONG_FRAME2;
			} else {
				q.moveWest();
			}
			return state;
		case DRAW_LONG_FRAME2:
			if(q.get().isBound()) {
				q.moveWest();
				return PS.DRAW_LONG_FRAME3;
			} else {
				q.setScratch(q.getScratch().add(Scratch.FRAME));
				q.moveEast();
			}
			return state;
		case DRAW_LONG_FRAME3:
			if(q.isMarked(this)) {
				q.mark(null);
				return PS.DRAW_LONG_FRAME_END;
			} else {
				q.moveWest();
			}
			return state;
		default:
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.DRAW_LONG_FRAME_INIT,
				PS.DRAW_LONG_FRAME1,
				PS.DRAW_LONG_FRAME2,
				PS.DRAW_LONG_FRAME3,
				PS.DRAW_LONG_FRAME_END);
		return r;
	}

}
