package edu.udel.cis.vsl.sarl.universe.common;

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
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.prove.SimplifierIF;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.ObjectComparator;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.util.SingletonMap;

/**
 * This class provides partial implementation of the SymbolicUniverseIF
 * interface. Generic implementations of methods "make" and "canonicalize" are
 * given.
 * 
 * @author siegel
 */
public class CommonSymbolicUniverse implements SymbolicUniverseIF {

	/**
	 * A sequence of array writes in which the index never exceeds this bound
	 * will be represented in a dense format, i.e., like a regular Java array.
	 */
	public final static int DENSE_ARRAY_MAX_SIZE = 100000;

	/**
	 * A forall or exists expression over an integer range will be expanded to a
	 * conjunction or disjunction as long as the the size of the range
	 * (high-low) does not exceed this bound.
	 */
	public final static int QUANTIFIER_EXPAND_BOUND = 1000;

	private IntegerNumberIF denseArrayMaxSize, quantifierExpandBound;

	// private FactorySystem system;

	private ObjectFactory objectFactory;

	private SymbolicTypeFactory typeFactory;

	private ExpressionFactory expressionFactory;

	private NumberFactoryIF numberFactory;

	private CollectionFactory collectionFactory;

	private NumericExpressionFactory numericExpressionFactory;

	private ObjectComparator objectComparator;

	private TheoremProverIF prover;

	private SymbolicTypeIF booleanType, integerType, realType;

	private SymbolicExpressionIF nullExpression;

	private SymbolicExpressionIF trueExpr, falseExpr;

	private SymbolicExpressionIF zeroInt, zeroReal, oneInt, oneReal;

	public CommonSymbolicUniverse(FactorySystem system) {
		// this.system = system;
		this.objectFactory = system.objectFactory();
		this.typeFactory = system.typeFactory();
		this.expressionFactory = system.expressionFactory();
		this.collectionFactory = system.collectionFactory();
		this.numericExpressionFactory = expressionFactory.numericFactory();
		this.numberFactory = numericExpressionFactory.numberFactory();
		this.objectComparator = objectFactory.comparator();
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
		denseArrayMaxSize = numberFactory.integer(DENSE_ARRAY_MAX_SIZE);
		quantifierExpandBound = numberFactory.integer(QUANTIFIER_EXPAND_BOUND);
		nullExpression = expressionFactory.nullExpression();
		prover = Prove.newIdealCVC3HybridProver(this);
	}

	public NumericExpressionFactory numericExpressionFactory() {
		return numericExpressionFactory;
	}

	@Override
	public SymbolicObject canonic(SymbolicObject object) {
		return objectFactory.canonic(object);
	}

