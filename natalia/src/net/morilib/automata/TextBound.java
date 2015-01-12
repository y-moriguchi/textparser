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

/**
 * 
 *
 *
 * @author MORIGUCHI, Yuichiro 2006
 */
public enum TextBound {
	BEGIN_LINE,
	END_LINE,
	WORD,
	NOT_WORD,
	BEGIN_INPUT,
	END_INPUT,
	END_INPUT_WITHOUT_LINE,
	;

	/**
	 * 
	 */
	public static final EnumSet<TextBound> BEGIN_LINE_SET =
		EnumSet.of(BEGIN_LINE);

	/**
	 * 
	 */
	public static final EnumSet<TextBound> END_LINE_SET =
		EnumSet.of(END_LINE);

	/**
	 * 
	 */
	public static final EnumSet<TextBound> ALL =
		EnumSet.allOf(TextBound.class);

}
