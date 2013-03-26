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
package edu.udel.cis.vsl.sarl;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.universe.Universes;

/**
 * The SARL class provides static methods for creating new symbolic universes.
 * (A symbolic universe provides methods for creating, manipulating, and
 * reasoning about symbolic expressions that belong to that universe.)
 * 
 * A typical user applications will call one of these static methods once, near
 * the beginning of the execution, and use the universe returned for all
 * symbolic expression operations.
 * 
 * @author siegel
 * 
 */
public class SARL {

	/**
	 * Returns a new standard symbolic universe, which supports all symbolic
	 * types, including herbrand integer and real types, and ideal
	 * (mathematical) integers and reals.
	 * 
	 * @return a new standard symbolic universe
	 */
	public static SymbolicUniverse newStandardUniverse() {
		return Universes.newStandardUniverse();
	}

	/**
	 * Returns a symbolic universe that only deals with ideal (mathematical)
	 * integers and reals. There might be slight performance advantages over the
	 * standard universe (if no non-ideal expressions are used).
	 * 
	 * @return an ideal symbolic universe
	 */
	public static SymbolicUniverse newIdealUniverse() {
		return Universes.newIdealUniverse();
	}

}
