package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonExpressionFactory implements ExpressionFactory {

	private ObjectFactory objectFactory;

	private ExpressionComparator expressionComparator;

	private NumericExpressionFactory numericFactory;

	private BooleanExpressionFactory booleanFactory;

	private SymbolicExpression nullExpression;

	public CommonExpressionFactory(NumericExpressionFactory numericFactory,
			BooleanExpressionFactory booleanFactory) {
		Comparator<NumericExpression> numericComparator = numericFactory
				.numericComparator();

		this.objectFactory = numericFactory.objectFactory();
		this.numericFactory = numericFactory;
		this.booleanFactory = booleanFactory;
		numericFactory.setExpressionFactory(this);
		expressionComparator = new ExpressionComparator(numericComparator);
		nullExpression = canonic(expression(SymbolicOperator.NULL, null,
				new SymbolicObject[] {}));
	}

	@Override
	public void setObjectComparator(Comparator<SymbolicObject> c) {
		expressionComparator.setObjectComparator(c);
		numericFactory.setObjectComparator(c);
	}

	@Override
	public void setTypeComparator(Comparator<SymbolicType> c) {
		expressionComparator.setTypeComparator(c);
	}

	@Override
	public void init() {
		assert expressionComparator.objectComparator() != null;
		numericFactory.init();
	}

	@Override
	public NumericExpressionFactory numericFactory() {
		return numericFactory;
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	@Override
	public SymbolicExpression canonic(SymbolicExpression expression) {
		return objectFactory.canonic(expression);
	}

	@Override
	public Comparator<SymbolicExpression> comparator() {
		return expressionComparator;
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] arguments) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.newNumericExpression(operator, type,
						arguments);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arguments);
		}
		return new CommonSymbolicExpression(operator, type, arguments);
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory
						.newNumericExpression(operator, type, arg0);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arg0);
		}
		return new CommonSymbolicExpression(operator, type, arg0);
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.newNumericExpression(operator, type,
						arg0, arg1);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arg0, arg1);
		}
		return new CommonSymbolicExpression(operator, type, arg0, arg1);
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.newNumericExpression(operator, type,
						arg0, arg1, arg2);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arg0, arg1,
						arg2);
		}
		return new CommonSymbolicExpression(operator, type, arg0, arg1, arg2);
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, Collection<SymbolicObject> args) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory
						.newNumericExpression(operator, type, args);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, args);
		}
		return new CommonSymbolicExpression(operator, type, args);

	}

	@Override
	public SymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		return new CommonSymbolicConstant(name, type);
	}

	@Override
	public SymbolicExpression nullExpression() {
		return nullExpression;
	}

	@Override
	public BooleanExpressionFactory booleanFactory() {
		return booleanFactory;
	}

}
