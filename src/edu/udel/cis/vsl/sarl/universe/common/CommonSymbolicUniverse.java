package edu.udel.cis.vsl.sarl.universe.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection.SymbolicCollectionKind;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.prove.Simplifier;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
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
 * A standard implementation of SymbolicUniverse, relying heavily on a given
 * NumericExpressionFactory for dealing with numeric issues.
 * 
 * @author siegel
 */
public class CommonSymbolicUniverse implements SymbolicUniverse {

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

	private IntegerNumber denseArrayMaxSize, quantifierExpandBound;

	// private FactorySystem system;

	private ObjectFactory objectFactory;

	private SymbolicTypeFactory typeFactory;

	private ExpressionFactory expressionFactory;

	private NumberFactory numberFactory;

	private CollectionFactory collectionFactory;

	private NumericExpressionFactory numericFactory;

	private ObjectComparator objectComparator;

	private TheoremProver prover;

	private SymbolicType booleanType, integerType, realType;

	private SymbolicExpression nullExpression;

	private SymbolicExpression trueExpr, falseExpr;

	public CommonSymbolicUniverse(FactorySystem system) {
		// this.system = system;
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		expressionFactory = system.expressionFactory();
		collectionFactory = system.collectionFactory();
		numericFactory = expressionFactory.numericFactory();
		numberFactory = numericFactory.numberFactory();
		objectComparator = objectFactory.comparator();
		booleanType = typeFactory.booleanType();
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
		trueExpr = expression(SymbolicOperator.CONCRETE, booleanType,
				objectFactory.trueObj());
		falseExpr = expression(SymbolicOperator.CONCRETE, booleanType,
				objectFactory.falseObj());
		denseArrayMaxSize = numberFactory.integer(DENSE_ARRAY_MAX_SIZE);
		quantifierExpandBound = numberFactory.integer(QUANTIFIER_EXPAND_BOUND);
		nullExpression = expressionFactory.nullExpression();
		prover = Prove.newIdealCVC3HybridProver(this);
	}

	public NumericExpressionFactory numericExpressionFactory() {
		return numericFactory;
	}

	@Override
	public SymbolicObject canonic(SymbolicObject object) {
		return objectFactory.canonic(object);
	}

