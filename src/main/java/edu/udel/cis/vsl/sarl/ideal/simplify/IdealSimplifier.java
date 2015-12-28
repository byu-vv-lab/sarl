/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.common.NTPolynomial;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.common.CommonSimplifier;

/**
 * An implementation of {@link Simplifier} for the "Ideal" (mathematical)
 * Universe. Provides methods to take a symbolic expression from an ideal
 * universe and return a "simplified" version of the expression which is
 * equivalent to the original in the mathematical "ideal" semantics. Similar
 * method is provided for types.
 * 
 * @author siegel
 * 
 */
// TODO: also would like to map symbolic constants that can be solved
// for in terms of earlier ones to expressions...
public class IdealSimplifier extends CommonSimplifier {

	private final static boolean debug = false;

	/**
	 * Object that gathers together references to various objects needed for
	 * simplification.
	 */
	private SimplifierInfo info;

	/**
	 * The current assumption underlying this simplifier. Initially this is the
	 * assumption specified at construction, but it can be simplified during
	 * construction. After construction completes, it does not change. It does
	 * not include the symbolic constants occurring in the substitutionMap.
	 */
	private BooleanExpression assumption;

	/**
	 * This is the same as the assumption, but without the information from the
	 * boundMap, booleanMap, and constantMap thrown in.
	 */
	private BooleanExpression rawAssumption;

	/**
	 * Map from symbolic constants to their "solved" values. These symbolic
	 * constants will be replaced by their corresponding values in all
	 * expressions simplified by this simplifier.
	 */
	private Map<SymbolicConstant, SymbolicExpression> substitutionMap = null;

	/**
	 * A simplified version of the context, including the substitutions.
	 */
	private BooleanExpression fullContext = null;

	/**
	 * A map that assigns bounds to pseudo primitive factored polynomials.
	 */
	private Map<Polynomial, BoundsObject> boundMap = new HashMap<Polynomial, BoundsObject>();

	/**
	 * A map that assigns concrete boolean values to boolean primitive
	 * expressions.
	 */
	private Map<BooleanExpression, Boolean> booleanMap = new HashMap<BooleanExpression, Boolean>();

	/**
	 * The keys in this map are pseudo-primitive factored polynomials. See
	 * {@link AffineExpression} for the definition. The value is the constant
	 * value that has been determined to be the value of that pseudo.
	 */
	private Map<Polynomial, Number> constantMap = new HashMap<Polynomial, Number>();

	// TODO: if it is determined that p=0, then all primitives occurring
	// in the factorization of of p are 0:
	// put all of these into the constantMap. If it is determined that
	// p!=0, then all primitives occurring in a factorization of p are non-0.
	// Put these facts into the booleanMap.

	/**
	 * Has the interval interpretation of this context been computed?
	 */
	private boolean intervalComputed = false;

	/**
	 * The interpretation of the context as an Interval, or <code>null</code> if
	 * it cannot be so interpreted.
	 */
	private Interval interval = null;

	/**
	 * The variable bound by the interval.
	 */
	private SymbolicConstant intervalVariable = null;

	public IdealSimplifier(SimplifierInfo info, BooleanExpression assumption) {
		super(info.universe);
		// need to decide if this should be a precondition, or if
		// it should be made canonic, or just to not care
		// assert assumption.isCanonic();
		this.info = info;
		this.assumption = assumption;
		initialize();
	}

	/***********************************************************************
	 * Begin Simplification Routines...................................... *
	 ***********************************************************************/

