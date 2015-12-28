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
 * 	SymbolicIntegerType is an interface that contains representation for a Symbolic Integer Type:
 * 	It has three kinds: 
 * 		- herbrand: it doesn't do simplifications, when used in equations
 * 		- ideal:
 * 		- bounded:
 * 
 * 	Also it has a method to return the kind of the integer.
 * 
 * 	@author alali
 *
 */
public interface SymbolicIntegerType extends SymbolicType {

	/**
	 * three kinds for the integer type
	 * 
	 * @author alali
	 */
	public enum IntegerKind {
		HERBRAND, IDEAL, BOUNDED
	}

	/**
	 * a method to return the kind of the integer type.
	 * @return IntegerKind: either herbrand, ideal, or bounded.
	 */
	IntegerKind integerKind();

}
