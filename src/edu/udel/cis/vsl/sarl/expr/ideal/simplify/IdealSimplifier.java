package edu.udel.cis.vsl.sarl.expr.ideal.simplify;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.ideal.IdealExpression;
import edu.udel.cis.vsl.sarl.expr.ideal.IdealFactory;
import edu.udel.cis.vsl.sarl.expr.ideal.Monomial;
import edu.udel.cis.vsl.sarl.expr.ideal.NumericPrimitive;
import edu.udel.cis.vsl.sarl.expr.ideal.Polynomial;
import edu.udel.cis.vsl.sarl.expr.ideal.RationalExpression;
import edu.udel.cis.vsl.sarl.expr.ideal.ReducedPolynomial;
import edu.udel.cis.vsl.sarl.simplify.IF.CommonSimplifier;
import edu.udel.cis.vsl.sarl.simplify.common.Simplification;

/**
 * An implementation of SimplifierIF for the Ideal Universe. Provides methods to
 * take a symbolic expression from an ideal universe and return a "simplified"
 * version of the expression which is equivalent to the original in the
 * mathematical "ideal" semantics. Similar method is provided for types.
 * 
 * @author siegel
 * 
 */
public class IdealSimplifier extends CommonSimplifier {

	private IdealFactory factory;

	private NumberFactory numberFactory;

	private AffineFactory affineFactory;

	private PrintStream out;

	private boolean verbose;

	/**
	 * The current assumption underlying this simplifier. Initially this is the
	 * assumption specified at construction, but it can be simplified during
	 * construction. After construction completes, it does not change. Also, any
	 * symbolic constant that is determined to have a concrete value is removed
	 * from this assumption; the concrete value can be obtained from the
	 * constantMap or booleanMap.
	 */
	private SymbolicExpression assumption;

	/**
	 * This is the same as the assumption, but without the information from the
	 * boundMap, booleanMap, and constantMap thrown in.
	 */
	private SymbolicExpression rawAssumption;

	/**
	 * A map that assigns bounds to pseudo primitive factored polynomials.
	 */
	private Map<Polynomial, BoundsObject> boundMap = new HashMap<Polynomial, BoundsObject>();

	/**
	 * A map that assigns concrete boolean values to boolean primitive
	 * expressions.
	 */
	private Map<SymbolicExpression, Boolean> booleanMap = new HashMap<SymbolicExpression, Boolean>();

	/**
	 * The keys in this map are pseudo-primitive factored polynomials. See
	 * AffineExpression for the definition. The value is the constant value that
	 * has been determined to be the value of that pseudo.
	 */
	private Map<Polynomial, Number> constantMap = new HashMap<Polynomial, Number>();

	// TODO: also would like to map symbolic constants that can be solved
	// for in terms of earlier ones to expressions...

	private boolean intervalComputed = false;

	private Interval interval = null;

	private SymbolicConstant intervalVariable = null;

	/**
	 * Treat every polynomial as a linear combination of monomials, so Gaussian
	 * elimination is performed on all equalities, and not just degree 1
	 * equalities.
	 */
	private boolean linearizePolynomials = false;

	public IdealSimplifier(SymbolicUniverse universe, IdealFactory factory,
			SymbolicExpression assumption) {
		super(universe);
		this.factory = factory;
		this.assumption = assumption;
		//initialize();
	}

	/***********************************************************************
	 * Begin Simplification Routines...................................... *
	 ***********************************************************************/

	protected SymbolicExpression simplify(SymbolicExpression expression) {
		// special handling for:
		// symbolic constants (booleans, numeric, ...)
		// numeric expressions (polynomials, ...)
		// relational expressions (0<a, 0<=a, 0==a, 0!=a)
		if (expression instanceof Polynomial)
			return simplifyPolynomial((Polynomial)expression);
		

		return null;
	}

	/**
	 * Simplifies a factored polynomial. Result could be either Polynomial or
	 * RationalExpression.
	 * 
	 * 
	 * sub(P) { write P=aX+b, X pseudo-primitive factored poly if
	 * map.contains(X) return a*map(X)+b; if P has more than one term: loop over
	 * terms of P and call sub. if any simplify, return sum of result. if P has
	 * more than one factor: loop over factors of P and call sub. if any
	 * simplify, return product of result. return P }
	 */
	private Polynomial simplifyPolynomialWork(Polynomial fp) {
		AffineExpression affine = affineFactory.affine(fp);
		Polynomial pseudo = affine.pseudo();
		Number pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null)
			return factory.constant(pseudoValue);
		{
			SymbolicType type = fp.type();
			int numTerms = fp.termMap(factory).size();

			if (numTerms > 1) {
				Polynomial result = (Polynomial) simplifyGenericExpression(fp);

				if (result != fp)
					return result;
			}
			{
				Monomial f1 = fp.factorization(factory);

				if (f1.degree() > 1) {
					Monomial f2 = (Monomial) simplify(f1);

					if (f2 != f1)
						return f2.expand(factory);
				}
			}
		}
		return fp;
	}

	private Polynomial simplifyPolynomial(Polynomial polynomial) {
		Polynomial result = (Polynomial) simplifyMap.get(polynomial);

		if (result == null) {
			result = simplifyPolynomialWork(polynomial);
			simplifyMap.put(polynomial, result);
		}
		return result;
	}

	@Override
	public SymbolicExpression newAssumption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 4 relations: 0<, 0<=, 0==, 0!=
	
	// 0<p/q <=> (0<p && 0<q) || (0<-p && 0<-q)
	
	// 0<=p/q <=> (0<=p && 0<q) || (0<=-p && 0<-q)
	
	// 0==p/q <=> 0==p
	
	// 0!=p/q <=> 0!=p
	

