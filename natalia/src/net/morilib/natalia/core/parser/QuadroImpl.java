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

import java.util.List;

/**
 *
 */
public class QuadroImpl implements Quadro {

	//
	private static final int N = 0;
	private static final int E = 1;
	private static final int S = 2;
	private static final int W = 3;

	//
	private int xptr, yptr, direction;
	private int xmax;
	private Pixel[][]   pixels;
	private Scratch[][] scratches;
	private Object[][]  marks;
	private StringBuilder regString;
	private int rowRegister, columnRegister;
	private int rowSpanRegister, columnSpanRegister;
	private TableModelBuilder tableModelBuilder;

	//
	QuadroImpl(Pixel[][] p) {
		pixels = p;
		xptr = yptr = xmax = 0;
		regString = new StringBuilder();
		for(int k = 0; k < p.length; k++) {
			xmax = xmax < p[k].length ? p[k].length : xmax;
		}
		scratches = new Scratch[p.length][];
		marks = new Object[p.length][];
		for(int k = 0; k < p.length; k++) {
			scratches[k] = new Scratch[xmax];
			marks[k]     = new Object[xmax];
		}
	}

	//
	QuadroImpl(List<Pixel[]> p) {
		pixels = p.toArray(new Pixel[0][]);
		xptr = yptr = xmax = 0;
		regString = new StringBuilder();
		for(int k = 0; k < p.size(); k++) {
			xmax = xmax < p.get(k).length ? p.get(k).length : xmax;
		}
		scratches = new Scratch[p.size()][];
		marks = new Object[p.size()][];
		for(int k = 0; k < p.size(); k++) {
			scratches[k] = new Scratch[xmax];
			marks[k]     = new Object[xmax];
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#get()
	 */
	@Override
	public Pixel get() {
		if(yptr > pixels.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.length || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
			return Pixel.BOUND;
		} else if(xptr >= pixels[yptr].length) {
			return Pixel.SPACE;
		} else {
			return pixels[yptr][xptr];
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#getScratch()
	 */
	@Override
	public Scratch getScratch() {
		Scratch s;

		if(yptr > scratches.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == scratches.length || yptr == -1) {
			return Scratch.NONE;
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
			return Scratch.NONE;
		} else {
			s = scratches[yptr][xptr];
			return s == null ? Scratch.NONE : s;
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#setScratch(net.morilib.natalia.Scratch)
	 */
	@Override
	public Quadro setScratch(Scratch x) {
		if(yptr > scratches.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == scratches.length || yptr == -1) {
			// do nothing
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
			// do nothing
		} else {
			scratches[yptr][xptr] = x;
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveNorth()
	 */
	@Override
	public Quadro moveNorth() {
		yptr = yptr < 0 ? -1 : yptr - 1;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveEast()
	 */
	@Override
	public Quadro moveEast() {
		xptr = xptr < xmax ? xptr + 1 : xmax;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveSouth()
	 */
	@Override
	public Quadro moveSouth() {
		yptr = yptr < pixels.length ? yptr + 1 : pixels.length;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#moveWest()
	 */
	@Override
	public Quadro moveWest() {
		xptr = xptr < 0 ? -1 : xptr - 1;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.core.parser.Quadro#crlf()
	 */
	@Override
	public Quadro crlf() {
		xptr = 0;
		return moveSouth();
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#forward()
	 */
	@Override
	public Quadro forward() {
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
	public Quadro turnNorth() {
		direction = N;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnEast()
	 */
	@Override
	public Quadro turnEast() {
		direction = E;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnSouth()
	 */
	@Override
	public Quadro turnSouth() {
		direction = S;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnWest()
	 */
	@Override
	public Quadro turnWest() {
		direction = W;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnLeft()
	 */
	@Override
	public Quadro turnLeft() {
		direction = (direction + 3) % 4;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#turnRight()
	 */
	@Override
	public Quadro turnRight() {
		direction = (direction + 1) % 4;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#back()
	 */
	@Override
	public Quadro back() {
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
	public Quadro mark(Object o) {
		if(yptr > marks.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == marks.length || yptr == -1) {
			// do nothing
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
			// do nothing
		} else {
			marks[yptr][xptr] = o;
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#isMarked(java.lang.Object)
	 */
	@Override
	public boolean isMarked(Object o) {
		if(o == null) {
			throw new NullPointerException();
		} else if(yptr > marks.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == marks.length || yptr == -1) {
			return false;
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
			return false;
		} else {
			return o.equals(marks[yptr][xptr]);
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#peekForward()
	 */
	@Override
	public Pixel peekForward() {
		Pixel p;

		if(yptr > pixels.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.length || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
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

		if(yptr > pixels.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.length || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
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

		if(yptr > pixels.length || yptr < -1) {
			throw new IllegalStateException();
		} else if(yptr == pixels.length || yptr == -1) {
			return Pixel.BOUND;
		} else if(xptr > xmax || xptr < -1) {
			throw new IllegalStateException();
		} else if(xptr == xmax || xptr == -1) {
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
	public Quadro clearTextRegister() {
		regString = new StringBuilder();
		return this;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#appendTextRegister(int)
	 */
	@Override
	public Quadro appendTextRegister(int c) {
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
	public Quadro setRowRegister(int x) {
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
	public Quadro setColumnRegister(int x) {
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
	public Quadro setRowSpanRegister(int x) {
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
	public Quadro setColumnSpanRegister(int x) {
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
	public Quadro setTableModelBuilder(TableModelBuilder x) {
		tableModelBuilder = x;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder b = new StringBuilder();

		for(int j = 0; j < scratches.length; j++) {
			for(int i = 0; i < xmax; i++) {
				if(scratches[j][i] != null) {
					b.append(scratches[j][i].toString());
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
