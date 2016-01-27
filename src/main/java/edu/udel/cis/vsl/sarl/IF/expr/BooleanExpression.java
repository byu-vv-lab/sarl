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
package edu.udel.cis.vsl.sarl.IF.expr;

import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;

/**
 * A symbolic expression of boolean type.
 * 
 * @author siegel
 * 
 */
public interface BooleanExpression extends SymbolicExpression {

	/**
	 * Returns the i-th argument of this expression in the case where the i-th
	 * argument should be an instance of BooleanExpression. A SARLException is
	 * thrown if that argument is not an instance of BooleanExpression, or if i
	 * is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	BooleanExpression booleanArg(int i);

	/**
	 * Returns the i-th argument of this expression in the case where the i-th
	 * argument should be an instance of
	 * <code>Iterable&lt;? extends BooleanExpression&gt;</code>. A SARLException
	 * is thrown if that argument is not an instance of
	 * <code>Iterable&lt;? extends
	 * BooleanExpression&gt;</code>, or if i is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	Iterable<? extends BooleanExpression> booleanCollectionArg(int i);

	/**
	 * Is this boolean expression valid, i.e., equivalent to true, i.e., a
	 * tautology? The result is cached here for convenience using method
	 * {@link #setValidity(edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType)}. There are four possible values: (1)
	 * <code>null</code>: nothing is known and nothing has been tried to figure
	 * it out, (2) YES: it is definitely valid, (3) NO: it is definitely not
	 * valid, and (4) MAYBE: unknown. The difference between <code>null</code>
	 * and MAYBE is that with MAYBE you know we already tried to figure out if
	 * it is valid and couldn't, hence, there is no need to try again.
	 * 
	 * @see #setValidity(edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType)
	 * @return the cached validity result
	 */
	ResultType getValidity();

	/**
	 * Store the validity result for this boolean expression.
	 * 
	 * @see #getValidity()
	 * @param value
	 *            the validity result to cache
	 */
	void setValidity(ResultType value);

}
