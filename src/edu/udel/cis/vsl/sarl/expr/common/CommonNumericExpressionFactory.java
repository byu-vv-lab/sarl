package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

// what should the Java type of the numeric expressions be?
// they could all be IdealExpression, as the symbolic type
// distinguished between ideals and herbrands.
// call this the IdealHerbrandFactory.
// place it with the Ideals.

// problem: the Herbrand concretes are not Constants so
// are considered primitives degree 1 by IdealFactory

public class CommonNumericExpressionFactory implements NumericExpressionFactory {

	private NumericExpressionFactory idealFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private SymbolicRealType herbrandRealType, realType;

	private SymbolicIntegerType herbrandIntegerType, integerType;

	private BooleanExpressionFactory booleanFactory;

	private SymbolicType booleanType;

	private ObjectFactory objectFactory;

	private NumberFactory numberFactory;

	private SymbolicFunctionType realBinaryOp, realUnaryOp, integerBinaryOp,
			integerUnaryOp, realBinaryPred, integerBinaryPred;

	private SymbolicConstant plusReal, plusInteger, minusReal, minusInteger,
			timesReal, timesInteger, divideReal, divideInteger, negativeReal,
			negativeInteger, modulo, powerInteger, powerReal, lessThanInteger,
			lessThanReal, lteInteger, lteReal;

	public CommonNumericExpressionFactory(NumericExpressionFactory idealFactory) {
		this.idealFactory = idealFactory;
		this.booleanFactory = idealFactory.booleanFactory();
		this.typeFactory = idealFactory.typeFactory();
		this.objectFactory = typeFactory.objectFactory();
		this.collectionFactory = idealFactory.collectionFactory();
		this.numberFactory = idealFactory.numberFactory();
		this.herbrandRealType = typeFactory.herbrandRealType();
		this.herbrandIntegerType = typeFactory.herbrandIntegerType();
		this.realType = typeFactory.realType();
		this.integerType = typeFactory.integerType();
		this.booleanType = typeFactory.booleanType();
	}

