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
package edu.udel.cis.vsl.sarl.IF.prove;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

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
 * @author siegel
 * 
 */
public class ValidityResult {

	public enum ResultType {
		YES, NO, MAYBE
	};

	private ResultType resultType;

	private Map<SymbolicConstant, SymbolicExpression> model;

	/** Constructs new ValidityResult with given resultType and model */
	public ValidityResult(ResultType resultType,
			Map<SymbolicConstant, SymbolicExpression> model) {
		this.resultType = resultType;
		this.model = model;
	}

	/** Constructs new ValidityResult with give resultType and null model */
	public ValidityResult(ResultType resultType) {
		this(resultType, null);
	}

	public ResultType getResultType() {
		return resultType;
	}

	public Map<SymbolicConstant, SymbolicExpression> getModel() {
		return model;
	}

}