	protected SymbolicExpression canonic(SymbolicExpression expression) {
		return objectFactory.canonic(expression);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] arguments) {
		return type.isNumeric() ? numericFactory.newNumericExpression(operator,
				type, arguments) : expressionFactory.expression(operator, type,
				arguments);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0) {
		return type.isNumeric() ? numericFactory.newNumericExpression(operator,
				type, arg0) : expressionFactory
				.expression(operator, type, arg0);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1) {
		return type.isNumeric() ? numericFactory.newNumericExpression(operator,
				type, arg0, arg1) : expressionFactory.expression(operator,
				type, arg0, arg1);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return type.isNumeric() ? numericFactory.newNumericExpression(operator,
				type, arg0, arg1, arg2) : expressionFactory.expression(
				operator, type, arg0, arg1, arg2);
	}

	protected SymbolicExpression zero(SymbolicType type) {
		if (type.isInteger())
			return zeroInt();
		else if (type.isReal())
			return zeroReal();
		else
			throw new SARLInternalException("Expected type int or real, not "
					+ type);
	}

	protected SymbolicSet<SymbolicExpression> set(SymbolicExpression x,
			SymbolicExpression y) {
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
	public SymbolicExpression make(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] args) {
		int numArgs = args.length;

		switch (operator) {
		case ADD: // 1 or 2 args
			if (numArgs == 1) // collection
				return add((SymbolicCollection<?>) args[0]);
			else
				return add((SymbolicExpression) args[0],
						(SymbolicExpression) args[1]);
		case AND: // 1 or 2 args
			if (numArgs == 1) // collection
				return and((SymbolicCollection<?>) args[0]);
			else
				return and((SymbolicExpression) args[0],
						(SymbolicExpression) args[1]);
		case APPLY: // 2 args: function and sequence
			return apply((SymbolicExpression) args[0],
					(SymbolicSequence<?>) args[1]);
		case ARRAY_LAMBDA:
			return arrayLambda((SymbolicCompleteArrayType) type,
					(SymbolicExpression) args[0]);
		case ARRAY_READ:
			return arrayRead((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case ARRAY_WRITE:
			return arrayWrite((SymbolicExpression) args[0],
					(SymbolicExpression) args[1], (SymbolicExpression) args[2]);
		case CAST:
			return castToReal((SymbolicExpression) args[0]);
		case CONCRETE:
			if (type.isNumeric())
				return canonic(numericFactory
						.newConcreteNumericExpression((NumberObject) args[0]));
			else
				return expression(SymbolicOperator.CONCRETE, type, args[0]);
		case COND:
			return cond((SymbolicExpression) args[0],
					(SymbolicExpression) args[1], (SymbolicExpression) args[2]);
		case DENSE_ARRAY_WRITE:
			return denseArrayWrite((SymbolicExpression) args[0],
					(SymbolicSequence<?>) args[1]);
		case DIVIDE:
			return divide((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case EQUALS:
			return equals((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case EXISTS:
			return exists((SymbolicConstant) args[0],
					(SymbolicExpression) args[1]);
		case FORALL:
			return forall((SymbolicConstant) args[0],
					(SymbolicExpression) args[1]);
		case INT_DIVIDE:
			return divide((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case LAMBDA:
			return lambda((SymbolicConstant) args[0],
					(SymbolicExpression) args[1]);
		case LENGTH:
			return length((SymbolicConstant) args[0]);
		case LESS_THAN:
			return lessThan((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case LESS_THAN_EQUALS:
			return lessThanEquals((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case MODULO:
			return modulo((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case MULTIPLY:
			if (numArgs == 1) // collection
				return multiply((SymbolicCollection<?>) args[0]);
			else
				return multiply((SymbolicExpression) args[0],
						(SymbolicExpression) args[1]);
		case NEGATIVE:
			return minus((SymbolicExpression) args[0]);
		case NEQ:
			return neq((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case NOT:
			return not((SymbolicExpression) args[0]);
		case OR: {
			if (numArgs == 1) // collection
				return or((SymbolicCollection<?>) args[0]);
			else
				return or((SymbolicExpression) args[0],
						(SymbolicExpression) args[1]);
		}
		case POWER: // exponent could be expression or int constant
			if (args[1] instanceof SymbolicExpression)
				return power((SymbolicExpression) args[0],
						(SymbolicExpression) args[1]);
			else
				return power((SymbolicExpression) args[0], (IntObject) args[1]);
		case SUBTRACT:
			return subtract((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case SYMBOLIC_CONSTANT:
			return symbolicConstant((StringObject) args[0], type);
		case TUPLE_READ:
			return tupleRead((SymbolicExpression) args[0], (IntObject) args[1]);
		case TUPLE_WRITE:
			return tupleWrite((SymbolicExpression) args[0],
					(IntObject) args[1], (SymbolicExpression) args[2]);
		case UNION_EXTRACT: {
			SymbolicExpression expression = (SymbolicExpression) args[1];

			return unionExtract((IntObject) args[0], expression);
		}
		case UNION_INJECT: {
			SymbolicExpression expression = (SymbolicExpression) args[1];
			SymbolicUnionType unionType = (SymbolicUnionType) type;

			return unionInject(unionType, (IntObject) args[0], expression);

		}
		case UNION_TEST: {
			SymbolicExpression expression = (SymbolicExpression) args[1];

			return unionTest((IntObject) args[0], expression);
		}
		default:
			throw new IllegalArgumentException("Unknown expression kind: "
					+ operator);
		}
	}

	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	@Override
	public SymbolicExpression add(SymbolicCollection<?> args) {
		int size = args.size();
		SymbolicExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpression arg : args) {
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
	public SymbolicExpression and(SymbolicCollection<?> args) {
		SymbolicExpression result = trueExpr;

		for (SymbolicExpression arg : args)
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
	public SymbolicExpression and(SymbolicExpression arg0,
			SymbolicExpression arg1) {
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

			if (isAnd0 && isAnd1) {
				@SuppressWarnings("unchecked")
				SymbolicSet<SymbolicExpression> result = (SymbolicSet<SymbolicExpression>) arg0
						.argument(0);

				return expression(SymbolicOperator.AND, booleanType,
						result.addAll((SymbolicSet<?>) arg1.argument(0)));
			}
			if (isAnd0 && !isAnd1) {
				@SuppressWarnings("unchecked")
				SymbolicSet<SymbolicExpression> result = (SymbolicSet<SymbolicExpression>) arg0
						.argument(0);

				return expression(SymbolicOperator.AND, booleanType,
						result.add(arg1));
			}
			if (!isAnd0 && isAnd1) {
				@SuppressWarnings("unchecked")
				SymbolicSet<SymbolicExpression> result = (SymbolicSet<SymbolicExpression>) arg1
						.argument(0);

				return expression(SymbolicOperator.AND, booleanType,
						result.add(arg0));
			}
			return expression(SymbolicOperator.AND, booleanType,
					set(arg0, arg1));
		}
	}

	@Override
	public SymbolicType booleanType() {
		return booleanType;
	}

	@Override
	public SymbolicType integerType() {
		return integerType;
	}

	@Override
	public SymbolicType realType() {
		return realType;
	}

	@Override
	public SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			SymbolicExpression extent) {
		return typeFactory.arrayType(elementType, extent);
	}

	@Override
	public SymbolicArrayType arrayType(SymbolicType elementType) {
		return typeFactory.arrayType(elementType);
	}

	@Override
	public SymbolicTypeSequence typeSequence(SymbolicType[] types) {
		return typeFactory.sequence(types);
	}

	@Override
	public SymbolicTypeSequence typeSequence(Iterable<SymbolicType> types) {
		return typeFactory.sequence(types);
	}

	@Override
	public SymbolicTupleType tupleType(StringObject name,
			SymbolicTypeSequence fieldTypes) {
		return typeFactory.tupleType(name, fieldTypes);
	}

	@Override
	public SymbolicFunctionType functionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType) {
		return typeFactory.functionType(inputTypes, outputType);
	}

	@Override
	public SymbolicUnionType unionType(StringObject name,
			SymbolicTypeSequence memberTypes) {
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
	public Simplifier simplifier(SymbolicExpression assumption) {
		// TODO: until we port the better one
		return new IdentitySimplifier(this, assumption);
	}

	@Override
	public TheoremProver prover() {
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
	public NumberObject numberObject(Number value) {
		return objectFactory.numberObject(value);
	}

	@Override
	public StringObject stringObject(String string) {
		return objectFactory.stringObject(string);
	}

	@Override
	public SymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		SymbolicConstant result;

		if (type.isNumeric())
			result = numericFactory.newNumericSymbolicConstant(name, type);
		else
			result = expressionFactory.symbolicConstant(name, type);
		return result;
	}

	@Override
	public SymbolicConstant extractSymbolicConstant(
			SymbolicExpression expression) {
		if (expression instanceof SymbolicConstant)
			return (SymbolicConstant) expression;
		return null;
	}

	public SymbolicExpression substitute(SymbolicExpression expression,
			SymbolicConstant variable, SymbolicExpression value) {
		return substitute(expression,
				new SingletonMap<SymbolicConstant, SymbolicExpression>(
						variable, value));
	}

	private SymbolicCollection<?> substituteGenericCollection(
			SymbolicCollection<?> set,
			Map<SymbolicConstant, SymbolicExpression> map) {
		Iterator<? extends SymbolicExpression> iter = set.iterator();

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substitute(oldElement, map);

			if (newElement != oldElement) {
				Collection<SymbolicExpression> newSet = new LinkedList<SymbolicExpression>();

				for (SymbolicExpression e : set) {
					if (e == oldElement)
						break;
					newSet.add(e);
				}
				newSet.add(newElement);
				while (iter.hasNext())
					newSet.add(substitute(iter.next(), map));
				return collectionFactory.basicCollection(newSet);
			}
		}
		return set;
	}

	private SymbolicCollection<?> substituteSequence(
			SymbolicSequence<?> sequence,
			Map<SymbolicConstant, SymbolicExpression> map) {
		Iterator<? extends SymbolicExpression> iter = sequence.iterator();

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substitute(oldElement, map);

			if (newElement != oldElement) {
				SymbolicSequence<SymbolicExpression> newSequence = collectionFactory
						.emptySequence();

				for (SymbolicExpression e : sequence) {
					if (e == oldElement)
						break;
					newSequence.add(e);
				}
				newSequence.add(newElement);
				while (iter.hasNext())
					newSequence.add(substitute(iter.next(), map));
				return newSequence;
			}
		}
		return sequence;
	}

	/**
	 * Only sequences need to be preserved because other collections are all
	 * processed through method make anyway.
	 * 
	 * @param collection
	 * @param map
	 * @return
	 */
	private SymbolicCollection<?> substitute(SymbolicCollection<?> collection,
			Map<SymbolicConstant, SymbolicExpression> map) {
		SymbolicCollectionKind kind = collection.collectionKind();

		if (kind == SymbolicCollectionKind.SEQUENCE)
			return substituteSequence((SymbolicSequence<?>) collection, map);
		return substituteGenericCollection(collection, map);
	}

	private SymbolicType substitute(SymbolicType type,
			Map<SymbolicConstant, SymbolicExpression> map) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType newElementType = substitute(elementType, map);

			if (arrayType.isComplete()) {
				SymbolicExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				SymbolicExpression newExtent = substitute(extent, map);

				if (elementType != newElementType || extent != newExtent)
					return typeFactory.arrayType(newElementType, newExtent);
				return arrayType;
			}
			if (elementType != newElementType)
				return typeFactory.arrayType(newElementType);
			return arrayType;
		}
		case FUNCTION: {
			SymbolicFunctionType functionType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicTypeSequence newInputs = substitute(inputs, map);
			SymbolicType output = functionType.outputType();
			SymbolicType newOutput = substitute(output, map);

			if (inputs != newInputs || output != newOutput)
				return typeFactory.functionType(newInputs, newOutput);
			return functionType;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence fields = tupleType.sequence();
			SymbolicTypeSequence newFields = substitute(fields, map);

			if (fields != newFields)
				return typeFactory.tupleType(tupleType.name(), newFields);
			return tupleType;

		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence members = unionType.sequence();
			SymbolicTypeSequence newMembers = substitute(members, map);

			if (members != newMembers)
				return typeFactory.unionType(unionType.name(), newMembers);
			return unionType;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	private SymbolicTypeSequence substitute(SymbolicTypeSequence sequence,
			Map<SymbolicConstant, SymbolicExpression> map) {
		int count = 0;

		for (SymbolicType t : sequence) {
			SymbolicType newt = substitute(t, map);

			if (t != newt) {
				int numTypes = sequence.numTypes();
				SymbolicType[] newTypes = new SymbolicType[numTypes];

				for (int i = 0; i < count; i++)
					newTypes[i] = sequence.getType(i);
				newTypes[count] = newt;
				for (int i = count + 1; i < numTypes; i++)
					newTypes[i] = substitute(sequence.getType(i), map);
				return typeFactory.sequence(newTypes);
			}
			count++;
		}
		return sequence;
	}

	/**
	 * If no changes takes place, result returned will be == given expression.
	 */
	@Override
	public SymbolicExpression substitute(SymbolicExpression expression,
			Map<SymbolicConstant, SymbolicExpression> map) {
		SymbolicOperator operator = expression.operator();

		if (operator == SymbolicOperator.SYMBOLIC_CONSTANT) {
			SymbolicExpression newValue = map
					.get((SymbolicConstant) expression);

			if (newValue != null)
				return newValue;
		}
		{
			int numArgs = expression.numArguments();
			SymbolicType type = expression.type();
			SymbolicType newType = substitute(type, map);
			SymbolicObject[] newArgs = type == newType ? null
					: new SymbolicObject[numArgs];

			for (int i = 0; i < numArgs; i++) {
				SymbolicObject arg = expression.argument(i);
				SymbolicObjectKind kind = arg.symbolicObjectKind();

				switch (kind) {
				case BOOLEAN:
				case INT:
				case NUMBER:
				case STRING:
					break;
				default:
					SymbolicObject newArg = null;

					switch (kind) {
					case EXPRESSION:
						newArg = substitute((SymbolicExpression) arg, map);
						break;
					case EXPRESSION_COLLECTION:
						newArg = substitute((SymbolicCollection<?>) arg, map);
						break;
					case TYPE:
						newArg = substitute((SymbolicType) arg, map);
						break;
					case TYPE_SEQUENCE:
						newArg = substitute((SymbolicTypeSequence) arg, map);
					default:
						throw new SARLInternalException("unreachable");
					}
					if (newArg != arg) {
						if (newArgs == null) {
							newArgs = new SymbolicObject[numArgs];

							for (int j = 0; j < i; j++)
								newArgs[j] = expression.argument(j);
						}
					}
					if (newArgs != null)
						newArgs[i] = newArg;
				}
			}
			if (newType == type && newArgs == null)
				return expression;
			return make(expression.operator(), newType, newArgs);
		}
	}

	@Override
	public SymbolicExpression symbolic(NumberObject numberObject) {
		return numericFactory.newConcreteNumericExpression(numberObject);
	}

	@Override
	public SymbolicExpression symbolic(int value) {
		return symbolic(numberObject(numberFactory.integer(value)));
	}

	@Override
	public SymbolicExpression zeroInt() {
		return numericFactory.zeroInt();
	}

	@Override
	public SymbolicExpression zeroReal() {
		return numericFactory.zeroReal();
	}

	@Override
	public SymbolicExpression oneInt() {
		return numericFactory.oneInt();
	}

	@Override
	public SymbolicExpression oneReal() {
		return numericFactory.oneReal();
	}

	@Override
	public SymbolicExpression add(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return numericFactory.add((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpression subtract(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return numericFactory.subtract((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpression multiply(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return numericFactory.multiply((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpression multiply(SymbolicCollection<?> args) {
		int size = args.size();
		SymbolicExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpression arg : args) {
			if (result == null)
				result = arg;
			else
				result = multiply(result, arg);
		}
		return result;
	}

	@Override
	public SymbolicExpression divide(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return numericFactory.divide((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpression modulo(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return numericFactory.modulo((NumericExpression) arg0,
				(NumericExpression) arg1);
	}

	@Override
	public SymbolicExpression minus(SymbolicExpression arg) {
		return numericFactory.minus((NumericExpression) arg);
	}

	@Override
	public SymbolicExpression power(SymbolicExpression base, IntObject exponent) {
		return numericFactory.power((NumericExpression) base, exponent);
	}

	@Override
	public SymbolicExpression power(SymbolicExpression base,
			SymbolicExpression exponent) {
		return numericFactory.power((NumericExpression) base,
				(NumericExpression) exponent);
	}

	@Override
	public SymbolicExpression castToReal(SymbolicExpression numericExpression) {
		return numericFactory.castToReal((NumericExpression) numericExpression);
	}

	@Override
	public Number extractNumber(SymbolicExpression expression) {
		if (expression.operator() == SymbolicOperator.CONCRETE) {
			SymbolicObject object = expression.argument(0);

			if (object.symbolicObjectKind() == SymbolicObjectKind.NUMBER)
				return ((NumberObject) object).getNumber();
		}
		return null;
	}

	@Override
	public SymbolicExpression symbolic(BooleanObject object) {
		return expression(SymbolicOperator.CONCRETE, booleanType, object);
	}

	@Override
	public SymbolicExpression symbolic(boolean value) {
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
	public SymbolicExpression or(SymbolicExpression arg0,
			SymbolicExpression arg1) {
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
				SymbolicExpression result = falseExpr;

				for (SymbolicExpression clause : (SymbolicSet<?>) arg0
						.argument(0))
					result = or(result, and(clause, arg1));
				return result;
			}
			if (op1 == SymbolicOperator.AND) {
				SymbolicExpression result = falseExpr;

				for (SymbolicExpression clause : (SymbolicSet<?>) arg1
						.argument(0))
					result = or(result, and(arg0, clause));
				return result;
			}
			if (op0 == SymbolicOperator.OR && op1 == SymbolicOperator.OR) {
				@SuppressWarnings("unchecked")
				SymbolicSet<SymbolicExpression> result = (SymbolicSet<SymbolicExpression>) arg0
						.argument(0);

				return expression(op0, booleanType,
						result.addAll((SymbolicSet<?>) arg1.argument(0)));
			}
			if (op0 == SymbolicOperator.OR) {
				@SuppressWarnings("unchecked")
				SymbolicSet<SymbolicExpression> result = (SymbolicSet<SymbolicExpression>) arg0
						.argument(0);

				return expression(op0, booleanType, result.add(arg1));
			}
			if (op1 == SymbolicOperator.OR) {
				@SuppressWarnings("unchecked")
				SymbolicSet<SymbolicExpression> result = (SymbolicSet<SymbolicExpression>) arg1
						.argument(0);

				return expression(op1, booleanType, result.add(arg0));
			}
			return expression(SymbolicOperator.OR, booleanType, set(arg0, arg1));
		}
	}

	/**
	 * Assume nothing about the list of args.
	 */
	@Override
	public SymbolicExpression or(SymbolicCollection<?> args) {
		SymbolicExpression result = falseExpr;

		for (SymbolicExpression arg : args)
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
	public SymbolicExpression not(SymbolicExpression arg) {
		SymbolicOperator operator = arg.operator();

		switch (operator) {
		case AND: {
			SymbolicExpression result = falseExpr;

			for (SymbolicExpression clause : (SymbolicSet<?>) arg.argument(0))
				result = or(result, not(clause));
			return result;
		}
		case OR: {
			SymbolicExpression result = trueExpr;

			for (SymbolicExpression clause : (SymbolicSet<?>) arg.argument(0))
				result = and(result, not(clause));
			return result;
		}
		case NOT:
			return (SymbolicExpression) arg.argument(0);
		case FORALL:
			return expression(SymbolicOperator.EXISTS, booleanType,
					(SymbolicConstant) arg.argument(0),
					not((SymbolicExpression) arg.argument(1)));
		case EXISTS:
			return expression(SymbolicOperator.FORALL, booleanType,
					(SymbolicConstant) arg.argument(0),
					not((SymbolicExpression) arg.argument(1)));
		case LESS_THAN:
			return expression(SymbolicOperator.LESS_THAN_EQUALS, booleanType,
					(SymbolicExpression) arg.argument(0),
					minus((SymbolicExpression) arg.argument(1)));
		case EQUALS:
			return expression(SymbolicOperator.NEQ, booleanType,
					(SymbolicExpression) arg.argument(0),
					(SymbolicExpression) arg.argument(1));
		case LESS_THAN_EQUALS:
			return expression(SymbolicOperator.LESS_THAN, booleanType,
					(SymbolicExpression) arg.argument(0),
					minus((SymbolicExpression) arg.argument(1)));
		case NEQ:
			return expression(SymbolicOperator.EQUALS, booleanType,
					(SymbolicExpression) arg.argument(0),
					(SymbolicExpression) arg.argument(1));
		default:
			return expression(SymbolicOperator.NOT, booleanType, arg);
		}
	}

	public SymbolicExpression implies(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return or(not(arg0), arg1);
	}

	public SymbolicExpression equiv(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return and(implies(arg0, arg1), implies(arg1, arg0));
	}

	public SymbolicExpression forallIntConcrete(SymbolicConstant index,
			IntegerNumber low, IntegerNumber high, SymbolicExpression predicate) {
		SymbolicExpression result = trueExpr;

		for (IntegerNumber i = low; i.compareTo(high) < 0; i = numberFactory
				.increment(i)) {
			SymbolicExpression iExpression = symbolic(numberObject(i));
			SymbolicExpression substitutedPredicate = substitute(predicate,
					index, iExpression);

			result = and(result, substitutedPredicate);
		}
		return result;
	}

	@Override
	public SymbolicExpression forallInt(SymbolicConstant index,
			SymbolicExpression low, SymbolicExpression high,
			SymbolicExpression predicate) {
		IntegerNumber lowNumber = (IntegerNumber) extractNumber(low);

		if (lowNumber != null) {
			IntegerNumber highNumber = (IntegerNumber) extractNumber(high);

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

	public SymbolicExpression existsIntConcrete(SymbolicConstant index,
			IntegerNumber low, IntegerNumber high, SymbolicExpression predicate) {
		SymbolicExpression result = falseExpr;

		for (IntegerNumber i = low; i.compareTo(high) < 0; i = numberFactory
				.increment(i)) {
			SymbolicExpression iExpression = symbolic(numberObject(i));
			SymbolicExpression substitutedPredicate = substitute(predicate,
					index, iExpression);

			result = or(result, substitutedPredicate);
		}
		return result;
	}

	@Override
	public SymbolicExpression existsInt(SymbolicConstant index,
			SymbolicExpression low, SymbolicExpression high,
			SymbolicExpression predicate) {
		IntegerNumber lowNumber = (IntegerNumber) extractNumber(low);

		if (lowNumber != null) {
			IntegerNumber highNumber = (IntegerNumber) extractNumber(high);

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
	public SymbolicExpression lessThan(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		SymbolicExpression difference = subtract(arg1, arg0);
		Number number = extractNumber(difference);

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
	public SymbolicExpression lessThanEquals(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		SymbolicExpression difference = subtract(arg1, arg0);
		Number number = extractNumber(difference);

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

	private SymbolicExpression equals(SymbolicTypeSequence seq0,
			SymbolicTypeSequence seq1, int nestingDepth) {
		int size = seq0.numTypes();

		if (size != seq1.numTypes())
			return falseExpr;
		if (size == 0)
			return trueExpr;
		else {
			SymbolicExpression result = equals(seq0.getType(0),
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
	public SymbolicExpression equals(SymbolicType type0, SymbolicType type1) {
		return equals(type0, type1, 0);
	}

	/**
	 * Returns a boolean expression which holds iff the two types are equal.
	 * 
	 * @param type0
	 * @param type1
	 * @return
	 */
	private SymbolicExpression equals(SymbolicType type0, SymbolicType type1,
			int nestingDepth) {
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
			SymbolicArrayType a0 = (SymbolicArrayType) type0;
			SymbolicArrayType a1 = (SymbolicArrayType) type1;

			if (a0.isComplete() != a1.isComplete())
				return falseExpr;
			else {
				SymbolicExpression result = equals(a0.elementType(),
						a1.elementType(), nestingDepth);

				if (a0.isComplete())
					result = and(
							result,
							equals(((SymbolicCompleteArrayType) a0).extent(),
									((SymbolicCompleteArrayType) a1).extent(),
									nestingDepth));
				return result;
			}
		}
		case FUNCTION:
			return and(
					equals(((SymbolicFunctionType) type0).inputTypes(),
							((SymbolicFunctionType) type1).inputTypes(),
							nestingDepth),
					equals(((SymbolicFunctionType) type0).outputType(),
							((SymbolicFunctionType) type1).outputType(),
							nestingDepth));
		case TUPLE: {
			SymbolicTupleType t0 = (SymbolicTupleType) type0;
			SymbolicTupleType t1 = (SymbolicTupleType) type1;

			if (!t0.name().equals(t1.name()))
				return falseExpr;
			return equals(t0.sequence(), t1.sequence(), nestingDepth);
		}
		case UNION: {
			SymbolicUnionType t0 = (SymbolicUnionType) type0;
			SymbolicUnionType t1 = (SymbolicUnionType) type1;

			if (!t0.name().equals(t1.name()))
				return falseExpr;
			return equals(t0.sequence(), t1.sequence(), nestingDepth);
		}
		default:
			throw new SARLInternalException("unreachable");
		}

	}

	@Override
	public SymbolicExpression equals(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		return equals(arg0, arg1, 0);
	}

	private SymbolicConstant boundVar(int index, SymbolicType type) {
		return symbolicConstant(stringObject("x" + index), type);
	}

	private SymbolicConstant intBoundVar(int index) {
		return symbolicConstant(stringObject("i" + index), integerType);
	}

	private SymbolicExpression equals(SymbolicExpression arg0,
			SymbolicExpression arg1, int quantifierDepth) {
		if (arg0.equals(arg1))
			return trueExpr;

		SymbolicType type = arg0.type();
		SymbolicExpression result = equals(type, arg1.type(), quantifierDepth);

		if (result.equals(falseExpr))
			return result;
		switch (type.typeKind()) {
		case BOOLEAN:
			return equiv(arg0, arg1);
		case INTEGER:
		case REAL: {
			SymbolicExpression difference = subtract(arg1, arg0);
			Number number = extractNumber(difference);

			if (number == null)
				return expression(SymbolicOperator.EQUALS, booleanType,
						zero(arg0.type()), difference);
			else
				return number.isZero() ? trueExpr : falseExpr;
		}
		case ARRAY: {
			SymbolicExpression length = length(arg0);

			if (!((SymbolicArrayType) type).isComplete())
				result = and(result,
						equals(length, length(arg1), quantifierDepth));
			if (result.isFalse())
				return result;
			else {
				SymbolicConstant index = intBoundVar(quantifierDepth);

				result = and(
						result,
						forallInt(
								index,
								zeroInt(),
								length,
								equals(arrayRead(arg0, index),
										arrayRead(arg1, index),
										quantifierDepth + 1)));
				return result;
			}
		}
		case FUNCTION: {
			SymbolicTypeSequence inputTypes = ((SymbolicFunctionType) type)
					.inputTypes();
			int numInputs = inputTypes.numTypes();

			if (numInputs == 0) {
				result = and(
						result,
						expression(SymbolicOperator.EQUALS, booleanType, arg0,
								arg1));
			} else {
				SymbolicConstant[] boundVariables = new SymbolicConstant[numInputs];
				SymbolicSequence<?> sequence;
				SymbolicExpression expr;

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
			int numComponents = ((SymbolicTupleType) type).sequence()
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
			SymbolicUnionType unionType = (SymbolicUnionType) type;

			if (arg0.operator() == SymbolicOperator.UNION_INJECT) {
				IntObject index = (IntObject) arg0.argument(0);
				SymbolicExpression value0 = (SymbolicExpression) arg0
						.argument(1);

				if (arg1.operator() == SymbolicOperator.UNION_INJECT)
					return index.equals(arg1.argument(0)) ? and(
							result,
							equals(value0,
									(SymbolicExpression) arg1.argument(1),
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
								equals((SymbolicExpression) arg1.argument(1),
										unionExtract(index, arg0),
										quantifierDepth)));
			} else {
				int numTypes = unionType.sequence().numTypes();
				SymbolicExpression expr = falseExpr;

				for (int i = 0; i < numTypes; i++) {
					IntObject index = intObject(i);
					SymbolicExpression clause = result;

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
	public SymbolicExpression neq(SymbolicExpression arg0,
			SymbolicExpression arg1) {
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
	public SymbolicExpression forall(SymbolicConstant boundVariable,
			SymbolicExpression predicate) {
		if (predicate == trueExpr)
			return trueExpr;
		if (predicate == falseExpr)
			return falseExpr;
		if (predicate.operator() == SymbolicOperator.AND) {
			SymbolicExpression result = trueExpr;

			for (SymbolicExpression clause : (SymbolicSet<?>) predicate
					.argument(0))
				result = and(result, forall(boundVariable, clause));
			return result;
		}
		return expression(SymbolicOperator.FORALL, booleanType, boundVariable,
				predicate);
	}

	@Override
	public SymbolicExpression exists(SymbolicConstant boundVariable,
			SymbolicExpression predicate) {
		if (predicate == trueExpr)
			return trueExpr;
		if (predicate == falseExpr)
			return falseExpr;
		if (predicate.operator() == SymbolicOperator.OR) {
			SymbolicExpression result = falseExpr;

			for (SymbolicExpression clause : (SymbolicSet<?>) predicate
					.argument(0))
				result = or(result, exists(boundVariable, clause));
			return result;
		}
		return expression(SymbolicOperator.EXISTS, booleanType, boundVariable,
				predicate);
	}

	@Override
	public Boolean extractBoolean(SymbolicExpression expression) {
		if (expression == trueExpr)
			return true;
		if (expression == falseExpr)
			return false;
		return null;
	}

	@Override
	public SymbolicExpression lambda(SymbolicConstant boundVariable,
			SymbolicExpression expression) {
		return expression(
				SymbolicOperator.LAMBDA,
				functionType(
						typeFactory.singletonSequence(boundVariable.type()),
						expression.type()), boundVariable, expression);
	}

	@Override
	public SymbolicExpression apply(SymbolicExpression function,
			SymbolicSequence<?> argumentSequence) {
		SymbolicOperator op0 = function.operator();
		SymbolicExpression result;

		if (op0 == SymbolicOperator.LAMBDA) {
			assert argumentSequence.size() == 1;
			result = substitute((SymbolicExpression) function.argument(1),
					new SingletonMap<SymbolicConstant, SymbolicExpression>(
							(SymbolicConstant) function.argument(0),
							argumentSequence.getFirst()));
		} else
			result = expression(SymbolicOperator.APPLY,
					((SymbolicFunctionType) function.type()).outputType(),
					function, argumentSequence);
		return result;
	}

	@Override
	public SymbolicExpression unionInject(SymbolicUnionType unionType,
			IntObject memberIndex, SymbolicExpression object) {
		SymbolicType objectType = object.type();
		int indexInt = memberIndex.getInt();
		SymbolicType memberType = unionType.sequence().getType(indexInt);

		if (!memberType.equals(objectType))
			throw new IllegalArgumentException("Expected type " + memberType
					+ " but object has type " + objectType + ": " + object);
		// inject_i(extract_i(x))=x...
		if (object.operator() == SymbolicOperator.UNION_EXTRACT
				&& unionType.equals(((SymbolicExpression) object.argument(1))
						.type()) && memberIndex.equals(object.argument(0)))
			return (SymbolicExpression) object.argument(1);
		return expression(SymbolicOperator.UNION_INJECT, unionType,
				memberIndex, object);
	}

	@Override
	public SymbolicExpression unionTest(IntObject memberIndex,
			SymbolicExpression object) {
		if (object.operator() == SymbolicOperator.UNION_INJECT)
			return object.argument(0).equals(memberIndex) ? trueExpr
					: falseExpr;
		return expression(SymbolicOperator.UNION_TEST, booleanType,
				memberIndex, object);
	}

	@Override
	public SymbolicExpression unionExtract(IntObject memberIndex,
			SymbolicExpression object) {
		if (object.operator() == SymbolicOperator.UNION_INJECT
				&& memberIndex.equals(object.argument(0)))
			return (SymbolicExpression) object.argument(1);
		return expression(
				SymbolicOperator.UNION_EXTRACT,
				((SymbolicUnionType) object).sequence().getType(
						memberIndex.getInt()), memberIndex, object);
	}

	/**
	 * Need to know type of elements in case empty. We do not check element
	 * types, but maybe we should.
	 */
	@Override
	public SymbolicExpression array(SymbolicType elementType,
			SymbolicSequence<?> elements) {
		return expression(SymbolicOperator.CONCRETE,
				arrayType(elementType, symbolic(elements.size())), elements);
	}

	@Override
	public SymbolicExpression length(SymbolicExpression array) {
		SymbolicArrayType type = (SymbolicArrayType) array.type();

		if (type.isComplete())
			return ((SymbolicCompleteArrayType) type).extent();
		else
			return expression(SymbolicOperator.LENGTH, integerType, array);
	}

	@Override
	public SymbolicExpression arrayRead(SymbolicExpression array,
			SymbolicExpression index) {
		SymbolicOperator op = array.operator();

		if (op == SymbolicOperator.DENSE_ARRAY_WRITE) {
			IntegerNumber indexNumber = (IntegerNumber) extractNumber(index);

			if (indexNumber != null) {
				SymbolicExpression origin = (SymbolicExpression) array
						.argument(0);

				if (indexNumber.compareTo(denseArrayMaxSize) < 0) {
					int indexInt = indexNumber.intValue();

					SymbolicSequence<?> values = (SymbolicSequence<?>) array
							.argument(1);
					int size = values.size();

					if (indexInt < size) {
						SymbolicExpression value = values.get(indexInt);

						if (!value.isNull())
							return value;
					}
				}
				// either indexNumber too big or entry is null
				return arrayRead(origin, index);
			}
		}
		return expression(SymbolicOperator.ARRAY_READ,
				((SymbolicArrayType) array.type()).elementType(), array, index);
	}

	@Override
	public SymbolicExpression arrayWrite(SymbolicExpression array,
			SymbolicExpression index, SymbolicExpression value) {
		IntegerNumber indexNumber = (IntegerNumber) extractNumber(index);

		if (indexNumber != null && indexNumber.compareTo(denseArrayMaxSize) < 0) {
			SymbolicOperator op = array.operator();
			int indexInt = indexNumber.intValue();
			SymbolicSequence<SymbolicExpression> sequence;
			SymbolicExpression origin;

			if (op == SymbolicOperator.DENSE_ARRAY_WRITE) {
				@SuppressWarnings("unchecked")
				SymbolicSequence<SymbolicExpression> arg1 = (SymbolicSequence<SymbolicExpression>) array
						.argument(1);

				sequence = arg1;
				origin = (SymbolicExpression) array.argument(0);
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
	public SymbolicExpression denseArrayWrite(SymbolicExpression array,
			SymbolicSequence<?> values) {
		return expression(SymbolicOperator.DENSE_ARRAY_WRITE, array.type(),
				array, values);
	}

	@Override
	public SymbolicExpression arrayLambda(SymbolicCompleteArrayType arrayType,
			SymbolicExpression function) {
		// TODO Auto-generated method stub
		return null;
	}

	public SymbolicExpression tupleUnsafe(SymbolicTupleType type,
			SymbolicSequence<?> components) {
		return expression(SymbolicOperator.CONCRETE, type, components);
	}

	@Override
	public SymbolicExpression tuple(SymbolicTupleType type,
			SymbolicSequence<?> components) {
		int n = components.size();
		SymbolicTypeSequence fieldTypes = type.sequence();
		int m = fieldTypes.numTypes();

		if (n != m)
			throw new IllegalArgumentException("Tuple type has exactly" + m
					+ " components but sequence has length " + n);
		for (int i = 0; i < n; i++) {
			SymbolicType fieldType = fieldTypes.getType(i);
			SymbolicType componentType = components.get(i).type();

			if (!fieldType.equals(componentType))
				throw new IllegalArgumentException(
						"Expected expression of type " + fieldType
								+ " but saw type " + componentType
								+ " at position " + i);
		}
		return expression(SymbolicOperator.CONCRETE, type, components);
	}

	@Override
	public SymbolicExpression tupleRead(SymbolicExpression tuple,
			IntObject index) {
		SymbolicOperator op = tuple.operator();
		int indexInt = index.getInt();

		if (op == SymbolicOperator.CONCRETE)
			return ((SymbolicSequence<?>) tuple.argument(0)).get(indexInt);
		return expression(
				SymbolicOperator.TUPLE_READ,
				((SymbolicTupleType) tuple.type()).sequence().getType(indexInt),
				tuple, index);
	}

	@Override
	public SymbolicExpression tupleWrite(SymbolicExpression tuple,
			IntObject index, SymbolicExpression value) {
		SymbolicOperator op = tuple.operator();
		int indexInt = index.getInt();
		SymbolicTupleType tupleType = (SymbolicTupleType) tuple.type();
		SymbolicType fieldType = tupleType.sequence().getType(indexInt);
		SymbolicType valueType = value.type();

		if (!fieldType.equals(valueType))
			throw new IllegalArgumentException("Expected expresion of type "
					+ fieldType + " but saw type " + valueType + ": " + value);
		if (op == SymbolicOperator.CONCRETE) {
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> arg0 = (SymbolicSequence<SymbolicExpression>) tuple
					.argument(0);

			return expression(op, tupleType, arg0.set(indexInt, value));
		}
		return expression(SymbolicOperator.TUPLE_WRITE, tupleType, tuple, value);
	}

	@Override
	public SymbolicExpression cast(SymbolicType newType,
			SymbolicExpression expression) {
		SymbolicType oldType = expression.type();

		if (oldType.equals(newType))
			return expression;
		if (oldType.isInteger() && newType.isReal()) {
			return numericFactory.castToReal((NumericExpression) expression);
		}
		if (oldType.typeKind() == SymbolicTypeKind.UNION) {
			Integer index = ((SymbolicUnionType) oldType).indexOfType(newType);

			if (index != null)
				return unionExtract(intObject(index), expression);
		}
		if (newType.typeKind() == SymbolicTypeKind.UNION) {
			Integer index = ((SymbolicUnionType) newType).indexOfType(oldType);

			if (index != null)
				return unionInject((SymbolicUnionType) newType,
						intObject(index), expression);
		}
		throw new IllegalArgumentException("Cannot cast from type " + oldType
				+ " to type " + newType + ": " + expression);
	}

	@Override
	public SymbolicExpression cond(SymbolicExpression predicate,
			SymbolicExpression trueValue, SymbolicExpression falseValue) {
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
