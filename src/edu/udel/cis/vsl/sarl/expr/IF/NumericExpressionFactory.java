package edu.udel.cis.vsl.sarl.expr.IF;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public interface NumericExpressionFactory {

	void setObjectComparator(Comparator<SymbolicObject> c);
	
	void init();

	NumberFactoryIF numberFactory();

	ObjectFactory objectFactory();

	SymbolicTypeFactory typeFactory();

	CollectionFactory collectionFactory();

	Comparator<NumericExpression> numericComparator();

	SymbolicExpressionIF newConcreteNumericExpression(NumberObject numberObject);

	NumericSymbolicConstant newNumericSymbolicConstant(StringObject name,
			SymbolicTypeIF type);

	NumericExpression newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject[] arguments);

	NumericExpression newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0);

	NumericExpression newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0, SymbolicObject arg1);

	NumericExpression newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0,
			SymbolicObject arg1, SymbolicObject arg2);

	NumericExpression add(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression representing the sum of the given argument
	 * sequence.
	 * 
	 * @param args
	 *            a sequence of symbolic expressions of numeric type. They must
	 *            all have the same type.
	 * @return expression representing the sum
	 */
	NumericExpression add(SymbolicCollection args);

	/**
	 * Returns a symbolic expression which is the result of subtracting arg1
	 * from arg0. The two given expressions must have the same (numeric) type:
	 * either both integers, or both real.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0-arg1
	 */
	NumericExpression subtract(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression which is the result of multiplying the two
	 * given symbolic exprssions. The two given expressions must have the same
	 * (numeric) type: either both integers, or both real.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0 * arg1, the product of arg0 and arg1.
	 */
	NumericExpression multiply(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns symbolic expression representing the product of the given
	 * sequence of expressions.
	 * 
	 * @param args
	 *            symbolic expression sequence; all elements have the same
	 *            numeric type
	 * @return a symbolic expression representing the product
	 */
	NumericExpression multiply(SymbolicCollection args);

	/**
	 * Returns a symbolic expression which is the result of dividing arg0 by
	 * arg1. The two given expressions must have the same (numeric) type: either
	 * both integers, or both real. In the integer case, division is interpreted
	 * as "integer division", which rounds towards 0.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0 / arg1
	 */
	NumericExpression divide(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression which represents arg0 modulo arg1. The two
	 * given expressions must have the integer type. What happens for negative
	 * integers is unspecified.
	 * 
	 * @param arg0
	 *            a symbolic expression of integer type
	 * @param arg1
	 *            a symbolic expression of integer type
	 * @return arg0 % arg1
	 */
	NumericExpression modulo(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression which is the negative of the given
	 * numerical expression. The given expression must be non-null and have
	 * either integer or real type.
	 * 
	 * @param arg
	 *            a symbolic expression of integer or real type
	 * @return -arg
	 */
	NumericExpression minus(NumericExpression arg);

	/**
	 * Concrete power operator: e^b, where b is a concrete non-negative integer.
	 * This method might actually multiply out the expression, i.e., it does not
	 * necessarily return an expression with operator POWER.
	 * 
	 * @param base
	 *            the base expression in the power expression
	 * @param exponent
	 *            a non-negative concrete integer exponent
	 */
	NumericExpression power(NumericExpression base, IntObject exponent);

	/**
	 * General power operator: e^b. Both e and b are numeric expressions.
	 * 
	 * @param base
	 *            the base expression in the power expression
	 * @param exponent
	 *            the exponent in the power expression
	 */
	NumericExpression power(NumericExpression base, NumericExpression exponent);

	/** Casts a real or integer type expression to an expression of real type. */
	NumericExpression castToReal(NumericExpression numericExpression);

	/**
	 * Attempts to interpret the given symbolic expression as a concrete number.
	 * If this is not possible, returns null.
	 */
	NumberIF extractNumber(NumericExpression expression);

}
