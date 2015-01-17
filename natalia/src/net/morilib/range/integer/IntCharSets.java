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
package net.morilib.range.integer;

import java.util.Arrays;

import net.morilib.range.IntervalsInt;
import net.morilib.range.Range;
import net.morilib.range.RangeAdder;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2011/10/09
 */
public class IntCharSets {

	/**
	 * 
	 */
	public static final IntRange ALL_CHAR =
		new IntInterval(0, Character.MAX_VALUE);

	/**
	 * 
	 */
	public static final IntRange ASCII_CONTROLSPACE =
			new IntInterval('\n', '\n').
			join(new IntInterval('\t', '\t'));

	/**
	 * 
	 */
	public static final IntRange ASCII_WHITESPACE =
			ASCII_CONTROLSPACE.join(new IntInterval(' ', ' '));

	/**
	 * 
	 */
	public static final IntRange NOT_ASCII_WHITESPACE =
			ASCII_WHITESPACE.complement();

	/**
	 * 
	 */
	public static final IntRange ASCII_WORD =
			new IntInterval('A', 'Z').
			join(new IntInterval('a', 'z')).
			join(new IntInterval('0', '9')).
			join(new IntInterval('_', '_'));

	/**
	 * 
	 */
	public static final IntRange NOT_ASCII_WORD =
			ASCII_WORD.complement();

	/**
	 * The character set of the numbers by ASCII characters.
	 */
	public static final IntRange ASCII_NUMBERS =
			new IntInterval('0', '9');

	/**
	 * 
	 */
	public static final IntRange NOT_ASCII_NUMBERS =
			ASCII_NUMBERS.complement();

	//
	private static final int _CI_INIT = 0;
	private static final int _CI_ON   = 1;

	/**
	 * 
	 * @param r
	 * @return
	 */
	public static Range toRange(IntRange r) {
		RangeAdder a = new RangeAdder();

		for(IntInterval v : r.intervals()) {
			a.add(IntervalsInt.newClosedInterval(v.left, v.right));
		}
		return a.toRange();
	}

	private static boolean _get(int[] a, int k) {
		int x, b;

		x = k / 32;
		b = k % 32;
		return (a[x] & (1 << b)) != 0;
	}

	private static void _set(int[] a, int k) {
		int x, b;

		if(k >= 0 && k < 65536) {
			x = k / 32;
			b = k % 32;
			a[x] |= 1 << b;
		}
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public static IntRange toCaseInsensitive(IntRange r) {
		IntRangeAdder s = new IntRangeAdder();
		int[] a = new int[65536 / 32];
		int st = _CI_INIT;
		int b = 0;

		Arrays.fill(a, 0);
		for(IntInterval v : r.intervals()) {
			b = v.right < 65536 ? v.right : 65536;
			for(int k = v.left < 0 ? 0 : v.left; k <= b; k++) {
				_set(a, Character.toUpperCase(k));
				_set(a, Character.toLowerCase(k));
			}
		}

		for(int k = 0; k < 65536; k++) {
			switch(st) {
			case _CI_INIT:
				if(_get(a, k)) {
					b = k;
					st = _CI_ON;
				}
				break;
			case _CI_ON:
				if(!_get(a, k)) {
					s.addInterval(new IntInterval(b, k - 1));
					st = _CI_INIT;
				}
				break;
			default:
				throw new RuntimeException();
			}
		}

		if(st == _CI_ON) {
			s.addInterval(new IntInterval(b, 65535));
		}
		return s.toRange();
	}

}
