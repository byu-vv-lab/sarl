package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonExpressionFactory implements ExpressionFactory {

	private ObjectFactory objectFactory;

	private ExpressionComparator expressionComparator;

	private NumericExpressionFactory numericFactory;

	private BooleanExpressionFactory booleanFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private SymbolicExpression nullExpression;

	public CommonExpressionFactory(NumericExpressionFactory numericFactory) {
		this.numericFactory = numericFactory;
		this.objectFactory = numericFactory.objectFactory();
		this.booleanFactory = numericFactory.booleanFactory();
		this.typeFactory = numericFactory.typeFactory();
		this.collectionFactory = numericFactory.collectionFactory();
		this.expressionComparator = new ExpressionComparator(
				numericFactory.comparator(), objectFactory.comparator(),
				typeFactory.typeComparator());
		this.nullExpression = objectFactory.canonic(expression(
				SymbolicOperator.NULL, null, new SymbolicObject[] {}));
		typeFactory.setExpressionComparator(expressionComparator);
		collectionFactory.setElementComparator(expressionComparator);
		objectFactory.setExpressionComparator(expressionComparator);
	}

	@Override
	public void init() {
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
	public Comparator<SymbolicExpression> comparator() {
		return expressionComparator;
	}

	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] arguments) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.expression(operator, type,
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
						.expression(operator, type, arg0);
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
				return numericFactory.expression(operator, type,
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
				return numericFactory.expression(operator, type,
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
						.expression(operator, type, args);
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

	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

}
