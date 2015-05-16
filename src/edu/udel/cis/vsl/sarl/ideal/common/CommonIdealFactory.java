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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Primitive;
import edu.udel.cis.vsl.sarl.ideal.IF.PrimitivePower;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.number.real.Exponentiator;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

/**
 * <p>
 * Implementation of a {@link NumericExpressionFactory} based on the theories of
 * mathematical real and integer arithmetic.
 * </p>
 *
 * <p>
 * The numeric symbolic expressions consumed and produced by this factory must
 * be instances of {@link IdealExpression}, the root of the ideal expression
 * type hierarchy. This factory guarantees the following invariants on ideal
 * expressions:
 * </p>
 * 
 * <p>
 * A concrete value that is 1 (integer or real) is represented as an instance of
 * {@link One}.
 * </p>
 * 
 * <p>
 * All concrete values are instances of {@link Constant}. A concrete value that
 * is not 1 is furthermore an instance of {@link NTConstant} (short for
 * <i>non-trivial constant</i>.
 * </p>
 * 
 * <p>
 * A {@link Primitive} numeric expression is an expression of integer or real
 * type which is not concrete and cannot be expressed as a sum or product or
 * quotient of other expressions. Examples include: symbolic constants, array
 * read expressions of integer or real type, tuple read expressions of integer
 * or real type, function applications for functions returning integer or real.
 * Every {@link Primitive} numeric expression will be an instance of
 * {@link NumericPrimitive}.
 * </p>
 * 
 * <p>
 * Any value which is the result of raising a primitive expression to a concrete
 * positive integer exponent is an instance of {@link PrimitivePower}. If the
 * exponent is 2 or greater, it is an instance of {@link NTPrimitivePower}.
 * </p>
 * 
 * <p>
 * Any product of {@link PrimitivePower}s is a {@link Monic}. If there are at
 * least two different {@link Primitive}s involved in the product, it is
 * furthermore an instance of {@link NTMonic}.
 * </p>
 * 
 * <p>
 * Any product of a {@link Constant} and a {@link Monic} is a {@link Monomial}.
 * If the {@link Constant} is neither 0 nor 1, and the {@link Monic} is not 1,
 * it is also an instance of {@link NTMonomial}.
 * </p>
 * 
 * <p>
 * Any sum of {@link Monomial}s is a {@link Polynomial}. If that sum involves at
 * least two terms with different monics, it is furthermore an instance of
 * {@link NTPolynomial}.
 * </p>
 * 
 * <p>
 * Every polynomial has an associated <strong>factorization</strong>. The
 * factors are not necessarily irreducible. The factorization is represented as
 * an instance of {@link Monomial}. The primitives occurring in the monic
 * component of the monomial are special: they may be instances of
 * {@link ReducedPolynomial}. A {@link ReducedPolynomial} "wraps" a polynomial
 * into a numeric primitive. The polynomial so wrapped must satisfy: (1) if the
 * type is real then the leading coefficient of the polynomial is 1, (2) if the
 * type is integer then the leading coefficient is positive and the GCD of the
 * absolute values of the coefficients is 1, and (3) there is no known
 * nontrivial factorization of the polynomial. The method
 * {@link Monomial#expand(IdealFactory)} returns an equivalent polynomial that
 * contains no instances of {@link ReducedPolynomial}.
 * </p>
 * 
 * <p>
 * A {@link RationalExpression} is the quotient of two polynomials. The
 * factorizations of the numerator and denominator will have no common factors.
 * Any {@link RationalExpression} of integer type is a {@link Polynomial}. (The
 * result of integer division of two integer polynomials may be a
 * {@link NumericPrimitive} with operator {@link SymbolicOperator#INT_DIVIDE}.)
 * An {@link NTRationalExpression} always has real type and the degree of the
 * denominator is at least 1.
 * </p>
 *
 * <p>
 * A relational numeric expression will always be in one of the following forms:
 * 
 * <ul>
 * <li><code>0&lt;p</code></li>
 * <li><code>0&le;p</code></li>
 * <li><code>0=p</code></li>
 * <li><code>0&ne;p</code></li>
 * </ul>
 * 
 * where <code>p</code> is a {@link Polynomial}.
 * </p>
 * 
 * @author Stephen F. Siegel
 */
public class CommonIdealFactory implements IdealFactory {

	/**
	 * The number factory used by this ideal factory to create and manipulate
	 * infinite-precision concrete integer and rational numbers, instances of
	 * {@link Number}, {@link IntegerNumber}, and {@link RationalNumber}.
	 */
	private NumberFactory numberFactory;

	/**
	 * The boolean expression factory used by this ideal factory to create and
	 * manipulate boolean expressions, instances of {@link BooleanExpression}
	 */
	private BooleanExpressionFactory booleanFactory;

	/**
	 * The object factory used by this ideal factory to manipulate symbolic
	 * objects, instances of {@link SymbolicObject}.
	 */
	private ObjectFactory objectFactory;

	/**
	 * The symbolic type factory used by this ideal factory to create and
	 * manipulate symbolic types, instances of {@link SymbolicType}.
	 */
	private SymbolicTypeFactory typeFactory;

	/**
	 * The factory used to manage symbolic collections, instances of
	 * {@link SymbolicCollection}.
	 */
	private CollectionFactory collectionFactory;

	/**
	 * The object used to compare ideal symbolic expressions and thereby place a
	 * total order on them.
	 */
	private IdealComparator comparator;

	/**
	 * The symbolic map with no entries.
	 */
	private SymbolicMap<?, ?> emptyMap;

	/**
	 * The (ideal mathematical) integer type.
	 */
	private SymbolicType integerType;

	/**
	 * The (ideal mathematical) real type.
	 */
	private SymbolicType realType;

	/**
	 * The integer 1, which in the ideal factory is represented as an instance
	 * of {@link One}. A special class is needed here because 1 is both a
	 * {@link Constant} and a {@link Monic} (the empty monic).
	 */
	private One oneInt;

	/**
	 * The real number 1, which in the ideal factory is represented as an
	 * instance of {@link One}. A special class is needed here because 1 is both
	 * a {@link Constant} and a {@link Monic} (the empty monic).
	 */
	private One oneReal;

	/**
	 * The {@link IntObject} wrapping the Java int 1.
	 */
	private IntObject oneIntObject;

	/**
	 * The integer 0 as a symbolic expression, which in this ideal universe is
	 * an instance of {@link Constant}.
	 */
	private Constant zeroInt;

	/**
	 * The real number 0 as a symbolic expression, which in this ideal universe
	 * is an instance of {@link Constant}.
	 */
	private Constant zeroReal;

	/**
	 * The boolean symbolic expression "true".
	 */
	private BooleanExpression trueExpr;

	/**
	 * The boolean symbolic expression "false".
	 */
	private BooleanExpression falseExpr;

	/**
	 * An object used to add two monomials over the same monic.
	 */
	private MonomialAdder monomialAdder;

	/**
	 * An object used to negate (multiply by -1) a monomial.
	 */
	private MonomialNegater monomialNegater;

	/**
	 * An object used to multiply two primitive powers over the same primitive
	 * base.
	 */
	private PrimitivePowerMultiplier primitivePowerMultipler;

	/**
	 * An object used to raise integer numeric expressions to some concrete
	 * nonnegative integer power, efficiently.
	 */
	private Exponentiator<NumericExpression> integerExponentiator;

	/**
	 * An object used to raise real numeric expressions to some concrete
	 * nonnegative integer power, efficiently.
	 */
	private Exponentiator<NumericExpression> realExponentiator;

