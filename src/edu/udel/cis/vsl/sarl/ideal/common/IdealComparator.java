package edu.udel.cis.vsl.sarl.ideal.common;

import java.util.Comparator;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.PrimitivePower;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;

public class IdealComparator implements Comparator<NumericExpression> {

	private Comparator<SymbolicObject> objectComparator;

	private CommonIdealFactory idealFactory;

	public IdealComparator(CommonIdealFactory idealFactory) {
		this.idealFactory = idealFactory;
	}

	private static boolean debug = false;

	@Override
	public int compare(NumericExpression o1, NumericExpression o2) {
		if (debug) {
			int result;

			System.out.print("Comparing " + o1 + " and " + o2 + ": ");
			result = compareWork(o1, o2);
			System.out.println(result);
			System.out.flush();
			return result;
		} else
			return compareWork(o1, o2);
	}

	/**
	 * Compares IdealExpressions. First come all expressions of integer type,
	 * then all of real type. Within a type, first all the NTRationalExpression,
	 * then everything else. "Everything else" are instances of Polynomial.
	 * Polynomials are sorted first by degree: larger degree comes first (since
	 * that's the way you typically write them). Given two polynomials of the
	 * same degree:
	 * 
	 * if the two polynomials are monomials of the same degree, compare monics,
	 * then constants
	 * 
	 * to compare two monics of the same degree: use dictionary order on the
	 * primitive powers
	 * 
	 * to compare two primitive power of same degree: compare the bases
	 */
	public int compareWork(NumericExpression o1, NumericExpression o2) {
		IdealExpression e1 = (IdealExpression) o1;
		IdealExpression e2 = (IdealExpression) o2;
		SymbolicType t1 = e1.type();
		SymbolicType t2 = e2.type();

		if (t1.isInteger()) {
			if (t2.isInteger()) {
				if (o1 instanceof Monic && o2 instanceof Monic)
					return compareMonics((Monic) o1, (Monic) o2);
				else
					return comparePolynomials((Polynomial) e1, (Polynomial) e2);
			} else {
				return -1;
			}
		} else {
			if (t2.isInteger()) {
				return 1;
			} else {
				return compareRationals((RationalExpression) e1,
						(RationalExpression) e2);
			}
		}
	}

	private int comparePolynomials(Polynomial p1, Polynomial p2) {
		int result = p2.degree() - p1.degree();

		if (result != 0)
			return result;

		Iterator<Monomial> monomialIter1 = p1.termMap(idealFactory).values()
				.iterator();
		Iterator<Monomial> monomialIter2 = p2.termMap(idealFactory).values()
				.iterator();

		while (monomialIter1.hasNext()) {
			Monomial monomial1 = monomialIter1.next();

			if (monomialIter2.hasNext()) {
				Monomial monomial2 = monomialIter2.next();

				result = compareMonomials(monomial1, monomial2);
				if (result != 0)
					return result;
			} else {
				return -1;
			}
		}
		if (monomialIter2.hasNext())
			return 1;
		return 0;
	}

	private int compareMonomials(Monomial m1, Monomial m2) {
		int result = m2.degree() - m1.degree();

		if (result != 0)
			return result;
		result = compareMonics(m1.monic(idealFactory), m2.monic(idealFactory));
		if (result != 0)
			return result;
		return compareConstants(m1.monomialConstant(idealFactory),
				m2.monomialConstant(idealFactory));
	}

	/**
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	private int compareMonics(Monic m1, Monic m2) {
		int result = m2.degree() - m1.degree();

		if (result != 0)
			return result;

		Iterator<PrimitivePower> ppIter1 = m1.monicFactors(idealFactory)
				.iterator();
		Iterator<PrimitivePower> ppIter2 = m2.monicFactors(idealFactory)
				.iterator();

		while (ppIter1.hasNext()) {
			PrimitivePower ppower1 = ppIter1.next();
			PrimitivePower ppower2 = ppIter2.next();

			result = comparePrimitives(ppower1.primitive(idealFactory),
					ppower2.primitive(idealFactory));
			if (result != 0)
				return result;
			result = ppower2.degree() - ppower1.degree();
			if (result != 0)
				return result;
		}
		return 0;
	}

	/**
	 * ReducedPolynomial, IdealSymbolicConstant, all others.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public int comparePrimitives(NumericPrimitive p1, NumericPrimitive p2) {
		int result = p1.operator().compareTo(p2.operator());

		if (result != 0)
			return result;
		else {
			int numArgs = p1.numArguments();

			result = numArgs - p2.numArguments();
			if (result != 0)
				return result;
			for (int i = 0; i < numArgs; i++) {
				result = objectComparator.compare(p1.argument(i),
						p2.argument(i));
				if (result != 0)
					return result;
			}
			return 0;
		}
	}

	private int compareConstants(Constant c1, Constant c2) {
		return c1.number().compareTo(c2.number());
	}

	private int compareRationals(RationalExpression e1, RationalExpression e2) {
		int result = comparePolynomials(e1.numerator(idealFactory),
				e2.numerator(idealFactory));

		if (result != 0)
			return result;
		return comparePolynomials(e1.denominator(idealFactory),
				e2.denominator(idealFactory));
	}

	/**
	 * The general comparator is used to compare objects that are not instances
	 * of IdealExpression.
	 * 
	 * @param comparator
	 */
	public void setObjectComparator(Comparator<SymbolicObject> comparator) {
		this.objectComparator = comparator;
	}

	public Comparator<SymbolicObject> objectComparator() {
		return objectComparator;
	}

}
