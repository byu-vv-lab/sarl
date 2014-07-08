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

/**
 * T map type is a type consisting of two components: a key type K and a value
 * type V. A map value consists of a set of ordered pair of the form (k,v),
 * where k is in K and v is in V, and such that for any k0, there is at most one
 * v such that (k0,v) is in the set. It is very similar to the function type
 * from K to V, but the domain of the map does not have to be all of K.
 * 
 * @author siegel
 * 
 */
public interface SymbolicMapType extends SymbolicType {

	/**
	 * Returns the type of the domain (keys).
	 * 
	 * @return the key type
	 */
	SymbolicType keyType();

	/**
	 * Returns the type of the range (values).
	 * 
	 * @return the value type
	 */
	SymbolicType valueType();

}
