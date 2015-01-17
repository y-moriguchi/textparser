/*
 * Copyright 2009-2013 Yuichiro Moriguchi
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

/**
 * <i>USEful Implements</i> for Object.hashCode().
 * <p>ハッシュコード関連の便利な関数である.
 * 
 * <p>比較的よいハッシュコードを生成する規則として以下のような方法がある
 * (参照: Joshua Bloch. The Effective Java).この方法を本ドキュメントでは
 * &quot; EJ生成法(EJ method) &quot; と記述する.
 * 
 * <ol>
 * <li>ローカル変数rを定義し、rにハッシュコードの初期値{@link #INIT}
 * を代入する.
 * <li>オブジェクトを識別する、つまりequalsメソッドにて評価される
 * フィールドfについて次の処理をする.
 * 
 * <ol>
 * <li>フィールドのhashCodeを生成する(この値をhとする).
 * 生成するメソッドは以下の通りである.
 * <table>
 * <tr><th>fの型  </th><th>hashCodeの生成法</th></tr>
 * <tr><td>byte, short, char, int</td><td>fの値そのもの</td></tr>
 * <tr><td>long   </td><td>{@link #hashCode(long)}  </td></tr>
 * <tr><td>float  </td><td>{@link #hashCode(float)} </td></tr>
 * <tr><td>double </td><td>{@link #hashCode(double)}</td></tr>
 * <tr><td>boolean</td><td>{@link #hashCode(double)}</td></tr>
 * <tr><td>Object </td><td>Object.hashCode()</td></tr>
 * <tr><td>配列   </td>
 * <td>sumHashCodeメソッド群<br/>
 *     {@link #sumHashCode(boolean[])}<br/>
 *     {@link #sumHashCode(byte[])}<br/>
 *     {@link #sumHashCode(short[])}<br/>
 *     {@link #sumHashCode(char[])}<br/>
 *     {@link #sumHashCode(int[])}<br/>
 *     {@link #sumHashCode(long[])}<br/>
 *     {@link #sumHashCode(float[])}<br/>
 *     {@link #sumHashCode(double[])}<br/>
 *     {@link #sumHashCode(boolean[])}<br/>
 *     {@link #sumHashCode(Object[])}<br/>
 * </td></tr>
 * </table>
 * <li>{@link #A}*h + rを計算し、rに代入する.
 * </ol>
 * </ol>
 * 
 * @author MORIGUCHI, Yuichiro 2005/04/09
 */
public final class Hashes {
	
	/**
	 *  A initial hash code for the EJ method.
	 * <p>EJ生成法におけるハッシュコードの初期値である.
	 */
	public static final int INIT = 17;
	
	/**
	 *  A multiplication coefficient for the EJ method.
	 * <p>EJ生成法におけるハッシュコードの乗数である.
	 */
	public static final int A = 37;
	
	/**
	 * calculates the hash code of the given argument.
	 * <p>引数に対応するhashCodeを返す.
	 * 
	 * @param b the value to be calculated
	 * @return  the hash code
	 */
	public static int hashCode(boolean b) {
		return b ? 0 : 1;
	}
	
	/**
	 * calculates the hash code of the given argument.
	 * <p>引数に対応するhashCodeを返す.
	 * 
	 * @param l the value to be calculated
	 * @return  the hash code
	 */
	public static int hashCode(long l) {
		return (int)(l ^ (l >>> 32));
	}
	
	/**
	 * calculates the hash code of the given argument.
	 * <p>引数に対応するhashCodeを返す.
	 * 
	 * @param d the value to be calculated
	 * @return  the hash code
	 */
	public static int hashCode(double d) {
		long l;
		l = Double.doubleToLongBits(d);
		return (int)(l ^ (l >>> 32));
	}
	
	/**
	 * calculates the hash code of the given argument.
	 * <p>引数に対応するhashCodeを返す.
	 * 
	 * @param f the value to be calculated
	 * @return  the hash code
	 */
	public static int hashCode(float f) {
		return Float.floatToIntBits(f);
	}
	
	/**
	 * calculates the hash code of the given argument.
	 * <p>引数に対応するhashCodeを返す.
	 * 
	 * @param o the value to be calculated
	 * @return  the hash code
	 */
	public static int hashCode(Object o) {
		return (o == null) ? 0 : o.hashCode();
	}
	
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(int[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + arr[i];
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(boolean[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + hashCode(arr[i]);
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(byte[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + arr[i];
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(short[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + arr[i];
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(char[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + arr[i];
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(long[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + hashCode(arr[i]);
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(float[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + hashCode(arr[i]);
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(double[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + hashCode(arr[i]);
		}
		return res;
	}
	
	/**
	 * calculates the hash code of the given array.
	 * <p>引数の配列に対応するhashCodeを返す.
	 * 
	 * @param arr the array to be calculated
	 * @return    the hash code
	 */
	public static int sumHashCode(Object[] arr) {
		if(arr == null) {
			return 0;
		}
		
		int res = 0;
		for(int i = 0; i < arr.length; i++) {
			res = A * res + hashCode(arr[i]);
		}
		return res;
	}

}