	protected SymbolicExpressionIF canonic(SymbolicExpressionIF expression) {
		return expressionFactory.canonic(expression);
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] arguments) {
		return expressionFactory.expression(operator, type, arguments);
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0) {
		return expressionFactory.expression(operator, type, arg0);
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1) {
		return expressionFactory.expression(operator, type, arg0, arg1);
	}

	protected SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return expressionFactory.expression(operator, type, arg0, arg1, arg2);
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

			return unionExtract((IntObject) args[0], expression);
		}
		case UNION_INJECT: {
			SymbolicExpressionIF expression = (SymbolicExpressionIF) args[1];
			SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) type;

			return unionInject(unionType, (IntObject) args[0], expression);

		}
		case UNION_TEST: {
			SymbolicExpressionIF expression = (SymbolicExpressionIF) args[1];

			return unionTest((IntObject) args[0], expression);
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
		// TODO: until we port the better one
		return new IdentitySimplifier(this, assumption);
	}

	@Override
	public TheoremProverIF prover() {
		return prover;
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
			result = expressionFactory.symbolicConstant(name, type);
		return result;
	}

	@Override
	public SymbolicConstantIF extractSymbolicConstant(
			SymbolicExpressionIF expression) {
		if (expression instanceof SymbolicConstantIF)
			return (SymbolicConstantIF) expression;
		return null;
	}

	public SymbolicExpressionIF substitute(SymbolicExpressionIF expression,
			SymbolicConstantIF variable, SymbolicExpressionIF value) {
		return substitute(expression,
				new SingletonMap<SymbolicConstantIF, SymbolicExpressionIF>(
						variable, value));
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
		return numericExpressionFactory.add((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpressionIF subtract(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return numericExpressionFactory.subtract((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpressionIF multiply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return numericExpressionFactory.multiply((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpressionIF multiply(SymbolicCollection args) {
		int size = args.size();
		SymbolicExpressionIF result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpressionIF arg : args) {
			if (result == null)
				result = arg;
			else
				result = multiply(result, arg);
		}
		return result;
	}

	@Override
	public SymbolicExpressionIF divide(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return numericExpressionFactory.divide((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpressionIF modulo(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return numericExpressionFactory.modulo((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpressionIF minus(SymbolicExpressionIF arg) {
		return numericExpressionFactory.minus((NumericExpression) arg);
	}

	@Override
	public SymbolicExpressionIF power(SymbolicExpressionIF base,
			IntObject exponent) {
		return numericExpressionFactory.power((NumericExpression) base,
				exponent);
	}

	@Override
	public SymbolicExpressionIF power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent) {
		return numericExpressionFactory.power((NumericExpression) base,
				(NumericExpression) exponent);
	}

	@Override
	public SymbolicExpressionIF castToReal(
			SymbolicExpressionIF numericExpression) {
		return numericExpressionFactory
				.castToReal((NumericExpression) numericExpression);
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

	public SymbolicExpressionIF implies(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return or(not(arg0), arg1);
	}

	public SymbolicExpressionIF equiv(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return and(implies(arg0, arg1), implies(arg1, arg0));
	}

	public SymbolicExpressionIF forallIntConcrete(SymbolicConstantIF index,
			IntegerNumberIF low, IntegerNumberIF high,
			SymbolicExpressionIF predicate) {
		SymbolicExpressionIF result = trueExpr;

		for (IntegerNumberIF i = low; i.compareTo(high) < 0; i = numberFactory
				.increment(i)) {
			SymbolicExpressionIF iExpression = symbolic(numberObject(i));
			SymbolicExpressionIF substitutedPredicate = substitute(predicate,
					index, iExpression);

			result = and(result, substitutedPredicate);
		}
		return result;
	}

	@Override
	public SymbolicExpressionIF forallInt(SymbolicConstantIF index,
			SymbolicExpressionIF low, SymbolicExpressionIF high,
			SymbolicExpressionIF predicate) {
		IntegerNumberIF lowNumber = (IntegerNumberIF) extractNumber(low);

		if (lowNumber != null) {
			IntegerNumberIF highNumber = (IntegerNumberIF) extractNumber(high);

			if (highNumber != null
					&& numberFactory.subtract(highNumber, lowNumber).compareTo(
							quantifierExpandBound) <= 0) {
				return forallIntConcrete(index, lowNumber, highNumber,
						predicate);
			}
		}
		return forall(
				index,
				implies(and(lessThanEquals(low, index), lessThan(index, high)),
						predicate));
	}

	public SymbolicExpressionIF existsIntConcrete(SymbolicConstantIF index,
			IntegerNumberIF low, IntegerNumberIF high,
			SymbolicExpressionIF predicate) {
		SymbolicExpressionIF result = falseExpr;

		for (IntegerNumberIF i = low; i.compareTo(high) < 0; i = numberFactory
				.increment(i)) {
			SymbolicExpressionIF iExpression = symbolic(numberObject(i));
			SymbolicExpressionIF substitutedPredicate = substitute(predicate,
					index, iExpression);

			result = or(result, substitutedPredicate);
		}
		return result;
	}

	@Override
	public SymbolicExpressionIF existsInt(SymbolicConstantIF index,
			SymbolicExpressionIF low, SymbolicExpressionIF high,
			SymbolicExpressionIF predicate) {
		IntegerNumberIF lowNumber = (IntegerNumberIF) extractNumber(low);

		if (lowNumber != null) {
			IntegerNumberIF highNumber = (IntegerNumberIF) extractNumber(high);

			if (highNumber != null
					&& numberFactory.subtract(highNumber, lowNumber).compareTo(
							quantifierExpandBound) <= 0) {
				return existsIntConcrete(index, lowNumber, highNumber,
						predicate);
			}
		}
		return exists(
				index,
				implies(and(lessThanEquals(low, index), lessThan(index, high)),
						predicate));
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

	// private SymbolicExpressionIF equals(SymbolicTypeSequenceIF seq0,
	// SymbolicTypeSequenceIF seq1) {
	// return equals(seq0, seq1, 0);
	// }

	private SymbolicExpressionIF equals(SymbolicTypeSequenceIF seq0,
			SymbolicTypeSequenceIF seq1, int nestingDepth) {
		int size = seq0.numTypes();

		if (size != seq1.numTypes())
			return falseExpr;
		if (size == 0)
			return trueExpr;
		else {
			SymbolicExpressionIF result = equals(seq0.getType(0),
					seq1.getType(1), nestingDepth);

			if (size > 1)
				for (int i = 1; i < size; i++)
					result = and(
							result,
							equals(seq0.getType(i), seq1.getType(i),
									nestingDepth));
			return result;
		}
	}

	/**
	 * Returns a boolean expression which holds iff the two types are equal.
	 * 
	 * @param type0
	 * @param type1
	 * @return
	 */
	public SymbolicExpressionIF equals(SymbolicTypeIF type0,
			SymbolicTypeIF type1) {
		return equals(type0, type1, 0);
	}

	/**
	 * Returns a boolean expression which holds iff the two types are equal.
	 * 
	 * @param type0
	 * @param type1
	 * @return
	 */
	private SymbolicExpressionIF equals(SymbolicTypeIF type0,
			SymbolicTypeIF type1, int nestingDepth) {
		if (type0.equals(type1))
			return trueExpr;

		SymbolicTypeKind kind = type0.typeKind();

		if (kind != type1.typeKind())
			return falseExpr;
		switch (kind) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			return trueExpr;
		case ARRAY: {
			SymbolicArrayTypeIF a0 = (SymbolicArrayTypeIF) type0;
			SymbolicArrayTypeIF a1 = (SymbolicArrayTypeIF) type1;

			if (a0.isComplete() != a1.isComplete())
				return falseExpr;
			else {
				SymbolicExpressionIF result = equals(a0.elementType(),
						a1.elementType(), nestingDepth);

				if (a0.isComplete())
					result = and(
							result,
							equals(((SymbolicCompleteArrayTypeIF) a0).extent(),
									((SymbolicCompleteArrayTypeIF) a1).extent(),
									nestingDepth));
				return result;
			}
		}
		case FUNCTION:
			return and(
					equals(((SymbolicFunctionTypeIF) type0).inputTypes(),
							((SymbolicFunctionTypeIF) type1).inputTypes(),
							nestingDepth),
					equals(((SymbolicFunctionTypeIF) type0).outputType(),
							((SymbolicFunctionTypeIF) type1).outputType(),
							nestingDepth));
		case TUPLE: {
			SymbolicTupleTypeIF t0 = (SymbolicTupleTypeIF) type0;
			SymbolicTupleTypeIF t1 = (SymbolicTupleTypeIF) type1;

			if (!t0.name().equals(t1.name()))
				return falseExpr;
			return equals(t0.sequence(), t1.sequence(), nestingDepth);
		}
		case UNION: {
			SymbolicUnionTypeIF t0 = (SymbolicUnionTypeIF) type0;
			SymbolicUnionTypeIF t1 = (SymbolicUnionTypeIF) type1;

			if (!t0.name().equals(t1.name()))
				return falseExpr;
			return equals(t0.sequence(), t1.sequence(), nestingDepth);
		}
		default:
			throw new SARLInternalException("unreachable");
		}

	}

	@Override
	public SymbolicExpressionIF equals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return equals(arg0, arg1, 0);
	}

	private SymbolicConstantIF boundVar(int index, SymbolicTypeIF type) {
		return symbolicConstant(stringObject("x" + index), type);
	}

	private SymbolicConstantIF intBoundVar(int index) {
		return symbolicConstant(stringObject("i" + index), integerType);
	}

	private SymbolicExpressionIF equals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1, int quantifierDepth) {
		if (arg0.equals(arg1))
			return trueExpr;

		SymbolicTypeIF type = arg0.type();
		SymbolicExpressionIF result = equals(type, arg1.type(), quantifierDepth);

		if (result.equals(falseExpr))
			return result;
		switch (type.typeKind()) {
		case BOOLEAN:
			return equiv(arg0, arg1);
		case INTEGER:
		case REAL: {
			SymbolicExpressionIF difference = subtract(arg1, arg0);
			NumberIF number = extractNumber(difference);

			if (number == null)
				return expression(SymbolicOperator.EQUALS, booleanType,
						zero(arg0.type()), difference);
			else
				return number.isZero() ? trueExpr : falseExpr;
		}
		case ARRAY: {
			SymbolicExpressionIF length = length(arg0);

			if (!((SymbolicArrayTypeIF) type).isComplete())
				result = and(result,
						equals(length, length(arg1), quantifierDepth));
			if (result.isFalse())
				return result;
			else {
				SymbolicConstantIF index = intBoundVar(quantifierDepth);

				result = and(
						result,
						forallInt(
								index,
								zeroInt,
								length,
								equals(arrayRead(arg0, index),
										arrayRead(arg1, index),
										quantifierDepth + 1)));
				return result;
			}
		}
		case FUNCTION: {
			SymbolicTypeSequenceIF inputTypes = ((SymbolicFunctionTypeIF) type)
					.inputTypes();
			int numInputs = inputTypes.numTypes();

			if (numInputs == 0) {
				result = and(
						result,
						expression(SymbolicOperator.EQUALS, booleanType, arg0,
								arg1));
			} else {
				SymbolicConstantIF[] boundVariables = new SymbolicConstantIF[numInputs];
				SymbolicSequence sequence;
				SymbolicExpressionIF expr;

				for (int i = 0; i < numInputs; i++)
					boundVariables[i] = boundVar(quantifierDepth + i,
							inputTypes.getType(i));
				sequence = collectionFactory.sequence(boundVariables);
				expr = equals(apply(arg0, sequence), apply(arg1, sequence),
						quantifierDepth + numInputs);
				for (int i = numInputs - 1; i >= 0; i--)
					expr = forall(boundVariables[i], expr);
				result = and(result, expr);
				return result;
			}
		}
		case TUPLE: {
			int numComponents = ((SymbolicTupleTypeIF) type).sequence()
					.numTypes();

			for (int i = 0; i < numComponents; i++) {
				IntObject index = intObject(i);

				result = and(
						result,
						equals(tupleRead(arg0, index), tupleRead(arg1, index),
								quantifierDepth));
			}
			return result;
		}
		case UNION: {
			SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) type;

			if (arg0.operator() == SymbolicOperator.UNION_INJECT) {
				IntObject index = (IntObject) arg0.argument(0);
				SymbolicExpressionIF value0 = (SymbolicExpressionIF) arg0
						.argument(1);

				if (arg1.operator() == SymbolicOperator.UNION_INJECT)
					return index.equals(arg1.argument(0)) ? and(
							result,
							equals(value0,
									(SymbolicExpressionIF) arg1.argument(1),
									quantifierDepth)) : falseExpr;
				else
					return and(
							result,
							and(unionTest(index, arg1),
									equals(value0, unionExtract(index, arg1),
											quantifierDepth)));
			} else if (arg1.operator() == SymbolicOperator.UNION_INJECT) {
				IntObject index = (IntObject) arg1.argument(0);

				return and(
						result,
						and(unionTest(index, arg0),
								equals((SymbolicExpressionIF) arg1.argument(1),
										unionExtract(index, arg0),
										quantifierDepth)));
			} else {
				int numTypes = unionType.sequence().numTypes();
				SymbolicExpressionIF expr = falseExpr;

				for (int i = 0; i < numTypes; i++) {
					IntObject index = intObject(i);
					SymbolicExpressionIF clause = result;

					clause = and(clause, unionTest(index, arg0));
					if (clause.isFalse())
						continue;
					clause = and(clause, unionTest(index, arg1));
					if (clause.isFalse())
						continue;
					clause = and(
							clause,
							equals(unionExtract(index, arg0),
									unionExtract(index, arg1), quantifierDepth));
					if (clause.isFalse())
						continue;
					expr = or(expr, clause);
				}
				return expr;
			}
		}
		default:
			throw new SARLInternalException("Unknown type: " + type);
		}
	}

	@Override
	public SymbolicExpressionIF neq(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return not(equals(arg0, arg1));
	}

	/**
	 * We are assuming that each type has a nonempty domain.
	 * 
	 * <pre>
	 * forall x.true => true
	 * forall x.false => false
	 * forall x.(p && q) => (forall x.p) && (forall x.q)
	 * </pre>
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
		SymbolicOperator op0 = function.operator();
		SymbolicExpressionIF result;

		if (op0 == SymbolicOperator.LAMBDA) {
			assert argumentSequence.size() == 1;
			result = substitute((SymbolicExpressionIF) function.argument(1),
					new SingletonMap<SymbolicConstantIF, SymbolicExpressionIF>(
							(SymbolicConstantIF) function.argument(0),
							argumentSequence.getFirst()));
		} else
			result = expression(SymbolicOperator.APPLY,
					((SymbolicFunctionTypeIF) function.type()).outputType(),
					function, argumentSequence);
		return result;
	}

	@Override
	public SymbolicExpressionIF unionInject(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object) {
		SymbolicTypeIF objectType = object.type();
		int indexInt = memberIndex.getInt();
		SymbolicTypeIF memberType = unionType.sequence().getType(indexInt);

		if (!memberType.equals(objectType))
			throw new IllegalArgumentException("Expected type " + memberType
					+ " but object has type " + objectType + ": " + object);
		// inject_i(extract_i(x))=x...
		if (object.operator() == SymbolicOperator.UNION_EXTRACT
				&& unionType.equals(((SymbolicExpressionIF) object.argument(1))
						.type()) && memberIndex.equals(object.argument(0)))
			return (SymbolicExpressionIF) object.argument(1);
		return expression(SymbolicOperator.UNION_INJECT, unionType,
				memberIndex, object);
	}

	@Override
	public SymbolicExpressionIF unionTest(IntObject memberIndex,
			SymbolicExpressionIF object) {
		if (object.operator() == SymbolicOperator.UNION_INJECT)
			return object.argument(0).equals(memberIndex) ? trueExpr
					: falseExpr;
		return expression(SymbolicOperator.UNION_TEST, booleanType,
				memberIndex, object);
	}

	@Override
	public SymbolicExpressionIF unionExtract(IntObject memberIndex,
			SymbolicExpressionIF object) {
		if (object.operator() == SymbolicOperator.UNION_INJECT
				&& memberIndex.equals(object.argument(0)))
			return (SymbolicExpressionIF) object.argument(1);
		return expression(
				SymbolicOperator.UNION_EXTRACT,
				((SymbolicUnionTypeIF) object).sequence().getType(
						memberIndex.getInt()), memberIndex, object);
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
		SymbolicOperator op = array.operator();

		if (op == SymbolicOperator.DENSE_ARRAY_WRITE) {
			IntegerNumberIF indexNumber = (IntegerNumberIF) extractNumber(index);

			if (indexNumber != null) {
				SymbolicExpressionIF origin = (SymbolicExpressionIF) array
						.argument(0);

				if (indexNumber.compareTo(denseArrayMaxSize) < 0) {
					int indexInt = indexNumber.intValue();

					SymbolicSequence values = (SymbolicSequence) array
							.argument(1);
					int size = values.size();

					if (indexInt < size) {
						SymbolicExpressionIF value = values.get(indexInt);

						if (!value.isNull())
							return value;
					}
				}
				// either indexNumber too big or entry is null
				return arrayRead(origin, index);
			}
		}
		return expression(SymbolicOperator.ARRAY_READ,
				((SymbolicArrayTypeIF) array.type()).elementType(), array,
				index);
	}

	@Override
	public SymbolicExpressionIF arrayWrite(SymbolicExpressionIF array,
			SymbolicExpressionIF index, SymbolicExpressionIF value) {
		IntegerNumberIF indexNumber = (IntegerNumberIF) extractNumber(index);

		if (indexNumber != null && indexNumber.compareTo(denseArrayMaxSize) < 0) {
			SymbolicOperator op = array.operator();
			int indexInt = indexNumber.intValue();
			SymbolicSequence sequence;
			SymbolicExpressionIF origin;

			if (op == SymbolicOperator.DENSE_ARRAY_WRITE) {
				origin = (SymbolicExpressionIF) array.argument(0);
				sequence = ((SymbolicSequence) array.argument(1));
			} else {
				origin = array;
				sequence = collectionFactory.emptySequence();
			}
			sequence = sequence.setExtend(indexInt, value, nullExpression);
			return expression(SymbolicOperator.DENSE_ARRAY_WRITE, array.type(),
					origin, sequence);
		}
		return expression(SymbolicOperator.ARRAY_WRITE, array.type(), array,
				index, value);
	}

	@Override
	public SymbolicExpressionIF denseArrayWrite(SymbolicExpressionIF array,
			SymbolicSequence values) {
		return expression(SymbolicOperator.DENSE_ARRAY_WRITE, array.type(),
				array, values);
	}

	@Override
	public SymbolicExpressionIF arrayLambda(
			SymbolicCompleteArrayTypeIF arrayType, SymbolicExpressionIF function) {
		// TODO Auto-generated method stub
		return null;
	}

	public SymbolicExpressionIF tupleUnsafe(SymbolicTupleTypeIF type,
			SymbolicSequence components) {
		return expression(SymbolicOperator.CONCRETE, type, components);
	}

	@Override
	public SymbolicExpressionIF tuple(SymbolicTupleTypeIF type,
			SymbolicSequence components) {
		int n = components.size();
		SymbolicTypeSequenceIF fieldTypes = type.sequence();
		int m = fieldTypes.numTypes();

		if (n != m)
			throw new IllegalArgumentException("Tuple type has exactly" + m
					+ " components but sequence has length " + n);
		for (int i = 0; i < n; i++) {
			SymbolicTypeIF fieldType = fieldTypes.getType(i);
			SymbolicTypeIF componentType = components.get(i).type();

			if (!fieldType.equals(componentType))
				throw new IllegalArgumentException(
						"Expected expression of type " + fieldType
								+ " but saw type " + componentType
								+ " at position " + i);
		}
		return expression(SymbolicOperator.CONCRETE, type, components);
	}

	@Override
	public SymbolicExpressionIF tupleRead(SymbolicExpressionIF tuple,
			IntObject index) {
		SymbolicOperator op = tuple.operator();
		int indexInt = index.getInt();

		if (op == SymbolicOperator.CONCRETE)
			return ((SymbolicSequence) tuple.argument(0)).get(indexInt);
		return expression(
				SymbolicOperator.TUPLE_READ,
				((SymbolicTupleTypeIF) tuple.type()).sequence().getType(
						indexInt), tuple, index);
	}

	@Override
	public SymbolicExpressionIF tupleWrite(SymbolicExpressionIF tuple,
			IntObject index, SymbolicExpressionIF value) {
		SymbolicOperator op = tuple.operator();
		int indexInt = index.getInt();
		SymbolicTupleTypeIF tupleType = (SymbolicTupleTypeIF) tuple.type();
		SymbolicTypeIF fieldType = tupleType.sequence().getType(indexInt);
		SymbolicTypeIF valueType = value.type();

		if (!fieldType.equals(valueType))
			throw new IllegalArgumentException("Expected expresion of type "
					+ fieldType + " but saw type " + valueType + ": " + value);
		if (op == SymbolicOperator.CONCRETE)
			return expression(op, tupleType,
					((SymbolicSequence) tuple.argument(0)).set(indexInt, value));
		return expression(SymbolicOperator.TUPLE_WRITE, tupleType, tuple, value);
	}

	@Override
	public SymbolicExpressionIF cast(SymbolicTypeIF newType,
			SymbolicExpressionIF expression) {
		SymbolicTypeIF oldType = expression.type();

		if (oldType.equals(newType))
			return expression;
		if (oldType.isInteger() && newType.isReal()) {
			return numericExpressionFactory
					.castToReal((NumericExpression) expression);
		}
		if (oldType.typeKind() == SymbolicTypeKind.UNION) {
			Integer index = ((SymbolicUnionTypeIF) oldType)
					.indexOfType(newType);

			if (index != null)
				return unionExtract(intObject(index), expression);
		}
		if (newType.typeKind() == SymbolicTypeKind.UNION) {
			Integer index = ((SymbolicUnionTypeIF) newType)
					.indexOfType(oldType);

			if (index != null)
				return unionInject((SymbolicUnionTypeIF) newType,
						intObject(index), expression);
		}
		throw new IllegalArgumentException("Cannot cast from type " + oldType
				+ " to type " + newType + ": " + expression);
	}

	@Override
	public SymbolicExpressionIF cond(SymbolicExpressionIF predicate,
			SymbolicExpressionIF trueValue, SymbolicExpressionIF falseValue) {
		if (predicate.isTrue())
			return trueValue;
		if (predicate.isFalse())
			return falseValue;
		return expression(SymbolicOperator.COND, trueValue.type(), predicate,
				trueValue, falseValue);
	}

	@Override
	public ObjectComparator comparator() {
		return objectComparator;
	}

}
