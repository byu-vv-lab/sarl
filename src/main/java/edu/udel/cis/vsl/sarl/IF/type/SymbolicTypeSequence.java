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

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

/**
 * A finite, ordered sequence of SymbolicType.
 * 
 * @author siegel
 */
public interface SymbolicTypeSequence extends Iterable<SymbolicType>,
		SymbolicObject {

	/**
	 * Returns the number of types in this sequence.
	 * 
	 * @return the number of types
	 */
	int numTypes();

	/**
	 * Returns the index-th type in this sequence.
	 * 
	 * @param index
	 *            an integer in range [0,n-1], where n is the number of types in
	 *            this sequence
	 * @return the index-th type
	 */
	SymbolicType getType(int index);

}