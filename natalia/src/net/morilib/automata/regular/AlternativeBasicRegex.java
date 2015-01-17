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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2013/01/02
 */
public class AlternativeBasicRegex implements BasicRegex {

	//
	Set<BasicRegex> exprs;

	/**
	 * 
	 * @param exprs
	 */
	public AlternativeBasicRegex(BasicRegex... exprs) {
		this(Arrays.asList(exprs));
	}

	/**
	 * 
	 * @param exprs
	 */
	public AlternativeBasicRegex(Collection<BasicRegex> exprs) {
		this.exprs = new HashSet<BasicRegex>(exprs);
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.regular.BasicRegEx#simplify()
	 */
	@Override
	public BasicRegex simplify() {
		List<BasicRegex> l1 = new ArrayList<BasicRegex>();
		Set<BasicRegex>  s1;
		BasicRegex e, f, g, w;
		int nc = 0;

		for(BasicRegex z : exprs) {
			w = z.simplify();
			if(w.equals(BasicRegexUtils.NIHIL)) {
				// 0|A to A
				nc++;
			} else if(w instanceof AlternativeBasicRegex) {
				// A|(B|C) to A|B|C
				l1.addAll(((AlternativeBasicRegex)w).exprs);
			} else {
				l1.add(w);
			}
		}

		if(nc > 0 && nc == exprs.size()) {
			return BasicRegexUtils.NIHIL;
		} else if(l1.size() == 0) {
			return BasicRegexUtils.EPSILON;
		} else if(l1.size() == 1) {
			return l1.get(0);
		}

		// simplify A|A* or A*|A to A*
		s1 = new HashSet<BasicRegex>(l1);
		for(int i = 0; i < l1.size(); i++) {
			e = l1.get(i);
			for(int j = i + 1; j < l1.size(); j++) {
				f = l1.get(j);
				if(f instanceof StarClosureBasicRegex &&
						e instanceof StarClosureBasicRegex) {
					g = ((StarClosureBasicRegex)e).expr;
					w = ((StarClosureBasicRegex)f).expr;
					if(g.equals(w))  s1.remove(f);
				} else if(f instanceof StarClosureBasicRegex) {
					g = ((StarClosureBasicRegex)f).expr;
					if(e.equals(g))  s1.remove(e);
				} else if(e instanceof StarClosureBasicRegex) {
					g = ((StarClosureBasicRegex)e).expr;
					if(f.equals(g))  s1.remove(f);
				}
			}
		}

		if(s1.size() == 0) {
			return BasicRegexUtils.EPSILON;
		} else if(s1.size() == 1) {
			return s1.iterator().next();
		} else {
			return new AlternativeBasicRegex(s1);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int r = 17 * 91;

		for(BasicRegex e : exprs)  r = r * 37 + e.hashCode();
		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(!(o instanceof AlternativeBasicRegex)) {
			return false;
		} else {
			return exprs.equals(((AlternativeBasicRegex)o).exprs);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer b = new StringBuffer();
		String d = "";

		b.append("(");
		for(BasicRegex e : exprs) {
			b.append(d).append(e.toString());
			d = "|";
		}
		return b.append(")").toString();
	}

}
