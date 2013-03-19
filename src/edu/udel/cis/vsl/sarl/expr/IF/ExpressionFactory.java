package edu.udel.cis.vsl.sarl.expr.IF;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * An ExpressionFactory is used to instantiate instances of SymbolicExpression.
 * 
 * @author siegel
 * 
 */
public interface ExpressionFactory {

	/**
	 * Initialize this expression factory. This should be called before it is
	 * used.
	 */
	void init();

	// SimplifierFactory simplifierFactory();

	/**
	 * Returns the numeric expression factory used by this expression factory.
	 * 
	 * @return the numeric expression factory
	 */
	NumericExpressionFactory numericFactory();

	/**
	 * Returns the boolean expression factory used by this expression factory.
	 * 
	 * @return the boolean expression factory
	 */
	BooleanExpressionFactory booleanFactory();

	/**
	 * Returns the symbolic type factory used by this expression factory.
	 * 
	 * @return the symbolic type factory
	 */
	SymbolicTypeFactory typeFactory();

	/**
	 * Returns the object factory used by this expression factory.
	 * 
	 * @return the object factory
	 */
	ObjectFactory objectFactory();

	/**
	 * Returns a comparator on all SymbolicExpression objects. The comparator
	 * defines a total order on the set of symbolic expressions.
	 * 
	 * @return a comparator on symbolic expressions
	 */
	Comparator<SymbolicExpression> comparator();

	/**
	 * Returns an expression with the given operator type and argument sequence.
	 * 
	 * @param operator
	 *            a symbolic operator
	 * @param type
	 *            a symbolic type
	 * @param arguments
	 *            the arguments to the operator as an array
	 * @return the expression specified by above
	 */
	SymbolicExpression expression(SymbolicOperator operator, SymbolicType type,
			SymbolicObject[] arguments);

	SymbolicExpression expression(SymbolicOperator operator, SymbolicType type,
			SymbolicObject arg0);

	SymbolicExpression expression(SymbolicOperator operator, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1);

	SymbolicExpression expression(SymbolicOperator operator, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2);

	SymbolicExpression expression(SymbolicOperator operator, SymbolicType type,
			Collection<SymbolicObject> args);

	SymbolicConstant symbolicConstant(StringObject name, SymbolicType type);

	/**
	 * Returns the special expression "NULL", which has the NULL operator, null
	 * type, and no arguments.
	 * 
	 * @return the NULL symbolic expression
	 */
	SymbolicExpression nullExpression();

}
