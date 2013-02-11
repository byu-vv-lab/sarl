package edu.udel.cis.vsl.sarl.ideal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.UnaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.collections.CollectionFactory;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.symbolic.NumericComparator;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpression;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.symbolic.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

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
public class IdealFactory implements NumericExpressionFactory {

	private NumberFactoryIF numberFactory;

	private ObjectFactory objectFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private IdealComparator comparator;

	private Comparator<SymbolicExpressionIF> wrappedComparator;

	private SymbolicMap emptyMap;

	private SymbolicTypeIF integerType, realType;

	private One oneInt, oneReal;

	private IntObject oneIntObject;

	private Constant zeroInt, zeroReal, twoInt, twoReal, negOneInt, negOneReal;

	private MonomialAdder monomialAdder;

	private MonomialNegater monomialNegater;

	private PrimitivePowerMultiplier primitivePowerMultipler;

	public IdealFactory(NumberFactoryIF numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory) {
		this.numberFactory = numberFactory;
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.collectionFactory = collectionFactory;
		this.comparator = new IdealComparator(this);
		this.wrappedComparator = new Comparator<SymbolicExpressionIF>() {
			@Override
			public int compare(SymbolicExpressionIF o1, SymbolicExpressionIF o2) {
				return comparator.compare((IdealExpression) o1,
						(IdealExpression) o2);
			}
		};
		// forms and joins all the comparators...
		objectFactory.formComparators(this);
		this.integerType = typeFactory.integerType();
		this.realType = typeFactory.realType();
		this.oneIntObject = objectFactory.oneIntObj();
		this.emptyMap = collectionFactory.emptySortedMap(wrappedComparator);
		this.oneInt = (One) objectFactory.canonic(new One(integerType,
				objectFactory.numberObject(numberFactory.oneInteger())));
		this.oneReal = (One) objectFactory.canonic(new One(realType,
				objectFactory.numberObject(numberFactory.oneRational())));
		this.zeroInt = canonicIntConstant(0);
		this.zeroReal = canonicIntConstant(0);
		this.twoInt = canonicIntConstant(2);
		this.twoReal = canonicRealConstant(2);
		this.negOneInt = canonicIntConstant(-1);
		this.negOneReal = canonicRealConstant(-1);
		this.monomialAdder = new MonomialAdder(this);
		this.monomialNegater = new MonomialNegater(this);
		this.primitivePowerMultipler = new PrimitivePowerMultiplier(this);
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	public SymbolicTypeIF integerType() {
		return integerType;
	}

	public SymbolicTypeIF realType() {
		return realType;
	}

	// Basic symbolic objects...

	public SymbolicMap emptyMap() {
		return emptyMap;
	}

	public IntObject oneIntObject() {
		return oneIntObject;
	}

	public SymbolicMap singletonMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value) {
		return collectionFactory.singletonSortedMap(wrappedComparator, key,
				value);
	}

	// Constants...

	public Constant intConstant(int value) {
		if (value == 1)
			return oneInt;
		return new NTConstant(integerType,
				objectFactory.numberObject(numberFactory.integer(value)));
	}

	private Constant canonicIntConstant(int value) {
		return (Constant) objectFactory.canonic(intConstant(value));
	}

	public Constant realConstant(int value) {
		if (value == 1)
			return oneReal;
		return new NTConstant(realType,
				objectFactory.numberObject(numberFactory
						.integerToRational(numberFactory.integer(value))));
	}

	private Constant canonicRealConstant(int value) {
		return (Constant) objectFactory.canonic(realConstant(value));
	}

	public Constant constant(NumberObject object) {
		if (object.isOne())
			return object.isInteger() ? oneInt : oneReal;
		return new NTConstant(object.isInteger() ? integerType : realType,
				object);
	}

	public Constant constant(NumberIF number) {
		return constant(objectFactory.numberObject(number));
	}

	public Constant zeroInt() {
		return zeroInt;
	}

