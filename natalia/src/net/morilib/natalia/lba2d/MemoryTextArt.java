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

import java.util.List;

/**
 *
 */
public class MemoryTextArt implements TextArt {

	//
	private int xmax;
	private Pixel[][] pixels;

	//
	MemoryTextArt(Pixel[][] p) {
		pixels = p;
		xmax = 0;
		for(int k = 0; k < p.length; k++) {
			xmax = xmax < p[k].length ? p[k].length : xmax;
		}
	}

	//
	MemoryTextArt(List<Pixel[]> p) {
		pixels = p.toArray(new Pixel[0][]);
		xmax = 0;
		for(int k = 0; k < p.size(); k++) {
			xmax = xmax < p.get(k).length ? p.get(k).length : xmax;
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.Quadro#get()
	 */
	@Override
	public Pixel get(int row, int column) {
		if(row > pixels.length || row < -1) {
			throw new IllegalStateException();
		} else if(row == pixels.length || row == -1) {
			return Pixel.BOUND;
		} else if(column > xmax || column < -1) {
			throw new IllegalStateException();
		} else if(column == xmax || column == -1) {
			return Pixel.BOUND;
		} else if(column >= pixels[row].length) {
			return Pixel.SPACE;
		} else {
			return pixels[row][column];
		}
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.lba2d.TextArt#getRows()
	 */
	@Override
	public int getRows() {
		return pixels.length;
	}

	/* (non-Javadoc)
	 * @see net.morilib.natalia.lba2d.TextArt#getColumns()
	 */
	@Override
	public int getColumns() {
		return xmax;
	}

}
