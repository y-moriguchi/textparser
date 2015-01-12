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
package net.morilib.natalia.db.parser;

import net.morilib.natalia.core.Scratch;
import net.morilib.natalia.core.SimpleTableModelBuilder;
import net.morilib.natalia.core.TableModel;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import net.morilib.natalia.lba2d.Transition;
import junit.framework.TestCase;

/**
 *
 */
public class DBTextParserTest extends TestCase {

	//
	private static final int INFINITE_LOOP = 100;

	//
	static void go(PS init, PS end, Transition<Scratch, PS> t,
			Quadro<Scratch> q) {
		PS s = init, p = null;
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
		go(PS.POSTGRES_MAIN_INIT, PS.POSTGRES_MAIN_END,
				DBParseMainTransition.I, q);
		return q.getTableModelBuilder().toTableModel();
	}

	public void testA0001() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				" aa | bb | cc \n" +
				"----+----+----\n" +
				" 11 | 12 | 13 \n" +
				" 21 | 22 | 23 \n" +
				" 31 | 32 | 33 \n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "aa", t.get(0, 1).getCell());
		assertEquals(q.toString(), "bb", t.get(0, 2).getCell());
		assertEquals(q.toString(), "cc", t.get(0, 3).getCell());
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

	public void testA0002() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+----+----+----+\n" +
				"| aa | bb | cc |\n" +
				"+----+----+----+\n" +
				"| 11 | 12 | 13 |\n" +
				"| 21 | 22 | 23 |\n" +
				"| 31 | 32 | 33 |\n" +
				"+----+----+----+\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "aa", t.get(0, 1).getCell());
		assertEquals(q.toString(), "bb", t.get(0, 2).getCell());
		assertEquals(q.toString(), "cc", t.get(0, 3).getCell());
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
				"  id  |       name       | b  \n" +
				"------+------------------+----\n" +
				" 0001 | AMAMI Haruka     | 83 \n" +
				" 0002 | KISARAGI Chihaya | 72 \n" +
				" 0003 | MIURA Azusa      | 91 \n" +
				"(3 rows) \n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "id", t.get(0, 1).getCell());
		assertEquals(q.toString(), "name", t.get(0, 2).getCell());
		assertEquals(q.toString(), "b", t.get(0, 3).getCell());
		assertEquals(q.toString(), "0001", t.get(1, 1).getCell());
		assertEquals(q.toString(), "AMAMI Haruka", t.get(1, 2).getCell());
		assertEquals(q.toString(), "83", t.get(1, 3).getCell());
		assertEquals(q.toString(), "0002", t.get(2, 1).getCell());
		assertEquals(q.toString(), "KISARAGI Chihaya", t.get(2, 2).getCell());
		assertEquals(q.toString(), "72", t.get(2, 3).getCell());
		assertEquals(q.toString(), "0003", t.get(3, 1).getCell());
		assertEquals(q.toString(), "MIURA Azusa", t.get(3, 2).getCell());
		assertEquals(q.toString(), "91", t.get(3, 3).getCell());
	}

	public void testA0004() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+------+------------------+------+\n" +
				"| id   | name             | b    |\n" +
				"+------+------------------+------+\n" +
				"| 0001 | AMAMI Haruka     |   83 |\n" +
				"| 0002 | KISARAGI Chihaya |   72 |\n" +
				"| 0003 | MIURA Azusa      |   91 |\n" +
				"+------+------------------+------+\n" +
				"3 rows in set (0.07 sec)\n" +
				"", Scratch.NONE);
		t = parseTable(q);
		assertEquals(3, t.rowSize());
		assertEquals(3, t.columnSize());
		assertEquals("id", t.get(0, 1).getCell());
		assertEquals("name", t.get(0, 2).getCell());
		assertEquals("b", t.get(0, 3).getCell());
		assertEquals("0001", t.get(1, 1).getCell());
		assertEquals("AMAMI Haruka", t.get(1, 2).getCell());
		assertEquals("83", t.get(1, 3).getCell());
		assertEquals("0002", t.get(2, 1).getCell());
		assertEquals("KISARAGI Chihaya", t.get(2, 2).getCell());
		assertEquals("72", t.get(2, 3).getCell());
		assertEquals("0003", t.get(3, 1).getCell());
		assertEquals("MIURA Azusa", t.get(3, 2).getCell());
		assertEquals("91", t.get(3, 3).getCell());
	}

}
