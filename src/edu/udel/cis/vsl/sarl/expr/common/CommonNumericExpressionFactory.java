package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

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

public class CommonNumericExpressionFactory implements NumericExpressionFactory {

	private NumericExpressionFactory idealFactory;

	private NumericExpressionFactory herbrandFactory;

	private Comparator<NumericExpression> comparator;

	public CommonNumericExpressionFactory(
			NumericExpressionFactory idealFactory,
			NumericExpressionFactory herbrandFactory) {
		this.idealFactory = idealFactory;
		this.herbrandFactory = herbrandFactory;
		this.comparator = new CommonNumericComparator(
				idealFactory.comparator(), herbrandFactory.comparator());
	}

	@Override
	public void init() {
		idealFactory.init();
		herbrandFactory.init();
	}

	@Override
	public BooleanExpressionFactory booleanFactory() {
		return idealFactory.booleanFactory();
	}

	@Override
	public NumberFactory numberFactory() {
		return idealFactory.numberFactory();
	}

	@Override
	public ObjectFactory objectFactory() {
		return idealFactory.objectFactory();
	}

	@Override
	public SymbolicTypeFactory typeFactory() {
		return idealFactory.typeFactory();
	}

	@Override
	public CollectionFactory collectionFactory() {
		return idealFactory.collectionFactory();
	}

	@Override
	public Comparator<NumericExpression> comparator() {
		return comparator;
	}

	/**
	 * Returns the ideal number. If you want it to be Herbrand you need to cast
	 * it to the appropriate Herbrand type.
	 */
	@Override
	public NumericExpression number(NumberObject numberObject) {
		return idealFactory.number(numberObject);
	}

	@Override
	public NumericSymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		if (type.isHerbrand())
			return herbrandFactory.symbolicConstant(name, type);
		return idealFactory.symbolicConstant(name, type);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		if (numericType.isHerbrand())
			return herbrandFactory.expression(operator, numericType, arguments);
		return idealFactory.expression(operator, numericType, arguments);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		if (numericType.isHerbrand())
			return herbrandFactory.expression(operator, numericType, arguments);
		return idealFactory.expression(operator, numericType, arguments);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		if (numericType.isHerbrand())
			return herbrandFactory.expression(operator, numericType, arg0);
		return idealFactory.expression(operator, numericType, arg0);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		if (numericType.isHerbrand())
			return herbrandFactory
					.expression(operator, numericType, arg0, arg1);
		return idealFactory.expression(operator, numericType, arg0, arg1);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		if (numericType.isHerbrand())
			return herbrandFactory.expression(operator, numericType, arg0,
					arg1, arg2);
		return idealFactory.expression(operator, numericType, arg0, arg1, arg2);
	}

	@Override
	public NumericExpression zeroInt() {
		return idealFactory.zeroInt();
	}

	@Override
	public NumericExpression zeroReal() {
		return idealFactory.zeroReal();
	}

	@Override
	public NumericExpression oneInt() {
		return idealFactory.oneInt();
	}

	@Override
	public NumericExpression oneReal() {
		return idealFactory.oneReal();
	}

	@Override
	public NumericExpression add(NumericExpression arg0, NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.add(arg0, arg1);
		return idealFactory.add(arg0, arg1);
	}

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.subtract(arg0, arg1);
		return idealFactory.subtract(arg0, arg1);
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.multiply(arg0, arg1);
		return idealFactory.multiply(arg0, arg1);
	}

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.divide(arg0, arg1);
		return idealFactory.divide(arg0, arg1);
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.modulo(arg0, arg1);
		return idealFactory.modulo(arg0, arg1);
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
		if (arg.type().isHerbrand())
			return herbrandFactory.minus(arg);
		return idealFactory.minus(arg);
	}

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		if (base.type().isHerbrand())
			return herbrandFactory.power(base, exponent);
		return idealFactory.power(base, exponent);
	}

	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		if (base.type().isHerbrand() || exponent.type().isHerbrand())
			return herbrandFactory.power(base, exponent);
		return idealFactory.power(base, exponent);
	}

	@Override
	public NumericExpression cast(NumericExpression numericExpression,
			SymbolicType newType) {
		if (newType.isHerbrand())
			return herbrandFactory.cast(numericExpression, newType);
		return idealFactory.cast(numericExpression, newType);
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression.type().isHerbrand())
			return herbrandFactory.extractNumber(expression);
		return idealFactory.extractNumber(expression);
	}

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.lessThan(arg0, arg1);
		return idealFactory.lessThan(arg0, arg1);
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.lessThanEquals(arg0, arg1);
		return idealFactory.lessThanEquals(arg0, arg1);
	}

	@Override
	public BooleanExpression notLessThan(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.notLessThan(arg0, arg1);
		return idealFactory.notLessThan(arg0, arg1);
	}

	@Override
	public BooleanExpression notLessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.notLessThanEquals(arg0, arg1);
		return idealFactory.notLessThanEquals(arg0, arg1);
	}

	@Override
	public BooleanExpression equals(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.equals(arg0, arg1);
		return idealFactory.equals(arg0, arg1);
	}

	@Override
	public BooleanExpression neq(NumericExpression arg0, NumericExpression arg1) {
		if (arg0.type().isHerbrand() || arg1.type().isHerbrand())
			return herbrandFactory.neq(arg0, arg1);
		return idealFactory.neq(arg0, arg1);
	}
}

class CommonNumericComparator implements Comparator<NumericExpression> {
	private Comparator<NumericExpression> idealComparator;
	private Comparator<NumericExpression> herbrandComparator;

	CommonNumericComparator(Comparator<NumericExpression> idealComparator,
			Comparator<NumericExpression> herbrandComparator) {
		this.idealComparator = idealComparator;
		this.herbrandComparator = herbrandComparator;
	}

	@Override
	public int compare(NumericExpression o1, NumericExpression o2) {
		if (o1.type().isHerbrand()) {
			if (o2.type().isHerbrand())
				return herbrandComparator.compare(o1, o2);
			return -1;
		} else {
			if (o2.type().isHerbrand())
				return 1;
			return idealComparator.compare(o1, o2);
		}
	}

}
