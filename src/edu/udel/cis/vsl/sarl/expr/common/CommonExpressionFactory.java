package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonExpressionFactory implements ExpressionFactory {

	private ObjectFactory objectFactory;

	private ExpressionComparator expressionComparator;

	private NumericExpressionFactory numericFactory;

	public CommonExpressionFactory(NumericExpressionFactory numericFactory) {
		Comparator<NumericExpression> numericComparator = numericFactory
				.numericComparator();

		this.objectFactory = numericFactory.objectFactory();
		this.numericFactory = numericFactory;
		expressionComparator = new ExpressionComparator(numericComparator);
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
	public SymbolicExpressionIF canonic(SymbolicExpressionIF expression) {
		return (SymbolicExpressionIF) objectFactory.canonic(expression);
	}

	@Override
	public Comparator<SymbolicExpressionIF> comparator() {
		return expressionComparator;
	}

	@Override
	public SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] arguments) {
		return new CommonSymbolicExpression(operator, type, arguments);
	}

	@Override
	public SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0) {
		return new CommonSymbolicExpression(operator, type, arg0);

	}

	@Override
	public SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1) {
		return new CommonSymbolicExpression(operator, type, arg0, arg1);

	}

	@Override
	public SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return new CommonSymbolicExpression(operator, type, arg0, arg1, arg2);

	}

	@Override
	public SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, Collection<SymbolicObject> args) {
		return new CommonSymbolicExpression(operator, type, args);

	}

	@Override
	public SymbolicConstantIF symbolicConstant(StringObject name,
			SymbolicTypeIF type) {
		return new CommonSymbolicConstant(name, type);
	}

}
