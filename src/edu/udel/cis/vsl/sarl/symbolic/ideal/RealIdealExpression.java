package edu.udel.cis.vsl.sarl.symbolic.ideal;

import edu.udel.cis.vsl.sarl.symbolic.rational.RationalExpression;

/**
 * The symbolic expression class in the ideal universe used to represent a real
 * number. It essentially wraps an instance of RationalExpression, which is the
 * canonical form for rational values in the ideal universe: a quotient of two
 * factored polynomials.
 * 
 * @author siegel
 * 
 */
public class RealIdealExpression extends IdealExpression {

	protected RealIdealExpression(RationalExpression rational) {
		super(rational);
		assert type().isReal();
	}

	public RationalExpression rational() {
		return (RationalExpression) expression;
	}
}
