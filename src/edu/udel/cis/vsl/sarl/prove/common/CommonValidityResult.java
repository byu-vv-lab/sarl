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
package edu.udel.cis.vsl.sarl.prove.common;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;

/**
 * Straightforward implementation of {@link ValidityResult}.
 * 
 * @author Stephen F. Siegel
 */
public class CommonValidityResult implements ValidityResult {

	/** The type of this validity result */
	private ResultType resultType;

	/**
	 * Constructs new ValidityResult with give resultType.
	 * 
	 * @param resultType
	 *            the result type
	 */
	public CommonValidityResult(ResultType resultType) {
		this.resultType = resultType;
	}

	@Override
	public ResultType getResultType() {
		return resultType;
	}

	@Override
	public String toString() {
		return resultType.toString();
	}

}