	// Helpers...

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
			plusReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("PLUS_REAL"), realBinaryOp()));
		return plusReal;
	}

	private SymbolicConstant plusInteger() {
		if (plusInteger == null)
			plusInteger = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("PLUS_INT"), integerBinaryOp()));
		return plusInteger;
	}

	private SymbolicConstant plusOperator(SymbolicType type) {
		return type.isInteger() ? plusInteger() : plusReal();
	}

	private SymbolicConstant minusReal() {
		if (minusReal == null)
			minusReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("MINUS_REAL"), realBinaryOp()));
		return minusReal;
	}

	private SymbolicConstant minusInteger() {
		if (minusInteger == null)
			minusInteger = objectFactory
					.canonic(new CommonSymbolicConstant(objectFactory
							.stringObject("MINUS_INT"), integerBinaryOp()));
		return minusInteger;
	}

	private SymbolicConstant minusOperator(SymbolicType type) {
		return type.isInteger() ? minusInteger() : minusReal();
	}

	private SymbolicConstant timesReal() {
		if (timesReal == null)
			timesReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("TIMES_REAL"), realBinaryOp()));
		return timesReal;
	}

	private SymbolicConstant timesInteger() {
		if (timesInteger == null)
			timesInteger = objectFactory
					.canonic(new CommonSymbolicConstant(objectFactory
							.stringObject("TIMES_INT"), integerBinaryOp()));
		return timesInteger;
	}

	private SymbolicConstant timesOperator(SymbolicType type) {
		return type.isInteger() ? timesInteger() : timesReal();
	}

	private SymbolicConstant divideReal() {
		if (divideReal == null)
			divideReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("DIVIDE_REAL"), realBinaryOp()));
		return divideReal;
	}

	private SymbolicConstant divideInteger() {
		if (divideInteger == null)
			divideInteger = objectFactory
					.canonic(new CommonSymbolicConstant(objectFactory
							.stringObject("DIVIDE_INT"), integerBinaryOp()));
		return divideInteger;
	}

	private SymbolicConstant divideOperator(SymbolicType type) {
		return type.isInteger() ? divideInteger() : divideReal();
	}

	private SymbolicConstant moduloOperator() {
		if (modulo == null)
			modulo = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("MODULO"), integerBinaryOp()));
		return modulo;
	}

	private SymbolicConstant negativeInteger() {
		if (negativeInteger == null)
			negativeInteger = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("NEGATIVE_INT"),
					integerUnaryOp()));
		return negativeInteger;
	}

	private SymbolicConstant negativeReal() {
		if (negativeReal == null)
			negativeReal = objectFactory
					.canonic(new CommonSymbolicConstant(objectFactory
							.stringObject("NEGATIVE_REAL"), realUnaryOp()));
		return negativeReal;
	}

	private SymbolicConstant negativeOperator(SymbolicType type) {
		return type.isInteger() ? negativeInteger() : negativeReal();
	}

	private SymbolicConstant powerReal() {
		if (powerReal == null)
			powerReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("POWER_REAL"), realBinaryOp()));
		return powerReal;
	}

	private SymbolicConstant powerInteger() {
		if (powerInteger == null)
			powerInteger = objectFactory
					.canonic(new CommonSymbolicConstant(objectFactory
							.stringObject("POWER_INT"), integerBinaryOp()));
		return powerInteger;
	}

	// private SymbolicConstant powerOperator(SymbolicType type) {
	// return type.isInteger() ? powerInteger() : powerReal();
	// }

	private SymbolicConstant lessThanInteger() {
		if (lessThanInteger == null)
			lessThanInteger = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("LT_INT"), integerBinaryPred()));
		return lessThanInteger;
	}

	private SymbolicConstant lessThanReal() {
		if (lessThanReal == null)
			lessThanReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("LT_REAL"), realBinaryPred()));
		return lessThanReal;
	}

	private SymbolicConstant lessThanOperator(SymbolicType type) {
		return type.isInteger() ? lessThanInteger() : lessThanReal();
	}

	private SymbolicConstant lteInteger() {
		if (lteInteger == null)
			lteInteger = objectFactory
					.canonic(new CommonSymbolicConstant(objectFactory
							.stringObject("LTE_INT"), integerBinaryPred()));
		return lteInteger;
	}

	private SymbolicConstant lteReal() {
		if (lteReal == null)
			lteReal = objectFactory.canonic(new CommonSymbolicConstant(
					objectFactory.stringObject("LTE_REAL"), realBinaryPred()));
		return lteReal;
	}

	private SymbolicConstant lteOperator(SymbolicType type) {
		return type.isInteger() ? lteInteger() : lteReal();
	}

	// // should expression return an instance of HerbrandExpression...
	// // does it matter? It can be any NumericExpression.
	// private NumericExpression herbrandNumber(NumberObject numberObject) {
	// return expression(SymbolicOperator.CONCRETE,
	// numberObject.isReal() ? herbrandRealType : herbrandIntegerType,
	// numberObject);
	// }

	// Exported methods....

	@Override
	public void init() {
		idealFactory.init();
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
		return idealFactory.comparator();
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
		return idealFactory.symbolicConstant(name, type);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		return idealFactory.expression(operator, numericType, arguments);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		return idealFactory.expression(operator, numericType, arguments);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		return idealFactory.expression(operator, numericType, arg0);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		return idealFactory.expression(operator, numericType, arg0, arg1);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
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
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return expression(SymbolicOperator.APPLY, h0 ? t0 : t1,
					plusOperator(t0), sequence(arg0, arg1));
		return idealFactory.add(arg0, arg1);
	}

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return expression(SymbolicOperator.APPLY, h0 ? t0 : t1,
					minusOperator(t0), sequence(arg0, arg1));
		return idealFactory.subtract(arg0, arg1);
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return expression(SymbolicOperator.APPLY, h0 ? t0 : t1,
					timesOperator(t0), sequence(arg0, arg1));
		return idealFactory.multiply(arg0, arg1);
	}

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return expression(SymbolicOperator.APPLY, h0 ? t0 : t1,
					divideOperator(t0), sequence(arg0, arg1));
		return idealFactory.divide(arg0, arg1);
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return expression(SymbolicOperator.APPLY, h0 ? t0 : t1,
					moduloOperator(), sequence(arg0, arg1));
		return idealFactory.modulo(arg0, arg1);
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
		SymbolicType type = arg.type();

		if (type.isHerbrand())
			return expression(SymbolicOperator.APPLY, type,
					negativeOperator(type),
					collectionFactory.singletonSequence(arg));
		return idealFactory.minus(arg);
	}

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		SymbolicType type = base.type();

		if (type.isHerbrand()) {
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
								.numberObject(numberFactory
										.rational(numberFactory
												.integer(exponent.getInt()))))));
		}
		return idealFactory.power(base, exponent);
	}

	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		SymbolicType t1 = base.type(), t2 = exponent.type();

		if (t1.isHerbrand() || t2.isHerbrand()) {
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
		return idealFactory.power(base, exponent);
	}

	// legal casts: any
	// int, hint, bint(...), real, hreal, float(...)
	// concretes are all translated over: no casts
	// if new type is ideal, use idealFactory.number
	@Override
	public NumericExpression cast(NumericExpression expr, SymbolicType newType) {
		SymbolicType oldType = expr.type();

		if (oldType.equals(newType))
			return expr;

		SymbolicOperator op = expr.operator();

		if (op == SymbolicOperator.CONCRETE) {
			NumberObject numberObject = (NumberObject) expr.argument(0);
			Number number = numberObject.getNumber();

			if (oldType.isInteger() && newType.isReal()) {
				numberObject = objectFactory.numberObject(numberFactory
						.rational(number));
			} else if (oldType.isReal() && newType.isInteger()) {
				if (number.signum() >= 0)
					number = numberFactory.floor((RationalNumber) number);
				else
					number = numberFactory.ceil((RationalNumber) number);
				numberObject = objectFactory.numberObject(number);
			}
			if (newType.isIdeal())
				return idealFactory.number(numberObject);
			else
				return expression(op, newType, numberObject);
		} else {
			if (oldType.isIdeal() && newType.isIdeal())
				return idealFactory.cast(expr, newType);
			return expression(SymbolicOperator.CAST, newType, expr);
		}
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression.type().isHerbrand()) {
			if (expression.operator() == SymbolicOperator.CONCRETE)
				return (Number) expression.argument(0);
			return null;
		}
		return idealFactory.extractNumber(expression);
	}

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return booleanFactory.booleanExpression(SymbolicOperator.APPLY,
					lessThanOperator(t0), sequence(arg0, arg1));
		return idealFactory.lessThan(arg0, arg1);
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return booleanFactory.booleanExpression(SymbolicOperator.APPLY,
					lteOperator(t0), sequence(arg0, arg1));
		return idealFactory.lessThan(arg0, arg1);
	}

	@Override
	public BooleanExpression notLessThan(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return booleanFactory.booleanExpression(SymbolicOperator.NOT,
					lessThan(arg0, arg1));
		return idealFactory.notLessThan(arg0, arg1);
	}

	@Override
	public BooleanExpression notLessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();
		boolean h0 = t0.isHerbrand();

		if (h0 || t1.isHerbrand())
			return booleanFactory.booleanExpression(SymbolicOperator.NOT,
					lessThanEquals(arg0, arg1));
		return idealFactory.notLessThanEquals(arg0, arg1);
	}

	private NumericExpression castToIdeal(NumericExpression arg) {
		SymbolicType oldType = arg.type();

		if (oldType.isIdeal())
			return arg;
		else {
			if (arg.operator() == SymbolicOperator.CONCRETE) {
				SymbolicObject object = arg.argument(0);

				if (object instanceof NumberObject)
					return idealFactory.number((NumberObject) object);
			}
			return expression(SymbolicOperator.CAST,
					oldType.isInteger() ? integerType : realType, arg);
		}
	}

	@Override
	public BooleanExpression equals(NumericExpression arg0,
			NumericExpression arg1) {
		return idealFactory.equals(castToIdeal(arg0), castToIdeal(arg1));
	}

	@Override
	public BooleanExpression neq(NumericExpression arg0, NumericExpression arg1) {
		return idealFactory.neq(castToIdeal(arg0), castToIdeal(arg1));
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
