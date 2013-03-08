package edu.udel.cis.vsl.sarl.expr.cnf;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
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
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

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
		return new CnfBooleanExpression(operator, _booleanType, args);
	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject[] args) {
		return new CnfBooleanExpression(operator, _booleanType, args);
	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0) {
		return new CnfBooleanExpression(operator, _booleanType, arg0);
	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1) {
		return new CnfBooleanExpression(operator, _booleanType, arg0, arg1);

	}

	@Override
	public BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		return new CnfBooleanExpression(operator, _booleanType, arg0, arg1,
				arg2);

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
			boolean isAnd0 = arg0.operator() == SymbolicOperator.AND;
			boolean isAnd1 = arg1.operator() == SymbolicOperator.AND;

			if (isAnd0 && isAnd1)
				return booleanExpression(SymbolicOperator.AND, arg0
						.booleanSetArg(0).addAll(arg1.booleanSetArg(0)));
			if (isAnd0 && !isAnd1)
				return booleanExpression(SymbolicOperator.AND, arg0
						.booleanSetArg(0).add(arg1));
			if (!isAnd0 && isAnd1)
				return booleanExpression(SymbolicOperator.AND, arg1
						.booleanSetArg(0).add(arg0));
			return booleanExpression(SymbolicOperator.AND, hashSet(arg0, arg1));
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
			SymbolicOperator op0 = arg0.operator();
			SymbolicOperator op1 = arg1.operator();

			if (op0 == SymbolicOperator.AND) {
				BooleanExpression result = falseExpr;

				for (BooleanExpression clause : arg0.booleanSetArg(0))
					result = or(result, and(clause, arg1));
				return result;
			}
			if (op1 == SymbolicOperator.AND) {
				BooleanExpression result = falseExpr;

				for (BooleanExpression clause : arg1.booleanSetArg(0))
					result = or(result, and(arg0, clause));
				return result;
			}
			if (op0 == SymbolicOperator.OR && op1 == SymbolicOperator.OR) {
				return booleanExpression(op0,
						arg0.booleanSetArg(0).addAll(arg1.booleanSetArg(0)));
			}
			if (op0 == SymbolicOperator.OR) {
				return booleanExpression(op0, arg0.booleanSetArg(0).add(arg1));
			}
			if (op1 == SymbolicOperator.OR) {
				return booleanExpression(op1, arg1.booleanSetArg(0).add(arg0));
			}
			return booleanExpression(SymbolicOperator.OR, hashSet(arg0, arg1));
		}
	}

	@Override
	public BooleanExpression not(BooleanExpression arg) {
		SymbolicOperator operator = arg.operator();

		switch (operator) {
		case AND: {
			BooleanExpression result = falseExpr;

			for (BooleanExpression clause : arg.booleanSetArg(0))
				result = or(result, not(clause));
			return result;
		}
		case OR: {
			BooleanExpression result = trueExpr;

			for (BooleanExpression clause : arg.booleanSetArg(0))
				result = and(result, not(clause));
			return result;
		}
		case NOT:
			return arg.booleanArg(0);
		case FORALL:
			return booleanExpression(SymbolicOperator.EXISTS,
					(SymbolicConstant) arg.argument(0), not(arg.booleanArg(1)));
		case EXISTS:
			return booleanExpression(SymbolicOperator.FORALL,
					(SymbolicConstant) arg.argument(0), not(arg.booleanArg(1)));
		case EQUALS:
			return booleanExpression(SymbolicOperator.NEQ,
					(SymbolicExpression) arg.argument(0),
					(SymbolicExpression) arg.argument(1));
		case NEQ:
			return booleanExpression(SymbolicOperator.EQUALS,
					(SymbolicExpression) arg.argument(0),
					(SymbolicExpression) arg.argument(1));
		default:
			return booleanExpression(SymbolicOperator.NOT, arg);
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

			for (BooleanExpression clause : predicate.booleanSetArg(0))
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

			for (BooleanExpression clause : predicate.booleanSetArg(0))
				result = or(result, exists(boundVariable, clause));
			return result;
		}
		return booleanExpression(SymbolicOperator.EXISTS, boundVariable,
				predicate);
	}

}
