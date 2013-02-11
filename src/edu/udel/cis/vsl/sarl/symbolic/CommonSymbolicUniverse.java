package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Collection;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.prove.SimplifierIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.collections.CollectionFactory;
import edu.udel.cis.vsl.sarl.object.ObjectComparator;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

/**
 * This class provides partial implementation of the SymbolicUniverseIF
 * interface. Generic implementations of methods "make" and "canonicalize" are
 * given.
 * 
 * @author siegel
 */
public class CommonSymbolicUniverse implements SymbolicUniverseIF {

	private ObjectFactory objectFactory;

	private SymbolicTypeFactory typeFactory;

	private NumberFactoryIF numberFactory;

	private CollectionFactory collectionFactory;

	private NumericExpressionFactory numericExpressionFactory;

	private ObjectComparator objectComparator;

	private SymbolicTypeIF booleanType, integerType, realType;

	private SymbolicExpressionIF trueExpr, falseExpr;

	private SymbolicExpressionIF zeroInt, zeroReal, oneInt, oneReal;

	public CommonSymbolicUniverse(
			NumericExpressionFactory numericExpressionFactory) {
		this.numericExpressionFactory = numericExpressionFactory;
		this.objectFactory = numericExpressionFactory.objectFactory();
		this.typeFactory = numericExpressionFactory.typeFactory();
		this.collectionFactory = numericExpressionFactory.collectionFactory();
		this.numberFactory = numericExpressionFactory.numberFactory();
		this.objectComparator = objectFactory
				.formComparators(numericExpressionFactory);
		this.booleanType = typeFactory.booleanType();
		this.integerType = typeFactory.integerType();
		this.realType = typeFactory.realType();
		this.trueExpr = expression(SymbolicOperator.CONCRETE, booleanType,
				objectFactory.trueObj());
		this.falseExpr = expression(SymbolicOperator.CONCRETE, booleanType,
				objectFactory.falseObj());
		zeroInt = expression(SymbolicOperator.CONCRETE, integerType,
				objectFactory.zeroIntObj());
		zeroReal = expression(SymbolicOperator.CONCRETE, realType,
				objectFactory.zeroRealObj());
		oneInt = expression(SymbolicOperator.CONCRETE, integerType,
				objectFactory.oneIntObj());
		oneReal = expression(SymbolicOperator.CONCRETE, realType,
				objectFactory.oneRealObj());
	}

	public void setNumericExpressionFactory(
			NumericExpressionFactory numericExpressionFactory) {
		this.numericExpressionFactory = numericExpressionFactory;
	}

	public NumericExpressionFactory numericExpressionFactory() {
		return numericExpressionFactory;
	}

	@Override
	public SymbolicObject canonic(SymbolicObject object) {
		return objectFactory.canonic(object);
	}