	/**
	 * Constructs new factory based on the given factories.
	 * 
	 * @param numberFactory
	 *            the number factory used by this ideal factory to create and
	 *            manipulate infinite-precision concrete integer and rational
	 *            numbers, instances of {@link Number}, {@link IntegerNumber},
	 *            and {@link RationalNumber}
	 * @param objectFactory
	 *            the object factory used by this ideal factory to manipulate
	 *            symbolic objects, instances of {@link SymbolicObject}.
	 * @param typeFactory
	 *            the symbolic type factory used by this ideal factory to create
	 *            and manipulate symbolic types, instances of
	 *            {@link SymbolicType}
	 * @param collectionFactory
	 *            the factory used to manage symbolic collections, instances of
	 *            {@link SymbolicCollection}
	 * @param booleanFactory
	 *            the boolean expression factory used by this ideal factory to
	 *            create and manipulate boolean expressions, instances of
	 *            {@link BooleanExpression}
	 */
	public CommonIdealFactory(NumberFactory numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory,
			BooleanExpressionFactory booleanFactory) {
		this.numberFactory = numberFactory;
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.collectionFactory = collectionFactory;
		this.booleanFactory = booleanFactory;
		this.trueExpr = booleanFactory.trueExpr();
		this.falseExpr = booleanFactory.falseExpr();
		this.comparator = new IdealComparator(this);
		this.integerType = typeFactory.integerType();
		this.realType = typeFactory.realType();
		this.oneIntObject = objectFactory.oneIntObj();
		this.emptyMap = collectionFactory.emptySortedMap(comparator);
		this.oneInt = objectFactory.canonic(new One(integerType, objectFactory
				.numberObject(numberFactory.oneInteger())));
		this.oneReal = objectFactory.canonic(new One(realType, objectFactory
				.numberObject(numberFactory.oneRational())));
		this.zeroInt = canonicIntConstant(0);
		this.zeroReal = canonicRealConstant(0);
		this.monomialAdder = new MonomialAdder(this);
		this.monomialNegater = new MonomialNegater(this);
		this.primitivePowerMultipler = new PrimitivePowerMultiplier(this);
		this.integerExponentiator = new Exponentiator<NumericExpression>(
				new BinaryOperator<NumericExpression>() {

					@Override
					public NumericExpression apply(NumericExpression x,
							NumericExpression y) {
						return multiply((Polynomial) x, (Polynomial) y);
					}

				}, oneInt);
		this.realExponentiator = new Exponentiator<NumericExpression>(
				new BinaryOperator<NumericExpression>() {

					@Override
					public NumericExpression apply(NumericExpression x,
							NumericExpression y) {
						return multiplyRational((RationalExpression) x,
								(RationalExpression) y);
					}

				}, oneReal);
	}

	// ************************** Private Methods *************************

	/**
	 * Returns the canonic {@link Constant} of integer type with the value
	 * specified.
	 * 
	 * @param value
	 *            any Java <code>int</code>
	 * @return the canonic integer {@link Constant} wrapping that value
	 */
	private Constant canonicIntConstant(int value) {
		return objectFactory.canonic(intConstant(value));
	}

	/**
	 * Returns a {@link Constant} of real type with value the given integer.
	 * 
	 * @param value
	 *            any Java <code>int</code>
	 * @return a real {@link Constant} wrapping that value
	 */
	private Constant realConstant(int value) {
		if (value == 1)
			return oneReal;
		return new NTConstant(realType,
				objectFactory.numberObject(numberFactory
						.integerToRational(numberFactory.integer(value))));
	}

	/**
	 * Returns the canonic {@link Constant} of real type with value the given
	 * integer.
	 * 
	 * @param value
	 *            any Java <code>int</code>
	 * @return the canonic real {@link Constant} wrapping that value
	 */
	private Constant canonicRealConstant(int value) {
		return objectFactory.canonic(realConstant(value));
	}

	/**
	 * Returns a {@link Constant} wrapping the given number object. The constant
	 * will have either integer or real type, depending on the kind of the
	 * number object.
	 * 
	 * @param object
	 *            any number object
	 * @return an instance of {@link Constant} corresonding to that number
	 */
	private Constant constant(NumberObject object) {
		if (object.isOne())
			return object.isInteger() ? oneInt : oneReal;
		return new NTConstant(object.isInteger() ? integerType : realType,
				object);
	}

	/**
	 * Constructs a non-trivial primitive power with given base and exponent.
	 * 
	 * @param primitive
	 *            a non-<code>null</code> instance of {@link Primitive} which
	 *            will be the base in the new primitive power expression
	 * @param exponent
	 *            the exponent in the new expression, which must be an integer
	 *            greater than or equal to 2
	 * @return the non-trivial primitive power as specified
	 */
	private NTPrimitivePower ntPrimitivePower(Primitive primitive,
			IntObject exponent) {
		return new NTPrimitivePower(primitive, exponent);
	}

	/**
	 * Constructs a non-trivial monic.
	 * 
	 * @param type
	 *            either the integer or the real type
	 * @param monicMap
	 *            a monic map with at least two entries; this maps a primitive
	 *            to a power of that primitive; all keys and values must have
	 *            type consistent with <code>type</code>
	 * @return the monic which represents the product of the values of the
	 *         <code>monicMap</code>
	 */
	private NTMonic ntMonic(SymbolicType type,
			SymbolicMap<NumericPrimitive, PrimitivePower> monicMap) {
		return new NTMonic(type, monicMap);
	}

	/**
	 * Returns a (possibly trivial) monic as specified. If the given monic map
	 * is empty, this returns 1 (an instance of {@link One} of the appropriate
	 * type). If the monic map has a single entry, this returns the value for
	 * that entry, which is a {@link PrimitivePower}. Otherwise, returns a
	 * non-trivial monic (instance of {@link NTMonic}).
	 * 
	 * @see #ntMonic(SymbolicType, SymbolicMap)
	 * 
	 * @param type
	 *            either integer or real type
	 * @param monicMap
	 *            a monic map with any number of entries; this maps a primitive
	 *            to a power of that primitive; all keys and values must have
	 *            type consistent with <code>type</code>
	 * @return instance of {@link Monic} corresponding to arguments as described
	 *         above
	 */
	private Monic monic(SymbolicType type,
			SymbolicMap<NumericPrimitive, PrimitivePower> monicMap) {
		if (monicMap.isEmpty())
			return one(type);
		if (monicMap.size() == 1)
			return monicMap.iterator().next();
		return ntMonic(type, monicMap);
	}

	/**
	 * Constructs an instance of {@link NTMonomial}, a non-trivial monomial.
	 * 
	 * @param constant
	 *            a {@link Constant} which is neither 0 nor 1
	 * @param monic
	 *            a non-empty {@link Monic} (i.e., not 1)
	 * @return new instance of {@link NTMonomial} as specified
	 */
	private NTMonomial ntMonomial(Constant constant, Monic monic) {
		return new NTMonomial(constant, monic);
	}

	/**
	 * Returns a new reduced polynomial from the given type and term map. The
	 * precondition is that the polynomial specified by the sum of the monomials
	 * of the term map is reduced. This will not be checked.
	 * 
	 * @param type
	 *            the numeric type of the polynomial
	 * @param termMap
	 *            the terms of the polynomial expressed as a map in which a
	 *            monic maps to the unique monomial term involving that monic;
	 *            all of the terms must have the specified type
	 * @return the reduced polynomial as specified
	 */
	private ReducedPolynomial reducedPolynomial(SymbolicType type,
			SymbolicMap<Monic, Monomial> termMap) {
		return new ReducedPolynomial(type, termMap);
	}

