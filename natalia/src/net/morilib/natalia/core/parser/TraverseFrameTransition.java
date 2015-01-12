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

import net.morilib.natalia.core.Scratch;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.Transition;

public class TraverseFrameTransition
implements Transition<Scratch, ParserState> {

	//
	static final Transition<Scratch, ParserState> INSTANCE =
			new TraverseFrameTransition();

	@Override
	public ParserState transit(Quadro<Scratch> q, ParserState state) {
		switch(state) {
		case TFRAME_INIT:
			q.forward();
			return ParserState.TFRAME_FORWARD;
		case TFRAME_FORWARD:
			if(q.getScratch().isFrameCorner()) {
				q.turnRight();
				return ParserState.TFRAME_END;
			} else if(q.get().isBound()) {
				throw new IllegalStateException();
			} else if(q.get().isJunction()) {
				return ParserState.TWALL_INIT;
			} else if(q.getScratch().isFrame()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		case TWALL_END:
			q.forward();
			return ParserState.TFRAME_FORWARD;
		default:
			return TraverseWallTransition.INSTANCE.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.TFRAME_INIT,
				ParserState.TFRAME_FORWARD,
				ParserState.TFRAME_END);
		r.addAll(TraverseWallTransition.INSTANCE.getStates());
		return r;
	}

}
