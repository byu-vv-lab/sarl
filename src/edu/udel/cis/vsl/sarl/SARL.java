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
package edu.udel.cis.vsl.sarl;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.Prover;
import edu.udel.cis.vsl.sarl.IF.config.SARLConfig;
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

	// would like option to specify either a specific prover or
	// an entire config

	// prover
	// config
	// nothing

	/**
	 * Returns a new standard symbolic universe, which supports all symbolic
	 * types, including Herbrand integer and real types, and ideal
	 * (mathematical) integers and reals.
	 * 
	 * @param config
	 *            a SARL configuration object providing information on the
	 *            external theorem provers available to SARL
	 * 
	 * @return a new standard symbolic universe
	 */
	public static SymbolicUniverse newStandardUniverse(SARLConfig config,
			Prover prover) {
		return Universes.newStandardUniverse(config, prover);
	}

	/**
	 * Returns a symbolic universe that only deals with ideal (mathematical)
	 * integers and reals. There might be slight performance advantages over the
	 * standard universe (if no non-ideal expressions are used).
	 * 
	 * @param config
	 *            a SARL configuration object providing information on the
	 *            external theorem provers available to SARL
	 * @return an ideal symbolic universe
	 */
	public static SymbolicUniverse newIdealUniverse(SARLConfig config,
			Prover prover) {
		return Universes.newIdealUniverse(config, prover);
	}

	/**
	 * <p>
	 * Returns a new standard symbolic universe, which supports all symbolic
	 * types, including Herbrand integer and real types, and ideal
	 * (mathematical) integers and reals.
	 * </p>
	 * 
	 * <p>
	 * The SARL configuration is determined by looking for a SARL configuration
	 * file. First, looks in the current working directory for a file named
	 * <code>.sarl</code>, then in that directory for a file named
	 * <code>.sarl_default</code>, then repeats in user's home directory.
	 * </p>
	 * 
	 * @return a new standard symbolic universe
	 */
	public static SymbolicUniverse newStandardUniverse() {
		SARLConfig config = Configurations.findConfiguration();

		if (config == null)
			throw new SARLException("Could not find SARL configuration file");
		return Universes.newStandardUniverse(config, null);

	}

	/**
	 * <p>
	 * Returns a symbolic universe that only deals with ideal (mathematical)
	 * integers and reals. There might be slight performance advantages over the
	 * standard universe (if no non-ideal expressions are used).
	 * </p>
	 * 
	 * <p>
	 * The SARL configuration is determined by looking for a SARL configuration
	 * file. First, looks in the current working directory for a file named
	 * <code>.sarl</code>, then in that directory for a file named
	 * <code>.sarl_default</code>, then repeats in user's home directory.
	 * </p>
	 *
	 * @return an ideal symbolic universe
	 */
	public static SymbolicUniverse newIdealUniverse() {
		SARLConfig config = Configurations.findConfiguration();

		if (config == null)
			throw new SARLException("Could not find SARL configuration file");
		return Universes.newIdealUniverse(config, null);
	}
}
