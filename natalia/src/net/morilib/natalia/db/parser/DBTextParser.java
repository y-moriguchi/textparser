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
package net.morilib.natalia.db.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import net.morilib.natalia.core.Scratch;
import net.morilib.natalia.core.SimpleTableModelBuilder;
import net.morilib.natalia.core.TableModel;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import net.morilib.natalia.lba2d.Transition;

/**
 *
 */
public class DBTextParser {

	//
	private static final int INFINITE_LOOP = 720000;
	private static final DBTextParser INS = new DBTextParser();

	/**
	 * 
	 * @return
	 */
	public static final DBTextParser getInstance() {
		return INS;
	}

	//
	static void go(PS init, PS end, Transition<Scratch, PS> t,
			Quadro<Scratch> q) {
		PS s = init, p = null;
		int c = 0;

		for(; !s.equals(end); p = s) {
			s = t.transit(q, s);
			if(!s.equals(p)) {
				c = 0;
			} else if(c++ > INFINITE_LOOP) {
				throw new IllegalStateException("maybe infinite loop");
			}
		}
	}

	//
	static TableModel parseDBText(Quadro<Scratch> q) {
		q.setTableModelBuilder(new SimpleTableModelBuilder());
		go(PS.POSTGRES_MAIN_INIT, PS.POSTGRES_MAIN_END,
				DBParseMainTransition.I, q);
		return q.getTableModelBuilder().toTableModel();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public TableModel parseDBText(String s) {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(s, Scratch.NONE);
		return parseDBText(q);
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parseDBText(Reader ins) throws IOException {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(new BufferedReader(ins),
				Scratch.NONE);
		return parseDBText(q);
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parseDBText(InputStream ins) throws IOException {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(new BufferedReader(
				new InputStreamReader(ins)), Scratch.NONE);
		return parseDBText(q);
	}

	/**
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public TableModel parseDBText(File f) throws IOException {
		InputStream ins = null;

		try {
			ins = new FileInputStream(f);
			return parseDBText(ins);
		} finally {
			if(ins != null) {
				ins.close();
			}
		}
	}

}
