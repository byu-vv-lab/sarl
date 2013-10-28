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

import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public class CommonIntObject extends CommonSymbolicObject implements IntObject {

	private int value;

	CommonIntObject(int value) {
		super(SymbolicObjectKind.INT);
		this.value = value;
	}

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value == ((IntObject) o).getInt();
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode() ^ new Integer(value).hashCode();
	}

	@Override
	public String toString() {
		return new Integer(value).toString();
	}

	@Override
	public IntObject minWith(IntObject that) {
		return value <= that.getInt() ? this : that;
	}

	@Override
	public IntObject maxWith(IntObject that) {
		return value >= that.getInt() ? this : that;
	}

	@Override
	public IntObject minus(IntObject that) {
		return new CommonIntObject(value - that.getInt());
	}

	@Override
	public IntObject plus(IntObject that) {
		return new CommonIntObject(value + that.getInt());
	}

	@Override
	public int signum() {
		if (value > 0)
			return 1;
		else if (value == 0)
			return 0;
		else
			return -1;
	}

	@Override
	public boolean isZero() {
		return value == 0;
	}

	@Override
	public boolean isPositive() {
		return value > 0;
	}

	@Override
	public boolean isNegative() {
		return value < 0;
	}

	@Override
	public boolean isOne() {
		return value == 1;
	}

	/**
	 * Does nothing; Basic objects have no children, so there is nothing to do.
	 */
	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public int compareTo(IntObject o) {
		return value - o.getInt();
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer buffer = new StringBuffer(Integer.toString(value));
		if (atomize)
			this.atomize(buffer);
		return buffer;
			
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return new StringBuffer(Integer.toString(value));
	}

}
