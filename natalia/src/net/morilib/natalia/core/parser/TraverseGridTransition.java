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
public class TraverseGridTransition implements Transition {

	//
	static final Transition INSTANCE = new TraverseGridTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#transit(net.morilib.natalia.Quadro, net.morilib.natalia.ParserState)
	 */
	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case TGRID_INIT:
			q.setColumnRegister(1);
			q.turnLeft().mark(this);
			return ParserState.TGRID_FORWARD;
		case TGRID_FORWARD:
			if(q.getScratch().isBorder() &&
					q.getScratch().isVerticalFrame() &&
					!q.isMarked(this)) {
				q.turnLeft().turnLeft();
				return ParserState.TGRID_BACK;
			} else if(q.get().isJunction() &&
					!q.peekForward().isCell()) {
				return ParserState.EXTRACT_TEXT_INIT;
			} else if(q.getScratch().isBorder() ||
					q.getScratch().isFrame()) {
				if(q.getScratch().isBorderCrossing()) {
					q.setColumnRegister(q.getColumnRegister() + 1);
				}
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		case TGRID_BACK:
			if(!q.getScratch().isBorder()) {
				throw new IllegalStateException();
			} else if(q.isMarked(this)) {
				q.turnLeft();
				return ParserState.TGRID_END;
			} else {
				q.forward();
			}
			return state;
		case EXTRACT_TEXT_END:
			q.setColumnRegister(q.getColumnRegister() + 1);
			return ParserState.TGRID_FORWARD;
		default:
			return ExtractTextTransition.INSTANCE.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.TGRID_INIT,
				ParserState.TGRID_FORWARD,
				ParserState.TGRID_BACK,
				ParserState.TGRID_END);
		r.addAll(ExtractTextTransition.INSTANCE.getStates());
		return r;
	}

}
