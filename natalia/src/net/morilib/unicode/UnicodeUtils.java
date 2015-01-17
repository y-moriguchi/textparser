/*
 * Copyright 2013-2014 Yuichiro Moriguchi
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
package net.morilib.unicode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.morilib.range.CharSets;
import net.morilib.range.Range;
import net.morilib.range.integer.IntCharSets;
import net.morilib.range.integer.IntRange;
import net.morilib.regex.dfa.CharSet;

public final class UnicodeUtils {

	//
	private static final String PROPNAME = "/" +
			UnicodeUtils.class.getPackage().getName().replace('.', '/') +
			"/charset2.txt";
	private static final Map<String, IntRange> _RANGE;

	static {
		InputStream ins = null;
		BufferedReader rd;
		String s, p, q;
		int k;

		_RANGE = new HashMap<String, IntRange>();
		try {
			ins = CharSets.class.getResourceAsStream(PROPNAME);
			rd  = new BufferedReader(new InputStreamReader(ins));
			while((s = rd.readLine()) != null) {
				if(s.equals("") || s.charAt(0) == '#') {
					// do nothing
				} else if((k = s.indexOf('=')) < 0) {
					_RANGE.put(s, IntRange.O);
				} else {
					p = s.substring(0, k);
					q = s.substring(k + 1);
					_RANGE.put(p, CharSet.parse(q));
				}
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(ins != null) {
				try {
					ins.close();
				} catch(IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public static IntRange getPropertyIntRange(String p) {
		IntRange r;

		if((r = _RANGE.get(p)) != null) {
			return r;
		} else {
			return IntRange.O;
		}
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public static Range getPropertyRange(String p) {
		return IntCharSets.toRange(getPropertyIntRange(p));
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static int toKatakana(int c) {
		if(c < 0x3041) {
			return c;
		} else if(c <= 0x3096) {
			return c + (0x30A1 - 0x3041);
		} else {
			return c;
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String toKatakana(String s) {
		StringBuffer b;

		b = new StringBuffer();
		for(int k = 0; k < s.length(); k++) {
			b.append((char)toKatakana(s.charAt(k)));
		}
		return b.toString();
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static int toHiragana(int c) {
		if(c < 0x30A1) {
			return c;
		} else if(c <= 0x30F6) {
			return c + (0x3041 - 0x30A1);
		} else {
			return c;
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String toHiragana(String s) {
		StringBuffer b;

		b = new StringBuffer();
		for(int k = 0; k < s.length(); k++) {
			b.append((char)toHiragana(s.charAt(k)));
		}
		return b.toString();
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isFullWidthLatin(int c) {
		return c >= 0xFF01 && c <= 0xFF5E;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static int toFullWidthLatin(int c) {
		if(c < 0x21) {
			return c;
		} else if(c <= 0x7E) {
			return c + (0xFF01 - 0x21);
		} else {
			return c;
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String toFullWidthLatin(String s) {
		StringBuffer b;

		b = new StringBuffer();
		for(int k = 0; k < s.length(); k++) {
			b.append((char)toFullWidthLatin(s.charAt(k)));
		}
		return b.toString();
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static int toHalfWidthLatin(int c) {
		if(c < 0xFF01) {
			return c;
		} else if(c <= 0xFF5E) {
			return c + (0x21 - 0xFF01);
		} else {
			return c;
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String toHalfWidthLatin(String s) {
		StringBuffer b;

		b = new StringBuffer();
		for(int k = 0; k < s.length(); k++) {
			b.append((char)toHalfWidthLatin(s.charAt(k)));
		}
		return b.toString();
	}

}
