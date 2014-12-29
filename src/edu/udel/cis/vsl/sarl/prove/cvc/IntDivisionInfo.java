package edu.udel.cis.vsl.sarl.prove.cvc;

import cvc3.Expr;
import cvc3.Rational;
import cvc3.ValidityChecker;

/**
 * Class containing information pertaining to integer division of two
 * expressions.
 * 
 * Since integer division and modulus operations are not supported by CVC3, this
 * is dealt with by adding auxiliary variables and constraints to the CVC3
 * representation of the query. Given any integer division or modulus operations
 * occurring in the query, A OP B, we create auxiliary integer variables Q and
 * R on the CVC3 side and add constraints A=QB+R, |R|<|B|, sgn(R)=sgn(A).
 * 
 * Specifically: introduce integer variables Q and R. Introduce constraint
 * A=QB+R. If we assume A and B are non-negative: 0<=R<B. Otherwise, little more
 * work. FOR NOW, assume A and B are non-negative.
 * 
 * This object stores the CVC3 objects corresponding to one integer division
 * pair. It stores Q, R, and the list of constraints.
 */
public class IntDivisionInfo {

	/**
	 * The numerator in the division (or modulus) operation.
	 */
	Expr numerator;

	/**
	 * The denominator in the division (or modulus) operation.
	 */
	Expr denominator;

	/**
	 * The CVC3 variable representing the quotient,
	 */
	Expr quotient;

	/**
	 * The CVC3 variable representing the remainder.
	 */
	Expr remainder;

	/**
	 * The first constraint: numerator = quotient*denominator + remainder.
	 */
	private Expr constraint1;

	/**
	 * The second constraint: 0 <= remainder < denominator
	 */
	private Expr constraint2;

	/**
	 * Constructs new IntDivisionInfo from given fields, constructing the
	 * constraints in the process. The constraints are not added to the vc.
	 * 
	 * @param vc
	 *            the validity checker used to construct the constraints
	 * @param numerator
	 *            numerator in division or modulus operation
	 * @param denominator
	 *            denominator in division or modulus operation
	 * @param quotient
	 *            quotient variable
	 * @param remainder
	 *            remainder variable
	 */
	IntDivisionInfo(ValidityChecker vc, Expr numerator, Expr denominator,
			Expr quotient, Expr remainder) {
		this.numerator = numerator;
		this.denominator = denominator;
		this.quotient = quotient;
		this.remainder = remainder;
		constraint1 = vc.eqExpr(numerator,
				vc.plusExpr(vc.multExpr(quotient, denominator), remainder));
		constraint2 = null; // 0<=R<B

		if (denominator.isRational()) {
			Rational rationalDenominator = denominator.getRational();

			if (rationalDenominator.isInteger()) {
				int denominatorInt = rationalDenominator.getInteger();

				if (denominatorInt == 2) {
					constraint2 = vc.orExpr(
							vc.eqExpr(vc.ratExpr(0), remainder),
							vc.eqExpr(vc.ratExpr(1), remainder));
				}
			}
		}
		if (constraint2 == null) {
			constraint2 = vc.andExpr(vc.leExpr(vc.ratExpr(0), remainder),
					vc.ltExpr(remainder, denominator));
		}
	}

	void addConstraints(ValidityChecker vc) {
		vc.assertFormula(constraint1);
		vc.assertFormula(constraint2);
	}

}
