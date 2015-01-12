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
package net.morilib.natalia.core.parser;

import net.morilib.natalia.core.Scratch;
import net.morilib.natalia.core.SimpleTableModelBuilder;
import net.morilib.natalia.core.TableModel;
import net.morilib.natalia.core.parser.ParserState;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import net.morilib.natalia.lba2d.Transition;
import junit.framework.TestCase;

/**
 *
 */
public class NataliaSearchTest extends TestCase {

	//
	private static final int INFINITE_LOOP = 100;

	//
	static void go(ParserState init, ParserState end,
			Transition<Scratch, ParserState> t, Quadro<Scratch> q) {
		ParserState s = init, p = null;
		int c = 0;

		for(; !s.equals(end); p = s) {
//			System.out.println(q.getRowPosition() + "," +
//					q.getColumnPosition() +
//					"," + q.getDirection());
//			System.out.println((char)q.get().getChar());
//			System.out.println(s);
//			System.out.println(q.toString());
			s = t.transit(q, s);
			if(!s.equals(p)) {
				c = 0;
			} else if(c++ > INFINITE_LOOP) {
				throw new IllegalStateException("maybe infinite loop");
			}
		}
	}

	//
	static TableModel parseTable(Quadro<Scratch> q) {
		q.setTableModelBuilder(new SimpleTableModelBuilder());
		go(ParserState.FSEARCH_INIT, ParserState.FSEARCH_END,
				FrameSearchTransition.INSTANCE, q);
		return q.getTableModelBuilder().toTableModel();
	}

	public void testA0001() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				" a += 1       \n" +
				"  +---+---+--+\n" +
				"  |11 |12 |13|\n" +
				"  +---+---+--+\n" +
				"  |21 |22 |23|\n" +
				"  +---+---+--+\n" +
				"  |31 |32 |33|\n" +
				"  |   |   |  |\n" +
				"  +---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

}
