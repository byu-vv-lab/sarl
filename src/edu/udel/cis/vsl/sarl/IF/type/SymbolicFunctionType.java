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
 * A function type is specified by the number and types of inputs, and a single
 * output type. It represents an abstract (mathematical) function from the
 * Cartesian product of the input sets to the output set.
 * 
 * @author siegel
 * 
 */
public interface SymbolicFunctionType extends SymbolicType {

	/**
	 * The ordered sequence of input types. These can be any number (including
	 * 0) of non-null types.
	 * 
	 * @return the inputs type sequence
	 */
	SymbolicTypeSequence inputTypes();

	/**
	 * The output type, a non-null symbolic type.
	 * 
	 * @return the output type
	 */
	SymbolicType outputType();

}
