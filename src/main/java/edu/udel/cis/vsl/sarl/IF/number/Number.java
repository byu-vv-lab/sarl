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
package edu.udel.cis.vsl.sarl.IF.number;

/**
 * A number is some kind of representation of a real number.
 * 
 * Integer numbers and rational numbers live in two different universes, so are
 * never equal.
 * 
 * The natural comparison places some arbitrary order on Numbers consistent with
 * equals.
 * */
public interface Number extends Comparable<Number> {

	/**
	 * Returns 0 if this number equals 0, -1 if this number is negative, 1 if
	 * this number is positive.
	 */
	int signum();

	/**
	 * Is this number equal to 0? Same as signum() == 0, but sometimes more
	 * convenient.
	 */
	boolean isZero();

	/** Is this number equal to 1? */
	boolean isOne();

	/**
	 * The string representation used when this number appears inside of another
	 * expression. For example if the number is 3/4, this method could return
	 * (3/4), because in the expression 10*(3/4) you really need the
	 * parentheses. On the other hand, if the representation is 0.75, the parens
	 * probably are not necessary, and this method might return the same thing
	 * as the toString() method.
	 */
	String atomString();

}
