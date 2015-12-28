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
package edu.udel.cis.vsl.sarl.simplify.IF;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;

/**
 * A factory for producing instances of {@link Simplifier}. Each instance of
 * {@link Simplifier} is formed around a single "assumption", a boolean-valued
 * symbolic expression which forms the context under which all simplifications
 * take place. (In symbolic execution, the assumption is typically the path
 * condition.)
 * 
 * @author Stephen F. Siegel
 */
public interface SimplifierFactory {

	/**
	 * Returns a new instance of {@link Simplifier} formed from the specified
	 * assumption
	 * 
	 * @param assumption
	 *            a non-<code>null</code> boolean symbolic expression
	 * @return a new simplifier with the specified assumption
	 */
	Simplifier newSimplifier(BooleanExpression assumption);
}
