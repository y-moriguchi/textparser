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

import net.morilib.natalia.core.TableModel;
import net.morilib.natalia.core.parser.Quadro;
import net.morilib.natalia.core.parser.QuadroFactory;
import net.morilib.natalia.core.parser.SimpleTableModelBuilder;

/**
 *
 */
public class DbTextParser {

	//
	private static final int INFINITE_LOOP = 720000;
	private static final DbTextParser INS = new DbTextParser();

	/**
	 * 
	 * @return
	 */
	public static final DbTextParser getInstance() {
		return INS;
	}

	//
	static void go(PS init, PS end, Transition t, Quadro q) {
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
	static TableModel parsePostgresTable(Quadro q) {
		q.setTableModelBuilder(new SimpleTableModelBuilder());
		go(PS.POSTGRES_MAIN_INIT, PS.POSTGRES_MAIN_END,
				PostgresMainTransition.I, q);
		return q.getTableModelBuilder().toTableModel();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public TableModel parsePostgresTable(String s) {
		Quadro q;

		q = QuadroFactory.newInstance(s);
		return parsePostgresTable(q);
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parsePostgresTable(Reader ins) throws IOException {
		Quadro q;

		q = QuadroFactory.newInstance(new BufferedReader(ins));
		return parsePostgresTable(q);
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parsePostgresTable(InputStream ins) throws IOException {
		Quadro q;

		q = QuadroFactory.newInstance(new BufferedReader(
				new InputStreamReader(ins)));
		return parsePostgresTable(q);
	}

	/**
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public TableModel parsePostgresTable(File f) throws IOException {
		InputStream ins = null;

		try {
			ins = new FileInputStream(f);
			return parsePostgresTable(ins);
		} finally {
			if(ins != null) {
				ins.close();
			}
		}
	}

}
