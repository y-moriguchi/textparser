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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class QuadroFactory {

	//
	static int len(char c, boolean notfull) {
		Character.UnicodeBlock b;

		b = Character.UnicodeBlock.of(c);
		if(notfull) {
			return 1;
		} else if(b == null) {
			return 1;
		} else if(c >= 0xff01 && c <= 0xff60) {
			return 2;
		} else if(c >= 0xffe0 && c <= 0xffe6) {
			return 2;
		} else if(b.equals(Character.UnicodeBlock.CJK_COMPATIBILITY) ||
				b.equals(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) ||
				b.equals(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) ||
				b.equals(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT) ||
				b.equals(Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT) ||
				b.equals(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) ||
				b.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) ||
				b.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) ||
				b.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) ||
				b.equals(Character.UnicodeBlock.KATAKANA) ||
				b.equals(Character.UnicodeBlock.HIRAGANA) ||
				b.equals(Character.UnicodeBlock.HANGUL_SYLLABLES)) {
			return 2;
		} else {
			return 1;
		}
	}

	//
	static int len(String s, boolean notfull) {
		int r = 0;

		for(int i = 0; i < s.length(); i++) {
			r = r + len(s.charAt(i), notfull);
		}
		return r;
	}

	//
	private static Pixel[] toPixel(String s) {
		Pixel[] a;
		int m;

		a = new Pixel[len(s, false)];
		for(int i = 0, j = 0; j < s.length(); i += m, j++) {
			m = len(s.charAt(j), false);
			a[i] = Pixel.getPixel(s.charAt(j));
			for(int k = i + 1; k < i + m; k++) {
				a[k] = Pixel.EQ_TO_LEFT;
			}
		}
		return a;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static<S> Quadro<S> newInstance(String s, S zero) {
		String[] a = s.split("\n");
		Pixel[][] p;
		TextArt t;

		p = new Pixel[a.length][];
		for(int k = 0; k < a.length; k++) {
			p[k] = toPixel(a[k]);
		}
		t = new MemoryTextArt(p);
		return new QuadroImpl<S>(t, zero);
	}

	/**
	 * 
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	public static<S> Quadro<S> newInstance(BufferedReader rd,
			S zero) throws IOException {
		List<Pixel[]> l;
		TextArt t;
		String s;

		l = new ArrayList<Pixel[]>();
		while((s = rd.readLine()) != null) {
			l.add(toPixel(s));
		}
		t = new MemoryTextArt(l);
		return new QuadroImpl<S>(t, zero);
	}

}
