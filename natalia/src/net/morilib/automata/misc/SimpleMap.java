/*
 * Copyright 2009 Yuichiro Moriguchi
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
 * An interface represents maps simply.
 * <p>単純な写像の概念を抽象化したインターフェースです。
 * 
 * 
 * @author MORIGUCHI, Yuichiro 2010/04/18
 */
public interface SimpleMap<K, V> {
	
	/**
	 * maps the given key to the value.
	 * <p>与えられたキーを値に写像します。
	 * 
	 * @param key key to be mapped
	 * @return the value
	 */
	public V map(K key);
	
}
