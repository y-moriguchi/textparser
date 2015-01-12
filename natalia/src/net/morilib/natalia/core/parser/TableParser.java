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
import net.morilib.natalia.core.TableModelBuilder;
import net.morilib.natalia.lba2d.Quadro;
import net.morilib.natalia.lba2d.QuadroFactory;
import net.morilib.natalia.lba2d.Transition;

/**
 *
 */
public class TableParser {

	//
	private static final int INFINITE_LOOP = 720000;
	private static final TableParser INS = new TableParser();

	/**
	 * 
	 * @return
	 */
	public static final TableParser getInstance() {
		return INS;
	}

	//
	static void go(ParserState init, ParserState end,
			Transition<Scratch, ParserState> t, Quadro<Scratch> q) {
		ParserState s = init, p = null;
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
	static TableModel parseTable(Quadro<Scratch> q) {
		q.setTableModelBuilder(new SimpleTableModelBuilder());
		go(ParserState.FSEARCH_INIT, ParserState.FSEARCH_END,
				FrameSearchTransition.INSTANCE, q);
		return q.getTableModelBuilder().toTableModel();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public TableModel parseTable(String s) {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(s, Scratch.NONE);
		return parseTable(q);
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parseTable(Reader ins) throws IOException {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(new BufferedReader(ins),
				Scratch.NONE);
		return parseTable(q);
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parseTable(InputStream ins) throws IOException {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(new BufferedReader(
				new InputStreamReader(ins)), Scratch.NONE);
		return parseTable(q);
	}

	/**
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public TableModel parseTable(File f) throws IOException {
		InputStream ins = null;

		try {
			ins = new FileInputStream(f);
			return parseTable(ins);
		} finally {
			if(ins != null) {
				ins.close();
			}
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public TableModel parseMySQLOutput(TableModel m) {
		TableModelBuilder b;
		int k;

		b = new SimpleTableModelBuilder();
		for(int j = 1; j <= m.columnSize(); j++) {
			b.appendCell(1, j, 1, 1, m.get(1, j).getCell());
			k = 2;
			for(String s : m.get(2, j).getCell().split("\n")) {
				b.appendCell(k++, j, 1, 1, s.trim());
			}
		}
		return b.toTableModel();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public TableModel parseMySQLOutput(String s) {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(s, Scratch.NONE);
		return parseMySQLOutput(parseTable(q));
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parseMySQLOutput(Reader ins) throws IOException {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(new BufferedReader(ins),
				Scratch.NONE);
		return parseMySQLOutput(parseTable(q));
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException
	 */
	public TableModel parseMySQLOutput(
			InputStream ins) throws IOException {
		Quadro<Scratch> q;

		q = QuadroFactory.newInstance(new BufferedReader(
				new InputStreamReader(ins)), Scratch.NONE);
		return parseMySQLOutput(parseTable(q));
	}

	/**
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public TableModel parseMySQLOutput(File f) throws IOException {
		InputStream ins = null;

		try {
			ins = new FileInputStream(f);
			return parseMySQLOutput(ins);
		} finally {
			if(ins != null) {
				ins.close();
			}
		}
	}

}
