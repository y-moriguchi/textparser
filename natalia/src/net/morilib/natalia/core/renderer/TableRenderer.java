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

import java.io.IOException;
import java.io.OutputStream;

import net.morilib.natalia.core.TableModel;

/**
 *
 * @author Yuichiro MORIGUCHI
 */
public interface TableRenderer {

	/**
	 * 
	 * @param model
	 * @param out
	 * @throws IOException
	 */
	public void render(TableModel model,
			OutputStream out) throws IOException;

}