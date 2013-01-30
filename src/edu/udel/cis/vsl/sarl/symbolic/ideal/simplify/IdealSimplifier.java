package edu.udel.cis.vsl.sarl.symbolic.ideal.simplify;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.IntervalIF;
import edu.udel.cis.vsl.sarl.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive.BooleanPrimitiveKind;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive.NumericPrimitiveKind;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.affine.AffineExpression;
import edu.udel.cis.vsl.sarl.symbolic.affine.AffineFactory;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayExpression;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayLambdaExpression;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayRead;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayWrite;
import edu.udel.cis.vsl.sarl.symbolic.cast.RealCastExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.BasicExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.CnfBooleanExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.symbolic.cnf.LiteralExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.OrExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.QuantifierExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.QuantifierExpression.Quantifier;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.cond.ConditionalExpression;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantExpression;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantFactory;
import edu.udel.cis.vsl.sarl.symbolic.factor.Factorization;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.function.EvaluatedFunctionExpression;
import edu.udel.cis.vsl.sarl.symbolic.function.LambdaExpression;
import edu.udel.cis.vsl.sarl.symbolic.ideal.BooleanIdealExpression;
import edu.udel.cis.vsl.sarl.symbolic.ideal.IdealExpression;
import edu.udel.cis.vsl.sarl.symbolic.ideal.IdealUniverse;
import edu.udel.cis.vsl.sarl.symbolic.integer.IntegerDivisionExpression;
import edu.udel.cis.vsl.sarl.symbolic.integer.IntegerModulusExpression;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.MonomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.Polynomial;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpression;
import edu.udel.cis.vsl.sarl.symbolic.rational.RationalExpression;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalExpression;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalExpression.RelationKind;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalFactory;
import edu.udel.cis.vsl.sarl.symbolic.tuple.Tuple;
import edu.udel.cis.vsl.sarl.symbolic.tuple.TupleRead;
import edu.udel.cis.vsl.sarl.symbolic.tuple.TupleWrite;
import edu.udel.cis.vsl.sarl.symbolic.util.Simplifier;

/**
 * An implementation of SimplifierIF for the Ideal Universe. Provides methods to
 * take a symbolic expression from an ideal universe and return a "simplified"
 * version of the expression which is equivalent to the original in the
 * mathematical "ideal" semantics. Similar method is provided for types.
 * 
 * @author siegel
 * 
 */
public class IdealSimplifier extends Simplifier {

	private IdealUniverse universe;

	/**
	 * The current assumption underlying this simplifier. Initially this is the
	 * assumption specified at construction, but it can be simplified during
	 * construction. After construction completes, it does not change. Also, any
	 * symbolic constant that is determined to have a concrete value is removed
	 * from this assumption; the concrete value can be obtained from the
	 * constantMap or booleanMap.
	 */
	private BooleanIdealExpression assumption;

	/**
	 * This is the same as the assumption, but without the information from the
	 * boundMap, booleanMap, and constantMap thrown in.
	 */
	private BooleanIdealExpression rawAssumption;

	/**
	 * A map that assigns bounds to pseudo primitive factored polynomials.
	 */
	private Map<FactoredPolynomial, BoundsObject> boundMap = new HashMap<FactoredPolynomial, BoundsObject>();

	/**
	 * A map that assigns concrete boolean values to boolean primitive
	 * expressions.
	 */
	private Map<BooleanPrimitive, Boolean> booleanMap = new HashMap<BooleanPrimitive, Boolean>();

	/**
	 * The keys in this map are pseudo-primitive factored polynomials. See
	 * AffineExpression for the definition. The value is the constant value that
	 * has been determined to be the value of that pseudo.
	 */
	private Map<FactoredPolynomial, NumberIF> constantMap = new HashMap<FactoredPolynomial, NumberIF>();

	/** Cached simplifications. */
	private Map<TreeExpressionIF, Simplification> treeSimplifyMap = new HashMap<TreeExpressionIF, Simplification>();

	private PrintStream debugOut = null;

	// TODO: also would like to map symbolic constants that can be solved
	// for in terms of earlier ones to expressions...

	private boolean intervalComputed = false;

	private IntervalIF interval = null;

	private SymbolicConstantIF intervalVariable = null;

	/**
	 * Treat every polynomial as a linear combination of monomials, so Gaussian
	 * elimination is performed on all equalities, and not just degree 1
	 * equalities.
	 */
	private boolean linearizePolynomials = false;

	public IdealSimplifier(IdealUniverse universe,
			BooleanIdealExpression assumption) {
		super(universe);

		this.universe = universe;
		if (verbose()) {
			out().println(
					"Ideal simplifier created with assumption: " + assumption);
			out().flush();
		}
		this.assumption = assumption;
		initialize();
		if (verbose()) {
			out().println("New assumption: " + assumption);
		}
	}

	private boolean verbose() {
		return debugOut != null;
	}

	private PrintStream out() {
		return debugOut;
	}

	public IdealUniverse universe() {
		return universe;
	}

	NumberFactoryIF numberFactory() {
		return universe.numberFactory();
	}

	FactoredPolynomialFactory fpFactory() {
		return universe.factoredPolynomialFactory();
	}

	ConcreteFactory concreteFactory() {
		return universe.concreteFactory();
	}

	MonomialFactory monomialFactory() {
		return universe.monomialFactory();
	}

	AffineFactory affineFactory() {
		return universe.affineFactory();
	}

	RelationalFactory relationalFactory() {
		return universe.relationalFactory();
	}

	CnfFactory cnfFactory() {
		return universe.cnfFactory();
	}

	SymbolicConstantFactory constantFactory() {
		return universe.symbolicConstantFactory();
	}

	private BooleanIdealExpression trueExpression() {
		return universe.concreteExpression(true);
	}

	private BooleanIdealExpression falseExpression() {
		return universe.concreteExpression(false);
	}

	private CnfBooleanExpression cnfTrue() {
		return cnfFactory().booleanExpression(true);
	}

	private CnfBooleanExpression cnfFalse() {
		return cnfFactory().booleanExpression(false);
	}

	/***********************************************************************
	 * Begin Simplification Routines...................................... *
	 ***********************************************************************/

	/**
	 * Simplifies an IdealExpression. The input must be an instance of
	 * IdealExpression. Uses the constantMap, booleanMap, and boundMap to
	 * simplify expressions.
	 */
	public IdealExpression simplify(SymbolicExpressionIF expression) {
		if (expression == null)
			return null;
		if (verbose()) {
			out().println("Simplifying expression: " + expression);
			out().flush();
		}
		if (!(expression instanceof IdealExpression)) {
			throw new IllegalArgumentException(
					"Expression must be an ideal expression: " + expression);
		} else {
			IdealExpression ideal = (IdealExpression) expression;
			IdealExpression result = simplifyTree(ideal.expression()).result();

			if (verbose()) {
				out().println("Result of simplification: " + result);
				out().flush();
			}
			assert result != null;
			return result;
		}
	}

	/**
	 * Takes a tree expression in ideal canonical form, and simplifies it,
	 * returning a tree expression in canonical form.
	 * <p>
	 * 
	 * If type is boolean, the expression and result returned are instances of
	 * CnfBooleanExpression. If type is integer, FactoredPolynomial. If type is
	 * real, RationalExpression.
	 * <p>
	 * 
	 * If the type is not one of those primitive types, then the expression must
	 * be an instance of one of the following, and an instance of one of the
	 * following will also be returned:
	 * 
	 * Tuple, TupleRead, TupleWrite, ArrayRead, ArrayWrite,
	 * EvaluatedFunctionExpression, ConditionalExpression,
	 * SymbolicConstantExpression.
	 */
	private Simplification simplifyTree(TreeExpressionIF expression) {
		assert expression != null;

		Simplification result = treeSimplifyMap.get(expression);

		if (result != null)
			return result;

		SymbolicTypeIF type = expression.type();

		if (type.isBoolean()) {
			assert expression instanceof CnfBooleanExpression;
			// result will be instance of CnfBooleanExpression
			result = simplifyCnf((CnfBooleanExpression) expression);
		} else if (type.isInteger()) {
			assert expression instanceof FactoredPolynomial;
			// result will be instance of FactoredPolynomial (if integer type)
			// or RationalExpression (if real type)...
			result = simplifyFactoredPolynomial((FactoredPolynomial) expression);
		} else if (type.isReal()) {
			assert expression instanceof RationalExpression;
			// result will be instance of RationalExpression....
			result = simplifyRational((RationalExpression) expression);
		} else if (expression instanceof Tuple) {
			result = simplifyTuple((Tuple) expression);
		} else if (expression instanceof TupleRead) {
			result = simplifyTupleRead((TupleRead) expression);
		} else if (expression instanceof TupleWrite) {
			result = simplifyTupleWrite((TupleWrite) expression);
		} else if (expression instanceof ArrayRead) {
			result = simplifyArrayRead((ArrayRead) expression);
		} else if (expression instanceof ArrayWrite) {
			result = simplifyArrayWrite((ArrayWrite) expression);
		} else if (expression instanceof ArrayLambdaExpression) {
			result = simplifyArrayLambda((ArrayLambdaExpression) expression);
		} else if (expression instanceof ArrayExpression) {
			result = simplifyArrayExpression((ArrayExpression) expression);
		} else if (expression instanceof EvaluatedFunctionExpression) {
			result = simplifyApply((EvaluatedFunctionExpression) expression);
		} else if (expression instanceof LambdaExpression) {
			result = simplifyLambda((LambdaExpression) expression);
		} else if (expression instanceof ConditionalExpression) {
			result = simplifyConditional((ConditionalExpression) expression);
		} else if (expression instanceof SymbolicConstantExpression) {
			result = simplifySymbolicConstant((SymbolicConstantExpression) expression);
		} else {
			throw new IllegalArgumentException("Unknown type of expression: "
					+ expression);
		}
		treeSimplifyMap.put(expression, result);
		return result;
	}

