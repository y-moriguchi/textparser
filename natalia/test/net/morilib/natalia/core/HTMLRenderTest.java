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
package net.morilib.natalia.core;

import net.morilib.natalia.core.TableTranslator;
import junit.framework.TestCase;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class HTMLRenderTest extends TestCase {

	public void testA0001() {
		String s;

		s = TableTranslator.toHTMLString(
				"+--+--+\n" +
				"|11|12|\n" +
				"+--+--+\n" +
				"|21|22|\n" +
				"+--+--+\n" +
				"");
		assertEquals(
				"<table>\n" +
				"<tr>\n" +
				"<td>\n" +
				"11\n" +
				"</td>\n" +
				"<td>\n" +
				"12\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td>\n" +
				"21\n" +
				"</td>\n" +
				"<td>\n" +
				"22\n" +
				"</td>\n" +
				"</tr>\n" +
				"</table>\n" +
				"",
				s);
	}

	public void testA0002() {
		String s;

		s = TableTranslator.toHTMLString(
				"+--+-----+\n" +
				"|11|12   |\n" +
				"+--+--+--+\n" +
				"|21|22|23|\n" +
				"+--+--+--+\n" +
				"");
		assertEquals(
				"<table>\n" +
				"<tr>\n" +
				"<td>\n" +
				"11\n" +
				"</td>\n" +
				"<td rowspan=\"1\" colspan=\"2\">\n" +
				"12\n" +
				"</td>\n" +
				"</tr>\n" +
				"<tr>\n" +
				"<td>\n" +
				"21\n" +
				"</td>\n" +
				"<td>\n" +
				"22\n" +
				"</td>\n" +
				"<td>\n" +
				"23\n" +
				"</td>\n" +
				"</tr>\n" +
				"</table>\n" +
				"",
				s);
	}

}