//	private Simplification simplifyRelational(SymbolicExpression expression) {
//		Simplification simplification = treeSimplifyMap.get(expression);
//
//		if (simplification != null)
//			return simplification;
//
//		RelationKind kind = expression.relationKind();
//		SymbolicExpression lhs = expression.expression();
//		CnfBooleanExpression result = null;
//		Simplification lhsSimplification;
//
//		if (lhs instanceof Polynomial) {
//			lhsSimplification = simplifyPolynomial((Polynomial) lhs);
//		} else if (lhs instanceof RationalExpression) {
//			lhsSimplification = simplifyRational((RationalExpression) lhs);
//		} else {
//			throw new RuntimeException(
//					"Unexpected type of expression in relation: " + lhs);
//		}
//
//		RelationalExpression newExpression = expression;
//		boolean change = false;
//
//		if (lhsSimplification.success()) {
//			change = true;
//			lhs = lhsSimplification.result().expression();
//			if (lhs instanceof RationalExpression
//					&& (kind == RelationKind.NEQ0 || kind == RelationKind.EQ0)) {
//				lhs = ((RationalExpression) lhs).numerator();
//			}
//			newExpression = relationalFactory().relational(kind, lhs);
//		}
//
//		switch (kind) {
//		case EQ0: {
//			result = simplifyEQ0((Polynomial) lhs);
//			break;
//		}
//		case NEQ0: {
//			result = simplifyEQ0((Polynomial) lhs);
//			if (result != null)
//				result = cnfFactory().not(result);
//			break;
//		}
//		case GT0:
//		case GTE0: {
//			if (lhs instanceof Polynomial)
//				result = simplifyGT0((Polynomial) lhs,
//						(kind == RelationKind.GT0 ? true : false));
//			else if (lhs instanceof RationalExpression) {
//				simplification = simplifyGT0Rational(newExpression);
//				treeSimplifyMap.put(expression, simplification);
//				return simplification;
//			} else
//				throw new IllegalArgumentException(
//						"Expression in GT0 is neither polynomial nor rational function: "
//								+ lhs);
//			break;
//		}
//		default:
//			throw new IllegalArgumentException("Unknown relation kind: " + kind);
//		}
//		if (result != null)
//			change = true;
//
//		if (!change)
//			simplification = new Simplification(expression,
//					universe.canonicalizeTree(expression), false);
//		else if (result != null)
//			simplification = new Simplification(expression,
//					universe.booleanIdeal(result), true);
//		else
//			simplification = new Simplification(expression,
//					universe.canonicalizeTree(newExpression), true);
//		treeSimplifyMap.put(expression, simplification);
//		return simplification;
//	}
//
//	/**
//	 * Attempts to simplify the expression fp=0. Returns null if no
//	 * simplification is possible, else returns a CnfBoolean expression
//	 * equivalent to fp=0.
//	 * 
//	 * @param fp
//	 *            the factored polynomial
//	 * @return null or a CnfBoolean expression equivalent to fp=0
//	 */
//	private CnfBooleanExpression simplifyEQ0(Polynomial fp) {
//		if (fp.isConstant()) {
//			return (fp.constantTerm().signum() == 0 ? cnfTrue() : cnfFalse());
//		}
//
//		SymbolicType type = fp.type();
//		AffineExpression affine = affineFactory().affine(fp);
//
//		assert affine != null;
//
//		Polynomial pseudo = affine.pseudo();
//
//		assert pseudo != null;
//
//		Number pseudoValue = constantMap.get(pseudo);
//
//		if (pseudoValue != null) {
//			if (affineFactory().affineValue(affine, pseudoValue).isZero())
//				return cnfTrue();
//			else
//				return cnfFalse();
//		}
//
//		Number offset = affine.offset();
//		Number coefficient = affine.coefficient();
//
//		if (type.isInteger()) {
//			if (!numberFactory().mod(
//					(IntegerNumber) offset,
//					(IntegerNumber) numberFactory().abs(
//							(IntegerNumber) coefficient)).isZero()) {
//				return cnfFalse();
//			}
//		}
//		pseudoValue = numberFactory().negate(
//				numberFactory().divide(offset, coefficient));
//
//		BoundsObject oldBounds = boundMap.get(pseudo);
//
//		if (oldBounds == null)
//			return null;
//
//		// have bounds on X, now simplify aX+b=0.
//		// aX+b=0 => solve for X=-b/a (check int arith)
//		// is -b/a within the bounds? if not: return FALSE
//		// if yes: no simplification.
//
//		int leftSign, rightSign;
//
//		{
//			Number lower = oldBounds.lower();
//
//			if (lower == null)
//				leftSign = -1;
//			else
//				leftSign = numberFactory().subtract(lower, pseudoValue)
//						.signum();
//
//			Number upper = oldBounds.upper();
//
//			if (upper == null)
//				rightSign = 1;
//			else
//				rightSign = numberFactory().subtract(upper, pseudoValue)
//						.signum();
//		}
//
//		// if 0 is not in that interval, return FALSE
//
//		if (leftSign > 0 || (leftSign == 0 && oldBounds.strictLower()))
//			return cnfFalse();
//		if (rightSign < 0 || (rightSign == 0 && oldBounds.strictUpper()))
//			return cnfTrue();
//
//		return null;
//	}
//
//	/**
//	 * Attempts to simplify the expression fp>?0. Returns null if no
//	 * simplification is possible, else returns a CnfBoolean expression
//	 * equivalent to fp>?0. (Here >? represents either > or >=, depending on
//	 * value of strictInequality.)
//	 * 
//	 * @param fp
//	 *            the factored polynomial
//	 * @return null or a CnfBoolean expression equivalent to fp>0
//	 */
//	private CnfBooleanExpression simplifyGT0(Polynomial fp,
//			boolean strictInequality) {
//		if (fp.isConstant()) {
//			int signum = fp.constantTerm().signum();
//
//			if (strictInequality)
//				return (signum > 0 ? cnfTrue() : cnfFalse());
//			else
//				return (signum >= 0 ? cnfTrue() : cnfFalse());
//		}
//
//		SymbolicType type = fp.type();
//		AffineExpression affine = affineFactory().affine(fp);
//		Polynomial pseudo = affine.pseudo();
//		assert pseudo != null;
//		Number pseudoValue = constantMap.get(pseudo);
//
//		if (pseudoValue != null) {
//			int signum = affineFactory().affineValue(affine, pseudoValue)
//					.signum();
//
//			if (strictInequality)
//				return (signum > 0 ? cnfTrue() : cnfFalse());
//			else
//				return (signum >= 0 ? cnfTrue() : cnfFalse());
//		}
//
//		BoundsObject oldBounds = boundMap.get(pseudo);
//
//		if (oldBounds == null)
//			return null;
//
//		Number newBound = affineFactory().bound(affine, strictInequality);
//		assert newBound != null;
//		// bound on pseudo X, assuming fp=aX+b>?0.
//		// If a>0, it is a lower bound. If a<0 it is an upper bound.
//		// newBound may or may not be strict
//		Number coefficient = affine.coefficient();
//		assert coefficient.signum() != 0;
//		boolean strictBound = (type.isInteger() ? false : strictInequality);
//
//		int leftSign, rightSign;
//		{
//			Number lower = oldBounds.lower(), upper = oldBounds.upper();
//
//			if (lower == null)
//				leftSign = -1;
//			else
//				leftSign = numberFactory().subtract(lower, newBound).signum();
//			if (upper == null)
//				rightSign = 1;
//			else
//				rightSign = numberFactory().subtract(upper, newBound).signum();
//		}
//
//		if (coefficient.signum() > 0) {
//			// simplify X>newBound or X>=newBound knowing X is in
//			// [oldLowerBound,oldUpperBound]
//			// let X'=X-newBound.
//			// simplify X'>0 (or X'>=0) knowing X' is in [left,right]
//			// if left>0: true
//			// if left=0 && (strictleft || strict): true
//			// if right<0: false
//			// if right=0 && (strictright || strict): false
//			if (leftSign > 0
//					|| (leftSign == 0 && (!strictBound || oldBounds
//							.strictLower())))
//				return cnfTrue();
//			if (rightSign < 0
//					|| (rightSign == 0 && (strictBound || oldBounds
//							.strictUpper())))
//				return cnfFalse();
//			if (rightSign == 0 && !strictBound && !oldBounds.strictUpper()) {
//				// X'=0, where X'=X-newBound.
//				IdealExpression x = universe.ideal(pseudo);
//				BooleanIdealExpression eq = universe.equals(x,
//						universe.concreteExpression(newBound));
//
//				return (CnfBooleanExpression) eq.cnf();
//			}
//		} else {
//			// simplify X<newBound or X<=newBound knowing X is in
//			// [oldLowerBound,oldUpperBound]
//			// simplify X'<0 or X'<=0 knowning X' is in [left,right]
//			// if left>0: false
//			// if left=0 && (strict || strictleft): false
//			// if right<0: true
//			// if right=0 && (strictright || strict): true
//			if (leftSign > 0
//					|| (leftSign == 0 && (strictBound || oldBounds
//							.strictLower())))
//				return cnfFalse();
//			if (rightSign < 0
//					|| (rightSign == 0 && (!strictBound || oldBounds
//							.strictUpper())))
//				return cnfTrue();
//			if (leftSign == 0 && !strictBound && !oldBounds.strictLower()) {
//				// X'=0, where X'=X-newBound.
//				IdealExpression x = universe.ideal(pseudo);
//				BooleanIdealExpression eq = universe.equals(x,
//						universe.concreteExpression(newBound));
//
//				return (CnfBooleanExpression) eq.cnf();
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Attemps to simplify expression p/q>0. If you can determine q>0, result is
//	 * simplify(p>0). If you can determine q<0, result is simplify(-p>0). If you
//	 * can determine p>0, result is simplify(q>0). If you can determine p<0,
//	 * result is simplify(-q>0). If you can determine p=0, result is false.
//	 */
//	private Simplification simplifyGT0Rational(RelationalExpression relational) {
//		RelationKind kind = relational.relationKind();
//
//		assert kind == RelationKind.GT0 || kind == RelationKind.GTE0;
//		assert relational.expression() instanceof RationalExpression;
//
//		Simplification rationalSimplification = simplifyRational((RationalExpression) relational
//				.expression());
//		RationalExpression rational = (RationalExpression) rationalSimplification
//				.result().expression();
//		Polynomial numerator = rational.numerator();
//		Polynomial denominator = rational.denominator();
//
//		if (denominator.isConstant()) {
//			Number denominatorNumber = denominator.constantTerm();
//			int signum = denominatorNumber.signum();
//
//			if (signum == 0) {
//				throw new IllegalArgumentException("Denominator is 0: "
//						+ relational);
//			} else if (signum > 0) { // denominator is positive
//				Simplification numeratorGT0Simplification = simplifyRelational(relationalFactory()
//						.relational(kind, numerator));
//
//				return new Simplification(relational,
//						numeratorGT0Simplification.result(), true);
//			} else { // denominator is negative
//				Simplification numeratorLT0Simplification = simplifyRelational(relationalFactory()
//						.relational(kind, fpFactory().negate(numerator)));
//
//				return new Simplification(relational,
//						numeratorLT0Simplification.result(), true);
//			}
//		}
//
//		boolean DO_RATIONAL_RELATIONAL_OPTIMIZATION = false;
//
//		// this optimization can mess up CVC3's ability sometimes....
//		if (DO_RATIONAL_RELATIONAL_OPTIMIZATION) {
//
//			Simplification denominatorGT0Simplification = simplifyRelational(relationalFactory()
//					.relational(RelationKind.GT0, denominator));
//			Simplification numeratorRelationSimplification = simplifyRelational(relationalFactory()
//					.relational(kind, numerator));
//
//			if (denominatorGT0Simplification.success()) {
//				IdealExpression denominatorGT0Result = denominatorGT0Simplification
//						.result();
//
//				if (denominatorGT0Result.equals(trueExpression())) {
//					return new Simplification(relational,
//							numeratorRelationSimplification.result(), true);
//				} else if (denominatorGT0Result.equals(falseExpression())) {
//					Simplification numeratorLT0Simplification = simplifyRelational(relationalFactory()
//							.relational(kind, fpFactory().negate(numerator)));
//
//					return new Simplification(relational,
//							numeratorLT0Simplification.result(), true);
//				}
//			}
//
//			if (numeratorRelationSimplification.success()
//					&& numeratorRelationSimplification.result().equals(
//							trueExpression()))
//				return new Simplification(relational,
//						denominatorGT0Simplification.result(), true);
//
//			Simplification numeratorNegRelationSimplification = simplifyRelational(relationalFactory()
//					.relational(kind, fpFactory().negate(numerator)));
//
//			if (numeratorNegRelationSimplification.success()
//					&& numeratorNegRelationSimplification.result().equals(
//							trueExpression()))
//				return new Simplification(relational, simplifyRelational(
//						relationalFactory().relational(RelationKind.GT0,
//								fpFactory().negate(denominator))).result(),
//						true);
//		}
//
//		Simplification numeratorEQ0Simplification = simplifyRelational(relationalFactory()
//				.relational(RelationKind.EQ0, numerator));
//
//		if (numeratorEQ0Simplification.result().equals(trueExpression()))
//			return new Simplification(relational,
//					(kind == RelationKind.GTE0 ? trueExpression()
//							: falseExpression()), true);
//		if (rationalSimplification.success())
//			return new Simplification(relational,
//					universe.canonicalizeTree(relationalFactory().relational(
//							kind, rational)), true);
//		return new Simplification(relational,
//				universe.canonicalizeTree(relational), false);
//	}
//
//	/***********************************************************************
//	 * End of Simplification Routines..................................... *
//	 ***********************************************************************/
//
//	public BooleanIdealExpression newAssumption() {
//		return assumption;
//	}
//
//	/**
//	 * Converts the bound to a boolean expression in canoncial form. Returns
//	 * null if both upper and lower bound are infinite (equivalent to "true").
//	 */
//	private IdealExpression boundToIdeal(BoundsObject bound) {
//		Number lower = bound.lower(), upper = bound.upper();
//		IdealExpression result = null;
//		Polynomial fp = (Polynomial) bound.expression;
//		IdealExpression ideal = simplifyPolynomial(fp).result();
//
//		if (lower != null) {
//			if (bound.strictLower())
//				result = universe.lessThan(universe.concreteExpression(lower),
//						ideal);
//			else
//				result = universe.lessThanEquals(
//						universe.concreteExpression(lower), ideal);
//		}
//		if (upper != null) {
//			IdealExpression upperResult;
//
//			if (bound.strictUpper())
//				upperResult = universe.lessThan(ideal,
//						universe.concreteExpression(upper));
//			else
//				upperResult = universe.lessThanEquals(ideal,
//						universe.concreteExpression(upper));
//			if (result == null)
//				result = upperResult;
//			else
//				result = universe.and(result, upperResult);
//		}
//		return result;
//	}
//
//	private void initialize() {
//		while (true) {
//			boundMap.clear();
//			treeSimplifyMap.clear();
//
//			boolean satisfiable = extractBounds();
//
//			if (!satisfiable) {
//				if (verbose) {
//					out.println("Path condition is unsatisfiable.");
//					out.flush();
//				}
//				assumption = falseExpression();
//				return;
//			} else {
//				// need to substitute into assumption new value of symbolic
//				// constants.
//				BooleanIdealExpression newAssumption = (BooleanIdealExpression) simplify(assumption);
//
//				rawAssumption = newAssumption;
//				for (BoundsObject bound : boundMap.values()) {
//					IdealExpression constraint = boundToIdeal(bound);
//
//					if (constraint != null)
//						newAssumption = universe.and(newAssumption, constraint);
//				}
//				// also need to add facts from constant map.
//				// but can eliminate any constant values for primitives since
//				// those will never occur in the state.
//
//				for (Entry<Polynomial, Number> entry : constantMap.entrySet()) {
//					Polynomial fp = entry.getKey();
//					NumericPrimitive primitive = fp.polynomial()
//							.extractPrimitive();
//
//					if (primitive != null
//							&& primitive.numericPrimitiveKind() == NumericPrimitiveKind.SYMBOLIC_CONSTANT) {
//						// symbolic constant: will be entirely eliminated
//					} else {
//						IdealExpression constraint = universe.equals(
//								universe.canonicalize(fp),
//								universe.concreteExpression(entry.getValue()));
//
//						newAssumption = universe.and(newAssumption, constraint);
//					}
//				}
//
//				for (Entry<BooleanPrimitive, Boolean> entry : booleanMap
//						.entrySet()) {
//					BooleanPrimitive primitive = entry.getKey();
//
//					if (primitive != null
//							&& primitive.booleanPrimitiveKind() == BooleanPrimitiveKind.SYMBOLIC_CONSTANT) {
//						// symbolic constant: will be entirely eliminated
//					} else {
//						IdealExpression constraint = universe
//								.canonicalizeTree(primitive);
//
//						if (!entry.getValue())
//							constraint = universe.not(constraint);
//						newAssumption = universe.and(newAssumption, constraint);
//					}
//				}
//
//				if (assumption.equals(newAssumption))
//					break;
//				assumption = newAssumption;
//			}
//		}
//		extractRemainingFacts();
//	}
//
//	/**
//	 * Attempts to determine bounds (upper and lower) on primitive expressions
//	 * by examining the assumption. Returns false if ideal is determined to be
//	 * unsatisfiable.
//	 */
//	private boolean extractBounds() {
//		CnfBooleanExpression cnf = assumption.cnf();
//		int numClauses = cnf.numClauses();
//
//		for (int i = 0; i < numClauses; i++) {
//			OrExpression clause = cnf.clause(i);
//
//			if (!extractBoundsOr(clause, boundMap, booleanMap)) {
//				return false;
//			}
//		}
//		return updateConstantMap();
//	}
//
//	private boolean updateConstantMap() {
//		for (BoundsObject bounds : boundMap.values()) {
//			Number lower = bounds.lower();
//
//			if (lower != null && lower.equals(bounds.upper)) {
//				Polynomial expression = (Polynomial) bounds.expression;
//
//				assert !bounds.strictLower && !bounds.strictUpper;
//				constantMap.put(expression, lower);
//			}
//		}
//
//		boolean satisfiable = LinearSolver.reduceConstantMap(this, constantMap);
//
//		if (verbose) {
//			printBoundMap(out);
//			printConstantMap(out);
//			printBooleanMap(out);
//		}
//
//		return satisfiable;
//	}
//
//	public void printBoundMap(PrintWriter out) {
//		out.println("Bounds map:");
//		for (BoundsObject boundObject : boundMap.values()) {
//			out.println(boundObject);
//		}
//		out.println();
//		out.flush();
//	}
//
//	public void printConstantMap(PrintWriter out) {
//		out.println("Constant map:");
//		for (Entry<Polynomial, Number> entry : constantMap.entrySet()) {
//			out.print(entry.getKey() + " = ");
//			out.println(entry.getValue());
//		}
//		out.println();
//		out.flush();
//	}
//
//	public void printBooleanMap(PrintWriter out) {
//		out.println("Boolean map:");
//		for (Entry<BooleanPrimitive, Boolean> entry : booleanMap.entrySet()) {
//			out.print(entry.getKey() + " = ");
//			out.println(entry.getValue());
//		}
//		out.println();
//		out.flush();
//	}
//
//	private boolean extractBoundsOr(OrExpression or,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap) {
//		int numClauses = or.numClauses();
//		boolean satisfiable;
//
//		if (numClauses == 0) {
//			satisfiable = false;
//		} else if (numClauses == 1) {
//			satisfiable = extractBounds(or.clause(0), aBoundMap, aBooleanMap);
//		} else {
//			// p & (q0 | ... | qn) = (p & q0) | ... | (p & qn)
//			// copies of original maps, corresponding to p. these never
//			// change...
//			Map<Polynomial, BoundsObject> originalBoundMap = new HashMap<Polynomial, BoundsObject>(
//					aBoundMap);
//			Map<BooleanPrimitive, Boolean> originalBooleanMap = new HashMap<BooleanPrimitive, Boolean>(
//					aBooleanMap);
//
//			// result <- p & q0:
//			satisfiable = extractBounds(or.clause(0), aBoundMap, aBooleanMap);
//			// result <- result | ((p & q1) | ... | (p & qn)) :
//			for (int i = 1; i < numClauses; i++) {
//				Map<Polynomial, BoundsObject> newBoundMap = new HashMap<Polynomial, BoundsObject>(
//						originalBoundMap);
//				Map<BooleanPrimitive, Boolean> newBooleanMap = new HashMap<BooleanPrimitive, Boolean>(
//						originalBooleanMap);
//				// compute p & q_i:
//				boolean newSatisfiable = extractBounds(or.clause(i),
//						newBoundMap, newBooleanMap);
//
//				// result <- result | (p & q_i) where result is (aBoundMap,
//				// aBooleanMap)....
//				satisfiable = satisfiable || newSatisfiable;
//				if (newSatisfiable) {
//					LinkedList<BooleanPrimitive> removeList = new LinkedList<BooleanPrimitive>();
//
//					for (Map.Entry<Polynomial, BoundsObject> entry : newBoundMap
//							.entrySet()) {
//						SymbolicExpression primitive = entry.getKey();
//						BoundsObject bound2 = entry.getValue();
//						BoundsObject bound1 = aBoundMap.get(primitive);
//
//						if (bound1 != null)
//							bound1.enlargeTo(bound2);
//					}
//					for (Map.Entry<BooleanPrimitive, Boolean> entry : newBooleanMap
//							.entrySet()) {
//						BooleanPrimitive primitive = entry.getKey();
//						Boolean newValue = entry.getValue();
//						assert newValue != null;
//						Boolean oldValue = aBooleanMap.get(primitive);
//
//						if (oldValue != null && !oldValue.equals(newValue))
//							removeList.add(primitive);
//					}
//					for (BooleanPrimitive primitive : removeList)
//						aBooleanMap.remove(primitive);
//				}
//			}
//		}
//		return satisfiable;
//	}
//
//	/**
//	 * A basic expression is either a LiteralExpression or QuantifierExpression
//	 */
//	private boolean extractBounds(BasicExpression basic,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap) {
//		if (basic instanceof LiteralExpression) {
//			LiteralExpression literal = (LiteralExpression) basic;
//			boolean not = literal.not();
//			BooleanPrimitive primitive = literal.primitive();
//			Boolean value = aBooleanMap.get(primitive);
//
//			if (value != null)
//				return (not ? !value : value);
//			aBooleanMap.put(primitive, !not);
//			return true;
//		} else if (basic instanceof QuantifierExpression) {
//			// forall or exists: difficult
//			// forall x: ()bounds: can substitute whatever you want for x
//			// and extract bounds.
//			// example: forall i: a[i]<7. Look for all occurrence of a[*]
//			// and add bounds
//			return true;
//		} else if (basic instanceof RelationalExpression) {
//			RelationalExpression relational = (RelationalExpression) basic;
//			RelationKind kind = relational.relationKind();
//			SymbolicExpression tree = relational.expression();
//
//			switch (kind) {
//			case EQ0:
//				return extractEQ0Bounds(false, (Polynomial) tree, aBoundMap,
//						aBooleanMap);
//			case NEQ0: {
//				boolean result = extractEQ0Bounds(true, (Polynomial) tree,
//						aBoundMap, aBooleanMap);
//
//				return result;
//			}
//			case GT0:
//				return extractGT0Bounds(true, tree, aBoundMap, aBooleanMap);
//			case GTE0:
//				return extractGT0Bounds(false, tree, aBoundMap, aBooleanMap);
//			default:
//				throw new RuntimeException("Unknown RelationKind: " + kind);
//			}
//		} else {
//			throw new RuntimeException("Unknown type of BasicExpression: "
//					+ basic);
//		}
//	}
//
//	// TODO: go further and perform backwards substitution...
//
//	private boolean extractEQ0Bounds(boolean not, Polynomial fp,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap) {
//		if (not)
//			return extractNEQ0Bounds(fp, aBoundMap, aBooleanMap);
//
//		IntegerNumber degree = fp.degree();
//
//		if (degree.signum() == 0)
//			return fp.isZero();
//
//		// this branch is here as a compromise. Gaussian elimination
//		// takes a long time and most of the time it is only useful
//		// for degree 1 polynomials.
//		if (!linearizePolynomials
//				&& numberFactory()
//						.compare(degree, numberFactory().oneInteger()) > 0)
//			return true;
//
//		AffineExpression affine = affineFactory().affine(fp);
//		Polynomial pseudo = affine.pseudo();
//		RationalNumber coefficient = numberFactory().rational(
//				affine.coefficient());
//		RationalNumber offset = numberFactory().rational(affine.offset());
//		RationalNumber rationalValue = numberFactory().negate(
//				numberFactory().divide(offset, coefficient));
//		Number value;
//		BoundsObject bound = aBoundMap.get(pseudo);
//
//		if (pseudo.type().isInteger()) {
//			if (numberFactory().isIntegral(rationalValue)) {
//				value = numberFactory().integerValue(rationalValue);
//			} else {
//				return false;
//			}
//		} else {
//			value = rationalValue;
//		}
//		if (bound == null) {
//			bound = BoundsObject.newTightBound(pseudo, value);
//			aBoundMap.put(pseudo, bound);
//		} else {
//			if ((bound.lower != null && numberFactory().compare(bound.lower,
//					value) > 0)
//					|| (bound.upper != null && numberFactory().compare(value,
//							bound.upper) > 0))
//				return false;
//			bound.makeConstant(value);
//		}
//		return true;
//	}
//
//	private boolean extractNEQ0Bounds(Polynomial fp,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap) {
//		return true;
//	}
//
//	/**
//	 * Exracts bounds from expression of the form e>0 (strict true) or e>=0
//	 * (strict false). Updates aBoundMap and aBooleanMap.
//	 */
//	private boolean extractGT0Bounds(boolean strict, SymbolicExpression tree,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap) {
//		if (tree instanceof Polynomial) {
//			Polynomial fp = (Polynomial) tree;
//
//			return extractGT0(fp, aBoundMap, aBooleanMap, strict);
//		} else if (tree instanceof RationalExpression) {
//			RationalExpression rational = (RationalExpression) tree;
//
//			return extractGT0(rational, aBoundMap, aBooleanMap, strict);
//		} else {
//			throw new IllegalArgumentException(
//					"Expected polynomial or rational expression: " + tree);
//		}
//
//	}
//
//	/**
//	 * Glean bounds on p and q given expression of the form p/q>0.
//	 * */
//	// what about a bound on p/q?
//	private boolean extractGT0(RationalExpression rational,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap, boolean strict) {
//		Polynomial numerator = rational.numerator();
//		Polynomial denominator = rational.denominator();
//		BoundsObject denominatorBounds = aBoundMap.get(denominator);
//
//		if (denominatorBounds != null) {
//			if (denominatorBounds.lower() != null
//					&& denominatorBounds.lower().signum() >= 0)
//				return extractGT0(numerator, aBoundMap, aBooleanMap, strict);
//			else if (denominatorBounds.upper() != null
//					&& denominatorBounds.upper().signum() <= 0)
//				return extractGT0(fpFactory().negate(numerator), aBoundMap,
//						aBooleanMap, strict);
//		}
//
//		BoundsObject numeratorBounds = aBoundMap.get(denominator);
//
//		if (numeratorBounds != null) {
//			Number lower = numeratorBounds.lower();
//
//			if (lower.signum() > 0 || lower.signum() == 0
//					&& numeratorBounds.strictLower())
//				return extractGT0(denominator, aBoundMap, aBooleanMap, strict);
//
//			Number upper = numeratorBounds.upper();
//
//			if (upper.signum() < 0 || upper.signum() == 0
//					&& numeratorBounds.strictUpper())
//				return extractGT0(fpFactory().negate(denominator), aBoundMap,
//						aBooleanMap, strict);
//		}
//		return true;
//	}
//
//	private boolean extractGT0(Polynomial fp,
//			Map<Polynomial, BoundsObject> aBoundMap,
//			Map<BooleanPrimitive, Boolean> aBooleanMap, boolean strict) {
//		AffineExpression affine = affineFactory().affine(fp);
//		Polynomial pseudo;
//
//		if (affine == null)
//			return true;
//		pseudo = affine.pseudo();
//		if (pseudo != null) {
//			BoundsObject boundsObject = aBoundMap.get(pseudo);
//			Number coefficient = affine.coefficient();
//			Number bound = affineFactory().bound(affine, strict);
//
//			if (pseudo.type().isInteger())
//				strict = false;
//			if (coefficient.signum() > 0) { // lower bound
//				if (boundsObject == null) {
//					boundsObject = BoundsObject.newLowerBound(pseudo, bound,
//							strict);
//					aBoundMap.put(pseudo, boundsObject);
//				} else {
//					boundsObject.restrictLower(bound, strict);
//					return boundsObject.isConsistent();
//				}
//			} else { // upper bound
//				if (boundsObject == null) {
//					boundsObject = BoundsObject.newUpperBound(pseudo, bound,
//							strict);
//					aBoundMap.put(pseudo, boundsObject);
//				} else {
//					boundsObject.restrictUpper(bound, strict);
//					return boundsObject.isConsistent();
//				}
//			}
//			return true;
//		}
//		return (strict ? affine.offset().signum() > 0 : affine.offset()
//				.signum() >= 0);
//	}
//
//	private void declareFact(SymbolicExpression booleanExpression, boolean truth) {
//		BooleanIdealExpression value = (truth ? trueExpression()
//				: falseExpression());
//
//		treeSimplifyMap.put(booleanExpression, new Simplification(
//				booleanExpression, value, true));
//	}
//
//	/**
//	 * This method inserts into the simplification cache all facts from the
//	 * assumption that are not otherwised encoded in the constantMap,
//	 * booleanMap, or boundMap. It is to be invoked only after the assumption
//	 * has been simplified for the final time.
//	 */
//	private void extractRemainingFacts() {
//		CnfBooleanExpression cnf = assumption.cnf();
//
//		for (int i = 0; i < cnf.numClauses(); i++) {
//			OrExpression or = cnf.clause(i);
//
//			declareFact(or, true);
//
//			int numBasics = or.numClauses();
//
//			if (numBasics == 1) {
//				BasicExpression basic = or.clause(0);
//
//				if (basic instanceof RelationalExpression) {
//					RelationalExpression relational = (RelationalExpression) basic;
//
//					if (relational.relationKind() == RelationKind.NEQ0) {
//						RelationalExpression eq0 = relationalFactory().negate(
//								relational);
//
//						declareFact(eq0, false);
//					}
//				} else if (basic instanceof QuantifierExpression) {
//					declareFact(basic, true);
//				}
//			}
//		}
//	}
//
//	@Override
//	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
//		if (intervalComputed) {
//			if (interval != null && intervalVariable.equals(symbolicConstant))
//				return interval;
//			return null;
//		}
//		intervalComputed = true;
//		if (!booleanMap.isEmpty() || !trueExpression().equals(rawAssumption)) {
//			return null;
//		}
//		if (!constantMap.isEmpty()) {
//			if (!boundMap.isEmpty() || constantMap.size() != 1) {
//				return null;
//			}
//			Entry<Polynomial, Number> entry = constantMap.entrySet().iterator()
//					.next();
//			Polynomial fp1 = entry.getKey();
//			Polynomial fp2 = fpFactory().factoredPolynomial(
//					constantFactory().expression(symbolicConstant));
//			Number value = entry.getValue();
//
//			if (!fp1.equals(fp2)) {
//				return null;
//			}
//			interval = BoundsObject.newTightBound(fp2, value);
//			intervalVariable = symbolicConstant;
//			return interval;
//		}
//		if (boundMap.size() == 1) {
//			Entry<Polynomial, BoundsObject> entry = boundMap.entrySet()
//					.iterator().next();
//			Polynomial fp1 = entry.getKey();
//			Polynomial fp2 = fpFactory().factoredPolynomial(
//					constantFactory().expression(symbolicConstant));
//			if (!fp1.equals(fp2)) {
//				return null;
//			}
//			interval = entry.getValue();
//			intervalVariable = symbolicConstant;
//			return interval;
//		}
//		return null;
//	}
}