	/**
	 * Simplifies a CnfBooleanExpression. Result will be instanceof
	 * CnfBooleanExpression.
	 */
	private Simplification simplifyCnf(CnfBooleanExpression cnf) {
		Simplification simplification = treeSimplifyMap.get(cnf);

		if (simplification != null)
			return simplification;

		int numClauses = cnf.numClauses();
		Simplification[] arguments = new Simplification[numClauses];
		IdealExpression result = null;

		for (int i = 0; i < numClauses; i++) {
			OrExpression clause = cnf.clause(i);
			Simplification argument = simplifyOr(clause);

			if (result != null) {
				result = universe.and(result, argument.result());
			} else {
				if (argument.success()) {
					result = trueExpression();
					for (int j = 0; j < i; j++)
						result = universe.and(result, arguments[j].result());
					result = universe.and(result, argument.result());
				} else {
					arguments[i] = argument;
				}
			}
		}
		if (result == null)
			simplification = new Simplification(cnf,
					universe.canonicalizeTree(cnf), false);
		else
			simplification = new Simplification(cnf, result, true);
		treeSimplifyMap.put(cnf, simplification);
		return simplification;
	}

	/** Simplifies an OrExpression, returning a CnfBooleanExpression. */
	private Simplification simplifyOr(OrExpression or) {
		Simplification simplification = treeSimplifyMap.get(or);

		if (simplification != null)
			return simplification;

		int numClauses = or.numClauses();
		Simplification[] arguments = new Simplification[numClauses];
		boolean change = false;
		IdealExpression result;

		for (int i = 0; i < numClauses; i++) {
			BasicExpression basic = or.clause(i);
			Simplification argument = simplifyBasic(basic);

			change = change || argument.success();
			arguments[i] = argument;
		}
		if (change) {
			result = falseExpression();
			for (int i = 0; i < numClauses; i++) {
				result = universe.or(result, arguments[i].result());
			}
		} else {
			result = universe.canonicalizeTree(or);
		}
		simplification = new Simplification(or, result, change);
		treeSimplifyMap.put(or, simplification);
		return simplification;
	}

	/** Result is CnfBooleanExpression. */
	private Simplification simplifyBasic(BasicExpression basic) {
		if (basic instanceof LiteralExpression) {
			return simplifyLiteral((LiteralExpression) basic);
		} else if (basic instanceof QuantifierExpression) {
			return simplifyQuantifier((QuantifierExpression) basic);
		} else if (basic instanceof RelationalExpression) {
			return simplifyRelational((RelationalExpression) basic);
		} else {
			throw new IllegalArgumentException("Unknown type of basic: "
					+ basic);
		}
	}

	/** Result is CnfBooleanExpression */
	private Simplification simplifyLiteral(LiteralExpression literal) {
		Simplification simplification = treeSimplifyMap.get(literal);

		if (simplification != null)
			return simplification;

		Simplification arg = simplifyBooleanPrimitive(literal.primitive());

		if (arg.success()) {
			IdealExpression result;

			if (literal.not())
				result = universe.not(arg.result());
			else
				result = arg.result();
			simplification = new Simplification(literal, result, true);
		} else {
			simplification = new Simplification(literal,
					universe.canonicalizeTree(literal), false);
		}
		treeSimplifyMap.put(literal, simplification);
		return simplification;
	}

	/** Result is CnfBooleanExpression */
	private Simplification simplifyQuantifier(QuantifierExpression expression) {
		Simplification simplification = treeSimplifyMap.get(expression);

		if (simplification != null)
			return simplification;

		Simplification predicateSimplification = simplifyCnf(expression
				.predicate());

		if (predicateSimplification.success()) {
			IdealExpression result;

			if (expression.quantifier() == Quantifier.FORALL)
				result = universe.forall(expression.variable()
						.symbolicConstant(), predicateSimplification.result());
			else
				result = universe.exists(expression.variable()
						.symbolicConstant(), predicateSimplification.result());
			simplification = new Simplification(expression, result, true);
		} else {
			simplification = new Simplification(expression,
					universe.canonicalizeTree(expression), false);
		}
		treeSimplifyMap.put(expression, simplification);
		return simplification;
	}

	/**
	 * Simplifies a boolean primitive, always returning a CnfBooleanExpression.
	 * Lookup in the boolean map is done here.
	 */
	private Simplification simplifyBooleanPrimitive(BooleanPrimitive primitive) {
		Simplification simplification = treeSimplifyMap.get(primitive);

		if (simplification != null)
			return simplification;

		Boolean value = booleanMap.get(primitive);

		if (value != null)
			return new Simplification(primitive, (value ? trueExpression()
					: falseExpression()), true);

		switch (primitive.booleanPrimitiveKind()) {
		case ARRAY_READ:
			simplification = simplifyArrayRead((ArrayRead) primitive);
			break;
		case TUPLE_READ:
			simplification = simplifyTupleRead((TupleRead) primitive);
			break;
		case APPLY:
			simplification = simplifyApply((EvaluatedFunctionExpression) primitive);
			break;
		case SYMBOLIC_CONSTANT:
			simplification = simplifySymbolicConstant((SymbolicConstantExpressionIF) primitive);
			break;
		default:
			throw new IllegalArgumentException(
					"Unknown type of boolean primitive: " + primitive);
		}
		treeSimplifyMap.put(primitive, simplification);
		return simplification;
	}

	/**
	 * Simplifies a numeric primitive. The type of the primitive will be either
	 * integer or real. If the type is integer, a FactoredPolynomial is
	 * returned. If the type is real, a RationalExpression is returned.
	 * 
	 * @param primitive
	 * @return
	 */
	private Simplification simplifyNumericPrimitive(NumericPrimitive primitive) {
		Simplification simplification = treeSimplifyMap.get(primitive);

		if (simplification != null)
			return simplification;

		FactoredPolynomial pseudo = fpFactory().factoredPolynomial(primitive);
		NumberIF value = constantMap.get(pseudo);

		if (value != null) {
			simplification = new Simplification(primitive,
					universe.concreteExpression(value), true);
		} else {
			switch (primitive.numericPrimitiveKind()) {
			case CAST:
				// result will be instance of RationalExpression...
				simplification = simplifyCast((RealCastExpression) primitive);
				break;
			case ARRAY_READ:
				// result will be either factored poly or rational, depending on
				// type...
				simplification = simplifyArrayRead((ArrayRead) primitive);
				break;
			case TUPLE_READ:
				// result will be either factored poly or rational, depending on
				// type...
				simplification = simplifyTupleRead((TupleRead) primitive);
				break;
			case SYMBOLIC_CONSTANT:
				// result will be either factored poly or rational, depending on
				// type...
				simplification = simplifySymbolicConstant((SymbolicConstantExpression) primitive);
				break;
			case APPLY:
				simplification = simplifyApply((EvaluatedFunctionExpression) primitive);
				break;
			case COND:
				simplification = simplifyConditional((ConditionalExpression) primitive);
				break;
			case INT_DIV:
				// result will be factored poly...
				simplification = simplifyIntDiv((IntegerDivisionExpression) primitive);
				break;
			case INT_MOD:
				// result will be factored poly...
				simplification = simplifyIntMod((IntegerModulusExpression) primitive);
				break;
			default:
				throw new RuntimeException(
						"Unknown kind of numeric primitive: " + primitive);
			}
		}
		treeSimplifyMap.put(primitive, simplification);
		return simplification;
	}

