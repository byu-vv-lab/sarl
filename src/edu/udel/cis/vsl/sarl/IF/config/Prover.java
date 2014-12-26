package edu.udel.cis.vsl.sarl.IF.config;

import java.io.File;
import java.util.Set;

/**
 * Abstract interface representing information about an external theorem prover:
 * what kind of prover it is, where it can be found, etc.
 * 
 * @author siegel
 *
 */
public interface Prover {

	// TODO: add time out (seconds) to the config field
	// Absolute path (beginning with /) or API.

	public static enum ProverKind {
		CVC3, CVC3_API, CVC4, CVC4_API, Z3, Z3_API
	};

	/**
	 * Returns the set of aliases associated to this theorem prover. The set
	 * should not be modified.
	 * 
	 * @return the set of aliases associated to this theorem prover
	 */
	Set<String> getAliases();

	/**
	 * The kind of theorem prover this is. This is a classification by kind of
	 * input language the prover expects.
	 * 
	 * @return kind of this prover
	 */
	ProverKind getKind();

	/**
	 * The path to the executable theorem prover.
	 * 
	 * @return path to executable
	 */
	File getPath();

	/**
	 * The version, e.g. "1.4".
	 * 
	 * @return version string
	 */
	String getVersion();

}
