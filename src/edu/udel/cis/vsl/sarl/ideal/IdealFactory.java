package edu.udel.cis.vsl.sarl.ideal;

import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.NumberObject;
import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicUniverse;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpressionFactory;

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

	private CommonSymbolicUniverse universe;

	private NumberFactoryIF numberFactory;

	private SymbolicMap emptyMap;

	private SymbolicTypeIF integerType, realType;

	private Monic emptyIntMonic, emptyRealMonic;

	private IntObject oneIntObject;

	private Constant zeroInt, zeroReal, oneInt, oneReal, twoInt, twoReal,
			negOneInt, negOneReal;

	public IdealFactory(CommonSymbolicUniverse universe) {
		this.universe = universe;
		this.numberFactory = universe.numberFactory();
		this.integerType = universe.integerType();
		this.realType = universe.realType();
		this.oneIntObject = universe.intObject(1);
		this.emptyMap = universe.emptySortedMap();
		this.emptyIntMonic = (Monic) universe.canonic(new TrivialMonic(
				integerType, emptyMap));
		this.emptyRealMonic = (Monic) universe.canonic(new TrivialMonic(
				realType, emptyMap));
		this.zeroInt = intConstant(0);
		this.zeroReal = realConstant(0);
		this.oneInt = intConstant(1);
		this.oneReal = realConstant(1);
		this.twoInt = intConstant(2);
		this.twoReal = realConstant(2);
		this.negOneInt = intConstant(-1);
		this.negOneReal = realConstant(-1);
	}

	public SymbolicUniverseIF universe() {
		return universe;
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	public SymbolicExpressionIF canonic(SymbolicExpressionIF expression) {
		return (SymbolicExpressionIF) universe.canonic(expression);
	}

	// Basic symbolic objects...

	public IntObject oneIntObject() {
		return oneIntObject;
	}

	public SymbolicMap singletonMap(SymbolicExpressionIF key,
			SymbolicExpressionIF value) {
		return universe.singletonSortedMap(key, value);
	}

	// Constants...

	public Constant intConstant(int value) {
		return (Constant) universe.canonic(new Constant(integerType, universe
				.numberObject(numberFactory.integer(value))));
	}

	public Constant realConstant(int value) {
		return (Constant) universe.canonic(new Constant(realType, universe
				.numberObject(numberFactory.integerToRational(numberFactory
						.integer(value)))));
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

	// Monics...

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

	private NTMonomial ntMonomial(Constant constant, Monic monic) {
		return (NTMonomial) canonic(new NTMonomial(constant, monic));
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

	public ReducedPolynomial reducedPolynomial(SymbolicTypeIF type,
			SymbolicMap termMap) {
		return (ReducedPolynomial) canonic(new NTReducedPolynomial(type,
				termMap));
	}

	// ReducedPolynomialPowers...

	private NTReducedPolynomialPower ntReducedPolynomialPower(
			ReducedPolynomial polynomial, IntObject exponent) {
		return (NTReducedPolynomialPower) canonic(new NTReducedPolynomialPower(
				polynomial, exponent));
	}

	/**
	 * Returns ReducedPolynomialPower from given polynomial and positive
	 * exponent.
	 * 
	 * @param polynomial
	 *            an instance of ReducedPolynomial
	 * @param exponent
	 *            positive int exponent
	 * @return ReducedPolynomialPower
	 */
	public ReducedPolynomialPower reducedPolynomialPower(
			ReducedPolynomial polynomial, IntObject exponent) {
		if (exponent.isZero())
			throw new IllegalArgumentException(
					"Only applies to positive exponents");
		if (exponent.isOne())
			return polynomial;
		return ntReducedPolynomialPower(polynomial, exponent);
	}

	// MonicFactorizations...

	private MonicFactorization ntMonicFactorization(SymbolicTypeIF type,
			SymbolicMap monicFactorizationMap) {
		return (MonicFactorization) universe.canonic(new NTMonicFactorization(
				type, monicFactorizationMap));
	}

	/**
	 * The map must have form ReducedPolynomial->ReducedPolynomialPower.
	 * 
	 * @param type
	 * @param monicFactorizationMap
	 * @return
	 */
	public MonicFactorization monicFactorization(SymbolicTypeIF type,
			SymbolicMap monicFactorizationMap) {
		// if the map has form Primitive->PrimitivePower, return
		// a Monic.  I.e., if all keys are instances of Primitive.
		if (monicFactorizationMap.size() == 1)
			return (ReducedPolynomialPower) monicFactorizationMap.entries()
					.iterator().next().getValue();
		return ntMonicFactorization(type, monicFactorizationMap);
	}

	// Factorizations...

	private NTFactorization ntFactorization(Constant constant,
			MonicFactorization monicFactorization) {
		return (NTFactorization) canonic(new NTFactorization(constant,
				monicFactorization));
	}

	public Factorization factorization(Constant constant,
			MonicFactorization monicFactorization) {
		// if all keys in monicFactorization map are Primitive,
		// return a monomial
		if (constant.isZero())
			return constant;
		if (constant.isOne())
			return monicFactorization;
		return ntFactorization(constant, monicFactorization);
	}

	// Extract Commonality...

	private MonicFactorization[] extractCommonality(MonicFactorization fact1,
			MonicFactorization fact2) {
		SymbolicTypeIF type = fact1.type();
		// maps from ReducedPolynomial to ReducedPolynomialPower...
		SymbolicMap map1 = fact1.monicFactorizationMap(this);
		SymbolicMap map2 = fact2.monicFactorizationMap(this);
		SymbolicMap commonMap = universe.emptySortedMap();
		SymbolicMap newMap1 = map1, newMap2 = map2;

		for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : map1
				.entries()) {
			ReducedPolynomial polynomial = (ReducedPolynomial) entry.getKey();
			ReducedPolynomialPower ppower1 = (ReducedPolynomialPower) entry
					.getValue();
			ReducedPolynomialPower ppower2 = (ReducedPolynomialPower) map2
					.get(polynomial);

			if (ppower2 != null) {
				IntObject exponent1 = ppower1.polynomialPowerExponent(this);
				IntObject exponent2 = ppower2.polynomialPowerExponent(this);
				IntObject minExponent = exponent1.minWith(exponent2);
				IntObject newExponent1 = exponent1.minus(minExponent);
				IntObject newExponent2 = exponent2.minus(minExponent);

				commonMap = commonMap.put(polynomial,
						reducedPolynomialPower(polynomial, minExponent));
				if (newExponent1.isPositive())
					newMap1 = newMap1.put(polynomial,
							reducedPolynomialPower(polynomial, newExponent1));
				else
					newMap1 = newMap1.remove(polynomial);
				if (newExponent2.isPositive())
					newMap2 = newMap2.put(polynomial,
							reducedPolynomialPower(polynomial, newExponent2));
				else
					newMap2 = newMap2.remove(polynomial);
			}
		}
		return new MonicFactorization[] { monicFactorization(type, commonMap),
				monicFactorization(type, newMap1),
				monicFactorization(type, newMap2) };
	}

	/**
	 * Given two factorizations f1 and f2, this returns an array of length 3
	 * containing 3 factorizations a, g1, g2 (in that order), satisfying
	 * f1=a*g1, f2=a*g2, g1 and g2 have no factors in common, a is a monic
	 * factorization (its constant is 1).
	 */
	public Factorization[] extractCommonality(Factorization fact1,
			Factorization fact2) {
		MonicFactorization[] monicTriple = extractCommonality(
				fact1.monicFactorization(this), fact2.monicFactorization(this));

		return new Factorization[] {
				monicTriple[0],
				factorization(fact1.factorizationConstant(this), monicTriple[1]),
				factorization(fact2.factorizationConstant(this), monicTriple[2]) };
	}

	/**
	 * Addition strategy:
	 * 
	 * <pre>
	 * rat + rat : a/b + c/d = a/(xr) + c/(yr) = (ay+cx)/(xyr)
	 * rat + factpoly : a/b + c = (a+bc)/b
	 * factpoly + factpoly : (a1,f1)+(a2,f2) = (a1,rx)+(a2,ry) = (a1+a2, r(x+y))
	 * factpoly + polypow : (a,f)+p^i = (a, p^jg) + p^i = (a+p^i, p^j(g+p^(i-j)))
	 * factpoly + poly ...
	 * </pre>
	 * 
	 * @param r1
	 *            a RationalExpression
	 * @param r2
	 *            a RationalExpression
	 * @return r1+r2
	 */
	public RationalExpression add(RationalExpression r1, RationalExpression r2) {

		return null;
	}

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

class MonomialAdder implements BinaryOperatorIF {

	private IdealFactory factory;

	private Monomial leadingTerm = null;

	public MonomialAdder(IdealFactory factory) {
		this.factory = factory;
	}

	// this only sees the terms for which both are non-null.
	// never sees the terms that occur in only one of the polys.
	// so not possible to get leading term.
	// leading term will be either leading term from poly0,
	// or leading term from poly1, or leading term discovered
	// here. Take max.

	@Override
	public SymbolicExpressionIF apply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		Constant c0 = ((Monomial) arg0).monomialConstant(factory);
		Constant c1 = ((Monomial) arg1).monomialConstant(factory);
		Constant c2 = (Constant) c0.add(factory, c1);
		Monomial monomial;

		if (c2.value().isZero())
			return null;
		monomial = (Monomial) factory.canonic(new NTMonomial(c2,
				((Monomial) arg0).monic(factory)));
		if (leadingTerm == null
				|| leadingTerm.monic(factory)
						.compareTo(monomial.monic(factory)) > 0)
			leadingTerm = monomial;
		return monomial;
	}

	public Monomial leadingTerm() {
		return leadingTerm;
	}
};
