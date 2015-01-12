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
import net.morilib.natalia.core.parser.FrameMainTransition;
import net.morilib.natalia.core.parser.ParserState;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import net.morilib.natalia.lba2d.Transition;
import junit.framework.TestCase;

/**
 *
 */
public class NataliaParserTest extends TestCase {

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
		go(ParserState.FMAIN_INIT, ParserState.FMAIN_END,
				FrameMainTransition.INSTANCE, q);
		return q.getTableModelBuilder().toTableModel();
	}

	public void testA0001() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+-------+-------+\n" +
				"|a      |b      |\n" +
				"+-------+-------+\n" +
				"|c      |d      |\n" +
				"+-------+-------+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 2, t.rowSize());
		assertEquals(q.toString(), 2, t.columnSize());
		assertEquals(q.toString(), "a", t.get(1, 1).getCell());
		assertEquals(q.toString(), "b", t.get(1, 2).getCell());
		assertEquals(q.toString(), "c", t.get(2, 1).getCell());
		assertEquals(q.toString(), "d", t.get(2, 2).getCell());
	}

	public void testA0002() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
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

	public void testA0003() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"|41 |42 |43|\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 4, t.rowSize());
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
		assertEquals(q.toString(), "41", t.get(4, 1).getCell());
		assertEquals(q.toString(), "42", t.get(4, 2).getCell());
		assertEquals(q.toString(), "43", t.get(4, 3).getCell());
	}

	public void testA0004() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 1, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
	}

	public void testA0005() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+\n" +
				"|11 |\n" +
				"+---+\n" +
				"|21 |\n" +
				"+---+\n" +
				"|31 |\n" +
				"|   |\n" +
				"+---+\n" +
				"|41 |\n" +
				"+---+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 4, t.rowSize());
		assertEquals(q.toString(), 1, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "41", t.get(4, 1).getCell());
	}

	public void testA0006() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+-------+--+\n" +
				"|11     |13|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "11", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0007() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21     |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "21", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0008() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31     |33|\n" +
				"|       |  |\n" +
				"+---+---+--+\n" +
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
		assertEquals(q.toString(), "31", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0009() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+----------+\n" +
				"|11        |\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "11", t.get(1, 2).getCell());
		assertEquals(q.toString(), "11", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0010() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21        |\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "21", t.get(2, 2).getCell());
		assertEquals(q.toString(), "21", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0011() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31        |\n" +
				"|          |\n" +
				"+---+---+--+\n" +
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
		assertEquals(q.toString(), "31", t.get(3, 2).getCell());
		assertEquals(q.toString(), "31", t.get(3, 3).getCell());
	}

	public void testA0012() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"|   +---+--+\n" +
				"|   |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "11", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0013() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+   +--+\n" +
				"|21 |   |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "12", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0014() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+  |\n" +
				"|21 |22 |  |\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "13", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0015() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"|   +---+--+\n" +
				"|   |22 |23|\n" +
				"|   +---+--+\n" +
				"|   |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "11", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "11", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0016() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+   +--+\n" +
				"|21 |   |23|\n" +
				"+---+   +--+\n" +
				"|31 |   |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "12", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "12", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0017() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+  |\n" +
				"|21 |22 |  |\n" +
				"+---+---+  |\n" +
				"|31 |32 |  |\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "13", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "13", t.get(3, 3).getCell());
	}

	public void testA0018() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+------+\n" +
				"|11 |12    |\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "12", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0019() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22    |\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "22", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0020() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|31 |32    |\n" +
				"|   |      |\n" +
				"+---+------+\n" +
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
		assertEquals(q.toString(), "32", t.get(3, 3).getCell());
	}

	public void testA0021() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+-------+--+\n" +
				"|11     |13|\n" +
				"|       +--+\n" +
				"|       |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "11", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "11", t.get(2, 1).getCell());
		assertEquals(q.toString(), "11", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0022() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21 |22    |\n" +
				"+---+      |\n" +
				"|31 |      |\n" +
				"|   |      |\n" +
				"+---+------+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "22", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "22", t.get(3, 2).getCell());
		assertEquals(q.toString(), "22", t.get(3, 3).getCell());
	}

	public void testA0023() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+------+\n" +
				"|11 |12    |\n" +
				"+---+      |\n" +
				"|21 |      |\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "12", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "12", t.get(2, 2).getCell());
		assertEquals(q.toString(), "12", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0024() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11 |12 |13|\n" +
				"+---+---+--+\n" +
				"|21     |23|\n" +
				"|       +--+\n" +
				"|       |33|\n" +
				"|       |  |\n" +
				"+-------+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "21", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "21", t.get(3, 1).getCell());
		assertEquals(q.toString(), "21", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0025() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+------+\n" +
				"|11 |12    |\n" +
				"+---+---+--+\n" +
				"|21     |23|\n" +
				"+---+---+--+\n" +
				"|31 |32    |\n" +
				"|   |      |\n" +
				"+---+------+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "12", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "21", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "32", t.get(3, 3).getCell());
	}

	public void testA0026() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+-------+--+-----+\n" +
				"|11     |13|14   |\n" +
				"|       +--+     |\n" +
				"|       |23|     |\n" +
				"+---+---+--+--+--+\n" +
				"|31 |32 |33|34|35|\n" +
				"+---+---+--+--+--+\n" +
				"|41     |43|44   |\n" +
				"|       +--+     |\n" +
				"|       |53|     |\n" +
				"+-------+--+-----+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 5, t.rowSize());
		assertEquals(q.toString(), 5, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "11", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "14", t.get(1, 4).getCell());
		assertEquals(q.toString(), "14", t.get(1, 5).getCell());
		assertEquals(q.toString(), "11", t.get(2, 1).getCell());
		assertEquals(q.toString(), "11", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "14", t.get(2, 4).getCell());
		assertEquals(q.toString(), "14", t.get(2, 5).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
		assertEquals(q.toString(), "34", t.get(3, 4).getCell());
		assertEquals(q.toString(), "35", t.get(3, 5).getCell());
		assertEquals(q.toString(), "41", t.get(4, 1).getCell());
		assertEquals(q.toString(), "41", t.get(4, 2).getCell());
		assertEquals(q.toString(), "43", t.get(4, 3).getCell());
		assertEquals(q.toString(), "44", t.get(4, 4).getCell());
		assertEquals(q.toString(), "44", t.get(4, 5).getCell());
		assertEquals(q.toString(), "41", t.get(5, 1).getCell());
		assertEquals(q.toString(), "41", t.get(5, 2).getCell());
		assertEquals(q.toString(), "53", t.get(5, 3).getCell());
		assertEquals(q.toString(), "44", t.get(5, 4).getCell());
		assertEquals(q.toString(), "44", t.get(5, 5).getCell());
	}

	public void testA0027() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+--+--+\n" +
				"|11 |12 |13|14|15|\n" +
				"+---+---+--+--+--+\n" +
				"|21 |22       |25|\n" +
				"+---+         +--+\n" +
				"|31 |         |35|\n" +
				"+---+         +--+\n" +
				"|41 |         |45|\n" +
				"+---+---+--+--+--+\n" +
				"|51 |52 |53|54|55|\n" +
				"+---+---+--+--+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 5, t.rowSize());
		assertEquals(q.toString(), 5, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "12", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "14", t.get(1, 4).getCell());
		assertEquals(q.toString(), "15", t.get(1, 5).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "22", t.get(2, 3).getCell());
		assertEquals(q.toString(), "22", t.get(2, 4).getCell());
		assertEquals(q.toString(), "25", t.get(2, 5).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "22", t.get(3, 2).getCell());
		assertEquals(q.toString(), "22", t.get(3, 3).getCell());
		assertEquals(q.toString(), "22", t.get(3, 4).getCell());
		assertEquals(q.toString(), "35", t.get(3, 5).getCell());
		assertEquals(q.toString(), "41", t.get(4, 1).getCell());
		assertEquals(q.toString(), "22", t.get(4, 2).getCell());
		assertEquals(q.toString(), "22", t.get(4, 3).getCell());
		assertEquals(q.toString(), "22", t.get(4, 4).getCell());
		assertEquals(q.toString(), "45", t.get(4, 5).getCell());
		assertEquals(q.toString(), "51", t.get(5, 1).getCell());
		assertEquals(q.toString(), "52", t.get(5, 2).getCell());
		assertEquals(q.toString(), "53", t.get(5, 3).getCell());
		assertEquals(q.toString(), "54", t.get(5, 4).getCell());
		assertEquals(q.toString(), "55", t.get(5, 5).getCell());
	}

	public void testA0028() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|11     |13|\n" +
				"+       +--+\n" +
				"|       |23|\n" +
				"+---+---+--+\n" +
				"|31 |32 |33|\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
		assertEquals(q.toString(), "11", t.get(1, 2).getCell());
		assertEquals(q.toString(), "13", t.get(1, 3).getCell());
		assertEquals(q.toString(), "11", t.get(2, 1).getCell());
		assertEquals(q.toString(), "11", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "31", t.get(3, 1).getCell());
		assertEquals(q.toString(), "32", t.get(3, 2).getCell());
		assertEquals(q.toString(), "33", t.get(3, 3).getCell());
	}

	public void testA0029() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+\n" +
				"|11 |\n" +
				"+---+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 1, t.rowSize());
		assertEquals(q.toString(), 1, t.columnSize());
		assertEquals(q.toString(), "11", t.get(1, 1).getCell());
	}

}
