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
package edu.udel.cis.vsl.sarl.IF.expr;

public interface ReferenceExpression extends SymbolicExpression {

	public enum ReferenceKind {
		NULL, IDENTITY, ARRAY_ELEMENT, TUPLE_COMPONENT, UNION_MEMBER, OFFSET
	}

	/**
	 * 
	 * @return an item within the ReferenceKind enumeration
	 */
	ReferenceKind referenceKind();

	/** Is this the null reference? */
	boolean isNullReference();

	/** Is this the identity reference? */
	boolean isIdentityReference();

	/** Is this an array element reference? */
	boolean isArrayElementReference();

	/** Is this a tuple component reference? */
	boolean isTupleComponentReference();

	/** Is this a union member reference? */
	boolean isUnionMemberReference();

	/** Is this an "offset reference" ? */
	boolean isOffsetReference();

}
