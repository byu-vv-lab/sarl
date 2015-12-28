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
package edu.udel.cis.vsl.sarl.IF.type;


/**
 * An array type T[]. The elements of this type are arrays of T. A subtype is
 * the complete array type, in which the extents are also specified.
 * 
 * @author siegel
 * 
 */
public interface SymbolicArrayType extends SymbolicType {

	/**
	 * The type of each element of the array, a non-null symbolic type.
	 * 
	 * @return the element type
	 */
	SymbolicType elementType();

	/**
	 * Is this a complete array type, i.e., is the extent specified? If true,
	 * this can be safely cast to SymbolicCompleteArrayType.
	 * 
	 * @return true iff this array type is complete
	 */
	boolean isComplete();

}
