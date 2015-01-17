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
 * This interface represents the pair of objects.
 * <p>オブジェクトの対(Pair)を表すインターフェースである.
 * 
 * @author MORIGUCHI, Yuichiro 2005/09/21
 */
public class Tuple2<A, B> implements Pair<A, B> {

	//
	private A valueA;
	private B valueB;

	/**
	 * 
	 * @param a
	 * @param b
	 */
	public Tuple2(A a, B b) {
		valueA = a;
		valueB = b;
	}

	/**
	 * gets the first value of this pair.
	 * <p>対の第1の値を得る.
	 */
	public A getA() {
		return valueA;
	}

	/**
	 * gets the second value of this pair.
	 * <p>対の第2の値を得る.
	 */
	public B getB() {
		return valueB;
	}

	//
	static boolean _equals(Object obj1, Object obj2) {
		return (obj1 == null) ? obj2 == null : obj1.equals(obj2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		} else if(o instanceof Tuple2<?, ?>) {
			Tuple2<?, ?> o2 = (Tuple2<?, ?>)o;

			return (_equals(valueA, o2.valueA) &&
					_equals(valueB, o2.valueB));
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int res = 17;

		res = 37 * res + (valueA != null ? valueA.hashCode() : 0);
		res = 37 * res + (valueB != null ? valueB.hashCode() : 0);
		return res;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + valueA + "," + valueB + ")";
	}

}
