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
package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * A symbolic type represents the type of a symbolic expression.
 * 
 * Every symbolic type has a "kind", given by the enumerated type
 * {@link SymbolicTypeKind}.
 * 
 * If the kind is ARRAY, the object can be cast to {@link SymbolicArrayType}.
 * 
 * If the kind is FUNCTION, the object can be cast to
 * {@link SymbolicFunctionType}.
 * 
 * If the kind is TUPLE, the object can be cast to {@link SymbolicTupleType}.
 * 
 * If the kind is UNION, the object can be cast to {@link SymbolicUnionType}.
 * 
 * @author siegel
 * 
 */
public interface SymbolicType extends SymbolicObject {

	/**
	 * The different kinds of types.
	 */
	public enum SymbolicTypeKind {
		ARRAY, BOOLEAN, CHAR, FUNCTION, INTEGER, MAP, REAL, SET, TUPLE, UNION
	};

	/**
	 * Tells whether the type is the boolean type.
	 * 
	 * @return true if kind is BOOLEAN, else false
	 */
	boolean isBoolean();

	/**
	 * Tells whether the type is an integer type.
	 * 
	 * @return true if kind is INTEGER, else false
	 */
	boolean isInteger();

	/**
	 * Tells whether the type is a real or integer type
	 * 
	 * @return true if kind is REAL or INTEGER, false otherwise
	 */
	boolean isNumeric();

	/**
	 * Tells whether the type is a real type.
	 * 
	 * @return true if kind is REAL, else false
	 */
	boolean isReal();

	/**
	 * Returns the kind of the type.
	 */
	SymbolicTypeKind typeKind();

	/**
	 * Is this a Herbrand type? There are Herbrand variants of real and integer
	 * types. Operations on Herbrand expressions are treated as uninterpreted
	 * functions: no simplifications or transformations of any kind are
	 * performed.
	 * 
	 * @return true iff this type is a Herbrand type
	 */
	boolean isHerbrand();

	/**
	 * Is this an Ideal numeric type? These are the mathematical integer and
	 * real types.
	 * 
	 * @return true iff this is the ideal real type or ideal integer type
	 */
	boolean isIdeal();

}
