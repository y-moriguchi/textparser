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
import net.morilib.natalia.core.TableModel;
import net.morilib.natalia.core.parser.TableParser;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import junit.framework.TestCase;

/**
 *
 */
public class NataliaMultibyteTest extends TestCase {

	public void testA0002() {
		TableModel t;
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|あ |ず |さ|\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|や |よ |い|\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		t = TableParser.parseTable(q);
		assertEquals(q.toString(), 3, t.rowSize());
		assertEquals(q.toString(), 3, t.columnSize());
		assertEquals(q.toString(), "あ", t.get(1, 1).getCell());
		assertEquals(q.toString(), "ず", t.get(1, 2).getCell());
		assertEquals(q.toString(), "さ", t.get(1, 3).getCell());
		assertEquals(q.toString(), "21", t.get(2, 1).getCell());
		assertEquals(q.toString(), "22", t.get(2, 2).getCell());
		assertEquals(q.toString(), "23", t.get(2, 3).getCell());
		assertEquals(q.toString(), "や", t.get(3, 1).getCell());
		assertEquals(q.toString(), "よ", t.get(3, 2).getCell());
		assertEquals(q.toString(), "い", t.get(3, 3).getCell());
	}

}
