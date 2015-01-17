/*
 * Copyright 2009 Yuichiro Moriguchi
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
package net.morilib.automata.dfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.morilib.automata.CharSequenceHead;
import net.morilib.automata.DFA;
import net.morilib.automata.DFAState;
import net.morilib.automata.TextBound;
import net.morilib.automata.misc.IntervalMap;
import net.morilib.automata.misc.LookaheadIterator;
import net.morilib.automata.misc.PairSet;
import net.morilib.automata.misc.WrappedLookaheadIterator;
import net.morilib.automata.regular.AlternativeBasicRegex;
import net.morilib.automata.regular.BasicRegex;
import net.morilib.automata.regular.BasicRegexUtils;
import net.morilib.automata.regular.ConcatenateBasicRegex;
import net.morilib.automata.regular.ObjectBasicRegex;
import net.morilib.automata.regular.StarClosureBasicRegex;
import net.morilib.range.Interval;

/**
 * 
 *
 *
 * @author MORIGUCHI, Yuichiro 2009
 */
public final class DFAs {

	//
	private DFAs() {
		// do nothing
	}

	/**
	 * 
	 */
	public static final DFAState<Object, Object, Object>
	DEAD_STATE = new DFAState<Object, Object, Object>() {

		public Set<Object> getAccepted() {
			return Collections.emptySet();
		}

		public DFAState<Object, Object, Object> go(Object a) {
			return this;
		}

		public DFAState<Object, Object, Object> goBound(TextBound a) {
			return this;
		}

		public boolean isInitialState() {
			return false;
		}

		public boolean isDead() {
			return true;
		}

		@Override
		public DFAState<Object, Object, Object> goInt(int x) {
			return this;
		}

		@Override
		public DFAState<Object, Object, Object> goChar(char x) {
			return this;
		}

		@Override
		public boolean isAccepted() {
			return false;
		}

		@Override
		public Set<Object> getAlphabets() {
			return Collections.emptySet();
		}

		@Override
		public Iterable<Interval> getAlphabetRanges() {
			return Collections.emptySet();
		}

		@Override
		public Object getLabel(Object alphabet) {
			return null;
		}

		@Override
		public Object getLabelInt(int x) {
			return null;
		}

		@Override
		public Object getLabelChar(char x) {
			return null;
		}

	};

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T, A, B> DFAState<T, A, B> deadState() {
		return (DFAState<T, A, B>)DEAD_STATE;
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> input(
			DFAState<Integer, A, B> st, CharSequenceHead seq) {
		DFAState<Integer, A, B> s2 = st;
		int c;

		while(seq.hasNext()) {
			DFAState<Integer, A, B> s3 = s2;
			EnumSet<TextBound> bs = seq.getBounds();

			c = seq.readInt();
			while((s2 = s2.goInt(c)).isDead()) {
				for(TextBound b : bs) {
					s2 = s3.goBound(b);
					if(!s2.isDead()) {
						s3 = s2;
						break;
					}
				}
				if(s2.isDead()) {
					return Collections.emptySet();
				}
			}

		}

		loop: while(true) {
			DFAState<Integer, A, B> s3 = s2;

			for(TextBound b : seq.getBounds()) {
				s3 = s2.goBound(b);
				if(!s3.isDead()) {
					s2 = s3;
					continue loop;
				}
			}
			break;
		}
		return s2.getAccepted();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> input(
			DFAState<Integer, A, B> st, CharSequence seq) {
		/*DFAState<Integer, A, B> s2 = st;

		for(int i = 0; i < seq.length(); i++) {
			s2 = s2.go((int)seq.charAt(i));
			if(s2.isDead()) {
				return Collections.emptySet();
			}
		}
		return s2.getAccepted();*/
		return input(st, new CharSequenceHead(seq));
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> input(
			DFA<Integer, A, B> dfa, CharSequenceHead seq) {
		return input(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> input(
			DFA<Integer, A, B> dfa, CharSequence seq) {
		return input(dfa, new CharSequenceHead(seq));
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> match(
			DFAState<Integer, A, B> st, CharSequenceHead seq) {
		DFAState<Integer, A, B> s2 = st;
		int c;

		while(seq.hasNext()) {
			DFAState<Integer, A, B> s3 = s2;
			EnumSet<TextBound> bs = seq.getBounds();

			c = seq.readInt();
			while((s2 = s2.goInt(c)).isDead()) {
				for(TextBound b : bs) {
					s2 = s3.goBound(b);
					if(!s2.isDead()) {
						s3 = s2;
						break;
					}
				}

				if(s2.isDead()) {
					seq.unread();
					return s3.getAccepted();
				}
			}
		}

		loop: while(true) {
			DFAState<Integer, A, B> s3 = s2;

			for(TextBound b : seq.getBounds()) {
				s3 = s2.goBound(b);
				if(!s3.isDead()) {
					s2 = s3;
					continue loop;
				}
			}
			break;
		}
		return s2.getAccepted();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> match(
			DFAState<Integer, A, B> st, CharSequence seq) {
		return match(st, new CharSequenceHead(seq));
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> match(
			DFA<Integer, A, B> dfa, CharSequenceHead seq) {
		return match(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<A, B> Set<A> match(
			DFA<Integer, A, B> dfa, CharSequence seq) {
		return match(dfa, new CharSequenceHead(seq));
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<A, B> boolean isMatched(
			DFAState<Integer, A, B> st, CharSequenceHead seq) {
		return !match(st, seq).isEmpty();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<A, B> boolean isMatched(
			DFAState<Integer, A, B> st, CharSequence seq) {
		return isMatched(st, new CharSequenceHead(seq));
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<A, B> boolean isMatched(
			DFA<Integer, A, B> dfa, CharSequenceHead seq) {
		return isMatched(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<A, B> boolean isMatched(
			DFA<Integer, A, B> dfa, CharSequence seq) {
		return isMatched(dfa, new CharSequenceHead(seq));
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> match(
			DFAState<T, A, ?> st, LookaheadIterator<T> seq) {
		DFAState<T, A, ?> s2 = st;
		T c;

		while(seq.hasNext()) {
			DFAState<T, A, ?> s3 = s2;

			c = seq.peek();
			if((s2 = s2.go(c)).isDead()) {
				return s3.getAccepted();
			} else {
				seq.next();
			}
		}
		return s2.getAccepted();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> match(
			DFAState<T, A, ?> st, Collection<T> seq) {
		return match(st,
				new WrappedLookaheadIterator<T>(seq.iterator()));
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> match(
			DFA<T, A, ?> dfa, LookaheadIterator<T> seq) {
		return match(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> match(
			DFA<T, A, ?> dfa, Collection<T> seq) {
		return match(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T, A> boolean isMatched(
			DFAState<T, A, ?> st, LookaheadIterator<T> seq) {
		return !match(st, seq).isEmpty();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T, A> boolean isMatched(
			DFAState<T, A, ?> st, Collection<T> seq) {
		return !match(st, seq).isEmpty();
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T, A> boolean isMatched(
			DFA<T, A, ?> dfa, LookaheadIterator<T> seq) {
		return !match(dfa, seq).isEmpty();
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T, A> boolean isMatched(
			DFA<T, A, ?> dfa, Collection<T> seq) {
		return !match(dfa, seq).isEmpty();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> matchAll(DFAState<T, A, ?> st,
			Iterator<T> seq) {
		DFAState<T, A, ?> s2 = st;
		T c;

		while(seq.hasNext()) {
			c = seq.next();
			if((s2 = s2.go(c)).isDead()) {
				return Collections.emptySet();
			}
		}
		return s2.getAccepted();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> matchAll(
			DFAState<T, A, ?> st, Collection<T> seq) {
		return matchAll(st, seq.iterator());
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> matchAll(DFA<T, A, ?> dfa,
			Iterator<T> seq) {
		return matchAll(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T, A> Set<A> matchAll(
			DFA<T, A, ?> dfa, Collection<T> seq) {
		return matchAll(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T> boolean isMatchAll(DFAState<T, ?, ?> st,
			Iterator<T> seq) {
		DFAState<T, ?, ?> s2 = st;
		T c;

		while(seq.hasNext()) {
			c = seq.next();
			if((s2 = s2.go(c)).isDead()) {
				return false;
			}
		}
		return !s2.getAccepted().isEmpty();
	}

	/**
	 * 
	 * @param st
	 * @param seq
	 * @return
	 */
	public static<T> boolean isMatchAll(
			DFAState<T, ?, ?> st, Collection<T> seq) {
		return isMatchAll(st, seq.iterator());
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T> boolean isMatchAll(DFA<T, ?, ?> dfa,
			Iterator<T> seq) {
		return isMatchAll(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @param seq
	 * @return
	 */
	public static<T> boolean isMatchAll(
			DFA<T, ?, ?> dfa, Collection<T> seq) {
		return isMatchAll(dfa.getInitialState(), seq);
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> List<DFAState<T, A, B>> allStates(
			DFAState<T, A, B> state) {
		List<DFAState<T, A, B>> l = new ArrayList<DFAState<T, A, B>>();
		DFAState<T, A, B> s;

		l.add(state);
		for(int i = 0; i < l.size(); i++) {
			for(T o : l.get(i).getAlphabets()) {
				s = l.get(i).go(o);
				if(!l.contains(s))  l.add(s);
			}
		}
		return l;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> List<DFAState<T, A, B>> allStates(
			DFA<T, A, B> dfa) {
		return allStates(dfa.getInitialState());
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> List<DFAState<T, A, B>> allAcceptedStates(
			DFA<T, A, B> dfa) {
		List<DFAState<T, A, B>> l = allStates(dfa);
		Iterator<DFAState<T, A, B>> i;

		i = l.iterator();
		while(i.hasNext()) {
			if(!i.next().isAccepted())  i.remove();
		}
		return l;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T, A, B> List<DFAState<T, A, B>> allStateRanges(
			DFAState<T, A, B> state) {
		List<DFAState<T, A, B>> l = new ArrayList<DFAState<T, A, B>>();
		DFAState<T, A, B> s;

		l.add(state);
		for(int i = 0; i < l.size(); i++) {
			for(Interval o : l.get(i).getAlphabetRanges()) {
				if(o.isInfimumClosed()) {
					s = l.get(i).go((T)o.getInfimumBound());
				} else if(o.isSupremumClosed()) {
					s = l.get(i).go((T)o.getSupremumBound());
				} else {
					throw new RuntimeException();
				}
				if(!l.contains(s))  l.add(s);
			}
		}
		return l;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> List<DFAState<T, A, B>> allStateRanges(
			DFA<T, A, B> dfa) {
		return allStateRanges(dfa.getInitialState());
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> List<DFAState<T, A, B>> allAcceptedStateRanges(
			DFA<T, A, B> dfa) {
		List<DFAState<T, A, B>> l = allStateRanges(dfa);
		Iterator<DFAState<T, A, B>> i;

		i = l.iterator();
		while(i.hasNext()) {
			if(!i.next().isAccepted())  i.remove();
		}
		return l;
	}

	// R^(k)_ij
	private static<T, A, B> void writere(BasicRegex[][][] tbl,
			List<DFAState<T, A, B>> l,
			int m) {
		int n;

		for(int i = 0; i < tbl[m].length; i++) {
			for(T o : l.get(i).getAlphabets()) {
				n = l.indexOf(l.get(i).go(o));
				if(tbl[m][i][n] == null) {
					tbl[m][i][n] = new ObjectBasicRegex(o);
				} else {
					tbl[m][i][n] = new AlternativeBasicRegex(
							new ObjectBasicRegex(o),
							tbl[m][i][n]);
				}
			}
		}
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> BasicRegex convertToRegex(DFA<T, A, B> dfa) {
		List<DFAState<T, A, B>> l = allStates(dfa);
		BasicRegex[][][] tbl =
				new BasicRegex[l.size() + 1][l.size()][l.size()];
		BasicRegex e, f, g, h;
		int k;

		for(int m = 0; m < tbl.length; m++) {
			for(int i = 0; i < tbl[m].length; i++) {
				for(int j = 0; j < tbl[m][i].length; j++) {
					if(m == 0 && i == j) {
						tbl[m][i][j] = BasicRegexUtils.EPSILON;
					} else {
						tbl[m][i][j] = BasicRegexUtils.NIHIL;
					}
				}
			}
		}

		for(int m = 0; m < tbl.length; m++) {
			if(m == 0) {
				writere(tbl, l, m);   // R^(k)_ij
			} else {
				k = m - 1;
				for(int i = 0; i < tbl[m].length; i++) {
					for(int j = 0; j < tbl[m][i].length; j++) {
						tbl[m][i][j] = tbl[k][i][j];
					}
				}

				for(int i = 0; i < tbl[m].length; i++) {
					for(int j = 0; j < tbl[m][i].length; j++) {
						e = tbl[m    ][i][j];
						f = tbl[m - 1][i][k];
						g = tbl[m - 1][k][k];
						h = tbl[m - 1][k][j];
						g = new StarClosureBasicRegex(g);
						f = new ConcatenateBasicRegex(f, g, h);
						e = new AlternativeBasicRegex(e, f);
						tbl[m][i][j] = e.simplify();
					}
				}
			}
		}

		e = BasicRegexUtils.NIHIL;
		for(int i = 0; i < l.size(); i++) {
			if(l.get(i).isAccepted()) {
				f = tbl[tbl.length - 1][0][i];
				e = new AlternativeBasicRegex(f, e);
			}
		}
		return e.simplify();
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B>
	Set<DFAState<T, A, B>> getReachableStatesDiscrete(
			DFA<T, A, B> dfa) {
		Set<DFAState<T, A, B>> s = new HashSet<DFAState<T, A, B>>();
		Stack<DFAState<T, A, B>> st = new Stack<DFAState<T, A, B>>();
		DFAState<T, A, B> x, y;

		s.add(dfa.getInitialState());
		st.push(dfa.getInitialState());
		while(!st.isEmpty()) {
			x = st.pop();
			for(T a : x.getAlphabets()) {
				y = x.go(a);
				if(!y.isDead() && !s.contains(y)) {
					s.add(y);
					st.push(y);
				}
			}
		}
		return s;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> boolean isEmptyDiscrete(DFA<T, A, B> dfa) {
		Set<DFAState<T, A, B>> s = getReachableStatesDiscrete(dfa);

		for(DFAState<T, ?, ?> t : s) {
			if(t.isAccepted())  return false;
		}
		return true;
	}

	//
	private static<T, A, B> boolean finddist(
			Set<PairSet<DFAState<T, A, B>>> st,
			DFAState<T, ?, ?> a, DFAState<T, ?, ?> b) {
		if(a.isDead() && b.isDead()) {
			return false;
		} else if(a.isDead() || b.isDead()) {
			return true;
		} else if(a.equals(b)) {
			return false;
		} else {
			for(PairSet<DFAState<T, A, B>> p : st) {
				if(p.isPair(a, b))  return false;
			}
			return true;
		}
	}

	//
	static<T, A, B> Set<PairSet<DFAState<T, A, B>>> fillTable(
			List<DFAState<T, A, B>> l) {
		Iterator<PairSet<DFAState<T, A, B>>> itr;
		Set<PairSet<DFAState<T, A, B>>> r, s;
		PairSet<DFAState<T, A, B>> p;
		DFAState<T, A, B> a, b;
		boolean f, dirty = true;

		// remove accept and non-accept states
		r = new HashSet<PairSet<DFAState<T, A, B>>>();
		for(int i = 0; i < l.size(); i++) {
			for(int j = i + 1; j < l.size(); j++) {
				if(l.get(i).isAccepted() == l.get(j).isAccepted()) {
					r.add(new PairSet<DFAState<T, A, B>>(
							l.get(i), l.get(j)));
				}
			}
		}

		// remove distinguishable states
		while(dirty) {
			dirty = false;
			s = new HashSet<PairSet<DFAState<T, A, B>>>(r);
			itr = s.iterator();
			while(itr.hasNext()) {
				f = false;
				p = itr.next();
				a = p.get1();  b = p.get2();

				for(T t : a.getAlphabets()) {
					f |= finddist(r, a.go(t), b.go(t));
				}

				for(T t : b.getAlphabets()) {
					f |= finddist(r, a.go(t), b.go(t));
				}

				if(f) {
					itr.remove();
					dirty = true;
				}
			}
			r = new HashSet<PairSet<DFAState<T, A, B>>>(s);
		}
		return r;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> Set<PairSet<DFAState<T, A, B>>>
	getEquivalentStatesDiscrete(DFA<T, A, B> dfa) {
		List<DFAState<T, A, B>> l;

		l = new ArrayList<DFAState<T, A, B>>(
				getReachableStatesDiscrete(dfa));
		return fillTable(l);
	}

	/**
	 * 
	 * @param dfa1
	 * @param dfa2
	 * @return
	 */
	public static<T, A, B> boolean isEquivalentDiscrete(
			DFA<T, A, B> dfa1, DFA<T, A, B> dfa2) {
		Set<PairSet<DFAState<T, A, B>>> r;
		List<DFAState<T, A, B>> l;
		Set<DFAState<T, A, B>> s, t;

		if(dfa1.equals(dfa2)) {
			return true;
		} else if(isEmptyDiscrete(dfa1)) {
			return isEmptyDiscrete(dfa2);
		} else if(isEmptyDiscrete(dfa2)) {
			return false;
		}

		l = new ArrayList<DFAState<T, A, B>>();
		l.addAll(s = getReachableStatesDiscrete(dfa1));
		l.addAll(t = getReachableStatesDiscrete(dfa2));
		r = fillTable(l);

		for(DFAState<T, A, B> x : s) {
			for(PairSet<DFAState<T, A, B>> a : r) {
				if(a.contains(x))  t.remove(a.getOpposite(x));
			}
		}
		return t.isEmpty();
	}

	//
	@SuppressWarnings("unchecked")
	private static<T> T prendUnPoint(Interval a) {
		T t;

		if(a.isInfimumClosed()) {
			t = (T)a.getInfimumBound();
		} else if(a.isSupremumClosed()) {
			t = (T)a.getSupremumBound();
		} else {
			throw new UnsupportedOperationException();
		}
		return t;
	}

	//
	static<T, A, B> Set<PairSet<DFAState<T, A, B>>> fillTableRange(
			List<DFAState<T, A, B>> l) {
		Iterator<PairSet<DFAState<T, A, B>>> itr;
		Set<PairSet<DFAState<T, A, B>>> r, s;
		PairSet<DFAState<T, A, B>> p;
		DFAState<T, A, B> a, b;
		boolean f, dirty = true;
		IntervalMap<Object> v;
		T t;

		// remove accept and non-accept states
		r = new HashSet<PairSet<DFAState<T, A, B>>>();
		for(int i = 0; i < l.size(); i++) {
			for(int j = i + 1; j < l.size(); j++) {
//				if(l.get(i).isAccepted() == l.get(j).isAccepted()) {
				if(l.get(i).getAccepted().equals(
						l.get(j).getAccepted())) {
					r.add(new PairSet<DFAState<T, A, B>>(
							l.get(i), l.get(j)));
				}
			}
		}

		// remove distinguishable states
		while(dirty) {
			dirty = false;
			s = new HashSet<PairSet<DFAState<T, A, B>>>(r);
			itr = s.iterator();
			v = new IntervalMap<Object>();
			while(itr.hasNext()) {
				f = false;
				p = itr.next();
				a = p.get1();  b = p.get2();

				for(Interval q : a.getAlphabetRanges())  v.put(q, q);
				for(Interval q : b.getAlphabetRanges())  v.put(q, q);
				for(Interval q : v.keySet()) {
					t = prendUnPoint(q);
					f |= finddist(r, a.go(t), b.go(t));
				}

				if(f) {
					itr.remove();
					dirty = true;
				}
			}
			r = new HashSet<PairSet<DFAState<T, A, B>>>(s);
		}
		return r;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> Set<DFAState<T, A, B>> getReachableStates(
			DFA<T, A, B> dfa) {
		Stack<DFAState<T, A, B>> st = new Stack<DFAState<T, A, B>>();
		Set<DFAState<T, A, B>> s = new HashSet<DFAState<T, A, B>>();
		DFAState<T, A, B> x, y;
		T t;

		s.add(dfa.getInitialState());
		st.push(dfa.getInitialState());
		while(!st.isEmpty()) {
			x = st.pop();
			for(Interval a : x.getAlphabetRanges()) {
				t = prendUnPoint(a);
				y = x.go(t);
				if(!y.isDead() && !s.contains(y)) {
					s.add(y);
					st.push(y);
				}
			}
		}
		return s;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> Set<PairSet<DFAState<T, A, B>>>
	getEquivalentStates(DFA<T, A, B> dfa) {
		List<DFAState<T, A, B>> l;

		l = new ArrayList<DFAState<T, A, B>>(getReachableStates(dfa));
		return fillTableRange(l);
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> Set<Set<DFAState<T, A, B>>>
	getEquivalentGroup(DFA<T, A, B> dfa) {
		Set<PairSet<DFAState<T, A, B>>> t;
		Set<Set<DFAState<T, A, B>>> g;
		Set<DFAState<T, A, B>> q;

		t = getEquivalentStates(dfa);
		g = new HashSet<Set<DFAState<T, A, B>>>();
		for(PairSet<DFAState<T, A, B>> p : t) {
			q = null;
			for(Set<DFAState<T, A, B>> v : g) {
				if(g.contains(p.get1()) || g.contains(p.get2())) {
					q = v;
					break;
				}
			}

			if(q == null) {
				q = new HashSet<DFAState<T, A, B>>();
				g.add(q);
			}
			q.add(p.get1());  q.add(p.get2());
		}
		return g;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> boolean isEmpty(DFA<T, A, B> dfa) {
		Set<DFAState<T, A, B>> s = getReachableStates(dfa);

		for(DFAState<T, A, B> t : s) {
			if(t.isAccepted())  return false;
		}
		return true;
	}

	/**
	 * 
	 * @param dfa1
	 * @param dfa2
	 * @return
	 */
	public static<T, A, B> boolean isEquivalent(DFA<T, A, B> dfa1,
			DFA<T, A, B> dfa2) {
		Set<PairSet<DFAState<T, A, B>>> r;
		Set<DFAState<T, A, B>> s, t;
		List<DFAState<T, A, B>> l;

		if(dfa1.equals(dfa2)) {
			return true;
		} else if(isEmpty(dfa1)) {
			return isEmpty(dfa2);
		} else if(isEmpty(dfa2)) {
			return false;
		}

		l = new ArrayList<DFAState<T, A, B>>();
		l.addAll(s = getReachableStates(dfa1));
		l.addAll(t = getReachableStates(dfa2));
		r = fillTableRange(l);

		for(DFAState<T, A, B> x : s) {
			for(PairSet<DFAState<T, A, B>> a : r) {
				if(a.contains(x))  t.remove(a.getOpposite(x));
			}
		}
		return t.isEmpty();
	}

}
