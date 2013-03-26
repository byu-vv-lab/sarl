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

import edu.udel.cis.vsl.sarl.IF.object.StringObject;

/**
 * A union type of a sequence of types t_i. The elements of the union type have
 * the form inject_i(x), where x is an element of type t_i.
 * 
 * @author siegel
 * 
 */
public interface SymbolicUnionType extends SymbolicType {

	/**
	 * The sequence of types comprising this union. This sequence must not
	 * contain duplicates.
	 * 
	 * @return the sequence of types comprising the union
	 */
	SymbolicTypeSequence sequence();

	/**
	 * The name of this union type.
	 * 
	 * @return name of this union type
	 */
	StringObject name();

	/**
	 * If this type exists in the sequence of types comprising this union, this
	 * method will find the index of that type and return it; otherwise it
	 * returns null.
	 * 
	 * @param type
	 *            a symbolic type
	 * @return index of this type in the type sequence or null
	 */
	Integer indexOfType(SymbolicType type);

}
