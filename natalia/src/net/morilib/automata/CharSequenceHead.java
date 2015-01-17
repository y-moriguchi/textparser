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
package net.morilib.automata;

import java.util.EnumSet;
import java.util.NoSuchElementException;

public final class CharSequenceHead implements BoundOnewayHead<Integer> {
	
	//
	private CharSequence seq;
	private int ptr  = 0;
	private int befc = -1;
	
	
	public CharSequenceHead(CharSequence seq) {
		this.seq = seq;
	}
	
	
	public EnumSet<TextBound> getBounds() {
		EnumSet<TextBound> res = EnumSet.noneOf(TextBound.class);
		
		if(befc == -1) {
			res.add(TextBound.BEGIN_INPUT);
			res.add(TextBound.BEGIN_LINE);
		} else if(befc == '\n') {
			res.add(TextBound.BEGIN_LINE);
		}
		
		if(ptr < seq.length()) {
			int cc = seq.charAt(ptr);
			
			if(cc == '\n') {
				res.add(TextBound.END_LINE);
				if(ptr + 1 >= seq.length()) {
					res.add(TextBound.END_INPUT_WITHOUT_LINE);
				}
			}
			
			if(Character.isWhitespace(befc) &&
					!Character.isWhitespace(cc)) {
				res.add(TextBound.NOT_WORD);
			}
			
			if(!Character.isWhitespace(befc) &&
					Character.isWhitespace(cc)) {
				res.add(TextBound.WORD);
			}
		} else {
			res.add(TextBound.END_LINE);
			res.add(TextBound.END_INPUT);
		}
		
		return res;
	}
	
	
	public boolean hasNext() {
		return ptr < seq.length();
	}
	
	
	public Integer read() {
		return readInt();
	}


	public int readInt() {
		if(hasNext()) {
			return (befc = seq.charAt(ptr++));
		} else {
			throw new NoSuchElementException();
		}
	}


	public void unread() {
		if(ptr > 0) {
			if(--ptr > 0) {
				befc = seq.charAt(ptr - 1);
			} else {
				befc = -1;
			}
		}
	}

}
