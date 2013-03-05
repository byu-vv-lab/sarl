package edu.udel.cis.vsl.sarl.ideal;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperator;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpression;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * <pre>
 * rat       : DIVIDE factpoly factpoly | factpoly
 * factpoly  : FACTPOLY poly fact | poly
 * fact      : MULTIPLY number monicfact | monicfact
 * monicfact : MULTIPLY polypow+ | polypow
 * polypow   : POWER poly int | poly
 * poly      : SUM monomial+ | monomial
 * monomial  : MULTIPLY number monic | number | monic
 * monic     : MULTIPLY ppow+ | ppow
 * ppow      : POWER primitive int | primitive
 * number    : CONCRETE numberObject
 * primitive : ...
 * </pre>
 * 
 * A primitive is anything that doesn't fall into one of the preceding
 * categories, including a symbolic constant, array read expression, tuple read
 * expression, function application, etc.
 * 
 * Rules of the normal form:
 * <ul>
 * <li>Any numeric expression will have one of the following 8 forms: rat,
 * factpoly, poly, monomial, monic, ppow, number, primitive; a numeric
 * expression of integer type will not have rat form
 * <li>NO MIXING OF TYPES: in an expression of real type, all of the descendant
 * arguments will have real type; in an expression of integer type, all of the
 * descendant arguments will have integer type</li>
 * <li>the second factpoly argument of DIVIDE in the rat rule must be _reduced_:
 * if real type, the leading coefficient is 1; if integer type, the leading
 * coefficient is positive and the GCD of the absolute values of the
 * coefficients is 1.</li>
 * <li>the two factpoly arguments of DIVIDE in the rat rule will have no common
 * factors</li>
 * <li>the poly argument of FACTPOLY cannot be a monomial</li>
 * <li>the poly argument of FACTPOLY must be a monic polynomial, i.e., have
 * leading coefficient 1</li>
 * <li>the fact argument of FACTPOLY, when multiplied out, will yield the same
 * polynomial as the poly argument of FACTPOLY</li>
 * <li>the sequence polypow+ in monicfact will have length at least 2</li>
 * <li>the int in polypow will be greater than or equal to 2</li>
 * <li>the sequence monomial+ in poly will have length >=2</li>
 * <li>the number argument of MULTIPLY in monomial rule will not be 1.</li>
 * <li>the sequence ppow+ in monic rule will have length >=2</li>
 * <li>the int in ppow rule will be >=2</li>
 * </ul>
 * 
 * <pre>
 * Normal form examples:
 * x         : x
 * 1         : 1
 * x+1       : SUM x 1
 * x^2       : POWER x 2
 * (x+1)^2   : FACTPOLY
 *               (SUM (POWER x 2) (MULTIPLY 2 x) 1)
 *               (POWER (SUM x 1) 2)
 * x/y       : DIVIDE x y
 * 2/3       : CONCRETE(2/3)
 * x/2       : MULTIPLY 1/2 x
 * (x+1)^2/3 : FACTPOLY
 *               (SUM (MULTIPLY 1/3 (POWER x 2)) (MULTIPLY 2/3 x) 1/3)
 *               (MULTIPLY 1/3 (POWER (SUM x 1) 2))
 * 
 * </pre>
 * 
 */

// create separate class IdealRelationFactory for lt, eq, ...
// and interface NumericRelationFactory
// needs numeric factory, expression factory, true, false exprs.

public class IdealFactory implements NumericExpressionFactory {

	private NumberFactory numberFactory;

	private ExpressionFactory expressionFactory;

	private BooleanExpressionFactory booleanFactory;

	private ObjectFactory objectFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private IdealComparator comparator;

	private SymbolicMap<?, ?> emptyMap;

	private SymbolicType integerType, realType;

	private One oneInt, oneReal;

	private IntObject oneIntObject;

	private Constant zeroInt, zeroReal, twoInt, twoReal, negOneInt, negOneReal;

	private BooleanExpression trueExpr, falseExpr;

