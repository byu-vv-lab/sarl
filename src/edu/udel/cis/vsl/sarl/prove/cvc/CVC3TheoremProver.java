package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cvc3.Cvc3Exception;
import cvc3.Expr;
import cvc3.OpMut;
import cvc3.QueryResult;
import cvc3.Rational;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.BooleanConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.prove.CVC3TheoremProverIF;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.prove.TernaryResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF.SymbolicKind;
import edu.udel.cis.vsl.sarl.util.Pair;
import edu.udel.cis.vsl.sarl.util.SARLInternalException;

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

	/** Mapping of SARL symbolic type to corresponding CVC3 type */
	private Map<SymbolicTypeIF, Type> typeMap;

	/** Mapping of SARL symbolic expression to corresponding CVC3 expresssion */
	private Map<SymbolicExpressionIF, Expr> expressionMap;

	/**
	 * Used to cahce the results of mapping symbolic constants to CVC3
	 * expressions.
	 * 
	 * It isn't clear if this is necessary---can't the expressionMap be used?
	 */
	private Map<SymbolicConstantIF, Expr> variableMap;

	/**
	 * Map from SARL symbolic constants of funcional type to corresponding CVC3
	 * operators. In SARL, an abstract (uninterpreted) function is just a
	 * symbolic constant which happens to have a functional type. In CVC3, this
	 * concept is represented as an instance of "OpMut" (Operator Mutable),
	 * which is not a subtype of Expr. Hence a separate map is needed.
	 */
	private Map<SymbolicConstantIF, OpMut> abstractFunctionMap;

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
	private Map<Pair<TreeExpressionIF, TreeExpressionIF>, Pair<Expr, Expr>> integerDivisionMap;

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
		variableMap = new LinkedHashMap<SymbolicConstantIF, Expr>();
		abstractFunctionMap = new LinkedHashMap<SymbolicConstantIF, OpMut>();
		integerDivisionMap = new LinkedHashMap<Pair<TreeExpressionIF, TreeExpressionIF>, Pair<Expr, Expr>>();
	}

	/**
	 * Translates the given symbolic expression to a CVC3 Expr.
	 * 
	 * @param symbolicExpression
	 *            a SARL symbolic expression
	 * @return the equivalent CVC3 Expr
	 * @throws Cvc3Exception
	 *             if CVC3 throws an exception
	 */
	private Expr translate(TreeExpressionIF symbolicExpression)
			throws Cvc3Exception {
		Expr result = expressionMap.get(symbolicExpression);
		SymbolicOperator kind;

		if (result != null)
			return result;
		kind = symbolicExpression.operator();
		switch (kind) {
		case SYMBOLIC_CONSTANT:
			result = translateSymbolicConstant(
					(SymbolicConstantExpressionIF) symbolicExpression, false);
			break;
		case CONCRETE_BOOLEAN:
			result = (((BooleanConcreteExpressionIF) symbolicExpression)
					.value() ? vc.trueExpr() : vc.falseExpr());
			break;
		case CONCRETE_NUMBER:
			result = vc
					.ratExpr(((NumericConcreteExpressionIF) symbolicExpression)
							.toString());
			break;
		case CAST:
			result = this.translate(symbolicExpression.argument(0));
			break;
		case NEGATIVE:
			result = vc.uminusExpr(translate(symbolicExpression.argument(0)));
			break;
		case APPLY:
			result = this.translateApply(symbolicExpression);
			break;
		case ADD:
			if (symbolicExpression.numArguments() == 0) {
				if (symbolicExpression.type().isInteger()) {
					result = vc.ratExpr(0);
				} else {
					result = vc.ratExpr("0");
				}
			} else if (symbolicExpression.numArguments() == 1) {
				result = translate(symbolicExpression.argument(0));
			} else {
				result = vc.plusExpr(translate(symbolicExpression.argument(0)),
						translate(symbolicExpression.argument(1)));
				for (int i = 2; i < symbolicExpression.numArguments(); i++) {
					result = vc.plusExpr(result,
							translate(symbolicExpression.argument(i)));
				}
			}
			break;
		case SUBTRACT:
			result = vc.minusExpr(translate(symbolicExpression.argument(0)),
					translate(symbolicExpression.argument(1)));
			break;
		case POWER: {
			TreeExpressionIF base = symbolicExpression.argument(0);
			TreeExpressionIF exponent = symbolicExpression.argument(1);

			if (isOne(exponent)) {
				result = translate(base);
			} else {
				result = vc.powExpr(translate(base), translate(exponent));
			}
			break;
		}
		case MULTIPLY:
			if (symbolicExpression.numArguments() == 0) {
				if (symbolicExpression.type().isInteger()) {
					result = vc.ratExpr(1);
				} else {
					result = vc.ratExpr("1");
				}
			} else if (symbolicExpression.numArguments() == 1) {
				result = translate(symbolicExpression.argument(0));
			} else {
				result = null;
				for (int i = 0; i < symbolicExpression.numArguments(); i++) {
					TreeExpressionIF argument = symbolicExpression.argument(i);

					if (!isOne(argument)) {
						if (result == null)
							result = translate(argument);
						else
							result = vc.multExpr(result, translate(argument));
					}
				}
				if (result == null)
					result = (symbolicExpression.type().isInteger() ? vc
							.ratExpr(1) : vc.ratExpr("1"));
			}
			break;
		case DIVIDE:
			// real (not integer) division
			result = vc.divideExpr(translate(symbolicExpression.argument(0)),
					translate(symbolicExpression.argument(1)));
			break;
		case INT_DIVIDE:
			result = translateIntegerDivision(symbolicExpression);
			break;
		case AND:
			if (symbolicExpression.numArguments() == 0) {
				result = vc.trueExpr();
			} else if (symbolicExpression.numArguments() == 1) {
				result = translate(symbolicExpression.argument(0));
			} else {
				result = vc.andExpr(translate(symbolicExpression.argument(0)),
						translate(symbolicExpression.argument(1)));
				for (int i = 2; i < symbolicExpression.numArguments(); i++) {
					result = vc.andExpr(result,
							translate(symbolicExpression.argument(i)));
				}
			}
			break;
		case OR:
			if (symbolicExpression.numArguments() == 0) {
				result = vc.falseExpr();
			} else if (symbolicExpression.numArguments() == 1) {
				result = translate(symbolicExpression.argument(0));
			} else {
				result = vc.orExpr(translate(symbolicExpression.argument(0)),
						translate(symbolicExpression.argument(1)));
				for (int i = 2; i < symbolicExpression.numArguments(); i++) {
					result = vc.orExpr(result,
							translate(symbolicExpression.argument(i)));
				}
			}
			break;
		case MODULO:
			result = this.translateIntegerModulo(symbolicExpression);
			break;
		case LESS_THAN:
			result = vc.ltExpr(translate(symbolicExpression.argument(0)),
					translate(symbolicExpression.argument(1)));
			break;
		case LESS_THAN_EQUALS:
			result = vc.leExpr(translate(symbolicExpression.argument(0)),
					translate(symbolicExpression.argument(1)));
			break;
		case EQUALS:
			result = translateEquality(symbolicExpression);
			break;
		case NEQ:
			result = vc.notExpr(translateEquality(symbolicExpression));
			break;
		case NOT:
			result = vc.notExpr(translate(symbolicExpression.argument(0)));
			break;
		case ARRAY_READ:
			result = translateArrayRead(symbolicExpression);
			break;
		case ARRAY_WRITE:
			result = translateArrayWrite(symbolicExpression);
			break;
		case COND:
			Expr predicate = translate(symbolicExpression.argument(0));
			Expr trueExpr = translate(symbolicExpression.argument(1));
			Expr falseExpr = translate(symbolicExpression.argument(2));

			result = vc.iteExpr(predicate, trueExpr, falseExpr);
			break;
		case FORALL:
		case EXISTS:
			result = translateQuantifier(symbolicExpression);
			break;
		case CONCRETE_TUPLE:
			result = this.translateTuple(symbolicExpression);
			break;
		case TUPLE_READ:
			result = translateTupleRead(symbolicExpression);
			break;
		case TUPLE_WRITE:
			result = translateTupleWrite(symbolicExpression);
			break;
		case LENGTH:
			// an array value of incomplete type is translated as
			// tuple<int,array>
			// where the first component is the extent (length)
			result = vc.tupleSelectExpr(
					translate(symbolicExpression.argument(0)), 0);
		default:
			throw new SARLInternalException(
					"Unknow symbolic compound expression: "
							+ symbolicExpression);
		}
		this.expressionMap.put(symbolicExpression, result);
		return result;
	}

	/**
	 * Translates a symbolic constant to CVC3 variable. Special handling is
	 * required if the symbolic constant is used as a bound variable in a
	 * quantified (forall, exists) expression.
	 */
	private Expr translateSymbolicConstant(SymbolicConstantExpressionIF expr,
			boolean isBoundVariable) throws Cvc3Exception {
		SymbolicConstantIF symbolicConstant = expr.symbolicConstant();
		Expr result = variableMap.get(symbolicConstant);
		Type type;

		if (result != null)
			return result;
		type = translateType(expr.type());
		if (isBoundVariable) {
			result = vc.boundVarExpr(symbolicConstant.name(),
					String.valueOf(this.boundVariableCounter), type);
			this.boundVariableCounter++;
		} else {
			result = vc.varExpr(symbolicConstant.name(), type);
		}
		variableMap.put(symbolicConstant, result);
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
		if (type.isBoolean()) {
			result = vc.boolType();
		} else if (type.isInteger()) {
			result = vc.intType();
		} else if (type.isNumeric()) {
			result = vc.realType();
		} else if (type instanceof SymbolicArrayTypeIF) {
			result = vc.arrayType(vc.intType(),
					translateType(((SymbolicArrayTypeIF) type).elementType()));
			if (!(type instanceof SymbolicCompleteArrayTypeIF)) {
				// tuple:<extent,array>
				result = vc.tupleType(vc.intType(), result);
			}
		} else if (type instanceof SymbolicTupleTypeIF) {
			SymbolicTupleTypeIF tupleType = (SymbolicTupleTypeIF) type;
			int numFields = tupleType.numFields();
			List<Type> types = new LinkedList<Type>();

			for (int i = 0; i < numFields; i++) {
				types.add(this.translateType(tupleType.fieldType(i)));
			}
			result = vc.tupleType(types);
		} else if (type instanceof SymbolicFunctionTypeIF) {
			SymbolicFunctionTypeIF functionType = (SymbolicFunctionTypeIF) type;
			List<Type> inputTypeList = new LinkedList<Type>();
			int numInputs = functionType.numInputs();
			Type outputType = this.translateType(functionType.outputType());

			for (int i = 0; i < numInputs; i++) {
				Type inputType = this.translateType(functionType.inputType(i));

				inputTypeList.add(inputType);
			}
			result = vc.funType(inputTypeList, outputType);
		} else {
			throw new RuntimeException("Unknown type: " + type);
		}
		typeMap.put(type, result);
		return result;
	}

	/**
	 * Translates a symbolic expression which is the application of an abstract
	 * function to a sequence of input expression, i.e., "f(x1,...,xn)".
	 */
	private Expr translateApply(TreeExpressionIF applyExpression)
			throws Cvc3Exception {
		int numArguments = applyExpression.numArguments(); // including f
		List<Expr> argumentList = new LinkedList<Expr>();
		Expr result;
		TreeExpressionIF arg0 = applyExpression.argument(0);
		SymbolicTypeIF arg0type = arg0.type();
		SymbolicConstantExpressionIF functionExpression;
		SymbolicConstantIF function;
		OpMut functionOp;

		if (!(arg0type instanceof SymbolicFunctionTypeIF)) {
			throw new SARLInternalException(
					"Symbolic function type expected instead of " + arg0type
							+ " in apply operation.");
		}
		functionExpression = (SymbolicConstantExpressionIF) arg0;
		function = functionExpression.symbolicConstant();
		functionOp = abstractFunctionMap.get(function);
		if (functionOp == null) {
			String functionName = arg0.atomString();
			SymbolicFunctionTypeIF functionType = (SymbolicFunctionTypeIF) arg0type;

			functionOp = vc.createOp(functionName,
					this.translateType(functionType));
			abstractFunctionMap.put(function, functionOp);
		}
		for (int i = 1; i < numArguments; i++) {
			Expr argument = this.translate(applyExpression.argument(i));

			argumentList.add(argument);
		}
		result = vc.funExpr(functionOp, argumentList);
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
	private Expr translateIntegerModulo(TreeExpressionIF modExpression)
			throws Cvc3Exception {
		Pair<Expr, Expr> value = getQuotientRemainderPair(
				modExpression.argument(0), modExpression.argument(1));

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
	private Expr translateIntegerDivision(TreeExpressionIF quotientExpression)
			throws Cvc3Exception {
		Pair<Expr, Expr> value = getQuotientRemainderPair(
				quotientExpression.argument(0), quotientExpression.argument(1));

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
			TreeExpressionIF numeratorExpression,
			TreeExpressionIF denominatorExpression) throws Cvc3Exception {
		Pair<TreeExpressionIF, TreeExpressionIF> key = new Pair<TreeExpressionIF, TreeExpressionIF>(
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
	private Expr translateArrayRead(TreeExpressionIF expr) throws Cvc3Exception {
		TreeExpressionIF arrayExpression = expr.argument(0);
		Expr array = translate(arrayExpression);
		Expr index = translate(expr.argument(1));
		Expr result;

		if (!(arrayExpression.type() instanceof SymbolicCompleteArrayTypeIF)) {
			array = vc.tupleSelectExpr(array, 1);
		}
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
	private Expr translateArrayWrite(TreeExpressionIF expr)
			throws Cvc3Exception {
		TreeExpressionIF arrayExpression = expr.argument(0);
		Expr array = translate(arrayExpression);
		Expr index = translate(expr.argument(1));
		Expr value = translate(expr.argument(2));
		Expr result;

		if (!(arrayExpression.type() instanceof SymbolicCompleteArrayTypeIF)) {
			array = vc.tupleSelectExpr(array, 1);
		}
		result = vc.writeExpr(array, index, value);
		return result;
	}

	private Expr translateQuantifier(TreeExpressionIF expr)
			throws Cvc3Exception {
		Expr variable = this.translateSymbolicConstant(
				(SymbolicConstantExpressionIF) (expr.argument(0)), true);
		List<Expr> vars = new LinkedList<Expr>();
		Expr predicate = translate(expr.argument(1));
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
				extent1 = translate(universe
						.tree(((SymbolicCompleteArrayTypeIF) arrayType1)
								.extent()));
				array1 = cvcExpression1;
			} else {
				extent1 = vc.tupleSelectExpr(cvcExpression1, 0);
				array1 = vc.tupleSelectExpr(cvcExpression1, 1);
			}
			if (arrayType2 instanceof SymbolicCompleteArrayTypeIF) {
				extent2 = translate(universe
						.tree(((SymbolicCompleteArrayTypeIF) arrayType2)
								.extent()));
				array2 = cvcExpression2;
			} else {
				extent2 = vc.tupleSelectExpr(cvcExpression2, 0);
				array2 = vc.tupleSelectExpr(cvcExpression2, 1);
			}
			result = vc.eqExpr(extent1, extent2);
			index = vc.boundVarExpr("i" + boundVariableCounter,
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

	private Expr translateEquality(TreeExpressionIF expr) throws Cvc3Exception {
		TreeExpressionIF leftExpression = expr.argument(0);
		TreeExpressionIF rightExpression = expr.argument(1);
		SymbolicTypeIF type1 = leftExpression.type();
		SymbolicTypeIF type2 = rightExpression.type();
		Expr cvcExpression1 = translate(leftExpression);
		Expr cvcExpression2 = translate(rightExpression);
		Expr result = processEquality(type1, type2, cvcExpression1,
				cvcExpression2);

		return result;
	}

	private Expr translateTuple(TreeExpressionIF symbolicExpression)
			throws Cvc3Exception {
		List<Expr> elements = new LinkedList<Expr>();
		int numFields = symbolicExpression.numArguments();
		Expr result;

		for (int i = 0; i < numFields; i++) {
			elements.add(this.translate(symbolicExpression.argument(i)));
		}
		result = vc.tupleExpr(elements);
		return result;
	}

	private Expr translateTupleRead(TreeExpressionIF symbolicExpression)
			throws Cvc3Exception {
		Expr tuple = this.translate(symbolicExpression.argument(0));
		TreeExpressionIF argument1 = symbolicExpression.argument(1);
		NumberIF indexNumber;
		int index;
		Expr result;

		if (!(argument1 instanceof NumericConcreteExpressionIF))
			throw new SARLInternalException(
					"Argument 1 to tuple read must be concrete integer");
		indexNumber = ((NumericConcreteExpressionIF) argument1).value();
		if (!(indexNumber instanceof IntegerNumberIF))
			throw new SARLInternalException(
					"Argument 1 to tuple read must be integer");
		index = ((IntegerNumberIF) indexNumber).intValue();
		result = vc.tupleSelectExpr(tuple, index);
		return result;
	}

	private Expr translateTupleWrite(TreeExpressionIF symbolicExpression)
			throws Cvc3Exception {
		Expr tuple = this.translate(symbolicExpression.argument(0));
		TreeExpressionIF argument1 = symbolicExpression.argument(1);
		Expr newValue = this.translate(symbolicExpression.argument(2));
		NumberIF indexNumber;
		int index;
		Expr result;

		if (!(argument1 instanceof NumericConcreteExpressionIF))
			throw new SARLInternalException(
					"Argument 1 to tuple read must be concrete integer");
		indexNumber = ((NumericConcreteExpressionIF) argument1).value();
		if (!(indexNumber instanceof IntegerNumberIF))
			throw new SARLInternalException(
					"Argument 1 to tuple read must be integer");
		index = ((IntegerNumberIF) indexNumber).intValue();
		result = vc.tupleUpdateExpr(tuple, index, newValue);
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
			cvcAssumption = this.translate(this.universe
					.tree(symbolicAssumption));
			vc.assertFormula(cvcAssumption);
			cvcPredicate = this
					.translate(this.universe.tree(symbolicPredicate));
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

	private boolean isOne(TreeExpressionIF symbolicExpression) {
		return symbolicExpression.operator() == SymbolicOperator.CONCRETE_NUMBER
				&& ((NumericConcreteExpressionIF) symbolicExpression).isOne();
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
