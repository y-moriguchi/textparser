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

import java.util.Iterator;

/**
 *
 */
public interface TableModel extends Iterable<TableCell> {

	/**
	 * 
	 * @return
	 */
	public int rowSize();

	/**
	 * 
	 * @return
	 */
	public int columnSize();

	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public TableCell get(int row, int col);

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<TableCell> iterator();

	/**
	 * 
	 * @return
	 */
	public Iterator<TableCell> allIterator();

}
