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
import edu.udel.cis.vsl.sarl.simplify.common.CommonSimplifier;

/**
 * An implementation of SimplifierIF for the Ideal Universe. Provides methods to
 * take a symbolic expression from an ideal universe and return a "simplified"
 * version of the expression which is equivalent to the original in the
 * mathematical "ideal" semantics. Similar method is provided for types.
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
	 * AffineExpression for the definition. The value is the constant value that
	 * has been determined to be the value of that pseudo.
	 */
	private Map<Polynomial, Number> constantMap = new HashMap<Polynomial, Number>();

	/**
	 * Has the interval interpretation of this context been computed?
	 */
	private boolean intervalComputed = false;

	/**
	 * The interpretation of the context as an Interval, or null if it cannot be
	 * so interpreted.
	 */
	private Interval interval = null;

	/**
	 * The variable bound by the interval.
	 */
	private SymbolicConstant intervalVariable = null;

	public IdealSimplifier(SimplifierInfo info, BooleanExpression assumption) {
		super(info.universe);
		this.info = info;
		this.assumption = assumption;
		initialize();
	}

	/***********************************************************************
	 * Begin Simplification Routines...................................... *
	 ***********************************************************************/

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

	private boolean isNumericRelational(SymbolicExpression expression) {
		return isRelational(expression.operator())
				&& ((SymbolicExpression) expression.argument(0)).isNumeric();
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
		AffineExpression affine = info.affineFactory.affine(fp);
		Polynomial pseudo = affine.pseudo();
		Number pseudoValue = constantMap.get(pseudo);

		if (pseudoValue != null)
			return info.idealFactory.constant(info.affineFactory.affineValue(
					affine, pseudoValue));
		{
			int numTerms = fp.termMap(info.idealFactory).size();

			if (numTerms > 1) {
				Polynomial result = (Polynomial) simplifyGenericExpression(fp);

				if (result != fp)
					return result;
			}
			{
				Monomial f1 = fp.factorization(info.idealFactory);

				if (f1.degree() > 1) {
					Monomial f2 = (Monomial) simplifyGenericExpression(f1);

					if (f2 != f1)
						return f2.expand(info.idealFactory);
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

	// 4 relations: 0<, 0<=, 0==, 0!=

	// 0<p/q <=> (0<p && 0<q) || (0<-p && 0<-q)

	// 0<=p/q <=> (0<=p && 0<q) || (0<=-p && 0<-q)

	// 0==p/q <=> 0==p

	// 0!=p/q <=> 0!=p

	private BooleanExpression simplifyRelational(BooleanExpression expression) {
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
		case NEQ:
			result = simplifyEQ0(poly);
			return result == null ? expression
					: (BooleanExpression) info.universe.not(result);
		default:
			throw new SARLInternalException("unreachable");
		}
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
	private BooleanExpression simplifyEQ0(Polynomial fp) {
		SymbolicType type = fp.type();
		AffineExpression affine = info.affineFactory.affine(fp);
		Polynomial pseudo = affine.pseudo(); // non-null since fp non-constant
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

		// have bounds on X, now simplify aX+b=0.
		// aX+b=0 => solve for X=-b/a (check int arith)
		// is -b/a within the bounds? if not: return FALSE
		// if yes: no simplification.

		int leftSign, rightSign;

		{
			Number lower = oldBounds.lower();

			if (lower == null)
				leftSign = -1;
			else
				leftSign = info.numberFactory.subtract(lower, pseudoValue)
						.signum();

			Number upper = oldBounds.upper();

			if (upper == null)
				rightSign = 1;
			else
				rightSign = info.numberFactory.subtract(upper, pseudoValue)
						.signum();
		}
		// if 0 is not in that interval, return FALSE
		if (leftSign > 0 || (leftSign == 0 && oldBounds.strictLower()))
			return info.falseExpr;
		if (rightSign < 0 || (rightSign == 0 && oldBounds.strictUpper()))
			return info.trueExpr;
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
		Polynomial ideal = simplifyPolynomial(fp);

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
			simplifyMap.clear(); // why?

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
				if (assumption.equals(newAssumption))
					break;
				assumption = newAssumption;
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

				simplifyMap.put(original, constant);
			}
		}
	}

	private boolean updateConstantMap() {
		for (BoundsObject bounds : boundMap.values()) {
			Number lower = bounds.lower();

			if (lower != null && lower.equals(bounds.upper)) {
				Polynomial expression = (Polynomial) bounds.expression;

				assert !bounds.strictLower && !bounds.strictUpper;
				constantMap.put(expression, lower);
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

	private boolean extractBoundsOr(BooleanExpression or,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		if (or.operator() == SymbolicOperator.OR) {
			// p & (q0 | ... | qn) = (p & q0) | ... | (p & qn)
			// copies of original maps, corresponding to p. these never
			// change...
			// TODO: HEY, USE IMMUTABLE MAPS!
			Map<Polynomial, BoundsObject> originalBoundMap = new HashMap<Polynomial, BoundsObject>(
					aBoundMap);
			Map<BooleanExpression, Boolean> originalBooleanMap = new HashMap<BooleanExpression, Boolean>(
					aBooleanMap);
			Iterator<? extends BooleanExpression> clauses = or
					.booleanCollectionArg(0).iterator();
			boolean satisfiable = extractBoundsBasic(clauses.next(), aBoundMap,
					aBooleanMap); // result <- p & q0:

			// result <- result | ((p & q1) | ... | (p & qn)) :
			while (clauses.hasNext()) {
				BooleanExpression clause = clauses.next();
				Map<Polynomial, BoundsObject> newBoundMap = new HashMap<Polynomial, BoundsObject>(
						originalBoundMap);
				Map<BooleanExpression, Boolean> newBooleanMap = new HashMap<BooleanExpression, Boolean>(
						originalBooleanMap);
				// compute p & q_i:
				boolean newSatisfiable = extractBoundsBasic(clause,
						newBoundMap, newBooleanMap);

				// result <- result | (p & q_i) where result is (aBoundMap,
				// aBooleanMap)....
				satisfiable = satisfiable || newSatisfiable;
				if (newSatisfiable) {
					LinkedList<BooleanExpression> removeList = new LinkedList<BooleanExpression>();

					for (Map.Entry<Polynomial, BoundsObject> entry : newBoundMap
							.entrySet()) {
						SymbolicExpression primitive = entry.getKey();
						BoundsObject bound2 = entry.getValue();
						BoundsObject bound1 = aBoundMap.get(primitive);

						if (bound1 != null)
							bound1.enlargeTo(bound2);
					}
					for (Map.Entry<BooleanExpression, Boolean> entry : newBooleanMap
							.entrySet()) {
						BooleanExpression primitive = entry.getKey();
						Boolean newValue = entry.getValue();
						assert newValue != null;
						Boolean oldValue = aBooleanMap.get(primitive);

						if (oldValue != null && !oldValue.equals(newValue))
							removeList.add(primitive);
					}
					for (BooleanExpression primitive : removeList)
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
				return extractEQ0Bounds(false, arg, aBoundMap, aBooleanMap);
			case NEQ: {
				boolean result = extractEQ0Bounds(true, arg, aBoundMap,
						aBooleanMap);

				return result;
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

	// TODO: go further and perform backwards substitution...

	private boolean extractEQ0Bounds(boolean not, Polynomial fp,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		if (not)
			return extractNEQ0Bounds(fp, aBoundMap, aBooleanMap);

		int degree = fp.degree();

		if (fp instanceof Constant)
			return fp.isZero();

		// this branch is here as a compromise. Gaussian elimination
		// takes a long time and most of the time it is only useful
		// for degree 1 polynomials.
		if (!info.linearizePolynomials && degree > 1)
			return true;

		AffineExpression affine = info.affineFactory.affine(fp);
		Polynomial pseudo = affine.pseudo();
		RationalNumber coefficient = info.numberFactory.rational(affine
				.coefficient());
		RationalNumber offset = info.numberFactory.rational(affine.offset());
		RationalNumber rationalValue = info.numberFactory
				.negate(info.numberFactory.divide(offset, coefficient));
		Number value;
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
			if ((bound.lower != null && bound.lower.compareTo(value) > 0)
					|| (bound.upper != null && value.compareTo(bound.upper) > 0))
				return false;
			bound.makeConstant(value);
		}
		return true;
	}

	private boolean extractNEQ0Bounds(Polynomial fp,
			Map<Polynomial, BoundsObject> aBoundMap,
			Map<BooleanExpression, Boolean> aBooleanMap) {
		return true;
	}

	/**
	 * Exracts bounds from expression of the form e>0 (strict true) or e>=0
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

		simplifyMap.put(booleanExpression, value);
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
	 * assumption that are not otherwised encoded in the constantMap,
	 * booleanMap, or boundMap. It is to be invoked only after the assumption
	 * has been simplified for the final time.
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

	@Override
	protected SymbolicExpression simplifyExpression(
			SymbolicExpression expression) {
		// special handling for:
		// symbolic constants (booleans, numeric, ...)
		// numeric expressions (polynomials, ...)
		// relational expressions (0<a, 0<=a, 0==a, 0!=a)
		expression = simplifyGenericExpression(expression);
		if (expression instanceof Polynomial)
			return simplifyPolynomial((Polynomial) expression);
		if (isNumericRelational(expression))
			return simplifyRelational((BooleanExpression) expression);
		return expression;
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

	@Override
	public BooleanExpression getReducedContext() {
		return assumption;
	}

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
