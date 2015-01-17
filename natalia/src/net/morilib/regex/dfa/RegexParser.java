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
/* @nina-version: 0.4.10.432 */
package net.morilib.regex.dfa;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import net.morilib.automata.NFAState;
import net.morilib.automata.nfa.NFAAccept;
import net.morilib.automata.nfa.NFAAlternative;
import net.morilib.automata.nfa.NFAConcatenation;
import net.morilib.automata.nfa.NFAObject;
import net.morilib.automata.nfa.NFAOptional;
import net.morilib.automata.nfa.NFAParenthesis;
import net.morilib.automata.nfa.NFARepetition;
import net.morilib.automata.nfa.NFAs;
import net.morilib.automata.nfa.RegexParseException;
import net.morilib.automata.nfa.SingleObjectNFA;
import net.morilib.automata.nfa.SingleSetNFA;
import net.morilib.range.CharSetException;
import net.morilib.range.CharSets;
import net.morilib.range.IntervalsInt;
import net.morilib.range.Range;
import net.morilib.range.integer.IntCharSets;
import net.morilib.range.integer.IntRange;
import net.morilib.unicode.UnicodeUtils;

/**
 * A parser of the regular expression.
 * 
 * @author Yuichiro MORIGUCHI
 */
@SuppressWarnings({"unchecked", "unused"})
public class RegexParser {

	/* @@@-PARSER-CODE-START-@@@ */
	static class TokenException extends RuntimeException {
	}

	static abstract class Engine {
		abstract int step(int c) throws java.io.IOException;
		abstract boolean accepted();
		abstract boolean isDead();
		abstract boolean isEmptyTransition();
		abstract int execaction(int c);
		abstract boolean isend();
		abstract int recover(Exception e);
		abstract int deadState();
		abstract int stateSize();
		abstract int finallyState();
	}
	static final int INVALIDTOKEN = 0x7fff7fff;
	private static final int NINA_BEGIN = -2;
	private static final int NINA_EOF = -1;
	private static final int NINA_ACCEPT = -8;
	private static final int NINA_FAIL = -9;
	private static final int NINA_HALT_ACCEPT = -91;
	private static final int NINA_HALT_REJECT = -72;
	private static final int NINA_YIELD = -85;
	private static final int NINA_STACKLEN = 72;
	static final int NINA_DISCARDSTATE = 0x40000000;
	static final int INITIAL = 0;
	static final int INDENT = 1;

	private int STATE;
	private int[] __sts = new int[NINA_STACKLEN];
	private Engine[] __stk = new Engine[NINA_STACKLEN];
	private Object[][] __stv = new Object[NINA_STACKLEN][];
	private int __slen = 0;
	private int unread = -1;

	NFAObject<Object, NFAState, Integer> _;
	java.util.List<NFAObject<Object, NFAState, Integer>> _l;
	Object yieldObject;
	Throwable exception;

	StringBuffer $buffer;
	int $int;
	java.math.BigInteger $bigint;
	Number $num;

	java.util.Stack<java.io.Reader> streamStack =
			new java.util.Stack<java.io.Reader>();

	void _initlist() {
		_l = new java.util.ArrayList<NFAObject<Object, NFAState, Integer>>();
	}

	void _addlist(NFAObject<Object, NFAState, Integer> x) {
		_l.add(x);
	}


	private int _unreadl = -1;

	void INCLUDE(java.io.Reader rd) {
		streamStack.push(rd);
	}

	void INCLUDE(String name) throws java.io.IOException {
		java.io.InputStream ins;

		ins = new java.io.FileInputStream(name);
		INCLUDE(new java.io.InputStreamReader(ins));
	}

	int _read1l() throws java.io.IOException {
		int c;

		while(streamStack.size() > 0) {
			if((c = streamStack.peek().read()) >= 0) {
				return c;
			} else if(streamStack.size() > 1) {
				streamStack.pop().close();
			} else {
				streamStack.pop();
			}
		}
		return NINA_EOF;
	}

	int _read1() throws java.io.IOException {
		int c;

		if(_unreadl != -1) {
			c = _unreadl;
			_unreadl = -1;
		} else if((c = _read1l()) == '\r' && (c = _read1l()) != '\n') {
			_unreadl = c;
			c = '\r';
		}
		return c;
	}

	private int _read() throws java.io.IOException {
		int c;

		while(true) {
			if(unread != -1) {

				c = unread;
				unread = -1;
				__logprint("Read unread: ", c);
			} else if((c = _read1()) != -1) {
				__logprint("Read: ", c);
			} else {
				__logprint("Read end-of-file");
			}
			return c;
		}
	}

	void UNGET(int c) {
		unread = c;
		__logprint("Set unread: ", c);
	}

