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
package net.morilib.range;

import java.util.Collection;

/**
 * This is the builder class for constructing an instance of
 * {@link MergedRange}.
 * <p>{@link MergedRange}を構築するためのビルダークラスである.
 * 
 * @author MORIGUCHI, Yuichiro 2008/01/01
 */
public final class RangeAdder {
	
	//
	private MergedRange range;
	
	/**
	 * constructs a new RangeAdder.
	 * <p>新しいRangeAdderを構築する.
	 */
	public RangeAdder() {
		range = new MergedRange();
	}

	/**
	 * adds a {@link Range} object to this instance.
	 * <p>{@link Range}オブジェクトをこのインスタンスに追加する.
	 * 
	 * @param r  a {@link Range} object to be added
	 * @return  this instance
	 */
	public RangeAdder add(Range r) {
		range.addAll(r.intervals());
		return this;
	}
	
	/**
	 * adds all {@link Range} objects in the given collection
	 * to this instance.
	 * <p>与えられたコレクションにある全ての{@link Range}オブジェクトを
	 * このインスタンスに追加する.
	 * 
	 * @param c  a collection of {@link Range} objects to be added
	 * @return  this instance
	 */
	public RangeAdder addAll(Collection<Interval> c) {
		range.addAll(c);
		return this;
	}
	
	/**
	 * adds a {@link Interval} object to this instance.
	 * <p>{@link Range}オブジェクトをこのインスタンスに追加する.
	 * 
	 * @param c  a {@link Interval} object to be added
	 * @return  this instance
	 */
	public RangeAdder addInterval(Interval c) {
		range.add(c);
		return this;
	}

	/**
	 * gets the {@link Range} object builded by this class.
	 * <p>このクラスにより構築された{@link Range}オブジェクトを得る.
	 */
	public MergedRange toRange() {
		return range;
	}

}
