/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.object.common;

import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public class CommonCharObject extends CommonSymbolicObject implements
		CharObject {

	private char value;

	CommonCharObject(char theChar) {
		super(SymbolicObjectKind.CHAR);
		this.value = theChar;
	}

	@Override
	public char getChar() {
		return value;
	}

	/**
	 * Compares the boolean values of two boolean objects Know that o has kind
	 * BOOLEAN and is not == to this.
	 * 
	 * @return Boolean
	 */
	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value == ((CharObject) o).getChar();
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode()
				^ new Character(value).hashCode();
	}

	@Override
	public String toString() {
		return new Character(value).toString();
	}

	/**
	 * Does nothing; Basic objects have no children, so there is nothing to do.
	 */
	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public int compareTo(CharObject o) {
		return (new Character(value)).compareTo(o.getChar());
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer buffer = new StringBuffer(Character.toString(value));
		return buffer;

	}

	@Override
	public StringBuffer toStringBufferLong() {
		return new StringBuffer(Character.toString(value));
	}

	@Override
	protected void commitChildren() {
		// no children; so nothing to do
	}

	@Override
	public CharObject commit() {
		return (CharObject) super.commit();
	}
}
