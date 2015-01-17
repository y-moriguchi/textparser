/*
 * Copyright 2009-2010 Yuichiro Moriguchi
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
package net.morilib.automata.regular;

import java.util.Set;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2013/01/02
 */
public class StarClosureBasicRegex implements BasicRegex {

	//
	BasicRegex expr;

	/**
	 * 
	 * @param exprs
	 */
	public StarClosureBasicRegex(BasicRegex expr) {
		this.expr = expr;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.regular.BasicRegEx#simplify()
	 */
	@Override
	public BasicRegex simplify() {
		BasicRegex e = expr.simplify();
		Set<BasicRegex> s;

		if(e.equals(BasicRegexUtils.NIHIL)) {
			// simplify 0* to e
			return BasicRegexUtils.EPSILON;
		} else if(e.equals(BasicRegexUtils.EPSILON)) {
			// simplify e* to e
			return e;
		} else if(e instanceof StarClosureBasicRegex) {
			// simplify A** to A*
			return e;
		} else if(e instanceof AlternativeBasicRegex) {
			// simplify (e|A)* to A*
			s = ((AlternativeBasicRegex)e).exprs;
			s.remove(BasicRegexUtils.EPSILON);
			e = new AlternativeBasicRegex(s);
			return new StarClosureBasicRegex(e);
		} else {
			return new StarClosureBasicRegex(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return expr.hashCode() * 83;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(!(o instanceof StarClosureBasicRegex)) {
			return false;
		} else {
			return expr.equals(((StarClosureBasicRegex)o).expr);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return expr.toString() + "*";
	}

}
