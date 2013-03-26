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
package edu.udel.cis.vsl.sarl.prove.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;


/**
 * A query consists of an assumption p and a predicate q, both boolean-valued
 * expressions. Two Query objects are equal iff theirs assumptions are equal and
 * their predicates are equal.
 */
public class SymbolicQuery {

	private SymbolicExpression assumption;

	private SymbolicExpression predicate;

	/**
	 * Creates new query. Query is immutable.
	 */
	SymbolicQuery(SymbolicExpression assumption, SymbolicExpression predicate) {
		if (assumption == null)
			throw new NullPointerException("null assumption");
		if (predicate == null)
			throw new NullPointerException("null predicate");
		this.assumption = assumption;
		this.predicate = predicate;
	}

	public SymbolicExpression assumption() {
		return assumption;
	}

	public SymbolicExpression predicate() {
		return predicate;
	}

	public int hashCode() {
		return assumption.hashCode() + predicate.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof SymbolicQuery) {
			SymbolicQuery that = (SymbolicQuery) object;

			return assumption.equals(that.assumption)
					&& predicate.equals(that.predicate);
		}
		return false;
	}
}