	private MonomialAdder monomialAdder;

	private MonomialNegater monomialNegater;

	private PrimitivePowerMultiplier primitivePowerMultipler;

	public IdealFactory(NumberFactory numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory) {
		this.numberFactory = numberFactory;
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.collectionFactory = collectionFactory;
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
		this.twoInt = canonicIntConstant(2);
		this.twoReal = canonicRealConstant(2);
		this.negOneInt = canonicIntConstant(-1);
		this.negOneReal = canonicRealConstant(-1);
		this.monomialAdder = new MonomialAdder(this);
		this.monomialNegater = new MonomialNegater(this);
		this.primitivePowerMultipler = new PrimitivePowerMultiplier(this);
	}

	@Override
	public void setObjectComparator(Comparator<SymbolicObject> c) {
		comparator.setObjectComparator(c);
	}

	@Override
	public void setExpressionFactory(ExpressionFactory expressionFactory) {
		this.expressionFactory = expressionFactory;
	}

	@Override
	public void init() {
		assert comparator.objectComparator() != null;
		assert expressionFactory != null;
		booleanFactory = expressionFactory.booleanFactory();
		trueExpr = booleanFactory.trueExpr();
		falseExpr = booleanFactory.falseExpr();
	}

	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	public BooleanExpressionFactory booleanFactory() {
		return booleanFactory;
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	public SymbolicType integerType() {
		return integerType;
	}

	public SymbolicType realType() {
		return realType;
	}

	// Basic symbolic objects...

	@SuppressWarnings("unchecked")
	public <K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyMap() {
		return (SymbolicMap<K, V>) emptyMap;
	}

	public IntObject oneIntObject() {
		return oneIntObject;
	}

	public <K extends NumericExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonMap(
			K key, V value) {
		return collectionFactory.singletonSortedMap(comparator, key, value);
	}

	// Constants...

	public Constant intConstant(int value) {
		if (value == 1)
			return oneInt;
		return new NTConstant(integerType,
				objectFactory.numberObject(numberFactory.integer(value)));
	}

	private Constant canonicIntConstant(int value) {
		return objectFactory.canonic(intConstant(value));
	}

	public Constant realConstant(int value) {
		if (value == 1)
			return oneReal;
		return new NTConstant(realType,
				objectFactory.numberObject(numberFactory
						.integerToRational(numberFactory.integer(value))));
	}

	private Constant canonicRealConstant(int value) {
		return objectFactory.canonic(realConstant(value));
	}

	public Constant constant(NumberObject object) {
		if (object.isOne())
			return object.isInteger() ? oneInt : oneReal;
		return new NTConstant(object.isInteger() ? integerType : realType,
				object);
	}

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

	public Constant zero(SymbolicType type) {
		return type.isInteger() ? zeroInt : zeroReal;
	}

	@Override
	public One oneInt() {
		return oneInt;
	}

	@Override
	public One oneReal() {
		return oneReal;
	}

	public One one(SymbolicType type) {
		return type.isInteger() ? oneInt : oneReal;
	}

	public Constant twoInt() {
		return twoInt;
	}

	public Constant twoReal() {
		return twoReal;
	}

	public Constant two(SymbolicType type) {
		return type.isInteger() ? twoInt : twoReal;
	}

	public Constant negOneInt() {
		return negOneInt;
	}

	public Constant negOneReal() {
		return negOneReal;
	}

	public Constant negOne(SymbolicType type) {
		return type.isInteger() ? negOneInt : negOneReal;
	}

	// PrimitivePowers...

	private NTPrimitivePower ntPrimitivePower(NumericPrimitive primitive,
			IntObject exponent) {
		return new NTPrimitivePower(primitive, exponent);
	}

	public PrimitivePower primitivePower(NumericPrimitive primitive,
			IntObject exponent) {
		if (exponent.isZero())
			throw new IllegalArgumentException(
					"Exponent to primitive power must be positive: "
							+ primitive);
		if (exponent.isOne())
			return primitive;
		return ntPrimitivePower(primitive, exponent);
	}

	// Monics...

	private NTMonic ntMonic(SymbolicType type,
			SymbolicMap<NumericPrimitive, PrimitivePower> monicMap) {
		return new NTMonic(type, monicMap);
	}

	public Monic monic(SymbolicType type,
			SymbolicMap<NumericPrimitive, PrimitivePower> monicMap) {
		if (monicMap.isEmpty())
			return one(type);
		if (monicMap.size() == 1)
			return monicMap.iterator().next();
		return ntMonic(type, monicMap);
	}

	// Monomials...

	public MonomialAdder newMonomialAdder() {
		return new MonomialAdder(this);
	}

	NTMonomial ntMonomial(Constant constant, Monic monic) {
		return new NTMonomial(constant, monic);
	}

	public Monomial monomial(Constant constant, Monic monic) {
		if (constant.isZero())
			return constant;
		if (constant.isOne())
			return monic;
		if (monic.isTrivialMonic())
			return constant;
		return ntMonomial(constant, monic);
	}

	// ReducedPolynomials and Polynomials...

	/**
	 * The pre-requisite is that the polynomial specified by the sum of the
	 * monomials of the term map is reduced. This will not be checked.
	 * 
	 * @param type
	 * @param termMap
	 * @return
	 */
	public ReducedPolynomial reducedPolynomial(SymbolicType type,
			SymbolicMap<Monic, Monomial> termMap) {
		return new ReducedPolynomial(type, termMap);
	}

	private NTPolynomial ntPolynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization) {
		return new NTPolynomial(termMap, factorization);
	}

