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
package edu.udel.cis.vsl.sarl.collections.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.object.common.CommonSymbolicObject;

public abstract class CommonSymbolicCollection<T extends SymbolicExpression>
		extends CommonSymbolicObject implements SymbolicCollection<T> {

	private SymbolicCollectionKind collectionKind;

	CommonSymbolicCollection(SymbolicCollectionKind kind) {
		super(SymbolicObjectKind.EXPRESSION_COLLECTION);
		this.collectionKind = kind;
	}

	@Override
	public SymbolicCollectionKind collectionKind() {
		return collectionKind;
	}

	/**
	 * Tells whether the two collections (o and this) are equal, assuming o and
	 * this have the same kind.
	 * 
	 * @param o
	 *            a symbolic collection with the same
	 *            <code>collectionKind</code> as this
	 * @return true iff the two collections are equal
	 */
	protected abstract boolean collectionEquals(SymbolicCollection<T> o);

	@Override
	protected boolean intrinsicEquals(SymbolicObject o) {
		@SuppressWarnings("unchecked")
		SymbolicCollection<T> that = (SymbolicCollection<T>) o;

		if (collectionKind != that.collectionKind())
			return false;
		if (size() != that.size())
			return false;
		return collectionEquals(that);
	}

	@Override
	public T getFirst() {
		return iterator().next();
	}

	@Override
	public String toString() {
		return toStringBuffer(true).toString();
	}

}
