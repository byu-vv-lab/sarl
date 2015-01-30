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
package edu.udel.cis.vsl.sarl.expr.cnf;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * A representation of a boolean expression using Conjunctive Normal Form.
 * 
 * Some possible things that could be cached here: 1. The negation of this
 * expression. 2. Is this satisfiable? Is this valid? 3. The most simplified
 * version of this expression.
 * 
 * @author siegel
 *
 */
public class CnfExpression extends CommonSymbolicExpression implements
		BooleanExpression {

	/**
	 * The negation of this boolean expression. Cached here for performance.
	 */
	private CnfExpression negation = null;

	/**
	 * Is this boolean expression "valid", i.e., equivalent to true, i.e., a
	 * tautology? Result is cached here for convenience. There are four possible
	 * values: (1) null: nothing is known and nothing has been tried to figure
	 * it out, (2) YES: it is definitely valid, (3) NO: it is definitely not
	 * valid, and (4) MAYBE: unknown. The difference between null and MAYBE is
	 * that with MAYBE you know we already tried to figure out if it is valid
	 * and couldn't, hence, there is no need to try again.
	 */
	private ResultType validity = null;

	/**
	 * One of several constructors that build a CnfExpression.
	 * 
	 * @param kind
	 * @param type
	 * @param args
	 *            args is a Collection of Symbolic Objects
	 */
	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			Collection<SymbolicObject> args) {
		super(kind, type, args);
		assert type.isBoolean();
	}

	/**
	 * One of several constructors that build a CnfExpression.
	 * 
	 * @param kind
	 * @param type
	 * @param args
	 *            args is an array of Symbolic Objects
	 */
	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject[] args) {
		super(kind, type, args);
		assert type.isBoolean();
	}

	/**
	 * One of several constructors that build a CnfExpression.
	 * 
	 * @param kind
	 * @param type
	 * @param arg
	 *            arg is a Symbolic Object
	 */
	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg) {
		super(kind, type, arg);
		assert type.isBoolean();
	}

	/**
	 * One of several constructors that build a CnfExpression.
	 * 
	 * @param kind
	 * @param type
	 * @param arg0
	 *            arg0 is a Symbolic Objects
	 * @param arg1
	 *            arg1 is a Symbolic Objects
	 */
	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(kind, type, arg0, arg1);
		assert type.isBoolean();
	}

	/**
	 * One of several constructors that build a CnfExpression.
	 * 
	 * @param kind
	 * @param type
	 * @param arg0
	 *            arg0 is a Symbolic Objects
	 * @param arg1
	 *            arg1 is a Symbolic Objects
	 * @param arg1
	 *            arg2 is a Symbolic Objects
	 */
	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(kind, type, arg0, arg1, arg2);
		assert type.isBoolean();
	}

	protected CnfExpression getNegation() {
		return negation;
	}

	protected void setNegation(CnfExpression value) {
		this.negation = value;
	}

	@Override
	public ResultType getValidity() {
		return validity;
	}

	@Override
	public void setValidity(ResultType value) {
		this.validity = value;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		super.canonizeChildren(factory);

		if (negation != null)
			negation = factory.canonic(negation);
	}

	/**
	 * Getter method that returns the BooleanExpression at argument i.
	 * 
	 * @param i
	 * 
	 * @return BooleanExpression
	 */
	@Override
	public BooleanExpression booleanArg(int i) {
		return (BooleanExpression) argument(i);
	}

	/**
	 * Getter method that returns the BooleanExpression at argument i when
	 * arguments is a collect.
	 * 
	 * @param i
	 * 
	 * @return BooleanExpression
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SymbolicCollection<BooleanExpression> booleanCollectionArg(int i) {
		return (SymbolicCollection<BooleanExpression>) argument(i);
	}

	/**
	 * Returns the i-th argument of this expression in the case where the i-th
	 * argument should be an instance of SymbolicSet<BooleanExpression>. A
	 * SARLException is thrown if that argument is not an instance of that type,
	 * or if i is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	@SuppressWarnings("unchecked")
	public SymbolicSet<BooleanExpression> booleanSetArg(int i) {
		return (SymbolicSet<BooleanExpression>) argument(i);
	}
}
