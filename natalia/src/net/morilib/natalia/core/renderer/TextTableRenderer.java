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

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.morilib.natalia.core.TableModel;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public abstract class TextTableRenderer implements TableRenderer {

	/**
	 * 
	 * @param model
	 * @param out
	 */
	public abstract void render(TableModel model, PrintWriter out);

	/**
	 * 
	 * @param model
	 * @param out
	 */
	public void render(TableModel model, Writer out) {
		PrintWriter p;

		p = new PrintWriter(new BufferedWriter(out));
		render(model, p);
		p.flush();
	}

	/**
	 * 
	 * @param model
	 * @param out
	 */
	public void render(TableModel model, OutputStream out) {
		render(model, new OutputStreamWriter(out));
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	public String toString(TableModel model) {
		StringWriter w = new StringWriter();

		render(model, w);
		return w.toString();
	}

}
