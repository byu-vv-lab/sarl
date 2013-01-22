package edu.udel.cis.vsl.sarl.symbolic.concrete;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.type.SymbolicTypeFactory;

public class ConcreteFactory {

	private Map<SymbolicExpressionKey<NumericConcreteExpression>, NumericConcreteExpression> map = new HashMap<SymbolicExpressionKey<NumericConcreteExpression>, NumericConcreteExpression>();

	private SymbolicTypeIF booleanType, integerType, realType;

	private NumberFactoryIF numberFactory;

	private NumericConcreteExpression zeroIntExpression, oneIntExpression,
			zeroRealExpression, oneRealExpression;

	private SymbolicTypeFactory typeFactory;

	private BooleanConcreteExpression trueValue, falseValue;

	public ConcreteFactory(SymbolicTypeFactory typeFactory,
			NumberFactoryIF numberFactory) {
		this.typeFactory = typeFactory;
		this.numberFactory = numberFactory;
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
		booleanType = typeFactory.booleanType();
		trueValue = new BooleanConcreteExpression(booleanType, true);
		falseValue = new BooleanConcreteExpression(booleanType, false);
		zeroIntExpression = concrete(numberFactory.zeroInteger());
		oneIntExpression = concrete(numberFactory.oneInteger());
		zeroRealExpression = concrete(numberFactory.zeroRational());
		oneRealExpression = concrete(numberFactory.oneRational());
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public NumericConcreteExpression concrete(IntegerNumberIF value) {
		return CommonSymbolicExpression.flyweight(map, new NumericConcreteExpression(
				integerType, value));
	}

	public NumericConcreteExpression concrete(RationalNumberIF value) {
		return CommonSymbolicExpression.flyweight(map, new NumericConcreteExpression(
				realType, value));
	}

	public NumericConcreteExpression concrete(NumberIF value) {
		return CommonSymbolicExpression.flyweight(map, new NumericConcreteExpression(
				(value instanceof IntegerNumberIF ? integerType : realType),
				value));
	}

	public NumericConcreteExpression concrete(int value) {
		return concrete(numberFactory.integer(value));
	}

	public BooleanConcreteExpression concrete(boolean value) {
		return (value ? trueValue : falseValue);
	}

	public NumericConcreteExpressionIF abs(NumericConcreteExpressionIF x) {
		return concrete(numberFactory.abs(x.value()));
	}

	public NumericConcreteExpressionIF add(NumericConcreteExpressionIF x,
			NumericConcreteExpressionIF y) {
		return concrete(numberFactory.add(x.value(), y.value()));
	}

	public NumericConcreteExpressionIF subtract(NumericConcreteExpressionIF x,
			NumericConcreteExpressionIF y) {
		return concrete(numberFactory.subtract(x.value(), y.value()));
	}

	public NumericConcreteExpressionIF multiply(NumericConcreteExpressionIF x,
			NumericConcreteExpressionIF y) {
		return concrete(numberFactory.multiply(x.value(), y.value()));
	}

	public NumericConcreteExpressionIF divide(NumericConcreteExpressionIF x,
			NumericConcreteExpressionIF y) {
		return concrete(numberFactory.divide(x.value(), y.value()));
	}

	public NumericConcreteExpressionIF mod(NumericConcreteExpressionIF x,
			NumericConcreteExpressionIF y) {
		return concrete(numberFactory.mod((IntegerNumberIF) x.value(),
				(IntegerNumberIF) y.value()));
	}

	public NumericConcreteExpressionIF negate(
			NumericConcreteExpressionIF expression) {
		return concrete(numberFactory.negate(expression.value()));
	}

	public NumericConcreteExpressionIF zeroIntExpression() {
		return zeroIntExpression;
	}

	public NumericConcreteExpressionIF oneIntExpression() {
		return oneIntExpression;
	}

	public NumericConcreteExpressionIF zeroRealExpression() {
		return zeroRealExpression;
	}

	public NumericConcreteExpressionIF oneRealExpression() {
		return oneRealExpression;
	}

	public NumericConcreteExpressionIF castToReal(
			NumericConcreteExpressionIF expression) {
		if (expression.type().isReal()) {
			return expression;
		} else {
			return concrete(numberFactory.rational(expression.value()));
		}
	}

}
