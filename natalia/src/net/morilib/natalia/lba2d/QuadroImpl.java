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
package net.morilib.natalia.lba2d;

import java.util.HashMap;
import java.util.Map;

import net.morilib.natalia.core.TableModelBuilder;

/**
 *
 */
public class QuadroImpl<S> implements Quadro<S> {

	//
	private static class P implements Comparable<P> {

		private int row, column;

		private P(int r, int c) {
			row = r;
			column = c;
		}

		@Override
		public int compareTo(P o) {
			if(row > o.row) {
				return 1;
			} else if(row < o.row) {
				return -1;
			} else if(column > o.column) {
				return 1;
			} else if(column < o.column) {
				return -1;
			} else {
				return 0;
			}
		}

		public boolean equals(Object o) {
			if(o == null) {
				return false;
			} else if(!(o instanceof P)) {
				return false;
			} else {
				return row == ((P)o).row && column == ((P)o).column;
			}
		}

		public int hashCode() {
			return 17 * (row * 49 + column);
		}

	}

	//
	private static final int N = 0;
	private static final int E = 1;
	private static final int S = 2;
	private static final int W = 3;

	//
	private int xptr, yptr, direction;
	private TextArt pixels;
	private Map<P, S> scratches;
	private Map<P, Object>  marks;
	private StringBuilder regString;
	private int rowRegister, columnRegister;
	private int rowSpanRegister, columnSpanRegister;
	private TableModelBuilder tableModelBuilder;
	private S zero;

	//
	private QuadroImpl() {}

