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
import edu.udel.cis.vsl.sarl.ideal.IF.Primitive;
import edu.udel.cis.vsl.sarl.ideal.IF.PrimitivePower;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;

/**
 * Comparator for ideal numeric expressions. This comparator is very heavily
 * used in most numeric operations (e.g., adding and multiplying polynomials) so
 * performance is critical.
 * 
 * The order is defined as follows. First come all expressions of integer type,
 * then all of real type. Within a type, first all the NTRationalExpression,
 * then everything else. "Everything else" are instances of Polynomial.
 * Polynomials are sorted first by degree: larger degree comes first (since
 * that's the way you typically write them). Given two polynomials of the same
 * degree:
 * 
 * if the two polynomials are monomials of the same degree, compare monics, then
 * constants
 * 
 * to compare two monics of the same degree: use dictionary order on the
 * primitive powers
 * 
 * to compare two primitive power of same degree: compare the bases
 * 
 * If the two polynomials of the same degree are not monomials, then compare
 * their leading terms. If those are equal, move to the next pair of terms. Etc.
 * 
 * TODO: all of these expressions should be assigned order numbers for fast
 * comparisons.
 * 
 * @author siegel
 * 
 */
public class IdealComparator implements Comparator<NumericExpression> {

	private Comparator<SymbolicObject> objectComparator;

	private Comparator<SymbolicType> typeComparator;

	private CommonIdealFactory idealFactory;

	public IdealComparator(CommonIdealFactory idealFactory) {
		this.idealFactory = idealFactory;
		this.objectComparator = idealFactory.objectFactory().comparator();
		this.typeComparator = idealFactory.typeFactory().typeComparator();
	}

	private static boolean debug = false;

	/**
	 * Compares two NumericExpressions that are of the same type.
	 * 
	 * Note: Currently, these expression should be of similar type. An
	 * optimization task here is to improve this method in such a way that it
	 * can compare two numeric expressions of different type.
	 * 
	 * @param o1
	 *            - NumericExpression
	 * @param o2
	 *            - NumericExpression of the same type
	 */
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
	 * <p>
	 * Compares IdealExpressions, placing a total order on the set of all
	 * IdealExpressions.
	 * </p>
	 * 
	 * 
	 * 
	 * <p>
	 * First compare types. Within a type, first all the NTRationalExpression,
	 * then everything else. "Everything else" are instances of Polynomial.
	 * Polynomials are sorted first by degree: larger degree comes first (since
	 * that's the way you typically write them). Given two polynomials of the
	 * same degree:
	 * <ul>
	 * 
	 * <li>if the two polynomials are monomials of the same degree, compare
	 * monics, then constants</li>
	 * 
	 * <li>to compare two monics of the same degree: use dictionary order on the
	 * primitive powers</li>
	 * 
	 * <li>to compare two primitive power of same degree: compare the bases</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Expression of two different types might be compared as follows: if a is
	 * int and b is real, the expression "a>0 && b>0" might be represented by a
	 * sorted list containing the two entries a>0 and b>0. Hence these two
	 * entries will be compared. To compare a>0 and b>0, the comparison first
	 * compares the operators, they are equal (both are >). Then it compares the
	 * arguments, so it will compare a and b. At this point it is comparing two
	 * expressions of different types. As an optimization, consider making a
	 * version of the comparator that assumes the two expressions have the same
	 * type.
	 * </p>
	 */
	public int compareWork(NumericExpression o1, NumericExpression o2) {
		IdealExpression e1 = (IdealExpression) o1;
		IdealExpression e2 = (IdealExpression) o2;
		SymbolicType t1 = e1.type();
		SymbolicType t2 = e2.type();
		int result = typeComparator.compare(t1, t2);

		if (result != 0) {
			return result;
		}
		if (t1.isInteger()) {
			if (o1 instanceof Monic && o2 instanceof Monic)
				return compareMonics((Monic) o1, (Monic) o2);
			else
				return comparePolynomials((Polynomial) e1, (Polynomial) e2);
		} else {
			return compareRationals((RationalExpression) e1,
					(RationalExpression) e2);
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
	 * Compares two monics of the same type.
	 * 
	 * @param m1
	 *            a monic
	 * @param m2
	 *            a monic of the same type as <code>m1</code>
	 * @return a negative integer if m1 precedes m2, 0 if they are equal, else a
	 *         positive integer
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
	 * Compares two numeric primitives of the same type.
	 * 
	 * @param p1
	 *            a numeric primitive
	 * @param p2
	 *            a numeric primitive of same type
	 * @return a negative integer if p1 precedes p2, 0 if they are equals, else
	 *         a positive integer
	 */
	public int comparePrimitives(Primitive p1, Primitive p2) {
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
		return idealFactory.numberFactory().compare(c1.number(), c2.number());
	}

	private int compareRationals(RationalExpression e1, RationalExpression e2) {
		int result = comparePolynomials(e1.numerator(idealFactory),
				e2.numerator(idealFactory));

		if (result != 0)
			return result;
		return comparePolynomials(e1.denominator(idealFactory),
				e2.denominator(idealFactory));
	}

	public Comparator<SymbolicObject> objectComparator() {
		return objectComparator;
	}

}
