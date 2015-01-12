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

/**
 *
 */
public class Scratch {

	//
	private static final int _FRAME = 1;
	private static final int _CORNER = 2;
	private static final int _BORDER = 4;
	private static final int _CROSSING = 8;
	private static final int _VERTICAL_FRAME = 16;
	private static final int _LONGITUDE_BORDER = 32;
	private static final int _LATITUDE_BORDER = 64;
	private static final int _ALL = 127;

	/**
	 * 
	 */
	public static final Scratch NONE = _get(0);

	/**
	 * 
	 */
	public static final Scratch FRAME_CORNER = _get(_FRAME | _CORNER);

	/**
	 * 
	 */
	public static final Scratch FRAME = _get(_FRAME);

	/**
	 * 
	 */
	public static final Scratch BORDER_CROSSING = _get(_BORDER | _CROSSING);

	/**
	 * 
	 */
	public static final Scratch VERTICAL_FRAME = _get(_FRAME | _VERTICAL_FRAME);

	/**
	 * 
	 */
	public static final Scratch LONGITUDINAL_BORDER =
			_get(_BORDER | _LONGITUDE_BORDER);

	/**
	 * 
	 */
	public static final Scratch LATITUDINAL_BORDER =
			_get(_BORDER | _LATITUDE_BORDER);

	/**
	 * 
	 */
	public static final Scratch FRAME_END_CORNER =
			_get(_FRAME | _CORNER | _VERTICAL_FRAME);

	//
	private static Scratch[] flyweight;

	//
	private int attrs;

	//
	private Scratch(int x) {
		attrs = x;
	}

	//
	private static Scratch _get(int x) {
		if(flyweight == null) {
			flyweight = new Scratch[_ALL + 1];
		}

		if(flyweight[x] == null) {
			flyweight[x] = new Scratch(x);
		}
		return flyweight[x];
	}

	/**
	 * 
	 * @param border2
	 * @return
	 */
	public Scratch add(Scratch border2) {
		return _get(attrs | border2.attrs);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFrameCorner() {
		return (attrs & _CORNER) != 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFrame() {
		return (attrs & _FRAME) != 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBorder() {
		return (attrs & _BORDER) != 0;
	}

	/**
	 * @return
	 */
	public boolean isBorderCrossing() {
		return (attrs & _CROSSING) != 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isVerticalFrame() {
		return (attrs & _VERTICAL_FRAME) != 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLongitudinalBorder() {
		return (attrs & _LONGITUDE_BORDER) != 0;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLatitudinalBorder() {
		return (attrs & _LATITUDE_BORDER) != 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		switch(attrs % 16) {
		case 0:   return " ";
		case 1:   return "=";
		case 2:   return "*";
		case 3:   return "*";
		case 4:   return "-";
		case 5:   return "=";
		case 6:   return "*";
		case 7:   return "*";
		case 8:   return "+";
		case 9:   return "=";
		case 10:  return "*";
		case 11:  return "*";
		case 12:  return "+";
		case 13:  return "=";
		case 14:  return "*";
		case 15:  return "*";
		default:  return "x";
		}
	}

}
