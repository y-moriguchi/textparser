package net.morilib.automata.misc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.morilib.range.Interval;
import net.morilib.range.IntervalsInt;

public class IntervalMap<T> {

	private SortedMap<Interval, Set<T>> map =
			new TreeMap<Interval, Set<T>>();

	public Set<T> get(Interval v) {
		for(Interval a : map.keySet()) {
			if(v.in(a)) {
				return new HashSet<T>(map.get(a));
			}
		}
		return Collections.emptySet();
	}

	private void _put(Interval k, T v) {
		Set<T> s;

		if((s = map.get(k)) == null) {
			s = new HashSet<T>();
			map.put(k, s);
		}
		s.add(v);
	}

	private void _putt(Interval k, Set<T> t) {
		map.put(k, new HashSet<T>(t));
	}

	private int infimumbound(Interval a) {
		Object o;

		o = a.getInfimumBound();
		return o == Interval.FROM_INFIMUM ? 0 : ((Integer)o).intValue();
	}

	private int supremumbound(Interval a) {
		Object o;

		o = a.getSupremumBound();
		return o == Interval.TO_SUPREMUM ?
				Integer.MAX_VALUE : ((Integer)o).intValue();
	}

	public void put(Interval intv, T v) {
		SortedMap<Interval, Set<T>> r;
		int xa, ya, xk, yk;
		Interval k = intv;
		Set<T> t;

		if(k.getInfimumBound() instanceof Integer) {
			r = new TreeMap<Interval, Set<T>>(map);
			for(Interval a : r.keySet()) {
				if(a.isEmpty())  continue;
				xa = infimumbound(a);  ya = supremumbound(a);
				xk = infimumbound(k);  yk = supremumbound(k);
				if(a.equals(k)) {
					_put(k, v);
					return;
				} else if(a.independentOf(k)) {
					// do nothing
				} else if(xa == xk && ya < yk) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xk, ya);
					_putt(k, t);  _put(k, v);
					k = IntervalsInt.newClosedInterval(ya + 1, yk);
					// next
				} else if(xa == xk && yk < ya) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xk, yk);
					_putt(k, t);  _put(k, v);
					k = IntervalsInt.newClosedInterval(yk + 1, ya);
					_putt(k, t);
					return;
				} else if(xa < xk && ya == yk) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xa, xk - 1);
					_putt(k, t);
					k = IntervalsInt.newClosedInterval(xk, yk);
					_putt(k, t);  _put(k, v);
					return;
				} else if(xk < xa && ya == yk) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xk, xa - 1);
					_put(k, v);
					k = IntervalsInt.newClosedInterval(xa, yk);
					_putt(k, t);  _put(k, v);
					return;
				} else if(xa < xk && xk <= ya && ya < yk) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xa, xk - 1);
					_putt(k, t);
					k = IntervalsInt.newClosedInterval(xk, ya);
					_putt(k, t);  _put(k, v);
					k = IntervalsInt.newClosedInterval(ya + 1, yk);
					// next
				} else if(xa < xk && yk < ya) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xa, xk - 1);
					_putt(k, t);
					k = IntervalsInt.newClosedInterval(xk, yk);
					_putt(k, t);  _put(k, v);
					k = IntervalsInt.newClosedInterval(yk + 1, ya);
					_putt(k, t);
					return;
				} else if(xk < xa && ya < yk) {
					t = map.remove(a);
					k = IntervalsInt.newClosedInterval(xk, xa - 1);
					_put(k, v);
					k = IntervalsInt.newClosedInterval(xa, ya);
					_putt(k, t);  _put(k, v);
					k = IntervalsInt.newClosedInterval(ya + 1, yk);
					// next
				}
			}
		}
		_put(k, v);
	}

	public Set<Interval> keySet() {
		return map.keySet();
	}

}
