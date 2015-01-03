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
import net.morilib.natalia.core.parser.Scratch;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class DrawLatitudinalBorderTransition implements Transition {

	//
	static final Transition I = new DrawLatitudinalBorderTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.db.parser.PS)
	 */
	@Override
	public PS transit(Quadro q, PS state) {
		switch(state) {
		case DRAW_LAT_INIT:
			if(q.get().isWall()) {
				q.moveNorth();
			} else {
				q.moveSouth();
				return PS.DRAW_LAT_UP1;
			}
			return state;
		case DRAW_LAT_UP1:
			if(q.getScratch().isFrame()) {
				return PS.DRAW_LAT_DOWN1;
			} else {
				return PS.DRAW_LONG_FRAME_INIT;
			}
		case DRAW_LAT_DOWN1:
			if(q.get().isWall()) {
				q.setScratch(q.getScratch().add(
						Scratch.LATITUDINAL_BORDER));
				q.moveSouth();
			} else if(q.get().isJunction()) {
				q.setScratch(q.getScratch().add(
						Scratch.LATITUDINAL_BORDER));
				q.moveSouth();
				return PS.DRAW_LAT_DOWN2;
			} else {
				throw new ParserException();
			}
			return state;
		case DRAW_LAT_DOWN2:
			if(q.get().isWall()) {
				q.setScratch(q.getScratch().add(
						Scratch.LATITUDINAL_BORDER));
				q.moveSouth();
			} else {
				q.moveNorth();
				return PS.DRAW_LAT_DOWN3;
			}
			return state;
		case DRAW_LAT_DOWN3:
			if(q.getScratch().isFrame()) {
				return PS.DRAW_LAT_UP;
			} else {
				return PS.DRAW_LONG_FRAME_INIT;
			}
		case DRAW_LONG_FRAME_END:
			if(q.getScratch().isLatitudinalBorder()) {
				return PS.DRAW_LAT_UP;
			} else {
				return PS.DRAW_LAT_DOWN1;
			}
		case DRAW_LAT_UP:
			if(q.get().isJunction()) {
				return PS.DRAW_LAT_END;
			} else {
				q.moveNorth();
			}
			return state;
		default:
			return DrawLongitudinalFrameTransition.I.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.DRAW_LAT_INIT,
				PS.DRAW_LAT_UP1,
				PS.DRAW_LAT_DOWN1,
				PS.DRAW_LAT_DOWN2,
				PS.DRAW_LAT_DOWN3,
				PS.DRAW_LAT_UP,
				PS.DRAW_LAT_END);
		r.addAll(DrawLongitudinalFrameTransition.I.getStates());
		return r;
	}

}
