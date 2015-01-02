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
package net.morilib.natalia.cmd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.morilib.natalia.core.TableModel;
import net.morilib.natalia.core.parser.TableParser;
import net.morilib.natalia.core.renderer.HTMLTableRenderer;
import net.morilib.natalia.core.renderer.TableRenderer;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public class T2T {

	//
	static void perror(String s, Object... os) {
		System.err.printf(s + "\n", os);
		System.exit(2);
	}

	/**
	 * 
	 * @param a
	 */
	public static void main(String[] a) {
		OutputStream out = null;
		InputStream  ins = null;
		TableRenderer r = null;
		TableModel m;
		String f = null;
		int k;

		for(k = 0; k < a.length; k++) {
			if(a[k].charAt(0) != '-') {
				break;
			} else if(a[k].equals("-html")) {
				f = ++k < a.length ? a[k] : null;
				r = new HTMLTableRenderer();
			} else {
				perror("invalid option: %s", a[k]);
			}
		}

		try {
			if(k < a.length) {
				ins = new FileInputStream(a[k]);
			} else {
				ins = System.in;
			}

			if(f != null) {
				out = new FileOutputStream(f);
			} else {
				out = System.out;
			}

			m = TableParser.getInstance().parseTable(ins);
			r.render(m, out);
		} catch(IOException e) {
			perror("IO error");
		} finally {
			try {
				if(ins != null)  ins.close();
				if(out != null)  out.close();
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		System.exit(0);
	}

}
