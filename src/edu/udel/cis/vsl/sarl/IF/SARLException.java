package edu.udel.cis.vsl.sarl.IF;

/**
 * Root of the SARL exception type hierarchy. A SARLException is thrown whenever
 * anything unexpected happens within SARL, whether due to user input or an
 * internal error.
 * 
 * Since this extends RuntimeException, SARL exception do not have to be
 * explicitly declared in "throws" and "catch" clauses.
 * 
 * @author siegel
 * 
 */
public class SARLException extends RuntimeException {

	/**
	 * Eclipse made me do it.
	 */
	private static final long serialVersionUID = 6164113938850258529L;

	public SARLException(String message) {
		super(message);
	}

}
