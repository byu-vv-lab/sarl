/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.object.common;

import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public class CommonBooleanObject extends CommonSymbolicObject implements
		BooleanObject {

	private boolean value;

	CommonBooleanObject(boolean value) {
		super(SymbolicObjectKind.BOOLEAN);
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the field {@link #value}.
	 */
	@Override
	public boolean getBoolean() {
		return value;
	}

	/**
	 * Compares the boolean values of two boolean objects
	 * Know that o has kind BOOLEAN and is not == to this.
	 * @return Boolean
	 */
	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value == ((BooleanObject) o).getBoolean();
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode() ^ new Boolean(value).hashCode();
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}

	/**
	 * Does nothing; Basic objects have no children, so there is nothing to do.
	 */
	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	/**
	 * Returns 1 when comparing a true BooleanObject to a false one.
	 * Returns -1 when comparing a false BooleanObject to a true one.
	 * Returns 0 when comparing false BooleanObject to a false one, or a true BooleanObject to a true one.
	 * @return 1, -1, or 0
	 */
	@Override
	public int compareTo(BooleanObject o) {
		return value ? (o.getBoolean() ? 0 : 1) : (o.getBoolean() ? -1 : 0);
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer buffer = new StringBuffer(Boolean.toString(value));
		return buffer;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return new StringBuffer(Boolean.toString(value));
	}

}
