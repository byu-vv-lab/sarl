package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cvc3.Cvc3Exception;
import cvc3.Expr;
import cvc3.Op;
import cvc3.QueryResult;
import cvc3.Rational;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.prove.CVC3TheoremProverIF;
import edu.udel.cis.vsl.sarl.IF.prove.TernaryResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.util.Pair;

/**
 * An implementation of TheoremProverIF using the automated theorem prover CVC3.
 * Transforms a theorem proving query into the language of CVC3, invokes CVC3
 * through its JNI interface, and interprets the output.
 */
public class CVC3TheoremProver implements CVC3TheoremProverIF {

	/** Number factory used for lifetime of this object. */
	@SuppressWarnings("unused")
	private NumberFactoryIF numberFactory;

	/** The symbolic universe used for managing symbolic expressions */
	private SymbolicUniverseIF universe;

	/** The number of calls to the valid method for this object. */
	private int numValidCalls = 0;

	/** Print the queries and results each time valid is called? */
	private boolean showProverQueries;

	/** The printwriter used to print the queries and results. */
	private PrintWriter out;

	/** The CVC3 object used to check queries. */
	private ValidityChecker vc = null;

	/**
	 * Number of bound variables created since last initialization. It seems
	 * CVC3 wants a String "uid" (unique identifier?) to create a bound
	 * variable, in addition to the usual name of the variable. This counter is
	 * used to construct the uid.
	 */
	private int boundVariableCounter = 0;

	/**
	 * A pool of auxiliary variables "_xi".
	 */
	private int auxVariableCounter = 0;

	/** Mapping of SARL symbolic type to corresponding CVC3 type */
	private Map<SymbolicTypeIF, Type> typeMap;

	/** Mapping of SARL symbolic expression to corresponding CVC3 expresssion */
	private Map<SymbolicExpressionIF, Expr> expressionMap;

	/**
	 * Map from SARL expressions of funcional type to corresponding CVC3
	 * operators. In SARL, a function is a kind of symbolic expression. In CVC3,
	 * this concept is represented as an instance of "OpMut" (Operator Mutable),
	 * a subtype of "Op" (operator), which is not a subtype of Expr. Hence a
	 * separate map is needed.
	 */
	private Map<SymbolicExpressionIF, Op> functionMap;

	/**
	 * Mapping of integer division expressions. Since integer division and
	 * modulus operations are not supported by CVC3, this is dealt with by
	 * adding auxialliary variables and constraints to the CVC3 representation
	 * of the query. Given any integer division or modulus operations occuring
	 * in the query, A OP B, we create auxiallary inter variables Q and R on the
	 * CVC3 side and add constraints A=QB+R, |R|<|B|, sgn(R)=sgn(A).
	 * 
	 * Specifically: introduce integer variables Q and R. Introduce constraint
	 * A=QB+R. If we assume A and B are non-negative: 0<=R<B. Otherwise, little
	 * more work. FOR NOW, assume A and B are non-negative.
	 * 
	 * A key is a numerator-denominator pair of symbolic expressions (in tree
	 * form). The value associated to that key is a pair of CVC3 expressions:
	 * the first element of the pair is the CVC3 expression (usually a variable)
	 * corresponding to the quotient, the second the CVC3 expression
	 * corresponding to the modulus.
	 */
	private Map<Pair<SymbolicExpressionIF, SymbolicExpressionIF>, Pair<Expr, Expr>> integerDivisionMap;

	/**
	 * Constructs new CVC3 theorem prover with given symbolic universe and run
	 * configuration.
	 */
	CVC3TheoremProver(SymbolicUniverseIF universe, PrintWriter out,
			boolean showProverQueries) {
		if (universe == null) {
			throw new RuntimeException("Null symbolic universe.");
		} else {
			this.universe = universe;
		}
		this.showProverQueries = showProverQueries;
		this.out = out;
		this.numberFactory = Numbers.REAL_FACTORY;
	}

