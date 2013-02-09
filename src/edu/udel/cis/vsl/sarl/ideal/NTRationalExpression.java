package edu.udel.cis.vsl.sarl.ideal;

/**
 * A nontrivial rational expression. It consists of a numerator and denominator,
 * both factored polynomials.
 * 
 * @author siegel
 * 
 */
public class NTRationalExpression extends IdealExpression implements
		RationalExpression {

	protected NTRationalExpression(Polynomial numerator, Polynomial denominator) {
		super(SymbolicOperator.DIVIDE, numerator.type(), numerator, denominator);
		assert !denominator.isOne();
		assert !denominator.isZero();
		assert !numerator.isZero();
		assert !numerator.equals(denominator);
	}

	public Polynomial numerator(IdealFactory factory) {
		return (Polynomial) argument(0);
	}

	public Polynomial numerator() {
		return (Polynomial) argument(0);
	}

	public Polynomial denominator(IdealFactory factory) {
		return (Polynomial) argument(1);
	}

	public Polynomial denominator() {
		return (Polynomial) argument(1);
	}

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isOne() {
		return false;
	}

	@Override
	public IdealKind idealKind() {
		return IdealKind.NTRationalExpression;
	}

	@Override
	protected int compareIdeal(IdealExpression that) {
		NTRationalExpression thatRational = (NTRationalExpression) that;
		int result = numerator().compareTo(thatRational.numerator());

		if (result != 0)
			return result;
		result = denominator().compareTo(thatRational.denominator());
		return result;
	}
}
