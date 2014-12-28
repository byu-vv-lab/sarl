package edu.udel.cis.vsl.sarl.IF.config;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Abstract interface representing information about an external theorem prover:
 * what kind of prover it is, where it can be found, etc.
 * 
 * @author siegel
 *
 */
public interface Prover {

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
	 * Adds the given string to the set of aliases.
	 * 
	 * @param value
	 *            the string to add to the set of aliases
	 * @return <code>true</code> if the set of aliases did not already contain
	 *         the given value
	 */
	boolean addAlias(String value);

	/**
	 * Returns the sequence of command line options that should be used when
	 * invoking this theorem prover.
	 * 
	 * @return list of command line options
	 */
	List<String> getOptions();

	/**
	 * Adds the given string to the end of the list of options.
	 * 
	 * @param value
	 *            the string to add
	 */
	void addOption(String value);

	/**
	 * Returns time limit in seconds or -1 if the time is unlimited.
	 * 
	 * @return time limit in seconds
	 */
	double getTimeout();

	/**
	 * Sets the timeout, i.e., the time limit in seconds.
	 * 
	 * @param value
	 *            the time limit in seconds or -1 if the time is unlimited
	 */
	void setTimeout(double value);

	/**
	 * The kind of theorem prover this is. This is a classification by kind of
	 * input language the prover expects.
	 * 
	 * @return kind of this prover
	 */
	ProverKind getKind();

	/**
	 * Sets the prover kind.
	 * 
	 * @param value
	 *            the prover kind
	 */
	void setKind(ProverKind value);

	/**
	 * The path to the executable theorem prover. This should be an absolute
	 * path to an executable file.
	 * 
	 * @return path to executable
	 */
	File getPath();

	/**
	 * Sets the path to the executable theorem prover. This should be an
	 * absolute path to an executable file.
	 * 
	 * @param value
	 */
	void setPath(File value);

	/**
	 * The version, e.g. "1.4".
	 * 
	 * @return version string
	 */
	String getVersion();

	/**
	 * Sets the version string.
	 * 
	 * @param value
	 *            the version string
	 */
	void setVersion(String value);

	/**
	 * Should the prover print the queries.
	 * 
	 * @return <code>true</code> if this prover should print queries
	 */
	boolean getShowQueries();

	/**
	 * Tell this prover whether it should print the queries.
	 * 
	 * @param value
	 *            <code>true</code> if this prover should print queries
	 */
	void setShowQueries(boolean value);

}
