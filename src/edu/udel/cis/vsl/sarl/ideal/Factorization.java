package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

/**
 * A Factorization represents a factorization of a polynomial into a form
 * c*f1^i1*...*fn^in, where the fi are polynomials and the in are concrete
 * positive integers.
 * 
 * @author siegel
 * 
 */
public interface Factorization extends SymbolicExpressionIF {

	Constant factorizationConstant(IdealFactory factory);

	MonicFactorization monicFactorization(IdealFactory factory);

}
