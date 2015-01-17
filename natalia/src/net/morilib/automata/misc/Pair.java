/*
 * Copyright 2009-2010 Yuichiro Moriguchi
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
package net.morilib.automata.misc;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2010/10/02
 */
public interface Pair<A, B> {
	
	/**
	 * gets the first value of this pair.
	 * <p>対の第1の値を得る.
	 */
	public A getA();
	
	/**
	 * gets the second value of this pair.
	 * <p>対の第2の値を得る.
	 */
	public B getB();

}
