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

import net.morilib.natalia.core.TableModelBuilder;

public interface Quadro<S> {

	/**
	 * 
	 * @return
	 */
	public Quadro<S> fork();

	/**
	 * 
	 * @return
	 */
	public Pixel get();

	/**
	 * 
	 * @return
	 */
	public S getScratch();

	/**
	 * 
	 * @param x
	 * @return
	 */
	public Quadro<S> setScratch(S x);

	/**
	 * 
	 * @return
	 */
	public Quadro<S> moveNorth();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> moveEast();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> moveSouth();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> moveWest();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> crlf();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> forward();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> turnNorth();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> turnEast();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> turnSouth();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> turnWest();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> turnLeft();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> turnRight();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> back();

	/**
	 * 
	 * @return
	 */
	public boolean isDirectionNorth();

	/**
	 * 
	 * @return
	 */
	public boolean isDirectionEast();

	/**
	 * 
	 * @return
	 */
	public boolean isDirectionSouth();

	/**
	 * 
	 * @return
	 */
	public boolean isDirectionWest();

	/**
	 * 
	 * @return
	 */
	public boolean isDirectionLatitudinal();

	/**
	 * 
	 * @return
	 */
	public boolean isDirectionLongitudinal();

	/**
	 * 
	 * @param o
	 * @return
	 */
	public Quadro<S> mark(Object o);

	/**
	 * 
	 * @param o
	 * @return
	 */
	public boolean isMarked(Object o);

	/**
	 * 
	 * @return
	 */
	public Pixel peekForward();

	/**
	 * 
	 * @return
	 */
	public Pixel peekLeft();

	/**
	 * 
	 * @return
	 */
	public Pixel peekRight();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> clearTextRegister();

	/**
	 * 
	 * @param c
	 * @return
	 */
	public Quadro<S> appendTextRegister(int c);

	/**
	 * 
	 * @return
	 */
	public String getTextRegister();

	/**
	 * 
	 * @return
	 */
	public int getRowRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> setRowRegister(int x);

	/**
	 * 
	 * @return
	 */
	public int getColumnRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> setColumnRegister(int x);

	/**
	 * 
	 * @return
	 */
	public int getRowSpanRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> setRowSpanRegister(int x);

	/**
	 * 
	 * @return
	 */
	public int getColumnSpanRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro<S> setColumnSpanRegister(int x);

	/**
	 * 
	 * @return
	 */
	public TableModelBuilder getTableModelBuilder();

	/**
	 * 
	 */
	public Quadro<S> setTableModelBuilder(TableModelBuilder x);

	/**
	 * 
	 * @return
	 */
	public int getRowPosition();

	/**
	 * 
	 * @return
	 */
	public int getColumnPosition();

	/**
	 * 
	 * @return
	 */
	public String getDirection();

}
