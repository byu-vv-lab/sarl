package edu.udel.cis.vsl.sarl.IF;

/**
 * A SARL internal exception represents an error that is
 * "not supposed to happen." It can be used like an assertion, whenever you feel
 * that something should always be true. It is a runtime exception, so there is
 * no need to declare or catch it. It will be thrown all the way up to main and
 * reported.
 * */
public class SARLInternalException extends SARLException {

	/**
	 * Eclipse made me do it.
	 */
	private static final long serialVersionUID = -2296472993062192755L;

	/**
	 * Constructs a new instance whose message is formed by pre-pending to the
	 * given msg string stating that this is an internal error and the error
	 * should be reported to the developers.
	 * 
	 * @param msg
	 *            a message describing what went wrong
	 */
	public SARLInternalException(String msg) {
		super("A SARL internal error has occurred.\n"
				+ "Please send an error report to siegel@udel.edu.\n" + msg);
	}

}
