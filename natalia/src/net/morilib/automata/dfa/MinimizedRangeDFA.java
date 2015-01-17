package net.morilib.automata.dfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.morilib.automata.DFA;
import net.morilib.automata.DFAState;
import net.morilib.automata.TextBound;
import net.morilib.range.Interval;
import net.morilib.range.RangeAdder;

public class MinimizedRangeDFA<T, A, B> implements DFA<T, A, B> {

	private class DFAS implements DFAState<T, A, B> {

		List<DFAState<T, A, B>> group;

		DFAS(Collection<DFAState<T, A, B>> c) {
			group = new ArrayList<DFAState<T, A, B>>(c);
		}

		@Override
		public DFAState<T, A, B> go(T alphabet) {
			DFAState<T, A, B> s, t;

			s = group.get(0).go(alphabet);
			return (t = groupmap.get(s)) != null ? t : getdfaw(s);
		}

		@SuppressWarnings("unchecked")
		@Override
		public DFAState<T, A, B> goInt(int x) {
			return go((T)Integer.valueOf(x));
		}

		@SuppressWarnings("unchecked")
		@Override
		public DFAState<T, A, B> goChar(char x) {
			return go((T)Integer.valueOf(x));
		}

		@Override
		public DFAState<T, A, B> goBound(TextBound bound) {
			DFAState<T, A, B> s, t;

			s = group.get(0).goBound(bound);
			return (t = groupmap.get(s)) != null ? t : getdfaw(s);
		}

		@Override
		public boolean isInitialState() {
			for(DFAState<T, A, B> s : group) {
				if(s.isInitialState())  return true;
			}
			return false;
		}

		@Override
		public Set<A> getAccepted() {
			Set<A> t = new HashSet<A>();

			for(DFAState<T, A, B> s : group) {
				t.addAll(s.getAccepted());
			}
			return t;
		}

		@Override
		public boolean isDead() {
			for(DFAState<T, A, B> s : group) {
				if(s.isDead())  return true;
			}
			return false;
		}

		@Override
		public boolean isAccepted() {
			for(DFAState<T, A, B> s : group) {
				if(s.isAccepted())  return true;
			}
			return false;
		}

		@Override
		public Set<T> getAlphabets() {
			Set<T> t = new HashSet<T>();

			for(DFAState<T, A, B> s : group) {
				t.addAll(s.getAlphabets());
			}
			return t;
		}

		@Override
		public Iterable<Interval> getAlphabetRanges() {
			RangeAdder a = new RangeAdder();

			for(DFAState<T, A, B> s : group) {
				for(Interval v : s.getAlphabetRanges()) {
					a.addInterval(v);
				}
			}
			return a.toRange().intervals();
		}

		@Override
		public Object getLabel(T alphabet) {
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

	}

	private class DFAW implements DFAState<T, A, B> {

		private DFAState<T, A, B> etat;

		DFAW(DFAState<T, A, B> s) {
			etat = s;
		}

		@Override
		public DFAState<T, A, B> go(T alphabet) {
			DFAState<T, A, B> s, t;

			s = etat.go(alphabet);
			return (t = groupmap.get(s)) != null ? t : getdfaw(s);
		}

		@SuppressWarnings("unchecked")
		@Override
		public DFAState<T, A, B> goInt(int x) {
			return go((T)Integer.valueOf(x));
		}

		@SuppressWarnings("unchecked")
		@Override
		public DFAState<T, A, B> goChar(char x) {
			return go((T)Integer.valueOf(x));
		}

		@Override
		public DFAState<T, A, B> goBound(TextBound bound) {
			DFAState<T, A, B> s, t;

			s = etat.goBound(bound);
			return (t = groupmap.get(s)) != null ? t : getdfaw(s);
		}

		@Override
		public boolean isInitialState() {
			return etat.isInitialState();
		}

		@Override
		public Set<A> getAccepted() {
			return etat.getAccepted();
		}

		@Override
		public boolean isDead() {
			return etat.isDead();
		}

		@Override
		public boolean isAccepted() {
			return etat.isAccepted();
		}

		@Override
		public Set<T> getAlphabets() {
			return etat.getAlphabets();
		}

		@Override
		public Iterable<Interval> getAlphabetRanges() {
			return etat.getAlphabetRanges();
		}

		@Override
		public Object getLabel(T alphabet) {
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

	}

	//
	private Map<DFAState<T, A, B>, DFAState<T, A, B>> groupmap =
			new HashMap<DFAState<T, A, B>, DFAState<T, A, B>>();
	private DFAState<T, A, B> initetat;
	private Map<DFAState<T, A, B>, DFAW> cache =
			new HashMap<DFAState<T, A, B>, DFAW>();

	private MinimizedRangeDFA() {
		// do nothing
	}

	//
	DFAW getdfaw(DFAState<T, A, B> s) {
		DFAW t;

		if((t = cache.get(s)) == null) {
			t = new DFAW(s);
			cache.put(s, t);
		}
		return t;
	}

	/**
	 * 
	 * @param dfa
	 * @return
	 */
	public static<T, A, B> MinimizedRangeDFA<T, A, B> newInstance(
			DFA<T, A, B> dfa) {
		Set<Set<DFAState<T, A, B>>> g;
		MinimizedRangeDFA<T, A, B> r;
		DFAState<T, A, B> x;

		r = new MinimizedRangeDFA<T, A, B>();
		g = DFAs.getEquivalentGroup(dfa);
		for(Set<DFAState<T, A, B>> t : g) {
			x = r.new DFAS(t);
			for(DFAState<T, A, B> s : t) {
				r.groupmap.put(s, x);
			}
		}

		if((x = r.groupmap.get(dfa.getInitialState())) != null) {
			r.initetat = x;
		} else {
			r.initetat = r.getdfaw(dfa.getInitialState());
		}
		return r;
	}

	@Override
	public DFAState<T, A, B> getInitialState() {
		return initetat;
	}

}
