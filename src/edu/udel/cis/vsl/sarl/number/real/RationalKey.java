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
package edu.udel.cis.vsl.sarl.number.real;

import java.math.BigInteger;

/**
 * A key used in the rational map. This is needed in order to implement the
 * flyweight pattern on RealRational.
 */
public class RationalKey {

	BigInteger numerator;

	BigInteger denominator;

	/**
	 * Constructs a RationalKey from BigInteger numerator and BigInteger denominator.
	 * 
	 * @param numerator
	 * @param denominator
	 */
	RationalKey(BigInteger numerator, BigInteger denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * Checks if two RationalKeys are equivalent.
	 * Fails if supplied object is not a RationalKey.
	 */
	public boolean equals(Object object) {
		if (object instanceof RationalKey) {
			RationalKey that = (RationalKey) object;

			return numerator.equals(that.numerator)
					&& denominator.equals(that.denominator);
		}
		return false;
	}

	/**
	 * returns a composite hashcode of the sum of the numerator and denominaor's hashcodes.
	 */
	public int hashCode() {
		return numerator.hashCode() + denominator.hashCode();
	}
}
