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
 * A ValidityResult represents the result of a validity query.
 * 
 * It defines an enumerated Type "ResultType" with three possible values: YES,
 * NO, MAYBE. This is used to represent the result returned by an automated
 * theorem prover.
 * 
 * In the case that the result type is NO, a ValidityResult may also provide a
 * model, i.e., an assignment of concrete values to symbolic constants that
 * leads the assumption to evaluate to true and the predicate to evaluate to
 * false.
 * 
 * The subclass {@link CommonModelResult} provides a method to get a model
 * (counterexample). A ModelResult always has result type NO, because only
 * invalid queries can have models. However, the model returned may be null:
 * this indicates that the attempt to find a model failed for some reason.
 * 
 * An instance of ValidityResult of type "NO" that is not an instance of
 * ModelResult indicates that there was no attempt to find a model.
 * 
 * @author siegel
 * 
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

	/**
	 * Returns the result type of this result.
	 * 
	 * @return the result type
	 */
	@Override
	public ResultType getResultType() {
		return resultType;
	}

}
