package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonExpressionFactory implements ExpressionFactory {

	private ObjectFactory objectFactory;

	private ExpressionComparator expressionComparator;

	private NumericExpressionFactory numericFactory;

	private SymbolicExpression nullExpression;

	public CommonExpressionFactory(NumericExpressionFactory numericFactory) {
		Comparator<NumericExpression> numericComparator = numericFactory
				.numericComparator();

		this.objectFactory = numericFactory.objectFactory();
		this.numericFactory = numericFactory;
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
		return new CommonSymbolicExpression(operator, type, arguments);
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0) {
		return new CommonSymbolicExpression(operator, type, arg0);

	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1) {
		return new CommonSymbolicExpression(operator, type, arg0, arg1);

	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return new CommonSymbolicExpression(operator, type, arg0, arg1, arg2);

	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, Collection<SymbolicObject> args) {
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

}