	void __sleep(int m) {
		try {
			Thread.sleep(m);
		} catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void __logprint(String s, int c) {
	}

	private void __logopen() {
	}

	private void __logprint(String s) {
	}

	private void __logclose() {
	}

	private void __puttrace() {
	}


	private int elem_step(int  $c)  throws java.io.IOException {
		switch(STATE) {
		case 0:
			if($c < 0) {
				STATE = 1;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 1;
				return 1;
			}
		case 1:
			if(($c == '[')) {
				STATE = 2;
				return 1;
			} else if(($c == '\\')) {
				STATE = 3;
				return 1;
			} else if(($c == '.')) {
				STATE = 4;
				return 1;
			} else if(($c == '(')) {
				STATE = 5;
				return 1;
			} else if($c >= 0) {
				STATE = 6;
				return 1;
			}
			return 0;
		case 6:
			return 0;
		case 5:
			if(($c == '?')) {
				STATE = 7;
				return 1;
			} else if($c < 0) {
				STATE = 8;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 8;
				return 1;
			}
		case 8:
			if($c >= 0) {
				__stkpush(9, ENGINE_regexParser);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 9:
			if(($c == ')')) {
				STATE = 10;
				return 1;
			}
			return 0;
		case 10:
			return 0;
		case 7:
			if(($c == ':')) {
				STATE = 8;
				return 1;
			} else if(($c == ')')) {
				STATE = 11;
				return 1;
			} else if($c >= 0) {
				STATE = 12;
				return 1;
			}
			return 0;
		case 12:
			if(($c == ':')) {
				STATE = 8;
				return 1;
			} else if(($c == ')')) {
				STATE = 11;
				return 1;
			} else if($c >= 0) {
				STATE = 12;
				return 1;
			}
			return 0;
		case 11:
				STATE = 1;
				return 1;
		case 4:
			return 0;
		case 3:
			if(($c == 'x')) {
				STATE = 13;
				return 1;
			} else if(($c == 'u')) {
				STATE = 14;
				return 1;
			} else if(($c == 'D') || ($c == 'S') || ($c >= 'W' && $c <= 'X') || ($c == 'd') || ($c == 's') || ($c == 'w')) {
				STATE = 15;
				return 1;
			} else if(($c == 'P') || ($c == 'p')) {
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				STATE = 6;
				return 1;
			}
			return 0;
		case 16:
			if(($c == '{')) {
				STATE = 17;
				return 1;
			}
			return 0;
		case 17:
			if(($c == '}')) {
				STATE = 18;
				return 1;
			} else if($c >= 0) {
				STATE = 19;
				return 1;
			}
			return 0;
		case 19:
			if(($c == '}')) {
				STATE = 18;
				return 1;
			} else if($c >= 0) {
				STATE = 19;
				return 1;
			}
			return 0;
		case 18:
			return 0;
		case 15:
			return 0;
		case 14:
			if(($c >= '0' && $c <= '9') || ($c >= 'A' && $c <= 'F') || ($c >= 'a' && $c <= 'f')) {
				$buffer = new StringBuffer();$buffer.append((char)$c);
				STATE = 20;
				return 1;
			}
			return 0;
		case 20:
			if(($c >= '0' && $c <= '9') || ($c >= 'A' && $c <= 'F') || ($c >= 'a' && $c <= 'f')) {
				$buffer.append((char)$c);
				STATE = 21;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			}
		case 22:
			return 0;
		case 21:
			if(($c >= '0' && $c <= '9') || ($c >= 'A' && $c <= 'F') || ($c >= 'a' && $c <= 'f')) {
				$buffer.append((char)$c);
				STATE = 23;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			}
		case 23:
			if(($c >= '0' && $c <= '9') || ($c >= 'A' && $c <= 'F') || ($c >= 'a' && $c <= 'f')) {
				$buffer.append((char)$c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 22;
				return 1;
			}
		case 13:
			if(($c >= '0' && $c <= '9') || ($c >= 'A' && $c <= 'F') || ($c >= 'a' && $c <= 'f')) {
				$buffer = new StringBuffer();$buffer.append((char)$c);
				STATE = 24;
				return 1;
			}
			return 0;
		case 24:
			if(($c >= '0' && $c <= '9') || ($c >= 'A' && $c <= 'F') || ($c >= 'a' && $c <= 'f')) {
				$buffer.append((char)$c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 25;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 25;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 16);
				STATE = 25;
				return 1;
			}
		case 25:
			return 0;
		case 2:
				STATE = 26;
				return 1;
		case 26:
			if($c >= 0) {
				__stkpush(27, ENGINE_cset);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 27:
			if(($c == ']')) {
				STATE = 28;
				return 1;
			}
			return 0;
		case 28:
			return 0;
		}
		return 0;
	}

	private boolean elem_accepted() {
		return (STATE == 18 ||
				STATE == 4 ||
				STATE == 6 ||
				STATE == 22 ||
				STATE == 25 ||
				STATE == 10 ||
				STATE == 28 ||
				STATE == 15);
	}

	int elem_execaction(int  $c) {
		switch(STATE) {
		case 18:
			// State42                    
			_ = prop(rn, b.toString());
			break;
		case 21:
			break;
		case 7:
			// State56   
			// none
			break;
		case 10:
			// newState19   
			_ = par(_);
			break;
		case 13:
			// State36   
			// none
			break;
		case 22:
			// State35        
			_ = atom($int);
			break;
		case 3:
			// State33   
			// none
			break;
		case 24:
			break;
		case 23:
			break;
		case 5:
			// State55   
			nowf = 0;
			break;
		case 17:
			// State43                 
			b = new StringBuilder();
			break;
		case 26:
			break;
		case 15:
			// State38     
			_ = esc($c);
			break;
		case 19:
			// State41            
			b.append((char)$c);
			break;
		case 1:
			// newState16   
			// none
			break;
		case 0:
			break;
		case 8:
			break;
		case 20:
			break;
		case 27:
			break;
		case 28:
			// State26                
			_ = cset(b.toString());
			break;
		case 16:
			// State40   
			rn = $c;
			break;
		case 14:
			// State34   
			// none
			break;
		case 4:
			// State39   
			_ = dot();
			break;
		case 12:
			// State57     
			setflag($c);
			break;
		case 9:
			break;
		case 2:
			// State22                 
			b = new StringBuilder();
			break;
		case 25:
			// State37        
			_ = atom($int);
			break;
		case 6:
			// newState17   
			_ = atom($c);
			break;
		case 11:
			// State60   
			_writef();
			break;
		}
		return 1;
	}

	boolean elem_isend() {
		return (STATE == 0 ||
				STATE == 2 ||
				STATE == 21 ||
				STATE == 20 ||
				STATE == 5 ||
				STATE == 23 ||
				STATE == 24 ||
				STATE == 11);
	}

	private final Engine ENGINE_elem = new Engine() {

		int step(int c) throws java.io.IOException {
			return elem_step(c);
		}

		boolean accepted() {
			return elem_accepted();
		}

		int execaction(int c) {
			return elem_execaction(c);
		}

		boolean isend() {
			return elem_isend();
		}

		int recover(Exception e) {
			return -1;
		}

		int deadState() {
			return -1;
		}

		int stateSize() {
			return 29;
		}

		int finallyState() {
			return -1;
		}

		boolean isDead() {
		return (STATE == 18 ||
				STATE == 4 ||
				STATE == 6 ||
				STATE == 22 ||
				STATE == 25 ||
				STATE == 10 ||
				STATE == 28 ||
				STATE == 15);
		}

		boolean isEmptyTransition() {
		return (STATE == 2 ||
				STATE == 11);
		}

		public String toString() {
			return "elem";
		}

	};

	private int alternate_step(int  $c)  throws java.io.IOException {
		switch(STATE) {
		case 0:
			if($c < 0) {
				STATE = 1;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 1;
				return 1;
			}
		case 1:
			if($c >= 0) {
				__stkpush(2, ENGINE_concat);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 2:
				STATE = 3;
				return 1;
		case 3:
			if(($c == '|')) {
				STATE = 4;
				return 1;
			}
			return 0;
		case 4:
			if($c >= 0) {
				__stkpush(5, ENGINE_concat);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 5:
				STATE = 6;
				return 1;
		case 6:
			if(($c == '|')) {
				STATE = 4;
				return 1;
			}
			return 0;
		}
		return 0;
	}

	private boolean alternate_accepted() {
		return (STATE == 3 ||
				STATE == 6);
	}

	int alternate_execaction(int  $c) {
		switch(STATE) {
		case 6:
			// newState7                             
			(__stv[__slen - 1][3]) = _ = alt(((NFAObject<Object, NFAState, Integer>)(__stv[__slen - 1][3])), _);
			break;
		case 1:
			break;
		case 5:
			break;
		case 2:
			break;
		case 3:
			// firstSel                                      
			(__stv[__slen - 1][3]) = _;
			break;
		case 4:
			break;
		case 0:
			break;
		}
		return 1;
	}

	boolean alternate_isend() {
		return (STATE == 0 ||
				STATE == 2 ||
				STATE == 5);
	}

	private final Engine ENGINE_alternate = new Engine() {

		int step(int c) throws java.io.IOException {
			return alternate_step(c);
		}

		boolean accepted() {
			return alternate_accepted();
		}

		int execaction(int c) {
			return alternate_execaction(c);
		}

		boolean isend() {
			return alternate_isend();
		}

		int recover(Exception e) {
			return -1;
		}

		int deadState() {
			return -1;
		}

		int stateSize() {
			return 7;
		}

		int finallyState() {
			return -1;
		}

		boolean isDead() {
		return false;
		}

		boolean isEmptyTransition() {
		return (STATE == 2 ||
				STATE == 5);
		}

		public String toString() {
			return "alternate";
		}

	};

	private int cset_step(int  $c)  throws java.io.IOException {
		switch(STATE) {
		case 0:
			if($c < 0) {
				STATE = 1;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 1;
				return 1;
			}
		case 1:
			if(($c == '[')) {
				STATE = 2;
				return 1;
			} else if($c >= 0) {
				STATE = 3;
				return 1;
			}
			return 0;
		case 3:
			if(($c == '[')) {
				STATE = 2;
				return 1;
			} else if(($c >= ' ' && $c <= 'Z') || ($c == '\\') || ($c >= '^' && $c <= 2147483647)) {
				STATE = 3;
				return 1;
			}
			return 0;
		case 2:
				STATE = 4;
				return 1;
		case 4:
			if($c >= 0) {
				__stkpush(5, ENGINE_cset);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 5:
			if(($c == ']')) {
				STATE = 6;
				return 1;
			}
			return 0;
		case 6:
				STATE = 3;
				return 1;
		case 7:
			if($c >= 0) {
				STATE = 3;
				return 1;
			}
			return 0;
		}
		return 0;
	}

	private boolean cset_accepted() {
		return (STATE == 3 ||
				STATE == 6);
	}

	int cset_execaction(int  $c) {
		switch(STATE) {
		case 5:
			break;
		case 3:
			// State23            
			b.append((char)$c);
			break;
		case 6:
			// State54            
			b.append((char)$c);
			break;
		case 7:
			// State24            
			b.append((char)$c);
			break;
		case 0:
			break;
		case 1:
			// State50   
			// none
			break;
		case 2:
			// State52            
			b.append((char)$c);
			break;
		case 4:
			break;
		}
		return 1;
	}

	boolean cset_isend() {
		return (STATE == 0 ||
				STATE == 2 ||
				STATE == 6);
	}

	private final Engine ENGINE_cset = new Engine() {

		int step(int c) throws java.io.IOException {
			return cset_step(c);
		}

		boolean accepted() {
			return cset_accepted();
		}

		int execaction(int c) {
			return cset_execaction(c);
		}

		boolean isend() {
			return cset_isend();
		}

		int recover(Exception e) {
			return -1;
		}

		int deadState() {
			return -1;
		}

		int stateSize() {
			return 8;
		}

		int finallyState() {
			return -1;
		}

		boolean isDead() {
		return false;
		}

		boolean isEmptyTransition() {
		return (STATE == 2 ||
				STATE == 6);
		}

		public String toString() {
			return "cset";
		}

	};

	private int regexParser_step(int  $c)  throws java.io.IOException {
		switch(STATE) {
		case 0:
			if($c < 0) {
				STATE = 1;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 1;
				return 1;
			}
		case 1:
				STATE = 2;
				return 1;
		case 2:
			if($c >= 0) {
				__stkpush(3, ENGINE_alternate);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 3:
				STATE = 4;
				return 1;
		case 4:
			return 0;
		}
		return 0;
	}

	private boolean regexParser_accepted() {
		return (STATE == 4);
	}

	int regexParser_execaction(int  $c) {
		switch(STATE) {
		case 4:
			// State45   
			// none
			break;
		case 0:
			break;
		case 3:
			break;
		case 1:
			// State58   
			_pushf();
			break;
		case 2:
			break;
		}
		return 1;
	}

	boolean regexParser_isend() {
		return (STATE == 0 ||
				STATE == 1 ||
				STATE == 3);
	}

	private final Engine ENGINE_regexParser = new Engine() {

		int step(int c) throws java.io.IOException {
			return regexParser_step(c);
		}

		boolean accepted() {
			return regexParser_accepted();
		}

		int execaction(int c) {
			return regexParser_execaction(c);
		}

		boolean isend() {
			return regexParser_isend();
		}

		int recover(Exception e) {
			return -1;
		}

		int deadState() {
			return -1;
		}

		int stateSize() {
			return 5;
		}

		int finallyState() {
			return -1;
		}

		boolean isDead() {
		return (STATE == 4);
		}

		boolean isEmptyTransition() {
		return (STATE == 1 ||
				STATE == 3);
		}

		public String toString() {
			return "regexParser";
		}

	};

	private int closure_step(int  $c)  throws java.io.IOException {
		switch(STATE) {
		case 0:
			if($c < 0) {
				STATE = 1;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 1;
				return 1;
			}
		case 1:
			if($c >= 0) {
				__stkpush(2, ENGINE_elem);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 2:
				STATE = 3;
				return 1;
		case 3:
			if(($c == '{')) {
				STATE = 4;
				return 1;
			} else if(($c == '+')) {
				STATE = 5;
				return 1;
			} else if(($c == '*')) {
				STATE = 6;
				return 1;
			} else if(($c == '?')) {
				STATE = 7;
				return 1;
			}
			return 0;
		case 7:
			return 0;
		case 6:
			return 0;
		case 5:
			return 0;
		case 4:
			if(($c == ',')) {
				STATE = 8;
				return 1;
			} else if(($c >= '0' && $c <= '9')) {
				$buffer = new StringBuffer();$buffer.append((char)$c);
				STATE = 9;
				return 1;
			}
			return 0;
		case 9:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);
				STATE = 10;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			}
		case 11:
			if(($c == ',')) {
				STATE = 12;
				return 1;
			}
			return 0;
		case 12:
			if(($c >= '0' && $c <= '9')) {
				$buffer = new StringBuffer();$buffer.append((char)$c);
				STATE = 13;
				return 1;
			} else if(($c == '}')) {
				STATE = 14;
				return 1;
			}
			return 0;
		case 14:
			return 0;
		case 13:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);
				STATE = 15;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			}
		case 16:
			if(($c == '}')) {
				STATE = 14;
				return 1;
			}
			return 0;
		case 15:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);
				STATE = 17;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			}
		case 17:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			}
		case 10:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);
				STATE = 18;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			}
		case 18:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 11;
				return 1;
			}
		case 8:
			if(($c >= '0' && $c <= '9')) {
				$buffer = new StringBuffer();$buffer.append((char)$c);
				STATE = 19;
				return 1;
			}
			return 0;
		case 19:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);
				STATE = 20;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			}
		case 20:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);
				STATE = 21;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			}
		case 21:
			if(($c >= '0' && $c <= '9')) {
				$buffer.append((char)$c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c < 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			} else if($c >= 0) {
				UNGET($c);$int=Integer.parseInt($buffer.toString(), 10);
				STATE = 16;
				return 1;
			}
		}
		return 0;
	}

	private boolean closure_accepted() {
		return (STATE == 3 ||
				STATE == 5 ||
				STATE == 6 ||
				STATE == 7 ||
				STATE == 14);
	}

	int closure_execaction(int  $c) {
		switch(STATE) {
		case 16:
			// State31   
			// none
			break;
		case 3:
			// firstClo                                      
			(__stv[__slen - 1][3]) = _;
			break;
		case 12:
			// State30   
			$int = -1;
			break;
		case 13:
			break;
		case 10:
			break;
		case 11:
			// State28   
			rn = $int;
			break;
		case 19:
			break;
		case 7:
			// State21              
			_ = opt(((NFAObject<Object, NFAState, Integer>)(__stv[__slen - 1][3])));
			break;
		case 0:
			break;
		case 1:
			break;
		case 6:
			// newState15           
			_ = clo(((NFAObject<Object, NFAState, Integer>)(__stv[__slen - 1][3])));
			break;
		case 8:
			// State29   
			rn = 0;
			break;
		case 5:
			// State20               
			_ = plus(((NFAObject<Object, NFAState, Integer>)(__stv[__slen - 1][3])));
			break;
		case 4:
			// State27   
			// none
			break;
		case 15:
			break;
		case 21:
			break;
		case 18:
			break;
		case 20:
			break;
		case 2:
			break;
		case 9:
			break;
		case 14:
			// State32                        
			_ = rep(((NFAObject<Object, NFAState, Integer>)(__stv[__slen - 1][3])), rn, $int);
			break;
		case 17:
			break;
		}
		return 1;
	}

	boolean closure_isend() {
		return (STATE == 17 ||
				STATE == 0 ||
				STATE == 2 ||
				STATE == 19 ||
				STATE == 18 ||
				STATE == 21 ||
				STATE == 20 ||
				STATE == 9 ||
				STATE == 10 ||
				STATE == 13 ||
				STATE == 15);
	}

	private final Engine ENGINE_closure = new Engine() {

		int step(int c) throws java.io.IOException {
			return closure_step(c);
		}

		boolean accepted() {
			return closure_accepted();
		}

		int execaction(int c) {
			return closure_execaction(c);
		}

		boolean isend() {
			return closure_isend();
		}

		int recover(Exception e) {
			return -1;
		}

		int deadState() {
			return -1;
		}

		int stateSize() {
			return 22;
		}

		int finallyState() {
			return -1;
		}

		boolean isDead() {
		return (STATE == 5 ||
				STATE == 6 ||
				STATE == 7 ||
				STATE == 14);
		}

		boolean isEmptyTransition() {
		return (STATE == 2);
		}

		public String toString() {
			return "closure";
		}

	};

	private int concat_step(int  $c)  throws java.io.IOException {
		switch(STATE) {
		case 0:
			if($c < 0) {
				STATE = 1;
				return 1;
			} else if($c >= 0) {
				UNGET($c);
				STATE = 1;
				return 1;
			}
		case 1:
			if($c >= 0) {
				__stkpush(2, ENGINE_closure);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 2:
				STATE = 3;
				return 1;
		case 3:
			if(($c >= ' ' && $c <= '(') || ($c >= ',' && $c <= '>') || ($c >= '@' && $c <= '{') || ($c >= '}' && $c <= 2147483647)) {
				UNGET($c);
				STATE = 4;
				return 1;
			}
			return 0;
		case 4:
			if($c >= 0) {
				__stkpush(5, ENGINE_closure);
				STATE = 0;
				return NINA_ACCEPT;
			}
			return 0;
		case 5:
				STATE = 6;
				return 1;
		case 6:
			if(($c >= ' ' && $c <= '(') || ($c >= ',' && $c <= '>') || ($c >= '@' && $c <= '{') || ($c >= '}' && $c <= 2147483647)) {
				UNGET($c);
				STATE = 4;
				return 1;
			}
			return 0;
		}
		return 0;
	}

	private boolean concat_accepted() {
		return (STATE == 3 ||
				STATE == 6);
	}

	int concat_execaction(int  $c) {
		switch(STATE) {
		case 5:
			break;
		case 1:
			break;
		case 0:
			break;
		case 3:
			// firstCat                                      
			(__stv[__slen - 1][3]) = _;
			break;
		case 6:
			// newState12                            
			(__stv[__slen - 1][3]) = _ = cat(((NFAObject<Object, NFAState, Integer>)(__stv[__slen - 1][3])), _);
			break;
		case 2:
			break;
		case 4:
			break;
		}
		return 1;
	}

	boolean concat_isend() {
		return (STATE == 0 ||
				STATE == 2 ||
				STATE == 5);
	}

	private final Engine ENGINE_concat = new Engine() {

		int step(int c) throws java.io.IOException {
			return concat_step(c);
		}

		boolean accepted() {
			return concat_accepted();
		}

		int execaction(int c) {
			return concat_execaction(c);
		}

		boolean isend() {
			return concat_isend();
		}

		int recover(Exception e) {
			return -1;
		}

		int deadState() {
			return -1;
		}

		int stateSize() {
			return 7;
		}

		int finallyState() {
			return -1;
		}

		boolean isDead() {
		return false;
		}

		boolean isEmptyTransition() {
		return (STATE == 2 ||
				STATE == 5);
		}

		public String toString() {
			return "concat";
		}

	};

	void __stkpush(int st, Engine en) {
		Object[][] c;
		Engine[] b;
		int[] a;

		if(__slen >= __sts.length) {
			a = new int[__sts.length * 2];
			b = new Engine[__stk.length * 2];
			c = new Object[__stk.length * 2][];
			System.arraycopy(__sts, 0, a, 0, __sts.length);
			System.arraycopy(__stk, 0, b, 0, __stk.length);
			System.arraycopy(__stv, 0, c, 0, __stv.length);
			__sts = a;
			__stk = b;
			__stv = c;
		}
		__sts[__slen] = st;
		__stk[__slen] = en;
		__stv[__slen++] = new Object[en.stateSize()];
	}

	private int _parse(int x, Boolean rt, boolean skip,
			int[] st) throws java.io.IOException {
		boolean b = false, p = skip;
		int c = x;
		Engine en;
		int a;

		b = __stk[__slen - 1].accepted();
		if(rt.booleanValue()) {
			switch(__stk[__slen - 1].execaction(NINA_BEGIN)) {
			case NINA_ACCEPT:
				__logprint("accept " + __stk[__slen - 1]);
				st[0] = NINA_ACCEPT;  return -1;
			case NINA_FAIL:
				__logprint("match failed: begin");
				__puttrace();
				st[0] = NINA_FAIL;  return -1;
			case NINA_HALT_ACCEPT:
				__logprint("machine halted: begin");
				st[0] = NINA_HALT_ACCEPT;  return -1;
			case NINA_HALT_REJECT:
				__logprint("machine halted: begin");
				st[0] = NINA_HALT_REJECT;  return -1;
			case NINA_YIELD:
				__logprint("machine yielded: ", c);
				st[0] = NINA_YIELD;  return -1;
			}
		}

		try {
			do {
				en = __stk[__slen - 1];
				if(p) {
					p = false;
				} else if((a = en.step(c)) > 0) {
					__logprint("transit to state " + STATE + ": ", c);
					b = en.accepted();
					switch(en.execaction(c)) {
					case NINA_ACCEPT:
						__logprint("accept " + __stk[__slen - 1]);
						UNGET(c);
						st[0] = NINA_ACCEPT;  return -1;
					case NINA_FAIL:
						__logprint("match failed: ", c);
						__puttrace();
						UNGET(c);
						st[0] = NINA_FAIL;  return -1;
					case NINA_HALT_ACCEPT:
						__logprint("machine halted: ", c);
						st[0] = NINA_HALT_ACCEPT;  return -1;
					case NINA_HALT_REJECT:
						__logprint("machine halted: ", c);
						st[0] = NINA_HALT_REJECT;  return -1;
					case NINA_YIELD:
						__logprint("machine yielded: ", c);
						st[0] = NINA_YIELD;  return -1;
					}
				} else if(a < 0) {
					__logprint("entering " + __stk[__slen - 1]);
					return c;
				} else if(b) {
					__logprint("accept " + __stk[__slen - 1]);
					UNGET(c);
					st[0] = NINA_ACCEPT;  return -1;
				} else if(c == -1) {
					if(!b)  throw new TokenException();
					st[0] = NINA_ACCEPT;  return -1;
				} else {
					__logprint("match failed: ", c);
					__puttrace();
					UNGET(c);
					st[0] = NINA_FAIL;  return -1;
				}

				if(__stk[__slen - 1].isEmptyTransition()) {
					// do nothing
				} else if(!__stk[__slen - 1].isDead()) {
					c = _read();
				} else if(b) {
					__logprint("accept " + __stk[__slen - 1]);
					st[0] = NINA_ACCEPT;  return -1;
				} else {
					__logprint("match failed: ", c);
					__puttrace();
					st[0] = NINA_FAIL;  return -1;
				}
			} while(true);
		} catch(RuntimeException e) {
			UNGET(c);
			throw e;
		}
	}

	private Boolean execfinally() {
		int a, b;

		if((a = __stk[__slen - 1].finallyState()) >= 0) {
			b = STATE;  STATE = a;
			switch(__stk[__slen - 1].execaction(NINA_BEGIN)) {
			case NINA_HALT_ACCEPT:
				__slen = 0;
				return Boolean.TRUE;
			case NINA_HALT_REJECT:
				__slen = 0;
				return Boolean.FALSE;
			}
			STATE = b;
		}
		return null;
	}

	private int getdeadstate() {
		return __stk[__slen - 1].deadState();
	}

	private int getrecover(Exception e) {
		return __stk[__slen - 1].recover(e);
	}

	boolean parse(Engine entry) throws java.io.IOException {
		Boolean b = Boolean.FALSE;
		int[] a = new int[1];
		boolean skip = true;
		int c = 0;

		__logopen();
		try {
			if(__slen == 0) {
				b = Boolean.TRUE;
				__stkpush(0, entry);
			}

			ot: while(true) {
				try {
					if((c = _parse(c, b, skip, a)) != -1) {
						skip = false;
					} else if(a[0] == NINA_FAIL) {
						while((STATE = getdeadstate()) < 0) {
							if((b = execfinally()) != null)  break ot;
							if(__slen-- <= 1) {
								throw new TokenException();
							}
						}
						skip = true;
					} else if(a[0] == NINA_HALT_ACCEPT) {
						if((b = execfinally()) != null)  break;
						__slen = 0;
						b = Boolean.TRUE;  break;
					} else if(a[0] == NINA_HALT_REJECT) {
						if((b = execfinally()) != null)  break;
						__slen = 0;
						b = Boolean.FALSE;  break;
					} else if(a[0] == NINA_YIELD) {
						return false;
					} else if(__slen > 1) {
						if((b = execfinally()) != null)  break;
						STATE = __sts[--__slen];
						skip = true;
					} else {
						if((b = execfinally()) != null)  break;
						b = new Boolean(__stk[--__slen].accepted());
						break;
					}
				} catch(RuntimeException e) {
					exception = e;
					if(__slen <= 0)  throw e;
					while((STATE = getrecover(e)) < 0) {
						if((b = execfinally()) != null)  return b;
						if(__slen-- <= 1)  throw e;
					}
				}
				b = Boolean.TRUE;
			}
			if(!b.booleanValue())  throw new TokenException();
			return b.booleanValue();
		} finally {
			__logclose();
		}
	}

	boolean parse(java.io.Reader rd) throws java.io.IOException {
		streamStack.push(rd);
		return parse(ENGINE_regexParser);
	}

	static boolean parseAll(java.io.Reader rd) throws java.io.IOException {
		RegexParser o = new RegexParser();

		return o.parse(rd);
	}

	void setStream(java.io.Reader rd) {
		if(streamStack.size() == 0) {
			throw new IllegalStateException();
		}
		yieldObject = rd;
		streamStack.push(rd);
	}

	Object parseNext() throws java.io.IOException {
		Object o;

		if(streamStack.size() == 0) {
			throw new IllegalStateException();
		} else if(yieldObject == null) {
			return null;
		} else if(parse(ENGINE_regexParser)) {
			if(yieldObject == null)  throw new NullPointerException();
			o = yieldObject;  yieldObject = null;
			return o;
		} else {
			if(yieldObject == null)  throw new NullPointerException();
			return yieldObject;
		}
	}

	static void puts(String s) {
		System.out.println(s);
	}

	boolean parse(java.io.InputStream rd) throws java.io.IOException {
		return parse(new java.io.InputStreamReader(rd));
	}

	static boolean parseAll(
			java.io.InputStream rd) throws java.io.IOException {
		return parseAll(new java.io.InputStreamReader(rd));
	}

	/* @@@-PARSER-CODE-END-@@@ */
	private static final int DOTALL = 1;
	private static final int IGNORE_CASE = 2;

	private StringBuilder b;
	private int rn;
	private Deque<Integer> flgstk = new ArrayDeque<Integer>();
	private int caputure = 1;
	private int nowf = 0;
	private boolean reset = false;

	private boolean isDotall() {
		int x;

		x = flgstk.peekFirst();
		return (x & DOTALL) != 0;
	}

	private boolean isIgnoreCase() {
		int x;

		x = flgstk.peekFirst();
		return (x & IGNORE_CASE) != 0;
	}

	private void _pushf() {
		flgstk.addFirst(nowf);
	}

	private void _writef() {
		flgstk.removeFirst();
		flgstk.addFirst(nowf);
	}

	private void setflag(int c) {
		switch(c) {
		case '-':
			reset = true;
			break;
		case 'i':
			if(reset) {
				nowf &= ~IGNORE_CASE;
			} else {
				nowf |= IGNORE_CASE;
			}
			reset = false;
			break;
		case 's':
			if(reset) {
				nowf &= ~DOTALL;
			} else {
				nowf |= DOTALL;
			}
			reset = false;
			break;
		default:
			reset = false;
			break;
		}
	}

	private NFAObject<Object, NFAState, Integer> alt(
			NFAObject<Object, NFAState, Integer> a,
			NFAObject<Object, NFAState, Integer> b) {
		return NFAAlternative.newInstance(a, b);
	}

	private NFAObject<Object, NFAState, Integer> cat(
			NFAObject<Object, NFAState, Integer> a,
			NFAObject<Object, NFAState, Integer> b) {
		return NFAConcatenation.newInstance(a, b);
	}

	private NFAObject<Object, NFAState, Integer> clo(
			NFAObject<Object, NFAState, Integer> a) {
		return NFARepetition.newInstance(a, true);
	}

	private NFAObject<Object, NFAState, Integer> plus(
			NFAObject<Object, NFAState, Integer> a) {
		return NFARepetition.newInstance(a, false);
	}

	private NFAObject<Object, NFAState, Integer> opt(
			NFAObject<Object, NFAState, Integer> a) {
		return NFAOptional.newInstance(a);
	}

	private NFAObject<Object, NFAState, Integer> par(
			NFAObject<Object, NFAState, Integer> a) {
		flgstk.removeFirst();
		return NFAParenthesis.newInstance(a, caputure++);
	}

	private NFAObject<Object, NFAState, Integer> atom(int c) {
		char u, l;
		Range o;

		if(isIgnoreCase()) {
			u = Character.toUpperCase((char)c);
			l = Character.toLowerCase((char)c);
			if(u == l) {
				o = IntervalsInt.newPoint(c);
			} else {
				o = IntervalsInt.newPoint(u);
				o = o.join(IntervalsInt.newPoint(l));
			}
		} else {
			o = IntervalsInt.newPoint(c);
		}
		return SingleSetNFA.newInstance(o);
	}

	private NFAObject<Object, NFAState, Integer> cset(String s) {
		IntRange r;

		try {
			r = CharSet.parse(s);
			if(isIgnoreCase()) {
				r = IntCharSets.toCaseInsensitive(r);
			}
			return SingleSetNFA.newInstance(IntCharSets.toRange(r));
		} catch(CharSetException e) {
			throw new TokenException();
		}
	}

	private NFAObject<Object, NFAState, Integer> rep(
			NFAObject<Object, NFAState, Integer> a, int n, int m) {
		if(n > m && m >= 0) {
			return NFAs.newRepetitionNM(a, n, n);
		} else {
			return NFAs.newRepetitionNM(a, n, m);
		}
	}

	private NFAObject<Object, NFAState, Integer> dot() {
		return SingleSetNFA.newInstance(
				isDotall() ? CharSets.ALL_CHAR : CharSets.DOT);
	}

	private NFAObject<Object, NFAState, Integer> esc(int c) {
		switch(c) {
		case 'd':
			return SingleSetNFA.newInstance(CharSets.ASCII_NUMBERS);
		case 'D':
			return SingleSetNFA.newInstance(CharSets.NOT_ASCII_NUMBERS);
		case 's':
			return SingleSetNFA.newInstance(CharSets.ASCII_WHITESPACE);
		case 'S':
			return SingleSetNFA.newInstance(CharSets.NOT_ASCII_WHITESPACE);
		case 'w':
			return SingleSetNFA.newInstance(CharSets.ASCII_WORD);
		case 'W':
			return SingleSetNFA.newInstance(CharSets.NOT_ASCII_WORD);
		case 'X':
			return NFAs.newComplexLetter();
		default:
			throw new RuntimeException();
		}
	}

	private NFAObject<Object, NFAState, Integer> prop(int c, String s) {
		IntRange r;

		r = UnicodeUtils.getPropertyIntRange(s);
//		r = UnicodeUtils.getPropertyRange(s);
		if(c == 'P') {
			r = r.complement();
//			r = CharSets.complement(r);
		}

		if(isIgnoreCase()) {
			r = IntCharSets.toCaseInsensitive(r);
		}
		return SingleSetNFA.newInstance(IntCharSets.toRange(r));
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static NFAObject<Object, NFAState, Integer> parse(
			String s) throws RegexParseException {
		RegexParser p;

		p = new RegexParser();
		try {
			p.parse(new java.io.StringReader(s));
			return NFAAccept.newInstance(p._, new NFAState() {});
		} catch(TokenException e) {
			throw new RegexParseException();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}