	//
	QuadroImpl(TextArt p, S z) {
		pixels = p;
		xptr = yptr = 0;
		regString = new StringBuilder();
		scratches = new HashMap<P, S>();
		marks     = new HashMap<P, Object>();
		zero      = z;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.lba2d.Quadro#fork()
	 */
	@Override
	public Quadro<S> fork() {
		QuadroImpl<S> q;

		q = new QuadroImpl<S>();
		q.pixels = pixels;
		q.xptr = xptr;
		q.yptr = yptr;
		q.direction = direction;
		q.scratches = new HashMap<P, S>(scratches);
		q.marks     = new HashMap<P, Object>(marks);
		q.regString = new StringBuilder(regString);
		q.rowRegister = rowRegister;
		q.columnRegister = columnRegister;
		q.rowSpanRegister = rowSpanRegister;
		q.columnSpanRegister = columnSpanRegister;
		q.tableModelBuilder = tableModelBuilder;
		q.zero = zero;
		return q;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#get()
	 */
	@Override
	public Pixel get() {
		return pixels.get(yptr, xptr);
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getScratch()
	 */
	@Override
	public S getScratch() {
		S s;

		s = scratches.get(new P(yptr, xptr));
		return s != null ? s : zero;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setScratch(net.morilib.natalia.Scratch)
	 */
	@Override
	public Quadro<S> setScratch(S x) {
		scratches.put(new P(yptr, xptr), x);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveNorth()
	 */
	@Override
	public Quadro<S> moveNorth() {
		yptr = yptr < 0 ? -1 : yptr - 1;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveEast()
	 */
	@Override
	public Quadro<S> moveEast() {
		xptr = xptr < pixels.getColumns() ?
				xptr + 1 : pixels.getColumns();
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveSouth()
	 */
	@Override
	public Quadro<S> moveSouth() {
		yptr = yptr < pixels.getRows() ? yptr + 1 : pixels.getRows();
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveWest()
	 */
	@Override
	public Quadro<S> moveWest() {
		xptr = xptr < 0 ? -1 : xptr - 1;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Quadro#crlf()
	 */
	@Override
	public Quadro<S> crlf() {
		xptr = 0;
		return moveSouth();
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#forward()
	 */
	@Override
	public Quadro<S> forward() {
		switch(direction) {
		case N:   return moveNorth();
		case E:   return moveEast();
		case S:   return moveSouth();
		case W:   return moveWest();
		default:  throw new IllegalStateException(direction + "");
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnNorth()
	 */
	@Override
	public Quadro<S> turnNorth() {
		direction = N;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnEast()
	 */
	@Override
	public Quadro<S> turnEast() {
		direction = E;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnSouth()
	 */
	@Override
	public Quadro<S> turnSouth() {
		direction = S;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnWest()
	 */
	@Override
	public Quadro<S> turnWest() {
		direction = W;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnLeft()
	 */
	@Override
	public Quadro<S> turnLeft() {
		direction = (direction + 3) % 4;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnRight()
	 */
	@Override
	public Quadro<S> turnRight() {
		direction = (direction + 1) % 4;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#back()
	 */
	@Override
	public Quadro<S> back() {
		switch(direction) {
		case S:   return moveNorth();
		case W:   return moveEast();
		case N:   return moveSouth();
		case E:   return moveWest();
		default:  throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isDirectionNorth()
	 */
	@Override
	public boolean isDirectionNorth() {
		return direction == N;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isDirectionEast()
	 */
	@Override
	public boolean isDirectionEast() {
		return direction == E;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isDirectionSouth()
	 */
	@Override
	public boolean isDirectionSouth() {
		return direction == S;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isDirectionWest()
	 */
	@Override
	public boolean isDirectionWest() {
		return direction == W;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isDirectionLatitudinal()
	 */
	@Override
	public boolean isDirectionLatitudinal() {
		return isDirectionNorth() || isDirectionSouth();
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isDirectionLongitudinal()
	 */
	@Override
	public boolean isDirectionLongitudinal() {
		return isDirectionEast() || isDirectionWest();
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#mark(java.lang.Object)
	 */
	@Override
	public Quadro<S> mark(Object o) {
		if(o != null) {
			marks.put(new P(yptr, xptr), o);
		} else {
			marks.remove(new P(yptr, xptr));
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isMarked(java.lang.Object)
	 */
	@Override
	public boolean isMarked(Object o) {
		if(o != null) {
			return o.equals(marks.get(new P(yptr, xptr)));
		} else {
			return marks.get(new P(yptr, xptr)) == null;
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#peekForward()
	 */
	@Override
	public Pixel peekForward() {
		Pixel p;

		if(yptr > pixels.getRows() || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.getRows() || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > pixels.getColumns() || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == pixels.getColumns() || xptr == -1) {
			return Pixel.BOUND;
		} else {
			p = forward().get();
			back();
			return p;
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#peekLeft()
	 */
	@Override
	public Pixel peekLeft() {
		Pixel p;

		if(yptr > pixels.getRows() || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.getRows() || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > pixels.getColumns() || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == pixels.getColumns() || xptr == -1) {
			return Pixel.BOUND;
		} else {
			p = turnLeft().forward().get();
			back().turnRight();
			return p;
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#peekRight()
	 */
	@Override
	public Pixel peekRight() {
		Pixel p;

		if(yptr > pixels.getRows() || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.getRows() || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > pixels.getColumns() || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == pixels.getColumns() || xptr == -1) {
			return Pixel.BOUND;
		} else {
			p = turnRight().forward().get();
			back().turnLeft();
			return p;
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#clearTextRegister()
	 */
	@Override
	public Quadro<S> clearTextRegister() {
		regString = new StringBuilder();
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#appendTextRegister(int)
	 */
	@Override
	public Quadro<S> appendTextRegister(int c) {
		regString.appendCodePoint(c);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getTextRegister()
	 */
	@Override
	public String getTextRegister() {
		return regString.toString();
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getRowRegister()
	 */
	@Override
	public int getRowRegister() {
		return rowRegister;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setRowRegister(int)
	 */
	@Override
	public Quadro<S> setRowRegister(int x) {
		rowRegister = x;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getColumnRegister()
	 */
	@Override
	public int getColumnRegister() {
		return columnRegister;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setColumnRegister(int)
	 */
	@Override
	public Quadro<S> setColumnRegister(int x) {
		columnRegister = x;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getRowSpanRegister()
	 */
	@Override
	public int getRowSpanRegister() {
		return rowSpanRegister;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setRowSpanRegister(int)
	 */
	@Override
	public Quadro<S> setRowSpanRegister(int x) {
		rowSpanRegister = x;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getColumnSpanRegister()
	 */
	@Override
	public int getColumnSpanRegister() {
		return columnSpanRegister;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setColumnSpanRegister(int)
	 */
	@Override
	public Quadro<S> setColumnSpanRegister(int x) {
		columnSpanRegister = x;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getTableModelBuilder()
	 */
	@Override
	public TableModelBuilder getTableModelBuilder() {
		return tableModelBuilder;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setTableModelBuilder(net.morilib.natalia.TableModelBuilder)
	 */
	@Override
	public Quadro<S> setTableModelBuilder(TableModelBuilder x) {
		tableModelBuilder = x;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder b = new StringBuilder();
		S s;

		for(int j = 0; j < pixels.getRows(); j++) {
			for(int i = 0; i < pixels.getColumns(); i++) {
				if((s = scratches.get(new P(j, i))) != null) {
					b.append(s.toString());
				} else {
					b.append(' ');
				}
			}
			b.append('\n');
		}
		return b.toString();
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getRowPosition()
	 */
	@Override
	public int getRowPosition() {
		return xptr;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getColumnPosition()
	 */
	@Override
	public int getColumnPosition() {
		return yptr;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getDirection()
	 */
	@Override
	public String getDirection() {
		switch(direction) {
		case N:   return "North";
		case E:   return "East";
		case S:   return "South";
		case W:   return "West";
		default:  throw new IllegalStateException();
		}
	}

}
