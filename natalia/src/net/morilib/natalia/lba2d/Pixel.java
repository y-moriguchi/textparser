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

/**
 * 
 */
public class Pixel {

	//
	static final Pixel BOUND = new Pixel(-72);
	static final Pixel SPACE = new Pixel(' ');
	static final Pixel EQ_TO_LEFT = new Pixel(-1);

	//
	private static Pixel[] flyweight = new Pixel[256];

	//
	private int ch;

	//
	Pixel(int c) {
		ch = c;
	}

	//
	static Pixel getPixel(int c) {
		if(c < 0 || c >= flyweight.length) {
			return new Pixel(c);
		} else if(flyweight[c] == null) {
			flyweight[c] = new Pixel(c);
		}
		return flyweight[c];
	}

	/**
	 * 
	 * @return
	 */
	public boolean isWall() {
		return ch == '-' || ch == '=' || ch == '|';
	}

	/**
	 * 
	 * @return
	 */
	public boolean isJunction() {
		return ch == '+';
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEqualsToLeft() {
		return ch == -1;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBound() {
		return ch == -72;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCell() {
		return !isWall() && !isJunction() && !isBound();
	}

	/**
	 * 
	 * @return
	 */
	public int getChar() {
		return ch;
	}

}
