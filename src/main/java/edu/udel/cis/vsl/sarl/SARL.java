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

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
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

	/**
	 * Returns a new standard symbolic universe, which supports all symbolic
	 * types, including Herbrand integer and real types, and ideal
	 * (mathematical) integers and reals.
	 * 
	 * @param config
	 *            a SARL configuration object providing information on the
	 *            external theorem provers available to SARL
	 * @param prover
	 *            either one of the {@link ProverInfo} objects in the
	 *            configuration, or <code>null</code>. If non-null, this
	 *            specifies the sole prover to use for resolving queries. If
	 *            null, a query will be resolved by starting with the first
	 *            prover in the configuration, and, if that result is
	 *            inconclusive, going to the second, and so on, until either a
	 *            conclusive result is achieved or every prover in the
	 *            configuration has been exhausted.
	 * 
	 * @return a new standard symbolic universe
	 */
	public static SymbolicUniverse newStandardUniverse(SARLConfig config,
			ProverInfo prover) {
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
	 * @param prover
	 *            either one of the {@link ProverInfo} objects in the
	 *            configuration, or <code>null</code>. If non-null, this
	 *            specifies the sole prover to use for resolving queries. If
	 *            null, a query will be resolved by starting with the first
	 *            prover in the configuration, and, if that result is
	 *            inconclusive, going to the second, and so on, until either a
	 *            conclusive result is achieved or every prover in the
	 *            configuration has been exhausted.
	 * @return an ideal symbolic universe
	 */
	public static SymbolicUniverse newIdealUniverse(SARLConfig config,
			ProverInfo prover) {
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
	 * file. See {@link Configurations#findOrMakeConfiguration()} for details on
	 * how the configuration file is found.
	 * </p>
	 * 
	 * <p>
	 * A query will be resolved by starting with the first prover in the
	 * configuration, and, if that result is inconclusive, going to the second,
	 * and so on, until either a conclusive result is achieved or every prover
	 * in the configuration has been exhausted.
	 * </p>
	 * 
	 * @return a new standard symbolic universe
	 */
	public static SymbolicUniverse newStandardUniverse() {
		SARLConfig config = Configurations.getDefaultConfiguration();

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
	 * file. See {@link Configurations#findOrMakeConfiguration()} for details on
	 * how the configuration file is found.
	 * </p>
	 * 
	 * <p>
	 * A query will be resolved by starting with the first prover in the
	 * configuration, and, if that result is inconclusive, going to the second,
	 * and so on, until either a conclusive result is achieved or every prover
	 * in the configuration has been exhausted.
	 * </p>
	 *
	 * @return an ideal symbolic universe
	 */
	public static SymbolicUniverse newIdealUniverse() {
		SARLConfig config = Configurations.getDefaultConfiguration();

		return Universes.newIdealUniverse(config, null);
	}
}
