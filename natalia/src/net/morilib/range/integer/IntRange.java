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
package net.morilib.range.integer;

import java.util.Collection;

import net.morilib.range.Range;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2011/10/09
 */
public interface IntRange extends Iterable<Integer> {

	/**
	 * 
	 */
	public static final IntRange O = new IntInterval(false);

	/**
	 * 
	 */
	public static final IntRange UINT =
		new IntInterval(Integer.MIN_VALUE, Integer.MAX_VALUE);

	/**
	 * 
	 * @param x
	 * @return
	 */
	public boolean contains(int x);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public boolean isContained(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public boolean isIndependent(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public boolean isOverlapped(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public boolean containsAll(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public boolean containsAny(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public IntRange join(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public IntRange meet(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public IntRange difference(IntRange r);

	/**
	 * 
	 * @param r
	 * @return
	 */
	public IntRange xor(IntRange r);

	/**
	 * 
	 * @return
	 */
	public IntRange complement();

	/**
	 * 
	 * @return
	 */
	public int minimum();

	/**
	 * 
	 * @return
	 */
	public int maximum();

	/**
	 * 
	 * @return
	 */
	public Collection<IntInterval> intervals();

	/**
	 * 
	 * @return
	 */
	public Range wrappingRange();

	/**
	 * 
	 * @return
	 */
	public long cardinality();

}
