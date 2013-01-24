package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicUniverse;

/**
 * <pre>
 * rat       : DIVIDE factpoly factpoly | factpoly
 * factpoly  : FACTPOLY poly fact | poly
 * fact      : MULTIPLY number monicfact | monicfact
 * monicfact : MULTIPLY polypow+ | polypow
 * polypow   : POWER poly int | poly
 * poly      : SUM monomial+ | monomial
 * monomial  : MULTIPLY number monic | number | monic
 * monic     : MULTIPLY ppow+ | ppow
 * ppow      : POWER primitive int | primitive
 * number    : CONCRETE numberObject
 * primitive : ...
 * </pre>
 * 
 * A primitive is anything that doesn't fall into one of the preceding
 * categories, including a symbolic constant, array read expression, tuple read
 * expression, function application, etc.
 * 
 * Rules of the normal form:
 * <ul>
 * <li>Any numeric expression will have one of the following 8 forms: rat,
 * factpoly, poly, monomial, monic, ppow, number, primitive; a numeric
 * expression of integer type will not have rat form
 * <li>NO MIXING OF TYPES: in an expression of real type, all of the descendant
 * arguments will have real type; in an expression of integer type, all of the
 * descendant arguments will have integer type</li>
 * <li>the second factpoly argument of DIVIDE in the rat rule must be monic
 * (leading coefficient 1) and cannot be a number</li>
 * <li>the two factpoly arguments of DIVIDE in the rat rule will have no common
 * factors</li>
 * <li>the poly argument of FACTPOLY cannot be a monomial</li>
 * <li>the poly argument of FACTPOLY must be a monic polynomial, i.e., have
 * leading coefficient 1</li>
 * <li>the fact argument of FACTPOLY, when multiplied out, will yield the same
 * polynomial as the poly argument of FACTPOLY</li>
 * <li>the sequence polypow+ in monicfact will have length at least 2</li>
 * <li>the int in polypow will be greater than or equal to 2</li>
 * <li>the sequence monomial+ in poly will have length >=2</li>
 * <li>the number argument of MULTIPLY in monomial rule will not be 1.</li>
 * <li>the sequence ppow+ in monic rule will have length >=2</li>
 * <li>the int in ppow rule will be >=2</li>
 * </ul>
 * 
 * <pre>
 * Normal form examples:
 * x         : x
 * 1         : 1
 * x+1       : SUM x 1
 * x^2       : POWER x 2
 * (x+1)^2   : FACTPOLY
 *               (SUM (POWER x 2) (MULTIPLY 2 x) 1)
 *               (POWER (SUM x 1) 2)
 * x/y       : DIVIDE x y
 * 2/3       : CONCRETE(2/3)
 * x/2       : MULTIPLY 1/2 x
 * (x+1)^2/3 : FACTPOLY
 *               (SUM (MULTIPLY 1/3 (POWER x 2)) (MULTIPLY 2/3 x) 1/3)
 *               (MULTIPLY 1/3 (POWER (SUM x 1) 2))
 * 
 * </pre>
 * 
 */
public class IdealFactory {

	private CommonSymbolicUniverse universe;

	public IdealFactory(CommonSymbolicUniverse universe) {
		this.universe = universe;
	}

	// Helpers

	// triple common factorization routine.

	private SymbolicObject canonic(SymbolicObject object) {
		return universe.canonic(object);
	}

	private NTFactoredPolynomial ntFactoredPolynomial(Polynomial polynomial,
			Factorization factorization) {
		return (NTFactoredPolynomial) canonic(new NTFactoredPolynomial(
				polynomial, factorization));
	}

	private NTFactorization ntFactorization(Constant constant,
			MonicFactorization monicFactorization) {
		return (NTFactorization) canonic(new NTFactorization(constant,
				monicFactorization));
	}

	private MonicFactorization[] extractCommonality(MonicFactorization fact1,
			MonicFactorization fact2) {

		return null;
	}

	/**
	 * Given two factorizations f1 and f2, this returns an array of length 3
	 * containing 3 factorizations a, g1, g2 (in that order), satisfying
	 * f1=a*g1, f2=a*g2, g1 and g2 have no factors in common, a is a monic
	 * factorization (its constant is 1).
	 */
	private Factorization[] extractCommonality(NTFactorization fact1,
			NTFactorization fact2) {
		MonicFactorization[] monicTriple = extractCommonality(
				fact1.monicFactorizatio(), fact2.monicFactorizatio());

		return new Factorization[] { monicTriple[0],
				ntFactorization(fact1.constant(), monicTriple[1]),
				ntFactorization(fact2.constant(), monicTriple[2]) };
	}

	private Factorization[] extractCommonality(NTFactorization fact1,
			Factorization fact2) {
		if (fact2 instanceof NTFactorization)
			return extractCommonality(fact1, (NTFactorization) fact2);
		else {
			// TODO
		}
		return null;
	}

	private Factorization[] extractCommonality(Factorization fact1,
			Factorization fact2) {
		if (fact1 instanceof NTFactorization)
			return extractCommonality((NTFactorization) fact1, fact2);
		else {
			// TODO
		}
		return null;
	}

	/**
	 * Addition strategy:
	 * 
	 * <pre>
	 * rat + rat : a/b + c/d = a/(xr) + c/(yr) = (ay+cx)/(xyr)
	 * rat + factpoly : a/b + c = (a+bc)/b
	 * factpoly + factpoly : (a1,f1)+(a2,f2) = (a1,rx)+(a2,ry) = (a1+a2, r(x+y))
	 * factpoly + polypow : (a,f)+p^i = (a, p^jg) + p^i = (a+p^i, p^j(g+p^(i-j)))
	 * factpoly + poly ...
	 * </pre>
	 * 
	 * @param r1
	 *            a RationalExpression
	 * @param r2
	 *            a RationalExpression
	 * @return r1+r2
	 */
	public RationalExpression add(RationalExpression r1, RationalExpression r2) {

		return null;
	}

}
