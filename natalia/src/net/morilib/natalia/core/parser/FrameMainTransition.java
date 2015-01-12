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

/**
 *
 */
public class FrameMainTransition
implements Transition<Scratch, ParserState> {

	//
	static final Transition<Scratch, ParserState> INSTANCE =
			new FrameMainTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#transit(net.morilib.natalia.Quadro, net.morilib.natalia.ParserState)
	 */
	@Override
	public ParserState transit(Quadro<Scratch> q, ParserState state) {
		switch(state) {
		case FMAIN_INIT:
			return ParserState.FRAME_INIT;
		case FRAME_END:
			q.turnEast();
			return ParserState.TFRAME_INIT;
		case TFRAME_END:
			if(q.isDirectionWest()) {
				q.forward();
				return ParserState.FMAIN_RETURN;
			} else {
				return ParserState.TFRAME_INIT;
			}
		case FMAIN_RETURN:
			if(q.isDirectionEast()) {
				q.back();
				return ParserState.TROW_INIT;
			} else if(q.getScratch().isFrameCorner()) {
				q.turnRight().forward();
			} else if(q.getScratch().isFrame()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		case TROW_END:
			q.turnEast().forward();
			return ParserState.FMAIN_FORWARD;
		case FMAIN_FORWARD:
			if(q.getScratch().isFrameCorner()) {
				return ParserState.FMAIN_END;
			} else if(q.getScratch().isFrame()) {
				q.forward();
			} else {
				throw new IllegalStateException();
			}
			return state;
		default:
			if(FrameTransition.INSTANCE.getStates().contains(state)) {
				return FrameTransition.INSTANCE.transit(q, state);
			} else if(TraverseFrameTransition.INSTANCE.getStates()
					.contains(state)) {
				return TraverseFrameTransition.INSTANCE.transit(q,
						state);
			} else if(TraverseRowTransition.INSTANCE.getStates()
					.contains(state)) {
				return TraverseRowTransition.INSTANCE.transit(q,
						state);
			} else {
				throw new IllegalStateException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Transition#getStates()
	 */
	@Override
	public EnumSet<ParserState> getStates() {
		EnumSet<ParserState> r;

		r = EnumSet.of(ParserState.FMAIN_INIT,
				ParserState.FMAIN_FORWARD,
				ParserState.FMAIN_END);
		r.addAll(FrameTransition.INSTANCE.getStates());
		r.addAll(TraverseFrameTransition.INSTANCE.getStates());
		return r;
	}

}
