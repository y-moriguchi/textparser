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

import net.morilib.natalia.core.TableModel;
import junit.framework.TestCase;

/**
 *
 */
public class NataliaMySQLParseTest extends TestCase {

	public void testA0001() {
		TableModel t;

		t = TableParser.getInstance().parseMySQLOutput(
				"+------+------------------+------+\n" +
				"| id   | name             | b    |\n" +
				"+------+------------------+------+\n" +
				"| 0001 | AMAMI Haruka     |   83 |\n" +
				"| 0002 | KISARAGI Chihaya |   72 |\n" +
				"| 0003 | MIURA Azusa      |   91 |\n" +
				"+------+------------------+------+\n" +
				"3 rows in set (0.07 sec)\n" +
				"");
		assertEquals(4, t.rowSize());
		assertEquals(3, t.columnSize());
		assertEquals("id", t.get(1, 1).getCell());
		assertEquals("name", t.get(1, 2).getCell());
		assertEquals("b", t.get(1, 3).getCell());
		assertEquals("0001", t.get(2, 1).getCell());
		assertEquals("AMAMI Haruka", t.get(2, 2).getCell());
		assertEquals("83", t.get(2, 3).getCell());
		assertEquals("0002", t.get(3, 1).getCell());
		assertEquals("KISARAGI Chihaya", t.get(3, 2).getCell());
		assertEquals("72", t.get(3, 3).getCell());
		assertEquals("0003", t.get(4, 1).getCell());
		assertEquals("MIURA Azusa", t.get(4, 2).getCell());
		assertEquals("91", t.get(4, 3).getCell());
	}

}
