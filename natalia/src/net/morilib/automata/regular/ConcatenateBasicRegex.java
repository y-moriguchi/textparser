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
public class ConcatenateBasicRegex implements BasicRegex {

	//
	private List<BasicRegex> exprs;

	/**
	 * 
	 * @param exprs
	 */
	public ConcatenateBasicRegex(BasicRegex... exprs) {
		this.exprs = Arrays.asList(exprs);
	}

	/**
	 * 
	 * @param exprs
	 */
	public ConcatenateBasicRegex(Collection<BasicRegex> exprs) {
		this.exprs = new ArrayList<BasicRegex>(exprs);
	}

	//
	private void set0(Set<List<BasicRegex>> s1, BasicRegex e) {
		if(s1.isEmpty()) {
			List<BasicRegex> l1;

			l1 = new ArrayList<BasicRegex>();
			if(e instanceof ConcatenateBasicRegex) {
				l1.addAll(((ConcatenateBasicRegex)e).exprs);
			} else {
				l1.add(e);
			}
			s1.add(l1);
		} else {
			for(List<BasicRegex> l1 : s1) {
				if(e instanceof ConcatenateBasicRegex) {
					l1.addAll(((ConcatenateBasicRegex)e).exprs);
				} else {
					l1.add(e);
				}
			}
		}
	}

	//
	private void set0(Set<List<BasicRegex>> s1, Set<BasicRegex> s2) {
		Set<List<BasicRegex>> s3;
		List<BasicRegex> l1;

		if(s1.isEmpty()) {
			for(BasicRegex e : s2) {
				l1 = new ArrayList<BasicRegex>();
				if(e instanceof ConcatenateBasicRegex) {
					l1.addAll(((ConcatenateBasicRegex)e).exprs);
				} else {
					l1.add(e);
				}
				s1.add(l1);
			}
		} else {
			s3 = new HashSet<List<BasicRegex>>(s1);
			s1.clear();
			for(List<BasicRegex> l0 : s3) {
				for(BasicRegex e : s2) {
					l1 = new ArrayList<BasicRegex>(l0);
					if(e instanceof ConcatenateBasicRegex) {
						l1.addAll(((ConcatenateBasicRegex)e).exprs);
					} else {
						l1.add(e);
					}
					s1.add(l1);
				}
			}
		}
	}

	private BasicRegex tore(List<BasicRegex> l) {
		if(l.size() == 1) {
			return l.get(0);
		} else {
			return new ConcatenateBasicRegex(l);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.regular.BasicRegEx#simplify()
	 */
	@Override
	public BasicRegex simplify() {
		Set<List<BasicRegex>> s1 = new HashSet<List<BasicRegex>>();
		List<BasicRegex> l0 = new ArrayList<BasicRegex>(), l1;
		Set<BasicRegex> s2;
		BasicRegex e, f, g, w;
		int pb, pe;

		for(BasicRegex z : exprs) {
			w = z.simplify();
			if(w.equals(BasicRegexUtils.NIHIL)) {
				// A0 to 0
				return BasicRegexUtils.NIHIL;
			} else if(!w.equals(BasicRegexUtils.EPSILON)) {
				// Ae to A
				l0.add(w);
			}
		}

		if(l0.size() == 0) {
			return BasicRegexUtils.EPSILON;
		} else if(l0.size() == 1) {
			return l0.get(0);
		}

		// simplify AAAA* or A*AAAA to A*
		l1 = new ArrayList<BasicRegex>();
		f  = null;
		pb = 0;  pe = 1;
		for(int i = 0; i < l0.size(); i++) {
			e = l0.get(i);
			if(e instanceof StarClosureBasicRegex &&
					f instanceof StarClosureBasicRegex) {
				// case A*A*
				g = ((StarClosureBasicRegex)e).expr;
				w = ((StarClosureBasicRegex)f).expr;
				if(!g.equals(w))  l1.addAll(l0.subList(pb, pe));
				pb = pe;  pe++;  f = e;
			} else if(f != null && f.equals(e)) {
				pe++;
			} else {
				l1.addAll(l0.subList(pb, pe));
				pb = pe;  pe++;  f = e;
			}
		}
		for(int i = pb; i < pe - 1; i++)  l1.add(f);

		// simplify A(B|C) to AB|AC
		for(BasicRegex z : l1) {
			if(z instanceof AlternativeBasicRegex) {
				set0(s1, ((AlternativeBasicRegex)z).exprs);
			} else {
				set0(s1, z);
			}
		}

		if(s1.size() == 1) {
			return tore(s1.iterator().next());
		} else {
			s2 = new HashSet<BasicRegex>();
			for(List<BasicRegex> z : s1)  s2.add(tore(z));
			return new AlternativeBasicRegex(s2);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int r = 17 * 77;

		for(int i = 0; i < exprs.size(); i++) {
			r = r * 37 + exprs.get(i).hashCode();
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		ConcatenateBasicRegex r;

		if(!(o instanceof ConcatenateBasicRegex)) {
			return false;
		} else if(exprs.size() !=
				(r = (ConcatenateBasicRegex)o).exprs.size()) {
			return false;
		} else {
			for(int i = 0; i < exprs.size(); i++) {
				if(!exprs.get(i).equals(r.exprs.get(i)))  return false;
			}
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer b = new StringBuffer();
		String d = "";

		b.append("(");
		for(int i = 0; i < exprs.size(); i++) {
			b.append(d).append(exprs.get(i).toString());
			d = "&";
		}
		return b.append(")").toString();
	}

}
