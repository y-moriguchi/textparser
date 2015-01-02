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
public class TraverseRowTransition implements Transition {

	//
	static final Transition INSTANCE = new TraverseRowTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#transit(net.morilib.natalia.Quadro, net.morilib.natalia.ParserState)
	 */
	@Override
	public ParserState transit(Quadro q, ParserState state) {
		switch(state) {
		case TROW_INIT:
			q.setRowRegister(1);
			q.turnSouth().forward();
			return ParserState.TROW_FORWARD;
		case TROW_FORWARD:
			if(q.getScratch().isBorderCrossing()) {
				return ParserState.TGRID_INIT;
			} else if(q.getScratch().isBorder()) {
				q.forward();
			} else if(q.getScratch().isFrame()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		case TGRID_END:
			if(q.getScratch().isFrameCorner()) {
				return ParserState.TROW_END;
			} else {
				q.setRowRegister(q.getRowRegister() + 1).forward();
				return ParserState.TROW_FORWARD;
			}
		default:
			return TraverseGridTransition.INSTANCE.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.TROW_INIT,
				ParserState.TROW_FORWARD,
				ParserState.TROW_END);
		r.addAll(TraverseGridTransition.INSTANCE.getStates());
		return r;
	}

}
