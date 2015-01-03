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
public class TraverseHorizontalAxisTransition implements Transition {

	//
	static final Transition I = new TraverseHorizontalAxisTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.core.parser.ParserState)
	 */
	@Override
	public PS transit(Quadro q, PS state) {
		switch(state) {
		case T_AXIS_INIT:
			if(q.get().isWall()) {
				q.moveEast();
			} else if(q.get().isJunction()) {
				q.moveNorth();
				return PS.DRAW_LAT_INIT;
			} else {
				q.moveWest().mark(this).moveNorth();
				return PS.T_AXIS_EAST_FRAME;
			}
			return state;
		case DRAW_LAT_END:
			q.moveEast();
			return PS.T_AXIS_INIT;
		case T_AXIS_EAST_FRAME:
			if(q.getScratch().isFrame()) {
				q.setScratch(q.getScratch().add(
						Scratch.VERTICAL_FRAME));
				q.moveSouth();
				return PS.T_AXIS_EAST_FRAME2;
			} else if(q.get().isBound()) {
				throw new ParserException();
			} else {
				q.moveNorth();
			}
			return state;
		case T_AXIS_EAST_FRAME2:
			if(q.getScratch().isFrame()) {
				q.setScratch(q.getScratch().add(Scratch.FRAME_CORNER));
				return PS.T_AXIS_EAST_FRAME3;
			} else {
				q.setScratch(q.getScratch().add(
						Scratch.VERTICAL_FRAME));
				q.moveSouth();
			}
			return state;
		case T_AXIS_EAST_FRAME3:
			if(q.isMarked(this)) {
				q.mark(null);
				q.setScratch(q.getScratch().add(
						Scratch.LONGITUDINAL_BORDER));
				q.moveWest();
				return PS.T_AXIS_WEST;
			} else {
				q.moveNorth();
			}
			return state;
		case T_AXIS_WEST:
			q.setScratch(q.getScratch().add(
					Scratch.LONGITUDINAL_BORDER));
			if(q.get().isWall() || q.get().isJunction()) {
				q.moveWest();
			} else {
				q.moveEast().mark(this);
				return PS.T_AXIS_WEST_FRAME;
			}
			return state;
		case T_AXIS_WEST_FRAME:
			if(q.getScratch().isFrame()) {
				q.setScratch(q.getScratch().add(Scratch.FRAME_CORNER));
				q.moveSouth();
				return PS.T_AXIS_WEST_FRAME2;
			} else if(q.get().isBound()) {
				throw new ParserException();
			} else {
				q.moveNorth();
			}
			return state;
		case T_AXIS_WEST_FRAME2:
			if(q.getScratch().isFrame()) {
//				q.setScratch(q.getScratch().add(Scratch.FRAME_CORNER));
				return PS.T_AXIS_WEST_FRAME3;
			} else {
				q.setScratch(q.getScratch().add(Scratch.FRAME));
				q.moveSouth();
			}
			return state;
		case T_AXIS_WEST_FRAME3:
			if(q.isMarked(this)) {
				q.mark(null);
				return PS.T_AXIS_END;
			} else {
				q.moveNorth();
			}
			return state;
		default:
			return DrawLatitudinalBorderTransition.I.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.T_AXIS_INIT,
				PS.T_AXIS_EAST_FRAME,
				PS.T_AXIS_EAST_FRAME2,
				PS.T_AXIS_EAST_FRAME3,
				PS.T_AXIS_WEST,
				PS.T_AXIS_WEST_FRAME,
				PS.T_AXIS_WEST_FRAME2,
				PS.T_AXIS_WEST_FRAME3,
				PS.T_AXIS_END);
		r.addAll(DrawLatitudinalBorderTransition.I.getStates());
		return r;
	}

}
