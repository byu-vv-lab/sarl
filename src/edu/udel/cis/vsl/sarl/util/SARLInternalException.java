package edu.udel.cis.vsl.sarl.util;

/**
 * A SARL internal exception represents an error that is
 * "not supposed to happen." It can be used like an assertion, whenever you feel
 * that something should always be true. It is a runtime exception, so there is
 * no need to declare or catch it. It will be thrown all the way up to main and
 * reported.
 */
public class SARLInternalException extends RuntimeException {

	/**
	 * The generated serial ID to implement Serializaeable.
	 */
	private static final long serialVersionUID = 6522079858283496490L;

	public SARLInternalException(String s) {
		super("A SARL internal error has occurred.\n"
				+ "Please send an error report to siegel@udel.edu.\n" + s);
	}

}