	protected SymbolicExpressionIF canonic(SymbolicExpressionIF expression) {
		return (SymbolicExpressionIF) objectFactory.canonic(expression);
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] arguments) {
		return canonic(new CommonSymbolicExpression(operator, type, arguments));
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0) {
		return canonic(new CommonSymbolicExpression(operator, type, arg0));
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1) {
		return canonic(new CommonSymbolicExpression(operator, type, arg0, arg1));
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return canonic(new CommonSymbolicExpression(operator, type, arg0, arg1,
				arg2));
	}

	protected SymbolicExpressionIF zero(SymbolicTypeIF type) {
		if (type.isInteger())
			return zeroInt();
		else if (type.isReal())
			return zeroReal();
		else
			throw new SARLInternalException("Expected type int or real, not "
					+ type);
	}

	protected SymbolicSet set(SymbolicExpressionIF x, SymbolicExpressionIF y) {
		return collectionFactory.singletonHashSet(x).add(y);
	}

	// Exported methods...

	/**
	 * For exists and forall, must provide an instance of
	 * SymbolicConstantExpressionIF as arg0. Cannot be applied to make concrete
	 * expressions or SymbolicConstantExpressionIF. There are separate methods
	 * for those.
	 */
	@Override
	public SymbolicExpressionIF make(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] args) {
		int numArgs = args.length;

		switch (operator) {
		case ADD: // 1 or 2 args
			if (numArgs == 1) // collection
				return add((SymbolicCollection) args[0]);
			else
				return add((SymbolicExpressionIF) args[0],
						(SymbolicExpressionIF) args[1]);
		case AND: // 1 or 2 args
			if (numArgs == 1) // collection
				return and((SymbolicCollection) args[0]);
			else
				return and((SymbolicExpressionIF) args[0],
						(SymbolicExpressionIF) args[1]);
		case APPLY: // 2 args: function and sequence
			return apply((SymbolicExpressionIF) args[0],
					(SymbolicSequence) args[1]);
		case ARRAY_LAMBDA:
			return arrayLambda((SymbolicCompleteArrayTypeIF) type,
					(SymbolicExpressionIF) args[0]);
		case ARRAY_READ:
			return arrayRead((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case ARRAY_WRITE:
			return arrayWrite((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1],
					(SymbolicExpressionIF) args[2]);
		case CAST:
			return castToReal((SymbolicExpressionIF) args[0]);
		case CONCRETE:
			if (type.isNumeric())
				return canonic(numericExpressionFactory
						.newConcreteNumericExpression((NumberObject) args[0]));
			else
				return expression(SymbolicOperator.CONCRETE, type, args[0]);
		case COND:
			return cond((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1],
					(SymbolicExpressionIF) args[2]);
		case DENSE_ARRAY_WRITE:
			return denseArrayWrite((SymbolicExpressionIF) args[0],
					(SymbolicSequence) args[1]);
		case DIVIDE:
			return divide((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case EQUALS:
			return equals((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case EXISTS:
			return exists((SymbolicConstantIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case FORALL:
			return forall((SymbolicConstantIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case INT_DIVIDE:
			return divide((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case LAMBDA:
			return lambda((SymbolicConstantIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case LENGTH:
			return length((SymbolicConstantIF) args[0]);
		case LESS_THAN:
			return lessThan((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case LESS_THAN_EQUALS:
			return lessThanEquals((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case MODULO:
			return modulo((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case MULTIPLY:
			if (numArgs == 1) // collection
				return multiply((SymbolicCollection) args[0]);
			else
				return multiply((SymbolicExpressionIF) args[0],
						(SymbolicExpressionIF) args[1]);
		case NEGATIVE:
			return minus((SymbolicExpressionIF) args[0]);
		case NEQ:
			return neq((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case NOT:
			return not((SymbolicExpressionIF) args[0]);
		case OR: {
			if (numArgs == 1) // collection
				return or((SymbolicCollection) args[0]);
			else
				return or((SymbolicExpressionIF) args[0],
						(SymbolicExpressionIF) args[1]);
		}
		case POWER: // exponent could be expression or int constant
			if (args[1] instanceof SymbolicExpressionIF)
				return power((SymbolicExpressionIF) args[0],
						(SymbolicExpressionIF) args[1]);
			else
				return power((SymbolicExpressionIF) args[0],
						(IntObject) args[1]);
		case SUBTRACT:
			return subtract((SymbolicExpressionIF) args[0],
					(SymbolicExpressionIF) args[1]);
		case SYMBOLIC_CONSTANT:
			return symbolicConstant((StringObject) args[0], type);
		case TUPLE_READ:
			return tupleRead((SymbolicExpressionIF) args[0],
					(IntObject) args[1]);
		case TUPLE_WRITE:
			return tupleWrite((SymbolicExpressionIF) args[0],
					(IntObject) args[1], (SymbolicExpressionIF) args[2]);
		case UNION_EXTRACT: {
			SymbolicExpressionIF expression = (SymbolicExpressionIF) args[1];
			SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) expression
					.type();

			return unionExtract(unionType, (IntObject) args[0], expression);
		}
		case UNION_INJECT: {
			SymbolicExpressionIF expression = (SymbolicExpressionIF) args[1];
			SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) type;

			return unionInject(unionType, (IntObject) args[0], expression);

		}
		case UNION_TEST: {
			SymbolicExpressionIF expression = (SymbolicExpressionIF) args[1];
			SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) expression
					.type();

			return unionTest(unionType, (IntObject) args[0], expression);
		}
		default:
			throw new IllegalArgumentException("Unknown expression kind: "
					+ operator);
		}
	}

	@Override
	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	@Override
	public SymbolicExpressionIF add(SymbolicCollection args) {
		int size = args.size();
		SymbolicExpressionIF result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpressionIF arg : args) {
			if (result == null)
				result = arg;
			else
				result = add(result, arg);
		}
		return result;
	}

	/**
	 * Cannot assume anything about the collection of arguments. Therefore just
	 * apply the binary and operator to them in order.
	 */
	@Override
	public SymbolicExpressionIF and(SymbolicCollection args) {
		SymbolicExpressionIF result = trueExpr;

		for (SymbolicExpressionIF arg : args)
			result = and(result, arg);
		return result;
	}

	/**
	 * Assumes the given arguments are in CNF form and produces the conjunction
	 * of the two.
	 * 
	 * CNF form: true | false | AND set | e
	 * 
	 * where set is a set of boolean expressions which are not true, false, or
	 * AND expressions and set has cardinality at least 2. e is any boolean
	 * expression not a true, false, or AND expression. Strategy: eliminate the
	 * true and false cases in the obvious way. Then
	 * 
	 * <pre>
	 * AND s1, AND s2 -> AND union(s1,s2)
	 * AND s1, e -> AND add(s1, e)
	 * AND e1, e2-> if e1.equals(e2) then e1 else AND {e1,e2}
	 * </pre>
	 */
	@Override
	public SymbolicExpressionIF and(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		if (arg0 == trueExpr)
			return arg1;
		if (arg1 == trueExpr)
			return arg0;
		if (arg0 == falseExpr || arg1 == falseExpr)
			return falseExpr;
		if (arg0.equals(arg1))
			return arg0;
		else {
			boolean isAnd0 = arg0.operator() == SymbolicOperator.AND;
			boolean isAnd1 = arg1.operator() == SymbolicOperator.AND;

			if (isAnd0 && isAnd1)
				return expression(SymbolicOperator.AND, booleanType,
						((SymbolicSet) arg0.argument(0))
								.addAll((SymbolicSet) arg1.argument(0)));
			if (isAnd0 && !isAnd1)
				return expression(SymbolicOperator.AND, booleanType,
						((SymbolicSet) arg0.argument(0)).add(arg1));
			if (!isAnd0 && isAnd1)
				return expression(SymbolicOperator.AND, booleanType,
						((SymbolicSet) arg1.argument(0)).add(arg0));
			return expression(SymbolicOperator.AND, booleanType,
					set(arg0, arg1));
		}
	}

	@Override
	public SymbolicTypeIF booleanType() {
		return booleanType;
	}

	@Override
	public SymbolicTypeIF integerType() {
		return integerType;
	}

	@Override
	public SymbolicTypeIF realType() {
		return realType;
	}

	@Override
	public SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		return typeFactory.arrayType(elementType, extent);
	}

	@Override
	public SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType) {
		return typeFactory.arrayType(elementType);
	}

	@Override
	public SymbolicTypeSequenceIF typeSequence(SymbolicTypeIF[] types) {
		return typeFactory.sequence(types);
	}

	@Override
	public SymbolicTypeSequenceIF typeSequence(Iterable<SymbolicTypeIF> types) {
		return typeFactory.sequence(types);
	}

	@Override
	public SymbolicTupleTypeIF tupleType(StringObject name,
			SymbolicTypeSequenceIF fieldTypes) {
		return typeFactory.tupleType(name, fieldTypes);
	}

	@Override
	public SymbolicFunctionTypeIF functionType(
			SymbolicTypeSequenceIF inputTypes, SymbolicTypeIF outputType) {
		return typeFactory.functionType(inputTypes, outputType);
	}

	@Override
	public SymbolicUnionTypeIF unionType(StringObject name,
			SymbolicTypeSequenceIF memberTypes) {
		return typeFactory.unionType(name, memberTypes);
	}

	@Override
	public int numObjects() {
		return objectFactory.numObjects();
	}

	@Override
	public SymbolicObject objectWithId(int index) {
		return objectFactory.objectWithId(index);
	}

	@Override
	public Collection<SymbolicObject> objects() {
		return objectFactory.objects();
	}

	@Override
	public SimplifierIF simplifier(SymbolicExpressionIF assumption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanObject booleanObject(boolean value) {
		return objectFactory.booleanObject(value);
	}

	@Override
	public IntObject intObject(int value) {
		return objectFactory.intObject(value);
	}

	@Override
	public NumberObject numberObject(NumberIF value) {
		return objectFactory.numberObject(value);
	}

	@Override
	public StringObject stringObject(String string) {
		return objectFactory.stringObject(string);
	}

	@Override
	public SymbolicConstantIF symbolicConstant(StringObject name,
			SymbolicTypeIF type) {
		SymbolicConstantIF result;

		if (type.isNumeric())
			result = numericExpressionFactory.newNumericSymbolicConstant(name,
					type);
		else
			result = new CommonSymbolicConstant(name, type);
		return result;
	}

	@Override
	public SymbolicConstantIF extractSymbolicConstant(
			SymbolicExpressionIF expression) {
		if (expression instanceof SymbolicConstantIF)
			return (SymbolicConstantIF) expression;
		return null;
	}

	@Override
	public SymbolicExpressionIF substitute(SymbolicExpressionIF expression,
			Map<SymbolicConstantIF, SymbolicExpressionIF> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF symbolic(NumberObject numberObject) {
		return numericExpressionFactory
				.newConcreteNumericExpression(numberObject);
	}

	@Override
	public SymbolicExpressionIF symbolic(int value) {
		return symbolic(numberObject(numberFactory.integer(value)));
	}

	@Override
	public SymbolicExpressionIF zeroInt() {
		return zeroInt;
	}

	@Override
	public SymbolicExpressionIF zeroReal() {
		return zeroReal;
	}

	@Override
	public SymbolicExpressionIF oneInt() {
		return oneInt;
	}

	@Override
	public SymbolicExpressionIF oneReal() {
		return oneReal;
	}

	@Override
	public SymbolicExpressionIF add(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF subtract(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF multiply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF multiply(SymbolicCollection args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF divide(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF modulo(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF minus(SymbolicExpressionIF arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF power(SymbolicExpressionIF base,
			IntObject exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF castToReal(
			SymbolicExpressionIF numericExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumberIF extractNumber(SymbolicExpressionIF expression) {
		if (expression.operator() == SymbolicOperator.CONCRETE) {
			SymbolicObject object = expression.argument(0);

			if (object.symbolicObjectKind() == SymbolicObjectKind.NUMBER)
				return ((NumberObject) object).getNumber();
		}
		return null;
	}

	@Override
	public SymbolicExpressionIF symbolic(BooleanObject object) {
		return expression(SymbolicOperator.CONCRETE, booleanType, object);
	}

	@Override
	public SymbolicExpressionIF symbolic(boolean value) {
		return symbolic(booleanObject(value));
	}

	/**
	 * Assume both args are in CNF normal form:
	 * 
	 * arg: true | false | AND set1 | OR set2 | e
	 * 
	 * Strategy: get rid of true false cases as usual. Then:
	 * 
	 * <pre>
	 * or(AND set, X) = and(s in set) or(s,X)
	 * or(X, AND set) = and(s in set) or(X,s)
	 * or(OR set0, OR set1) = OR(union(set0, set1))
	 * or(OR set, e) = OR(add(set, e))
	 * or(e, OR set) = OR(add(set, e))
	 * or(e1, e2) = OR(set(e1,e2))
	 * </pre>
	 * 
	 * where X is an AND, OR or e expression; set0 and set1 are sets of e
	 * expressions.
	 */
	@Override
	public SymbolicExpressionIF or(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		if (arg0 == trueExpr || arg1 == trueExpr)
			return trueExpr;
		if (arg0 == falseExpr)
			return arg1;
		if (arg1 == falseExpr)
			return arg0;
		if (arg0.equals(arg1))
			return arg0;
		else {
			SymbolicOperator op0 = arg0.operator();
			SymbolicOperator op1 = arg1.operator();

			if (op0 == SymbolicOperator.AND) {
				SymbolicExpressionIF result = falseExpr;

				for (SymbolicExpressionIF clause : (SymbolicSet) arg0
						.argument(0))
					result = or(result, and(clause, arg1));
				return result;
			}
			if (op1 == SymbolicOperator.AND) {
				SymbolicExpressionIF result = falseExpr;

				for (SymbolicExpressionIF clause : (SymbolicSet) arg1
						.argument(0))
					result = or(result, and(arg0, clause));
				return result;
			}
			if (op0 == SymbolicOperator.OR && op1 == SymbolicOperator.OR)
				return expression(op0, booleanType,
						((SymbolicSet) arg0.argument(0))
								.addAll((SymbolicSet) arg1.argument(0)));
			if (op0 == SymbolicOperator.OR)
				return expression(op0, booleanType,
						((SymbolicSet) arg0.argument(0)).add(arg1));
			if (op1 == SymbolicOperator.OR)
				return expression(op1, booleanType,
						((SymbolicSet) arg1.argument(0)).add(arg0));
			return expression(SymbolicOperator.OR, booleanType, set(arg0, arg1));
		}
	}

	/**
	 * Assume nothing about the list of args.
	 */
	@Override
	public SymbolicExpressionIF or(SymbolicCollection args) {
		SymbolicExpressionIF result = falseExpr;

		for (SymbolicExpressionIF arg : args)
			result = or(result, arg);
		return result;
	}

	/**
	 * <pre>
	 * expr       : AND set<or> | or
	 * or         : OR set<basic> | basic
	 * basic      : literal | quantifier | relational
	 * literal    : booleanPrimitive | ! booleanPrimitive
	 * quantifier : q[symbolicConstant].expr
	 * q          : forall | exists
	 * relational : 0<e | 0=e | 0<=e | 0!=e
	 * </pre>
	 * 
	 * Note: a booleanPrimitive is any boolean expression that doesn't fall into
	 * one of the other categories above.
	 * 
	 * <pre>
	 * not(AND set) => or(s in set) not(s)
	 * not(or set) => and(s in set) not(s)
	 * not(!e) => e
	 * not(forall x.e) => exists x.not(e)
	 * not(exists x.e) => forall x.not(e)
	 * not(0<e) => 0<=-e
	 * not(0=e) => 0!=e
	 * not(0!=e) => 0=e
	 * not(0<=e) => 0<-e
	 * not(booleanPrimitive) = !booleanPrimitive
	 * </pre>
	 */
	@Override
	public SymbolicExpressionIF not(SymbolicExpressionIF arg) {
		SymbolicOperator operator = arg.operator();

		switch (operator) {
		case AND: {
			SymbolicExpressionIF result = falseExpr;

			for (SymbolicExpressionIF clause : (SymbolicSet) arg.argument(0))
				result = or(result, not(clause));
			return result;
		}
		case OR: {
			SymbolicExpressionIF result = trueExpr;

			for (SymbolicExpressionIF clause : (SymbolicSet) arg.argument(0))
				result = and(result, not(clause));
			return result;
		}
		case NOT:
			return (SymbolicExpressionIF) arg.argument(0);
		case FORALL:
			return expression(SymbolicOperator.EXISTS, booleanType,
					(SymbolicConstantIF) arg.argument(0),
					not((SymbolicExpressionIF) arg.argument(1)));
		case EXISTS:
			return expression(SymbolicOperator.FORALL, booleanType,
					(SymbolicConstantIF) arg.argument(0),
					not((SymbolicExpressionIF) arg.argument(1)));
		case LESS_THAN:
			return expression(SymbolicOperator.LESS_THAN_EQUALS, booleanType,
					(SymbolicExpressionIF) arg.argument(0),
					minus((SymbolicExpressionIF) arg.argument(1)));
		case EQUALS:
			return expression(SymbolicOperator.NEQ, booleanType,
					(SymbolicExpressionIF) arg.argument(0),
					(SymbolicExpressionIF) arg.argument(1));
		case LESS_THAN_EQUALS:
			return expression(SymbolicOperator.LESS_THAN, booleanType,
					(SymbolicExpressionIF) arg.argument(0),
					minus((SymbolicExpressionIF) arg.argument(1)));
		case NEQ:
			return expression(SymbolicOperator.EQUALS, booleanType,
					(SymbolicExpressionIF) arg.argument(0),
					(SymbolicExpressionIF) arg.argument(1));
		default:
			return expression(SymbolicOperator.NOT, booleanType, arg);
		}
	}

	/**
	 * a<b => 0<b-a.
	 */
	@Override
	public SymbolicExpressionIF lessThan(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicExpressionIF difference = subtract(arg1, arg0);
		NumberIF number = extractNumber(difference);

		if (number == null)
			return expression(SymbolicOperator.LESS_THAN, booleanType,
					zero(arg0.type()), difference);
		else
			return number.signum() > 0 ? trueExpr : falseExpr;
	}

	/**
	 * a<=b => 0<=b-a.
	 */
	@Override
	public SymbolicExpressionIF lessThanEquals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicExpressionIF difference = subtract(arg1, arg0);
		NumberIF number = extractNumber(difference);

		if (number == null)
			return expression(SymbolicOperator.LESS_THAN_EQUALS, booleanType,
					zero(arg0.type()), difference);
		else
			return number.signum() >= 0 ? trueExpr : falseExpr;
	}

	@Override
	public SymbolicExpressionIF equals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicExpressionIF difference = subtract(arg1, arg0);
		NumberIF number = extractNumber(difference);

		if (number == null)
			return expression(SymbolicOperator.EQUALS, booleanType,
					zero(arg0.type()), difference);
		else
			return number.isZero() ? trueExpr : falseExpr;
	}

	@Override
	public SymbolicExpressionIF neq(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicExpressionIF difference = subtract(arg1, arg0);
		NumberIF number = extractNumber(difference);

		if (number == null)
			return expression(SymbolicOperator.NEQ, booleanType,
					zero(arg0.type()), difference);
		else
			return number.isZero() ? falseExpr : trueExpr;
	}

	/**
	 * We are assuming that each type has a nonempty domain.
	 * 
	 * <pre>
	 * forall x.true => true
	 * forall x.false => false
	 * forall x.(p && q) => (forall x.p) && (forall x.q)
	 * </pre>
	 * 
	 * TODO: forall i.(0<i-a && 0<b-i -> e) => and...
	 */
	@Override
	public SymbolicExpressionIF forall(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate) {
		if (predicate == trueExpr)
			return trueExpr;
		if (predicate == falseExpr)
			return falseExpr;
		if (predicate.operator() == SymbolicOperator.AND) {
			SymbolicExpressionIF result = trueExpr;

			for (SymbolicExpressionIF clause : (SymbolicSet) predicate
					.argument(0))
				result = and(result, forall(boundVariable, clause));
			return result;
		}
		return expression(SymbolicOperator.FORALL, booleanType, boundVariable,
				predicate);
	}

	@Override
	public SymbolicExpressionIF exists(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate) {
		if (predicate == trueExpr)
			return trueExpr;
		if (predicate == falseExpr)
			return falseExpr;
		if (predicate.operator() == SymbolicOperator.OR) {
			SymbolicExpressionIF result = falseExpr;

			for (SymbolicExpressionIF clause : (SymbolicSet) predicate
					.argument(0))
				result = or(result, exists(boundVariable, clause));
			return result;
		}
		return expression(SymbolicOperator.EXISTS, booleanType, boundVariable,
				predicate);
	}

	@Override
	public Boolean extractBoolean(SymbolicExpressionIF expression) {
		if (expression == trueExpr)
			return true;
		if (expression == falseExpr)
			return false;
		return null;
	}

	@Override
	public SymbolicExpressionIF lambda(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF expression) {
		return expression(
				SymbolicOperator.LAMBDA,
				functionType(
						typeFactory.singletonSequence(boundVariable.type()),
						expression.type()), boundVariable, expression);
	}

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF function,
			SymbolicSequence argumentSequence) {
		// TODO: check types!
		return expression(SymbolicOperator.APPLY,
				((SymbolicFunctionTypeIF) function.type()).outputType(),
				function, argumentSequence);
	}

	@Override
	public SymbolicExpressionIF unionInject(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF unionTest(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF unionExtract(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Need to know type of elements in case empty. We do not check element
	 * types, but maybe we should.
	 */
	@Override
	public SymbolicExpressionIF array(SymbolicTypeIF elementType,
			SymbolicSequence elements) {
		return expression(SymbolicOperator.CONCRETE,
				arrayType(elementType, symbolic(elements.size())), elements);
	}

	@Override
	public SymbolicExpressionIF length(SymbolicExpressionIF array) {
		SymbolicArrayTypeIF type = (SymbolicArrayTypeIF) array.type();

		if (type.isComplete())
			return ((SymbolicCompleteArrayTypeIF) type).extent();
		else
			return expression(SymbolicOperator.LENGTH, integerType, array);
	}

	@Override
	public SymbolicExpressionIF arrayRead(SymbolicExpressionIF array,
			SymbolicExpressionIF index) {
		// TODO Auto-generated method stub
		// do the fancy stuff to try to read????
		return null;
	}

	@Override
	public SymbolicExpressionIF arrayWrite(SymbolicExpressionIF array,
			SymbolicExpressionIF index, SymbolicExpressionIF value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF denseArrayWrite(SymbolicExpressionIF array,
			SymbolicSequence values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF arrayLambda(
			SymbolicCompleteArrayTypeIF arrayType, SymbolicExpressionIF function) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF tuple(SymbolicTupleTypeIF type,
			SymbolicSequence components) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF tupleRead(SymbolicExpressionIF tuple,
			IntObject index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF tupleWrite(SymbolicExpressionIF tuple,
			IntObject index, SymbolicExpressionIF value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF cast(SymbolicTypeIF newType,
			SymbolicExpressionIF expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF cond(SymbolicExpressionIF predicate,
			SymbolicExpressionIF trueValue, SymbolicExpressionIF falseValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectComparator comparator() {
		return objectComparator;
	}

}
