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
package edu.udel.cis.vsl.sarl.universe.common;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonPreUniverse;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;

/**
 * A standard implementation of SymbolicUniverse, relying heavily on a given
 * NumericExpressionFactory for dealing with numeric issues and a
 * BooleanExpressionFactory for dealing with boolean expressions.
 * 
 * @author siegel
 */
public class CommonSymbolicUniverse extends CommonPreUniverse implements
		SymbolicUniverse {

	/**
	 * The factory for producing new Reasoner instances.
	 */
	private ReasonerFactory reasonerFactory;

	/**
	 * A map from boolean valued symbolic expressions to simplifiers. The
	 * simplifer corresponding to a key "assumption" will be the simplifier
	 * formed from the assumption. The simplifier stores all kinds of data
	 * obtained by analyzing the assumptions, caches all simplifications made
	 * under that assumption, and so on. The assumption is typically the
	 * "path condition" of symbolic execution.
	 */
	private Map<BooleanExpression, Reasoner> reasonerMap = new HashMap<BooleanExpression, Reasoner>();

	// Constructor...

	/**
	 * Constructs a new CommonSymbolicUniverse from the given system of
	 * factories.
	 * 
	 * @param system
	 *            a factory system
	 */
	public CommonSymbolicUniverse(FactorySystem system) {
		super(system);
	}

	// Helper methods...

	@Override
	public Reasoner reasoner(BooleanExpression context) {
		Reasoner result = reasonerMap.get(context);

		if (result == null) {
			BooleanExpression canonicContext = (BooleanExpression) canonic(context);

			result = reasonerFactory.newReasoner(canonicContext);
			reasonerMap.put(canonicContext, result);
		}
		return result;
	}

	public void setReasonerFactory(ReasonerFactory reasonerFactory) {
		this.reasonerFactory = reasonerFactory;
	}

}