	/*
	 * Simplifies an ArrayRead expression. The result will be either an
	 * ArrayRead, or an element of the array (if the index expression could be
	 * concretized and an explicit element extracted from the array). The
	 * elements of the array must be instances of TreeExpressionIF.
	 */
	private Simplification simplifyArrayRead(ArrayRead read) {
		Simplification simplification = treeSimplifyMap.get(read);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = read.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		FactoredPolynomial oldIndex = (FactoredPolynomial) read.index();
		Simplification indexSimplification = simplifyFactoredPolynomial(oldIndex);
		TreeExpressionIF oldArray = read.array();
		Simplification arraySimplification = simplifyTree(oldArray);
		boolean change = !newType.equals(oldType)
				|| indexSimplification.success()
				|| arraySimplification.success();

		if (change) {
			IdealExpression result = universe.arrayRead(
					arraySimplification.result(), indexSimplification.result());

			simplification = new Simplification(read, result, true);
		} else {
			simplification = new Simplification(read,
					universe.canonicalizeTree(read), false);
		}
		treeSimplifyMap.put(read, simplification);
		return simplification;
	}

	private Simplification simplifyArrayWrite(ArrayWrite write) {
		Simplification simplification = treeSimplifyMap.get(write);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = write.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		FactoredPolynomial oldIndex = (FactoredPolynomial) write.index();
		Simplification indexSimplification = simplifyFactoredPolynomial(oldIndex);
		TreeExpressionIF oldArray = write.array();
		Simplification arraySimplification = simplifyTree(oldArray);
		TreeExpressionIF oldValue = write.value();
		Simplification valueSimplification = simplifyTree(oldValue);
		boolean change = !newType.equals(oldType)
				|| indexSimplification.success()
				|| arraySimplification.success()
				|| valueSimplification.success();

		if (change) {
			IdealExpression result = universe.arrayWrite(
					arraySimplification.result(), indexSimplification.result(),
					valueSimplification.result());

			simplification = new Simplification(write, result, true);
		} else {
			simplification = new Simplification(write,
					universe.canonicalizeTree(write), false);
		}
		treeSimplifyMap.put(write, simplification);
		return simplification;
	}

	private Simplification simplifyArrayLambda(ArrayLambdaExpression arrayLambda) {
		Simplification simplification = treeSimplifyMap.get(arrayLambda);

		if (simplification != null)
			return simplification;

		SymbolicCompleteArrayTypeIF oldType = arrayLambda.type();
		SymbolicCompleteArrayTypeIF newType = (SymbolicCompleteArrayTypeIF) simplifyType(oldType);
		TreeExpressionIF oldFunction = arrayLambda.function();
		Simplification functionSimplification = simplifyTree(oldFunction);
		boolean change = !newType.equals(oldType)
				|| functionSimplification.success();

		if (change) {
			IdealExpression result = universe.arrayLambda(newType,
					functionSimplification.result());

			simplification = new Simplification(arrayLambda, result, true);
		} else {
			simplification = new Simplification(arrayLambda,
					universe.canonicalizeTree(arrayLambda), false);
		}
		treeSimplifyMap.put(arrayLambda, simplification);
		return simplification;
	}

	private Simplification simplifyArrayExpression(ArrayExpression array) {
		Simplification simplification = treeSimplifyMap.get(array);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = array.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		TreeExpressionIF oldOrigin = array.origin();
		Simplification originSimplification = simplifyTree(oldOrigin);
		TreeExpressionIF[] oldElements = array.elements();
		int length = oldElements.length;
		TreeExpressionIF[] newElements = null;

		for (int i = 0; i < length; i++) {
			TreeExpressionIF oldElement = oldElements[i];

			if (oldElement != null) {
				Simplification elementSimplification = simplifyTree(oldElement);

				if (elementSimplification.success()) {
					if (newElements == null) {
						newElements = new TreeExpressionIF[length];

						for (int j = 0; j < i; j++)
							newElements[j] = oldElements[j];
					}
					newElements[i] = elementSimplification.result()
							.expression();
				}
			}
		}
		if (!newType.equals(oldType) || originSimplification.success()
				|| newElements != null) {
			if (newElements == null)
				newElements = oldElements;

			ArrayExpression newArray = universe.arrayFactory().arrayExpression(
					originSimplification.result().expression(), newElements);
			IdealExpression result = universe.ideal(newArray);

			simplification = new Simplification(array, result, true);
		} else {
			simplification = new Simplification(array,
					universe.canonicalizeTree(array), false);
		}
		treeSimplifyMap.put(array, simplification);
		return simplification;
	}

	private Simplification simplifyTupleRead(TupleRead read) {
		Simplification simplification = treeSimplifyMap.get(read);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = read.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		NumericConcreteExpressionIF index = read.index();
		TreeExpressionIF oldTuple = read.tuple();
		Simplification tupleSimplification = simplifyTree(oldTuple);
		boolean change = !oldType.equals(newType)
				|| tupleSimplification.success();

		if (change) {
			simplification = new Simplification(read, universe.tupleRead(
					tupleSimplification.result(), index), true);
		} else {
			simplification = new Simplification(read,
					universe.canonicalizeTree(read), false);
		}
		treeSimplifyMap.put(read, simplification);
		return simplification;
	}

	private Simplification simplifyTupleWrite(TupleWrite write) {
		Simplification simplification = treeSimplifyMap.get(write);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = write.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		NumericConcreteExpressionIF index = write.index();
		TreeExpressionIF oldTuple = write.tuple();
		Simplification tupleSimplification = simplifyTree(oldTuple);
		TreeExpressionIF oldValue = write.value();
		Simplification valueSimplification = simplifyTree(oldValue);
		boolean change = !oldType.equals(newType)
				|| tupleSimplification.success()
				|| valueSimplification.success();

		if (change) {
			simplification = new Simplification(write, universe.tupleWrite(
					tupleSimplification.result(), index,
					valueSimplification.result()), true);
		} else {
			simplification = new Simplification(write,
					universe.canonicalizeTree(write), false);
		}
		treeSimplifyMap.put(write, simplification);
		return simplification;
	}

	private Simplification simplifyTuple(Tuple tuple) {
		Simplification simplification = treeSimplifyMap.get(tuple);

		if (simplification != null)
			return simplification;

		SymbolicTupleTypeIF oldType = tuple.type();
		SymbolicTupleTypeIF newType = (SymbolicTupleTypeIF) simplifyType(oldType);
		boolean change = !newType.equals(oldType);
		TreeExpressionIF[] oldComponents = tuple.components();
		int numElements = oldComponents.length;
		IdealExpression[] newComponents = new IdealExpression[numElements];

		for (int i = 0; i < numElements; i++) {
			Simplification elementSimplification = simplifyTree(oldComponents[i]);

			change = change || elementSimplification.success();
			newComponents[i] = elementSimplification.result();
		}
		if (change) {
			simplification = new Simplification(tuple,
					universe.tupleExpression(newType, newComponents), true);
		} else {
			simplification = new Simplification(tuple,
					universe.canonicalizeTree(tuple), false);
		}
		treeSimplifyMap.put(tuple, simplification);
		return simplification;
	}

	private Simplification simplifyConditional(ConditionalExpression cond) {
		Simplification simplification = treeSimplifyMap.get(cond);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = cond.type();
		SymbolicTypeIF newType = (SymbolicTupleTypeIF) simplifyType(oldType);
		TreeExpressionIF oldPredicate = cond.predicate();
		Simplification predicateSimplification = simplifyTree(oldPredicate);
		TreeExpressionIF trueBranch = cond.trueValue();
		Simplification trueSimplification = simplifyTree(trueBranch);
		TreeExpressionIF falseBranch = cond.falseValue();
		Simplification falseSimplification = simplifyTree(falseBranch);
		boolean change = !newType.equals(oldType)
				|| predicateSimplification.success()
				|| trueSimplification.success()
				|| falseSimplification.success();

		if (change) {
			IdealExpression result = universe.cond(
					predicateSimplification.result(),
					trueSimplification.result(), falseSimplification.result());

			simplification = new Simplification(cond, result, true);
		} else {
			simplification = new Simplification(cond,
					universe.canonicalizeTree(cond), false);
		}
		treeSimplifyMap.put(cond, simplification);
		return simplification;
	}

