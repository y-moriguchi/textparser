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

public class DrawBorderTransition implements Transition {

	//
	static final Transition INSTANCE = new DrawBorderTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#transit(net.morilib.natalia.Quadro, net.morilib.natalia.ParserState)
	 */
	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case DRAW_BORDER_INIT:
			q.mark(this).turnRight().forward();
			return ParserState.DRAW_BORDER_RIGHT;
		case DRAW_BORDER_RIGHT:
			if(q.getScratch().isFrame()) {
				q.setScratch(q.getScratch().add(
						Scratch.BORDER_CROSSING));
				q.turnLeft().turnLeft().forward();
				return ParserState.DRAW_BORDER_LEFT_1;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			} else if(q.isDirectionLongitudinal() &&
					q.getScratch().isLatitudinalBorder()) {
				q.setScratch(Scratch.BORDER_CROSSING).forward();
			} else if(q.isDirectionLatitudinal() &&
					q.getScratch().isLongitudinalBorder()) {
				q.setScratch(Scratch.BORDER_CROSSING).forward();
			} else if(q.isDirectionEast() || q.isDirectionWest()) {
				q.setScratch(q.getScratch().add(
						Scratch.LONGITUDINAL_BORDER)).forward();
			} else {
				q.setScratch(q.getScratch().add(
						Scratch.LATITUDINAL_BORDER)).forward();
			}
			return state;
		case DRAW_BORDER_LEFT_1:
			if(q.isMarked(this)) {
				return ParserState.DRAW_BORDER_LEFT_2;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			} else {
				q.forward();
			}
			return state;
		case DRAW_BORDER_LEFT_2:
			if(q.getScratch().isFrame()) {
				q.setScratch(q.getScratch().add(
						Scratch.BORDER_CROSSING));
				q.turnLeft().turnLeft().forward();
				return ParserState.DRAW_BORDER_LEFT_3;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			} else if(q.isDirectionLongitudinal() &&
					q.getScratch().isLatitudinalBorder()) {
				q.setScratch(Scratch.BORDER_CROSSING).forward();
			} else if(q.isDirectionLatitudinal() &&
					q.getScratch().isLongitudinalBorder()) {
				q.setScratch(Scratch.BORDER_CROSSING).forward();
			} else if(q.isDirectionEast() || q.isDirectionWest()) {
				q.setScratch(q.getScratch().add(
						Scratch.LONGITUDINAL_BORDER)).forward();
			} else {
				q.setScratch(q.getScratch().add(
						Scratch.LATITUDINAL_BORDER)).forward();
			}
			return state;
		case DRAW_BORDER_LEFT_3:
			if(q.isMarked(this)) {
				q.mark(null).setScratch(Scratch.BORDER_CROSSING);
				q.turnLeft();
				return ParserState.DRAW_BORDER_END;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			} else {
				q.forward();
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
		return EnumSet.of(ParserState.DRAW_BORDER_INIT,
				ParserState.DRAW_BORDER_RIGHT,
				ParserState.DRAW_BORDER_LEFT_1,
				ParserState.DRAW_BORDER_LEFT_2,
				ParserState.DRAW_BORDER_LEFT_3,
				ParserState.DRAW_BORDER_END);
	}

}