	/**
	 * Constructs new instance of {@link NTPolynomial}. Precondition:
	 * <code>termMap</code> contains at least two terms; the factorization is
	 * indeed a factorization of the polynomial.
	 * 
	 * @param termMap
	 *            the terms of the polynomial expressed as a map in which a
	 *            monic maps to the unique monomial term involving that monic;
	 *            all of the terms must have the specified type
	 * @param factorization
	 *            a factorization of the polynomial
	 * @return new instance of {@link NTPolynomial} as specified
	 */
	private NTPolynomial ntPolynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization) {
		return new NTPolynomial(termMap, factorization);
	}

	/**
	 * Computes the a constant c as follows: if the type is real, c is the
	 * coefficient of the leading term (which is a term of highest degree); if
	 * the type is integer, this is the GCD of absolute values of the
	 * coefficients of the terms. This constant is typically uses as the
	 * constant factor in a factorization of the polynomial represented by the
	 * term map.
	 * 
	 * @param type
	 *            integer or real type
	 * @param termMap
	 *            a non-<code>null</code>, non-empty term map: map from monics x
	 *            to monomials y such that the monic of y equals x.
	 * @return the constant factor as specified above, which will be a non-zero
	 *         constant of the specified type
	 */
	private Constant getConstantFactor(SymbolicType type,
			SymbolicMap<Monic, Monomial> termMap) {
		if (type.isReal())
			return termMap.getFirst().monomialConstant(this);
		else {
			Iterator<Monomial> monomialIter = termMap.values().iterator();
			IntegerNumber gcd = (IntegerNumber) monomialIter.next()
					.monomialConstant(this).number();

			if (gcd.signum() < 0)
				gcd = numberFactory.negate(gcd);
			while (!gcd.isOne() && monomialIter.hasNext())
				gcd = numberFactory.gcd(
						gcd,
						(IntegerNumber) numberFactory.abs(monomialIter.next()
								.monomialConstant(this).number()));
			return constant(gcd);
		}
	}

	/**
	 * Constructs a {@link Polynomial} based on the given term map, using a
	 * "trivial" factorization. The constant factor c in the trivial
	 * factorization is obtained in the real type by taking the leading
	 * coefficient; in the integer case by taking the GCD of the absolute values
	 * of the coefficients. The factorization is then "c (p/c)^1", where p is
	 * the polynomial defined by the <code>termMap</code>.
	 * 
	 * @param type
	 *            integer or real type
	 * @param termMap
	 *            term map consistent with type
	 * @return polynomial with trivial factorization
	 */
	private Polynomial polynomialWithTrivialFactorization(SymbolicType type,
			SymbolicMap<Monic, Monomial> termMap) {
		Monomial factorization;
		Constant c = getConstantFactor(type, termMap);

		if (c.isOne())
			factorization = reducedPolynomial(type, termMap);
		else
			factorization = monomial(c,
					reducedPolynomial(type, divide(termMap, c)));
		return polynomial(termMap, factorization);
	}

	/**
	 * Constructs new instance of {@link NTRationalExpression}. Nothing is
	 * checked.
	 * 
	 * <p>
	 * Preconditions: numerator is not 0. If real type, denominator has degree
	 * at least 1 and leading coefficient 1. The numerator and denominator have
	 * no common factors in their factorizations.
	 * </p>
	 * 
	 * @param numerator
	 *            the polynomial to use as numerator
	 * @param denominator
	 *            the polynomial to use as denominator
	 * @return rational expression p/q
	 */
	private NTRationalExpression ntRationalExpression(Polynomial numerator,
			Polynomial denominator) {
		return new NTRationalExpression(numerator, denominator);
	}

	/**
	 * Extracts the common factors from two monics. Given monics f1 and f2, this
	 * computes the monic p such that: f1=p*g1, f2=p*g2, and g1 and g2 have no
	 * primitives in common.
	 * 
	 * @param fact1
	 *            a non-<code>null</code> monic
	 * @param fact2
	 *            a monic of same type as <code>fact1</code>
	 * @return array of length 3 consisting of p, g1, and g2, in that order
	 */
	private Monic[] extractCommonality(Monic fact1, Monic fact2) {
		SymbolicType type = fact1.type();
		SymbolicMap<NumericPrimitive, PrimitivePower> map1 = fact1
				.monicFactors(this);
		SymbolicMap<NumericPrimitive, PrimitivePower> map2 = fact2
				.monicFactors(this);
		SymbolicMap<NumericPrimitive, PrimitivePower> commonMap = collectionFactory
				.emptySortedMap();
		SymbolicMap<NumericPrimitive, PrimitivePower> newMap1 = map1, newMap2 = map2;

		for (Entry<NumericPrimitive, PrimitivePower> entry : map1.entries()) {
			NumericPrimitive base = entry.getKey();
			PrimitivePower ppower1 = entry.getValue();
			PrimitivePower ppower2 = map2.get(base);

			if (ppower2 != null) {
				IntObject exponent1 = ppower1.primitivePowerExponent(this);
				IntObject exponent2 = ppower2.primitivePowerExponent(this);
				IntObject minExponent = exponent1.minWith(exponent2);
				IntObject newExponent1 = exponent1.minus(minExponent);
				IntObject newExponent2 = exponent2.minus(minExponent);

				commonMap = commonMap.put(base,
						primitivePower(base, minExponent));
				if (newExponent1.isPositive())
					newMap1 = newMap1.put(base,
							primitivePower(base, newExponent1));
				else
					newMap1 = newMap1.remove(base);
				if (newExponent2.isPositive())
					newMap2 = newMap2.put(base,
							primitivePower(base, newExponent2));
				else
					newMap2 = newMap2.remove(base);
			}
		}
		return new Monic[] { monic(type, commonMap), monic(type, newMap1),
				monic(type, newMap2) };
	}

	/**
	 * Given two factorizations f1 and f2, this returns an array of length 3
	 * containing 3 factorizations a, g1, g2 (in that order), satisfying
	 * f1=a*g1, f2=a*g2, g1 and g2 have no factors in common, a is a monic
	 * factorization (its constant is 1).
	 * 
	 * @param fact1
	 *            a factorization
	 * @param fact2
	 *            a factorization of the same type
	 * @return the array {a,g1,g2}
	 */
	private Monomial[] extractCommonality(Monomial fact1, Monomial fact2) {
		Monic[] monicTriple = extractCommonality(fact1.monic(this),
				fact2.monic(this));

		return new Monomial[] { monicTriple[0],
				monomial(fact1.monomialConstant(this), monicTriple[1]),
				monomial(fact2.monomialConstant(this), monicTriple[2]) };
	}

	/**
	 * Computes the polynomial term map which represents the sum of the two
	 * given ones. Specifically: if a monic appears as a key in one map and not
	 * the other, the entry for that monic occurs in the result exactly as in
	 * the original. If the monic appears as a key in both maps, the
	 * coefficients are added. If the result is 0, no entry for that monic will
	 * appear in the result. Otherwise, an entry for that monic will appear in
	 * the result with the value the sum of the two values.
	 * 
	 * @param termMap1
	 *            a non-<code>null</code> polynomial term map
	 * @param termMap2
	 *            a polynomial term maps of the same type as
	 *            <code>termMap1</code>
	 * @return
	 */
	private SymbolicMap<Monic, Monomial> add(
			SymbolicMap<Monic, Monomial> termMap1,
			SymbolicMap<Monic, Monomial> termMap2) {
		return termMap1.combine(monomialAdder, termMap2);
	}

	/**
	 * Adds two polynomials that have no common primitives in their
	 * factorizations.
	 * 
	 * @param p1
	 *            a non-<code>null</code> {@link Polynomial}
	 * @param p2
	 *            a non-<code>null</code> {@link Polynomial} of same type as
	 *            <code>p1</code> and such that the factorizations of
	 *            <code>p1</code> and <code>p2</code> have no primitives in
	 *            common
	 * @return the sum p1+p2, with the trivial factorization
	 */
	private Polynomial addNoCommon(Polynomial p1, Polynomial p2) {
		SymbolicType type = p1.type();
		SymbolicMap<Monic, Monomial> newMap = add(p1.termMap(this),
				p2.termMap(this));

		if (newMap.isEmpty())
			return zero(type);
		if (newMap.size() == 1)
			return newMap.getFirst();
		else
			return polynomialWithTrivialFactorization(type, newMap);
	}

	/**
	 * Adds two rational expressions. The factorizations are used so that in the
	 * resulting rational expression, the numerator and denominator have not
	 * common factors.
	 * 
	 * @param r1
	 *            a non-<code>null</code> rational expression
	 * @param r2
	 *            a non-<code>null</code> rational expression of the same type
	 *            as <code>r1</code>
	 * @return the sum r1+r2
	 */
	private RationalExpression addRational(RationalExpression r1,
			RationalExpression r2) {
		Polynomial num1 = r1.numerator(this);
		Polynomial num2 = r2.numerator(this);
		Polynomial den1 = r1.denominator(this);
		Polynomial den2 = r2.denominator(this);
		Monomial fact1 = den1.factorization(this);
		Monomial fact2 = den2.factorization(this);
		Monomial[] triple = extractCommonality(fact1, fact2);
		// [a,g1,g2]: f1=a*g1, f2=a*g2
		// n1/d1+n2/d2=n1/rd1+n2/rd2=(n1d2+n2d1)/rd1d2
		Polynomial common = triple[0].expand(this);
		Polynomial d1 = triple[1].expand(this);
		Polynomial d2 = triple[2].expand(this);
		Polynomial denominator = multiply(common, multiply(d1, d2));
		Polynomial numerator = add(multiply(num1, d2), multiply(num2, d1));

		return divide(numerator, denominator);
	}

	/**
	 * Multiplies two constants.
	 * 
	 * @param c1
	 *            a non-<code>null</code> instance of {@link Constant}
	 * @param c2
	 *            a non-<code>null</code> instance of {@link Constant} of the
	 *            same type as <code>c1</code>
	 * @return the product c1*c2
	 */
	private Constant multiply(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.multiply(
				c1.number(), c2.number())));
	}

	/**
	 * Multiplies every term in a term map by the given non-0 constant. Note the
	 * original map is not modified: it is a {@link SymbolicMap}, which is a
	 * kind of {@link SymbolicObject}, and is therefore immutable.
	 * 
	 * @param constant
	 *            a non-0 non-<code>null</code> {@link Constant}
	 * @param termMap
	 *            a polynomial term map of the same type as
	 *            <code>constant</code>
	 * @return the term map obtained by multiplying each term by
	 *         <code>constant</code>
	 */
	private SymbolicMap<Monic, Monomial> multiply(Constant constant,
			SymbolicMap<Monic, Monomial> termMap) {
		MonomialMultiplier multiplier = new MonomialMultiplier(this,
				constant.number());

		return termMap.apply(multiplier);
	}

	/**
	 * Multiplies a {@link Constant} and a {@link Polynomial}.
	 * 
	 * @param constant
	 *            a non-<code>null</code> {@link Constant}
	 * @param polynomial
	 *            a non-<code>null</code> {@link Polynomial} of the same type as
	 *            <code>constant</code>
	 * @return the polynomial resulting from multiplying each term by the
	 *         constant
	 */
	private Polynomial multiply(Constant constant, Polynomial polynomial) {
		if (constant.isZero())
			return constant;
		if (constant.isOne())
			return polynomial;
		else {
			SymbolicMap<Monic, Monomial> oldTermMap = polynomial.termMap(this);
			SymbolicMap<Monic, Monomial> newTermMap = multiply(constant,
					oldTermMap);
			Monomial oldFactorization = polynomial.factorization(this);
			Monomial newFactorization = monomial(
					multiply(constant, oldFactorization.monomialConstant(this)),
					oldFactorization.monic(this));

			return polynomial(newTermMap, newFactorization);
		}
	}

	/**
	 * Multiplies two {@link Monic}s.
	 * 
	 * @param monic1
	 *            a non-<code>null</code> {@link Monic}
	 * @param monic2
	 *            a non-<code>null</code> {@link Monic} of the same type as
	 *            <code>monic1</code>
	 * @return the product of the two monics
	 */
	private Monic multiply(Monic monic1, Monic monic2) {
		return monic(
				monic1.type(),
				monic1.monicFactors(this).combine(primitivePowerMultipler,
						monic2.monicFactors(this)));
	}

	/**
	 * Multiplies two {@link Monomial}s.
	 * 
	 * @param m1
	 *            a non-<code>null</code> {@link Monomial}
	 * @param m2
	 *            a non-<code>null</code> {@link Monomial} of the same type as
	 *            <code>m1</code>
	 * @return the product of the two monomials
	 */
	private Monomial multiply(Monomial m1, Monomial m2) {
		return monomial(
				multiply(m1.monomialConstant(this), m2.monomialConstant(this)),
				multiply(m1.monic(this), m2.monic(this)));
	}

	/**
	 * <p>
	 * Multiplies two term maps, returning a term map. The product of two term
	 * maps is defined by considering each to be a sum its entries.
	 * </p>
	 * 
	 * <p>
	 * Implementation note: the trick to performance is to do perform all
	 * intermediate computations using mutable data structures, and only wrap
	 * things up into the immutable structures at the end.
	 * </p>
	 * 
	 * @param termMap1
	 *            a polynomial term map
	 * @param termMap2
	 *            a polynomial term map of same type as <code>termMap1</code>
	 * @return the term map that results from multiplying the two given ones as
	 *         if they both were sums, which results in a term map using the
	 *         distributive property
	 */
	private SymbolicMap<Monic, Monomial> multiply(
			SymbolicMap<Monic, Monomial> termMap1,
			SymbolicMap<Monic, Monomial> termMap2) {
		HashMap<Monic, Constant> productMap = new HashMap<Monic, Constant>();
		SymbolicMap<Monic, Monomial> result = emptyMap();

		for (Entry<Monic, Monomial> entry1 : termMap1.entries())
			for (Entry<Monic, Monomial> entry2 : termMap2.entries()) {
				Monic monicProduct = multiply(entry1.getKey(), entry2.getKey());
				Constant constantProduct = multiply(entry1.getValue()
						.monomialConstant(this), entry2.getValue()
						.monomialConstant(this));
				Constant oldConstant = productMap.get(monicProduct);
				Constant newConstant = oldConstant == null ? constantProduct
						: add(oldConstant, constantProduct);

				if (newConstant.isZero())
					productMap.remove(monicProduct);
				else
					productMap.put(monicProduct, newConstant);
			}
		for (Entry<Monic, Constant> entry : productMap.entrySet()) {
			Monic monic = entry.getKey();

			result = result.put(monic, monomial(entry.getValue(), monic));
		}
		return result;
	}

	/**
	 * Multiplies two rational expressions.
	 * 
	 * @param r1
	 *            a non-<code>null</code> {@link RationalExpression}
	 * @param r2
	 *            a non-<code>null</code> {@link RationalExpression} of the same
	 *            type as <code>r1</code>
	 * @return the product of <code>r1</code> and <code>r2</code>
	 */
	private RationalExpression multiplyRational(RationalExpression r1,
			RationalExpression r2) {
		// (n1/d1)*(n2/d2)
		if (r1.isZero())
			return r1;
		if (r2.isZero())
			return r2;
		if (r1.isOne())
			return r2;
		if (r2.isOne())
			return r1;
		return divide(multiply(r1.numerator(this), r2.numerator(this)),
				multiply(r1.denominator(this), r2.denominator(this)));
	}

	/**
	 * Divides two constants. The constants must have the same type. If the type
	 * is integer, integer division is performed.
	 * 
	 * @param c1
	 *            a constant
	 * @param c2
	 *            a constant of the same type as c1
	 * @return the constant c1/c2
	 */
	private Constant divide(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.divide(
				c1.number(), c2.number())));
	}

	/**
	 * Divides all the monomials in the termMap by the non-zero constant. The
	 * constant and all of the monomials must have the same type. If the type is
	 * integer, integer division is performed.
	 * 
	 * @param termMap
	 *            a polynomial term map
	 * @param constant
	 *            a constant
	 * @return termMap resulting from dividing all monomials by the constant
	 */
	private SymbolicMap<Monic, Monomial> divide(
			SymbolicMap<Monic, Monomial> termMap, Constant constant) {
		MonomialDivider divider = new MonomialDivider(this, constant.number());

		return termMap.apply(divider);
	}

	/**
	 * Divides the monomial by the non-zero constant. The monomial and constant
	 * must have the same type. If type is integer, integer division is
	 * performed on the coefficient.
	 * 
	 * @param monomial
	 *            a monomial
	 * @param constant
	 *            a non-zero constant
	 * @return the quotient monomial/constant
	 */
	private Monomial divide(Monomial monomial, Constant constant) {
		return monomial(divide(monomial.monomialConstant(this), constant),
				monomial.monic(this));
	}

	/**
	 * Returns 1/r, where r is a rational expression (which must necessarily
	 * have real type).
	 * 
	 * @param r
	 *            a rational expression
	 * @return 1/r
	 */
	private RationalExpression invert(RationalExpression r) {
		return divide(r.denominator(this), r.numerator(this));
	}

	/**
	 * Division of two rational expressions (which must necessarily have real
	 * type).
	 * 
	 * @param r1
	 *            a rational expression
	 * @param r2
	 *            a rational expression
	 * @return r1/r2 as rational expression
	 */
	private RationalExpression divide(RationalExpression r1,
			RationalExpression r2) {
		return multiplyRational(r1, invert(r2));
	}

	/**
	 * Performs integer division of two integer constants.
	 * 
	 * @param c1
	 *            a non-<code>null</code> constant of integer type
	 * @param c2
	 *            a non-<code>null</code> non-0 constant of integer type
	 * @return the result of integer division c1/c2
	 */
	private Constant intDivideConstants(Constant c1, Constant c2) {
		return constant(numberFactory.divide((IntegerNumber) c1.number(),
				(IntegerNumber) c2.number()));
	}

	/**
	 * Computes the integer modulus of two integer constants.
	 * 
	 * @param c1
	 *            a non-<code>null</code> constant of integer type
	 * @param c2
	 *            a non-<code>null</code> non-0 constant of integer type
	 * @return the result of integer modulus c1%c2
	 */
	private Constant intModuloConstants(Constant c1, Constant c2) {
		return constant(numberFactory.mod((IntegerNumber) c1.number(),
				(IntegerNumber) c2.number()));
	}

	/**
	 * Given two non-zero polynomials p1 and p2 of integer type, factor out
	 * common factors, including a positive constant. Returns a triple (r,q1,q2)
	 * of Polynomials such that:
	 * 
	 * <ul>
	 * <li>p1=r*q1 and p2=r*q2</li>
	 * <li>the GCD of the absolute values of all the coefficients in q1 and q2
	 * together is 1</li>
	 * <li>the factorizations of q1 and q2 have no common factors</li>
	 * <li>the leading coefficient of r is positive</li>
	 * </ul>
	 * 
	 * This is used in integer division and modulus operations. For integer
	 * division: p1/p2 = q1/q2. For modulus: p1%p2 = r*(q1%q2).
	 * 
	 * @param poly1
	 *            a non-0 polynomial of integer type
	 * @param poly2
	 *            a non-0 polynomial of integer type
	 * @return the triple (r,q1,q2) described above
	 */
	private Polynomial[] intFactor(Polynomial poly1, Polynomial poly2) {
		Monomial fact1 = poly1.factorization(this);
		Monomial fact2 = poly2.factorization(this);
		Monomial[] triple = extractCommonality(fact1, fact2);
		Polynomial r, q1, q2;
		IntegerNumber c1, c2;

		if (triple[0].isOne()) {
			q1 = poly1;
			q2 = poly2;
			r = oneInt;
		} else {
			q1 = triple[1].expand(this);
			fact1 = q1.factorization(this);
			q2 = triple[2].expand(this);
			fact2 = q2.factorization(this);
			r = triple[0].expand(this);
		}
		c1 = (IntegerNumber) numberFactory.abs(fact1.monomialConstant(this)
				.number());
		c2 = (IntegerNumber) numberFactory.abs(fact2.monomialConstant(this)
				.number());
		if (!c1.isOne() && !c2.isOne()) {
			IntegerNumber gcd = numberFactory.gcd(c1, c2);

			if (!gcd.isOne()) {
				Constant gcdConstant = constant(gcd);

				q1 = divide(q1, gcdConstant);
				q2 = divide(q2, gcdConstant);
				r = multiply(gcdConstant, r);
			}
		}
		return new Polynomial[] { r, q1, q2 };
	}

	/**
	 * Divides two polynomials of integer type. This will always return a
	 * {@link Polynomial}, never a non-polynomial {@link RationalExpression}. In
	 * the worst case (if the denominator does not evenly divide the numerator),
	 * the result will be a primitive expression ({@link NumericPrimitive}) in
	 * which the operator is {@link SymbolicOperator#INT_DIVIDE}.
	 * 
	 * Integer division D: assume all terms positive. (ad)/(bd) = a/b
	 * 
	 * @param numerator
	 *            polynomial of integer type
	 * @param denominator
	 *            polynomial of integer type
	 * @return result of division as Polynomial, which might be a new primitive
	 *         expression
	 */
	private Polynomial intDividePolynomials(Polynomial numerator,
			Polynomial denominator) {
		assert numerator.type().isInteger();
		assert denominator.type().isInteger();
		if (numerator.isZero())
			return numerator;
		if (denominator.isOne())
			return numerator;
		if (numerator instanceof Constant && denominator instanceof Constant)
			return intDivideConstants((Constant) numerator,
					(Constant) denominator);
		else {
			Polynomial[] triple = intFactor(numerator, denominator);

			numerator = triple[1];
			denominator = triple[2];
			if (denominator.isOne())
				return numerator;
			if (numerator instanceof Constant
					&& denominator instanceof Constant)
				return intDivideConstants((Constant) numerator,
						(Constant) denominator);
			return expression(SymbolicOperator.INT_DIVIDE, integerType,
					numerator, denominator);
		}
	}

	/**
	 * <p>
	 * Computes the integer modulus of two polynomials of integer type.
	 * </p>
	 * 
	 * <p>
	 * Precondition: this method assumes the numerator is nonnegative and
	 * denominator is positive. The behavior is undefined otherwise.
	 * </p>
	 * 
	 * <p>
	 * Implementation note: The following identity is used:
	 * 
	 * <pre>
	 * (ad)%(bd) = (a%b)d
	 * </pre>
	 * 
	 * Example : (2u)%2 = (u%1)2 = 0.
	 * </p>
	 * 
	 * @param numerator
	 *            an integer polynomial assumed to be nonnegative
	 * @param denominator
	 *            an integer polynomial assumed to be positive
	 * @return the polynomial representing numerator%denominator
	 */
	private Polynomial intModulusPolynomials(Polynomial numerator,
			Polynomial denominator) {
		if (numerator.isZero())
			return numerator;
		if (denominator.isOne())
			return zeroInt;
		if (numerator instanceof Constant && denominator instanceof Constant)
			return intModuloConstants((Constant) numerator,
					(Constant) denominator);
		else {
			Polynomial[] triple = intFactor(numerator, denominator);

			numerator = triple[1];
			denominator = triple[2];
			if (denominator.isOne())
				return zeroInt;
			if (numerator instanceof Constant
					&& denominator instanceof Constant)
				return multiply(
						intModuloConstants((Constant) numerator,
								(Constant) denominator), triple[0]);
			else
				return multiply(
						triple[0],
						expression(SymbolicOperator.MODULO, integerType,
								numerator, denominator));
		}
	}

	/**
	 * Negates a constant <i>c</i>.
	 * 
	 * @param constant
	 *            a non-<code>null</code> instance of {@link Constant}
	 * @return -<i>c</i>
	 */
	private Constant negate(Constant constant) {
		return constant(objectFactory.numberObject(numberFactory
				.negate(constant.number())));
	}

	/**
	 * Divides two polynomials of real type. Result is a
	 * {@link RationalExpression}. Simplifications are performed by canceling
	 * common factors as possible.
	 * 
	 * @param numerator
	 *            a non-<code>null</code> polynomial of real type
	 * @param denominator
	 *            a non-<code>null</code> non-0 polynomial of real type
	 * @return numerator/denominator
	 */
	private RationalExpression divide(Polynomial numerator,
			Polynomial denominator) {
		assert numerator.type().isReal();
		assert denominator.type().isReal();
		if (numerator.isZero())
			return numerator;
		if (denominator.isOne())
			return numerator;
		else { // cancel common factors...
			Monomial[] triple = extractCommonality(
					numerator.factorization(this),
					denominator.factorization(this));
			Constant denomConstant;

			if (!triple[0].isOne()) {
				numerator = triple[1].expand(this);
				denominator = triple[2].expand(this);
			}
			denomConstant = denominator.factorization(this).monomialConstant(
					this);
			if (!denomConstant.isOne()) {
				denominator = divide(denominator, denomConstant);
				numerator = divide(numerator, denomConstant);
			}
			if (denominator.isOne())
				return numerator;
			return ntRationalExpression(numerator, denominator);
		}
	}

	/**
	 * Negates a polynomial term map.
	 * 
	 * @param termMap
	 *            a non-<code>null</code> polynomial term map
	 * @return the term map in which every coefficient <i>c</i> is replaced with
	 *         <i>-c</i>
	 */
	private SymbolicMap<Monic, Monomial> negate(
			SymbolicMap<Monic, Monomial> termMap) {
		return termMap.apply(monomialNegater);

	}

	/**
	 * Negates a polynomial, i.e., given a polynomial p, returns the polynomial
	 * -p.
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> polynomial
	 * @return negation of that polynomial
	 */
	private Polynomial negate(Polynomial polynomial) {
		return polynomial(negate(polynomial.termMap(this)),
				negate(polynomial.factorization(this)));
	}

	/**
	 * Negates a rational expression, i.e., given a rational expression p/q,
	 * returns the rational expression -p/q.
	 * 
	 * @param rational
	 *            a non-<code>null</code> {@link RationalExpression}
	 * @return negation of that rational expression in normal form
	 */
	private RationalExpression negate(RationalExpression rational) {
		// TODO: here NO NEED TO go through all division checks, factorizations,
		// etc. just need to negate numerator. Need divideNoCommon...
		return divide(negate(rational.numerator(this)),
				rational.denominator(this));
	}

	/**
	 * Computes the sum of the results of casting the elements in a symbolic
	 * collection to real. The elements of the collection must all be instances
	 * of {@link IdealExpression}.
	 * 
	 * @param args
	 *            a non-<code>null</code> {@link SymbolicCollection} in which
	 *            every element is an instance of {@link IdealExpression}
	 * @return an expression equivalent to the result of summing the casts to
	 *         real of the elements of the collection
	 */
	private NumericExpression addWithCast(
			SymbolicCollection<? extends SymbolicExpression> args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpression arg : args) {
			if (result == null)
				result = castToReal((NumericExpression) arg);
			else
				result = add(result, castToReal((NumericExpression) arg));
		}
		return result;
	}

	/**
	 * Computes the product of the results of casting the elements in a symbolic
	 * collection to real. The elements of the collection must all be instances
	 * of {@link IdealExpression}.
	 * 
	 * @param args
	 *            a non-<code>null</code> {@link SymbolicCollection} in which
	 *            every element is an instance of {@link IdealExpression}
	 * @return an expression equivalent to the result of multiplying the casts
	 *         to real of the elements of the collection
	 */
	private NumericExpression multiplyWithCast(
			SymbolicCollection<? extends SymbolicExpression> args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpression arg : args) {
			if (result == null)
				result = castToReal((NumericExpression) arg);
			else
				result = multiply(result, castToReal((NumericExpression) arg));
		}
		return result;
	}

	/**
	 * Computes result of raising <code>base</code> to the <code>exponent</code>
	 * power.
	 * 
	 * @param base
	 *            a non-<code>null</code> {@link IdealExpression}
	 * 
	 * @param exponent
	 *            the exponent, which must be positive
	 * @return the result of raising base to the power exponent
	 */
	private NumericExpression powerNumber(NumericExpression base,
			IntegerNumber exponent) {
		if (base.type().isReal())
			return realExponentiator.exp(base, exponent);
		else
			return integerExponentiator.exp(base, exponent);
	}

	/**
	 * <p>
	 * Computes the result of casting any {@link IdealExpression} to real type.
	 * If <code>numericExpression</code> already has real type, it is returned
	 * as is. Otherwise, it has integer type, and the result depends on the
	 * operator of the expression.
	 * </p>
	 * 
	 * <p>
	 * For ideal arithmetic, casting commutes with most operations, i.e., cast(a
	 * O p) = cast(a) O cast(p), for O=+,-,*. However, not integer division. Not
	 * integer modulus. Primitives get a {@link SymbolicOperator#CAST} in front
	 * of them. {@link Constant}s get cast by the {@link #numberFactory}.
	 * </p>
	 * 
	 * @param numericExpression
	 *            a non-<code>null</code> {@link IdealExpression}
	 */
	private NumericExpression castToReal(NumericExpression numericExpression) {
		if (numericExpression.type().isReal())
			return numericExpression;

		SymbolicOperator operator = numericExpression.operator();
		SymbolicObject arg0 = numericExpression.argument(0);
		SymbolicObjectKind kind0 = arg0.symbolicObjectKind();

		switch (operator) {
		case ADD:
			if (kind0 == SymbolicObjectKind.EXPRESSION_COLLECTION) {
				@SuppressWarnings("unchecked")
				SymbolicCollection<? extends SymbolicExpression> collection = (SymbolicCollection<? extends SymbolicExpression>) arg0;

				return addWithCast(collection);
			} else
				return add(castToReal((NumericExpression) arg0),
						castToReal((NumericExpression) numericExpression
								.argument(1)));
		case CONCRETE:
			return constant(numberFactory.rational(((NumberObject) arg0)
					.getNumber()));
		case COND:
			return expression(
					operator,
					realType,
					arg0,
					castToReal((NumericPrimitive) numericExpression.argument(1)),
					castToReal((NumericPrimitive) numericExpression.argument(2)));
		case MULTIPLY:
			if (kind0 == SymbolicObjectKind.EXPRESSION_COLLECTION) {
				@SuppressWarnings("unchecked")
				SymbolicCollection<? extends SymbolicExpression> collection = (SymbolicCollection<? extends SymbolicExpression>) arg0;

				return multiplyWithCast(collection);
			} else
				return multiply(castToReal((NumericExpression) arg0),
						castToReal((NumericExpression) numericExpression
								.argument(1)));
		case NEGATIVE:
			return minus(castToReal((NumericExpression) arg0));
		case POWER: {
			NumericExpression realBase = castToReal((NumericExpression) arg0);
			SymbolicObject arg1 = numericExpression.argument(1);

			if (arg1.symbolicObjectKind() == SymbolicObjectKind.INT)
				return power(realBase, (IntObject) arg1);
			else
				return power(realBase, castToReal((NumericExpression) arg1));
		}
		case SUBTRACT:
			return subtract(castToReal((NumericExpression) arg0),
					castToReal((NumericExpression) numericExpression
							.argument(1)));
			// Primitives...
		case APPLY:
		case ARRAY_READ:
		case CAST:
		case INT_DIVIDE:
		case LENGTH:
		case MODULO:
		case SYMBOLIC_CONSTANT:
		case TUPLE_READ:
		case UNION_EXTRACT:
			return expression(SymbolicOperator.CAST, realType,
					numericExpression);
		default:
			throw new SARLInternalException("Should be unreachable");
		}
	}

	/**
	 * Given a <code>polynomial</code>, returns an expression equivalent to
	 * <code>polynomial&gt;0</code>. Basic simplifications are performed, e.g.,
	 * if <code>polynomial</code> is concrete, a concrete boolean expression is
	 * returned.
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> {@link Polynomial}
	 * @return an expression equivalent to <code>polynomial&gt;0</code>
	 */
	private BooleanExpression isPositive(Polynomial polynomial) {
		Number number = extractNumber(polynomial);

		if (number == null) {
			return booleanFactory.booleanExpression(SymbolicOperator.LESS_THAN,
					zero(polynomial.type()), polynomial);
		}
		return number.signum() > 0 ? trueExpr : falseExpr;
	}

	/**
	 * Given a <code>polynomial</code>, returns an expression equivalent to
	 * <code>polynomial&lt;0</code>. Basic simplifications are performed, e.g.,
	 * if <code>polynomial</code> is concrete, a concrete boolean expression is
	 * returned.
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> {@link Polynomial}
	 * @return an expression equivalent to <code>polynomial&lt;0</code>
	 */
	private BooleanExpression isNegative(Polynomial polynomial) {
		return isPositive(negate(polynomial));
	}

	/**
	 * Given a <code>polynomial</code>, returns an expression equivalent to
	 * <code>0&le;polynomial</code>. Basic simplifications are performed, e.g.,
	 * if <code>polynomial</code> is concrete, a concrete boolean expression is
	 * returned.
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> {@link Polynomial}
	 * @return an expression equivalent to <code>0&le;polynomial</code>
	 */
	private BooleanExpression isNonnegative(Polynomial polynomial) {
		Number number = extractNumber(polynomial);

		if (number == null) {
			return booleanFactory.booleanExpression(
					SymbolicOperator.LESS_THAN_EQUALS, zero(polynomial.type()),
					polynomial);
		}
		return number.signum() >= 0 ? trueExpr : falseExpr;
	}

	// 4 relations: 0<, 0<=, 0==, 0!=

	// 0<p/q <=> (0<p && 0<q) || (0<-p && 0<-q)

	// 0<=p/q <=> (0<=p && 0<q) || (0<=-p && 0<-q)

	// 0==p/q <=> 0==p

	// 0!=p/q <=> 0!=

	/**
	 * Given two polynomials <code>p1</code> and <code>p2</code>, returns an
	 * expression equivalent to <code>p1&gt;0 && p2&gt;0</code>.
	 * 
	 * @param p1
	 *            a non-<code>null</code> {@link Polynomial}
	 * @param p2
	 *            a non-<code>null</code> {@link Polynomial} of same type as
	 *            <code>p1</code>
	 * @return an expression equivalent to <code>p1&gt;0 && p2&gt;0</code>
	 */
	private BooleanExpression arePositive(Polynomial p1, Polynomial p2) {
		BooleanExpression result = isPositive(p1);

		if (result.isFalse())
			return result;
		return booleanFactory.and(result, isPositive(p2));
	}

	/**
	 * Given two polynomials <code>p1</code> and <code>p2</code>, returns an
	 * expression equivalent to <code>p1&lt;0 && p2&lt;0</code>.
	 * 
	 * @param p1
	 *            a non-<code>null</code> {@link Polynomial}
	 * @param p2
	 *            a non-<code>null</code> {@link Polynomial} of same type as
	 *            <code>p1</code>
	 * @return an expression equivalent to <code>p1&lt;0 && p2&lt;0</code>
	 */
	private BooleanExpression areNegative(Polynomial p1, Polynomial p2) {
		BooleanExpression result = isNegative(p1);

		if (result.isFalse())
			return result;
		return booleanFactory.and(result, isNegative(p2));
	}

	/**
	 * Given a rational expression <code>rational</code> returns an expression
	 * equivalent to 0&lt;<code>rational</code>. This method will perform basic
	 * simplifications; for example, if <code>rational</code> is concrete, this
	 * method will return a concrete boolean expression (either "true" or
	 * "false").
	 * 
	 * @param rational
	 *            a non-<code>null</code> instance of {@link RationalExpression}
	 * @return an expression equivalent to 0&lt;<code>rational</code>
	 */
	private BooleanExpression isPositive(RationalExpression rational) {
		Number number = extractNumber(rational);

		if (number == null) {
			Polynomial numerator = rational.numerator(this);
			Polynomial denominator = rational.denominator(this);
			BooleanExpression result = arePositive(numerator, denominator);

			if (result.isTrue())
				return result;
			return booleanFactory.or(result,
					areNegative(numerator, denominator));
		}
		return number.signum() > 0 ? trueExpr : falseExpr;
	}

	/**
	 * Given a rational expression <code>rational</code> returns an expression
	 * equivalent to 0&le;<code>rational</code>. This method will perform basic
	 * simplifications; for example, if <code>rational</code> is concrete, this
	 * method will return a concrete boolean expression (either "true" or
	 * "false").
	 * 
	 * @param rational
	 *            a non-<code>null</code> instance of {@link RationalExpression}
	 * @return an expression equivalent to 0&le;<code>rational</code>
	 */
	private BooleanExpression isNonnegative(RationalExpression rational) {
		Number number = extractNumber(rational);

		if (number == null) {
			Polynomial numerator = rational.numerator(this);
			Polynomial denominator = rational.denominator(this);
			BooleanExpression result = booleanFactory.and(
					isNonnegative(numerator), isPositive(denominator));

			if (result.isTrue())
				return result;
			return booleanFactory.or(result, booleanFactory.and(
					isNonnegative(negate(numerator)), isNegative(denominator)));
		}
		return number.signum() >= 0 ? trueExpr : falseExpr;
	}

	/**
	 * Given two numeric expressions <code>arg0</code> and <code>arg1</code>,
	 * returns a boolean expression equivalent to <code>arg0&le;arg1</code>. The
	 * result will be in ideal form, i.e., <code>0&le;arg1-arg0</code>.
	 * Implementation uses {@link #isNonnegative(Polynomial)} and
	 * {@link #isNonnegative(RationalExpression)}.
	 * 
	 * @param arg0
	 *            a non-<code>null</code> {@link IdealExpression}
	 * @param arg1
	 *            a non-<code>null</code> {@link IdealExpression} of the same
	 *            type as <code>arg0</code>
	 * @return an expression equivalent to <code>0&le;arg1-arg0</code>
	 */
	private BooleanExpression lessThanEqualsMain(NumericExpression arg0,
			NumericExpression arg1) {
		NumericExpression difference = subtract(arg1, arg0);

		return difference instanceof Polynomial ? isNonnegative((Polynomial) difference)
				: isNonnegative((RationalExpression) difference);
	}

	// ********************* Package-private methods **********************

	/**
	 * Computes the result of raising the given primitive to the given concrete
	 * integer exponent.
	 * 
	 * @param primitive
	 *            a non-<code>null</code> instance of {@link Primitive}
	 * @param exponent
	 *            a non-<code>null</code> concrete positive integer
	 * @return result of raising <code>primitive</code> to the
	 *         <code>exponent</code> power, in ideal normal form
	 */
	PrimitivePower primitivePower(Primitive primitive, IntObject exponent) {
		if (exponent.isZero())
			throw new IllegalArgumentException(
					"Exponent to primitive power must be positive: "
							+ primitive);
		if (exponent.isOne())
			return primitive;
		return ntPrimitivePower(primitive, exponent);
	}

	/**
	 * Returns the sum of two constants. The two constants must have the same
	 * type (both integer, or both real).
	 * 
	 * @param c1
	 *            any non-<code>null</code> {@link Constant}
	 * @param c2
	 *            a non-<code>null</code> {@link Constant} of same type as
	 *            <code>c1</code>
	 * @return the sum of the two constants
	 */
	Constant add(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.add(
				c1.number(), c2.number())));
	}

	/**
	 * Computes the negation of a monomial (i.e., multiplication by -1).
	 * 
	 * @param monomial
	 *            any non-<code>null</code> {@link Monomial}
	 * @return the monomial which is the negation of the given one
	 */
	Monomial negate(Monomial monomial) {
		return monomial(negate(monomial.monomialConstant(this)),
				monomial.monic(this));
	}

	// ********** Methods specified in NumericExpressionFactory ***********

	@Override
	public void init() {
	}

	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	@Override
	public BooleanExpressionFactory booleanFactory() {
		return booleanFactory;
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyMap() {
		return (SymbolicMap<K, V>) emptyMap;
	}

	@Override
	public One oneInt() {
		return oneInt;
	}

	@Override
	public One oneReal() {
		return oneReal;
	}

	@Override
	public NumericPrimitive expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		return new NumericPrimitive(operator, numericType, arguments);
	}

	@Override
	public NumericPrimitive expression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		return new NumericPrimitive(operator, numericType, arguments);
	}

	@Override
	public NumericPrimitive expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		return new NumericPrimitive(operator, numericType, arg0);
	}

	@Override
	public NumericPrimitive expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		return new NumericPrimitive(operator, numericType, arg0, arg1);
	}

	@Override
	public NumericPrimitive expression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return new NumericPrimitive(operator, numericType, arg0, arg1, arg2);
	}

	@Override
	public NumericExpression add(NumericExpression arg0, NumericExpression arg1) {
		if (arg0 instanceof Constant && arg1 instanceof Constant)
			return add((Constant) arg0, (Constant) arg1);
		if (arg0.type().isInteger())
			return add((Polynomial) arg0, (Polynomial) arg1);
		else
			return addRational((RationalExpression) arg0,
					(RationalExpression) arg1);
	}

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		return add(arg0, minus(arg1));
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0 instanceof Constant && arg1 instanceof Constant)
			return multiply((Constant) arg0, (Constant) arg1);
		if (arg0.type().isInteger())
			return multiply((Polynomial) arg0, (Polynomial) arg1);
		else
			return multiplyRational((RationalExpression) arg0,
					(RationalExpression) arg1);
	}

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0 instanceof Constant && arg1 instanceof Constant)
			return divide((Constant) arg0, (Constant) arg1);
		if (arg0.type().isInteger())
			return intDividePolynomials((Polynomial) arg0, (Polynomial) arg1);
		if (arg0 instanceof Polynomial && arg1 instanceof Polynomial)
			return divide((Polynomial) arg0, (Polynomial) arg1);
		return divide((RationalExpression) arg0, (RationalExpression) arg1);
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		return intModulusPolynomials((Polynomial) arg0, (Polynomial) arg1);
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
		if (arg.isZero())
			return arg;
		if (arg instanceof Constant)
			return negate((Constant) arg);
		if (arg instanceof Polynomial)
			return negate((Polynomial) arg);
		else
			return negate((RationalExpression) arg);
	}

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		NumericExpression result = one(base.type());
		int n = exponent.getInt();

		assert n >= 0;
		while (n > 0) {
			if (n % 2 != 0) {
				result = multiply(result, base);
				n -= 1;
			}
			base = multiply(base, base);
			n /= 2;
		}
		return result;
	}

	/**
	 * Raises base to exponent power, where exponent may be any kind of number.
	 * Handles special case where exponent is a concrete integer number.
	 * 
	 * TODO: (a^b)^c=a^(bc). (a^b)(a^c)=a^(b+c)
	 * 
	 */
	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		Number exponentNumber = extractNumber(exponent);

		if (exponentNumber != null) {
			if (exponentNumber instanceof IntegerNumber) {
				IntegerNumber exponentInteger = (IntegerNumber) exponentNumber;
				int signum = exponentNumber.signum();

				if (signum > 0)
					return powerNumber(base, exponentInteger);
				else {
					boolean isInteger = base.type().isInteger();

					if (signum < 0) {
						if (isInteger)
							throw new SARLException(
									"Power expression with integer base and negative exponent:\n"
											+ base + "\n" + exponent);
						return invert((RationalExpression) powerNumber(base,
								numberFactory.negate(exponentInteger)));
					} else {
						if (base.isZero())
							throw new SARLException("0^0 is undefined");
						return isInteger ? oneInt : oneReal;
					}
				}
			}
		}
		return expression(SymbolicOperator.POWER, base.type(), base, exponent);
	}

	@Override
	public NumericExpression cast(NumericExpression numericExpression,
			SymbolicType newType) {
		if (numericExpression.type().isIdeal() && newType.equals(realType))
			return castToReal(numericExpression);
		if (numericExpression.type().isReal() && newType.equals(integerType)) {
			RationalNumber number = (RationalNumber) extractNumber(numericExpression);

			if (number != null) {
				int sign = number.signum();

				if (sign >= 0) {
					return constant(numberFactory.floor(number));
				} else {
					return constant(numberFactory.ceil(number));
				}
			}
		}
		return expression(SymbolicOperator.CAST, newType, numericExpression);
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression instanceof Constant)
			return ((Constant) expression).number();
		return null;
	}

	@Override
	public NumericExpression number(NumberObject numberObject) {
		return constant(numberObject);
	}

	@Override
	public NumericSymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		return new IdealSymbolicConstant(name, type);
	}

	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	@Override
	public CollectionFactory collectionFactory() {
		return collectionFactory;
	}

	@Override
	public IdealComparator comparator() {
		return comparator;
	}

	// 4 relations: 0<, 0<=, 0==, 0!=

	// 0<p/q <=> (0<p && 0<q) || (0<-p && 0<-q)

	// 0<=p/q <=> (0<=p && 0<q) || (0<=-p && 0<-q)

	// 0==p/q <=> 0==p

	// 0!=p/q <=> 0!=

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		Number num0 = extractNumber(arg0);

		if (num0 != null) {
			Number num1 = extractNumber(arg1);

			if (num1 != null) // num0-num1<0 <=> num0<num1
				return numberFactory.compare(num0, num1) < 0 ? this.trueExpr
						: this.falseExpr;
		}

		NumericExpression difference = subtract(arg1, arg0);

		return difference instanceof Polynomial ? isPositive((Polynomial) difference)
				: isPositive((RationalExpression) difference);
	}

	// public BooleanExpression integerLessThan(NumericExpression arg0,
	// NumericExpression arg1) {
	// return lessThanEquals(add(arg0, oneInt), arg1);
	// }

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		Number num0 = extractNumber(arg0);

		if (num0 != null) {
			Number num1 = extractNumber(arg1);

			if (num1 != null) // num0-num1<=0 <=> num0<=num1
				return numberFactory.compare(num0, num1) <= 0 ? this.trueExpr
						: falseExpr;
		}
		return lessThanEqualsMain(arg0, arg1);
	}

	@Override
	public BooleanExpression notLessThan(NumericExpression arg0,
			NumericExpression arg1) {
		return lessThanEquals(arg1, arg0);
	}

	@Override
	public BooleanExpression notLessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		return lessThan(arg1, arg0);
	}

	@Override
	public BooleanExpression equals(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.equals(arg1))
			return trueExpr;
		// if they are constants but not equal, return false:
		if (arg0 instanceof Constant && arg1 instanceof Constant)
			return falseExpr;

		NumericExpression difference = subtract(arg1, arg0);
		Number number = extractNumber(difference);

		if (number == null)
			return booleanFactory.booleanExpression(SymbolicOperator.EQUALS,
					zero(arg0.type()), zeroEssence(difference));
		else
			return number.signum() == 0 ? trueExpr : falseExpr;
	}

	@Override
	public BooleanExpression neq(NumericExpression arg0, NumericExpression arg1) {
		if (arg0.equals(arg1))
			return falseExpr;
		if (arg0 instanceof Constant && arg1 instanceof Constant)
			return trueExpr;

		NumericExpression difference = subtract(arg1, arg0);
		Number number = extractNumber(difference);

		if (number == null)
			return booleanFactory.booleanExpression(SymbolicOperator.NEQ,
					zero(arg0.type()), zeroEssence(difference));
		else
			return number.signum() != 0 ? trueExpr : falseExpr;
	}

	// ***************** Methods specified in IdealFactory ******************

	@Override
	public Polynomial zeroEssence(NumericExpression expr) {
		Polynomial result = expr instanceof Polynomial ? (Polynomial) expr
				: ((RationalExpression) expr).numerator(this);

		if (result instanceof Monomial) {
			Monic monic = ((Monomial) result).monic(this);

			result = monic instanceof PrimitivePower ? ((PrimitivePower) monic)
					.primitive(this) : monic;
		} else {
			Monomial factorization = result.factorization(this);
			Monic monic = factorization.monic(this);

			if (monic instanceof PrimitivePower)
				monic = ((PrimitivePower) monic).primitive(this);
			// in monic, the primitives may be instances of
			// ReducedPolynomial. Need to expand them...
			result = monic.expand(this);
		}
		return result;
	}

	@Override
	public IntObject oneIntObject() {
		return oneIntObject;
	}

	@Override
	public <K extends NumericExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonMap(
			K key, V value) {
		return collectionFactory.singletonSortedMap(comparator, key, value);
	}

	@Override
	public Constant intConstant(int value) {
		if (value == 1)
			return oneInt;
		return new NTConstant(integerType,
				objectFactory.numberObject(numberFactory.integer(value)));
	}

	@Override
	public Constant constant(Number number) {
		return constant(objectFactory.numberObject(number));
	}

	@Override
	public Constant zeroInt() {
		return zeroInt;
	}

	@Override
	public Constant zeroReal() {
		return zeroReal;
	}

	@Override
	public Constant zero(SymbolicType type) {
		return type.isInteger() ? zeroInt : zeroReal;
	}

	@Override
	public One one(SymbolicType type) {
		return type.isInteger() ? oneInt : oneReal;
	}

	@Override
	public Monomial monomial(Constant constant, Monic monic) {
		if (constant.isZero())
			return constant;
		if (constant.isOne())
			return monic;
		if (monic.isTrivialMonic())
			return constant;
		// zirkel: A constant times big-O is just big-O
		if (monic.operator() == SymbolicOperator.APPLY
				&& ((SymbolicConstant) monic.argument(0)).name().toString()
						.equals("BIG_O")) {
			return monic;
		}
		return ntMonomial(constant, monic);
	}

	@Override
	public Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization) {
		if (termMap.size() == 0)
			return zero(factorization.type());
		if (termMap.size() == 1)
			return termMap.getFirst();
		return ntPolynomial(termMap, factorization);
	}

	@Override
	public Polynomial subtractConstantTerm(Polynomial polynomial) {
		SymbolicType type = polynomial.type();

		if (polynomial instanceof Constant)
			return zero(type);
		else {
			Constant constant = polynomial.constantTerm(this);

			if (constant.isZero())
				return polynomial;
			if (polynomial instanceof NTPolynomial) {
				SymbolicMap<Monic, Monomial> termMap = polynomial.termMap(this)
						.remove(one(type));

				if (termMap.size() == 1)
					return termMap.getFirst();
				assert termMap.size() > 1;
				return polynomialWithTrivialFactorization(type, termMap);
			} else
				throw new SARLInternalException("unreachable");
		}
	}

	@Override
	public Polynomial add(Polynomial p1, Polynomial p2) {
		assert p1.type().equals(p2.type());
		if (p1.isZero())
			return p2;
		if (p2.isZero())
			return p1;

		Monomial fact1 = p1.factorization(this);
		Monomial fact2 = p2.factorization(this);
		Monomial[] triple = extractCommonality(fact1, fact2);
		// p1+p2=a(q1+q2)

		if (triple[0].isOne())
			return addNoCommon(p1, p2);
		return multiply(triple[0].expand(this),
				addNoCommon(triple[1].expand(this), triple[2].expand(this)));
	}

	@Override
	public Polynomial multiply(Polynomial poly1, Polynomial poly2) {
		if (poly1.isZero())
			return poly1;
		if (poly2.isZero())
			return poly2;
		if (poly1.isOne())
			return poly2;
		if (poly2.isOne())
			return poly1;
		if (poly1 instanceof Monomial && poly2 instanceof Monomial)
			return multiply((Monomial) poly1, (Monomial) poly2);
		else {
			SymbolicMap<Monic, Monomial> termMap1 = poly1.termMap(this);
			SymbolicMap<Monic, Monomial> termMap2 = poly2.termMap(this);
			SymbolicMap<Monic, Monomial> newTermMap = multiply(termMap1,
					termMap2);
			Monomial fact1 = poly1.factorization(this);
			Monomial fact2 = poly2.factorization(this);
			Monomial newFact = multiply(fact1, fact2);
			Polynomial result = polynomial(newTermMap, newFact);

			return result;
		}
	}

	@Override
	public Polynomial divide(Polynomial polynomial, Constant constant) {
		return polynomial(divide(polynomial.termMap(this), constant),
				divide(polynomial.factorization(this), constant));
	}

}