	/** Result is FactoredPolynomial */
	private Simplification simplifyIntDiv(IntegerDivisionExpression expression) {
		Simplification simplification = treeSimplifyMap.get(expression);

		if (simplification != null)
			return simplification;

		TreeExpressionIF numerator = expression.numerator();
		Simplification numeratorSimplification = simplifyTree(numerator);
		TreeExpressionIF denominator = expression.denominator();
		Simplification denominatorSimplification = simplifyTree(denominator);
		boolean change = numeratorSimplification.success()
				|| denominatorSimplification.success();

		if (change) {
			simplification = new Simplification(expression, universe.divide(
					numeratorSimplification.result(),
					denominatorSimplification.result()), true);
		} else {
			simplification = new Simplification(expression,
					universe.canonicalizeTree(expression), false);
		}
		treeSimplifyMap.put(expression, simplification);
		return simplification;
	}

	/** Result is FactoredPolynomial */
	private Simplification simplifyIntMod(IntegerModulusExpression expression) {
		Simplification simplification = treeSimplifyMap.get(expression);

		if (simplification != null)
			return simplification;

		TreeExpressionIF numerator = expression.numerator();
		Simplification numeratorSimplification = simplifyTree(numerator);
		TreeExpressionIF denominator = expression.denominator();
		Simplification denominatorSimplification = simplifyTree(denominator);
		boolean change = numeratorSimplification.success()
				|| denominatorSimplification.success();

		if (change) {
			simplification = new Simplification(expression, universe.modulo(
					numeratorSimplification.result(),
					denominatorSimplification.result()), true);
		} else {
			simplification = new Simplification(expression,
					universe.canonicalizeTree(expression), false);
		}
		treeSimplifyMap.put(expression, simplification);
		return simplification;
	}

	private Simplification simplifyApply(EvaluatedFunctionExpression apply) {
		Simplification simplification = treeSimplifyMap.get(apply);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = apply.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		TreeExpressionIF oldFunction = apply.function();
		Simplification functionSimplification = simplifyTree(oldFunction);
		int numArgs = apply.arguments().length;
		IdealExpression[] newArguments = new IdealExpression[numArgs];
		boolean change = !newType.equals(oldType)
				|| functionSimplification.success();

		for (int i = 0; i < numArgs; i++) {
			Simplification argumentSimplification = simplifyTree(apply
					.functionArgument(i));
			change = change || argumentSimplification.success();
			newArguments[i] = argumentSimplification.result();
		}
		if (change) {
			// need array of function arguments...
			simplification = new Simplification(apply, universe.apply(
					functionSimplification.result(), newArguments), true);
		} else {
			simplification = new Simplification(apply,
					universe.canonicalizeTree(apply), false);
		}
		treeSimplifyMap.put(apply, simplification);
		return simplification;
	}

	// type might change...
	// simplify symbolicConstantExpression, expression, type
	private Simplification simplifyLambda(LambdaExpression lambda) {
		Simplification simplification = treeSimplifyMap.get(lambda);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = lambda.type();
		SymbolicTypeIF newType = simplifyType(oldType);
		SymbolicConstantExpressionIF oldSymbolicConstantExpression = lambda
				.variable();
		Simplification constantSimplification = simplifySymbolicConstant(oldSymbolicConstantExpression);
		TreeExpressionIF oldValueExpression = lambda.expression();
		Simplification valueSimplification = simplifyTree(oldValueExpression);
		boolean change = !newType.equals(oldType)
				|| constantSimplification.success()
				|| valueSimplification.success();

		if (change) {
			SymbolicConstantIF symbolicConstant = ((SymbolicConstantExpressionIF) constantSimplification
					.result().expression()).symbolicConstant();

			simplification = new Simplification(lambda, universe.lambda(
					symbolicConstant, valueSimplification.result()), true);
		} else {
			simplification = new Simplification(lambda,
					universe.canonicalizeTree(lambda), false);
		}
		treeSimplifyMap.put(lambda, simplification);
		return simplification;
	}

	/**
	 * Simplifies a "cast to real" expression. The result is always a rational
	 * expression and has type real.
	 */
	private Simplification simplifyCast(RealCastExpression cast) {
		Simplification simplification = treeSimplifyMap.get(cast);

		if (simplification != null)
			return simplification;

		NumericPrimitive intExpression = cast.integerExpression();
		Simplification intSimplification = simplifyNumericPrimitive(intExpression);

		if (intSimplification.success()) {
			simplification = new Simplification(cast,
					universe.castToReal(intSimplification.result()), true);
		} else {
			simplification = new Simplification(cast,
					universe.canonicalizeTree(cast), false);
		}
		treeSimplifyMap.put(cast, simplification);
		return simplification;
	}

	/**
	 * Simplifies a symbolic constant expression.
	 * 
	 * If type is boolean, this returns an instance of CnfBooleanExpression.
	 * 
	 * If type is integer, this returns an instance of FactoredPolynomial.
	 * 
	 * If type is real, this returns an instance of RationalExpression.
	 * 
	 * Otherwise, this returns an instance of SymbolicConstantExpression. In
	 * this case, the type returned may differ from the original type because
	 * simplifications can change the type. For example, type int[N] might
	 * change to int[10], if N simplifies to 10.
	 */
	private Simplification simplifySymbolicConstant(
			SymbolicConstantExpressionIF expression) {
		Simplification simplification = treeSimplifyMap.get(expression);

		if (simplification != null)
			return simplification;

		SymbolicTypeIF oldType = expression.type();

		if (oldType.isNumeric()) {
			FactoredPolynomial pseudo = fpFactory().factoredPolynomial(
					(SymbolicConstantExpression) expression);
			NumberIF value = constantMap.get(pseudo);

			if (value != null) {
				simplification = new Simplification(expression,
						universe.canonicalizeTree(concreteFactory().concrete(
								value)), true);
			} else {
				simplification = new Simplification(expression,
						universe.canonicalizeTree(expression), false);
			}
		} else if (oldType.isBoolean()) {
			Boolean value = booleanMap.get(expression); // why does this work?
			// expression is SymbolicConstantExpressionIF is not a
			// BooleanPrimitive
			// note: a BooleanPrimitive "is a" SymbolicExpressionIF

			if (value != null) {
				simplification = new Simplification(expression,
						(value ? trueExpression() : falseExpression()), true);
			} else {
				simplification = new Simplification(expression,
						universe.canonicalizeTree(expression), false);
			}
		} else {
			SymbolicTypeIF newType = simplifyType(oldType);
			boolean change = !newType.equals(oldType);

			if (change) {
				SymbolicConstantIF oldSymbolicConstant = expression
						.symbolicConstant();
				SymbolicConstantIF newSymbolicConstant = universe
						.getOrCreateSymbolicConstant(
								oldSymbolicConstant.name(), newType);

				simplification = new Simplification(expression,
						universe.canonicalizeTree(constantFactory().expression(
								newSymbolicConstant)), true);
			} else {
				simplification = new Simplification(expression,
						universe.canonicalizeTree(expression), false);
			}
		}
		treeSimplifyMap.put(expression, simplification);
		return simplification;
	}

	/**
	 * Simplifies a rational expression, returning a rational expression. Type
	 * is of course real.
	 */
	private Simplification simplifyRational(RationalExpression rational) {
		Simplification simplification = treeSimplifyMap.get(rational);

		if (simplification != null)
			return simplification;

		Simplification numeratorSimplification = simplifyFactoredPolynomial(rational
				.numerator());
		Simplification denominatorSimplification = simplifyFactoredPolynomial(rational
				.denominator());
		boolean change = numeratorSimplification.success()
				|| denominatorSimplification.success();

		if (change) {
			simplification = new Simplification(rational, universe.divide(
					numeratorSimplification.result(),
					denominatorSimplification.result()), true);
		} else {
			simplification = new Simplification(rational,
					universe.canonicalizeTree(rational), false);
		}
		treeSimplifyMap.put(rational, simplification);
		return simplification;
	}

