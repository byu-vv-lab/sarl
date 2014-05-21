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
package edu.udel.cis.vsl.sarl.IF.number;

import java.math.BigInteger;

/** An instance of this class represents an integer number. */
public interface IntegerNumber extends Number {

	/**
	 * Attempts to extract a Java int value from this IntegerNumber. The answer
	 * could be wrong if the integer value is outside of the range of the Java
	 * int type.
	 */
	int intValue();

	/**
	 * Returns this integer as an instance of Java's {@link BigInteger}.
	 * 
	 * @return instance of {@link BigInteger} equivalent to this integer
	 */
	BigInteger bigIntegerValue();
}