	public Constant zeroReal() {
		return zeroReal;
	}

	public Constant zero(SymbolicTypeIF type) {
		return type.isInteger() ? zeroInt : zeroReal;
	}

	public One oneInt() {
		return oneInt;
	}

	public One oneReal() {
		return oneReal;
	}

	public One one(SymbolicTypeIF type) {
		return type.isInteger() ? oneInt : oneReal;
	}

	public Constant twoInt() {
		return twoInt;
	}

	public Constant twoReal() {
		return twoReal;
	}

	public Constant two(SymbolicTypeIF type) {
		return type.isInteger() ? twoInt : twoReal;
	}

	public Constant negOneInt() {
		return negOneInt;
	}

	public Constant negOneReal() {
		return negOneReal;
	}

	public Constant negOne(SymbolicTypeIF type) {
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

	private NTMonic ntMonic(SymbolicTypeIF type, SymbolicMap monicMap) {
		return new NTMonic(type, monicMap);
	}

	public Monic monic(SymbolicTypeIF type, SymbolicMap monicMap) {
		if (monicMap.isEmpty())
			return one(type);
		if (monicMap.size() == 1)
			return (PrimitivePower) monicMap.iterator().next();
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
	public ReducedPolynomial reducedPolynomial(SymbolicTypeIF type,
			SymbolicMap termMap) {
		return new ReducedPolynomial(type, termMap);
	}

	private NTPolynomial ntPolynomial(SymbolicMap termMap,
			Monomial factorization) {
		return new NTPolynomial(termMap, factorization);
	}

	public Polynomial polynomial(SymbolicMap termMap, Monomial factorization) {
		if (termMap.size() == 0)
			return zero(factorization.type());
		if (termMap.size() == 1)
			return (Monomial) termMap.iterator().next();
		return ntPolynomial(termMap, factorization);
	}

	// Rational expressions

	private NTRationalExpression ntRationalExpression(Polynomial numerator,
			Polynomial denominator) {
		return new NTRationalExpression(numerator, denominator);
	}

	/************************ FACTORIZATION ***************************/

	// Extract Commonality...

	public Monic[] extractCommonality(Monic fact1, Monic fact2) {
		SymbolicTypeIF type = fact1.type();
		// maps from ReducedPolynomial to ReducedPolynomialPower...
		SymbolicMap map1 = fact1.monicFactors(this);
		SymbolicMap map2 = fact2.monicFactors(this);
		SymbolicMap commonMap = collectionFactory.emptySortedMap();
		SymbolicMap newMap1 = map1, newMap2 = map2;

		for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : map1
				.entries()) {
			NumericPrimitive base = (NumericPrimitive) entry.getKey();
			PrimitivePower ppower1 = (PrimitivePower) entry.getValue();
			PrimitivePower ppower2 = (PrimitivePower) map2.get(base);

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

	private Constant getConstantFactor(SymbolicTypeIF type, SymbolicMap termMap) {
		if (type.isReal())
			return ((Monomial) termMap.iterator().next())
					.monomialConstant(this);
		else {
			Iterator<SymbolicExpressionIF> monomialIter = termMap.iterator();
			IntegerNumberIF gcd = (IntegerNumberIF) ((Monomial) monomialIter
					.next()).monomialConstant(this).number();
			boolean isNegative = gcd.signum() < 0;

			if (isNegative)
				gcd = numberFactory.negate(gcd);
			while (monomialIter.hasNext()) {
				gcd = numberFactory.gcd(gcd, (IntegerNumberIF) numberFactory
						.abs(((Monomial) monomialIter.next()).monomialConstant(
								this).number()));
				if (gcd.isOne())
					break;
			}
			return constant(gcd);
		}
	}

	private SymbolicMap add(SymbolicMap termMap1, SymbolicMap termMap2) {
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
		SymbolicTypeIF type = p1.type();
		SymbolicMap newMap = add(p1.termMap(this), p2.termMap(this));

		if (newMap.isEmpty())
			return zero(type);
		if (newMap.size() == 1)
			return (Monomial) newMap.iterator().next();
		else {
			Monomial factorization;
			Constant c = getConstantFactor(type, newMap);

			if (c.isOne())
				factorization = reducedPolynomial(type, newMap);
			else
				factorization = monomial(c,
						reducedPolynomial(type, divide(newMap, c)));
			return polynomial(newMap, factorization);
		}
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

	private SymbolicMap multiply(Constant constant, SymbolicMap termMap) {
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
			SymbolicMap oldTermMap = polynomial.termMap(this);
			SymbolicMap newTermMap = multiply(constant, oldTermMap);
			Monomial oldFactorization = polynomial.factorization(this);
			Monomial newFactorization = monomial(
					multiply(constant, oldFactorization.monomialConstant(this)),
					oldFactorization.monic(this));

			return polynomial(newTermMap, newFactorization);
		}
	}

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

	private SymbolicMap multiply(Monomial monomial, SymbolicMap termMap) {
		SymbolicMap result = collectionFactory.emptySortedMap();

		for (SymbolicExpressionIF expr : termMap) {
			Monomial m = (Monomial) expr;
			Monomial product = multiply(monomial, m);

			result = result.put(product.monic(this), product);
		}
		return result;
	}

	public Polynomial multiply(Monomial monomial, Polynomial polynomial) {
		return polynomial(multiply(monomial, polynomial.termMap(this)),
				multiply(monomial, polynomial.factorization(this)));
	}

	private SymbolicMap multiply(SymbolicMap termMap1, SymbolicMap termMap2) {
		SymbolicMap result = collectionFactory.emptySortedMap();

		for (SymbolicExpressionIF expr : termMap1) {
			Monomial monomial = (Monomial) expr;
			SymbolicMap product = multiply(monomial, termMap2);

			result = add(result, product);
		}
		return result;
	}

	public Polynomial multiply(Polynomial poly1, Polynomial poly2) {
		if (poly1.isZero())
			return poly1;
		if (poly2.isZero())
			return poly2;
		if (poly1.isOne())
			return poly2;
		if (poly2.isOne())
			return poly1;
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

	private SymbolicMap divide(SymbolicMap termMap, Constant constant) {
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
		return constant(numberFactory.divide((IntegerNumberIF) c1.number(),
				(IntegerNumberIF) c2.number()));
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

	private SymbolicMap negate(SymbolicMap termMap) {
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
			SymbolicTypeIF numericType, SymbolicObject[] arguments) {
		return new NumericPrimitive(operator, numericType, arguments);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0) {
		return new NumericPrimitive(operator, numericType, arg0);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0, SymbolicObject arg1) {
		return new NumericPrimitive(operator, numericType, arg0, arg1);
	}

	@Override
	public NumericPrimitive newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0,
			SymbolicObject arg1, SymbolicObject arg2) {
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
	public NumericExpression add(SymbolicCollection args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpressionIF arg : args) {
			if (result == null)
				result = (NumericExpression) arg;
			else
				result = add(result, (NumericExpression) arg);
		}
		return result;
	}

	private NumericExpression addWithCast(SymbolicCollection args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpressionIF arg : args) {
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
	public NumericExpression multiply(SymbolicCollection args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpressionIF arg : args) {
			if (result == null)
				result = (NumericExpression) arg;
			else
				result = multiply(result, (NumericExpression) arg);
		}
		return result;
	}

	private NumericExpression multiplyWithCast(SymbolicCollection args) {
		int size = args.size();
		NumericExpression result = null;

		if (size == 0)
			throw new IllegalArgumentException(
					"Collection must contain at least one element");
		for (SymbolicExpressionIF arg : args) {
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
		return divide((RationalExpression) arg0, (RationalExpression) arg1);
	}

	@Override
	public NumericExpression modulo(NumericExpression arg0,
			NumericExpression arg1) {
		return intModulusPolynomials((Polynomial) arg0, (Polynomial) arg1);
	}

	@Override
	public NumericExpression minus(NumericExpression arg) {
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
			if (kind0 == SymbolicObjectKind.EXPRESSION_COLLECTION)
				return addWithCast((SymbolicCollection) arg0);
			else
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
			if (kind0 == SymbolicObjectKind.EXPRESSION_COLLECTION)
				return multiplyWithCast((SymbolicCollection) arg0);
			else
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
	public NumberIF extractNumber(NumericExpression expression) {
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
			StringObject name, SymbolicTypeIF type) {
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
	public NumericComparator numericComparator() {
		return comparator;
	}

}

/**
 * Add c0*m + c1*m, where m is a monic and c0 and c1 are constants. The answer
 * is (c0+c1)*m, or null if c0+c1=0.
 * 
 * @author siegel
 * 
 */
class MonomialAdder implements BinaryOperatorIF {
	private IdealFactory factory;

	public MonomialAdder(IdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		Constant c0 = ((Monomial) arg0).monomialConstant(factory);
		Constant c1 = ((Monomial) arg1).monomialConstant(factory);
		Constant c2 = factory.add(c0, c1);
		Monomial monomial;

		if (c2.isZero())
			return null;
		monomial = factory.ntMonomial(c2, ((Monomial) arg0).monic(factory));
		return monomial;
	}
}

/**
 * Multiply p^i*p^j, where p is a NumericPrimitive and i and j are positive
 * IntObjects. The answer is p^{i+j}.
 * 
 * @author siegel
 * 
 */
class PrimitivePowerMultiplier implements BinaryOperatorIF {
	private IdealFactory factory;

	public PrimitivePowerMultiplier(IdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		NumericPrimitive base = ((PrimitivePower) arg0).primitive(factory);
		IntObject exp0 = ((PrimitivePower) arg0)
				.primitivePowerExponent(factory);
		IntObject exp1 = ((PrimitivePower) arg1)
				.primitivePowerExponent(factory);
		IntObject newExponent = exp0.plus(exp1);
		PrimitivePower result = factory.primitivePower(base, newExponent);

		return result;
	}
}

class MonomialDivider implements UnaryOperatorIF {
	private IdealFactory factory;
	private NumberIF scalar;
	private NumberFactoryIF numberFactory;

	public MonomialDivider(IdealFactory factory, NumberIF scalar) {
		this.factory = factory;
		this.numberFactory = factory.numberFactory();
	}

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF arg) {
		Monomial oldMonomial = (Monomial) arg;
		Constant oldConstant = oldMonomial.monomialConstant(factory);
		NumberIF oldCoefficient = oldConstant.number();
		NumberIF newCoefficient = numberFactory.divide(oldCoefficient, scalar);
		Constant newConstant = factory.constant(newCoefficient);
		Monomial newMonomial = factory.monomial(newConstant,
				oldMonomial.monic(factory));

		return newMonomial;
	}
}

class MonomialMultiplier implements UnaryOperatorIF {
	private IdealFactory factory;
	private NumberIF scalar;
	private NumberFactoryIF numberFactory;

	public MonomialMultiplier(IdealFactory factory, NumberIF scalar) {
		this.factory = factory;
		this.numberFactory = factory.numberFactory();
	}

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF arg) {
		Monomial oldMonomial = (Monomial) arg;
		Constant oldConstant = oldMonomial.monomialConstant(factory);
		NumberIF oldCoefficient = oldConstant.number();
		NumberIF newCoefficient = numberFactory
				.multiply(oldCoefficient, scalar);
		Constant newConstant = factory.constant(newCoefficient);
		Monomial newMonomial = factory.monomial(newConstant,
				oldMonomial.monic(factory));

		return newMonomial;
	}
}

class MonomialNegater implements UnaryOperatorIF {
	private IdealFactory factory;

	public MonomialNegater(IdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF arg) {
		return factory.negate((Monomial) arg);
	}
}