	/**
	 * Simplifies a factored polynomial. Result could be either
	 * FactoredPolynomial or RationalExpression.
	 * 
	 * 
	 * sub(P) { write P=aX+b, X pseudo-primitive factored poly if
	 * map.contains(X) return a*map(X)+b; if P has more than one term: loop over
	 * terms of P and call sub. if any simplify, return sum of result. if P has
	 * more than one factor: loop over factors of P and call sub. if any
	 * simplify, return product of result. return P }
	 */
	private Simplification simplifyFactoredPolynomial(FactoredPolynomial fp) {
		Simplification simplification = treeSimplifyMap.get(fp);

		if (simplification != null)
			return simplification;

		AffineExpression affine = affineFactory().affine(fp);
		FactoredPolynomial pseudo = affine.pseudo();
		NumberIF pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null) {
			simplification = new Simplification(fp,
					universe.concreteExpression(affineFactory().affineValue(
							affine, pseudoValue)), true);
		} else {
			SymbolicTypeIF type = fp.type();
			Polynomial poly = fp.polynomial();
			int numTerms = poly.numTerms();
			IdealExpression result = null;

			if (numTerms >= 1) {
				boolean change = false;
				Monomial[] oldTerms = poly.terms();
				IdealExpression[] newTerms = new IdealExpression[numTerms];

				for (int i = 0; i < numTerms; i++) {
					Monomial oldMonomial = oldTerms[i];
					Simplification monomialSimplification = simplifyMonomial(oldMonomial);

					newTerms[i] = monomialSimplification.result();
					change = change || monomialSimplification.success();
				}
				if (change) {
					result = (type.isInteger() ? universe.zeroInt() : universe
							.zeroReal());
					for (int i = 0; i < numTerms; i++)
						result = universe.add(result, newTerms[i]);
				}
			}
			if (result == null) {
				Factorization factorization = fp.factorization();
				int numFactors = factorization.numFactors();

				if (numFactors > 1) {
					boolean change = false;
					PowerExpression[] oldFactors = factorization.factorPowers();
					IdealExpression[] newFactors = new IdealExpression[numFactors];

					for (int i = 0; i < numFactors; i++) {
						Polynomial oldPolynomial = (Polynomial) oldFactors[i]
								.polynomialPowerBase();
						FactoredPolynomial oldFactoredPolynomial = fpFactory()
								.factoredPolynomial(oldPolynomial);
						Simplification factorSimplification = simplifyFactoredPolynomial(oldFactoredPolynomial);

						change = change || factorSimplification.success();
						newFactors[i] = factorSimplification.result();
					}
					if (change) {
						result = (type.isInteger() ? universe.oneInt()
								: universe.oneReal());
						for (int i = 0; i < numFactors; i++)
							result = universe.multiply(
									result,
									universe.power(newFactors[i],
											oldFactors[i].exponent()));
					} // end if (change)...
				} // end if numFactors>1
			} // end if (result == null)
			if (result == null) {
				simplification = new Simplification(fp,
						universe.canonicalizeTree(fp), false);
			} else {
				simplification = new Simplification(fp, result, true);
			}
		} // end if (pseudoValue != null) {...} else ...
		treeSimplifyMap.put(fp, simplification);
		return simplification;
	}

	private Simplification simplifyMonomial(Monomial monomial) {
		Simplification simplification = treeSimplifyMap.get(monomial);

		if (simplification != null)
			return simplification;

		MonicMonomial monic = monomial.monicMonomial();
		PowerExpression[] factorPowers = monic.factorPowers();
		int numFactorPowers = factorPowers.length;
		Simplification[] factorSimplifications = new Simplification[numFactorPowers];
		boolean change = false;

		for (int i = 0; i < numFactorPowers; i++) {
			PowerExpression factorPower = factorPowers[i];
			NumericPrimitive factor = (NumericPrimitive) factorPower.polynomialPowerBase();
			Simplification factorSimplification = simplifyNumericPrimitive(factor);

			change = change || factorSimplification.success();
			factorSimplifications[i] = factorSimplification;
		}
		if (change) {
			IdealExpression result = universe.canonicalizeTree(monomial
					.coefficient());

			for (int i = 0; i < numFactorPowers; i++) {
				IdealExpression base = factorSimplifications[i].result();
				IdealExpression exponent = universe
						.canonicalizeTree(factorPowers[i].exponent());

				result = universe.multiply(result,
						universe.power(base, exponent));
			}
			simplification = new Simplification(monomial, result, true);
		} else {
			simplification = new Simplification(monomial,
					universe.canonicalizeTree(monomial), false);
		}
		treeSimplifyMap.put(monomial, simplification);
		return simplification;
	}

	/** Result is returned as CnfBooleanExpression */
	private Simplification simplifyRelational(RelationalExpression expression) {
		Simplification simplification = treeSimplifyMap.get(expression);

		if (simplification != null)
			return simplification;

		RelationKind kind = expression.relationKind();
		TreeExpressionIF lhs = expression.expression();
		CnfBooleanExpression result = null;
		Simplification lhsSimplification;

		if (lhs instanceof FactoredPolynomial) {
			lhsSimplification = simplifyFactoredPolynomial((FactoredPolynomial) lhs);
		} else if (lhs instanceof RationalExpression) {
			lhsSimplification = simplifyRational((RationalExpression) lhs);
		} else {
			throw new RuntimeException(
					"Unexpected type of expression in relation: " + lhs);
		}

		RelationalExpression newExpression = expression;
		boolean change = false;

		if (lhsSimplification.success()) {
			change = true;
			lhs = lhsSimplification.result().expression();
			if (lhs instanceof RationalExpression
					&& (kind == RelationKind.NEQ0 || kind == RelationKind.EQ0)) {
				lhs = ((RationalExpression) lhs).numerator();
			}
			newExpression = relationalFactory().relational(kind, lhs);
		}

		switch (kind) {
		case EQ0: {
			result = simplifyEQ0((FactoredPolynomial) lhs);
			break;
		}
		case NEQ0: {
			result = simplifyEQ0((FactoredPolynomial) lhs);
			if (result != null)
				result = cnfFactory().not(result);
			break;
		}
		case GT0:
		case GTE0: {
			if (lhs instanceof FactoredPolynomial)
				result = simplifyGT0((FactoredPolynomial) lhs,
						(kind == RelationKind.GT0 ? true : false));
			else if (lhs instanceof RationalExpression) {
				simplification = simplifyGT0Rational(newExpression);
				treeSimplifyMap.put(expression, simplification);
				return simplification;
			} else
				throw new IllegalArgumentException(
						"Expression in GT0 is neither polynomial nor rational function: "
								+ lhs);
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown relation kind: " + kind);
		}
		if (result != null)
			change = true;

		if (!change)
			simplification = new Simplification(expression,
					universe.canonicalizeTree(expression), false);
		else if (result != null)
			simplification = new Simplification(expression,
					universe.booleanIdeal(result), true);
		else
			simplification = new Simplification(expression,
					universe.canonicalizeTree(newExpression), true);
		treeSimplifyMap.put(expression, simplification);
		return simplification;
	}

	/**
	 * Attempts to simplify the expression fp=0. Returns null if no
	 * simplification is possible, else returns a CnfBoolean expression
	 * equivalent to fp=0.
	 * 
	 * @param fp
	 *            the factored polynomial
	 * @return null or a CnfBoolean expression equivalent to fp=0
	 */
	private CnfBooleanExpression simplifyEQ0(FactoredPolynomial fp) {
		if (fp.isConstant()) {
			return (fp.constantTerm().signum() == 0 ? cnfTrue() : cnfFalse());
		}

		SymbolicTypeIF type = fp.type();
		AffineExpression affine = affineFactory().affine(fp);

		assert affine != null;

		FactoredPolynomial pseudo = affine.pseudo();

		assert pseudo != null;

		NumberIF pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null) {
			if (affineFactory().affineValue(affine, pseudoValue).isZero())
				return cnfTrue();
			else
				return cnfFalse();
		}

		NumberIF offset = affine.offset();
		NumberIF coefficient = affine.coefficient();

		if (type.isInteger()) {
			if (!numberFactory().mod(
					(IntegerNumberIF) offset,
					(IntegerNumberIF) numberFactory().abs(
							(IntegerNumberIF) coefficient)).isZero()) {
				return cnfFalse();
			}
		}
		pseudoValue = numberFactory().negate(
				numberFactory().divide(offset, coefficient));

		BoundsObject oldBounds = boundMap.get(pseudo);

		if (oldBounds == null)
			return null;

		// have bounds on X, now simplify aX+b=0.
		// aX+b=0 => solve for X=-b/a (check int arith)
		// is -b/a within the bounds? if not: return FALSE
		// if yes: no simplification.

		int leftSign, rightSign;

		{
			NumberIF lower = oldBounds.lower();

			if (lower == null)
				leftSign = -1;
			else
				leftSign = numberFactory().subtract(lower, pseudoValue)
						.signum();

			NumberIF upper = oldBounds.upper();

			if (upper == null)
				rightSign = 1;
			else
				rightSign = numberFactory().subtract(upper, pseudoValue)
						.signum();
		}

		// if 0 is not in that interval, return FALSE

		if (leftSign > 0 || (leftSign == 0 && oldBounds.strictLower()))
			return cnfFalse();
		if (rightSign < 0 || (rightSign == 0 && oldBounds.strictUpper()))
			return cnfTrue();

		return null;
	}

	/**
	 * Attempts to simplify the expression fp>?0. Returns null if no
	 * simplification is possible, else returns a CnfBoolean expression
	 * equivalent to fp>?0. (Here >? represents either > or >=, depending on
	 * value of strictInequality.)
	 * 
	 * @param fp
	 *            the factored polynomial
	 * @return null or a CnfBoolean expression equivalent to fp>0
	 */
	private CnfBooleanExpression simplifyGT0(FactoredPolynomial fp,
			boolean strictInequality) {
		if (fp.isConstant()) {
			int signum = fp.constantTerm().signum();

			if (strictInequality)
				return (signum > 0 ? cnfTrue() : cnfFalse());
			else
				return (signum >= 0 ? cnfTrue() : cnfFalse());
		}

		SymbolicTypeIF type = fp.type();
		AffineExpression affine = affineFactory().affine(fp);
		FactoredPolynomial pseudo = affine.pseudo();
		assert pseudo != null;
		NumberIF pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null) {
			int signum = affineFactory().affineValue(affine, pseudoValue)
					.signum();

			if (strictInequality)
				return (signum > 0 ? cnfTrue() : cnfFalse());
			else
				return (signum >= 0 ? cnfTrue() : cnfFalse());
		}

		BoundsObject oldBounds = boundMap.get(pseudo);

		if (oldBounds == null)
			return null;

		NumberIF newBound = affineFactory().bound(affine, strictInequality);
		assert newBound != null;
		// bound on pseudo X, assuming fp=aX+b>?0.
		// If a>0, it is a lower bound. If a<0 it is an upper bound.
		// newBound may or may not be strict
		NumberIF coefficient = affine.coefficient();
		assert coefficient.signum() != 0;
		boolean strictBound = (type.isInteger() ? false : strictInequality);

		int leftSign, rightSign;
		{
			NumberIF lower = oldBounds.lower(), upper = oldBounds.upper();

			if (lower == null)
				leftSign = -1;
			else
				leftSign = numberFactory().subtract(lower, newBound).signum();
			if (upper == null)
				rightSign = 1;
			else
				rightSign = numberFactory().subtract(upper, newBound).signum();
		}

		if (coefficient.signum() > 0) {
			// simplify X>newBound or X>=newBound knowing X is in
			// [oldLowerBound,oldUpperBound]
			// let X'=X-newBound.
			// simplify X'>0 (or X'>=0) knowing X' is in [left,right]
			// if left>0: true
			// if left=0 && (strictleft || strict): true
			// if right<0: false
			// if right=0 && (strictright || strict): false
			if (leftSign > 0
					|| (leftSign == 0 && (!strictBound || oldBounds
							.strictLower())))
				return cnfTrue();
			if (rightSign < 0
					|| (rightSign == 0 && (strictBound || oldBounds
							.strictUpper())))
				return cnfFalse();
			if (rightSign == 0 && !strictBound && !oldBounds.strictUpper()) {
				// X'=0, where X'=X-newBound.
				IdealExpression x = universe.ideal(pseudo);
				BooleanIdealExpression eq = universe.equals(x,
						universe.concreteExpression(newBound));

				return (CnfBooleanExpression) eq.cnf();
			}
		} else {
			// simplify X<newBound or X<=newBound knowing X is in
			// [oldLowerBound,oldUpperBound]
			// simplify X'<0 or X'<=0 knowning X' is in [left,right]
			// if left>0: false
			// if left=0 && (strict || strictleft): false
			// if right<0: true
			// if right=0 && (strictright || strict): true
			if (leftSign > 0
					|| (leftSign == 0 && (strictBound || oldBounds
							.strictLower())))
				return cnfFalse();
			if (rightSign < 0
					|| (rightSign == 0 && (!strictBound || oldBounds
							.strictUpper())))
				return cnfTrue();
			if (leftSign == 0 && !strictBound && !oldBounds.strictLower()) {
				// X'=0, where X'=X-newBound.
				IdealExpression x = universe.ideal(pseudo);
				BooleanIdealExpression eq = universe.equals(x,
						universe.concreteExpression(newBound));

				return (CnfBooleanExpression) eq.cnf();
			}
		}
		return null;
	}

	/**
	 * Attemps to simplify expression p/q>0. If you can determine q>0, result is
	 * simplify(p>0). If you can determine q<0, result is simplify(-p>0). If you
	 * can determine p>0, result is simplify(q>0). If you can determine p<0,
	 * result is simplify(-q>0). If you can determine p=0, result is false.
	 */
	private Simplification simplifyGT0Rational(RelationalExpression relational) {
		RelationKind kind = relational.relationKind();

		assert kind == RelationKind.GT0 || kind == RelationKind.GTE0;
		assert relational.expression() instanceof RationalExpression;

		Simplification rationalSimplification = simplifyRational((RationalExpression) relational
				.expression());
		RationalExpression rational = (RationalExpression) rationalSimplification
				.result().expression();
		FactoredPolynomial numerator = rational.numerator();
		FactoredPolynomial denominator = rational.denominator();

		if (denominator.isConstant()) {
			NumberIF denominatorNumber = denominator.constantTerm();
			int signum = denominatorNumber.signum();

			if (signum == 0) {
				throw new IllegalArgumentException("Denominator is 0: "
						+ relational);
			} else if (signum > 0) { // denominator is positive
				Simplification numeratorGT0Simplification = simplifyRelational(relationalFactory()
						.relational(kind, numerator));

				return new Simplification(relational,
						numeratorGT0Simplification.result(), true);
			} else { // denominator is negative
				Simplification numeratorLT0Simplification = simplifyRelational(relationalFactory()
						.relational(kind, fpFactory().negate(numerator)));

				return new Simplification(relational,
						numeratorLT0Simplification.result(), true);
			}
		}

		boolean DO_RATIONAL_RELATIONAL_OPTIMIZATION = false;

		// this optimization can mess up CVC3's ability sometimes....
		if (DO_RATIONAL_RELATIONAL_OPTIMIZATION) {

			Simplification denominatorGT0Simplification = simplifyRelational(relationalFactory()
					.relational(RelationKind.GT0, denominator));
			Simplification numeratorRelationSimplification = simplifyRelational(relationalFactory()
					.relational(kind, numerator));

			if (denominatorGT0Simplification.success()) {
				IdealExpression denominatorGT0Result = denominatorGT0Simplification
						.result();

				if (denominatorGT0Result.equals(trueExpression())) {
					return new Simplification(relational,
							numeratorRelationSimplification.result(), true);
				} else if (denominatorGT0Result.equals(falseExpression())) {
					Simplification numeratorLT0Simplification = simplifyRelational(relationalFactory()
							.relational(kind, fpFactory().negate(numerator)));

					return new Simplification(relational,
							numeratorLT0Simplification.result(), true);
				}
			}

			if (numeratorRelationSimplification.success()
					&& numeratorRelationSimplification.result().equals(
							trueExpression()))
				return new Simplification(relational,
						denominatorGT0Simplification.result(), true);

			Simplification numeratorNegRelationSimplification = simplifyRelational(relationalFactory()
					.relational(kind, fpFactory().negate(numerator)));

			if (numeratorNegRelationSimplification.success()
					&& numeratorNegRelationSimplification.result().equals(
							trueExpression()))
				return new Simplification(relational, simplifyRelational(
						relationalFactory().relational(RelationKind.GT0,
								fpFactory().negate(denominator))).result(),
						true);
		}

		Simplification numeratorEQ0Simplification = simplifyRelational(relationalFactory()
				.relational(RelationKind.EQ0, numerator));

		if (numeratorEQ0Simplification.result().equals(trueExpression()))
			return new Simplification(relational,
					(kind == RelationKind.GTE0 ? trueExpression()
							: falseExpression()), true);
		if (rationalSimplification.success())
			return new Simplification(relational,
					universe.canonicalizeTree(relationalFactory().relational(
							kind, rational)), true);
		return new Simplification(relational,
				universe.canonicalizeTree(relational), false);
	}

	/***********************************************************************
	 * End of Simplification Routines..................................... *
	 ***********************************************************************/

	public BooleanIdealExpression newAssumption() {
		return assumption;
	}

	/**
	 * Converts the bound to a boolean expression in canoncial form. Returns
	 * null if both upper and lower bound are infinite (equivalent to "true").
	 */
	private IdealExpression boundToIdeal(BoundsObject bound) {
		NumberIF lower = bound.lower(), upper = bound.upper();
		IdealExpression result = null;
		FactoredPolynomial fp = (FactoredPolynomial) bound.expression;
		IdealExpression ideal = simplifyFactoredPolynomial(fp).result();

		if (lower != null) {
			if (bound.strictLower())
				result = universe.lessThan(universe.concreteExpression(lower),
						ideal);
			else
				result = universe.lessThanEquals(
						universe.concreteExpression(lower), ideal);
		}
		if (upper != null) {
			IdealExpression upperResult;

			if (bound.strictUpper())
				upperResult = universe.lessThan(ideal,
						universe.concreteExpression(upper));
			else
				upperResult = universe.lessThanEquals(ideal,
						universe.concreteExpression(upper));
			if (result == null)
				result = upperResult;
			else
				result = universe.and(result, upperResult);
		}
		return result;
	}

	private void initialize() {
		while (true) {
			boundMap.clear();
			treeSimplifyMap.clear();

			boolean satisfiable = extractBounds();

			if (!satisfiable) {
				if (verbose()) {
					out().println("Path condition is unsatisfiable.");
					out().flush();
				}
				assumption = falseExpression();
				return;
			} else {
				// need to substitute into assumption new value of symbolic
				// constants.
				BooleanIdealExpression newAssumption = (BooleanIdealExpression) simplify(assumption);

				rawAssumption = newAssumption;
				for (BoundsObject bound : boundMap.values()) {
					IdealExpression constraint = boundToIdeal(bound);

					if (constraint != null)
						newAssumption = universe.and(newAssumption, constraint);
				}
				// also need to add facts from constant map.
				// but can eliminate any constant values for primitives since
				// those will never occur in the state.

				for (Entry<FactoredPolynomial, NumberIF> entry : constantMap
						.entrySet()) {
					FactoredPolynomial fp = entry.getKey();
					NumericPrimitive primitive = fp.polynomial()
							.extractPrimitive();

					if (primitive != null
							&& primitive.numericPrimitiveKind() == NumericPrimitiveKind.SYMBOLIC_CONSTANT) {
						// symbolic constant: will be entirely eliminated
					} else {
						IdealExpression constraint = universe.equals(
								universe.canonicalize(fp),
								universe.concreteExpression(entry.getValue()));

						newAssumption = universe.and(newAssumption, constraint);
					}
				}

				for (Entry<BooleanPrimitive, Boolean> entry : booleanMap
						.entrySet()) {
					BooleanPrimitive primitive = entry.getKey();

					if (primitive != null
							&& primitive.booleanPrimitiveKind() == BooleanPrimitiveKind.SYMBOLIC_CONSTANT) {
						// symbolic constant: will be entirely eliminated
					} else {
						IdealExpression constraint = universe
								.canonicalizeTree(primitive);

						if (!entry.getValue())
							constraint = universe.not(constraint);
						newAssumption = universe.and(newAssumption, constraint);
					}
				}

				if (assumption.equals(newAssumption))
					break;
				assumption = newAssumption;
			}
		}
		extractRemainingFacts();
	}

	/**
	 * Attempts to determine bounds (upper and lower) on primitive expressions
	 * by examining the assumption. Returns false if ideal is determined to be
	 * unsatisfiable.
	 */
	private boolean extractBounds() {
		CnfBooleanExpression cnf = assumption.cnf();
		int numClauses = cnf.numClauses();

		for (int i = 0; i < numClauses; i++) {
			OrExpression clause = cnf.clause(i);

			if (!extractBoundsOr(clause, boundMap, booleanMap)) {
				return false;
			}
		}
		return updateConstantMap();
	}

	private boolean updateConstantMap() {
		for (BoundsObject bounds : boundMap.values()) {
			NumberIF lower = bounds.lower();

			if (lower != null && lower.equals(bounds.upper)) {
				FactoredPolynomial expression = (FactoredPolynomial) bounds.expression;

				assert !bounds.strictLower && !bounds.strictUpper;
				constantMap.put(expression, lower);
			}
		}

		boolean satisfiable = LinearSolver.reduceConstantMap(this, constantMap);

		if (verbose()) {
			printBoundMap(out());
			printConstantMap(out());
			printBooleanMap(out());
		}

		return satisfiable;
	}

	public void printBoundMap(PrintStream out) {
		out.println("Bounds map:");
		for (BoundsObject boundObject : boundMap.values()) {
			out.println(boundObject);
		}
		out.println();
		out.flush();
	}

	public void printConstantMap(PrintStream out) {
		out.println("Constant map:");
		for (Entry<FactoredPolynomial, NumberIF> entry : constantMap.entrySet()) {
			out.print(entry.getKey() + " = ");
			out.println(entry.getValue());
		}
		out.println();
		out.flush();
	}

	public void printBooleanMap(PrintStream out) {
		out.println("Boolean map:");
		for (Entry<BooleanPrimitive, Boolean> entry : booleanMap.entrySet()) {
			out.print(entry.getKey() + " = ");
			out.println(entry.getValue());
		}
		out.println();
		out.flush();
	}

	private boolean extractBoundsOr(OrExpression or,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap) {
		int numClauses = or.numClauses();
		boolean satisfiable;

		if (numClauses == 0) {
			satisfiable = false;
		} else if (numClauses == 1) {
			satisfiable = extractBounds(or.clause(0), aBoundMap, aBooleanMap);
		} else {
			// p & (q0 | ... | qn) = (p & q0) | ... | (p & qn)
			// copies of original maps, corresponding to p. these never
			// change...
			Map<FactoredPolynomial, BoundsObject> originalBoundMap = new HashMap<FactoredPolynomial, BoundsObject>(
					aBoundMap);
			Map<BooleanPrimitive, Boolean> originalBooleanMap = new HashMap<BooleanPrimitive, Boolean>(
					aBooleanMap);

			// result <- p & q0:
			satisfiable = extractBounds(or.clause(0), aBoundMap, aBooleanMap);
			// result <- result | ((p & q1) | ... | (p & qn)) :
			for (int i = 1; i < numClauses; i++) {
				Map<FactoredPolynomial, BoundsObject> newBoundMap = new HashMap<FactoredPolynomial, BoundsObject>(
						originalBoundMap);
				Map<BooleanPrimitive, Boolean> newBooleanMap = new HashMap<BooleanPrimitive, Boolean>(
						originalBooleanMap);
				// compute p & q_i:
				boolean newSatisfiable = extractBounds(or.clause(i),
						newBoundMap, newBooleanMap);

				// result <- result | (p & q_i) where result is (aBoundMap,
				// aBooleanMap)....
				satisfiable = satisfiable || newSatisfiable;
				if (newSatisfiable) {
					LinkedList<BooleanPrimitive> removeList = new LinkedList<BooleanPrimitive>();

					for (Map.Entry<FactoredPolynomial, BoundsObject> entry : newBoundMap
							.entrySet()) {
						SymbolicExpressionIF primitive = entry.getKey();
						BoundsObject bound2 = entry.getValue();
						BoundsObject bound1 = aBoundMap.get(primitive);

						if (bound1 != null)
							bound1.enlargeTo(bound2);
					}
					for (Map.Entry<BooleanPrimitive, Boolean> entry : newBooleanMap
							.entrySet()) {
						BooleanPrimitive primitive = entry.getKey();
						Boolean newValue = entry.getValue();
						assert newValue != null;
						Boolean oldValue = aBooleanMap.get(primitive);

						if (oldValue != null && !oldValue.equals(newValue))
							removeList.add(primitive);
					}
					for (BooleanPrimitive primitive : removeList)
						aBooleanMap.remove(primitive);
				}
			}
		}
		return satisfiable;
	}

	/**
	 * A basic expression is either a LiteralExpression or QuantifierExpression
	 */
	private boolean extractBounds(BasicExpression basic,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap) {
		if (basic instanceof LiteralExpression) {
			LiteralExpression literal = (LiteralExpression) basic;
			boolean not = literal.not();
			BooleanPrimitive primitive = literal.primitive();
			Boolean value = aBooleanMap.get(primitive);

			if (value != null)
				return (not ? !value : value);
			aBooleanMap.put(primitive, !not);
			return true;
		} else if (basic instanceof QuantifierExpression) {
			// forall or exists: difficult
			// forall x: ()bounds: can substitute whatever you want for x
			// and extract bounds.
			// example: forall i: a[i]<7. Look for all occurrence of a[*]
			// and add bounds
			return true;
		} else if (basic instanceof RelationalExpression) {
			RelationalExpression relational = (RelationalExpression) basic;
			RelationKind kind = relational.relationKind();
			TreeExpressionIF tree = relational.expression();

			switch (kind) {
			case EQ0:
				return extractEQ0Bounds(false, (FactoredPolynomial) tree,
						aBoundMap, aBooleanMap);
			case NEQ0: {
				boolean result = extractEQ0Bounds(true,
						(FactoredPolynomial) tree, aBoundMap, aBooleanMap);

				return result;
			}
			case GT0:
				return extractGT0Bounds(true, tree, aBoundMap, aBooleanMap);
			case GTE0:
				return extractGT0Bounds(false, tree, aBoundMap, aBooleanMap);
			default:
				throw new RuntimeException("Unknown RelationKind: " + kind);
			}
		} else {
			throw new RuntimeException("Unknown type of BasicExpression: "
					+ basic);
		}
	}

	// TODO: go further and perform backwards substitution...

	private boolean extractEQ0Bounds(boolean not, FactoredPolynomial fp,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap) {
		if (not)
			return extractNEQ0Bounds(fp, aBoundMap, aBooleanMap);

		IntegerNumberIF degree = fp.degree();

		if (degree.signum() == 0)
			return fp.isZero();

		// this branch is here as a compromise. Gaussian elimination
		// takes a long time and most of the time it is only useful
		// for degree 1 polynomials.
		if (!linearizePolynomials
				&& numberFactory()
						.compare(degree, numberFactory().oneInteger()) > 0)
			return true;

		AffineExpression affine = affineFactory().affine(fp);
		FactoredPolynomial pseudo = affine.pseudo();
		RationalNumberIF coefficient = numberFactory().rational(
				affine.coefficient());
		RationalNumberIF offset = numberFactory().rational(affine.offset());
		RationalNumberIF rationalValue = numberFactory().negate(
				numberFactory().divide(offset, coefficient));
		NumberIF value;
		BoundsObject bound = aBoundMap.get(pseudo);

		if (pseudo.type().isInteger()) {
			if (numberFactory().isIntegral(rationalValue)) {
				value = numberFactory().integerValue(rationalValue);
			} else {
				return false;
			}
		} else {
			value = rationalValue;
		}
		if (bound == null) {
			bound = BoundsObject.newTightBound(pseudo, value);
			aBoundMap.put(pseudo, bound);
		} else {
			if ((bound.lower != null && numberFactory().compare(bound.lower,
					value) > 0)
					|| (bound.upper != null && numberFactory().compare(value,
							bound.upper) > 0))
				return false;
			bound.makeConstant(value);
		}
		return true;
	}

	private boolean extractNEQ0Bounds(FactoredPolynomial fp,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap) {
		return true;
	}

	/**
	 * Exracts bounds from expression of the form e>0 (strict true) or e>=0
	 * (strict false). Updates aBoundMap and aBooleanMap.
	 */
	private boolean extractGT0Bounds(boolean strict, TreeExpressionIF tree,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap) {
		if (tree instanceof FactoredPolynomial) {
			FactoredPolynomial fp = (FactoredPolynomial) tree;

			return extractGT0(fp, aBoundMap, aBooleanMap, strict);
		} else if (tree instanceof RationalExpression) {
			RationalExpression rational = (RationalExpression) tree;

			return extractGT0(rational, aBoundMap, aBooleanMap, strict);
		} else {
			throw new IllegalArgumentException(
					"Expected polynomial or rational expression: " + tree);
		}

	}

	/**
	 * Glean bounds on p and q given expression of the form p/q>0.
	 * */
	// what about a bound on p/q?
	private boolean extractGT0(RationalExpression rational,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap, boolean strict) {
		FactoredPolynomial numerator = rational.numerator();
		FactoredPolynomial denominator = rational.denominator();
		BoundsObject denominatorBounds = aBoundMap.get(denominator);

		if (denominatorBounds != null) {
			if (denominatorBounds.lower() != null
					&& denominatorBounds.lower().signum() >= 0)
				return extractGT0(numerator, aBoundMap, aBooleanMap, strict);
			else if (denominatorBounds.upper() != null
					&& denominatorBounds.upper().signum() <= 0)
				return extractGT0(fpFactory().negate(numerator), aBoundMap,
						aBooleanMap, strict);
		}

		BoundsObject numeratorBounds = aBoundMap.get(denominator);

		if (numeratorBounds != null) {
			NumberIF lower = numeratorBounds.lower();

			if (lower.signum() > 0 || lower.signum() == 0
					&& numeratorBounds.strictLower())
				return extractGT0(denominator, aBoundMap, aBooleanMap, strict);

			NumberIF upper = numeratorBounds.upper();

			if (upper.signum() < 0 || upper.signum() == 0
					&& numeratorBounds.strictUpper())
				return extractGT0(fpFactory().negate(denominator), aBoundMap,
						aBooleanMap, strict);
		}
		return true;
	}

	private boolean extractGT0(FactoredPolynomial fp,
			Map<FactoredPolynomial, BoundsObject> aBoundMap,
			Map<BooleanPrimitive, Boolean> aBooleanMap, boolean strict) {
		AffineExpression affine = affineFactory().affine(fp);
		FactoredPolynomial pseudo;

		if (affine == null)
			return true;
		pseudo = affine.pseudo();
		if (pseudo != null) {
			BoundsObject boundsObject = aBoundMap.get(pseudo);
			NumberIF coefficient = affine.coefficient();
			NumberIF bound = affineFactory().bound(affine, strict);

			if (pseudo.type().isInteger())
				strict = false;
			if (coefficient.signum() > 0) { // lower bound
				if (boundsObject == null) {
					boundsObject = BoundsObject.newLowerBound(pseudo, bound,
							strict);
					aBoundMap.put(pseudo, boundsObject);
				} else {
					boundsObject.restrictLower(bound, strict);
					return boundsObject.isConsistent();
				}
			} else { // upper bound
				if (boundsObject == null) {
					boundsObject = BoundsObject.newUpperBound(pseudo, bound,
							strict);
					aBoundMap.put(pseudo, boundsObject);
				} else {
					boundsObject.restrictUpper(bound, strict);
					return boundsObject.isConsistent();
				}
			}
			return true;
		}
		return (strict ? affine.offset().signum() > 0 : affine.offset()
				.signum() >= 0);
	}

	private void declareFact(TreeExpressionIF booleanExpression, boolean truth) {
		BooleanIdealExpression value = (truth ? trueExpression()
				: falseExpression());

		treeSimplifyMap.put(booleanExpression, new Simplification(
				booleanExpression, value, true));
	}

	/**
	 * This method inserts into the simplification cache all facts from the
	 * assumption that are not otherwised encoded in the constantMap,
	 * booleanMap, or boundMap. It is to be invoked only after the assumption
	 * has been simplified for the final time.
	 */
	private void extractRemainingFacts() {
		CnfBooleanExpression cnf = assumption.cnf();

		for (int i = 0; i < cnf.numClauses(); i++) {
			OrExpression or = cnf.clause(i);

			declareFact(or, true);

			int numBasics = or.numClauses();

			if (numBasics == 1) {
				BasicExpression basic = or.clause(0);

				if (basic instanceof RelationalExpression) {
					RelationalExpression relational = (RelationalExpression) basic;

					if (relational.relationKind() == RelationKind.NEQ0) {
						RelationalExpression eq0 = relationalFactory().negate(
								relational);

						declareFact(eq0, false);
					}
				} else if (basic instanceof QuantifierExpression) {
					declareFact(basic, true);
				}
			}
		}
	}

	@Override
	public IntervalIF assumptionAsInterval(SymbolicConstantIF symbolicConstant) {
		if (intervalComputed) {
			if (interval != null && intervalVariable.equals(symbolicConstant))
				return interval;
			return null;
		}
		intervalComputed = true;
		if (!booleanMap.isEmpty() || !trueExpression().equals(rawAssumption)) {
			return null;
		}
		if (!constantMap.isEmpty()) {
			if (!boundMap.isEmpty() || constantMap.size() != 1) {
				return null;
			}
			Entry<FactoredPolynomial, NumberIF> entry = constantMap.entrySet()
					.iterator().next();
			FactoredPolynomial fp1 = entry.getKey();
			FactoredPolynomial fp2 = fpFactory().factoredPolynomial(
					constantFactory().expression(symbolicConstant));
			NumberIF value = entry.getValue();

			if (!fp1.equals(fp2)) {
				return null;
			}
			interval = BoundsObject.newTightBound(fp2, value);
			intervalVariable = symbolicConstant;
			return interval;
		}
		if (boundMap.size() == 1) {
			Entry<FactoredPolynomial, BoundsObject> entry = boundMap.entrySet()
					.iterator().next();
			FactoredPolynomial fp1 = entry.getKey();
			FactoredPolynomial fp2 = fpFactory().factoredPolynomial(
					constantFactory().expression(symbolicConstant));
			if (!fp1.equals(fp2)) {
				return null;
			}
			interval = entry.getValue();
			intervalVariable = symbolicConstant;
			return interval;
		}
		return null;
	}
}