	/**
	 * Resets all data structures to initial state. The CVC3 validity checker is
	 * deleted and a new one created to replace it. All caches are cleared.
	 */
	public void reset() {
		try {
			if (vc != null)
				vc.delete();
			vc = ValidityChecker.create();
		} catch (Cvc3Exception e) {
			e.printStackTrace();
			throw new SARLInternalException(e.toString());
		}
		boundVariableCounter = 0;
		typeMap = new LinkedHashMap<SymbolicTypeIF, Type>();
		expressionMap = new LinkedHashMap<SymbolicExpressionIF, Expr>();
		functionMap = new LinkedHashMap<SymbolicExpressionIF, Op>();
		integerDivisionMap = new LinkedHashMap<Pair<SymbolicExpressionIF, SymbolicExpressionIF>, Pair<Expr, Expr>>();
	}

	private List<Expr> translateCollection(SymbolicCollection collection) {
		List<Expr> result = new LinkedList<Expr>();

		for (SymbolicExpressionIF expr : collection)
			result.add(expr == null ? null : translate(expr));
		return result;
	}

	private List<Type> translateTypeSequence(SymbolicTypeSequenceIF sequence) {
		List<Type> result = new LinkedList<Type>();

		for (SymbolicTypeIF t : sequence)
			result.add(t == null ? null : translateType(t));
		return result;
	}

	private <E> List<E> newSingletonList(E element) {
		List<E> result = new LinkedList<E>();

		result.add(element);
		return result;
	}

	private boolean isBigArrayType(SymbolicTypeIF type) {
		return type instanceof SymbolicArrayTypeIF
				&& !((SymbolicArrayTypeIF) type).isComplete();
	}

	private boolean isBigArray(SymbolicExpressionIF expr) {
		return isBigArrayType(expr.type());
	}

	private Expr bigArray(Expr length, Expr value) {
		List<Expr> list = new LinkedList<Expr>();

		list.add(length);
		list.add(value);
		return vc.tupleExpr(list);
	}

	private Expr bigArrayLength(Expr bigArray) {
		return vc.tupleSelectExpr(bigArray, 0);
	}

	private Expr bigArrayValue(Expr bigArray) {
		return vc.tupleSelectExpr(bigArray, 1);
	}

	/**
	 * Translates a symbolic expression of functional type. In CVC3, functions
	 * have type Op; expressions have type Expr.
	 */
	private Op translateFunction(SymbolicExpressionIF expr) {
		Op result = functionMap.get(expr);

		if (result != null)
			return result;
		else {
			SymbolicOperator op = expr.operator();

			switch (op) {
			case SYMBOLIC_CONSTANT:
				result = translateFunctionSymbolicConstant((SymbolicConstantIF) expr);
				break;
			case LAMBDA: {
				SymbolicConstantIF boundVariable = (SymbolicConstantIF) expr
						.argument(0);
				SymbolicExpressionIF body = (SymbolicExpressionIF) expr
						.argument(1);

				result = vc.lambdaExpr(
						newSingletonList(translateSymbolicConstant(
								boundVariable, true)), translate(body));
				break;
			}
			default:
				throw new SARLInternalException(
						"unknown kind of expression of functional type: "
								+ expr);
			}
			this.functionMap.put(expr, result);
			return result;
		}

	}

