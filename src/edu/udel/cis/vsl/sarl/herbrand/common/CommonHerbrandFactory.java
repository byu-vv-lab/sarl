package edu.udel.cis.vsl.sarl.herbrand.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonHerbrandFactory implements NumericExpressionFactory {

	private NumberFactory numberFactory;

	private BooleanExpressionFactory booleanFactory;

	private ObjectFactory objectFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private Comparator<NumericExpression> comparator;

	private SymbolicType herbrandIntegerType, herbrandRealType;

	private HerbrandExpression zeroInt, zeroReal, oneInt, oneReal;

	public CommonHerbrandFactory(NumberFactory numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory,
			BooleanExpressionFactory booleanFactory) {
		this.numberFactory = numberFactory;
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.collectionFactory = collectionFactory;
		this.booleanFactory = booleanFactory;
		this.herbrandIntegerType = typeFactory.integerType();
		this.herbrandRealType = typeFactory.realType();
		this.oneInt = objectFactory.canonic(number(objectFactory
				.numberObject(numberFactory.oneInteger())));
		this.oneReal = objectFactory.canonic(number(objectFactory
				.numberObject(numberFactory.oneRational())));
		this.zeroInt = objectFactory.canonic(number(objectFactory
				.numberObject(numberFactory.zeroInteger())));
		this.zeroReal = objectFactory.canonic(number(objectFactory
				.numberObject(numberFactory.zeroRational())));
		this.comparator = new HerbrandComparator(objectFactory.comparator(),
				typeFactory.typeComparator());
	}

	private SymbolicType herbrandType(SymbolicType type) {
		if (type.isReal())
			return herbrandRealType;
		if (type.isInteger())
			return herbrandIntegerType;
		throw new SARLInternalException("Unknown numeric type: " + type);
	}

	private SymbolicType herbrandType(NumericExpression expression) {
		return herbrandType(expression.type());
	}

	@Override
	public void init() {
	}

	@Override
	public BooleanExpressionFactory booleanFactory() {
		return booleanFactory;
	}

	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	@Override
	public CollectionFactory collectionFactory() {
		return collectionFactory;
	}

	@Override
	public Comparator<NumericExpression> comparator() {
		return comparator;
	}

	@Override
	public HerbrandExpression number(NumberObject numberObject) {
		return expression(SymbolicOperator.CONCRETE,
				numberObject.isReal() ? herbrandRealType : herbrandIntegerType,
				numberObject);
	}

	@Override
	public NumericSymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		return new HerbrandSymbolicConstant(name, type);
	}

	@Override
	public HerbrandExpression expression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		return new HerbrandExpression(operator, numericType, arguments);
	}

	@Override
	public HerbrandExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		return new HerbrandExpression(operator, numericType, arguments);
	}

	@Override
	public HerbrandExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		return new HerbrandExpression(operator, numericType, arg0);
	}

	@Override
	public HerbrandExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		return new HerbrandExpression(operator, numericType, arg0, arg1);
	}

	@Override
	public HerbrandExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return new HerbrandExpression(operator, numericType, arg0, arg1, arg2);

	}

	@Override
	public HerbrandExpression zeroInt() {
		return zeroInt;
	}

	@Override
	public HerbrandExpression zeroReal() {
		return zeroReal;
	}

	@Override
	public HerbrandExpression oneInt() {
		return oneInt;
	}

	@Override
	public HerbrandExpression oneReal() {
		return oneReal;
	}

	@Override
	public HerbrandExpression add(NumericExpression arg0, NumericExpression arg1) {
		return expression(SymbolicOperator.ADD, herbrandType(arg0), arg0, arg1);
	}

	@Override
	public HerbrandExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		return expression(SymbolicOperator.SUBTRACT, herbrandType(arg0), arg0,
				arg1);
	}

	@Override
	public HerbrandExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		return expression(SymbolicOperator.MULTIPLY, herbrandType(arg0), arg0,
				arg1);
	}

	@Override
	public HerbrandExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType type = herbrandType(arg0.type());

		if (type.isReal())
			return expression(SymbolicOperator.DIVIDE, type, arg0, arg1);
		else
			return expression(SymbolicOperator.INT_DIVIDE, type, arg0, arg1);
	}

	@Override
	public HerbrandExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		return expression(SymbolicOperator.MODULO, herbrandIntegerType, arg0,
				arg1);
	}

	@Override
	public HerbrandExpression minus(NumericExpression arg) {
		return expression(SymbolicOperator.NEGATIVE, herbrandType(arg), arg);
	}

	@Override
	public HerbrandExpression power(NumericExpression base, IntObject exponent) {
		return power(base, number(objectFactory.numberObject(numberFactory
				.integer(exponent.getInt()))));
	}

	@Override
	public HerbrandExpression power(NumericExpression base,
			NumericExpression exponent) {
		return expression(SymbolicOperator.POWER, herbrandType(base), base,
				exponent);
	}

	@Override
	public NumericExpression cast(NumericExpression numericExpression,
			SymbolicType newType) {
		if (newType.equals(numericExpression.type()))
			return numericExpression;
		return expression(SymbolicOperator.CAST, newType, numericExpression);
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression.operator() == SymbolicOperator.CONCRETE) {
			SymbolicObject arg = expression.argument(0);

			if (arg instanceof NumberObject) {
				return ((NumberObject) arg).getNumber();
			}
		}
		return null;
	}

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		return booleanFactory.booleanExpression(SymbolicOperator.LESS_THAN,
				arg0, arg1);
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		return booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN_EQUALS, arg0, arg1);
	}

	@Override
	public BooleanExpression notLessThan(NumericExpression arg0,
			NumericExpression arg1) {
		return booleanFactory.booleanExpression(SymbolicOperator.NOT,
				lessThan(arg0, arg1));
	}

	@Override
	public BooleanExpression notLessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		return booleanFactory.booleanExpression(SymbolicOperator.NOT,
				lessThanEquals(arg0, arg1));
	}

	@Override
	public BooleanExpression equals(NumericExpression arg0,
			NumericExpression arg1) {
		return booleanFactory.booleanExpression(SymbolicOperator.EQUALS, arg0,
				arg1);
	}

	@Override
	public BooleanExpression neq(NumericExpression arg0, NumericExpression arg1) {
		return booleanFactory.booleanExpression(SymbolicOperator.NEQ, arg0,
				arg1);
	}
}
