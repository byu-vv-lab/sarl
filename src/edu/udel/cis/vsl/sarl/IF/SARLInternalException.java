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
