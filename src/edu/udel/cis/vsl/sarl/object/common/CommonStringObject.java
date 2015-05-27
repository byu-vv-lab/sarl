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

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public class CommonStringObject extends CommonSymbolicObject implements
		StringObject {

	private String value;

	CommonStringObject(String value) {
		super(SymbolicObjectKind.STRING);
		this.value = value;
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value.equals(((StringObject) o).getString());
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode() ^ value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	/**
	 * Does nothing; Basic objects have no children, so there is nothing to do.
	 */
	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public int compareTo(StringObject o) {
		return value.compareTo(o.getString());
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer buffer = new StringBuffer(value);
		return buffer;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return new StringBuffer(value);
	}

	@Override
	protected void commitChildren() {
		// no children; so nothing to do
	}

	@Override
	public StringObject commit() {
		return (StringObject) super.commit();
	}

}
