/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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

	/**
	 * Constructor that creates a CommonNumericExpressionFactory.
	 * 
	 * @param idealFactory
	 * @param herbrandFactory
	 * 
	 * @return CommonNumericExpressionFactory
	 */
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

	/**
	 * Private method that casts NumericExpression to Ideal type.
	 * 
	 * @param arg
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Private method that casts NumericExpression to Herbrand type.
	 * 
	 * @param arg
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Getter method that returns idealFactory.
	 * 
	 * @return idealFactory
	 */
	public NumericExpressionFactory idealFactory() {
		return idealFactory;
	}

	/**
	 * Getter method that returns herbrandFactory.
	 * 
	 * @return herbrandFactory
	 */
	public NumericExpressionFactory herbrandFactory() {
		return herbrandFactory;
	}

	/**
	 * Method that initializes this CommonNumericExpressionFactory
	 * 
	 */
	@Override
	public void init() {
		idealFactory.init();
		herbrandFactory.init();
	}

	/**
	 * Getter method that returns booleanFactory.
	 * 
	 * @return BooleanExpressionFactory
	 */
	@Override
	public BooleanExpressionFactory booleanFactory() {
		return booleanFactory;
	}

	/**
	 * Getter method that returns numberFactory.
	 * 
	 * @return NumberFactory
	 */
	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	/**
	 * Getter method that returns objectFacotry.
	 * 
	 * @return ObjectFactory
	 */
	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	/**
	 * Getter method that returns typeFactory.
	 * 
	 * @return SymbolicTypeFactory
	 */
	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	/**
	 * Getter method that returns collectionFactory.
	 * 
	 * @return CollectionFactory
	 */
	@Override
	public CollectionFactory collectionFactory() {
		return collectionFactory;
	}

	/**
	 * Getter method that returns comparator.
	 * 
	 * @return Comparator<NumericExpression>
	 */
	@Override
	public Comparator<NumericExpression> comparator() {
		return comparator;
	}

	/**
	 * Returns the ideal number. If you want it to be Herbrand you need to cast
	 * it to the appropriate Herbrand type.
	 * 
	 * @param numberObject
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression number(NumberObject numberObject) {
		return idealFactory.number(numberObject);
	}

	/**
	 * Method that returns NumericSymbolicConstant.
	 * 
	 * @param name
	 * @param type
	 * 
	 * @return NumericSymbolicConstant
	 */
	@Override
	public NumericSymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		if (type.isIdeal())
			return idealFactory.symbolicConstant(name, type);
		else
			return herbrandFactory.symbolicConstant(name, type);
	}

	/**
	 * One of several methods that create a NumericExpression.
	 * 
	 * @param operator
	 * @param numericType
	 * @param arguments
	 * 	arguments is a Collection<SymbolicObject>
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arguments);
		else
			return herbrandFactory.expression(operator, numericType, arguments);
	}

	/**
	 * One of several methods that create a NumericExpression.
	 * 
	 * @param operator
	 * @param numericType
	 * @param arguments
	 * 	arguments is a SymbolicObject array
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arguments);
		else
			return herbrandFactory.expression(operator, numericType, arguments);
	}

	/**
	 * One of several methods that create a NumericExpression.
	 * 
	 * @param operator
	 * @param numericType
	 * @param arg0
	 * 	arg0 is a SymbolicObject
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arg0);
		else
			return herbrandFactory.expression(operator, numericType, arg0);
	}

	/**
	 * One of several methods that create a NumericExpression.
	 * 
	 * @param operator
	 * @param numericType
	 * @param arg0
	 * 	arg0 is a SymbolicObject
	 * @param arg1
	 * 	arg1 is a SymbolicObject
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		if (numericType.isIdeal())
			return idealFactory.expression(operator, numericType, arg0, arg1);
		else
			return herbrandFactory
					.expression(operator, numericType, arg0, arg1);
	}

	/**
	 * One of several methods that create a NumericExpression.
	 * 
	 * @param operator
	 * @param numericType
	 * @param arg0
	 * 	arg0 is a SymbolicObject
	 * @param arg1
	 * 	arg1 is a SymbolicObject
	 * @param arg2
	 * 	arg2 is a SymbolicObject
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that returns zeroInt NumericExpression.
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression zeroInt() {
		return idealFactory.zeroInt();
	}

	/**
	 * Method that returns zeroReal NumericExpression.
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression zeroReal() {
		return idealFactory.zeroReal();
	}

	/**
	 * Method that returns oneInt NumericExpression.
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression oneInt() {
		return idealFactory.oneInt();
	}

	/**
	 * Method that returns oneReal NumericExpression.
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression oneReal() {
		return idealFactory.oneReal();
	}

	/**
	 * Method that adds two NumericExpressions.
	 * 
	 * @param arg0
	 * 	arg0 is a NumericExpression
	 * @param arg1
	 * 	arg1 is a NumericExpression
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression add(NumericExpression arg0, NumericExpression arg1) {
		SymbolicType t0 = arg0.type(), t1 = arg1.type();

		if (t0.isIdeal() && t1.isIdeal())
			return idealFactory.add(arg0, arg1);
		else
			return herbrandFactory.add(castToHerbrand(arg0),
					castToHerbrand(arg1));
	}

	/**
	 * Method that subtracts a NumericExpression from another NumericExpression.
	 * 
	 * @param arg0
	 * 	arg0 is a NumericExpression
	 * @param arg1
	 * 	arg1 is a NumericExpression
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that multiplies two NumericExpressions.
	 * 
	 * @param arg0
	 * 	arg0 is a NumericExpression
	 * @param arg1
	 * 	arg1 is a NumericExpression
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that divides one numeric expression by another.
	 * 
	 * @param arg0
	 * 	arg0 is a NumericExpression
	 * @param arg1
	 * 	arg1 is a NumericExpression
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that returns modulo arg1 of arg0.
	 * 
	 * @param arg0
	 * 	arg0 is a NumericExpression
	 * @param arg1 is a NumericExpression
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that returns a NumericExpression minus arg.
	 * 
	 * @param arg
	 * 	arg is a numeric expression
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression minus(NumericExpression arg) {
		SymbolicType type = arg.type();

		if (type.isIdeal())
			return idealFactory.minus(arg);
		else
			return herbrandFactory.minus(castToHerbrand(arg));
	}

	/**
	 * One of two methods that returns base raised to exponent.
	 * 
	 * @param base
	 * @param exponent
	 * 	exponent is an IntObject
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		SymbolicType type = base.type();

		if (type.isIdeal())
			return idealFactory.power(base, exponent);
		else
			return herbrandFactory.power(castToHerbrand(base), exponent);
	}

	/**
	 * One of two methods that returns base raised to exponent.
	 * 
	 * @param base
	 * @param exponent
	 * 	exponent is a NumericExpression
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that casts expr to newType
	 * 
	 * @param expr
	 * @param newType
	 * 
	 * @return NumericExpression
	 */
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

	/**
	 * Method that extracts Number from expression.
	 * 
	 * @param expression
	 * 
	 * @return Number
	 */
	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression.type().isHerbrand())
			return herbrandFactory.extractNumber(expression);
		else if (expression.type().isIdeal())
			return idealFactory.extractNumber(expression);
		return null;
	}

	/**
	 * Method that returns BooleanExpression of arg0 < arg1
	 * 
	 * @param arg0
	 * @param arg1
	 * 
	 * @return BooleanExpression
	 */
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

	/**
	 * Method that returns BooleanExpression of arg0 <= arg1
	 * 
	 * @param arg0
	 * @param arg1
	 * 
	 * @return BooleanExpression
	 */
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

	/**
	 * Method that returns BooleanExpression of arg0 !< arg1
	 * 
	 * @param arg0
	 * @param arg1
	 * 
	 * @return BooleanExpression
	 */
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

	/**
	 * Method that returns BooleanExpression of arg0 !<= arg1
	 * 
	 * @param arg0
	 * @param arg1
	 * 
	 * @return BooleanExpression
	 */
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

	/**
	 * Method that returns BooleanExpression of equals arg0 arg1
	 * 
	 * @param arg0
	 * @param arg1
	 * 
	 * @return BooleanExpression
	 */
	@Override
	public BooleanExpression equals(NumericExpression arg0,
			NumericExpression arg1) {
		return idealFactory.equals(castToIdeal(arg0), castToIdeal(arg1));
	}

	/**
	 * Method that returns BooleanExpression of not equals arg0 arg1
	 * 
	 * @param arg0
	 * @param arg1
	 * 
	 * @return BooleanExpression
	 */
	@Override
	public BooleanExpression neq(NumericExpression arg0, NumericExpression arg1) {
		return idealFactory.neq(castToIdeal(arg0), castToIdeal(arg1));
	}
}

/**
 * 
 * CommonNumericComparator class implements Comparator<NumericExpression> interface
 * 
 * @author siegel
 *
 */
class CommonNumericComparator implements Comparator<NumericExpression> {
	private Comparator<NumericExpression> idealComparator;
	private Comparator<NumericExpression> herbrandComparator;

	/**
	 * Constructor that creates a CommonNumericComparator
	 * 
	 * @param idealComparator
	 * @param herbrandComparator
	 * 
	 * @return CommonNumericComparator
	 */
	CommonNumericComparator(Comparator<NumericExpression> idealComparator,
			Comparator<NumericExpression> herbrandComparator) {
		this.idealComparator = idealComparator;
		this.herbrandComparator = herbrandComparator;
	}

	/**
	 * Compare method for NumericExpressions.
	 * 
	 * @param o1
	 * 	o1 is a NumericExpression
	 * @param o2
	 * 	o2 is a NumericExpression
	 * 
	 * @return int
	 */
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
