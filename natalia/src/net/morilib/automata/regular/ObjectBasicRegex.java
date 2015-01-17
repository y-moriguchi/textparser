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
package net.morilib.automata.regular;

/**
 *
 *
 * @author MORIGUCHI, Yuichiro 2013/01/02
 */
public class ObjectBasicRegex implements BasicRegex {

	//
	private Object object;

	/**
	 * 
	 * @param o
	 */
	public ObjectBasicRegex(Object o) {
		this.object = o;
	}

	/* (non-Javadoc)
	 * @see net.morilib.automata.regular.BasicRegEx#simplify()
	 */
	@Override
	public BasicRegex simplify() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (object != null) ? object.hashCode() : 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(!(o instanceof ObjectBasicRegex)) {
			return false;
		} else if(object != null) {
			return object.equals(((ObjectBasicRegex)o).object);
		} else {
			return ((ObjectBasicRegex)o).object == null;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (object != null) ? object.toString() : "null";
	}

}
