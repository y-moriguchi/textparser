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

import java.util.EnumSet;

import net.morilib.natalia.core.parser.Quadro;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class ExtractTableTransition implements Transition {

	//
	static final Transition I = new ExtractTableTransition();

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#transit(net.morilib.natalia.core.parser.Quadro, net.morilib.natalia.db.parser.PS)
	 */
	@Override
	public PS transit(Quadro q, PS state) {
		switch(state) {
		case EXT_TABLE_INIT:
			q.setColumnRegister(1);
			q.setRowRegister(1);
			q.clearTextRegister();
			return PS.EXT_HEADER_CORNER;
		case EXT_HEADER_CORNER:
			if(q.getScratch().isFrameCorner()) {
				return PS.EXT_HEADER_SCAN;
			} else {
				q.moveNorth();
			}
			return state;
		case EXT_HEADER_SCAN:
			if(q.getScratch().isVerticalFrame()) {
				q.getTableModelBuilder().appendCell(
						0, q.getColumnRegister(), 1, 1,
						q.getTextRegister().trim());
				q.clearTextRegister();
				q.setColumnRegister(1);
				q.moveWest().moveSouth();
				return PS.EXT_HEADER_SCAN_R;
			} else if(q.getScratch().isLatitudinalBorder()) {
				q.getTableModelBuilder().appendCell(
						0, q.getColumnRegister(), 1, 1,
						q.getTextRegister().trim());
				q.clearTextRegister();
				q.setColumnRegister(q.getColumnRegister() + 1);
				q.moveEast();
			} else {
				q.appendTextRegister(q.get().getChar());
				q.moveEast();
			}
			return state;
		case EXT_HEADER_SCAN_R:
			if(q.getScratch().isFrame()) {
				return PS.EXT_HEADER_SCAN_R2;
			} else {
				q.moveWest();
			}
			return state;
		case EXT_HEADER_SCAN_R2:
			if(q.getScratch().isLongitudinalBorder()) {
				q.moveSouth();
				return PS.EXT_TABLE_SCAN;
			} else {
				return PS.EXT_HEADER_SCAN;
			}
		case EXT_TABLE_SCAN:
			if(q.getScratch().isFrameCorner()) {
				q.getTableModelBuilder().appendCell(
						q.getRowRegister(), q.getColumnRegister(),
						1, 1, q.getTextRegister().trim());
				return PS.EXT_TABLE_END;
			} else if(q.getScratch().isVerticalFrame()) {
				q.getTableModelBuilder().appendCell(
						q.getRowRegister(), q.getColumnRegister(),
						1, 1, q.getTextRegister().trim());
				q.clearTextRegister();
				q.setRowRegister(q.getRowRegister() + 1);
				q.setColumnRegister(1);
				q.moveWest();
				return PS.EXT_TABLE_SCAN_R;
			} else if(q.getScratch().isLatitudinalBorder()) {
				q.getTableModelBuilder().appendCell(
						q.getRowRegister(), q.getColumnRegister(),
						1, 1, q.getTextRegister().trim());
				q.clearTextRegister();
				q.setColumnRegister(q.getColumnRegister() + 1);
				q.moveEast();
			} else {
				q.appendTextRegister(q.get().getChar());
				q.moveEast();
			}
			return state;
		case EXT_TABLE_SCAN_R:
			if(q.getScratch().isFrame()) {
				q.moveSouth();
				return PS.EXT_TABLE_SCAN;
			} else {
				q.moveWest();
			}
			return state;
		default:
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.db.parser.Transition#getStates()
	 */
	@Override
	public EnumSet<PS> getStates() {
		EnumSet<PS> r;

		r = EnumSet.of(PS.EXT_TABLE_INIT,
				PS.EXT_HEADER_CORNER,
				PS.EXT_HEADER_SCAN,
				PS.EXT_HEADER_SCAN_R,
				PS.EXT_HEADER_SCAN_R2,
				PS.EXT_TABLE_SCAN,
				PS.EXT_TABLE_SCAN_R,
				PS.EXT_TABLE_END);
		return r;
	}

}
