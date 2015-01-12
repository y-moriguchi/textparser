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

import net.morilib.natalia.core.Scratch;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.Transition;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class DBParseMainTransition
implements Transition<Scratch, PS> {

	//
	static final Transition<Scratch, PS> I =
			new DBParseMainTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.db.parser.PS)
	 */
	@Override
	public PS transit(Quadro<Scratch> q, PS state) {
		switch(state) {
		case POSTGRES_MAIN_INIT:
			return PS.SEARCH_AXIS_INIT;
		case SEARCH_AXIS_END:
			return PS.T_AXIS_INIT;
		case T_AXIS_END:
			return PS.EXT_TABLE_INIT;
		case EXT_TABLE_END:
			return PS.POSTGRES_MAIN_END;
		default:
			if(SearchHorizontalAxisTransition.I.getStates().contains(
					state)) {
				return SearchHorizontalAxisTransition.I.transit(q,
						state);
			} else if(TraverseHorizontalAxisTransition.I.getStates()
					.contains(state)) {
				return TraverseHorizontalAxisTransition.I.transit(q,
						state);
			} else if(ExtractTableTransition.I.getStates().contains(
					state)) {
				return ExtractTableTransition.I.transit(q, state);
			} else {
				throw new IllegalStateException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.POSTGRES_MAIN_INIT,
				PS.POSTGRES_MAIN_END);
		r.addAll(SearchHorizontalAxisTransition.I.getStates());
		r.addAll(TraverseHorizontalAxisTransition.I.getStates());
		r.addAll(ExtractTableTransition.I.getStates());
		return r;
	}

}