	private Expr translate(SymbolicExpressionIF expr) {
		Expr result = expressionMap.get(expr);
		SymbolicOperator op;
		int numArgs = expr.numArguments();

		if (result != null)
			return result;
		op = expr.operator();
		switch (op) {
		case ADD:
			if (numArgs == 2)
				result = vc.plusExpr(
						translate((SymbolicExpressionIF) expr.argument(0)),
						translate((SymbolicExpressionIF) expr.argument(1)));
			else if (numArgs == 1)
				result = vc
						.plusExpr(translateCollection((SymbolicCollection) expr
								.argument(0)));
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for ADD");
			break;
		case AND:
			if (numArgs == 2)
				result = vc.andExpr(
						translate((SymbolicExpressionIF) expr.argument(0)),
						translate((SymbolicExpressionIF) expr.argument(1)));
			else if (numArgs == 1)
				result = vc
						.andExpr(translateCollection((SymbolicCollection) expr
								.argument(0)));
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for AND: " + expr);
			break;
		case APPLY:
			result = vc.funExpr(
					translateFunction((SymbolicExpressionIF) expr.argument(0)),
					translateCollection((SymbolicCollection) expr.argument(1)));
			break;
		case ARRAY_LAMBDA: {
			SymbolicExpressionIF function = (SymbolicExpressionIF) expr
					.argument(0);
			SymbolicOperator op0 = function.operator();
			Expr var, body;

			if (op0 == SymbolicOperator.LAMBDA) {
				var = translate((SymbolicConstantIF) function.argument(0));
				body = translate((SymbolicExpressionIF) function.argument(1));
			} else {
				// create new SymbolicConstantIF _SARL_i
				// create new APPLY expression apply(f,i)
				// need universe.
				// or just assert forall i.a[i]=f(i)
				throw new UnsupportedOperationException("TO DO");
			}
			result = vc.arrayLiteral(var, body);
			break;
		}
		case ARRAY_READ:
			result = translateArrayRead(expr);
			break;
		case ARRAY_WRITE:
			result = translateArrayWrite(expr);
			break;
		case CAST:
			result = this.translate((SymbolicExpressionIF) expr.argument(0));
			break;
		case CONCRETE:
			result = translateConcrete(expr);
			break;
		case COND:
			result = vc.iteExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)),
					translate((SymbolicExpressionIF) expr.argument(2)));
			break;
		case DENSE_ARRAY_WRITE:
			result = translateDenseArrayWrite(expr);
			break;
		case DIVIDE: // real division
			result = vc.divideExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
			break;
		case EQUALS:
			result = translateEquality(expr);
			break;
		case EXISTS:
			result = translateQuantifier(expr);
			break;
		case FORALL:
			result = translateQuantifier(expr);
			break;
		case INT_DIVIDE:
			result = translateIntegerDivision(expr);
			break;
		case LENGTH:
			result = bigArrayLength(translate((SymbolicExpressionIF) expr
					.argument(0)));
			break;
		case LESS_THAN:
			result = vc.ltExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
			break;
		case LESS_THAN_EQUALS:
			result = vc.leExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
			break;
		case MODULO:
			result = translateIntegerModulo(expr);
			break;
		case MULTIPLY:
			result = translateMultiply(expr);
			break;
		case NEGATIVE:
			result = vc.uminusExpr(translate((SymbolicExpressionIF) expr
					.argument(0)));
			break;
		case NEQ:
			result = vc.notExpr(translateEquality(expr));
			break;
		case NOT:
			result = vc.notExpr(translate((SymbolicExpressionIF) expr
					.argument(0)));
			break;
		case OR:
			result = translateOr(expr);
			break;
		case POWER:
			result = vc.powExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
			break;
		case SUBTRACT:
			result = vc.minusExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
			break;
		case SYMBOLIC_CONSTANT:
			result = translateSymbolicConstant((SymbolicConstantIF) expr, false);
			break;
		case TUPLE_READ:
			result = vc.tupleSelectExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					((IntObject) expr.argument(1)).getInt());
			break;
		case TUPLE_WRITE:
			result = vc.tupleUpdateExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					((IntObject) expr.argument(1)).getInt(),
					translate((SymbolicExpressionIF) expr.argument(2)));
			break;
		case UNION_EXTRACT:
			result = translateUnionExtract(expr);
			break;
		case UNION_INJECT:
			result = translateUnionInject(expr);
			break;
		case UNION_TEST:
			result = translateUnionTest(expr);
			break;
		default:
			throw new SARLInternalException("unreachable");
		}
		this.expressionMap.put(expr, result);
		return result;
	}

	private Expr newAuxVariable(Type type) {
		Expr result = vc.varExpr("_x" + auxVariableCounter, type);

		// if dense array type: need tuple?
		auxVariableCounter++;
		return result;
	}

	private Expr translateConcrete(SymbolicExpressionIF expr) {
		SymbolicTypeIF type = expr.type();
		SymbolicTypeKind kind = type.typeKind();
		SymbolicObject object = expr.argument(0);
		Expr result;

		switch (kind) {
		case ARRAY: {
			SymbolicExpressionIF extentExpression = ((SymbolicCompleteArrayTypeIF) type)
					.extent();
			IntegerNumberIF extentNumber = (IntegerNumberIF) universe
					.extractNumber(extentExpression);
			SymbolicSequence sequence = (SymbolicSequence) object;
			int size = sequence.size();
			Type cvcType = translateType(type);

			assert extentNumber != null && extentNumber.intValue() == size;
			result = newAuxVariable(cvcType);
			for (int i = 0; i < size; i++)
				result = vc.writeExpr(result, vc.ratExpr(i),
						translate(sequence.get(i)));
			break;
		}
		case BOOLEAN:
			result = ((BooleanObject) object).getBoolean() ? vc.trueExpr() : vc
					.falseExpr();
			break;
		case INTEGER:
		case REAL:
			result = vc.ratExpr(((NumberObject) object).getNumber().toString());
			break;
		case TUPLE:
			result = vc
					.tupleExpr(translateCollection((SymbolicSequence) object));
			break;
		default:
			throw new SARLInternalException("Unknown concrete object: " + expr);
		}
		return result;
	}

	/**
	 * Translates a symbolic constant to CVC3 variable. Special handling is
	 * required if the symbolic constant is used as a bound variable in a
	 * quantified (forall, exists) expression.
	 */
	private Expr translateSymbolicConstant(SymbolicConstantIF symbolicConstant,
			boolean isBoundVariable) throws Cvc3Exception {
		Expr result;
		Type type = translateType(symbolicConstant.type());
		String name = symbolicConstant.name().getString();

		if (isBoundVariable) {
			result = vc.boundVarExpr(name,
					String.valueOf(this.boundVariableCounter), type);
			this.boundVariableCounter++;
		} else {
			result = vc.varExpr(name, type);
		}
		return result;
	}

	private Op translateFunctionSymbolicConstant(
			SymbolicConstantIF symbolicConstant) throws Cvc3Exception {
		Op result = functionMap.get(symbolicConstant);

		if (result == null) {
			result = vc.createOp(symbolicConstant.name().getString(),
					this.translateType(symbolicConstant.type()));
			functionMap.put(symbolicConstant, result);
		}
		return result;
	}

	/**
	 * Translates the symbolic type to a CVC3 type.
	 * 
	 * @param type
	 *            a SARL symbolic expression type
	 * @return the equivalent CVC3 type
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Type translateType(SymbolicTypeIF type) throws Cvc3Exception {
		Type result = typeMap.get(type);

		if (result != null)
			return result;

		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {

		case BOOLEAN:
			result = vc.boolType();
			break;
		case INTEGER:
			result = vc.intType();
			break;
		case REAL:
			result = vc.realType();
			break;
		case ARRAY:
			result = vc.arrayType(vc.intType(),
					translateType(((SymbolicArrayTypeIF) type).elementType()));
			if (!(type instanceof SymbolicCompleteArrayTypeIF))
				// tuple:<extent,array>
				result = vc.tupleType(vc.intType(), result);
			break;
		case TUPLE:
			result = vc
					.tupleType(translateTypeSequence(((SymbolicTupleTypeIF) type)
							.sequence()));
			break;
		case FUNCTION: {
			SymbolicFunctionTypeIF functionType = (SymbolicFunctionTypeIF) type;

			result = vc.funType(
					translateTypeSequence(functionType.inputTypes()),
					translateType(functionType.outputType()));
			break;
		}
		case UNION: {
			SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) type;
			List<String> constructors = new LinkedList<String>();
			List<String> selectors = new LinkedList<String>();
			List<Type> types = new LinkedList<Type>();
			SymbolicTypeSequenceIF sequence = unionType.sequence();
			int index = 0;

			for (SymbolicTypeIF t : sequence) {
				constructors.add(constructor(unionType, index));
				selectors.add(selector(unionType, index));
				types.add(translateType(t));
				index++;
			}
			result = vc.dataType(unionType.name().getString(), constructors,
					selectors, types);
			break;
		}
		default:
			throw new RuntimeException("Unknown type: " + type);
		}
		typeMap.put(type, result);
		return result;
	}

	private Expr translateMultiply(SymbolicExpressionIF expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1) {
			result = vc.ratExpr(1);
			for (SymbolicExpressionIF operand : (SymbolicCollection) expr
					.argument(0))
				result = vc.multExpr(result, translate(operand));
		} else if (numArgs == 2)
			result = vc.multExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
		else
			throw new SARLInternalException(
					"Wrong number of arguments to multiply: " + expr);
		return result;
	}

	private Expr translateOr(SymbolicExpressionIF expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1)
			result = vc.orExpr(translateCollection((SymbolicCollection) expr
					.argument(0)));
		else if (numArgs == 2)
			result = vc.orExpr(
					translate((SymbolicExpressionIF) expr.argument(0)),
					translate((SymbolicExpressionIF) expr.argument(1)));
		else
			throw new SARLInternalException("Wrong number of arguments to or: "
					+ expr);
		return result;
	}

	/**
	 * Translates an integer modulo symbolic expression (a%b) into an equivalent
	 * CVC3 expression. This involves possibly adding extra integer variables
	 * and constraints to the validity checker.
	 * 
	 * @param modExpression
	 *            a SARL symbolic expression of form a%b
	 * @return an equivalent CVC3 expression
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateIntegerModulo(SymbolicExpressionIF modExpression)
			throws Cvc3Exception {
		Pair<Expr, Expr> value = getQuotientRemainderPair(
				(SymbolicExpressionIF) modExpression.argument(0),
				(SymbolicExpressionIF) modExpression.argument(1));

		return value.right;
	}

	/**
	 * Translates an integer division symbolic expression (a/b) into an
	 * equivalent CVC3 expression. This involves possibly adding extra integer
	 * variables and constraints to the validity checker.
	 * 
	 * @param quotientExpression
	 *            a SARL symbolic expression of form a (intdiv) b
	 * @return an equivalent CVC3 expression
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateIntegerDivision(
			SymbolicExpressionIF quotientExpression) throws Cvc3Exception {
		Pair<Expr, Expr> value = getQuotientRemainderPair(
				(SymbolicExpressionIF) quotientExpression.argument(0),
				(SymbolicExpressionIF) quotientExpression.argument(1));

		return value.left;
	}

	/**
	 * Looks to see if pair has already been created. If so, returns old. If
	 * not, creates new quotient and remainder variables, adds constraints
	 * (assumptions) to vc, adds new pair to map, and returns pair.
	 * 
	 * FOR NOW, we assume all quantities are non-negative.
	 **/
	private Pair<Expr, Expr> getQuotientRemainderPair(
			SymbolicExpressionIF numeratorExpression,
			SymbolicExpressionIF denominatorExpression) throws Cvc3Exception {
		Pair<SymbolicExpressionIF, SymbolicExpressionIF> key = new Pair<SymbolicExpressionIF, SymbolicExpressionIF>(
				numeratorExpression, denominatorExpression);
		Pair<Expr, Expr> value = integerDivisionMap.get(key);

		if (value == null) {
			int counter = integerDivisionMap.size();
			Expr quotientVariable = vc.varExpr("q_" + counter, vc.intType());
			Expr remainderVariable = vc.varExpr("r_" + counter, vc.intType());
			Expr numerator = translate(numeratorExpression);
			Expr denominator = translate(denominatorExpression);
			// numerator=quotient*denominator+remainder
			Expr constraint1 = vc.eqExpr(numerator, vc.plusExpr(
					vc.multExpr(quotientVariable, denominator),
					remainderVariable));
			Expr constraint2 = null; // 0<=R<B

			if (denominator.isRational()) {
				Rational rationalDenominator = denominator.getRational();

				if (rationalDenominator.isInteger()) {
					int denominatorInt = rationalDenominator.getInteger();

					if (denominatorInt == 2) {
						constraint2 = vc.orExpr(
								vc.eqExpr(vc.ratExpr(0), remainderVariable),
								vc.eqExpr(vc.ratExpr(1), remainderVariable));
					}
				}
			}
			if (constraint2 == null) {
				constraint2 = vc.andExpr(
						vc.leExpr(vc.ratExpr(0), remainderVariable),
						vc.ltExpr(remainderVariable, denominator));
			}
			vc.assertFormula(constraint1);
			vc.assertFormula(constraint2);
			value = new Pair<Expr, Expr>(quotientVariable, remainderVariable);
			integerDivisionMap.put(key, value);
		}
		return value;
	}

	/**
	 * Translates an array-read expression a[i] into equivalent CVC3 expression
	 * 
	 * @param expr
	 *            a SARL symbolic expression of form a[i]
	 * @return an equivalent CVC3 expression
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateArrayRead(SymbolicExpressionIF expr)
			throws Cvc3Exception {
		SymbolicExpressionIF arrayExpression = (SymbolicExpressionIF) expr
				.argument(0);
		Expr array = translate(arrayExpression);
		Expr index = translate((SymbolicExpressionIF) expr.argument(1));
		Expr result;

		if (isBigArray(arrayExpression))
			array = bigArrayValue(array);
		result = vc.readExpr(array, index);
		return result;
	}

	/**
	 * Translates an array-write (or array update) SARL symbolic expression to
	 * equivalent CVC3 expression.
	 * 
	 * @param expr
	 *            an array update expression array[WITH i:=newValue].
	 * @return the equivalent CVC3 Expr
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateArrayWrite(SymbolicExpressionIF expr)
			throws Cvc3Exception {
		SymbolicExpressionIF arrayExpression = (SymbolicExpressionIF) expr
				.argument(0);
		Expr array = translate(arrayExpression);
		Expr index = translate((SymbolicExpressionIF) expr.argument(1));
		Expr value = translate((SymbolicExpressionIF) expr.argument(2));
		Expr result = isBigArray(arrayExpression) ? bigArray(
				bigArrayLength(array),
				vc.writeExpr(bigArrayValue(array), index, value)) : vc
				.writeExpr(array, index, value);

		return result;
	}

	private Expr translateDenseArrayWrite(SymbolicExpressionIF expr)
			throws Cvc3Exception {
		SymbolicExpressionIF arrayExpression = (SymbolicExpressionIF) expr
				.argument(0);
		boolean isBig = isBigArray(arrayExpression);
		Expr origin = translate(arrayExpression);
		Expr result = isBig ? bigArrayValue(origin) : origin;
		List<Expr> values = translateCollection((SymbolicSequence) expr
				.argument(1));
		int index = 0;

		for (Expr value : values) {
			if (value != null)
				result = vc.writeExpr(result, vc.ratExpr(index), value);
			index++;
		}
		if (isBig)
			result = bigArray(bigArrayLength(origin), result);
		return result;
	}

	private Expr translateQuantifier(SymbolicExpressionIF expr)
			throws Cvc3Exception {
		Expr variable = this.translateSymbolicConstant(
				(SymbolicConstantIF) (expr.argument(0)), true);
		List<Expr> vars = new LinkedList<Expr>();
		Expr predicate = translate((SymbolicExpressionIF) expr.argument(1));
		SymbolicOperator kind = expr.operator();

		vars.add(variable);
		if (kind == SymbolicOperator.FORALL) {
			return vc.forallExpr(vars, predicate);
		} else if (kind == SymbolicOperator.EXISTS) {
			return vc.existsExpr(vars, predicate);
		} else {
			throw new SARLInternalException(
					"Cannot translate quantifier into CVC3: " + expr);
		}
	}

	private Expr processEquality(SymbolicTypeIF type1, SymbolicTypeIF type2,
			Expr cvcExpression1, Expr cvcExpression2) {
		SymbolicTypeKind kind = type1.typeKind();

		if (kind == SymbolicTypeKind.ARRAY) {
			// length are equal and forall i (0<=i<length).a[i]=b[i].
			SymbolicArrayTypeIF arrayType1 = (SymbolicArrayTypeIF) type1;
			SymbolicArrayTypeIF arrayType2 = (SymbolicArrayTypeIF) type2;
			List<Expr> boundVariableList = new LinkedList<Expr>();
			Expr extent1, extent2, array1, array2, readExpr1, readExpr2;
			Expr result, index, indexRangeExpr, elementEqualsExpr, forallExpr;

			if (arrayType1 instanceof SymbolicCompleteArrayTypeIF) {
				extent1 = translate(((SymbolicCompleteArrayTypeIF) arrayType1)
						.extent());
				array1 = cvcExpression1;
			} else {
				extent1 = bigArrayLength(cvcExpression1);
				array1 = bigArrayValue(cvcExpression1);
			}
			if (arrayType2 instanceof SymbolicCompleteArrayTypeIF) {
				extent2 = translate(((SymbolicCompleteArrayTypeIF) arrayType2)
						.extent());
				array2 = cvcExpression2;
			} else {
				extent2 = bigArrayLength(cvcExpression2);
				array2 = bigArrayValue(cvcExpression2);
			}
			result = vc.eqExpr(extent1, extent2);
			index = vc.boundVarExpr("_i" + boundVariableCounter,
					String.valueOf(this.boundVariableCounter), vc.intType());
			indexRangeExpr = vc.andExpr(vc.geExpr(index, vc.ratExpr(0)),
					vc.ltExpr(index, extent1));
			this.boundVariableCounter++;
			boundVariableList.add(index);
			readExpr1 = vc.readExpr(array1, index);
			readExpr2 = vc.readExpr(array2, index);
			elementEqualsExpr = processEquality(arrayType1.elementType(),
					arrayType2.elementType(), readExpr1, readExpr2);
			forallExpr = vc.forallExpr(boundVariableList,
					vc.impliesExpr(indexRangeExpr, elementEqualsExpr));
			result = vc.andExpr(result, forallExpr);
			return result;
		} else {
			return vc.eqExpr(cvcExpression1, cvcExpression2);
		}
	}

	private Expr translateEquality(SymbolicExpressionIF expr)
			throws Cvc3Exception {
		SymbolicExpressionIF leftExpression = (SymbolicExpressionIF) expr
				.argument(0);
		SymbolicExpressionIF rightExpression = (SymbolicExpressionIF) expr
				.argument(1);
		SymbolicTypeIF type1 = leftExpression.type();
		SymbolicTypeIF type2 = rightExpression.type();
		Expr cvcExpression1 = translate(leftExpression);
		Expr cvcExpression2 = translate(rightExpression);
		Expr result = processEquality(type1, type2, cvcExpression1,
				cvcExpression2);

		return result;
	}

	private String selector(SymbolicUnionTypeIF unionType, int index) {
		return unionType.name().toString() + "_extract_" + index;
	}

	private String constructor(SymbolicUnionTypeIF unionType, int index) {
		return unionType.name().toString() + "_inject_" + index;
	}

	/**
	 * UNION_EXTRACT: 2 arguments: arg0 is an IntObject giving the index of a
	 * member type of a union type; arg1 is a symbolic expression whose type is
	 * the union type. The resulting expression has type the specified member
	 * type. This essentially pulls the expression out of the union and casts it
	 * to the member type. If arg1 does not belong to the member type (as
	 * determined by a UNION_TEST expression), the value of this expression is
	 * undefined.
	 * 
	 * Get the type of arg1. It is a union type. Get arg0 and call it i. Get the
	 * name of the i-th component of the union type. It must have a globally
	 * unique name. That is the selector. Translate arg1.
	 * 
	 * Every symbolic union type has a name, so name of selector could be
	 * unionName_i.
	 * 
	 * @param expr
	 * @return
	 */
	private Expr translateUnionExtract(SymbolicExpressionIF expr) {
		SymbolicExpressionIF arg = (SymbolicExpressionIF) expr.argument(1);
		SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) arg.type();
		int index = ((IntObject) expr.argument(0)).getInt();
		String selector = selector(unionType, index);
		Expr result = vc.datatypeSelExpr(selector, translate(arg));

		return result;
		// vc.dataType(name, constructor, selectors, types);
		// vc.datatypeConsExpr(constructor, exprs);
		// vc.datatypeTestExpr(constructor, arg);
	}

	/**
	 * UNION_INJECT: injects an element of a member type into a union type that
	 * inclues that member type. 2 arguments: arg0 is an IntObject giving the
	 * index of the member type of the union type; arg1 is a symbolic expression
	 * whose type is the member type. The union type itself is the type of the
	 * UNION_INJECT expression.
	 * 
	 * @param expr
	 * @return
	 */
	private Expr translateUnionInject(SymbolicExpressionIF expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpressionIF arg = (SymbolicExpressionIF) expr.argument(1);
		SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) expr.type();
		String constructor = constructor(unionType, index);
		List<Expr> argumentList = new LinkedList<Expr>();
		Expr result;

		argumentList.add(translate(arg));
		result = vc.datatypeConsExpr(constructor, argumentList);
		return result;
	}

	/**
	 * UNION_TEST: 2 arguments: arg0 is an IntObject giving the index of a
	 * member type of the union type; arg1 is a symbolic expression whose type
	 * is the union type. This is a boolean-valued expression whose value is
	 * true iff arg1 belongs to the specified member type of the union type.
	 * 
	 * @param expr
	 * @return
	 */
	private Expr translateUnionTest(SymbolicExpressionIF expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpressionIF arg = (SymbolicExpressionIF) expr.argument(1);
		SymbolicUnionTypeIF unionType = (SymbolicUnionTypeIF) arg.type();
		String constructor = constructor(unionType, index);
		Expr result = vc.datatypeTestExpr(constructor, translate(arg));

		return result;
	}

	public ResultType valid(SymbolicExpressionIF symbolicAssumption,
			SymbolicExpressionIF symbolicPredicate) {
		QueryResult result = null;

		numValidCalls++;
		// Because canonicalization can re-define symbolic
		// constants with new types, need to start afresh:
		reset();
		if (showProverQueries) {
			out.println();
			out.print("SARL assumption " + numValidCalls + ": ");
			out.println(symbolicAssumption);
			out.print("SARL predicate  " + numValidCalls + ": ");
			out.println(symbolicPredicate);
			out.flush();
		}
		try {
			Expr cvcAssumption, cvcPredicate;

			this.vc.push();
			cvcAssumption = translate(symbolicAssumption);
			vc.assertFormula(cvcAssumption);
			cvcPredicate = translate(symbolicPredicate);
			if (showProverQueries) {
				out.println();
				out.print("CVC3 assumptions " + numValidCalls + ": ");
				// getUserAssumptions() is also possible:
				for (Object o : vc.getUserAssumptions()) {
					out.println(o);
				}
				out.print("CVC3 predicate   " + numValidCalls + ": ");
				out.println(cvcPredicate);
				out.flush();
			}
			result = vc.query(cvcPredicate);
		} catch (Cvc3Exception e) {
			e.printStackTrace();
			throw new SARLInternalException(
					"Error in parsing the symbolic expression or querying CVC3:\n"
							+ e);
		}
		try {
			vc.pop();
		} catch (Cvc3Exception e) {
			throw new SARLInternalException("CVC3 error: " + e);
		}
		if (showProverQueries) {
			out.println("CVC3 result      " + numValidCalls + ": " + result);
			out.flush();
		}
		if (result.equals(QueryResult.VALID)) {
			return ResultType.YES;
		} else if (result.equals(QueryResult.INVALID)) {
			return ResultType.NO;
		} else if (result.equals(QueryResult.UNKNOWN)) {
			return ResultType.MAYBE;
		} else if (result.equals(QueryResult.ABORT)) {
			out.println("Warning: Query aborted by CVC3.");
			return ResultType.MAYBE;
		} else {
			out.println("Warning: Unknown CVC3 query result: " + result);
			return ResultType.MAYBE;
		}
	}

	public SymbolicUniverseIF universe() {
		return universe;
	}

	public String toString() {
		return "CVC3TheoremProver";
	}

	public void close() {
		try {
			if (vc != null)
				this.vc.delete();
		} catch (Cvc3Exception e) {
			throw new SARLInternalException(
					"CVC3: could not delete validity checker:\n" + e);
		}
	}

	@Override
	public int numInternalValidCalls() {
		return numValidCalls;
	}

	@Override
	public int numValidCalls() {
		return numValidCalls;
	}

	@Override
	public Map<SymbolicConstantIF, SymbolicExpressionIF> findModel(
			SymbolicExpressionIF context) throws TheoremProverException {
		throw new TheoremProverException("Unimplemented");
	}

	public boolean showProverQueries() {
		return showProverQueries;
	}

}
