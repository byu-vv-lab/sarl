package edu.udel.cis.vsl.sarl.ideal;

import java.util.Iterator;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.UnaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.collections.CollectionFactory;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpressionFactory;
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

	private SymbolicMap emptyMap;

	private SymbolicTypeIF integerType, realType;

	private Monic emptyIntMonic, emptyRealMonic;

	private IntObject oneIntObject;

	private Constant zeroInt, zeroReal, oneInt, oneReal, twoInt, twoReal,
			negOneInt, negOneReal;

	private MonomialAdder monomialAdder;

	private PrimitivePowerMultiplier primitivePowerMultipler;

	public IdealFactory(NumberFactoryIF numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory) {
		this.numberFactory = numberFactory;
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.collectionFactory = collectionFactory;
		this.integerType = typeFactory.integerType();
		this.realType = typeFactory.realType();
		this.oneIntObject = objectFactory.oneIntObj();
		this.emptyMap = collectionFactory.emptySortedMap();
		this.emptyIntMonic = (Monic) objectFactory.canonic(new TrivialMonic(
				integerType, emptyMap));
		this.emptyRealMonic = (Monic) objectFactory.canonic(new TrivialMonic(
				realType, emptyMap));
		this.zeroInt = canonicIntConstant(0);
		this.zeroReal = canonicIntConstant(0);
		this.oneInt = canonicIntConstant(1);
		this.oneReal = canonicRealConstant(1);
		this.twoInt = canonicIntConstant(2);
		this.twoReal = canonicRealConstant(2);
		this.negOneInt = canonicIntConstant(-1);
		this.negOneReal = canonicRealConstant(-1);
		this.monomialAdder = new MonomialAdder(this);
		this.primitivePowerMultipler = new PrimitivePowerMultiplier(this);
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	// Basic symbolic objects...

	public IntObject oneIntObject() {
		return oneIntObject;
	}

	public SymbolicMap singletonMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value) {
		return collectionFactory.singletonSortedMap(key, value);
	}

	// Constants...

	public Constant intConstant(int value) {
		return new Constant(integerType,
				objectFactory.numberObject(numberFactory.integer(value)));
	}

	private Constant canonicIntConstant(int value) {
		return (Constant) objectFactory.canonic(intConstant(value));
	}

	public Constant realConstant(int value) {
		return new Constant(realType, objectFactory.numberObject(numberFactory
				.integerToRational(numberFactory.integer(value))));
	}

	private Constant canonicRealConstant(int value) {
		return (Constant) objectFactory.canonic(realConstant(value));
	}

	public Constant constant(NumberObject object) {
		return (new Constant(object.isInteger() ? integerType : realType,
				object));
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

	public Constant oneInt() {
		return oneInt;
	}

	public Constant oneReal() {
		return oneReal;
	}

	public Constant one(SymbolicTypeIF type) {
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
			return emptyMonic(type);
		if (monicMap.size() == 1)
			return (PrimitivePower) monicMap.iterator().next();
		return ntMonic(type, monicMap);
	}

	public Monic emptyIntMonic() {
		return emptyIntMonic;
	}

	public Monic emptyRealMonic() {
		return emptyRealMonic;
	}

	public Monic emptyMonic(SymbolicTypeIF type) {
		return type.isInteger() ? emptyIntMonic : emptyRealMonic;
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

	private MonomialSum monomialSum(SymbolicTypeIF type, SymbolicMap termMap) {
		return new MonomialSum(type, termMap);
	}

	private NTPolynomial ntPolynomial(SymbolicMap termMap,
			Monomial factorization) {
		MonomialSum monomialSum = monomialSum(factorization.type(), termMap);

		return new NTPolynomial(monomialSum, factorization);
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

	private Monic[] extractCommonality(Monic fact1, Monic fact2) {
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

	private Constant add(Constant c1, Constant c2) {
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

	private Constant getConstantFactor(MonomialSum sum) {
		return getConstantFactor(sum.type(), sum.termMap());
	}

	private SymbolicMap add(SymbolicMap termMap1, SymbolicMap termMap2) {
		return termMap1.combine(monomialAdder, termMap2);
	}

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

	private Polynomial add(Polynomial p1, Polynomial p2) {
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

	private Constant multiply(Constant c1, Constant c2) {
		return constant(objectFactory.numberObject(numberFactory.multiply(
				c1.number(), c2.number())));
	}

	private SymbolicMap multiply(Constant constant, SymbolicMap termMap) {
		MonomialMultiplier multiplier = new MonomialMultiplier(this,
				constant.number());

		return termMap.apply(multiplier);
	}

	private Polynomial multiply(Constant constant, Polynomial polynomial) {
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

	private Monic multiply(Monic monic1, Monic monic2) {
		return monic(
				monic1.type(),
				monic1.termMap(this).combine(primitivePowerMultipler,
						monic2.termMap(this)));
	}

	private Monomial multiply(Monomial m1, Monomial m2) {
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

	private Polynomial multiply(Monomial monomial, Polynomial polynomial) {
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

	private Polynomial multiply(Polynomial poly1, Polynomial poly2) {
		return polynomial(multiply(poly1.termMap(this), poly2.termMap(this)),
				multiply(poly1.factorization(this), poly2.factorization(this)));
	}

	private RationalExpression multiply(RationalExpression r1,
			RationalExpression r2) {
		// (n1/d1)*(n2/d2)
		return divide(multiply(r1.numerator(this), r2.numerator(this)),
				multiply(r1.denominator(this), r2.denominator(this)));
	}

	/*************************** DIVIDE *******************************/

	// TODO questions: use MonomialSum as arguments instead of SymbolicMap.
	// isolate from universe just those methods used here and put them
	// in their own class for greater modularity so we can test this
	// without everything in universe.

	// TODO: make canonic optional (recursive needed)

	private SymbolicMap divide(SymbolicMap termMap, Constant constant) {
		MonomialDivider divider = new MonomialDivider(this, constant.number());

		return termMap.apply(divider);
	}

	public RationalExpression divide(Polynomial numerator,
			Polynomial denominator) {

		// cancel common factors...
		Monomial[] triple = extractCommonality(numerator.factorization(this),
				denominator.factorization(this));
		if (!triple[0].isOne()) {
			numerator = triple[1].expand(this);
			denominator = triple[2].expand(this);
		}
		// TODO
		Constant denomConstant = null; // = getConstantFactor(denominator);

		if (denomConstant.isOne()) {
			// divide every term in denominator polynomial
			// multiply every term in numerator polynomial
		}
		if (denominator.isOne())
			return numerator;
		if (numerator.isZero())
			return numerator;
		return ntRationalExpression(numerator, denominator);

	}

	private Constant intDivideConstants(Constant c1, Constant c2) {
		return constant(numberFactory.divide((IntegerNumberIF) c1.number(),
				(IntegerNumberIF) c2.number()));
	}

	// (a+b) DIV c
	// (ab) DIV c
	// etc.

	private Polynomial intDividePolynomials(Polynomial poly1, Polynomial poly2) {
		// TODO
		return null;

	}

	/*************************** NEGATE *******************************/

	/*************************** EXPORTED *****************************/

	// Methods specified in interface NumericExpressionFactory...

	@Override
	public SymbolicExpressionIF newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject[] arguments) {
		return new NumericPrimitive(operator, numericType, arguments);
	}

	@Override
	public SymbolicExpressionIF newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0) {
		return new NumericPrimitive(operator, numericType, arg0);
	}

	@Override
	public SymbolicExpressionIF newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0, SymbolicObject arg1) {
		return new NumericPrimitive(operator, numericType, arg0, arg1);
	}

	@Override
	public SymbolicExpressionIF newNumericExpression(SymbolicOperator operator,
			SymbolicTypeIF numericType, SymbolicObject arg0,
			SymbolicObject arg1, SymbolicObject arg2) {
		return new NumericPrimitive(operator, numericType, arg0, arg1, arg2);
	}

	@Override
	public SymbolicExpressionIF add(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF add(SymbolicCollection args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF subtract(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF multiply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF multiply(SymbolicCollection args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF divide(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF modulo(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF minus(SymbolicExpressionIF arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF power(SymbolicExpressionIF base,
			IntObject exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF castToReal(
			SymbolicExpressionIF numericExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumberIF extractNumber(SymbolicExpressionIF expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF newConcreteNumericExpression(
			NumberObject numberObject) {
		return new Constant(
				numberObject.getNumber() instanceof IntegerNumberIF ? integerType
						: realType, numberObject);

	}

	@Override
	public SymbolicConstantIF newNumericSymbolicConstant(StringObject name,
			SymbolicTypeIF type) {
		return new NumericSymbolicConstant(name, type);
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
		Constant c2 = (Constant) c0.plus(factory, c1);
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
