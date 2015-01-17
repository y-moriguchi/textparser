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
package net.morilib.range;

import java.util.ArrayList;
import java.util.List;

/**
 * Constant objects and utilities for character sets
 * implemented by the Range library.
 * <p>Rangeライブラリにより実装された文字セットの定数オブジェクトと
 * ユーティリティである.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class CharSets {

	/**
	 * 
	 *
	 *
	 * @author MORIGUCHI, Yuichiro 2011/10/09
	 */
	public interface CharSetHandler {

		/**
		 * 
		 * @param ch
		 */
		public void singleChar(int ch);

		/**
		 * 
		 * @param cb
		 * @param ce
		 */
		public void rangedChar(int cb, int ce);

		/**
		 * 
		 * @param ra
		 */
		public void addRange(Range ra);

	}

	/**
	 * 
	 */
	public static final Range ALL_CHAR =
		IntervalsInt.newClosedInterval(0, Integer.MAX_VALUE);

	/**
	 * 
	 */
	public static final Range DOT =
			complement(Interval.newPoint(Integer.valueOf('\n')));

	/**
	 * The character set of the numbers by ASCII characters.
	 * <p>ASCII文字による数字の文字セットである.
	 */
	public static final Range NUMBERS = newCharInterval('0', '9');

	/**
	 * 
	 */
	public static final Range NOT_NUMBER = complement(NUMBERS);

	/**
	 * 
	 */
	public static final Range ASCII_CONTROLSPACE =
			newCharInterval('\t', '\r');

	/**
	 * 
	 */
	public static final Range ASCII_WHITESPACE =
			ASCII_CONTROLSPACE.join(
					Interval.newPoint(Integer.valueOf(' ')));

	/**
	 * 
	 */
	public static final Range NOT_ASCII_WHITESPACE =
			complement(ASCII_WHITESPACE);

	/**
	 * 
	 */
	public static final Range ASCII_WORD = parse("a-zA-Z_0-9");

	/**
	 * 
	 */
	public static final Range NOT_ASCII_WORD = parse("^a-zA-Z_0-9");

	/**
	 * The character set of the numbers by ASCII characters.
	 * <p>ASCII文字による数字の文字セットである.
	 */
	public static final Range ASCII_NUMBERS = newCharInterval('0', '9');

	/**
	 * 
	 */
	public static final Range NOT_ASCII_NUMBERS =
			complement(ASCII_NUMBERS);

	/**
	 * The character set of the capital alphabets.
	 * <p>英大文字の文字セットである.
	 */
	public static final Range CAPITAL_LETTERS =
		newCharInterval('A', 'Z');

	/**
	 * The character set of the small alphabets.
	 * <p>英小文字の文字セットである.
	 */
	public static final Range SMALL_LETTERS = newCharInterval('a', 'z');

	/**
	 * The character set of the English alphabets.
	 * <p>英文字の文字セットである.
	 */
	public static final Range ENGLISH_ALPHABETS =
		Ranges.sum(CAPITAL_LETTERS, SMALL_LETTERS);

	/**
	 * The character set of the English alphabets and numbers.
	 * <p>英数字の文字セットである.
	 */
	public static final Range ENGLISH_ALPHABETS_NUMBERS =
		Ranges.sum(ENGLISH_ALPHABETS, ASCII_NUMBERS);

	/**
	 * The character set of the Japanese hiraganas.
	 * <p>ひらがなの文字セットである.
	 */
	public static final Range HIRAGANA =
		newCharInterval('\u3041', '\u3094');

	/**
	 * The character set of the Japanese katakanas.
	 * <p>カタカナの文字セットである.
	 */
	public static final Range KATAKANA =
		newCharInterval('\u30a1', '\u30fa');

	/**
	 * The character set of the white spaces by ASCII characters.
	 * <p>ASCII文字による空白の文字セットである.
	 */
	public static final Range SPACES = Ranges.sum(
			IntervalsInt.newPoint(' '), IntervalsInt.newPoint('\t'));

	/**
	 * The character set of the token separators by ASCII characters.
	 * <p>ASCII文字によるトークン区切り文字の文字セットである.
	 */
	public static final Range TOKEN = Ranges.sum(
			IntervalsInt.newPoint(' '), IntervalsInt.newPoint('\t'));

	//
	private CharSets() {}

	/**
	 * creates a new character set of the given characters.
	 * <p>与えられた文字を端点とする文字セットを生成する.
	 * 
	 * @param f  the starting character
	 * @param t  the ending character
	 * @return  a new character set
	 */
	public static final Range newCharInterval(char f, char t) {
		return IntervalsInt.newClosedInterval((int)f, (int)t);
	}

	/**
	 * 
	 * @param f
	 * @param t
	 * @return
	 */
	public static final Range newCharInterval(char c) {
		return IntervalsInt.newClosedInterval((int)c, (int)c);
	}

	//
	private static Interval getintv(int ch, int ch2) {
		return IntervalsInt.newClosedInterval(ch, ch2);
	}

	/**
	 * 
	 * @param s
	 * @param start
	 * @param h
	 */
	public static void parse(CharSequence s, int start,
			CharSetHandler h) {
		int c, cb = -1, pt = start, st = 1;
		StringBuilder b = new StringBuilder();

		cnt: while(pt < s.length()) {
			c = s.charAt(pt++);
			if(st == 1) {
				if(c == '\\') {
					st = 2;
				} else {
					cb = c;  st = 4;
				}
			} else if(st == 2) {
				switch(c) {
				case 'x':
				case 'u':   st = 3;  break;
				case '\\':  cb = c;  st = 4;  break;
				case 't':   cb = '\t';  st = 4;  break;
				case 'n':   cb = '\n';  st = 4;  break;
				case 'r':   cb = '\r';  st = 4;  break;
				case 'f':   cb = '\f';  st = 4;  break;
				case 'd':
					h.addRange(NUMBERS);  st = 1;  break;
				case 'D':
					h.addRange(NOT_NUMBER);  st = 1;  break;
				case 's':
					h.addRange(ASCII_WHITESPACE);  st = 1;  break;
				case 'S':
					h.addRange(NOT_ASCII_WHITESPACE);  st = 1;  break;
				case 'w':
					h.addRange(ASCII_WORD);  st = 1;  break;
				case 'W':
					h.addRange(NOT_ASCII_WORD);  st = 1;  break;
				default:    cb = c;  st = 4;  break;
				}
			} else if(st == 3) {
				if(pt + 2 >= s.length()) {
					throw new CharSetException();
				}

				b.append((char)c);
				b.append(s.charAt(pt));  pt++;
				b.append(s.charAt(pt));  pt++;
				b.append(s.charAt(pt));  pt++;
				try {
					cb = Integer.parseInt(b.toString(), 16);
					b.delete(0, b.length());
					st = 4;
				} catch(NumberFormatException e) {
					throw new RuntimeException("Illegal code");
				}
			} else if(st == 4) {
				if(c == '\\') {
					h.singleChar(cb);
					st = 2;
				} else if(c == '-') {
					st = 5;
				} else {
					h.singleChar(cb);
					cb = c;  st = 4;
				}
			} else if(st == 5) {
				if(c == '\\') {
					st = 6;
				} else {
					if(c < cb) {
						throw new CharSetException();
					}
					h.rangedChar(cb, c);
					st = 1;
				}
			} else if(st == 6) {
				switch(c) {
				case 'x':
				case 'u':   st = 7;  continue cnt;
				case '\\':  break;
				case 't':   c = '\t';  break;
				case 'n':   c = '\n';  break;
				case 'r':   c = '\r';  break;
				default:    break;
				}

				if(c < cb) {
					throw new CharSetException(cb + ">" + c);
				}
				h.rangedChar(cb, c);
				st = 1;
			} else if(st == 7) {
				if(pt + 2 >= s.length()) {
					throw new CharSetException();
				}

				b.append((char)c);
				b.append(s.charAt(pt));  pt++;
				b.append(s.charAt(pt));  pt++;
				b.append(s.charAt(pt));  pt++;
				try {
					int cz = Integer.parseInt(b.toString(), 16);

					if(cz < cb) {
						throw new CharSetException();
					}
					h.rangedChar(cb, cz);
					b.delete(0, b.length());
					st = 1;
				} catch(NumberFormatException e) {
					throw new RuntimeException("Illegal code");
				}
			}
		}

		if(st == 4) {
			h.singleChar(cb);
		}
	}

	/**
	 * 
	 * @param ra
	 * @return
	 */
	public static List<Interval> complement(Iterable<Interval> ra) {
		List<Interval> rb = new ArrayList<Interval>();
		int l = 0, m = -1;

		for(Interval v : ra) {
			m = ((Integer)v.getInfimumBound()).intValue();
			if(l <= m - 1) {
				rb.add(IntervalsInt.newClosedInterval(l, m - 1));
			}
			l = ((Integer)v.getSupremumBound()).intValue();
			if(l < Integer.MAX_VALUE)  l++;
		}

		if(l < Integer.MAX_VALUE - 1) {
			rb.add(IntervalsInt.newClosedInterval(l,
					Integer.MAX_VALUE));
		}
		return rb;
	}

	/**
	 * 
	 * @param ra
	 * @return
	 */
	public static Range complement(Range ra) {
		RangeAdder rb = new RangeAdder();

		for(Interval v : complement(ra.intervals())) {
			rb.addInterval(v);
		}
		return rb.toRange();
	}

	/**
	 * 
	 * @param s
	 * @param cset
	 * @return
	 */
	public static Range parse(CharSequence s) {
		final RangeAdder ra = new RangeAdder();
		boolean c;
		int k = 0;

		if(s.length() == 0) {
			throw new CharSetException();
		} else if(c = s.charAt(0) == '^') {
			k++;
		}

		parse(s, k, new CharSetHandler() {

			public void singleChar(int ch) {
				ra.add(getintv(ch, ch));
			}

			public void rangedChar(int cb, int ce) {
				ra.add(getintv(cb, ce));
			}

			public void addRange(Range r) {
				ra.add(r);
			}

		});
		return c ? complement(ra.toRange()) : ra.toRange();
	}

	//
	private static List<Interval> parseTr(CharSequence s) {
		final List<Interval> ra = new ArrayList<Interval>();
		boolean c;
		int k = 0;

		if(s.length() == 0) {
			throw new CharSetException();
		} else if(c = s.charAt(0) == '^') {
			k++;
		}

		parse(s, k, new CharSetHandler() {

			public void singleChar(int ch) {
				ra.add(getintv(ch, ch));
			}

			public void rangedChar(int cb, int ce) {
				ra.add(getintv(cb, ce));
			}

			public void addRange(Range r) {
				RangeAdder b = new RangeAdder();

				for(Interval v : ra)  b.addInterval(v);
				b.add(r);
				ra.clear();
				for(Interval v : b.toRange().intervals())  ra.add(v);
			}

		});
		return c ? complement(ra) : ra;
	}

	//
	private static int card(Interval r1) {
		return (int)IntervalsInt.cardinality(r1);
	}

	//
	private static int card(List<Interval> r1) {
		int r = 0;

		for(int i = 0; i < r1.size(); i++) {
			r += (int)IntervalsInt.cardinality(r1.get(i));
		}
		return r;
	}

	/**
	 * 
	 * @param s
	 * @param t1
	 * @param t2
	 * @param cset
	 * @return
	 */
	public static String tr(CharSequence s, CharSequence t1,
			CharSequence t2) {
		StringBuilder b = new StringBuilder();
		List<Interval> r1 = parseTr(t1);
		List<Interval> r2 = parseTr(t2);

		if(card(r1) != card(r2)) {
			throw new IllegalArgumentException();
		}

		outer: for(int i = 0; i < s.length(); i++) {
			int ind = 0;
			int c   = s.charAt(i);

			for(int j = 0; j < r1.size(); j++) {
				Interval i1 = r1.get(j);

				if(i1.contains(c)) {
					int in2 = 0;
					int c1  = (int)IntervalsInt.indexOf(i1, c);

					for(int k = 0; k < r2.size(); k++) {
						int cd2 = card(r2.get(k));

						if(ind + c1 < in2 + cd2) {
							b.append((char)IntervalsInt.intAt(
									r2.get(k), c1));
							continue outer;
						}
						in2 += cd2;
					}
					throw new RuntimeException();
				}
				ind += card(i1);
			}
			b.append((char)c);
		}
		return b.toString();
	}

	/**
	 * 
	 * @param s
	 * @param start
	 * @return
	 */
	public static List<Range> parseSequence(CharSequence s,
			int start) {
		StringBuilder b = new StringBuilder();
		int c, pt = start, st = 1;
		List<Range> l;

		l = new ArrayList<Range>();
		while(pt < s.length()) {
			c = s.charAt(pt++);
			if(st == 1) {
				if(c == '\\') {
					st = 2;
				} else {
					l.add(Interval.newPoint(Integer.valueOf(c)));
				}
			} else if(st == 2) {
				switch(c) {
				case 'x':
				case 'u':   st = 3;  break;
				case '\\':
					l.add(Interval.newPoint(Integer.valueOf(c)));
					st = 1;  break;
				case 'd':
					l.add(NUMBERS);  st = 1;  break;
				case 'D':
					l.add(NOT_NUMBER);  st = 1;  break;
				case 's':
					l.add(ASCII_WHITESPACE);  st = 1;  break;
				case 'S':
					l.add(NOT_ASCII_WHITESPACE);  st = 1;  break;
				case 'w':
					l.add(ASCII_WORD);  st = 1;  break;
				case 'W':
					l.add(NOT_ASCII_WORD);  st = 1;  break;
				default:
					l.add(Interval.newPoint(Integer.valueOf(c)));
					st = 1;  break;
				}
			} else if(st == 3) {
				if(pt + 2 >= s.length()) {
					throw new CharSetException();
				}

				b.append((char)c);
				b.append(s.charAt(pt));  pt++;
				b.append(s.charAt(pt));  pt++;
				b.append(s.charAt(pt));  pt++;
				try {
					c = Integer.parseInt(b.toString(), 16);
					b.delete(0, b.length());
					l.add(Interval.newPoint(Integer.valueOf(c)));
					st = 1;
				} catch(NumberFormatException e) {
					throw new RuntimeException("Illegal code");
				}
			}
		}
		return l;
	}

}