	/**
	 * Determines if the operator is one of the relation operators
	 * {@link SymbolicOperator#LESS_THAN},
	 * {@link SymbolicOperator#LESS_THAN_EQUALS},
	 * {@link SymbolicOperator#EQUALS}, or {@link SymbolicOperator#NEQ}.
	 * 
	 * @param operator
	 *            a non-<code>null</code> symbolic operator
	 * @return <code>true</code> iff <code>operator</code> is one of the four
	 *         relationals
	 */
	private boolean isRelational(SymbolicOperator operator) {
		switch (operator) {
		case LESS_THAN:
		case LESS_THAN_EQUALS:
		case EQUALS:
		case NEQ:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Determines whether the expression is a numeric relational expression,
	 * i.e., the operator is one of the four relation operators and argument 0
	 * has numeric type.
	 * 
	 * @param expression
	 *            any non-<code>null</code> symbolic expression
	 * @return <code>true</code> iff the expression is relational with numeric
	 *         arguments
	 */
	private boolean isNumericRelational(SymbolicExpression expression) {
		return isRelational(expression.operator())
				&& ((SymbolicExpression) expression.argument(0)).isNumeric();
	}

	/**
	 * Attempts to determine the value of a polynomial using the information in
	 * the {@link #constantMap}. The polynomial is converted to affine form
	 * aX+b, where X is pseudo-primitive, and the constant map is used to
	 * determine a value for X, which can then be used to determine a value for
	 * the polynomial. If the constant map does not have a value for X, returns
	 * <code>null</code>.
	 * 
	 * @param polynomial
	 *            any non-<code>null</code> polynomial
	 * @return either a constant value for that polynomial or <code>null</code>
	 *         if no constant value can be determined
	 */
	private Constant pseudoReplacement(Polynomial polynomial) {
		AffineExpression affine = info.affineFactory.affine(polynomial);
		Polynomial pseudo = affine.pseudo();
		Number pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null)
			return info.idealFactory.constant(info.affineFactory.affineValue(
					affine, pseudoValue));
		else
			return null;
	}

	/**
	 * <p>
	 * Simplifies a polynomial. Assumes the polynomial is not currently in the
	 * simplification cache. Does NOT assume that the generic simplification has
	 * been performed on this polynomial.
	 * </p>
	 * 
	 * <p>
	 * Have to be careful here: some polynomials (instances of
	 * {@link NTPolynomial}) contain extrinsic data, namely, the factorization.
	 * So you can't just apply generic simplification to them or they will lose
	 * that extrinsic data.
	 * </p>
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> polynomial which does not have an
	 *            entry in the simplification cache
	 * @return simplified version of the polynomial
	 */
	private Polynomial simplifyPolynomial(Polynomial polynomial) {
		Polynomial result = pseudoReplacement(polynomial);

		if (result != null)
			return result;
		if (polynomial instanceof Monomial) {
			return (Polynomial) simplifyGenericExpression(polynomial);
		} else {
			Monomial f1 = polynomial.factorization(info.idealFactory);

			if (f1.degree() > 1) { // nontrivial factorization
				result = (Polynomial) simplifyGenericExpression(f1);

				if (result != f1) {
					if (result instanceof Monomial) {
						result = ((Monomial) result).expand(info.idealFactory);
					}
					if (result.degree() < polynomial.degree())
						return result;
				}
			}
			result = (Polynomial) simplifyGenericExpression(polynomial);
			return result;
		}
	}

	/**
	 * Simplifies a relational expression. The ideal factory ensures that every
	 * relational expression will be in one of the following four forms: 0&ltx;,
	 * 0&lt;=x, 0==x, 0!=x.
	 * 
	 * These are simplified according to the following rules:
	 * 
	 * <ul>
	 * <li>
	 * 0&lt;p/q &lt;=&gt; (0&lt;p &amp;&amp; 0&lt;q) || (0&lt;-p &amp;&amp;
	 * 0&lt;-q)</li>
	 * <li>
	 * 0&lt;=p/q &lt;=&gt; (0&lt;=p &amp;&amp; 0&lt;q) || (0&lt;=-p &amp;&amp;
	 * 0&lt;-q)</li>
	 * <li>
	 * 0==p/q &lt;=&gt; 0==p</li>
	 * <li>
	 * 0!=p/q &lt;=&gt; 0!=p</li>
	 * </ul>
	 * 
	 * @param expression
	 *            boolean-valued binary expression of one of the four forms
	 *            described above; in particular argument 0 must be 0
	 * @return the simplified version of the expression, which may be the
	 *         original expression if no further simplifications were possible
	 */
	private BooleanExpression simplifyRelationalWork(
			BooleanExpression expression) {
		SymbolicOperator operator = expression.operator();
		Polynomial poly = (Polynomial) expression.argument(1);
		BooleanExpression result;

		assert ((SymbolicExpression) expression.argument(0)).isZero();
		switch (operator) {
		case LESS_THAN:
		case LESS_THAN_EQUALS:
			result = simplifyGT0(poly, operator == SymbolicOperator.LESS_THAN);
			return result == null ? expression : result;
		case EQUALS:
			result = simplifyEQ0(poly);
			return result == null ? expression : result;
		case NEQ: {
			BooleanExpression negExpression = universe.not(expression);

			result = (BooleanExpression) simplifyExpression(negExpression);
			result = universe.not(result);
			return result;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * Simplifies a relational expression. Assumes expression does not already
	 * exist in simplification cache. Does NOT assume that generic
	 * simplification has been performed on expression.
	 * 
	 * @param expression
	 *            any boolean expression
	 * @return simplified version of the expression, which may be the original
	 *         expression
	 */
	private BooleanExpression simplifyRelational(BooleanExpression expression) {
		BooleanExpression result1 = (BooleanExpression) simplifyGenericExpression(expression);
		BooleanExpression result2 = null;

		if (result1 != expression) {
			result2 = (BooleanExpression) getCachedSimplification(result1);
			if (result2 == null) {
				if (result1.operator() == SymbolicOperator.CONCRETE)
					result2 = result1;
			}
		}
		if (result2 == null)
			result2 = simplifyRelationalWork(result1);
		return result2;
	}

	/**
	 * Attempts to simplify the expression poly=0. Returns <code>null</code> if
	 * no simplification is possible, else returns a {@link BooleanExpression}
	 * equivalent to poly=0.
	 * 
	 * @param poly
	 *            a non-<code>null</code>, non-constant {@link Polynomial}
	 * @return <code>null</code> or a {@link BooleanExpression} equivalent to
	 *         poly=0
	 */
	private BooleanExpression simplifyEQ0(Polynomial poly) {
		SymbolicType type = poly.type();
		AffineExpression affine = info.affineFactory.affine(poly);
		Polynomial pseudo = affine.pseudo(); // non-null since zep non-constant
		Number pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null)
			// substitute known constant value for pseudo...
			return info.affineFactory.affineValue(affine, pseudoValue).isZero() ? info.trueExpr
					: info.falseExpr;

		Number offset = affine.offset();
		Number coefficient = affine.coefficient();

		// aX+b=0 => -b/a=X is an integer
		if (type.isInteger()
				&& !info.numberFactory.mod(
						(IntegerNumber) offset,
						(IntegerNumber) info.numberFactory
								.abs((IntegerNumber) coefficient)).isZero())
			return info.falseExpr;
		pseudoValue = info.numberFactory.negate(info.numberFactory.divide(
				offset, coefficient));

		BoundsObject oldBounds = boundMap.get(pseudo);

		if (oldBounds == null)
			return null;

		// Know: lower <? pseudoValue <? upper
		// (Here "<?" means "<" or "<=".). So
		// lower - pseudoValue <? 0
		// upper - pseudoValue >? 0
		// if either of these is not true, you have a contradiction,
		// simplify to "false".

		// leftSign = sign of (lower-pseudoValue)
		// rightSign = sign of (upper-pseudoValue)
		int leftSign, rightSign;

		{
			Number lower = oldBounds.lower();

			if (lower == null)
				leftSign = -1; // -infinity
			else
				leftSign = info.numberFactory.subtract(lower, pseudoValue)
						.signum();

			Number upper = oldBounds.upper();

			if (upper == null) // +infinity
				rightSign = 1;
			else
				rightSign = info.numberFactory.subtract(upper, pseudoValue)
						.signum();
		}
		// if 0 is not in that interval, return FALSE
		if (leftSign > 0 || (leftSign == 0 && oldBounds.strictLower()))
			return info.falseExpr;
		if (rightSign < 0 || (rightSign == 0 && oldBounds.strictUpper()))
			return info.falseExpr;
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
	private BooleanExpression simplifyGT0(Polynomial fp,
			boolean strictInequality) {
		if (fp instanceof Constant) {
			int signum = ((Constant) fp).number().signum();

			if (strictInequality)
				return signum > 0 ? info.trueExpr : info.falseExpr;
			else
				return signum >= 0 ? info.trueExpr : info.falseExpr;
		}

		SymbolicType type = fp.type();
		AffineExpression affine = info.affineFactory.affine(fp);
		Polynomial pseudo = affine.pseudo();
		assert pseudo != null;
		Number pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null) {
			int signum = info.affineFactory.affineValue(affine, pseudoValue)
					.signum();

			if (strictInequality)
				return signum > 0 ? info.trueExpr : info.falseExpr;
			else
				return signum >= 0 ? info.trueExpr : info.falseExpr;
		}

		BoundsObject oldBounds = boundMap.get(pseudo);

		if (oldBounds == null)
			return null;

		Number newBound = info.affineFactory.bound(affine, strictInequality);
		assert newBound != null;
		// bound on pseudo X, assuming fp=aX+b>?0.
		// If a>0, it is a lower bound. If a<0 it is an upper bound.
		// newBound may or may not be strict
		Number coefficient = affine.coefficient();
		assert coefficient.signum() != 0;
		boolean strictBound = type.isInteger() ? false : strictInequality;

		int leftSign, rightSign;
		{
			Number lower = oldBounds.lower(), upper = oldBounds.upper();

			if (lower == null)
				leftSign = -1;
			else
				leftSign = info.numberFactory.subtract(lower, newBound)
						.signum();
			if (upper == null)
				rightSign = 1;
			else
				rightSign = info.numberFactory.subtract(upper, newBound)
						.signum();
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
				return info.trueExpr;
			if (rightSign < 0
					|| (rightSign == 0 && (strictBound || oldBounds
							.strictUpper())))
				return info.falseExpr;
			if (rightSign == 0 && !strictBound && !oldBounds.strictUpper())
				// X'=0, where X'=X-newBound.
				return info.idealFactory.equals(pseudo,
						info.idealFactory.constant(newBound));
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
				return info.falseExpr;
			if (rightSign < 0
					|| (rightSign == 0 && (!strictBound || oldBounds
							.strictUpper())))
				return info.trueExpr;
			if (leftSign == 0 && !strictBound && !oldBounds.strictLower())
				// X'=0, where X'=X-newBound.
				return info.idealFactory.equals(pseudo,
						info.idealFactory.constant(newBound));
		}
		return null;
	}

	/***********************************************************************
	 * End of Simplification Routines..................................... *
	 ***********************************************************************/

	/**
	 * Converts the bound to a boolean expression in canoncial form. Returns
	 * null if both upper and lower bound are infinite (equivalent to "true").
	 */
	private BooleanExpression boundToIdeal(BoundsObject bound) {
		Number lower = bound.lower(), upper = bound.upper();
		BooleanExpression result = null;
		Polynomial fp = (Polynomial) bound.expression;
		Polynomial ideal = (Polynomial) apply(fp);

		if (lower != null) {
			if (bound.strictLower())
				result = info.idealFactory.lessThan(
						info.idealFactory.constant(lower), ideal);
			else
				result = info.idealFactory.lessThanEquals(
						info.idealFactory.constant(lower), ideal);
		}
		if (upper != null) {
			BooleanExpression upperResult;

			if (bound.strictUpper())
				upperResult = info.idealFactory.lessThan(ideal,
						info.idealFactory.constant(upper));
			else
				upperResult = info.idealFactory.lessThanEquals(ideal,
						info.idealFactory.constant(upper));
			if (result == null)
				result = upperResult;
			else
				result = info.booleanFactory.and(result, upperResult);
		}
		return result;
	}

	private void initialize() {
		while (true) {
			boundMap.clear();
			clearSimplificationCache(); // why?

			boolean satisfiable = extractBounds();

			if (!satisfiable) {
				if (info.verbose) {
					info.out.println("Path condition is unsatisfiable.");
					info.out.flush();
				}
				assumption = info.falseExpr;
				return;
			} else {
				// need to substitute into assumption new value of symbolic
				// constants.
				BooleanExpression newAssumption = (BooleanExpression) simplifyExpression(assumption);

				rawAssumption = newAssumption;
				// at this point, rawAssumption contains only those facts that
				// could not be
				// determined from the booleanMap, boundMap, or constantMap.
				// these facts need to be added back in---except for the case
				// where
				// a symbolic constant is mapped to a concrete value in the
				// constantMap.
				// such symbolic constants will be entirely eliminated from the
				// assumption

				// after SimplifyExpression, the removable symbolic constants
				// should all be gone, replaced with their concrete values.
				// However, as we add back in facts from the constant map,
				// bound map and boolean
				// map, those symbolic constants might sneak back in!
				// We will remove them again later.

				for (BoundsObject bound : boundMap.values()) {
					BooleanExpression constraint = boundToIdeal(bound);

					if (constraint != null)
						newAssumption = info.booleanFactory.and(newAssumption,
								constraint);
				}
				// also need to add facts from constant map.
				// but can eliminate any constant values for primitives since
				// those will never occur in the state.
				for (Entry<Polynomial, Number> entry : constantMap.entrySet()) {
					Polynomial fp = entry.getKey();

					if (fp instanceof SymbolicConstant) {
						// symbolic constant: will be entirely eliminated
					} else {
						BooleanExpression constraint = info.idealFactory
								.equals(fp, info.idealFactory.constant(entry
										.getValue()));

						newAssumption = info.booleanFactory.and(newAssumption,
								constraint);
					}
				}
				for (Entry<BooleanExpression, Boolean> entry : booleanMap
						.entrySet()) {
					BooleanExpression primitive = entry.getKey();

					if (primitive instanceof SymbolicConstant) {
						// symbolic constant: will be entirely eliminated
					} else {
						newAssumption = info.booleanFactory.and(newAssumption,
								entry.getValue() ? primitive
										: info.booleanFactory.not(primitive));
					}
				}

				// now we remove those removable symbolic constants...

				Map<SymbolicExpression, SymbolicExpression> substitutionMap = new HashMap<>();

				for (Entry<Polynomial, Number> entry : constantMap.entrySet()) {
					SymbolicExpression key = entry.getKey();

					if (key.operator() == SymbolicOperator.SYMBOLIC_CONSTANT)
						substitutionMap.put(key,
								universe.number(entry.getValue()));
				}
				newAssumption = (BooleanExpression) universe.mapSubstituter(
						substitutionMap).apply(newAssumption);

				if (assumption.equals(newAssumption))
					break;
				assumption = (BooleanExpression) universe
						.canonic(newAssumption);
			}
		}
		extractRemainingFacts();
	}

	/**
	 * Attempts to determine bounds (upper and lower) on primitive expressions
	 * by examining the assumption. Returns false if assumption is determined to
	 * be unsatisfiable.
	 */
	private boolean extractBounds() {
		if (assumption.operator() == SymbolicOperator.AND) {
			for (BooleanExpression clause : assumption.booleanCollectionArg(0))
				if (!extractBoundsOr(clause, boundMap, booleanMap))
					return false;
		} else if (!extractBoundsOr(assumption, boundMap, booleanMap))
			return false;
		return updateConstantMap();
	}

	private void processHerbrandCast(Polynomial poly, Number value) {
		if (poly.operator() == SymbolicOperator.CAST) {
			SymbolicType type = poly.type();
			SymbolicExpression original = (SymbolicExpression) poly.argument(0);
			SymbolicType originalType = original.type();

			if (originalType.isHerbrand() && originalType.isInteger()
					&& type.isInteger() || originalType.isReal()
					&& type.isReal()) {
				SymbolicExpression constant = universe.cast(originalType,
						universe.number(value));

				cacheSimplification(original, constant);
			}
		}
	}

	private boolean updateConstantMap() {
		// The constant map doesn't get cleared because we want to keep
		// accumulating facts. Thus the map might not be empty at this point.
		for (BoundsObject bounds : boundMap.values()) {
			Number lower = bounds.lower();

			if (lower != null && lower.equals(bounds.upper)) {
				Polynomial expression = (Polynomial) bounds.expression;

				assert !bounds.strictLower && !bounds.strictUpper;
				constantMap.put(expression, lower);

				// TODO: consider removing the entry from bounds map

				processHerbrandCast(expression, lower);
			}
		}

		boolean satisfiable = LinearSolver.reduceConstantMap(info.idealFactory,
				constantMap);

		if (debug) {
			printBoundMap(info.out);
			printConstantMap(info.out);
			printBooleanMap(info.out);
		}
		return satisfiable;
	}

	private void printBoundMap(PrintStream out) {
		out.println("Bounds map:");
		for (BoundsObject boundObject : boundMap.values()) {
			out.println(boundObject);
		}
		out.println();
		out.flush();
	}

	private void printConstantMap(PrintStream out) {
		out.println("Constant map:");
		for (Entry<Polynomial, Number> entry : constantMap.entrySet()) {
			out.print(entry.getKey() + " = ");
			out.println(entry.getValue());
		}
		out.println();
		out.flush();
	}

	private void printBooleanMap(PrintStream out) {
		out.println("Boolean map:");
		for (Entry<BooleanExpression, Boolean> entry : booleanMap.entrySet()) {
			out.print(entry.getKey() + " = ");
			out.println(entry.getValue());
		}
		out.println();
		out.flush();
	}

	/**
	 * Performs deep copy of a bounds map. Temporary fix: once persistent maps
	 * and an immutable BoundsObject are used, this will not be needed.
	 * 
	 * @param map
	 *            a bounds map
	 * @return a deep copy of that map that does not share anything that is
	 *         mutable, such as a BoundsObject
	 */
	private Map<Polynomial, BoundsObject> copyBoundMap(
			Map<Polynomial, BoundsObject> map) {
		HashMap<Polynomial, BoundsObject> result = new HashMap<>();

		for (Entry<Polynomial, BoundsObject> entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().clone());
		}
		return result;
	}

	private Map<BooleanExpression, Boolean> copyBooleanMap(
			Map<BooleanExpression, Boolean> map) {
		return new HashMap<>(map);
	}

	private boolean extractBoundsOr(BooleanExpression or,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		if (or.operator() == SymbolicOperator.OR) {
			// p & (q0 | ... | qn) = (p & q0) | ... | (p & qn)
			// copies of original maps, corresponding to p. these never
			// change...
			Map<Polynomial, BoundsObject> originalBoundMap = copyBoundMap(aBoundMap);
			Map<BooleanExpression, Boolean> originalBooleanMap = copyBooleanMap(aBooleanMap);
			Iterator<? extends BooleanExpression> clauses = or
					.booleanCollectionArg(0).iterator();
			boolean satisfiable = extractBoundsBasic(clauses.next(), aBoundMap,
					aBooleanMap); // result <- p & q0:

			// result <- result | ((p & q1) | ... | (p & qn)) :
			while (clauses.hasNext()) {
				BooleanExpression clause = clauses.next();
				Map<Polynomial, BoundsObject> newBoundMap = copyBoundMap(originalBoundMap);
				Map<BooleanExpression, Boolean> newBooleanMap = copyBooleanMap(originalBooleanMap);
				// compute p & q_i:
				boolean newSatisfiable = extractBoundsBasic(clause,
						newBoundMap, newBooleanMap);

				// result <- result | (p & q_i) where result is (aBoundMap,
				// aBooleanMap)....
				satisfiable = satisfiable || newSatisfiable;
				if (newSatisfiable) {
					LinkedList<SymbolicExpression> boundRemoveList = new LinkedList<>();
					LinkedList<BooleanExpression> booleanRemoveList = new LinkedList<>();

					for (Map.Entry<Polynomial, BoundsObject> entry : aBoundMap
							.entrySet()) {
						SymbolicExpression primitive = entry.getKey();
						BoundsObject oldBound = entry.getValue();
						BoundsObject newBound = newBoundMap.get(primitive);

						if (newBound == null) {
							boundRemoveList.add(primitive);
						} else {
							oldBound.enlargeTo(newBound);
							if (oldBound.isUniversal())
								boundRemoveList.add(primitive);
						}
					}
					for (SymbolicExpression primitive : boundRemoveList)
						aBoundMap.remove(primitive);
					for (Map.Entry<BooleanExpression, Boolean> entry : aBooleanMap
							.entrySet()) {
						BooleanExpression primitive = entry.getKey();
						Boolean oldValue = entry.getValue();
						Boolean newValue = newBooleanMap.get(primitive);

						if (newValue == null || !newValue.equals(oldValue))
							booleanRemoveList.add(primitive);
					}
					for (BooleanExpression primitive : booleanRemoveList)
						aBooleanMap.remove(primitive);
				}
			}
			return satisfiable;
		} else { // 1 clause
			return extractBoundsBasic(or, aBoundMap, aBooleanMap);
		}
	}

	/**
	 * A basic expression is either a boolean constant (true/false), a
	 * LiteralExpression (p or !p) or QuantifierExpression
	 */
	private boolean extractBoundsBasic(BooleanExpression basic,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		SymbolicOperator operator = basic.operator();

		if (operator == SymbolicOperator.CONCRETE)
			return ((BooleanObject) basic.argument(0)).getBoolean();
		if (isRelational(operator)) {
			Polynomial arg = (Polynomial) basic.argument(1);

			switch (operator) {
			case EQUALS: // 0==x
				return extractEQ0Bounds(arg, aBoundMap, aBooleanMap);
			case NEQ: {
				return extractNEQ0Bounds(arg, aBoundMap, aBooleanMap);
			}
			case LESS_THAN: // 0<x
				return extractGT0Bounds(true, arg, aBoundMap, aBooleanMap);
			case LESS_THAN_EQUALS: // 0<=x
				return extractGT0Bounds(false, arg, aBoundMap, aBooleanMap);
			default:
				throw new RuntimeException("Unknown RelationKind: " + operator);
			}
		}
		if (operator == SymbolicOperator.EXISTS
				|| operator == SymbolicOperator.FORALL) {
			// forall or exists: difficult
			// forall x: ()bounds: can substitute whatever you want for x
			// and extract bounds.
			// example: forall i: a[i]<7. Look for all occurrence of a[*]
			// and add bounds
			return true;
		}
		if (operator == SymbolicOperator.NOT) {
			BooleanExpression primitive = basic.booleanArg(0);
			Boolean value = aBooleanMap.get(primitive);

			if (value != null)
				return !value;
			aBooleanMap.put(primitive, false);
			return true;
		}
		{
			Boolean value = aBooleanMap.get(basic);

			if (value != null)
				return value;
			aBooleanMap.put(basic, true);
			return true;
		}
	}

	/**
	 * Given the fact that poly==0, for some {@link Polynomial} poly, updates
	 * the specified bound map and boolean map appropriately.
	 * 
	 * @param poly
	 *            a non-<code>null</code> polynomial
	 * @param aBoundMap
	 *            a bound map: a map from pseudo-primitive polynomials to bound
	 *            objects specifying an interval bound for those polynomials
	 * @param aBooleanMap
	 *            a map specifying a concrete boolean value for some set of
	 *            boolean-valued symbolic expressions
	 * @return <code>false</code> if it is determined that the given assertion
	 *         is inconsistent with the information in the bound map and boolean
	 *         map; else <code>true</code>
	 */
	private boolean extractEQ0Bounds(Polynomial poly,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		if (poly instanceof Constant)
			return poly.isZero();

		AffineExpression affine = info.affineFactory.affine(poly);
		Polynomial pseudo = affine.pseudo();
		RationalNumber coefficient = info.numberFactory.rational(affine
				.coefficient());
		RationalNumber offset = info.numberFactory.rational(affine.offset());
		RationalNumber rationalValue = info.numberFactory
				.negate(info.numberFactory.divide(offset, coefficient));
		Number value; // same as rationalValue but IntegerNumber if type is
						// integer
		BoundsObject bound = aBoundMap.get(pseudo);

		if (pseudo.type().isInteger()) {
			if (info.numberFactory.isIntegral(rationalValue)) {
				value = info.numberFactory.integerValue(rationalValue);
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
			if ((bound.lower != null && info.numberFactory.compare(bound.lower,
					value) > 0)
					|| (bound.upper != null && value.compareTo(bound.upper) > 0))
				return false;
			bound.makeConstant(value);
		}
		return true;
	}

	/**
	 * Given the fact that poly!=0, for some {@link Polynomial} poly, updates
	 * the specified bound map and boolean map appropriately.
	 * 
	 * @param poly
	 *            a non-<code>null</code> polynomial
	 * @param aBoundMap
	 *            a bound map: a map from pseudo-primitive polynomials to bound
	 *            objects specifying an interval bound for those polynomials
	 * @param aBooleanMap
	 *            a map specifying a concrete boolean value for some set of
	 *            boolean-valued symbolic expressions
	 * @return <code>false</code> if it is determined that the given assertion
	 *         is inconsistent with the information in the bound map and boolean
	 *         map; else <code>true</code>
	 */
	private boolean extractNEQ0Bounds(Polynomial poly,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		if (poly instanceof Constant)
			return !poly.isZero();

		// poly=aX+b. if X=-b/a, contradiction.
		SymbolicType type = poly.type();
		AffineExpression affine = info.affineFactory.affine(poly);
		Polynomial pseudo = affine.pseudo();
		RationalNumber coefficient = info.numberFactory.rational(affine
				.coefficient());
		RationalNumber offset = info.numberFactory.rational(affine.offset());
		RationalNumber rationalValue = info.numberFactory
				.negate(info.numberFactory.divide(offset, coefficient));
		Number value; // same as rationalValue but IntegerNumber if type is
						// integer
		BoundsObject bound = aBoundMap.get(pseudo);
		Polynomial zero = info.idealFactory.zero(type);

		if (type.isInteger()) {
			if (info.numberFactory.isIntegral(rationalValue)) {
				value = info.numberFactory.integerValue(rationalValue);
			} else {
				// an integer cannot equal a non-integer.
				aBooleanMap.put(info.idealFactory.neq(zero, poly), true);
				return true;
			}
		} else {
			value = rationalValue;
		}
		// interpret fact pseudo!=value, where pseudo is in bound
		if (bound == null) {
			// for now, nothing can be done, since the bounds are
			// plain intervals. we need a more precise abstraction
			// than intervals here.
		} else if (bound.contains(value)) {
			// is value an end-point? Might be able to sharpen bound...
			if (bound.lower != null && bound.lower.equals(value)
					&& !bound.strictLower) {
				bound.restrictLower(bound.lower, true);
			}
			if (bound.upper != null && bound.upper.equals(value)
					&& !bound.strictUpper) {
				bound.restrictUpper(bound.upper, true);
			}
			if (bound.isEmpty()) {
				return false;
			}
		}
		aBooleanMap.put(info.universe.neq(zero, poly), true);
		return true;
	}

	/**
	 * Extracts bounds from expression of the form e>0 (strict true) or e>=0
	 * (strict false). Updates aBoundMap and aBooleanMap.
	 */
	private boolean extractGT0Bounds(boolean strict, Polynomial poly,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		return extractGT0(poly, aBoundMap, aBooleanMap, strict);
	}

	private boolean extractGT0(Polynomial fp,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap, boolean strict) {
		AffineExpression affine = info.affineFactory.affine(fp);
		Polynomial pseudo;

		if (affine == null)
			return true;
		pseudo = affine.pseudo();
		if (pseudo != null) {
			BoundsObject boundsObject = aBoundMap.get(pseudo);
			Number coefficient = affine.coefficient();
			Number bound = info.affineFactory.bound(affine, strict);

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

	private void declareFact(SymbolicExpression booleanExpression, boolean truth) {
		BooleanExpression value = truth ? info.trueExpr : info.falseExpr;

		cacheSimplification(booleanExpression, value);
	}

	private void declareClauseFact(BooleanExpression clause) {
		if (isNumericRelational(clause)) {
			if (clause.operator() == SymbolicOperator.NEQ) {
				BooleanExpression eq0 = (BooleanExpression) info.universe
						.not(clause);

				declareFact(eq0, false);
			}
		} else
			declareFact(clause, true);
	}

	/**
	 * This method inserts into the simplification cache all facts from the
	 * assumption that are not otherwise encoded in the {@link #constantMap},
	 * {@link #booleanMap}, or {@link #boundMap}. It is to be invoked only after
	 * the assumption has been simplified for the final time.
	 */
	private void extractRemainingFacts() {
		SymbolicOperator operator = assumption.operator();

		if (operator == SymbolicOperator.AND) {
			for (BooleanExpression or : assumption.booleanCollectionArg(0)) {
				declareClauseFact(or);
			}
		} else {
			declareClauseFact(assumption);
		}
	}

	// Exported methods.............................................

	/**
	 * {@inheritDoc}
	 * 
	 * Simplifies an expression, providing special handling beyond the generic
	 * simplification for ideal expressions. Relational expressions also get
	 * special handling. All other expressions are simplified generically.
	 * 
	 * This method does not look in the simplify cache for an existing
	 * simplification of expression. See the documentation for the super method.
	 * 
	 * @see {@link #simplifyGenericExpression}
	 * @see {@link #simplifyRelational}
	 * @see {@link #simplifyPolynomial}
	 */
	@Override
	protected SymbolicExpression simplifyExpression(
			SymbolicExpression expression) {
		if (expression instanceof Constant) // optimization
			return expression;
		if (expression instanceof Polynomial)
			return simplifyPolynomial((Polynomial) expression);
		if (expression.type() != null && expression.type().isBoolean()) {
			if (expression.isTrue() || expression.isFalse())
				return expression;

			Boolean booleanResult = booleanMap.get(expression);

			if (booleanResult != null) {
				return booleanResult ? universe.trueExpression() : universe
						.falseExpression();
			}
			if (isNumericRelational(expression))
				return simplifyRelational((BooleanExpression) expression);
		}
		return simplifyGenericExpression(expression);
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		if (intervalComputed) {
			if (interval != null && intervalVariable.equals(symbolicConstant))
				return interval;
			return null;
		}
		intervalComputed = true;
		if (!booleanMap.isEmpty() || !rawAssumption.isTrue()) {
			return null;
		}
		if (!constantMap.isEmpty()) {
			if (!boundMap.isEmpty() || constantMap.size() != 1) {
				return null;
			}
			Entry<Polynomial, Number> entry = constantMap.entrySet().iterator()
					.next();
			Polynomial fp1 = entry.getKey();
			Number value = entry.getValue();

			if (!fp1.equals(symbolicConstant)) {
				return null;
			}
			interval = BoundsObject.newTightBound(symbolicConstant, value);
			intervalVariable = symbolicConstant;
			return interval;
		}
		if (boundMap.size() == 1) {
			Entry<Polynomial, BoundsObject> entry = boundMap.entrySet()
					.iterator().next();
			Polynomial fp1 = entry.getKey();

			if (!fp1.equals(symbolicConstant)) {
				return null;
			}
			interval = entry.getValue();
			intervalVariable = symbolicConstant;
			return interval;
		}
		return null;
	}

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		if (substitutionMap == null) {
			substitutionMap = new HashMap<SymbolicConstant, SymbolicExpression>();
			for (Entry<Polynomial, Number> entry : constantMap.entrySet()) {
				Polynomial fp = entry.getKey();

				if (fp instanceof SymbolicConstant)
					substitutionMap.put((SymbolicConstant) fp,
							universe.number(entry.getValue()));
			}
			for (Entry<BooleanExpression, Boolean> entry : booleanMap
					.entrySet()) {
				BooleanExpression primitive = entry.getKey();

				if (primitive instanceof SymbolicConstant)
					substitutionMap.put((SymbolicConstant) primitive,
							universe.bool(entry.getValue()));
			}
		}
		return substitutionMap;
	}

	/**
	 * This method takes the assumption in the IdealSimplifier and reduces the
	 * Context to its basic form.
	 */
	@Override
	public BooleanExpression getReducedContext() {
		return assumption;
	}

	/**
	 * This method takes the assumption in the IdealSimplifier and
	 */
	@Override
	public BooleanExpression getFullContext() {
		if (fullContext == null) {
			Map<SymbolicConstant, SymbolicExpression> map = substitutionMap();

			fullContext = getReducedContext();
			for (Entry<SymbolicConstant, SymbolicExpression> entry : map
					.entrySet()) {
				SymbolicConstant key = entry.getKey();
				SymbolicExpression value = entry.getValue();
				BooleanExpression equation = universe.equals(key, value);

				fullContext = universe.and(fullContext, equation);
			}
		}
		return fullContext;
	}
}
