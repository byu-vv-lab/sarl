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
package edu.udel.cis.vsl.sarl.reason.IF;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;

/**
 * <p>
 * A factory for producing instances of {@link Reasoner}. Each
 * {@link BooleanExpression} has an associated {@link Reasoner}. The boolean
 * expression is called the <code>context</code> for that reasoner. The context
 * is the fixed, underlying assumption used by that reasoner whenever it is
 * called to check validity or to simplify a formula.
 * </p>
 * 
 * <p>
 * A {@link ReasonerFactory} may cache the {@link Reasoner}s it produces, so
 * that if called twice on the same (or just "equal") boolean expression, the
 * same instance of {@link Reasoner} will be returned.
 * </p>
 * 
 * @author Stephen F. Siegel
 *
 */
public interface ReasonerFactory {

	/**
	 * Gets a {@link Reasoner} for the given <code>context</code>. If this
	 * method is called twice with two contexts that are equal (according to
	 * method {@link BooleanExpression#equals(Object)}), the second call may
	 * return the same instance as the first call (i.e., the factory may cache
	 * the results).
	 * 
	 * @param context
	 *            a non-<code>null</code> boolean expression to be used as the
	 *            context for the {@link Reasoner}
	 * @return a {@link Reasoner} based on the given <code>context</code>
	 */
	Reasoner getReasoner(BooleanExpression context);

}
