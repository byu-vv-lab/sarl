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
 * An instance of Interval represents a numeric interval. It can have either
 * real or integer type. It can be open or closed on the left, open or closed on
 * right. It can be unbounded on either side.
 */
public interface Interval {

	/**
	 * Does this interval have real type? If so, then both the upper and lower
	 * bounds will be real.
	 * 
	 * @return true iff the type is real
	 */
	boolean isReal();

	/**
	 * Does this interval have integer type? If so, then both the upper and
	 * lower bounds will have integer type.
	 * 
	 * @return true iff the type is integer
	 */
	boolean isIntegral();

	/**
	 * The lower bound of this interval. If unbounded (i.e., negative infinity)
	 * on the left, this method returns null.
	 * 
	 * @return the lower bound or null
	 */
	Number lower();

	/**
	 * The upper bound of this interval. If unbounded (i.e., positive infinity)
	 * on the right, this method returns null.
	 * 
	 * @return the upper bound or null
	 */
	Number upper();

	/**
	 * Is the lower bound strict, i.e., does the interval consist of all x
	 * strictly greater than the lower bound and ...?
	 * 
	 * @return true iff the lower bound is strict
	 */
	boolean strictLower();

	/**
	 * Is the upper bound strict, i.e., does the interval consist of all x
	 * strictly less than the upper bound and ...?
	 * 
	 * @return true iff the upper bound is strict
	 */
	boolean strictUpper();

}