	public Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization) {
		if (termMap.size() == 0)
			return zero(factorization.type());
		if (termMap.size() == 1)
			return termMap.getFirst();
		return ntPolynomial(termMap, factorization);
	}

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

	public Polynomial polynomialWithTrivialFactorization(SymbolicType type,
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

	// Rational expressions

	private NTRationalExpression ntRationalExpression(Polynomial numerator,
			Polynomial denominator) {
		return new NTRationalExpression(numerator, denominator);
	}

	/************************ FACTORIZATION ***************************/

	// Extract Commonality...

	public Monic[] extractCommonality(Monic fact1, Monic fact2) {
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
	 */
	public Monomial[] extractCommonality(Monomial fact1, Monomial fact2) {
		Monic[] monicTriple = extractCommonality(fact1.monic(this),
				fact2.monic(this));

		return new Monomial[] { monicTriple[0],
				monomial(fact1.monomialConstant(this), monicTriple[1]),
				monomial(fact2.monomialConstant(this), monicTriple[2]) };
	}

	/***************************** ADD ********************************/

	public Constant add(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.add(
				c1.number(), c2.number())));
	}

	private SymbolicMap<Monic, Monomial> add(
			SymbolicMap<Monic, Monomial> termMap1,
			SymbolicMap<Monic, Monomial> termMap2) {
		return termMap1.combine(monomialAdder, termMap2);
	}

	/**
	 * Add to polynomials that have no common factors.
	 * 
	 * @param p1
	 *            a Polynomial
	 * @param p2
	 *            a Polynomial
	 * @return sum p1+p2
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
	 * Adds two polynomials, forming the factorization by factoring out common
	 * factors from the two factorizations.
	 * 
	 * @param p1
	 *            a Polynomial
	 * @param p2
	 *            a Polynomial
	 * @return the sum p1+p2
	 */
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

	public RationalExpression add(RationalExpression r1, RationalExpression r2) {
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

	/************************** MULTIPLY ******************************/

	public Constant multiply(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.multiply(
				c1.number(), c2.number())));
	}

	private SymbolicMap<Monic, Monomial> multiply(Constant constant,
			SymbolicMap<Monic, Monomial> termMap) {
		MonomialMultiplier multiplier = new MonomialMultiplier(this,
				constant.number());

		return termMap.apply(multiplier);
	}

	public Polynomial multiply(Constant constant, Polynomial polynomial) {
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

	// public Monic multiply(Monic monic1, Monic monic2) {
	// SymbolicMap<NumericPrimitive, PrimitivePower> factorMap1 = monic1
	// .monicFactors(this);
	// SymbolicMap<NumericPrimitive, PrimitivePower> factorMap2 = monic1
	// .monicFactors(this);
	// SymbolicMap<NumericPrimitive, PrimitivePower> productMap = emptyMap();
	// Iterator<Entry<NumericPrimitive, PrimitivePower>> iter1 = factorMap1
	// .entries().iterator();
	// Iterator<Entry<NumericPrimitive, PrimitivePower>> iter2 = factorMap2
	// .entries().iterator();
	//
	// if (iter1.hasNext() && iter2.hasNext()) {
	// Entry<NumericPrimitive, PrimitivePower> entry1 = iter1.next(), entry2 =
	// iter2
	// .next();
	//
	// do {
	// NumericPrimitive p1 = entry1.getKey(), p2 = entry2.getKey();
	// int compare = comparator.comparePrimitives(p1, p2);
	//
	// if (compare == 0) {
	// productMap = productMap.put(
	// p1,
	// primitivePower(
	// p1,
	// entry1.getValue()
	// .primitivePowerExponent(this)
	// .plus(entry2.getValue()
	// .primitivePowerExponent(
	// this))));
	// entry1 = iter1.next();
	// entry2 = iter2.next();
	// } else if (compare < 0) {
	// productMap = productMap.put(entry1.getKey(),
	// entry1.getValue());
	// entry1 = iter1.next();
	// } else {
	// productMap = productMap.put(entry2.getKey(),
	// entry2.getValue());
	// entry2 = iter2.next();
	// }
	// } while (entry1 != null && entry2 != null);
	// while (entry1 != null) {
	// productMap = productMap.put(entry1.getKey(), entry1.getValue());
	// entry1 = iter1.next();
	// }
	// while (entry2 != null) {
	// productMap = productMap.put(entry2.getKey(), entry2.getValue());
	// entry2 = iter2.next();
	// }
	// }
	// }

	public Monic multiply(Monic monic1, Monic monic2) {
		return monic(
				monic1.type(),
				monic1.monicFactors(this).combine(primitivePowerMultipler,
						monic2.monicFactors(this)));
	}

	public Monomial multiply(Monomial m1, Monomial m2) {
		return monomial(
				multiply(m1.monomialConstant(this), m2.monomialConstant(this)),
				multiply(m1.monic(this), m2.monic(this)));
	}

	// order is unchanged
	private SymbolicMap<Monic, Monomial> multiply(Monomial monomial,
			SymbolicMap<Monic, Monomial> termMap) {
		SymbolicMap<Monic, Monomial> result = collectionFactory
				.emptySortedMap();

		for (Monomial m : termMap) {
			Monomial product = multiply(monomial, m);

			result = result.put(product.monic(this), product);
		}
		return result;
	}

	public Polynomial multiply(Monomial monomial, Polynomial polynomial) {
		return polynomial(multiply(monomial, polynomial.termMap(this)),
				multiply(monomial, polynomial.factorization(this)));
	}

	/**
	 * Multiplies two term maps, returning a term map.
	 * 
	 * The trick to performance is to do perform all intermediate computations
	 * using mutable data structures, and only wrap things up into the immutable
	 * structures at the end.
	 * 
	 * @param termMap1
	 *            a term map
	 * @param termMap2
	 *            a term map of same type
	 * @return term map product
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

	// private SymbolicMap<Monic, Monomial> multiplyOld(
	// SymbolicMap<Monic, Monomial> termMap1,
	// SymbolicMap<Monic, Monomial> termMap2) {
	// SymbolicMap<Monic, Monomial> result = collectionFactory
	// .emptySortedMap();
	//
	// for (Monomial monomial : termMap1.values())
	// result = add(result, multiply(monomial, termMap2));
	// return result;
	// }

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
		return polynomial(multiply(poly1.termMap(this), poly2.termMap(this)),
				multiply(poly1.factorization(this), poly2.factorization(this)));
	}

	public RationalExpression multiply(RationalExpression r1,
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

	/*************************** DIVIDE *******************************/

	public Constant divide(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.divide(
				c1.number(), c2.number())));
	}

	private SymbolicMap<Monic, Monomial> divide(
			SymbolicMap<Monic, Monomial> termMap, Constant constant) {
		MonomialDivider divider = new MonomialDivider(this, constant.number());

		return termMap.apply(divider);
	}

	public Monomial divide(Monomial monomial, Constant constant) {
		return monomial(divide(monomial.monomialConstant(this), constant),
				monomial.monic(this));
	}

	public Polynomial divide(Polynomial polynomial, Constant constant) {
		return polynomial(divide(polynomial.termMap(this), constant),
				divide(polynomial.factorization(this), constant));
	}

	public RationalExpression divide(Polynomial numerator,
			Polynomial denominator) {
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

	// Integer division D: assume all terms positive
	// (ad)D(bd) = aDb
	// (ad)%(bd) = (a%b)d

	public Constant intDivideConstants(Constant c1, Constant c2) {
		return constant(numberFactory.divide((IntegerNumber) c1.number(),
				(IntegerNumber) c2.number()));
	}

	public Polynomial intDividePolynomials(Polynomial numerator,
			Polynomial denominator) {
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
			return newNumericExpression(SymbolicOperator.INT_DIVIDE,
					integerType, numerator, denominator);
		}
	}

	/**
	 * Integer modulus. Assume numerator is nonnegative and denominator is
	 * positive.
	 * 
	 * @param numerator
	 * @param denominator
	 * @return
	 */
	public Polynomial intModulusPolynomials(Polynomial numerator,
			Polynomial denominator) {
		if (numerator.isZero())
			return numerator;
		if (denominator.isOne())
			return zeroInt;
		else { // cancel common factors...
			Monomial[] triple = extractCommonality(
					numerator.factorization(this),
					denominator.factorization(this));
			boolean isOne = triple[0].isOne();

			if (!isOne) {
				numerator = triple[1].expand(this);
				denominator = triple[2].expand(this);
			}
			if (denominator.isOne())
				return zeroInt;
			else {
				Polynomial result = newNumericExpression(
						SymbolicOperator.MODULO, integerType, numerator,
						denominator);

				if (!isOne)
					result = multiply(triple[0].expand(this), result);
				return result;
			}
		}
	}

	/*************************** NEGATE *******************************/

	public Constant negate(Constant constant) {
		return constant(objectFactory.numberObject(numberFactory
				.negate(constant.number())));
	}

	public Monomial negate(Monomial monomial) {
		return monomial(negate(monomial.monomialConstant(this)),
				monomial.monic(this));
	}

	private SymbolicMap<Monic, Monomial> negate(
			SymbolicMap<Monic, Monomial> termMap) {
		return termMap.apply(monomialNegater);

	}

	public Polynomial negate(Polynomial polynomial) {
		return polynomial(negate(polynomial.termMap(this)),
				negate(polynomial.factorization(this)));
	}

	public RationalExpression negate(RationalExpression rational) {
		// here NO NEED TO go through all division checks, factorizations,
		// etc. just need to negate numerator. Need divideNoCommon...
		return divide(negate(rational.numerator(this)),
				rational.denominator(this));
	}

	/*************************** EXPORTED *****************************/

	// Methods specified in interface NumericExpressionFactory...

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject[] arguments) {
		return new NumericPrimitive(operator, numericType, arguments);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicType numericType, Collection<SymbolicObject> arguments) {
		return new NumericPrimitive(operator, numericType, arguments);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0) {
		return new NumericPrimitive(operator, numericType, arg0);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1) {
		return new NumericPrimitive(operator, numericType, arg0, arg1);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicType numericType, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		return new NumericPrimitive(operator, numericType, arg0, arg1, arg2);
	}

	@Override
	public NumericExpression add(NumericExpression arg0, NumericExpression arg1) {
		if (arg0.type().isInteger())
			return add((Polynomial) arg0, (Polynomial) arg1);
		else
			return add((RationalExpression) arg0, (RationalExpression) arg1);
	}

	@Override
	public NumericExpression add(
			SymbolicCollection<? extends SymbolicExpression> args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpression arg : args) {
			if (result == null)
				result = (NumericExpression) arg;
			else
				result = add(result, (NumericExpression) arg);
		}
		return result;
	}

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

	@Override
	public NumericExpression subtract(NumericExpression arg0,
			NumericExpression arg1) {
		return add(arg0, minus(arg1));
	}

	@Override
	public NumericExpression multiply(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isInteger())
			return multiply((Polynomial) arg0, (Polynomial) arg1);
		else
			return multiply((RationalExpression) arg0,
					(RationalExpression) arg1);
	}

	@Override
	public NumericExpression multiply(
			SymbolicCollection<? extends SymbolicExpression> args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpression arg : args) {
			if (result == null)
				result = (NumericExpression) arg;
			else
				result = multiply(result, (NumericExpression) arg);
		}
		return result;
	}

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

	@Override
	public NumericExpression divide(NumericExpression arg0,
			NumericExpression arg1) {
		if (arg0.type().isInteger())
			return divide((Polynomial) arg0, (Polynomial) arg1);
		return divide(arg0, arg1);
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
		if (arg instanceof Polynomial)
			return negate((Polynomial) arg);
		else
			return negate((RationalExpression) arg);
	}

	public Polynomial power(Polynomial base, IntObject exponent) {
		Polynomial result = one(base.type());
		int n = exponent.getInt();

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

	@Override
	public NumericExpression power(NumericExpression base, IntObject exponent) {
		NumericExpression result = one(base.type());
		int n = exponent.getInt();

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
	 * TODO: (a^b)^c=a^(bc). (a^b)(a^c)=a^(b+c)
	 * 
	 */
	@Override
	public NumericExpression power(NumericExpression base,
			NumericExpression exponent) {
		return newNumericExpression(SymbolicOperator.POWER, base.type(), base,
				exponent);
	}

	/**
	 * For ideal arithmetic, this respects almost every operation. cast(a O p) =
	 * cast(a) O cast(p), for O=+,-,*, Not division. Nod modulus. Primities get
	 * a CAST operator in front of them. Constants get cast by number factory.
	 */
	@Override
	public NumericExpression castToReal(NumericExpression numericExpression) {
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
			return newNumericExpression(
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
			return newNumericExpression(SymbolicOperator.CAST, realType,
					numericExpression);
		default:
			throw new SARLInternalException("Should be unreachable");
		}
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (expression instanceof Constant)
			return ((Constant) expression).number();
		return null;
	}

	@Override
	public NumericExpression newConcreteNumericExpression(
			NumberObject numberObject) {
		return constant(numberObject);
	}

	@Override
	public NumericSymbolicConstant newNumericSymbolicConstant(
			StringObject name, SymbolicType type) {
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
	public IdealComparator numericComparator() {
		return comparator;
	}

	private BooleanExpression isPositive(Polynomial polynomial) {
		Number number = extractNumber(polynomial);

		if (number == null) {
			return booleanFactory.booleanExpression(SymbolicOperator.LESS_THAN,
					zero(polynomial.type()), polynomial);
		}
		return number.signum() > 0 ? trueExpr : falseExpr;
	}

	private BooleanExpression isNegative(Polynomial polynomial) {
		return isPositive(negate(polynomial));
	}

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

	private BooleanExpression arePositive(Polynomial p1, Polynomial p2) {
		BooleanExpression result = isPositive(p1);

		if (result.isFalse())
			return result;
		return booleanFactory.and(result, isPositive(p2));
	}

	private BooleanExpression areNegative(Polynomial p1, Polynomial p2) {
		BooleanExpression result = isNegative(p1);

		if (result.isFalse())
			return result;
		return booleanFactory.and(result, isNegative(p2));
	}

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

	@Override
	public BooleanExpression lessThan(NumericExpression arg0,
			NumericExpression arg1) {
		NumericExpression difference = subtract(arg1, arg0);

		if (difference instanceof Polynomial)
			return isPositive((Polynomial) difference);
		return isPositive((RationalExpression) difference);
	}

	@Override
	public BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1) {
		NumericExpression difference = subtract(arg1, arg0);

		if (difference instanceof Polynomial)
			return isNonnegative((Polynomial) difference);
		return isNonnegative((RationalExpression) difference);
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
		NumericExpression difference = subtract(arg1, arg0);
		Number number = extractNumber(difference);

		if (number == null)
			return booleanFactory.booleanExpression(SymbolicOperator.EQUALS,
					zero(arg0.type()), difference);
		else
			return number.signum() == 0 ? trueExpr : falseExpr;
	}

	@Override
	public BooleanExpression neq(NumericExpression arg0, NumericExpression arg1) {
		NumericExpression difference = subtract(arg1, arg0);
		Number number = extractNumber(difference);

		if (number == null)
			return booleanFactory.booleanExpression(SymbolicOperator.NEQ,
					zero(arg0.type()), difference);
		else
			return number.signum() != 0 ? trueExpr : falseExpr;
	}

}

/**
 * Add c0*m + c1*m, where m is a monic and c0 and c1 are constants. The answer
 * is (c0+c1)*m, or null if c0+c1=0.
 * 
 * @author siegel
 * 
 */
class MonomialAdder implements BinaryOperator<Monomial> {
	private IdealFactory factory;

	public MonomialAdder(IdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public Monomial apply(Monomial arg0, Monomial arg1) {
		Constant c = factory.add(arg0.monomialConstant(factory),
				arg1.monomialConstant(factory));

		if (c.isZero())
			return null;
		return factory.monomial(c, arg0.monic(factory));
	}
}

/**
 * Multiply p^i*p^j, where p is a NumericPrimitive and i and j are positive
 * IntObjects. The answer is p^{i+j}.
 * 
 * @author siegel
 * 
 */
class PrimitivePowerMultiplier implements BinaryOperator<PrimitivePower> {
	private IdealFactory factory;

	public PrimitivePowerMultiplier(IdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public PrimitivePower apply(PrimitivePower arg0, PrimitivePower arg1) {
		return factory.primitivePower(
				arg0.primitive(factory),
				arg0.primitivePowerExponent(factory).plus(
						arg1.primitivePowerExponent(factory)));
	}
}

class MonomialDivider implements UnaryOperator<Monomial> {
	private IdealFactory factory;
	private Number scalar;
	private NumberFactory numberFactory;

	public MonomialDivider(IdealFactory factory, Number scalar) {
		this.factory = factory;
		this.scalar = scalar;
		this.numberFactory = factory.numberFactory();
	}

	@Override
	public Monomial apply(Monomial arg) {
		return factory.monomial(
				factory.constant(numberFactory.divide(
						arg.monomialConstant(factory).number(), scalar)),
				arg.monic(factory));
	}
}

class MonomialMultiplier implements UnaryOperator<Monomial> {
	private IdealFactory factory;
	private Number scalar;
	private NumberFactory numberFactory;

	public MonomialMultiplier(IdealFactory factory, Number scalar) {
		this.factory = factory;
		this.scalar = scalar;
		this.numberFactory = factory.numberFactory();
	}

	@Override
	public Monomial apply(Monomial arg) {
		return factory.monomial(
				factory.constant(numberFactory.multiply(
						arg.monomialConstant(factory).number(), scalar)),
				arg.monic(factory));
	}
}

class MonomialNegater implements UnaryOperator<Monomial> {
	private IdealFactory factory;

	public MonomialNegater(IdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public Monomial apply(Monomial arg) {
		return factory.negate(arg);
	}
}
