package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NTReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression.ReferenceKind;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.expr.TupleComponentReference;
import edu.udel.cis.vsl.sarl.IF.expr.UnionMemberReference;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicMapType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicSetType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonPreUniverse implements PreUniverse {

	// Fields...

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

	/**
	 * IntegerNumber versions of the corresponding static int fields.
	 */
	private IntegerNumber denseArrayMaxSize, quantifierExpandBound;

	/**
	 * Factory for producing general symbolic objects, canonicalizing them, etc.
	 */
	private ObjectFactory objectFactory;

	/**
	 * Factory for producing symbolic types.
	 */
	private SymbolicTypeFactory typeFactory;

	/**
	 * Factory for producing general symbolic expressions.
	 */
	private ExpressionFactory expressionFactory;

	/**
	 * Factory for producing and manipulating boolean expressions.
	 */
	private BooleanExpressionFactory booleanFactory;

	/**
	 * The factory for producing and manipulating concrete numbers (such as
	 * infinite precision integers and rationals).
	 */
	private NumberFactory numberFactory;

	/**
	 * Factory for producing and manipulating instances of SymbolicCollection,
	 * which are various collections of symbolic expressions.
	 */
	private CollectionFactory collectionFactory;

	/**
	 * Factory for dealing with symbolic expressions of numeric (i.e., integer
	 * or real) type. Includes dealing with relational expressions less-than and
	 * less-than-or-equal-to.
	 */
	private NumericExpressionFactory numericFactory;

	/**
	 * The comparator on all symbolic objects used by this universe to sort such
	 * objects.
	 */
	private Comparator<SymbolicObject> objectComparator;

	/**
	 * The object used to give quantified (bound) variables unique names.
	 */
	private BoundCleaner2 cleaner;

	/** The boolean type. */
	private SymbolicType booleanType;

	/** The ideal integer type. */
	private SymbolicIntegerType integerType;

	/** The ideal real type. */
	private SymbolicRealType realType;

	/**
	 * The "NULL" symbolic expression, which is not the Java null but is used to
	 * represent "no expression" in certain contexts where a Java null is not
	 * allowed or desirable. It is a symbolic expression with operator NULL,
	 * null type, and no arguments.
	 */
	private SymbolicExpression nullExpression;

	/**
	 * The boolean symbolic concrete values true and false as symbolic
	 * expressions.
	 */
	private BooleanExpression trueExpr, falseExpr;

	/**
	 * Symbolic constant used as bound variable in certain lambda expressions.
	 * It has integer type.
	 */
	private NumericSymbolicConstant arrayIndex;

	private int validCount = 0;

	private int proverValidCount = 0;

	/**
	 * The stream to which output such as theorem prover queries should be sent.
	 */
	private PrintStream out = System.out;

	/**
	 * Should SARL reasoner queries be printed?
	 */
	private boolean showQueries = false;

	/**
	 * Should the theorem prover queries to the underlying prover(s) be printed?
	 */
	private boolean showProverQueries = false;

	// Constructor...

	/**
	 * Constructs a new CommonSymbolicUniverse from the given system of
	 * factories.
	 * 
	 * @param system
	 *            a factory system
	 */
	public CommonPreUniverse(FactorySystem system) {
		// this.system = system;
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		expressionFactory = system.expressionFactory();
		booleanFactory = system.booleanFactory();
		collectionFactory = system.collectionFactory();
		numericFactory = expressionFactory.numericFactory();
		numberFactory = numericFactory.numberFactory();
		objectComparator = objectFactory.comparator();
		booleanType = typeFactory.booleanType();
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
		trueExpr = booleanFactory.trueExpr();
		falseExpr = booleanFactory.falseExpr();
		denseArrayMaxSize = numberFactory.integer(DENSE_ARRAY_MAX_SIZE);
		quantifierExpandBound = numberFactory.integer(QUANTIFIER_EXPAND_BOUND);
		nullExpression = expressionFactory.nullExpression();
		cleaner = new BoundCleaner2(this, collectionFactory, typeFactory);
		arrayIndex = (NumericSymbolicConstant) canonic(symbolicConstant(
				stringObject("i"), integerType));
	}

	// Helper methods...

	/**
	 * Returns a new instance of SARLException with the given message. (A
	 * SARLExcpetion is a RuntimeException, so it is not required to declare
	 * when it is thrown.) It is provided here for convenience since it is used
	 * a lot and it is short to say "throw err(...)" then
	 * "throw new SARLExcpeption(...)".
	 * 
	 * This type of exception is usually thrown when the user does something
	 * wrong, like provide bad parameter values to a method.
	 * 
	 * @param message
	 *            an error message
	 * @return a new instance of SARLException with that message.
	 */
	protected SARLException err(String message) {
		return new SARLException(message);
	}

	/**
	 * Throws a new instance of SARLInternalException with the given message.
	 * This type of exception is thrown when something bad happens that
	 * shouldn't be possible. (It is the developers' fault, not the user's.) A
	 * message that this is an internal error and it should be reported to the
	 * developers is pre-pended to the given message.
	 * 
	 * Note SARLInterException extends SARLException extends RuntimeException.
	 * 
	 * @param message
	 *            an explanation of the unexpected thing that happened
	 * @return new instance of SARLInternalExcpetion with that message
	 */
	protected SARLInternalException ierr(String message) {
		return new SARLInternalException(message);
	}

	/**
	 * Invokes the object factory's generic canonic method on a symbolic
	 * expression. Here for convenience.
	 * 
	 * @param expression
	 *            a symbolic expression
	 * @return canonic representative of that object's equivalence class under
	 *         "equals" (a la Flyweight Pattern)
	 */
	@Override
	public SymbolicExpression canonic(SymbolicExpression expression) {
		return objectFactory.canonic(expression);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] arguments) {
		return expressionFactory.expression(operator, type, arguments);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0) {
		return expressionFactory.expression(operator, type, arg0);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1) {
		return expressionFactory.expression(operator, type, arg0, arg1);
	}

	protected SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return expressionFactory.expression(operator, type, arg0, arg1, arg2);
	}

	protected NumericExpression zero(SymbolicType type) {
		if (type.isInteger())
			return zeroInt();
		else if (type.isReal())
			return zeroReal();
		else
			throw ierr("Expected type int or real, not " + type);
	}

	protected SymbolicSet<SymbolicExpression> hashSet(SymbolicExpression x,
			SymbolicExpression y) {
		return collectionFactory.singletonHashSet(x).add(y);
	}

	private SymbolicConstant boundVar(int index, SymbolicType type) {
		return symbolicConstant(stringObject("x" + index), type);
	}

	/**
	 * Returns a symbolic constant of integer type for use in binding
	 * expressions (e.g., "forall int i...").
	 * 
	 * @param index
	 *            unique ID to be used in name of the symbolic constant
	 * @return the symbolic constant
	 */
	private NumericSymbolicConstant intBoundVar(int index) {
		return numericFactory.symbolicConstant(stringObject("i" + index),
				integerType);
	}

	/**
	 * Returns a boolean expression which holds iff the two types are
	 * compatible, using nestingDepth to control the name of the next bound
	 * variable.
	 * 
	 * @param type0
	 *            a symbolic type
	 * @param type1
	 *            a symbolic type
	 * @return a boolean expression which holds iff the two types are compatible
	 */
	private BooleanExpression compatible(SymbolicType type0,
			SymbolicType type1, int nestingDepth) {
		// since the "equals" case should be by far the most frequent
		// case, we check it first...
		if (type0.equals(type1))
			return trueExpr;

		SymbolicTypeKind kind = type0.typeKind();

		if (kind != type1.typeKind())
			return falseExpr;
		switch (kind) {
		case BOOLEAN:
		case CHAR:
			// only one BOOLEAN type; only one CHAR type...
			throw ierr("Unreachable: types are not equal but both have kind "
					+ kind);
		case INTEGER:
		case REAL:
			// types are not equal but have same kind. We do not consider
			// Herbrand real and real to be compatible, e.g.
			return falseExpr;
		case ARRAY: {
			SymbolicArrayType a0 = (SymbolicArrayType) type0;
			SymbolicArrayType a1 = (SymbolicArrayType) type1;
			BooleanExpression result = compatible(a0.elementType(),
					a1.elementType(), nestingDepth);

			if (a0.isComplete() && a1.isComplete())
				result = and(
						result,
						equals(((SymbolicCompleteArrayType) a0).extent(),
								((SymbolicCompleteArrayType) a1).extent(),
								nestingDepth));
			return result;
		}
		case FUNCTION:
			return and(
					compatibleTypeSequence(
							((SymbolicFunctionType) type0).inputTypes(),
							((SymbolicFunctionType) type1).inputTypes(),
							nestingDepth),
					compatible(((SymbolicFunctionType) type0).outputType(),
							((SymbolicFunctionType) type1).outputType(),
							nestingDepth));
		case TUPLE: {
			SymbolicTupleType t0 = (SymbolicTupleType) type0;
			SymbolicTupleType t1 = (SymbolicTupleType) type1;

			if (!t0.name().equals(t1.name()))
				return falseExpr;
			return compatibleTypeSequence(t0.sequence(), t1.sequence(),
					nestingDepth);
		}
		case UNION: {
			SymbolicUnionType t0 = (SymbolicUnionType) type0;
			SymbolicUnionType t1 = (SymbolicUnionType) type1;

			if (!t0.name().equals(t1.name()))
				return falseExpr;
			return compatibleTypeSequence(t0.sequence(), t1.sequence(),
					nestingDepth);
		}
		default:
			throw ierr("unreachable");
		}
	}

	/**
	 * Returns a boolean expression which holds iff the two types are
	 * compatible. Two types are compatible if it is possible for them to have a
	 * value in common. For the most part, this is the same as saying they are
	 * the same type. The exception is that an incomplete array type and a
	 * complete array type with compatible element types are compatible.
	 * 
	 * @param type0
	 *            a type
	 * @param type1
	 *            a type
	 * @return a boolean expression which holds iff the two types are compatible
	 */
	@Override
	public BooleanExpression compatible(SymbolicType type0, SymbolicType type1) {
		return compatible(type0, type1, 0);
	}

	/**
	 * Are the two types definitely incompatible? If this method returns true,
	 * the types cannot be compatible (i.e., there cannot be any object
	 * belonging to both). If it returns false, the two types are probably
	 * compatible, but there is no guarantee.
	 * 
	 * @param type0
	 *            a type
	 * @param type1
	 *            a type
	 * @return true iff definitely not compatible
	 */
	protected boolean incompatible(SymbolicType type0, SymbolicType type1) {
		return compatible(type0, type1).isFalse();
	}

	private BooleanExpression equals(ReferenceExpression arg0,
			ReferenceExpression arg1, int quantifierDepth) {
		BooleanExpression result;
		ReferenceKind kind = arg0.referenceKind();

		if (kind != arg1.referenceKind())
			result = falseExpr;
		else if (arg0 instanceof NTReferenceExpression) {
			ReferenceExpression parent0 = ((NTReferenceExpression) arg0)
					.getParent();
			ReferenceExpression parent1 = ((NTReferenceExpression) arg1)
					.getParent();

			result = equals(parent0, parent1, quantifierDepth);
			if (result.isFalse())
				return result;
			switch (kind) {
			case ARRAY_ELEMENT: {
				ArrayElementReference ref0 = (ArrayElementReference) arg0;
				ArrayElementReference ref1 = (ArrayElementReference) arg1;

				result = and(
						result,
						equals(ref0.getIndex(), ref1.getIndex(),
								quantifierDepth));
				break;
			}
			case OFFSET: {
				OffsetReference ref0 = (OffsetReference) arg0;
				OffsetReference ref1 = (OffsetReference) arg1;

				result = and(
						result,
						equals(ref0.getOffset(), ref1.getOffset(),
								quantifierDepth));
				break;
			}
			case TUPLE_COMPONENT: {
				TupleComponentReference ref0 = (TupleComponentReference) arg0;
				TupleComponentReference ref1 = (TupleComponentReference) arg1;

				result = ref0.getIndex().equals(ref1.getIndex()) ? result
						: falseExpr;
				break;
			}
			case UNION_MEMBER: {
				UnionMemberReference ref0 = (UnionMemberReference) arg0;
				UnionMemberReference ref1 = (UnionMemberReference) arg1;

				result = ref0.getIndex().equals(ref1.getIndex()) ? result
						: falseExpr;
				break;
			}
			default:
				throw err("Unreachable because the only kinds of NTReferenceExpression "
						+ "are as listed above.\n" + "This is: " + kind);
			}
		} else {
			// either both are identity of both are null
			result = trueExpr;
		}
		return result;
	}

	/**
	 * Compares two arguments to check compatibility first, then passes those
	 * arguments to a case/switch. Each case checks the equality of the two
	 * arguments based on the following types:
	 * <ul>
	 * <li>BOOLEAN: Tests 2 boolean values for equality</li>
	 * <li>CHAR: Tests 2 char values for equality. Checks whether both are
	 * concrete or not.</li>
	 * <li>INTEGER:</li>
	 * <li>REAL: Checks whether 2 real values are equal</li>
	 * <li>ARRAY: Checks whether 2 arrays are equal</li>
	 * <li>FUNCTION: Takes a sequence and checks the content and equality of its
	 * elements</li>
	 * <li>TUPLE: Checks whether 2 tuples are equal</li>
	 * <li>UNION: Scans 2 separate unions to check equality</li>
	 * </ul>
	 * 
	 * @param arg0
	 *            SymbolicType
	 * @param arg1
	 *            SymbolicType
	 * @param quantifierDepth
	 *            int
	 * @return BooleanExpression
	 */
	private BooleanExpression equals(SymbolicExpression arg0,
			SymbolicExpression arg1, int quantifierDepth) {
		if (arg0.equals(arg1))
			return trueExpr;

		SymbolicType type = arg0.type();
		BooleanExpression result = compatible(type, arg1.type(),
				quantifierDepth);

		if (result.equals(falseExpr))
			return result;
		if (arg0 instanceof ReferenceExpression
				&& arg1 instanceof ReferenceExpression)
			return equals((ReferenceExpression) arg0,
					(ReferenceExpression) arg1, quantifierDepth);
		switch (type.typeKind()) {
		case BOOLEAN:
			return equiv((BooleanExpression) arg0, (BooleanExpression) arg1);
		case CHAR: {
			SymbolicOperator op0 = arg0.operator();
			SymbolicOperator op1 = arg1.operator();

			if (op0 == SymbolicOperator.CONCRETE
					&& op1 == SymbolicOperator.CONCRETE) {
				return bool(arg0.argument(0).equals(arg1.argument(0)));
			}
			return booleanFactory.booleanExpression(SymbolicOperator.EQUALS,
					arg0, arg1);
		}
		case INTEGER:
		case REAL:
			return numericFactory.equals((NumericExpression) arg0,
					(NumericExpression) arg1);
		case ARRAY: {
			NumericExpression length = length(arg0);

			if (!(type instanceof SymbolicCompleteArrayType)
					|| !(arg1.type() instanceof SymbolicCompleteArrayType))
				result = and(result,
						equals(length, length(arg1), quantifierDepth));
			if (result.isFalse())
				return result;
			else {
				NumericSymbolicConstant index = intBoundVar(quantifierDepth);

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
				result = and(result, booleanFactory.booleanExpression(
						SymbolicOperator.EQUALS, arg0, arg1));
			} else {
				SymbolicConstant[] boundVariables = new SymbolicConstant[numInputs];
				SymbolicSequence<?> sequence;
				BooleanExpression expr;

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

			return result;
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
				BooleanExpression expr = falseExpr;

				for (int i = 0; i < numTypes; i++) {
					IntObject index = intObject(i);
					BooleanExpression clause = result;

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
			throw ierr("Unknown type: " + type);
		}
	}

	private BooleanExpression compatibleTypeSequence(SymbolicTypeSequence seq0,
			SymbolicTypeSequence seq1, int nestingDepth) {
		int size = seq0.numTypes();

		if (size != seq1.numTypes())
			return falseExpr;
		if (size == 0)
			return trueExpr;
		else {
			BooleanExpression result = compatible(seq0.getType(0),
					seq1.getType(0), nestingDepth);

			if (size > 1)
				for (int i = 1; i < size; i++)
					result = and(
							result,
							compatible(seq0.getType(i), seq1.getType(i),
									nestingDepth));
			return result;
		}
	}

	protected BooleanExpression forallIntConcrete(
			NumericSymbolicConstant index, IntegerNumber low,
			IntegerNumber high, BooleanExpression predicate) {
		BooleanExpression result = trueExpr;

		for (IntegerNumber i = low; numberFactory.compare(i, high) < 0; i = numberFactory
				.increment(i)) {
			SymbolicExpression iExpression = number(numberObject(i));
			BooleanExpression substitutedPredicate = (BooleanExpression) simpleSubstituter(
					index, iExpression).apply(predicate);

			result = and(result, substitutedPredicate);
		}
		return result;
	}

	protected BooleanExpression existsIntConcrete(SymbolicConstant index,
			IntegerNumber low, IntegerNumber high, BooleanExpression predicate) {
		BooleanExpression result = falseExpr;

		for (IntegerNumber i = low; numberFactory.compare(i, high) < 0; i = numberFactory
				.increment(i)) {
			SymbolicExpression iExpression = number(numberObject(i));
			BooleanExpression substitutedPredicate = (BooleanExpression) simpleSubstituter(
					index, iExpression).apply(predicate);

			result = or(result, substitutedPredicate);
		}
		return result;
	}

	protected SymbolicExpression tupleUnsafe(SymbolicTupleType type,
			SymbolicSequence<?> components) {
		return expression(SymbolicOperator.CONCRETE, type, components);
	}

	// Public methods...

	public NumericExpressionFactory numericExpressionFactory() {
		return numericFactory;
	}

	// Public methods implementing SymbolicUniverse...

	@Override
	public boolean getShowQueries() {
		return showQueries;
	}

	@Override
	public void setShowQueries(boolean value) {
		this.showQueries = value;
	}

	@Override
	public boolean getShowProverQueries() {
		return showProverQueries;
	}

	@Override
	public void setShowProverQueries(boolean value) {
		this.showProverQueries = value;
	}

	@Override
	public PrintStream getOutputStream() {
		return out;
	}

	@Override
	public void setOutputStream(PrintStream out) {
		this.out = out;
	}

	@Override
	public SymbolicObject canonic(SymbolicObject object) {
		return objectFactory.canonic(object);
	}

	/**
	 * For exists and forall, must provide an instance of
	 * SymbolicConstantExpressionIF as arg0. Cannot be applied to make concrete
	 * expressions or SymbolicConstantExpressionIF. There are separate methods
	 * for those.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SymbolicExpression make(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] args) {
		int numArgs = args.length;

		switch (operator) {
		case ADD: // 1 or 2 args
			if (numArgs == 1) // collection
				return add((Iterable<? extends NumericExpression>) args[0]);
			else
				return add((NumericExpression) args[0],
						(NumericExpression) args[1]);
		case AND: // 1 or 2 args
			if (numArgs == 1) // collection
				return and((Iterable<? extends BooleanExpression>) args[0]);
			else
				return and((BooleanExpression) args[0],
						(BooleanExpression) args[1]);
		case APPLY: // 2 args: function and sequence
			return apply((SymbolicExpression) args[0],
					(SymbolicSequence<?>) args[1]);
		case ARRAY_LAMBDA:
			return arrayLambda((SymbolicCompleteArrayType) type,
					(SymbolicExpression) args[0]);
		case ARRAY_READ:
			return arrayRead((SymbolicExpression) args[0],
					(NumericExpression) args[1]);
		case ARRAY_WRITE:
			return arrayWrite((SymbolicExpression) args[0],
					(NumericExpression) args[1], (SymbolicExpression) args[2]);
		case CAST:
			return cast(type, (SymbolicExpression) args[0]);
		case CONCRETE:
			if (type.isNumeric())
				return canonic(numericFactory.number((NumberObject) args[0]));
			else
				return expression(SymbolicOperator.CONCRETE, type, args[0]);
		case COND:
			return cond((BooleanExpression) args[0],
					(SymbolicExpression) args[1], (SymbolicExpression) args[2]);
		case DENSE_ARRAY_WRITE:
			return denseArrayWrite((SymbolicExpression) args[0],
					(SymbolicSequence<?>) args[1]);
		case DENSE_TUPLE_WRITE:
			return denseTupleWrite((SymbolicExpression) args[0],
					(SymbolicSequence<?>) args[1]);
		case DIVIDE:
			return divide((NumericExpression) args[0],
					(NumericExpression) args[1]);
		case EQUALS:
			return equals((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case EXISTS:
			return exists((SymbolicConstant) args[0],
					(BooleanExpression) args[1]);
		case FORALL:
			return forall((SymbolicConstant) args[0],
					(BooleanExpression) args[1]);
		case INT_DIVIDE:
			return divide((NumericExpression) args[0],
					(NumericExpression) args[1]);
		case LAMBDA:
			return lambda((SymbolicConstant) args[0],
					(SymbolicExpression) args[1]);
		case LENGTH:
			return length((SymbolicExpression) args[0]);
		case LESS_THAN:
			return lessThan((NumericExpression) args[0],
					(NumericExpression) args[1]);
		case LESS_THAN_EQUALS:
			return lessThanEquals((NumericExpression) args[0],
					(NumericExpression) args[1]);
		case MODULO:
			return modulo((NumericExpression) args[0],
					(NumericExpression) args[1]);
		case MULTIPLY:
			if (numArgs == 1) // collection
				return multiply((Iterable<? extends NumericExpression>) args[0]);
			else
				return multiply((NumericExpression) args[0],
						(NumericExpression) args[1]);
		case NEGATIVE:
			return minus((NumericExpression) args[0]);
		case NEQ:
			return neq((SymbolicExpression) args[0],
					(SymbolicExpression) args[1]);
		case NOT:
			return not((BooleanExpression) args[0]);
		case OR: {
			if (numArgs == 1) // collection
				return or((Iterable<? extends BooleanExpression>) args[0]);
			else
				return or((BooleanExpression) args[0],
						(BooleanExpression) args[1]);
		}
		case POWER: // exponent could be expression or int constant
			if (args[1] instanceof SymbolicExpression)
				return power((NumericExpression) args[0],
						(NumericExpression) args[1]);
			else
				return power((NumericExpression) args[0], (IntObject) args[1]);
		case SUBTRACT:
			return subtract((NumericExpression) args[0],
					(NumericExpression) args[1]);
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
			throw ierr("Unknown expression kind: " + operator);
		}
	}

	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	@Override
	public NumericExpression add(Iterable<? extends NumericExpression> args) {
		if (args == null)
			throw err("Argument args to method add was null");

		Iterator<? extends NumericExpression> iter = args.iterator();

		if (!iter.hasNext())
			throw err("Iterable argument to add was empty but should have at least one element");
		else {
			NumericExpression result = iter.next();

			while (iter.hasNext()) {
				NumericExpression next = iter.next();

				result = add(result, next);
			}
			return result;
		}
	}

	/**
	 * Cannot assume anything about the collection of arguments. Therefore just
	 * apply the binary and operator to them in order.
	 */
	@Override
	public BooleanExpression and(Iterable<? extends BooleanExpression> args) {
		BooleanExpression result = trueExpr;

		for (BooleanExpression arg : args)
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
	public BooleanExpression and(BooleanExpression arg0, BooleanExpression arg1) {
		return booleanFactory.and(arg0, arg1);
	}

	@Override
	public SymbolicType pureType(SymbolicType type) {
		return typeFactory.pureType(type);
	}

	@Override
	public SymbolicType booleanType() {
		return booleanType;
	}

	@Override
	public SymbolicIntegerType integerType() {
		return integerType;
	}

	@Override
	public SymbolicIntegerType herbrandIntegerType() {
		return typeFactory.herbrandIntegerType();
	}

	@Override
	public SymbolicRealType realType() {
		return realType;
	}

	@Override
	public SymbolicIntegerType boundedIntegerType(NumericExpression min,
			NumericExpression max, boolean cyclic) {
		return typeFactory.boundedIntegerType(min, max, cyclic);
	}

	@Override
	public SymbolicRealType herbrandRealType() {
		return typeFactory.herbrandRealType();
	}

	@Override
	public SymbolicType characterType() {
		return typeFactory.characterType();
	}

	@Override
	public SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent) {
		return typeFactory.arrayType(elementType, extent);
	}

	@Override
	public SymbolicArrayType arrayType(SymbolicType elementType) {
		return typeFactory.arrayType(elementType);
	}

	public SymbolicTypeSequence typeSequence(SymbolicType[] types) {
		return typeFactory.sequence(types);
	}

	public SymbolicTypeSequence typeSequence(
			Iterable<? extends SymbolicType> types) {
		return typeFactory.sequence(types);
	}

	public SymbolicTupleType tupleType(StringObject name,
			SymbolicTypeSequence fieldTypes) {
		return typeFactory.tupleType(name, fieldTypes);
	}

	@Override
	public SymbolicTupleType tupleType(StringObject name,
			Iterable<? extends SymbolicType> types) {
		return tupleType(name, typeSequence(types));
	}

	public SymbolicFunctionType functionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType) {
		return typeFactory.functionType(inputTypes, outputType);
	}

	@Override
	public SymbolicFunctionType functionType(
			Iterable<? extends SymbolicType> inputTypes, SymbolicType outputType) {
		return typeFactory.functionType(typeSequence(inputTypes), outputType);
	}

	public SymbolicUnionType unionType(StringObject name,
			SymbolicTypeSequence memberTypes) {
		return typeFactory.unionType(name, memberTypes);
	}

	@Override
	public SymbolicUnionType unionType(StringObject name,
			Iterable<? extends SymbolicType> memberTypes) {
		return typeFactory.unionType(name, typeSequence(memberTypes));
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
	public BooleanObject booleanObject(boolean value) {
		return objectFactory.booleanObject(value);
	}

	@Override
	public CharObject charObject(char value) {
		return objectFactory.charObject(value);
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
		if (type.isNumeric())
			return numericFactory.symbolicConstant(name, type);
		if (type.isBoolean())
			return booleanFactory.booleanSymbolicConstant(name);
		return expressionFactory.symbolicConstant(name, type);
	}

	@Override
	public SymbolicExpression nullExpression() {
		return nullExpression;
	}

	@Override
	public NumericExpression number(NumberObject numberObject) {
		return numericFactory.number(numberObject);
	}

	@Override
	public NumericExpression integer(int value) {
		return number(numberObject(numberFactory.integer(value)));
	}

	@Override
	public NumericExpression rational(double value) {
		return number(numberObject(numberFactory.rational(Double
				.toString(value))));
	}

	@Override
	public NumericExpression rational(int numerator, int denominator) {
		return number(numberObject(numberFactory.divide(
				numberFactory.rational(numberFactory.integer(numerator)),
				numberFactory.rational(numberFactory.integer(denominator)))));
	}

	@Override
	public NumericExpression zeroInt() {
		return numericFactory.zeroInt();
	}

	@Override
	public NumericExpression zeroReal() {
		return numericFactory.zeroReal();
	}

	@Override
	public NumericExpression oneInt() {
		return numericFactory.oneInt();
	}

	@Override
	public NumericExpression oneReal() {
		return numericFactory.oneReal();
	}

	@Override
	public SymbolicExpression character(char theChar) {
		CharObject charObject = (CharObject) canonic(charObject(theChar));

		return expression(SymbolicOperator.CONCRETE,
				typeFactory.characterType(), charObject);
	}

	@Override
	public Character extractCharacter(SymbolicExpression expression) {
		if (expression.type().typeKind() == SymbolicTypeKind.CHAR
				&& expression.operator() == SymbolicOperator.CONCRETE)
			return ((CharObject) expression.argument(0)).getChar();
		return null;
	}

	@Override
	public SymbolicExpression stringExpression(String theString) {
		List<SymbolicExpression> charExprList = new LinkedList<SymbolicExpression>();
		int numChars = theString.length();

		for (int i = 0; i < numChars; i++)
			charExprList.add(character(theString.charAt(i)));
		return array(typeFactory.characterType(), charExprList);
	}

	private void checkSameType(SymbolicExpression arg0,
			SymbolicExpression arg1, String message) {
		if (!arg0.type().equals(arg1.type()))
			throw err(message + ".\narg0: " + arg0 + "\narg0 type: "
					+ arg0.type() + "\narg1: " + arg1 + "\narg1 type: "
					+ arg1.type());
	}

	@Override
	public NumericExpression add(NumericExpression arg0, NumericExpression arg1) {
		checkSameType(arg0, arg1, "Arguments to add had different types");
		return numericFactory.add(arg0, arg1);
	}

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		checkSameType(arg0, arg1, "Arguments to subtract had different types");
		return numericFactory.subtract(arg0, arg1);
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		checkSameType(arg0, arg1, "Arguments to multiply had different types");
		return numericFactory.multiply(arg0, arg1);
	}

	@Override
	public NumericExpression multiply(Iterable<? extends NumericExpression> args) {
		Iterator<? extends NumericExpression> iter = args.iterator();

		if (!iter.hasNext())
			throw err("Iterable argument to multiply was empty but should have"
					+ " at least one element");
		else {
			NumericExpression result = iter.next();

			while (iter.hasNext())
				result = multiply(result, iter.next());
			return result;
		}
	}

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		checkSameType(arg0, arg1, "Arguments to divide had different types");
		return numericFactory.divide(arg0, arg1);
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		if (!arg0.type().isInteger())
			throw err("Argument arg0 to modulo did not have integer type.\n"
					+ "\narg0: " + arg0 + "\narg0 type: " + arg0.type());
		if (!arg1.type().isInteger())
			throw err("Argument arg1 to modulo did not have integer type.\n"
					+ "\narg0: " + arg1 + "\narg0 type: " + arg1.type());
		return numericFactory.modulo(arg0, arg1);
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
		return numericFactory.minus(arg);
	}

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		if (exponent.isNegative())
			throw err("Argument exponent to method power was negative."
					+ "\nexponent: " + exponent);
		return numericFactory.power(base, exponent);
	}

	@Override
	public NumericExpression power(NumericExpression base, int exponent) {
		return power(base, intObject(exponent));
	}

	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		return numericFactory.power(base, exponent);
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression.operator() == SymbolicOperator.CONCRETE) {
			SymbolicObject object = expression.argument(0);

			if (object.symbolicObjectKind() == SymbolicObjectKind.NUMBER)
				return ((NumberObject) object).getNumber();
		}
		return null;
	}

	@Override
	public BooleanExpression bool(BooleanObject object) {
		return booleanFactory.symbolic(object);
	}

	@Override
	public BooleanExpression bool(boolean value) {
		return booleanFactory.symbolic(value);
	}

	@Override
	public void setBooleanExpressionSimplification(boolean value) {
		booleanFactory.setBooleanExpressionSimplification(value);
	}

	@Override
	public boolean getBooleanExpressionSimplification() {
		return booleanFactory.getBooleanExpressionSimplification();
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
	public BooleanExpression or(BooleanExpression arg0, BooleanExpression arg1) {
		return booleanFactory.or(arg0, arg1);
	}

	/**
	 * Assume nothing about the list of args.
	 */
	@Override
	public BooleanExpression or(Iterable<? extends BooleanExpression> args) {
		return booleanFactory.or(args);
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
	public BooleanExpression not(BooleanExpression arg) {
		SymbolicOperator operator = arg.operator();

		switch (operator) {
		case LESS_THAN:
			return numericFactory.notLessThan(
					(NumericExpression) arg.argument(0),
					(NumericExpression) arg.argument(1));
		case LESS_THAN_EQUALS:
			return numericFactory.notLessThanEquals(
					(NumericExpression) arg.argument(0),
					(NumericExpression) arg.argument(1));
		default:
			return booleanFactory.not(arg);
		}
	}

	@Override
	public BooleanExpression implies(BooleanExpression arg0,
			BooleanExpression arg1) {
		return booleanFactory.implies(arg0, arg1);
	}

	@Override
	public BooleanExpression equiv(BooleanExpression arg0,
			BooleanExpression arg1) {
		return booleanFactory.equiv(arg0, arg1);
	}

	// @Override
	// public SymbolicExpression substitute(SymbolicExpression expression,
	// SymbolicConstant variable, SymbolicExpression value) {
	// return substituteSymbolicConstants(expression,
	// new SingletonMap<SymbolicConstant, SymbolicExpression>(
	// variable, value));
	// }

	@Override
	public BooleanExpression forallInt(NumericSymbolicConstant index,
			NumericExpression low, NumericExpression high,
			BooleanExpression predicate) {
		IntegerNumber lowNumber = (IntegerNumber) extractNumber(low);

		if (lowNumber != null) {
			IntegerNumber highNumber = (IntegerNumber) extractNumber(high);

			if (highNumber != null
					&& numberFactory.compare(
							numberFactory.subtract(highNumber, lowNumber),
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

	@Override
	public BooleanExpression existsInt(NumericSymbolicConstant index,
			NumericExpression low, NumericExpression high,
			BooleanExpression predicate) {
		IntegerNumber lowNumber = (IntegerNumber) extractNumber(low);

		if (lowNumber != null) {
			IntegerNumber highNumber = (IntegerNumber) extractNumber(high);

			if (highNumber != null
					&& numberFactory.compare(
							numberFactory.subtract(highNumber, lowNumber),
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

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {

		return numericFactory.lessThan(arg0, arg1);
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		return numericFactory.lessThanEquals(arg0, arg1);
	}

	@Override
	public BooleanExpression equals(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		if (arg0.isNumeric() && arg1.isNumeric())
			return numericFactory.equals((NumericExpression) arg0,
					(NumericExpression) arg1);
		return equals(arg0, arg1, 0);
	}

	@Override
	public BooleanExpression neq(SymbolicExpression arg0,
			SymbolicExpression arg1) {
		if (arg0.isNumeric())
			return numericFactory.neq((NumericExpression) arg0,
					(NumericExpression) arg1);
		return not(equals(arg0, arg1));
	}

	@Override
	public BooleanExpression divides(NumericExpression a, NumericExpression b) {
		return equals(modulo(b, a), zeroInt());
	}

	private <T extends SymbolicExpression> SymbolicSequence<T> sequence(
			Iterable<T> elements) {
		if (elements instanceof SymbolicSequence<?>)
			return (SymbolicSequence<T>) elements;
		return collectionFactory.sequence(elements);
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
	public BooleanExpression forall(SymbolicConstant boundVariable,
			BooleanExpression predicate) {
		return booleanFactory.forall(boundVariable, predicate);
	}

	@Override
	public BooleanExpression exists(SymbolicConstant boundVariable,
			BooleanExpression predicate) {
		return booleanFactory.exists(boundVariable, predicate);
	}

	@Override
	public Boolean extractBoolean(BooleanExpression expression) {
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
			Iterable<? extends SymbolicExpression> argumentSequence) {
		SymbolicOperator op0 = function.operator();
		SymbolicExpression result;

		if (op0 == SymbolicOperator.LAMBDA) {
			Iterator<? extends SymbolicExpression> iter = argumentSequence
					.iterator();
			SymbolicExpression arg;

			if (!iter.hasNext())
				throw err("Argument argumentSequence to method apply is empty"
						+ " but since function is a lambda expression it should"
						+ " have at least one element");
			arg = iter.next();
			assert !iter.hasNext();
			if (iter.hasNext())
				throw err("Argument argumentSequence to method apply has more than one element"
						+ " but since function is a lambda expression it should"
						+ " have exactly one element");
			// function.argument(0): bound symbolic constant : dummy variable
			// function.argument(1): symbolic expression: body of function
			result = simpleSubstituter((SymbolicConstant) function.argument(0),
					arg).apply((SymbolicExpression) function.argument(1));
		} else {
			// TODO check the argument types...
			result = expression(SymbolicOperator.APPLY,
					((SymbolicFunctionType) function.type()).outputType(),
					function, sequence(argumentSequence));
		}
		return result;
	}

	@Override
	public SymbolicExpression unionInject(SymbolicUnionType unionType,
			IntObject memberIndex, SymbolicExpression object) {
		SymbolicType objectType = object.type();
		int indexInt = memberIndex.getInt();
		int numMembers = unionType.sequence().numTypes();
		SymbolicType memberType;

		if (indexInt < 0 || indexInt >= numMembers)
			throw err("Argument memberIndex to unionInject is out of range.\n"
					+ "unionType: " + unionType + "\nSaw: " + indexInt
					+ "\nExpected: integer in range [0," + (numMembers - 1)
					+ "]");
		memberType = unionType.sequence().getType(indexInt);
		if (incompatible(memberType, objectType))
			throw err("Argument object of unionInject has the wrong type.\n"
					+ "Its type should agree with the type of member "
					+ memberIndex + " of the union type.\n" + "Expected: "
					+ memberType + "\n.Saw: " + objectType + ": " + object);
		// inject_i(extract_i(x))=x...
		if (object.operator() == SymbolicOperator.UNION_EXTRACT
				&& unionType.equals(((SymbolicExpression) object.argument(1))
						.type()) && memberIndex.equals(object.argument(0)))
			return (SymbolicExpression) object.argument(1);
		return expression(SymbolicOperator.UNION_INJECT, unionType,
				memberIndex, object);
	}

	@Override
	public BooleanExpression unionTest(IntObject memberIndex,
			SymbolicExpression object) {
		if (object.operator() == SymbolicOperator.UNION_INJECT)
			return object.argument(0).equals(memberIndex) ? trueExpr
					: falseExpr;
		return booleanFactory.booleanExpression(SymbolicOperator.UNION_TEST,
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
				((SymbolicUnionType) object.type()).sequence().getType(
						memberIndex.getInt()), memberIndex, object);
	}

	/**
	 * Need to know type of elements in case empty.
	 */
	@Override
	public SymbolicExpression array(SymbolicType elementType,
			Iterable<? extends SymbolicExpression> elements) {
		int count = 0;

		if (elementType == null)
			throw err("Argument elementType to method array was null");
		if (elements == null)
			throw err("Argument elements to method array was null");
		for (SymbolicExpression element : elements) {
			if (element == null || element.isNull())
				throw err("Element " + count
						+ " of array elements argument has illegal value:\n"
						+ element);
			if (incompatible(elementType, element.type()))
				throw err("Element "
						+ count
						+ " of array elements argument had incompatible type:\n"
						+ "Expected: " + elementType + "\nSaw: "
						+ element.type());
			count++;
		}
		return expression(SymbolicOperator.CONCRETE,
				arrayType(elementType, integer(count)), sequence(elements));
	}

	@Override
	public SymbolicExpression append(SymbolicExpression concreteArray,
			SymbolicExpression element) {
		SymbolicType type = concreteArray.type();

		if (type.typeKind() != SymbolicTypeKind.ARRAY)
			throw err("argument concreteArray not array type:\n"
					+ concreteArray);
		if (concreteArray.operator() != SymbolicOperator.CONCRETE) {
			throw err("append invoked on non-concrete array:\n" + concreteArray);
		} else {
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> elements = (SymbolicSequence<SymbolicExpression>) concreteArray
					.argument(0);
			SymbolicType elementType = ((SymbolicArrayType) type).elementType();
			SymbolicExpression result;

			if (element == null || element.isNull())
				throw err("Element to append has illegal value:\n" + element);
			if (incompatible(elementType, element.type()))
				throw err("Element to append has incompatible type:\n"
						+ "Expected: " + elementType + "\nSaw: "
						+ element.type());
			elements = elements.add(element);
			type = arrayType(elementType, integer(elements.size()));
			result = expression(SymbolicOperator.CONCRETE, type,
					sequence(elements));
			return result;
		}
	}

	@Override
	public SymbolicExpression removeElementAt(SymbolicExpression concreteArray,
			int index) {
		SymbolicType type = concreteArray.type();

		if (type.typeKind() != SymbolicTypeKind.ARRAY)
			throw err("argument concreteArray not array type:\n"
					+ concreteArray);
		if (concreteArray.operator() != SymbolicOperator.CONCRETE) {
			throw err("argument concreteArray is not concrete:\n"
					+ concreteArray);
		} else {
			SymbolicType elementType = ((SymbolicArrayType) type).elementType();
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> elements = (SymbolicSequence<SymbolicExpression>) concreteArray
					.argument(0);
			int length = elements.size();
			SymbolicExpression result;

			if (index < 0 || index >= length)
				throw err("Index in removeElementAt out of range:\narray: "
						+ concreteArray + "\nlength: " + length + "\nindex: "
						+ index);
			elements = elements.remove(index);
			type = arrayType(elementType, integer(elements.size()));
			result = expression(SymbolicOperator.CONCRETE, type,
					sequence(elements));
			return result;
		}
	}

	@Override
	public SymbolicExpression emptyArray(SymbolicType elementType) {
		return expression(SymbolicOperator.CONCRETE,
				arrayType(elementType, zeroInt()),
				collectionFactory.emptySequence());
	}

	@Override
	public SymbolicExpression constantArray(SymbolicType elementType,
			NumericExpression length, SymbolicExpression value) {
		SymbolicCompleteArrayType arrayType = arrayType(elementType, length);
		IntegerNumber lengthNumber = (IntegerNumber) extractNumber(length);
		SymbolicExpression result;

		if (lengthNumber == null) {
			result = arrayLambda(arrayType, lambda(arrayIndex, value));
		} else {
			int lengthInt = lengthNumber.intValue();
			SymbolicExpression[] elements = new SymbolicExpression[lengthInt];

			Arrays.fill(elements, value);
			result = expression(SymbolicOperator.CONCRETE, arrayType,
					collectionFactory.sequence(elements));
		}
		return result;
	}

	@Override
	public NumericExpression length(SymbolicExpression array) {
		if (array == null)
			throw err("Argument array to method length was null");
		if (!(array.type() instanceof SymbolicArrayType))
			throw err("Argument array to method length does not have array type."
					+ "\narray: " + array + "\ntype: " + array.type());
		else {
			SymbolicArrayType type = (SymbolicArrayType) array.type();

			if (type.isComplete())
				return (NumericExpression) ((SymbolicCompleteArrayType) type)
						.extent();
			else
				return numericFactory.expression(SymbolicOperator.LENGTH,
						integerType, array);
		}
	}

	@Override
	public SymbolicExpression arrayRead(SymbolicExpression array,
			NumericExpression index) {
		if (array == null)
			throw err("Argument array to method arrayRead is null.");
		if (index == null)
			throw err("Argument index to method arrayRead is null.");
		if (!(array.type() instanceof SymbolicArrayType))
			throw err("Argument array to method arrayRead does not have array type."
					+ "\narray: " + array + "\ntype: " + array.type());
		else {
			SymbolicArrayType arrayType = (SymbolicArrayType) array.type();
			SymbolicOperator op = array.operator();
			IntegerNumber indexNumber = (IntegerNumber) extractNumber(index);

			if (indexNumber != null) {
				if (indexNumber.signum() < 0)
					throw err("Argument index to arrayRead is negative."
							+ "\nindex: " + indexNumber);
				if (arrayType.isComplete()) {
					IntegerNumber lengthNumber = (IntegerNumber) extractNumber(((SymbolicCompleteArrayType) arrayType)
							.extent());

					if (lengthNumber != null
							&& numberFactory.compare(indexNumber, lengthNumber) >= 0)
						throw err("Array index out of bounds in method arrayRead."
								+ "\narray: "
								+ array
								+ "\nextent: "
								+ lengthNumber + "\nindex: " + indexNumber);
				}
				if (op == SymbolicOperator.CONCRETE)
					return ((SymbolicSequence<?>) array.argument(0))
							.get(indexNumber.intValue());
				else if (op == SymbolicOperator.DENSE_ARRAY_WRITE) {
					SymbolicExpression origin = (SymbolicExpression) array
							.argument(0);

					if (numberFactory.compare(indexNumber, denseArrayMaxSize) < 0) {
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
				} else if (op == SymbolicOperator.ARRAY_LAMBDA) {
					return apply((SymbolicExpression) array.argument(0),
							Arrays.asList(index));
				}
			}
			return expression(SymbolicOperator.ARRAY_READ,
					((SymbolicArrayType) array.type()).elementType(), array,
					index);
		}
	}

	private SymbolicExpression arrayWrite_noCheck(SymbolicExpression array,
			SymbolicArrayType arrayType, NumericExpression index,
			SymbolicExpression value) {
		IntegerNumber indexNumber = (IntegerNumber) extractNumber(index);
		IntegerNumber lengthNumber = null; // length of array type if complete

		if (indexNumber != null) {
			int indexInt = indexNumber.intValue();
			SymbolicOperator op = array.operator();

			if (indexNumber.signum() < 0)
				throw err("Argument index to arrayWrite is negative."
						+ "\nindex: " + indexNumber);
			if (arrayType.isComplete()) {
				lengthNumber = (IntegerNumber) extractNumber(((SymbolicCompleteArrayType) arrayType)
						.extent());
				if (lengthNumber != null
						&& numberFactory.compare(indexNumber, lengthNumber) >= 0)
					throw err("Array index out of bounds in method arrayWrite."
							+ "\narray: " + array + "\nextent: " + lengthNumber
							+ "\nindex: " + indexNumber);
			}
			if (op == SymbolicOperator.CONCRETE) {
				@SuppressWarnings("unchecked")
				SymbolicSequence<SymbolicExpression> sequence = (SymbolicSequence<SymbolicExpression>) array
						.argument(0);

				return expression(op, arrayType, sequence.set(indexInt, value));
			}
			if (indexInt < DENSE_ARRAY_MAX_SIZE) {
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
				// if the length of the sequence is the extent of the array type
				// AND the sequence has no null values, you can forget the
				// origin and make a concrete array value, since every cell
				// has been over-written...
				if (lengthNumber != null && sequence.getNumNull() == 0
						&& lengthNumber.intValue() == sequence.size()) {
					return expression(SymbolicOperator.CONCRETE, arrayType,
							sequence);
				}
				return expression(SymbolicOperator.DENSE_ARRAY_WRITE,
						arrayType, origin, sequence);
			}
		}
		return expression(SymbolicOperator.ARRAY_WRITE, arrayType, array,
				index, value);
	}

	@Override
	public SymbolicExpression arrayWrite(SymbolicExpression array,
			NumericExpression index, SymbolicExpression value) {
		if (array == null)
			throw err("Argument array to method arrayWrite is null.");
		if (index == null)
			throw err("Argument index to method arrayWrite is null.");
		if (value == null)
			throw err("Argument value to method arrayWrite is null.");
		if (!(array.type() instanceof SymbolicArrayType))
			throw err("Argument array to method arrayWrite does not have array type."
					+ "\narray: " + array + "\ntype: " + array.type());
		if (!index.type().isInteger())
			throw err("Argument index to method arrayWrite does not have integer type."
					+ "\nindex: " + index + "\ntype: " + index.type());
		if (value.isNull())
			throw err("Argument value to method arrayWrite is NULL.");
		else {
			SymbolicArrayType arrayType = (SymbolicArrayType) array.type();

			if (incompatible(arrayType.elementType(), value.type()))
				throw err("Argument value to method arrayWrite has incompatible type."
						+ "\nvalue: "
						+ value
						+ "\ntype: "
						+ value.type()
						+ "\nExpected: " + arrayType.elementType());
			return arrayWrite_noCheck(array, arrayType, index, value);
		}
	}

	/**
	 * Returns an iterable object equivalent to given one except that any "null"
	 * values are replaced by the SymbolicExpression NULL. Also, trailing
	 * nulls/NULLs are removed.
	 * 
	 * @param values
	 *            any iterable of symbolic expressions, which may contain null
	 *            values
	 * @return an iterable object with nulls replaced with NULLs
	 */
	private <T extends SymbolicExpression> Iterable<? extends SymbolicExpression> replaceNulls(
			Iterable<T> values) {
		int count = 0;
		int lastNonNullIndex = -1;

		for (T value : values) {
			if (value == null) { // element in position count is null
				LinkedList<SymbolicExpression> list = new LinkedList<SymbolicExpression>();
				Iterator<T> iter = values.iterator();

				for (int i = 0; i < count; i++)
					list.add(iter.next());
				list.add(nullExpression);
				iter.next();
				count++;
				while (iter.hasNext()) {
					T element = iter.next();

					list.add(element == null ? nullExpression : element);
					if (element != null && !element.isNull())
						lastNonNullIndex = count;
					count++;
				}
				// count is size of list, lastNonNullIndex is index of
				// last non-null element
				if (lastNonNullIndex < count - 1) {
					// remove elements lastNonNullIndex+1,...,count-1
					list.subList(lastNonNullIndex + 1, count).clear();
				}
				return list;
			}
			if (!value.isNull())
				lastNonNullIndex = count;
			count++;
		}
		if (lastNonNullIndex < count - 1) {
			LinkedList<SymbolicExpression> list = new LinkedList<SymbolicExpression>();
			Iterator<T> iter = values.iterator();

			for (int i = 0; i <= lastNonNullIndex; i++)
				list.add(iter.next());
			return list;
		}
		return values;
	}

	@Override
	public SymbolicExpression denseArrayWrite(SymbolicExpression array,
			Iterable<? extends SymbolicExpression> values) {
		SymbolicType theArraysType = array.type();

		if (!(theArraysType instanceof SymbolicArrayType))
			throw new SARLException(
					"Argument 0 of denseArrayWrite must have array type but had type "
							+ theArraysType);
		else {
			SymbolicArrayType arrayType = (SymbolicArrayType) theArraysType;
			SymbolicType elementType = arrayType.elementType();
			int count = 0;
			int numNulls = 0;

			values = replaceNulls(values);
			for (SymbolicExpression value : values) {
				if (value.isNull())
					numNulls++;
				else if (incompatible(elementType, value.type()))
					throw err("Element "
							+ count
							+ " of values argument to denseArrayWrite has incompatible type.\n"
							+ "Expected: " + elementType + "\nSaw: "
							+ value.type());
				count++;
			}
			if (numNulls == 0 && arrayType.isComplete()) {
				IntegerNumber lengthNumber = (IntegerNumber) extractNumber(((SymbolicCompleteArrayType) arrayType)
						.extent());

				if (lengthNumber != null && count == lengthNumber.intValue())
					return expression(SymbolicOperator.CONCRETE, arrayType,
							sequence(values));
			}
			return expression(SymbolicOperator.DENSE_ARRAY_WRITE, arrayType,
					array, sequence(values));
		}
	}

	public SymbolicExpression denseTupleWrite(SymbolicExpression tuple,
			Iterable<? extends SymbolicExpression> values) {
		int count = 0;

		for (SymbolicExpression value : values) {
			if (value != null && !value.isNull()) {
				tuple = tupleWrite(tuple, intObject(count), value);
			}
			count++;
		}
		return tuple;
	}

	@Override
	public SymbolicExpression arrayLambda(SymbolicCompleteArrayType arrayType,
			SymbolicExpression function) {
		if (arrayType == null)
			throw err("Argument arrayType to method arrayLambda was null");
		if (function == null)
			throw err("Argument function to method arrayLambda was null");
		if (function.operator() != SymbolicOperator.LAMBDA)
			throw err("Function must be LAMBDA type");

		if (function.type().typeKind() != SymbolicTypeKind.FUNCTION)
			throw err("function must have a function type, not "
					+ function.type());

		SymbolicFunctionType functionType = (SymbolicFunctionType) function
				.type();
		SymbolicTypeSequence inputSeq = functionType.inputTypes();
		int numInputs = inputSeq.numTypes();

		if (numInputs != 1)
			throw err("function in array lambda must take one input, not "
					+ numInputs + ": " + functionType);

		SymbolicType inputType = inputSeq.getType(0);

		if (inputType.typeKind() != SymbolicTypeKind.INTEGER)
			throw err("input type of array lambda function must be integer, not "
					+ inputType + ": " + functionType);

		SymbolicType outputType = functionType.outputType();

		if (compatible(outputType, arrayType.elementType()).isFalse()) {
			throw err("Return type of array lambda function is incompatible with element type:\n"
					+ "element type: "
					+ arrayType.elementType()
					+ "\n"
					+ "lambda function type: "
					+ functionType
					+ "\n"
					+ "lambda function output type: " + outputType + "\n");
		}

		NumericExpression lengthExpression = arrayType.extent();
		Number lengthNumber = this.extractNumber(lengthExpression);

		if (lengthNumber != null) {
			int length = ((IntegerNumber) lengthNumber).intValue();

			if (length < DENSE_ARRAY_MAX_SIZE) {
				SymbolicExpression[] elements = new SymbolicExpression[length];
				SymbolicConstant boundVar = (SymbolicConstant) function
						.argument(0);
				SymbolicExpression elementExpr = (SymbolicExpression) function
						.argument(1);

				for (int i = 0; i < length; i++) {
					elements[i] = simpleSubstituter(boundVar, integer(i))
							.apply(elementExpr);
				}
				return expression(SymbolicOperator.CONCRETE, arrayType,
						collectionFactory.sequence(elements));
			}
		}
		return expression(SymbolicOperator.ARRAY_LAMBDA, arrayType, function);
	}

	@Override
	public SymbolicExpression tuple(SymbolicTupleType type,
			Iterable<? extends SymbolicExpression> components) {
		SymbolicTypeSequence fieldTypes = type.sequence();
		SymbolicSequence<? extends SymbolicExpression> sequence = sequence(components);
		int m = fieldTypes.numTypes();
		int n = sequence.size();

		if (n != m)
			throw err("In method tuple, tuple type has exactly" + m
					+ " components but sequence has length " + n);
		for (int i = 0; i < n; i++) {
			SymbolicType fieldType = fieldTypes.getType(i);
			SymbolicType componentType = sequence.get(i).type();

			if (incompatible(fieldType, componentType))
				throw err("Element "
						+ i
						+ " of components argument to method tuple has incompatible type.\n"
						+ "\nExpected: " + fieldType + "\nSaw: "
						+ componentType);
		}
		return expression(SymbolicOperator.CONCRETE, type, sequence);
	}

	@Override
	public SymbolicExpression tupleRead(SymbolicExpression tuple,
			IntObject index) {
		SymbolicType type = tuple.type();
		SymbolicOperator op = tuple.operator();
		int indexInt = index.getInt();

		if (type.typeKind() != SymbolicTypeKind.TUPLE)
			throw new SARLException(
					"Argument tuple to tupleRead does not have tuple type:\n"
							+ tuple);
		if (op == SymbolicOperator.CONCRETE)
			return ((SymbolicSequence<?>) tuple.argument(0)).get(indexInt);
		if (op == SymbolicOperator.DENSE_TUPLE_WRITE) {
			SymbolicExpression value = ((SymbolicSequence<?>) tuple.argument(1))
					.get(indexInt);

			if (!value.isNull())
				return value;
			return tupleRead((SymbolicExpression) tuple.argument(0), index);

		}
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

		if (incompatible(fieldType, valueType))
			throw err("Argument value to tupleWrite has incompatible type."
					+ "\nExpected: " + fieldType + "\nSaw: " + valueType);
		if (op == SymbolicOperator.CONCRETE) {
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> arg0 = (SymbolicSequence<SymbolicExpression>) tuple
					.argument(0);
			SymbolicExpression oldValue = arg0.get(indexInt);

			if (value == oldValue)
				return tuple;
			return expression(op, tupleType, arg0.set(indexInt, value));
		} else if (op == SymbolicOperator.DENSE_TUPLE_WRITE) {
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> sequence = (SymbolicSequence<SymbolicExpression>) tuple
					.argument(1);
			SymbolicExpression oldValue = sequence.get(indexInt);

			if (value == oldValue)
				return tuple;
			sequence = sequence.set(indexInt, value);
			for (SymbolicExpression x : sequence) {
				if (x == null || x.isNull())
					return expression(SymbolicOperator.DENSE_TUPLE_WRITE,
							tupleType, tuple.argument(0), sequence);
			}
			return expression(SymbolicOperator.CONCRETE, tupleType, sequence);
		} else {
			int numComponents = tupleType.sequence().numTypes();
			SymbolicExpression[] elementsArray = new SymbolicExpression[numComponents];
			SymbolicSequence<SymbolicExpression> sequence;

			for (int i = 0; i < numComponents; i++) {
				elementsArray[i] = nullExpression;
			}
			elementsArray[indexInt] = value;
			sequence = collectionFactory.sequence(elementsArray);
			if (numComponents <= 1)
				return expression(SymbolicOperator.CONCRETE, tupleType,
						sequence);
			else
				return expression(SymbolicOperator.DENSE_TUPLE_WRITE,
						tupleType, tuple, sequence);
		}
	}

	@Override
	public SymbolicExpression cast(SymbolicType newType,
			SymbolicExpression expression) {
		SymbolicType oldType = expression.type();

		if (oldType.equals(newType))
			return expression;
		if (oldType.isNumeric() && newType.isNumeric()) {
			return numericFactory.cast((NumericExpression) expression, newType);
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
		throw err("Cannot cast from type " + oldType + " to type " + newType
				+ ": " + expression);
	}

	@Override
	public SymbolicExpression cond(BooleanExpression predicate,
			SymbolicExpression trueValue, SymbolicExpression falseValue) {
		if (predicate.isTrue())
			return trueValue;
		if (predicate.isFalse())
			return falseValue;
		assert trueValue.type().equals(falseValue.type());
		return expression(SymbolicOperator.COND, trueValue.type(), predicate,
				trueValue, falseValue);
	}

	@Override
	public Comparator<SymbolicObject> comparator() {
		return objectComparator;
	}

	@Override
	public NumericExpression integer(long value) {
		return number(numberFactory.integer(value));
	}

	@Override
	public NumericExpression integer(BigInteger value) {
		return number(numberFactory.integer(value));
	}

	@Override
	public NumericExpression rational(int value) {
		return number(numberFactory.rational(numberFactory.integer(value)));
	}

	@Override
	public NumericExpression rational(long value) {
		return number(numberFactory.rational(numberFactory.integer(value)));
	}

	@Override
	public NumericExpression rational(BigInteger value) {
		return number(numberFactory.rational(numberFactory.integer(value)));
	}

	@Override
	public NumericExpression rational(float value) {
		return number(numberFactory.rational(Float.toString(value)));
	}

	@Override
	public NumericExpression rational(long numerator, long denominator) {
		return rational(BigInteger.valueOf(numerator),
				BigInteger.valueOf(denominator));
	}

	@Override
	public NumericExpression rational(BigInteger numerator,
			BigInteger denominator) {
		return number(numberFactory.rational(numerator, denominator));
	}

	@Override
	public NumericExpression number(Number number) {
		return number(numberObject(number));
	}

	@Override
	public BooleanExpression trueExpression() {
		return trueExpr;
	}

	@Override
	public BooleanExpression falseExpression() {
		return falseExpr;
	}

	@Override
	public int numValidCalls() {
		return validCount;
	}

	@Override
	public int numProverValidCalls() {
		return proverValidCount;
	}

	@Override
	public void incrementValidCount() {
		validCount++;
	}

	@Override
	public void incrementProverValidCount() {
		proverValidCount++;
	}

	@Override
	public <T extends SymbolicExpression> SymbolicCollection<T> basicCollection(
			Collection<T> javaCollection) {
		return collectionFactory.basicCollection(javaCollection);
	}

	@Override
	public SymbolicType referenceType() {
		return expressionFactory.referenceType();
	}

	@Override
	public ReferenceExpression nullReference() {
		return expressionFactory.nullReference();
	}

	@Override
	public SymbolicExpression dereference(SymbolicExpression value,
			ReferenceExpression reference) {
		if (value == null)
			throw new SARLException("dereference given null value");
		if (reference == null)
			throw new SARLException("dereference given null reference");
		switch (reference.referenceKind()) {
		case NULL:
			throw new SARLException(
					"Cannot dereference the null reference expression:\n"
							+ value + "\n" + reference);
		case IDENTITY:
			return value;
		case ARRAY_ELEMENT: {
			ArrayElementReference ref = (ArrayElementReference) reference;

			return arrayRead(dereference(value, ref.getParent()),
					ref.getIndex());
		}
		case TUPLE_COMPONENT: {
			TupleComponentReference ref = (TupleComponentReference) reference;

			return tupleRead(dereference(value, ref.getParent()),
					ref.getIndex());
		}
		case UNION_MEMBER: {
			UnionMemberReference ref = (UnionMemberReference) reference;

			return this.unionExtract(ref.getIndex(),
					dereference(value, ref.getParent()));
		}
		case OFFSET: {
			OffsetReference ref = (OffsetReference) reference;
			NumericExpression index = ref.getOffset();
			IntegerNumber indexNumber = (IntegerNumber) extractNumber(index);

			if (indexNumber == null || !indexNumber.isZero())
				throw new SARLException(
						"Cannot dereference an offset reference with non-zero offset:\n"
								+ reference + "\n" + value);
			return dereference(value, ref.getParent());
		}
		default:
			throw new SARLInternalException("Unknown reference kind: "
					+ reference);
		}
	}

	@Override
	public SymbolicType referencedType(SymbolicType type,
			ReferenceExpression reference) {
		if (reference == null)
			throw new SARLException("referencedType given null reference");
		if (type == null)
			throw new SARLException("referencedType given null type");
		switch (reference.referenceKind()) {
		case NULL:
			throw new SARLException(
					"Cannot compute referencedType of the null reference expression:\n"
							+ type + "\n" + reference);
		case IDENTITY:
			return type;
		case ARRAY_ELEMENT: {
			ArrayElementReference ref = (ArrayElementReference) reference;
			SymbolicType parentType = referencedType(type, ref.getParent());

			if (parentType instanceof SymbolicArrayType)
				return ((SymbolicArrayType) parentType).elementType();
			else
				throw new SARLException("Incompatible type and reference:\n"
						+ type + "\n" + reference);
		}
		case TUPLE_COMPONENT: {
			TupleComponentReference ref = (TupleComponentReference) reference;
			SymbolicType parentType = referencedType(type, ref.getParent());

			if (parentType instanceof SymbolicTupleType)
				return ((SymbolicTupleType) parentType).sequence().getType(
						ref.getIndex().getInt());
			else
				throw new SARLException("Incompatible type and reference:\n"
						+ type + "\n" + reference);
		}
		case UNION_MEMBER: {
			UnionMemberReference ref = (UnionMemberReference) reference;
			SymbolicType parentType = referencedType(type, ref.getParent());

			if (parentType instanceof SymbolicUnionType)
				return ((SymbolicUnionType) parentType).sequence().getType(
						ref.getIndex().getInt());
			else
				throw new SARLException("Incompatible type and reference:\n"
						+ type + "\n" + reference);
		}
		case OFFSET: {
			OffsetReference ref = (OffsetReference) reference;
			SymbolicType parentType = referencedType(type, ref.getParent());

			return parentType;
		}
		default:
			throw new SARLInternalException("Unknown reference kind: "
					+ reference);// unreachable
		}
	}

	@Override
	public ReferenceExpression identityReference() {
		return expressionFactory.identityReference();
	}

	@Override
	public ArrayElementReference arrayElementReference(
			ReferenceExpression arrayReference, NumericExpression index) {
		return expressionFactory.arrayElementReference(arrayReference, index);
	}

	@Override
	public TupleComponentReference tupleComponentReference(
			ReferenceExpression tupleReference, IntObject fieldIndex) {
		return expressionFactory.tupleComponentReference(tupleReference,
				fieldIndex);
	}

	@Override
	public UnionMemberReference unionMemberReference(
			ReferenceExpression unionReference, IntObject memberIndex) {
		return expressionFactory.unionMemberReference(unionReference,
				memberIndex);
	}

	@Override
	public OffsetReference offsetReference(ReferenceExpression reference,
			NumericExpression offset) {
		return expressionFactory.offsetReference(reference, offset);
	}

	@Override
	public SymbolicExpression assign(SymbolicExpression value,
			ReferenceExpression reference, SymbolicExpression subValue) {
		ReferenceKind kind;

		if (reference == null)
			throw new SARLException("assign given null reference");
		if (subValue == null)
			throw new SARLException("assign given null subValue");
		kind = reference.referenceKind();
		if (kind == ReferenceKind.IDENTITY)
			return subValue;
		if (value == null)
			throw new SARLException("assign given null value");
		switch (kind) {
		case NULL:
			throw new SARLException(
					"Cannot assign using the null reference expression:\n"
							+ value + "\n" + reference + "\n" + subValue);
		case ARRAY_ELEMENT: {
			ArrayElementReference ref = (ArrayElementReference) reference;
			ReferenceExpression arrayReference = ref.getParent();
			SymbolicExpression array = dereference(value, arrayReference);
			SymbolicExpression newArray = arrayWrite(array, ref.getIndex(),
					subValue);

			return assign(value, arrayReference, newArray);
		}
		case TUPLE_COMPONENT: {
			TupleComponentReference ref = (TupleComponentReference) reference;
			ReferenceExpression tupleReference = ref.getParent();
			SymbolicExpression tuple = dereference(value, tupleReference);
			SymbolicExpression newTuple = tupleWrite(tuple, ref.getIndex(),
					subValue);

			return assign(value, tupleReference, newTuple);
		}
		case UNION_MEMBER: {
			UnionMemberReference ref = (UnionMemberReference) reference;
			ReferenceExpression unionReference = ref.getParent();
			SymbolicExpression unionValue = dereference(value, unionReference);
			SymbolicUnionType unionType = (SymbolicUnionType) unionValue.type();
			SymbolicExpression newUnionValue = unionInject(unionType,
					ref.getIndex(), subValue);

			return assign(value, unionReference, newUnionValue);
		}
		case OFFSET: {
			OffsetReference ref = (OffsetReference) reference;
			NumericExpression index = ref.getOffset();
			IntegerNumber indexNumber = (IntegerNumber) extractNumber(index);

			if (indexNumber == null || !indexNumber.isZero()) // first case
																// unreachable
				throw new SARLException(
						"Cannot assign via an offset reference with non-zero offset:\n"
								+ reference + "\n" + value);
			return assign(value, ref.getParent(), subValue);
		}
		default: // unreachable
			throw new SARLInternalException("Unknown reference kind: "
					+ reference);
		}
	}

	public SymbolicExpression cleanBoundVariables(SymbolicExpression expr) {
		return cleaner.apply(expr);
	}

	@Override
	public SymbolicSetType setType(SymbolicType elementType) {
		return typeFactory.setType(elementType);
	}

	@Override
	public SymbolicMapType mapType(SymbolicType keyType, SymbolicType valueType) {
		return typeFactory.mapType(keyType, valueType);
	}

	@Override
	public SymbolicTupleType entryType(SymbolicMapType mapType) {
		return typeFactory.entryType(mapType);
	}

	@Override
	public SymbolicExpression insertElementAt(SymbolicExpression concreteArray,
			int index, SymbolicExpression value) {
		SymbolicType type = concreteArray.type();

		if (type.typeKind() != SymbolicTypeKind.ARRAY)
			throw err("argument concreteArray not array type:\n"
					+ concreteArray);
		if (concreteArray.operator() != SymbolicOperator.CONCRETE) {
			throw err("argument concreteArray is not concrete:\n"
					+ concreteArray);

		} else {
			SymbolicType elementType = ((SymbolicArrayType) type).elementType();
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> elements = (SymbolicSequence<SymbolicExpression>) concreteArray
					.argument(0);
			int length = elements.size();
			SymbolicExpression result;

			if (index < 0 || index > length)
				throw err("Index out of range:\narray: " + concreteArray
						+ "\nlength: " + length + "\nindex: " + index);
			if (incompatible(elementType, value.type()))
				throw err("Argument value to method insertElementAt has incompatible type."
						+ "\nvalue: "
						+ value
						+ "\ntype: "
						+ value.type()
						+ "\nExpected: " + elementType);
			elements = elements.insert(index, value);
			type = arrayType(elementType, integer(elements.size()));
			result = expression(SymbolicOperator.CONCRETE, type,
					sequence(elements));
			return result;
		}
	}

	@Override
	public SymbolicExpression emptySet(SymbolicSetType setType) {
		return expression(SymbolicOperator.CONCRETE, setType,
				collectionFactory.emptyHashSet());
	}

	@Override
	public SymbolicExpression singletonSet(SymbolicSetType setType,
			SymbolicExpression value) {
		return expression(SymbolicOperator.CONCRETE, setType,
				collectionFactory.singletonHashSet(value));
	}

	/**
	 * Under construction.
	 */
	@Override
	public BooleanExpression isElementOf(SymbolicExpression value,
			SymbolicExpression set) {
		if (set == null)
			throw err("Set argument to isElementof is null");
		if (set.type().typeKind() != SymbolicTypeKind.SET)
			throw err("Argument to isElementOf does not have set type: " + set
					+ "\nType is: " + set.type());
		if (value == null)
			throw err("Value argument to isElementOf is null");
		if (set.operator() != SymbolicOperator.CONCRETE)
			throw err("Sets must be concrete for now");
		else {
			SymbolicObject arg0 = set.argument(0);
			boolean result;

			if (!(arg0 instanceof SymbolicSet))
				throw err("Argument of concrete set expression is not a set: "
						+ set);
			@SuppressWarnings("unchecked")
			SymbolicSet<SymbolicExpression> contents = (SymbolicSet<SymbolicExpression>) arg0;
			// check type of value compatible with set element type?
			result = contents.contains(value);
			return bool(result);
			// TODO: problem: need to compute equals on each element???????
			// Disjunction.
		}
	}

	@Override
	public BooleanExpression isSubsetOf(SymbolicExpression set1,
			SymbolicExpression set2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression setAdd(SymbolicExpression set,
			SymbolicExpression value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression setRemove(SymbolicExpression set,
			SymbolicExpression value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression setUnion(SymbolicExpression set1,
			SymbolicExpression set2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression setIntersection(SymbolicExpression set1,
			SymbolicExpression set2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression setDifference(SymbolicExpression set1,
			SymbolicExpression set2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression cardinality(SymbolicExpression set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression emptyMap(SymbolicMapType mapType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression put(SymbolicExpression map,
			SymbolicExpression key, SymbolicExpression value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression get(SymbolicExpression map, SymbolicExpression key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression removeEntryWithKey(SymbolicExpression map,
			SymbolicExpression key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression keySet(SymbolicExpression map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression mapSize(SymbolicExpression map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpression entrySet(SymbolicExpression map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SymbolicConstant> getFreeSymbolicConstants(
			SymbolicExpression expr) {
		return new ExpressionWalker(expr).getResult();
	}

	@Override
	public UnaryOperator<SymbolicExpression> mapSubstituter(
			Map<SymbolicExpression, SymbolicExpression> map) {
		return new MapSubstituter(this, collectionFactory, typeFactory, map);
	}

	@Override
	public UnaryOperator<SymbolicExpression> nameSubstituter(
			Map<StringObject, StringObject> nameMap) {
		return new NameSubstituter(this, collectionFactory, typeFactory,
				nameMap);
	}

	@Override
	public UnaryOperator<SymbolicExpression> simpleSubstituter(
			SymbolicConstant var, SymbolicExpression value) {
		return new SimpleSubstituter(this, collectionFactory, typeFactory, var,
				value);
	}

}
