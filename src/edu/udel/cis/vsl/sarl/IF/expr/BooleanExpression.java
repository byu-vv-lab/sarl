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
package edu.udel.cis.vsl.sarl.IF.expr;

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
	 * argument should be an instance of Iterable<? extends BooleanExpression>.
	 * A SARLException is thrown if that argument is not an instance of
	 * Iterable<? extends BooleanExpression>, or if i is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	Iterable<? extends BooleanExpression> booleanCollectionArg(int i);
}
