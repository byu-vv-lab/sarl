package edu.udel.cis.vsl.sarl.herbrand.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
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

	private SymbolicType herbrandIntegerType, herbrandRealType, booleanType;

	private HerbrandExpression zeroInt, zeroReal, oneInt, oneReal;

	private SymbolicFunctionType realBinaryOp, realUnaryOp, integerBinaryOp,
			integerUnaryOp, realBinaryPred, integerBinaryPred;

	private SymbolicConstant plusReal, plusInteger, minusReal, minusInteger,
			timesReal, timesInteger, divideReal, divideInteger, negativeReal,
			negativeInteger, modulo, powerInteger, powerReal, lessThanInteger,
			lessThanReal, lteInteger, lteReal;

	public CommonHerbrandFactory(NumberFactory numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory,
			BooleanExpressionFactory booleanFactory) {
		this.numberFactory = numberFactory;
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.collectionFactory = collectionFactory;
		this.booleanFactory = booleanFactory;
		this.herbrandIntegerType = typeFactory.herbrandIntegerType();
		this.herbrandRealType = typeFactory.herbrandRealType();
		this.booleanType = typeFactory.booleanType();
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

	private SymbolicSequence<NumericExpression> sequence(NumericExpression e0,
			NumericExpression e1) {
		return collectionFactory.sequence(new NumericExpression[] { e0, e1 });
	}

	private SymbolicFunctionType realBinaryOp() {
		if (realBinaryOp == null)
			realBinaryOp = objectFactory.canonic(typeFactory.functionType(
					typeFactory.sequence(new SymbolicType[] { herbrandRealType,
							herbrandRealType }), herbrandRealType));
		return realBinaryOp;
	}

	private SymbolicFunctionType integerBinaryOp() {
		if (integerBinaryOp == null)
			integerBinaryOp = objectFactory.canonic(typeFactory.functionType(
					typeFactory.sequence(new SymbolicType[] {
							herbrandIntegerType, herbrandIntegerType }),
					herbrandIntegerType));
		return integerBinaryOp;
	}

	private SymbolicFunctionType realUnaryOp() {
		if (realUnaryOp == null)
			realUnaryOp = objectFactory.canonic(typeFactory.functionType(
					typeFactory
							.sequence(new SymbolicType[] { herbrandRealType }),
					herbrandRealType));
		return realUnaryOp;
	}

	private SymbolicFunctionType integerUnaryOp() {
		if (integerUnaryOp == null)
			integerUnaryOp = objectFactory
					.canonic(typeFactory.functionType(
							typeFactory
									.sequence(new SymbolicType[] { herbrandIntegerType }),
							herbrandIntegerType));
		return integerUnaryOp;
	}

	private SymbolicFunctionType realBinaryPred() {
		if (realBinaryPred == null)
			realBinaryPred = objectFactory.canonic(typeFactory.functionType(
					typeFactory.sequence(new SymbolicType[] { herbrandRealType,
							herbrandRealType }), booleanType));
		return realBinaryPred;
	}

	private SymbolicFunctionType integerBinaryPred() {
		if (integerBinaryPred == null)
			integerBinaryPred = objectFactory.canonic(typeFactory.functionType(
					typeFactory.sequence(new SymbolicType[] {
							herbrandIntegerType, herbrandIntegerType }),
					booleanType));
		return integerBinaryPred;
	}

	private SymbolicConstant plusReal() {
		if (plusReal == null)
			plusReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("PLUS_REAL"), realBinaryOp()));
		return plusReal;
	}

	private SymbolicConstant plusInteger() {
		if (plusInteger == null)
			plusInteger = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("PLUS_INT"), integerBinaryOp()));
		return plusInteger;
	}

	private SymbolicConstant plusOperator(SymbolicType type) {
		return type.isInteger() ? plusInteger() : plusReal();
	}

	private SymbolicConstant minusReal() {
		if (minusReal == null)
			minusReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("MINUS_REAL"), realBinaryOp()));
		return minusReal;
	}

	private SymbolicConstant minusInteger() {
		if (minusInteger == null)
			minusInteger = objectFactory
					.canonic(symbolicConstant(
							objectFactory.stringObject("MINUS_INT"),
							integerBinaryOp()));
		return minusInteger;
	}

	private SymbolicConstant minusOperator(SymbolicType type) {
		return type.isInteger() ? minusInteger() : minusReal();
	}

	private SymbolicConstant timesReal() {
		if (timesReal == null)
			timesReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("TIMES_REAL"), realBinaryOp()));
		return timesReal;
	}

	private SymbolicConstant timesInteger() {
		if (timesInteger == null)
			timesInteger = objectFactory
					.canonic(symbolicConstant(
							objectFactory.stringObject("TIMES_INT"),
							integerBinaryOp()));
		return timesInteger;
	}

	private SymbolicConstant timesOperator(SymbolicType type) {
		return type.isInteger() ? timesInteger() : timesReal();
	}

	private SymbolicConstant divideReal() {
		if (divideReal == null)
			divideReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("DIVIDE_REAL"), realBinaryOp()));
		return divideReal;
	}

	private SymbolicConstant divideInteger() {
		if (divideInteger == null)
			divideInteger = objectFactory
					.canonic(symbolicConstant(
							objectFactory.stringObject("DIVIDE_INT"),
							integerBinaryOp()));
		return divideInteger;
	}

	private SymbolicConstant divideOperator(SymbolicType type) {
		return type.isInteger() ? divideInteger() : divideReal();
	}

	private SymbolicConstant moduloOperator() {
		if (modulo == null)
			modulo = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("MODULO"), integerBinaryOp()));
		return modulo;
	}

	private SymbolicConstant negativeInteger() {
		if (negativeInteger == null)
			negativeInteger = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("NEGATIVE_INT"),
					integerUnaryOp()));
		return negativeInteger;
	}

	private SymbolicConstant negativeReal() {
		if (negativeReal == null)
			negativeReal = objectFactory
					.canonic(symbolicConstant(
							objectFactory.stringObject("NEGATIVE_REAL"),
							realUnaryOp()));
		return negativeReal;
	}

	private SymbolicConstant negativeOperator(SymbolicType type) {
		return type.isInteger() ? negativeInteger() : negativeReal();
	}

	private SymbolicConstant powerReal() {
		if (powerReal == null)
			powerReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("POWER_REAL"), realBinaryOp()));
		return powerReal;
	}

	private SymbolicConstant powerInteger() {
		if (powerInteger == null)
			powerInteger = objectFactory
					.canonic(symbolicConstant(
							objectFactory.stringObject("POWER_INT"),
							integerBinaryOp()));
		return powerInteger;
	}

	// private SymbolicConstant powerOperator(SymbolicType type) {
	// return type.isInteger() ? powerInteger() : powerReal();
	// }

	private SymbolicConstant lessThanInteger() {
		if (lessThanInteger == null)
			lessThanInteger = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("LT_INT"), integerBinaryPred()));
		return lessThanInteger;
	}

	private SymbolicConstant lessThanReal() {
		if (lessThanReal == null)
			lessThanReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("LT_REAL"), realBinaryPred()));
		return lessThanReal;
	}

	private SymbolicConstant lessThanOperator(SymbolicType type) {
		return type.isInteger() ? lessThanInteger() : lessThanReal();
	}

	private SymbolicConstant lteInteger() {
		if (lteInteger == null)
			lteInteger = objectFactory
					.canonic(symbolicConstant(
							objectFactory.stringObject("LTE_INT"),
							integerBinaryPred()));
		return lteInteger;
	}

	private SymbolicConstant lteReal() {
		if (lteReal == null)
			lteReal = objectFactory.canonic(symbolicConstant(
					objectFactory.stringObject("LTE_REAL"), realBinaryPred()));
		return lteReal;
	}

	private SymbolicConstant lteOperator(SymbolicType type) {
		return type.isInteger() ? lteInteger() : lteReal();
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
	public NumericExpression add(NumericExpression arg0, NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return expression(SymbolicOperator.APPLY, t0, plusOperator(t0),
				sequence(arg0, arg1));
	}

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return expression(SymbolicOperator.APPLY, t0, minusOperator(t0),
				sequence(arg0, arg1));
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return expression(SymbolicOperator.APPLY, t0, timesOperator(t0),
				sequence(arg0, arg1));
	}

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return expression(SymbolicOperator.APPLY, t0, divideOperator(t0),
				sequence(arg0, arg1));
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return expression(SymbolicOperator.APPLY, t0, moduloOperator(),
				sequence(arg0, arg1));
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
		SymbolicType type = arg.type();

		return expression(SymbolicOperator.APPLY, type, negativeOperator(type),
				collectionFactory.singletonSequence(arg));
	}

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		SymbolicType type = base.type();

		if (type.isInteger())
			return expression(
					SymbolicOperator.APPLY,
					type,
					powerInteger(),
					sequence(base, number(objectFactory
							.numberObject(numberFactory.integer(exponent
									.getInt())))));
		else
			return expression(
					SymbolicOperator.APPLY,
					type,
					powerReal(),
					sequence(base, number(objectFactory
							.numberObject(numberFactory.rational(numberFactory
									.integer(exponent.getInt()))))));
	}

	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		SymbolicType t1 = base.type(), t2 = exponent.type();

		if (t1.isInteger() && t2.isInteger()) {
			return expression(SymbolicOperator.APPLY, herbrandIntegerType,
					powerInteger(), sequence(base, exponent));
		} else {
			if (t1.isInteger())
				base = cast(base, herbrandRealType);
			if (t2.isInteger())
				exponent = cast(exponent, herbrandRealType);
			return expression(SymbolicOperator.APPLY, herbrandRealType,
					powerReal(), sequence(base, exponent));
		}
	}

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return booleanFactory.booleanExpression(SymbolicOperator.APPLY,
				lessThanOperator(t0), sequence(arg0, arg1));
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type();

		return booleanFactory.booleanExpression(SymbolicOperator.APPLY,
				lteOperator(t0), sequence(arg0, arg1));
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
