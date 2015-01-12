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

import net.morilib.natalia.core.ParserException;
import net.morilib.natalia.core.Scratch;
import net.morilib.natalia.core.parser.TableParser;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import junit.framework.TestCase;

/**
 *
 */
public class NataliaErrorTest extends TestCase {

	static void err(Quadro<Scratch> q) {
		try {
			TableParser.parseTable(q);
			fail();
		} catch(ParserException e) {
			// ok
		}
	}

	public void testE0001() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+-- +---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0002() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|   |   |   \n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0003() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"|21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+- -+--+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0004() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				" 21 |22 |23|\n" +
				"+---+---+--+\n" +
				"|   |   |  |\n" +
				"+---+---+--+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0005() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"    |   |  |\n" +
				"    +---+--+\n" +
				"    |22 |23|\n" +
				"    +---+--+\n" +
				"    |   |  |\n" +
				"    +---+--+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0006() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+---+---+--+\n" +
				"        |  |\n" +
				"    +---+--+\n" +
				"    |22 |23|\n" +
				"    +---+--+\n" +
				"    |   |  |\n" +
				"    +---+--+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0007() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(
				"+\n" +
				"", Scratch.NONE);
		err(q);
	}

	public void testE0008() {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance("", Scratch.NONE);
		err(q);
	}

}
