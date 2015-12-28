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
package edu.udel.cis.vsl.sarl.util;

/**
 * A binary operator on a type T is an object which provides a method "apply"
 * which takes two elements of T and returns an element of T.
 * 
 * @author siegel
 * 
 */
public interface BinaryOperator<T> {

	/**
	 * Apply this binary operator to the given two elements of T.
	 * 
	 * @param x
	 *            an element of T
	 * @param y
	 *            an element of T
	 * @return the result of applying this binary operator to arg0 and arg1
	 */
	T apply(T x, T y);

}
