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

public enum ParserState {
	INIT,
	FRAME_INIT, FRAME_END,
	FRAME_LINE_INIT, FRAME_LINE_WALL, FRAME_LINE_JUNCTION,
	FRAME_LINE_END,
	DRAW_BORDER_INIT, DRAW_BORDER_RIGHT,
	DRAW_BORDER_LEFT_1, DRAW_BORDER_LEFT_2, DRAW_BORDER_LEFT_3,
	DRAW_BORDER_END,
	TWALL_INIT, TWALL_FORWARD, TWALL_BACK, TWALL_END,
	TFRAME_INIT, TFRAME_FORWARD, TFRAME_END,
	DRAW_GRID_INIT, DRAW_GRID_RETURN, DRAW_GRID_END,
	TGRID_INIT, TGRID_FORWARD, TGRID_BACK, TGRID_END,
	EXTRACT_TEXT_INIT,
	EXTRACT_TEXT_MOVE1, EXTRACT_TEXT_MOVE2, EXTRACT_TEXT_MOVE3,
	EXTRACT_TEXT_COLLECT,
	EXTRACT_TEXT_COLLECT_CR1, EXTRACT_TEXT_COLLECT_CR2,
	EXTRACT_TEXT_COLLECT_CR3,
	EXTRACT_TEXT_END,
	TROW_INIT, TROW_FORWARD, TROW_END,
	FMAIN_INIT, FMAIN_FORWARD, FMAIN_RETURN, FMAIN_END,
	FSEARCH_INIT, FSEARCH_FIND0, FSEARCH_FIND1, FSEARCH_FIND2,
	FSEARCH_END,
}
