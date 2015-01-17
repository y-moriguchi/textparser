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
package net.morilib.range.integer;

import java.util.Collection;

/**
 * This is the builder class for constructing an instance of
 * {@link MergedRange}.
 * <p>{@link MergedRange}を構築するためのビルダークラスである.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class IntRangeAdder {
	
	//
	private IntMergedRange range;
	
	/**
	 * constructs a new RangeAdder.
	 * <p>新しいRangeAdderを構築する.
	 */
	public IntRangeAdder() {
		range = new IntMergedRange();
	}

	/**
	 * adds a {@link IntRange} object to this instance.
	 * <p>{@link IntRange}オブジェクトをこのインスタンスに追加する.
	 * 
	 * @param r  a {@link IntRange} object to be added
	 * @return  this instance
	 */
	public IntRangeAdder add(IntRange r) {
		range.intervals.addAll(r.intervals());
		return this;
	}
	
	/**
	 * adds all {@link IntRange} objects in the given collection
	 * to this instance.
	 * <p>与えられたコレクションにある全ての{@link IntRange}オブジェクトを
	 * このインスタンスに追加する.
	 * 
	 * @param c  a collection of {@link IntRange} objects to be added
	 * @return  this instance
	 */
	public IntRangeAdder addAll(Collection<IntInterval> c) {
		range.intervals.addAll(c);
		return this;
	}
	
	/**
	 * adds a {@link IntInterval} object to this instance.
	 * <p>{@link IntRange}オブジェクトをこのインスタンスに追加する.
	 * 
	 * @param c  a {@link IntInterval} object to be added
	 * @return  this instance
	 */
	public IntRangeAdder addInterval(IntInterval c) {
		range.intervals.add(c);
		return this;
	}

	/**
	 * gets the {@link IntRange} object builded by this class.
	 * <p>このクラスにより構築された{@link IntRange}オブジェクトを得る.
	 */
	public IntMergedRange toRange() {
		return range;
	}

}
