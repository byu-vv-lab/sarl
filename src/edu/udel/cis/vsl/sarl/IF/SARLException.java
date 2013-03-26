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
