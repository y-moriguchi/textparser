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
package net.morilib.natalia.lba2d;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public final class LBAs {

	private LBAs() {}

	/**
	 * 
	 * @param lba
	 * @param quadro
	 * @param state
	 * @param accept
	 * @return
	 */
	public static<S, P> boolean transit(LBATransition<S, P> lba,
			Quadro<S> quadro, P state, P accept) {
		List<ID<S, P>> l;
		ID<S, P> d;

		l = new ArrayList<ID<S, P>>();
		l.add(new ID<S, P>(quadro, state));
		while(l.size() > 0) {
			d = l.remove(l.size() - 1);
			if(d.getState().equals(accept)) {
				return true;
			}
			l.addAll(lba.transit(d));
		}
		return false;
	}

}
