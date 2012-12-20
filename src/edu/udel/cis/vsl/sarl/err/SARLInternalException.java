package edu.udel.cis.vsl.sarl.err;

public class SARLInternalException extends RuntimeException {

	/**
	 * Eclipse made me do it.
	 */
	private static final long serialVersionUID = -2296472993062192755L;

	public SARLInternalException(String msg) {
		super(msg);
	}

	@Override
	public String toString() {
		return "SARL internal error: " + super.toString();
	}
}
