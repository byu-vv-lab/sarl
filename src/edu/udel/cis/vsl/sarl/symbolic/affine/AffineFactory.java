package edu.udel.cis.vsl.sarl.symbolic.affine;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomialFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class AffineFactory {

	private Map<SymbolicExpressionKey<AffineExpression>, AffineExpression> map = new HashMap<SymbolicExpressionKey<AffineExpression>, AffineExpression>();

	private FactoredPolynomialFactory fpFactory;

	private NumberFactoryIF numberFactory;

	private ConcreteFactory concreteFactory;

	private SymbolicTypeFactory typeFactory;

	private IntegerNumberIF ZERO_INT, ONE_INT;

	private RationalNumberIF ZERO_REAL;

	private AffineExpression zeroIntAffine, zeroRealAffine;

	private SymbolicTypeIF integerType, realType;

	public AffineFactory(FactoredPolynomialFactory fpFactory) {
		this.fpFactory = fpFactory;
		this.concreteFactory = fpFactory.concreteFactory();
		this.numberFactory = concreteFactory.numberFactory();
		this.typeFactory = concreteFactory.typeFactory();
		ZERO_INT = numberFactory.zeroInteger();
		ZERO_REAL = numberFactory.zeroRational();
		ONE_INT = numberFactory.oneInteger();
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
		zeroIntAffine = affine(null, ZERO_INT, ZERO_INT);
		zeroRealAffine = affine(null, ZERO_REAL, ZERO_REAL);
	}

	public AffineExpression affine(FactoredPolynomial pseudo,
			NumberIF coefficient, NumberIF offset) {
		SymbolicTypeIF type = (coefficient instanceof IntegerNumberIF ? integerType
				: realType);

		assert pseudo == null || type.equals(pseudo.type());
		if (type.isInteger()) {
			assert offset instanceof IntegerNumberIF;
		} else {
			assert offset instanceof RationalNumberIF;
		}
		return CommonSymbolicExpression.flyweight(map, new AffineExpression(pseudo,
				coefficient, offset, type));
	}

	public AffineExpression affine(FactoredPolynomial fp) {
		SymbolicTypeIF type = fp.type();
		IntegerNumberIF degree = fp.degree();
		int signum = degree.signum();

		if (signum < 0) { // fp=0
			return (type.isInteger() ? zeroIntAffine : zeroRealAffine);
		} else if (signum == 0) { // fp is constant
			return affine(null, (type.isInteger() ? ZERO_INT : ZERO_REAL), fp
					.factorization().constant().value());
		} else {
			// first, subtract off constant term (if it is non-0).
			// then factor out best you can:
			// if real: factor out leading coefficient (unless it is 1)
			// if int: take gcd of coefficients and factor that out (unless it
			// is 1)
			NumberIF constantTerm = fp.constantTerm();
			FactoredPolynomial difference; // fp-constantTerm
			FactoredPolynomial pseudo;
			NumericConcreteExpressionIF coefficientConcrete;

			if (constantTerm.signum() == 0) {
				difference = fp;
			} else {
				difference = fpFactory.subtract(fp, fpFactory
						.factoredPolynomial(constantTerm));
			}
			coefficientConcrete = difference.factorization().constant();
			pseudo = fpFactory.divide(difference, coefficientConcrete);
			return affine(pseudo, coefficientConcrete.value(), constantTerm);
		}
	}

	// may return null if two expressions are incompatible
	// public AffineExpression add(AffineExpression arg0, AffineExpression arg1)
	// {
	// FactoredPolynomial pseudo0 = arg0.pseudo();
	// FactoredPolynomial pseudo1 = arg1.pseudo();
	// FactoredPolynomial pseudo;
	//
	// if (pseudo0 != null && pseudo1 != null && !pseudo0.equals(pseudo1))
	// return null;
	// if (pseudo0 == null) {
	// pseudo = pseudo1;
	// setCoefficient(pseudo == null ? ZERO_R : that.coefficient);
	// } else if (that.pseudo != null) {
	// setCoefficient(factory.add(coefficient, that.coefficient));
	// }
	// offset = factory.add(offset, that.offset);
	// return true;
	// }

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
	public NumberIF bound(AffineExpression affine, boolean strict) {
		RationalNumberIF rationalBound = null;
		NumberIF result = null;
		FactoredPolynomial pseudo = affine.pseudo();
		RationalNumberIF offset = numberFactory.rational(affine.offset());
		RationalNumberIF coefficient = numberFactory.rational(affine
				.coefficient());

		if (pseudo != null) {
			rationalBound = numberFactory.negate(numberFactory.divide(offset,
					coefficient));
			if (pseudo.type().isInteger()) {
				if (coefficient.signum() >= 0) {
					if (strict)
						result = numberFactory.add(ONE_INT, numberFactory
								.floor(rationalBound));
					else
						result = numberFactory.ceil(rationalBound);
				} else {
					if (strict)
						result = numberFactory.subtract(numberFactory
								.ceil(rationalBound), ONE_INT);
					else
						result = numberFactory.floor(rationalBound);
				}
			} else {
				result = rationalBound;
			}
		}
		return result;
	}

	public NumberIF affineValue(AffineExpression affine, NumberIF pseudoValue) {
		if (affine.pseudo() == null)
			return affine.offset();
		return numberFactory.add(numberFactory.multiply(affine.coefficient(),
				pseudoValue), affine.offset());
	}

}
