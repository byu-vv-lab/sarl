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
package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.IF.object.IntObject;

/**
 * A power of a Primitive expression, x^i, where x is a Primitive and i is a
 * concrete nonnegative int.
 * 
 * @author siegel
 * 
 */
public interface PrimitivePower extends Monic {

	/**
	 * Creates a numeric primitive expression using an idealFactory
	 * 
	 * @param factory - the ideal factory owning this polynomial
	 * 
	 * @return
	 * 			a numeric primitive expression
	 */
	Primitive primitive(IdealFactory factory);

	/**
	 * The exponent part of a primitive power which is an integer is returned.
	 * 
	 * @param factory - the ideal factory owning this polynomial
	 * 
	 * @return
	 * 			the integer exponent part of a PrimitivePower
	 */
	IntObject primitivePowerExponent(IdealFactory factory);

}
