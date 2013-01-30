package edu.udel.cis.vsl.sarl.symbolic.factorpoly;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.factor.Factorization;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.Polynomial;

/**
 * A polynomial together with a factorization of that polynomial.
 * 
 * The factorization is not part of the state. It is not taken into
 * consideration in the hashCode or equals method. It is also mutable.
 */
public class FactoredPolynomial extends CommonSymbolicExpression implements
		TreeExpressionIF {

	private Polynomial polynomial;

	// should it be possible to allow a polynomial to have more than one
	// factorization?
	// p=ab=cd, for example. it would be nice if we could factor polynomials.

	private Factorization factorization;

	public FactoredPolynomial(Polynomial polynomial, Factorization factorization) {
		super(polynomial.type());
		assert factorization != null;
		this.polynomial = polynomial;
		this.factorization = factorization;
	}

	public Polynomial polynomial() {
		return polynomial;
	}

	public Factorization factorization() {
		return factorization;
	}

	void setFactorization(Factorization factorization) {
		this.factorization = factorization;
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression that) {
		return that instanceof FactoredPolynomial
				&& polynomial.equals(((FactoredPolynomial) that).polynomial);
	}

	@Override
	protected int intrinsicHashCode() {
		return polynomial.hashCode();
	}

	public String toString() {
		return polynomial.toString();
	}

	public String toStringLong() {
		return "[" + polynomial + ", " + factorization + "]";
	}

	public String atomString() {
		return polynomial.atomString();
	}

	public boolean isZero() {
		return polynomial.isZero();
	}

	public boolean isOne() {
		return polynomial.isOne();
	}

	public IntegerNumberIF degree() {
		return polynomial.degree();
	}

	public TreeExpressionIF argument(int index) {
		return factorization.argument(index);
	}

	public SymbolicOperator operator() {
		return factorization.operator();
	}

	public int numArguments() {
		return factorization.numArguments();
	}

	/** Same as in Polynomial: always returns something. 0 if polynomial is 0. */
	public NumberIF constantTerm() {
		return polynomial.constantTerm();
	}

	public boolean isConstant() {
		return degree().signum() <= 0;
	}

}
