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
package edu.udel.cis.vsl.sarl.IF;

/**
 * A Transform from type S to type T is an object that provides a method "apply"
 * which takes an element of S and returns an element of T.
 * 
 * @author siegel
 * 
 * @param <S>
 *            any Java type
 * @param <T>
 *            any Java type
 */
public interface Transform<S, T> {

	/**
	 * Apply this Transform to the element x of S.
	 * 
	 * @param x
	 *            an element of S
	 * @return the result of applying this Transform to x
	 * */
	T apply(S x);

}
