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

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class SearchHorizontalAxisTransition implements Transition {

	static final Transition I = new SearchHorizontalAxisTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.core.parser.ParserState)
	 */
	@Override
	public PS transit(Quadro q, PS state) {
		switch(state) {
		case SEARCH_AXIS_INIT:
			if(q.getScratch().isFrame()) {
				q.moveWest();
				return PS.SEARCH_AXIS_R0;
			} else if(q.get().isWall()) {
				q.moveEast();
				return PS.SEARCH_AXIS_WALL;
			} else if(q.get().isJunction()) {
				q.moveEast();
				return PS.SEARCH_AXIS_FIND1;
			} else if(q.get().isBound()) {
				q.crlf();
			} else {
				q.moveEast();
			}
			return state;
		case SEARCH_AXIS_R0:
			if(q.getScratch().isFrame() || q.get().isBound()) {
				q.moveEast().moveSouth();
				return PS.SEARCH_AXIS_INIT;
			} else {
				q.moveWest();
			}
			return state;
		case SEARCH_AXIS_FIND1:
			if(q.get().isWall() || q.get().isJunction()) {
				q.moveEast();
				return PS.SEARCH_AXIS_FIND2;
			} else {
				return PS.SEARCH_AXIS_INIT;
			}
		case SEARCH_AXIS_FIND2:
			if(q.get().isWall() || q.get().isJunction()) {
				q.moveWest().moveWest();
				return PS.FRAME_INIT;
			} else {
				q.moveWest();
				return PS.SEARCH_AXIS_INIT;
			}
		case FRAME_END:
			q.turnNorth().moveEast().moveSouth();
			return PS.SEARCH_AXIS_INIT;
		case SEARCH_AXIS_WALL:
			if(q.get().isWall()) {
				q.moveEast();
			} else if(q.get().isJunction()) {
				q.moveWest();
				return PS.SEARCH_AXIS_R;
			} else {
				q.moveEast();
				return PS.SEARCH_AXIS_INIT;
			}
			return state;
		case SEARCH_AXIS_R:
			if(q.get().isWall()) {
				q.moveWest();
			} else {
				q.moveEast();
				return PS.SEARCH_AXIS_END;
			}
			return state;
		default:
			return FrameTransition.I.transit(q, state);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.SEARCH_AXIS_INIT,
				PS.SEARCH_AXIS_R0,
				PS.SEARCH_AXIS_FIND1,
				PS.SEARCH_AXIS_FIND2,
				PS.SEARCH_AXIS_WALL,
				PS.SEARCH_AXIS_R,
				PS.SEARCH_AXIS_END);
		r.addAll(FrameTransition.I.getStates());
		return r;
	}

}
