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
package net.morilib.natalia.core.renderer;

import java.io.PrintWriter;
import java.util.Iterator;

import net.morilib.natalia.core.TableCell;
import net.morilib.natalia.core.TableModel;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class CSVTableRenderer extends TextTableRenderer {

	/* (non-Javadoc)
	 * @see net.morilib.natalia.renderer.TextTableRenderer#render(net.morilib.natalia.TableModel, java.io.PrintWriter)
	 */
	@Override
	public void render(TableModel model, PrintWriter out) {
		Iterator<TableCell> i;
		int c = -1;

		i = model.allIterator();
		while(i.hasNext()) {
			TableCell x = i.next();

			if(c > x.getColumn()) {
				out.println();
			} else if(c >= 0) {
				out.print(',');
			}
			c = x.getColumn();
			out.print(x.getCell());
		}
	}

}
