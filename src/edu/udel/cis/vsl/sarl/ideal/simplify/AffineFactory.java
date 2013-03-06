package edu.udel.cis.vsl.sarl.ideal.simplify;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class AffineFactory {

	private IdealFactory idealFactory;

	private NumberFactory numberFactory;

	private SymbolicTypeFactory typeFactory;

	private IntegerNumber ZERO_INT, ONE_INT;

	private RationalNumber ZERO_REAL;

	private AffineExpression zeroIntAffine, zeroRealAffine;

	private SymbolicType integerType, realType;

	public AffineFactory(IdealFactory idealFactory) {
		this.idealFactory = idealFactory;
		this.numberFactory = idealFactory.numberFactory();
		this.typeFactory = idealFactory.typeFactory();
		ZERO_INT = numberFactory.zeroInteger();
		ZERO_REAL = numberFactory.zeroRational();
		ONE_INT = numberFactory.oneInteger();
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
		zeroIntAffine = affine(null, ZERO_INT, ZERO_INT);
		zeroRealAffine = affine(null, ZERO_REAL, ZERO_REAL);
	}

	public AffineExpression affine(Polynomial pseudo, Number coefficient,
			Number offset) {
		SymbolicType type = (coefficient instanceof IntegerNumber ? integerType
				: realType);

		assert pseudo == null || type.equals(pseudo.type());
		assert type.isInteger() && offset instanceof IntegerNumber
				|| type.isReal() && offset instanceof RationalNumber;
		return new AffineExpression(pseudo, coefficient, offset);
	}

	public AffineExpression affine(Polynomial fp) {
		SymbolicType type = fp.type();
		int degree = fp.degree();

		if (degree < 0) { // fp=0
			return type.isInteger() ? zeroIntAffine : zeroRealAffine;
		} else if (degree == 0) { // fp is constant
			return affine(null, type.isInteger() ? ZERO_INT : ZERO_REAL,
					((Constant) fp).number());
		} else {
			// first, subtract off constant term (if it is non-0).
			// then factor out best you can:
			// if real: factor out leading coefficient (unless it is 1)
			// if int: take gcd of coefficients and factor that out (unless it
			// is 1)
			Number constantTerm = fp.constantTerm(idealFactory).number();
			Polynomial difference = idealFactory.subtractConstantTerm(fp); // fp-constantTerm
			Constant coefficientConcrete = difference.factorization(
					idealFactory).monomialConstant(idealFactory);
			Polynomial pseudo = idealFactory.divide(difference,
					coefficientConcrete);

			return affine(pseudo, coefficientConcrete.number(), constantTerm);
		}
	}

	/**
	 * Determines a bound on the psuedo primitive polynomial X, assuming the
	 * predicate aX+b>0 holds (if strict is true), or aX+b>=0 (if strict is
	 * false).
	 * 
	 * The bound will be either an upper or a lower bound, depending upon the
	 * sign of a. If a>0, it is a lower bound. If a<0 it is an upper bound.
	 * 
	 * The bound returned will be either strict or not strict. If X is real (not
	 * integral) then the bound is strict iff the argument strict is true. If x
	 * is integral, the returned bound is always non-strict.
	 * 
	 * If a=0, this is null.
	 * 
	 * If a!=0 and X is real (not integral), this is -b/a. The argument strict
	 * is not used in this case.
	 * 
	 * If X is integral, there are 4 cases to consider. The first factor is the
	 * sign of a, the second is the argument strict.
	 * 
	 * Suppose a<0, so we are dealing with an upper bound c=-b/a.
	 * 
	 * If strict is true, we have, X<c, which is equivalent to X<=ceil(c)-1.
	 * 
	 * If strict is false, we have X<=c, which is equivalent to X<=floor(c).
	 * 
	 * Suppose a>0, so we are dealing with a lower bound c=-b/a.
	 * 
	 * If strict is true, we have X>c, i.e. X>=floor(c)+1.
	 * 
	 * If strict is false, we have X>=c, i.e., X>=ceil(c).
	 */
	public Number bound(AffineExpression affine, boolean strict) {
		RationalNumber rationalBound = null;
		Number result = null;
		Polynomial pseudo = affine.pseudo();
		RationalNumber offset = numberFactory.rational(affine.offset());
		RationalNumber coefficient = numberFactory.rational(affine
				.coefficient());

		if (pseudo != null) {
			rationalBound = numberFactory.negate(numberFactory.divide(offset,
					coefficient));
			if (pseudo.type().isInteger()) {
				if (coefficient.signum() >= 0) {
					if (strict)
						result = numberFactory.add(ONE_INT,
								numberFactory.floor(rationalBound));
					else
						result = numberFactory.ceil(rationalBound);
				} else {
					if (strict)
						result = numberFactory.subtract(
								numberFactory.ceil(rationalBound), ONE_INT);
					else
						result = numberFactory.floor(rationalBound);
				}
			} else {
				result = rationalBound;
			}
		}
		return result;
	}

	public Number affineValue(AffineExpression affine, Number pseudoValue) {
		if (affine.pseudo() == null)
			return affine.offset();
		return numberFactory.add(
				numberFactory.multiply(affine.coefficient(), pseudoValue),
				affine.offset());
	}

}
