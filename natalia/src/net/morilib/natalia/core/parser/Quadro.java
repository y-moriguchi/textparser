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

public interface Quadro {

	/**
	 * 
	 * @return
	 */
	public Pixel get();

	/**
	 * 
	 * @return
	 */
	public Scratch getScratch();

	/**
	 * 
	 * @param x
	 * @return
	 */
	public Quadro setScratch(Scratch x);

	/**
	 * 
	 * @return
	 */
	public Quadro moveNorth();

	/**
	 * 
	 * @return
	 */
	public Quadro moveEast();

	/**
	 * 
	 * @return
	 */
	public Quadro moveSouth();

	/**
	 * 
	 * @return
	 */
	public Quadro moveWest();

	/**
	 * 
	 * @return
	 */
	public Quadro crlf();

	/**
	 * 
	 * @return
	 */
	public Quadro forward();

	/**
	 * 
	 * @return
	 */
	public Quadro turnNorth();

	/**
	 * 
	 * @return
	 */
	public Quadro turnEast();

	/**
	 * 
	 * @return
	 */
	public Quadro turnSouth();

	/**
	 * 
	 * @return
	 */
	public Quadro turnWest();

	/**
	 * 
	 * @return
	 */
	public Quadro turnLeft();

	/**
	 * 
	 * @return
	 */
	public Quadro turnRight();

	/**
	 * 
	 * @return
	 */
	public Quadro back();

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
	public Quadro mark(Object o);

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
	public Quadro clearTextRegister();

	/**
	 * 
	 * @param c
	 * @return
	 */
	public Quadro appendTextRegister(int c);

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
	public Quadro setRowRegister(int x);

	/**
	 * 
	 * @return
	 */
	public int getColumnRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro setColumnRegister(int x);

	/**
	 * 
	 * @return
	 */
	public int getRowSpanRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro setRowSpanRegister(int x);

	/**
	 * 
	 * @return
	 */
	public int getColumnSpanRegister();

	/**
	 * 
	 * @return
	 */
	public Quadro setColumnSpanRegister(int x);

	/**
	 * 
	 * @return
	 */
	public TableModelBuilder getTableModelBuilder();

	/**
	 * 
	 */
	public Quadro setTableModelBuilder(TableModelBuilder x);

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
