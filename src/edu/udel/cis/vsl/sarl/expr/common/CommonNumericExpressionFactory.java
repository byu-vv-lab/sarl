package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * Given a numeric factory that deals with expressions of ideal type, and a
 * numeric factory that deals with expressions of herbrand type, this factory
 * puts them together to work on any numeric type.
 * 
 * @author siegel
 * 
 */
public class CommonNumericExpressionFactory implements NumericExpressionFactory {

	private NumericExpressionFactory idealFactory;

	private NumericExpressionFactory herbrandFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private SymbolicRealType herbrandRealType, idealRealType;

	private SymbolicIntegerType herbrandIntegerType, idealIntegerType;

	private BooleanExpressionFactory booleanFactory;

	private ObjectFactory objectFactory;

	private NumberFactory numberFactory;

	private Comparator<NumericExpression> comparator;

	public CommonNumericExpressionFactory(
			NumericExpressionFactory idealFactory,
			NumericExpressionFactory herbrandFactory) {
		this.idealFactory = idealFactory;
		this.herbrandFactory = herbrandFactory;
		this.booleanFactory = idealFactory.booleanFactory();
		this.typeFactory = idealFactory.typeFactory();
		this.objectFactory = typeFactory.objectFactory();
		this.collectionFactory = idealFactory.collectionFactory();
		this.numberFactory = idealFactory.numberFactory();
		this.herbrandRealType = typeFactory.herbrandRealType();
		this.herbrandIntegerType = typeFactory.herbrandIntegerType();
		this.idealRealType = typeFactory.realType();
		this.idealIntegerType = typeFactory.integerType();
		this.comparator = new CommonNumericComparator(
				idealFactory.comparator(), herbrandFactory.comparator());
	}

	// Helpers...

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
			return idealFactory
					.expression(SymbolicOperator.CAST,
							oldType.isInteger() ? idealIntegerType
									: idealRealType, arg);
		}
	}

	private NumericExpression castToHerbrand(NumericExpression arg) {
		SymbolicType oldType = arg.type();

		if (oldType.isHerbrand())
			return arg;
		else {
			if (arg.operator() == SymbolicOperator.CONCRETE) {
				SymbolicObject object = arg.argument(0);

				if (object instanceof NumberObject)
					return herbrandFactory.number((NumberObject) object);
			}
			return herbrandFactory.expression(SymbolicOperator.CAST, oldType
					.isInteger() ? herbrandIntegerType : herbrandRealType, arg);
		}
	}

	// Exported methods....

	public NumericExpressionFactory idealFactory() {
		return idealFactory;
	}

	public NumericExpressionFactory herbrandFactory() {
		return herbrandFactory;
	}

	@Override
	public void init() {
		idealFactory.init();
		herbrandFactory.init();
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
		if (type.isIdeal())
			return idealFactory.symbolicConstant(name, type);
		else
			return herbrandFactory.symbolicConstant(name, type);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arguments);
		else
			return herbrandFactory.expression(operator, numericType, arguments);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arguments);
		else
			return herbrandFactory.expression(operator, numericType, arguments);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arg0);
		else
			return herbrandFactory.expression(operator, numericType, arg0);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arg0, arg1);
		else
			return herbrandFactory
					.expression(operator, numericType, arg0, arg1);
	}

	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arg0, arg1,
					arg2);
		else
			return herbrandFactory.expression(operator, numericType, arg0,
					arg1, arg2);
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

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.add(arg0, arg1);
		else
			return herbrandFactory.add(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.subtract(arg0, arg1);
		else
			return herbrandFactory.subtract(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.multiply(arg0, arg1);
		else
			return herbrandFactory.multiply(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.divide(arg0, arg1);
		else
			return herbrandFactory.divide(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.modulo(arg0, arg1);
		else
			return herbrandFactory.modulo(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
		SymbolicType type = arg.type();

		if (type.isIdeal())
			return idealFactory.minus(arg);
		else
			return herbrandFactory.minus(castToHerbrand(arg));
	}

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		SymbolicType type = base.type();

		if (type.isIdeal())
			return idealFactory.power(base, exponent);
		else
			return herbrandFactory.power(castToHerbrand(base), exponent);
	}

	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		SymbolicType t1 = base.type(), t2 = exponent.type();

		if (t1.isIdeal() && t2.isIdeal())
			return idealFactory.power(base, exponent);
		else
			return herbrandFactory.power(castToHerbrand(base),
					castToHerbrand(exponent));
	}

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
			else if (newType.isHerbrand())
				return herbrandFactory.number(numberObject);
			else
				throw new SARLInternalException("Unknown type: " + newType);
		} else if (newType.isIdeal())
			return idealFactory.cast(castToIdeal(expr), newType);
		else if (newType.isHerbrand())
			return herbrandFactory.cast(castToHerbrand(expr), newType);
		else
			throw new SARLInternalException("Unknown type: " + newType);
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression.type().isHerbrand())
			return herbrandFactory.extractNumber(expression);
		else if (expression.type().isIdeal())
			return idealFactory.extractNumber(expression);
		return null;
	}

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.lessThan(arg0, arg1);
		else
			return herbrandFactory.lessThan(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.lessThanEquals(arg0, arg1);
		else
			return herbrandFactory.lessThanEquals(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public BooleanExpression notLessThan(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.notLessThan(arg0, arg1);
		else
			return herbrandFactory.notLessThan(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	@Override
	public BooleanExpression notLessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.notLessThanEquals(arg0, arg1);
		else
			return herbrandFactory.notLessThanEquals(castToHerbrand(arg0),
					castToHerbrand(arg1));
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
