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

import net.morilib.natalia.core.parser.ParserException;
import net.morilib.natalia.core.parser.Quadro;
import net.morilib.natalia.core.parser.QuadroFactory;
import net.morilib.natalia.core.parser.TableParser;
import junit.framework.TestCase;

/**
 *
 */
public class NataliaErrorTest extends TestCase {

	static void err(Quadro q) {
		try {
			TableParser.parseTable(q);
			fail();
		} catch(ParserException e) {
			// ok
		}
	}

	public void testE0001() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+-- +---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"");
		err(q);
	}

	public void testE0002() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|   |   |   \n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"");
		err(q);
	}

	public void testE0003() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+- -+--+\n" +
				"");
		err(q);
	}

	public void testE0004() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				" 21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"");
		err(q);
	}

	public void testE0005() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"    |   |  |\n" +
				"    +---+--+\n" +
				"    |22 |23|\n" +
				"    +---+--+\n" +
				"    |   |  |\n" +
				"    +---+--+\n" +
				"");
		err(q);
	}

	public void testE0006() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"        |  |\n" +
				"    +---+--+\n" +
				"    |22 |23|\n" +
				"    +---+--+\n" +
				"    |   |  |\n" +
				"    +---+--+\n" +
				"");
		err(q);
	}

	public void testE0007() {
		Quadro q;

		q = QuadroFactory.newInstance(
				"+\n" +
				"");
		err(q);
	}

	public void testE0008() {
		Quadro q;

		q = QuadroFactory.newInstance("");
		err(q);
	}

}
