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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import net.morilib.natalia.core.TableCell;
import net.morilib.natalia.core.TableModel;

/**
 *
 */
public class SimpleTableModelBuilder implements TableModelBuilder {

	//
	private SortedMap<Integer, SortedMap<Integer, TableCell>> table =
			new TreeMap<Integer, SortedMap<Integer, TableCell>>();

	//
	private static class Cell implements TableCell {

		//
		private int row, col, rspan, cspan;
		private String text;
		private TableCell cell;

		private Cell(int r, int c, int rs, int cs, String t,
				TableCell l) {
			row = r;
			col = c;
			rspan = rs;
			cspan = cs;
			text = t;
			cell = l;
		}

		@Override
		public int getRow() {
			return row;
		}

		@Override
		public int getColumn() {
			return col;
		}

		@Override
		public int getRowSpan() {
			return rspan;
		}

		@Override
		public int getColumnSpan() {
			return cspan;
		}

		@Override
		public String getCell() {
			return text;
		}

		@Override
		public TableCell getMergedCell() {
			return cell;
		}

	}

	//
	private static class Iter implements Iterator<TableCell> {

		private Iterator<SortedMap<Integer, TableCell>> iterr;
		private Iterator<TableCell> iterc;
		private TableCell nx;
		private boolean span;

		private Iter(SortedMap<Integer,
				SortedMap<Integer, TableCell>> c, boolean p) {
			span  = p;
			iterr = c.values().iterator();
			if(iterr.hasNext()) {
				iterc = iterr.next().values().iterator();
				nt();
			} else {
				iterc = null;
			}
		}

		//
		private void nt() {
			while(iterr.hasNext() || iterc.hasNext()) {
				if(!iterc.hasNext()) {
					iterc = iterr.hasNext() ?
							iterr.next().values().iterator() : null;
				}

				while(iterc.hasNext()) {
					nx = iterc.next();
					if(span || nx.getMergedCell() == null) {
						return;
					}
				}
			}
			iterc = null;
		}

		@Override
		public boolean hasNext() {
			return iterc != null;
		}

		@Override
		public TableCell next() {
			TableCell c = nx;

			if(iterc == null) {
				throw new NoSuchElementException();
			} else {
				nt();
			}
			return c;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.TableModelBuilder#appendCell(int, int, int, int, java.lang.String)
	 */
	@Override
	public void appendCell(final int row, final int col,
			final int rspan, final int cspan, final String text) {
		SortedMap<Integer, TableCell> m;
		TableCell c, d = null;

		for(int i = 0; i < rspan; i++) {
			if((m = table.get(row + i)) == null) {
				m = new TreeMap<Integer, TableCell>();
				table.put(row + i, m);
			}

			for(int j = 0; j < cspan; j++) {
				c = new Cell(row, col, rspan, cspan, text, d);
				m.put(col + j, c);
				d = d == null ? c : d;
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.TableModelBuilder#toTableModel()
	 */
	@Override
	public TableModel toTableModel() {
		return new TableModel() {

			@Override
			public int rowSize() {
				if(table.size() > 0) {
					return table.lastKey();
				} else {
					return 0;
				}
			}

			@Override
			public int columnSize() {
				SortedMap<Integer, TableCell> m;

				if(table.size() == 0) {
					return 0;
				} else if((m = table.get(table.firstKey())).size() > 0) {
					return m.lastKey();
				} else {
					return 0;
				}
			}

			@Override
			public TableCell get(int row, int col) {
				SortedMap<Integer, TableCell> m;

				if((m = table.get(row)) == null) {
					return null;
				} else {
					return m.get(col);
				}
			}

			@Override
			public Iterator<TableCell> iterator() {
				return new Iter(table, false);
			}

			@Override
			public Iterator<TableCell> allIterator() {
				return new Iter(table, true);
			}

			@Override
			public String toString() {
				StringBuilder b = new StringBuilder();

				for(Integer i : table.keySet()) {
					for(Integer j : table.get(i).keySet()) {
						b.append("(" + i + "," + j + "):");
						b.append(table.get(i).get(j).getCell());
						b.append('\n');
					}
				}
				return b.toString();
			}

		};
	}

}
