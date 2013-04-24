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
package edu.udel.cis.vsl.sarl.expr.cnf;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * A CNF factory is an implementation of BooleanExpressionFactory that works by
 * putting all boolean expressions into a conjunctive normal form.
 * 
 * @author siegel
 * 
 */
public class CnfFactory implements BooleanExpressionFactory {

	private CollectionFactory collectionFactory;

	private SymbolicType _booleanType;

	private BooleanExpression trueExpr, falseExpr;

	public CnfFactory(SymbolicTypeFactory typeFactory,
			ObjectFactory objectFactory, CollectionFactory collectionFactory) {
		this.collectionFactory = collectionFactory;
		_booleanType = typeFactory.booleanType();
		trueExpr = objectFactory.canonic(booleanExpression(
				SymbolicOperator.CONCRETE, objectFactory.trueObj()));
		falseExpr = objectFactory.canonic(booleanExpression(
				SymbolicOperator.CONCRETE, objectFactory.falseObj()));
	}

	// Helpers...

	private SymbolicSet<SymbolicExpression> hashSet(SymbolicExpression x,
			SymbolicExpression y) {
		return collectionFactory.singletonHashSet(x).add(y);
	}

	// Public functions specified in BooleanExpressionFactory...

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			Collection<SymbolicObject> args) {
		return new CnfExpression(operator, _booleanType, args);
	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject[] args) {
		return new CnfExpression(operator, _booleanType, args);
	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0) {
		return new CnfExpression(operator, _booleanType, arg0);
	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1) {
		return new CnfExpression(operator, _booleanType, arg0, arg1);

	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		return new CnfExpression(operator, _booleanType, arg0, arg1, arg2);

	}

	@Override
	public BooleanSymbolicConstant booleanSymbolicConstant(StringObject name) {
		return new CnfSymbolicConstant(name, _booleanType);
	}

	@Override
	public BooleanExpression trueExpr() {
		return trueExpr;
	}

	@Override
	public BooleanExpression falseExpr() {
		return falseExpr;
	}

	@Override
	public BooleanExpression symbolic(BooleanObject object) {
		return object.getBoolean() ? trueExpr : falseExpr;
	}

	@Override
	public BooleanExpression symbolic(boolean value) {
		return value ? trueExpr : falseExpr;
	}

	@Override
	public BooleanExpression and(BooleanExpression arg0, BooleanExpression arg1) {
		if (arg0 == trueExpr)
			return arg1;
		if (arg1 == trueExpr)
			return arg0;
		if (arg0 == falseExpr || arg1 == falseExpr)
			return falseExpr;
		if (arg0.equals(arg1))
			return arg0;
		else {
			CnfExpression c0 = (CnfExpression) arg0;
			CnfExpression c1 = (CnfExpression) arg1;
			boolean isAnd0 = c0.operator() == SymbolicOperator.AND;
			boolean isAnd1 = c1.operator() == SymbolicOperator.AND;

			if (isAnd0 && isAnd1)
				return booleanExpression(SymbolicOperator.AND, c0
						.booleanSetArg(0).addAll(c1.booleanSetArg(0)));
			if (isAnd0 && !isAnd1)
				return booleanExpression(SymbolicOperator.AND, c0
						.booleanSetArg(0).add(c1));
			if (!isAnd0 && isAnd1)
				return booleanExpression(SymbolicOperator.AND, c1
						.booleanSetArg(0).add(c0));
			return booleanExpression(SymbolicOperator.AND, hashSet(c0, c1));
		}
	}

	@Override
	public BooleanExpression or(BooleanExpression arg0, BooleanExpression arg1) {
		if (arg0 == trueExpr || arg1 == trueExpr)
			return trueExpr;
		if (arg0 == falseExpr)
			return arg1;
		if (arg1 == falseExpr)
			return arg0;
		if (arg0.equals(arg1))
			return arg0;
		else {
			CnfExpression c0 = (CnfExpression) arg0;
			CnfExpression c1 = (CnfExpression) arg1;
			SymbolicOperator op0 = c0.operator();
			SymbolicOperator op1 = c1.operator();

			if (op0 == SymbolicOperator.AND) {
				BooleanExpression result = falseExpr;

				for (BooleanExpression clause : c0.booleanSetArg(0))
					result = or(result, and(clause, c1));
				return result;
			}
			if (op1 == SymbolicOperator.AND) {
				BooleanExpression result = falseExpr;

				for (BooleanExpression clause : c1.booleanSetArg(0))
					result = or(result, and(c0, clause));
				return result;
			}
			if (op0 == SymbolicOperator.OR && op1 == SymbolicOperator.OR) {
				return booleanExpression(op0,
						c0.booleanSetArg(0).addAll(c1.booleanSetArg(0)));
			}
			if (op0 == SymbolicOperator.OR) {
				return booleanExpression(op0, c0.booleanSetArg(0).add(c1));
			}
			if (op1 == SymbolicOperator.OR) {
				return booleanExpression(op1, c1.booleanSetArg(0).add(c0));
			}
			return booleanExpression(SymbolicOperator.OR, hashSet(c0, c1));
		}
	}

	@Override
	public BooleanExpression not(BooleanExpression arg) {
		CnfExpression cnf = (CnfExpression) arg;
		SymbolicOperator operator = cnf.operator();

		switch (operator) {
		case AND: {
			BooleanExpression result = falseExpr;

			for (BooleanExpression clause : cnf.booleanSetArg(0))
				result = or(result, not(clause));
			return result;
		}
		case OR: {
			BooleanExpression result = trueExpr;

			for (BooleanExpression clause : cnf.booleanSetArg(0))
				result = and(result, not(clause));
			return result;
		}
		case NOT:
			return cnf.booleanArg(0);
		case FORALL:
			return booleanExpression(SymbolicOperator.EXISTS,
					(SymbolicConstant) cnf.argument(0), not(cnf.booleanArg(1)));
		case EXISTS:
			return booleanExpression(SymbolicOperator.FORALL,
					(SymbolicConstant) cnf.argument(0), not(cnf.booleanArg(1)));
		case EQUALS:
			return booleanExpression(SymbolicOperator.NEQ,
					(SymbolicExpression) cnf.argument(0),
					(SymbolicExpression) cnf.argument(1));
		case NEQ:
			return booleanExpression(SymbolicOperator.EQUALS,
					(SymbolicExpression) cnf.argument(0),
					(SymbolicExpression) cnf.argument(1));
		default:
			return booleanExpression(SymbolicOperator.NOT, cnf);
		}
	}

	@Override
	public BooleanExpression implies(BooleanExpression arg0,
			BooleanExpression arg1) {
		return or(not(arg0), arg1);
	}

	@Override
	public BooleanExpression equiv(BooleanExpression arg0,
			BooleanExpression arg1) {
		BooleanExpression result = implies(arg0, arg1);

		if (result.isFalse())
			return result;
		return and(result, implies(arg1, arg0));
	}

	@Override
	public BooleanExpression forall(SymbolicConstant boundVariable,
			BooleanExpression predicate) {
		if (predicate == trueExpr)
			return trueExpr;
		if (predicate == falseExpr)
			return falseExpr;
		if (predicate.operator() == SymbolicOperator.AND) {
			BooleanExpression result = trueExpr;

			for (BooleanExpression clause : ((CnfExpression) predicate)
					.booleanSetArg(0))
				result = and(result, forall(boundVariable, clause));
			return result;
		}
		return booleanExpression(SymbolicOperator.FORALL, boundVariable,
				predicate);
	}

	@Override
	public BooleanExpression exists(SymbolicConstant boundVariable,
			BooleanExpression predicate) {
		if (predicate == trueExpr)
			return trueExpr;
		if (predicate == falseExpr)
			return falseExpr;
		if (predicate.operator() == SymbolicOperator.OR) {
			BooleanExpression result = falseExpr;

			for (BooleanExpression clause : ((CnfExpression) predicate)
					.booleanSetArg(0))
				result = or(result, exists(boundVariable, clause));
			return result;
		}
		return booleanExpression(SymbolicOperator.EXISTS, boundVariable,
				predicate);
	}

}