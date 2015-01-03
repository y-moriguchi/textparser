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

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public enum PS {
	SEARCH_AXIS_INIT, SEARCH_AXIS_WALL, SEARCH_AXIS_END,
	DRAW_LAT_INIT, DRAW_LAT_UP1,
	DRAW_LAT_DOWN1, DRAW_LAT_DOWN2, DRAW_LAT_DOWN3,
	DRAW_LAT_UP, DRAW_LAT_END,
	DRAW_LONG_FRAME_INIT,
	DRAW_LONG_FRAME1, DRAW_LONG_FRAME2, DRAW_LONG_FRAME3,
	DRAW_LONG_FRAME_END,
	T_AXIS_INIT, T_AXIS_EAST_FRAME, T_AXIS_EAST_FRAME2,
	T_AXIS_EAST_FRAME3, T_AXIS_WEST, T_AXIS_END,
	T_AXIS_WEST_FRAME, T_AXIS_WEST_FRAME2, T_AXIS_WEST_FRAME3,
	EXT_TABLE_INIT, EXT_HEADER_CORNER, EXT_HEADER_SCAN,
	EXT_HEADER_SCAN_R, EXT_HEADER_SCAN_R2,
	EXT_TABLE_SCAN, EXT_TABLE_SCAN_R, EXT_TABLE_END,
	POSTGRES_MAIN_INIT, POSTGRES_MAIN_END,
}
