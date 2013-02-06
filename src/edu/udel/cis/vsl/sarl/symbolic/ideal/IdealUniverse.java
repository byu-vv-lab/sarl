package edu.udel.cis.vsl.sarl.symbolic.ideal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.udel.cis.vsl.sarl.IF.Multiplier;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.Exponentiator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.prove.SimplifierIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicUniverse;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF.SymbolicKind;
import edu.udel.cis.vsl.sarl.symbolic.affine.AffineFactory;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayExpression;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayFactory;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayLambdaExpression;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayRead;
import edu.udel.cis.vsl.sarl.symbolic.array.ArrayWrite;
import edu.udel.cis.vsl.sarl.symbolic.cast.CastFactory;
import edu.udel.cis.vsl.sarl.symbolic.cnf.BasicExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.CnfBooleanExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.symbolic.cnf.LiteralExpression;
import edu.udel.cis.vsl.sarl.symbolic.cnf.OrExpression;
import edu.udel.cis.vsl.sarl.symbolic.concrete.BooleanConcreteExpression;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.concrete.NumericConcreteExpression;
import edu.udel.cis.vsl.sarl.symbolic.cond.ConditionalExpression;
import edu.udel.cis.vsl.sarl.symbolic.cond.ConditionalExpressionFactory;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstant;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantExpression;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantFactory;
import edu.udel.cis.vsl.sarl.symbolic.factor.FactorizationFactory;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.function.EvaluatedFunctionExpression;
import edu.udel.cis.vsl.sarl.symbolic.function.EvaluatedFunctionFactory;
import edu.udel.cis.vsl.sarl.symbolic.function.LambdaExpression;
import edu.udel.cis.vsl.sarl.symbolic.ideal.simplify.IdealSimplifier;
import edu.udel.cis.vsl.sarl.symbolic.integer.IntegerOperationFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.MonomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.Polynomial;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.PolynomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpressionFactory;
import edu.udel.cis.vsl.sarl.symbolic.rational.RationalExpression;
import edu.udel.cis.vsl.sarl.symbolic.rational.RationalFactory;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalExpression.RelationKind;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalFactory;
import edu.udel.cis.vsl.sarl.symbolic.tuple.Tuple;
import edu.udel.cis.vsl.sarl.symbolic.tuple.TupleFactory;
import edu.udel.cis.vsl.sarl.symbolic.tuple.TupleRead;
import edu.udel.cis.vsl.sarl.symbolic.tuple.TupleWrite;
import edu.udel.cis.vsl.sarl.symbolic.union.UnionFactory;
import edu.udel.cis.vsl.sarl.symbolic.util.Substituter;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

// Every ideal expression wraps a TreeExpressionIF.
// The ideal expression is determined by this TreeExpressionIF.
// only TreeExpressionIF in "canonical form" are allowed to be so wrapped.
// Canonical forms means CnfBooleanExpression for boolean type, Polynomial for integer type,
// RationalExpression for real type, and for other types there are many possibilities.

// given a TreeExpressionIF, there is at most one ideal expression wrapping it.

// there is a map, idealMap, mapping TreeExpressionIF to IdealExpression

// the method "expression" in IdealExpression returns the TreeExpressionIF wrapped

public class IdealUniverse extends CommonSymbolicUniverse implements
		SymbolicUniverseIF, Multiplier<SymbolicExpressionIF> {

	private AffineFactory affineFactory;

	private ArrayFactory arrayFactory;

	private CastFactory castFactory;

	private CnfFactory cnfFactory;

	private ConcreteFactory concreteFactory;

	private ConditionalExpressionFactory conditionalFactory;

	private EvaluatedFunctionFactory evaluatedFunctionFactory;

	private Vector<SymbolicExpressionIF> expressionVector = new Vector<SymbolicExpressionIF>();

	private FactorizationFactory factorizationFactory;

	private FactoredPolynomialFactory fpFactory;

	private Map<TreeExpressionIF, IdealExpression> idealMap = new HashMap<TreeExpressionIF, IdealExpression>();

	private IntegerOperationFactory integerFactory;

	private MonicFactory monicFactory;

	private MonomialFactory monomialFactory;

	private NumberFactoryIF numberFactory;

	private IntegerIdealExpression oneInt, zeroInt;

	private RealIdealExpression oneReal, zeroReal;

	private PolynomialFactory polynomialFactory;

	private PowerExpressionFactory powerExpressionFactory;

	private RationalFactory rationalFactory;

	private RelationalFactory relationalFactory;

	private SymbolicConstantFactory symbolicConstantFactory;

	private BooleanIdealExpression trueIdeal, falseIdeal;

	private TupleFactory tupleFactory;

	private SymbolicTypeFactory typeFactory;

	private CnfBooleanExpression trueCnf, falseCnf;

	private UnionFactory unionFactory;

	public IdealUniverse(NumberFactoryIF numberFactory) {
		this.numberFactory = numberFactory;
		powerExpressionFactory = new PowerExpressionFactory();
		symbolicConstantFactory = new SymbolicConstantFactory();
		evaluatedFunctionFactory = new EvaluatedFunctionFactory();
		conditionalFactory = new ConditionalExpressionFactory();
		typeFactory = new SymbolicTypeFactory();
		concreteFactory = new ConcreteFactory(typeFactory, numberFactory);
		arrayFactory = new ArrayFactory(concreteFactory);
		integerFactory = new IntegerOperationFactory();
		castFactory = new CastFactory(typeFactory);
		concreteFactory = new ConcreteFactory(typeFactory, numberFactory);
		monicFactory = new MonicFactory(typeFactory, castFactory,
				powerExpressionFactory, concreteFactory);
		monomialFactory = new MonomialFactory(monicFactory, concreteFactory);
		polynomialFactory = new PolynomialFactory(monomialFactory);
		factorizationFactory = new FactorizationFactory(polynomialFactory);
		fpFactory = new FactoredPolynomialFactory(factorizationFactory,
				integerFactory);
		affineFactory = new AffineFactory(fpFactory);
		rationalFactory = new RationalFactory(fpFactory);
		tupleFactory = new TupleFactory();
		zeroInt = concreteExpression(0);
		oneInt = concreteExpression(1);
		zeroReal = (RealIdealExpression) concreteExpression(numberFactory
				.zeroRational());
		oneReal = (RealIdealExpression) concreteExpression(numberFactory
				.oneRational());
		relationalFactory = new RelationalFactory(typeFactory, fpFactory,
				rationalFactory, concreteFactory.zeroIntExpression(),
				concreteFactory.zeroRealExpression());
		cnfFactory = new CnfFactory(relationalFactory, concreteFactory);
		trueCnf = cnfFactory.booleanExpression(true);
		falseCnf = cnfFactory.booleanExpression(false);
		trueIdeal = booleanIdeal(cnfFactory.booleanExpression(true));
		falseIdeal = booleanIdeal(cnfFactory.booleanExpression(false));
		unionFactory = new UnionFactory();
	}

	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public RelationalFactory relationalFactory() {
		return relationalFactory;
	}

	public MonomialFactory monomialFactory() {
		return monomialFactory;
	}

	/**
	 * takes one of the tree expressions used by this universe and puts it into
	 * canonical form.
	 */
	@Override
	public IdealExpression canonicalizeTree(TreeExpressionIF tree) {
		SymbolicTypeIF type = tree.type();

		if (type.isBoolean()) {
			if (tree instanceof BooleanPrimitive) {
				return booleanIdeal(cnfFactory.cnf((BooleanPrimitive) tree));
			} else if (tree instanceof LiteralExpression) {
				return booleanIdeal(cnfFactory.cnf((LiteralExpression) tree));
			} else if (tree instanceof BasicExpression) {
				return booleanIdeal(cnfFactory.cnf((BasicExpression) tree));
			} else if (tree instanceof OrExpression) {
				return booleanIdeal(cnfFactory.cnf((OrExpression) tree));
			} else if (tree instanceof CnfBooleanExpression) {
				return booleanIdeal((CnfBooleanExpression) tree);
			} else if (tree instanceof BooleanConcreteExpression) {
				return (((BooleanConcreteExpression) tree).value() ? trueIdeal
						: falseIdeal);
			} else {
				throw new IllegalArgumentException(
						"Unknown type of boolean expression: " + tree);
			}
		} else if (type.isInteger()) {
			if (tree instanceof NumericConcreteExpression) {
				return integerIdeal(fpFactory
						.factoredPolynomial((NumericConcreteExpression) tree));
			} else if (tree instanceof NumericPrimitive) {
				return integerIdeal(fpFactory
						.factoredPolynomial((NumericPrimitive) tree));
			} else if (tree instanceof MonicMonomial) {
				return integerIdeal(fpFactory
						.factoredPolynomial(monomialFactory
								.monomial((MonicMonomial) tree)));
			} else if (tree instanceof Monomial) {
				return integerIdeal(fpFactory
						.factoredPolynomial((Monomial) tree));
			} else if (tree instanceof Polynomial) {
				return integerIdeal(fpFactory
						.factoredPolynomial((Polynomial) tree));
			} else if (tree instanceof FactoredPolynomial) {
				return integerIdeal((FactoredPolynomial) tree);
			} else {
				throw new IllegalArgumentException(
						"Unknown type of integer expression: " + tree);
			}
		} else if (type.isReal()) {
			if (tree instanceof NumericConcreteExpression) {
				return realIdeal(rationalFactory
						.rational((NumericConcreteExpression) tree));
			} else if (tree instanceof NumericPrimitive) {
				return realIdeal(rationalFactory
						.rational((NumericPrimitive) tree));
			} else if (tree instanceof MonicMonomial) {
				return realIdeal(rationalFactory.rational(fpFactory
						.factoredPolynomial(monomialFactory
								.monomial((MonicMonomial) tree))));
			} else if (tree instanceof Monomial) {
				return realIdeal(rationalFactory.rational(fpFactory
						.factoredPolynomial((Monomial) tree)));
			} else if (tree instanceof Polynomial) {
				return realIdeal(rationalFactory.rational(fpFactory
						.factoredPolynomial((Polynomial) tree)));
			} else if (tree instanceof FactoredPolynomial) {
				return realIdeal(rationalFactory
						.rational((FactoredPolynomial) tree));
			} else if (tree instanceof RationalExpression) {
				return realIdeal((RationalExpression) tree);
			} else {
				throw new IllegalArgumentException(
						"Unknown type of real expression: " + tree);
			}
		} else {
			if (tree instanceof Tuple || tree instanceof TupleRead
					|| tree instanceof TupleWrite || tree instanceof ArrayRead
					|| tree instanceof ArrayWrite
					|| tree instanceof ArrayExpression
					|| tree instanceof EvaluatedFunctionExpression
					|| tree instanceof ConditionalExpression
					|| tree instanceof SymbolicConstantExpression
					|| tree instanceof LambdaExpression
					|| tree instanceof ArrayLambdaExpression) {
				return otherIdeal(tree);
			} else {
				throw new SARLInternalException("Unknown type of expression: "
						+ tree.getClass().getName() + ":\n" + tree);
			}
		}
	}

	public ArrayFactory arrayFactory() {
		return arrayFactory;
	}

	public TupleFactory tupleFactory() {
		return tupleFactory;
	}

	public EvaluatedFunctionFactory functionFactory() {
		return evaluatedFunctionFactory;
	}

	public SymbolicConstantFactory symbolicConstantFactory() {
		return symbolicConstantFactory;
	}

	public PowerExpressionFactory powerFactory() {
		return powerExpressionFactory;
	}

	public RationalFactory rationalFactory() {
		return rationalFactory;
	}

	@Override
	public IdealExpression add(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mismatch:\n" + arg0 + "\n"
					+ arg1);
		if (type.isInteger()) {
			return integerIdeal(fpFactory.add(
					((IntegerIdealExpression) arg0).factoredPolynomial(),
					((IntegerIdealExpression) arg1).factoredPolynomial()));
		} else if (type.isReal()) {
			return realIdeal(rationalFactory.add(
					((RealIdealExpression) arg0).rational(),
					((RealIdealExpression) arg1).rational()));
		} else {
			throw new IllegalArgumentException(
					"Arguments do not have numeric type:\n" + arg0 + "\n"
							+ arg1);
		}
	}

	@Override
	public BooleanIdealExpression and(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mismatch:\n" + arg0 + "\n"
					+ arg1);
		if (type.isBoolean()) {
			return booleanIdeal(cnfFactory.and(
					((BooleanIdealExpression) arg0).cnf(),
					((BooleanIdealExpression) arg1).cnf()));
		} else {
			throw new IllegalArgumentException(
					"Arguments do not have boolean type:\n" + arg0 + "\n"
							+ arg1);
		}
	}

	@Override
	public BooleanIdealExpression and(SymbolicExpressionIF[] args) {
		CnfBooleanExpression cnf = cnfFactory.booleanExpression(true);

		for (SymbolicExpressionIF arg : args) {
			if (!(arg instanceof BooleanIdealExpression)) {
				throw new IllegalArgumentException("Expected boolean type:\n"
						+ arg);
			}
			cnf = cnfFactory.and(cnf, ((BooleanIdealExpression) arg).cnf());
		}
		return booleanIdeal(cnf);
	}

	@Override
	public IdealExpression apply(SymbolicExpressionIF function,
			SymbolicExpressionIF[] arguments) {
		TreeExpressionIF functionTree = ((IdealExpression) function)
				.expression();

		if (functionTree instanceof LambdaExpression) {
			LambdaExpression lambda = (LambdaExpression) functionTree;
			TreeExpressionIF valueExpression = lambda.expression();
			IdealExpression idealValue = ideal(valueExpression);
			SymbolicConstantExpressionIF symbolicConstantExpression = lambda
					.variable();
			SymbolicConstantIF symbolicConstant = symbolicConstantExpression
					.symbolicConstant();
			Map<SymbolicConstantIF, SymbolicExpressionIF> substitutionMap = new HashMap<SymbolicConstantIF, SymbolicExpressionIF>();

			assert arguments.length == 1;
			substitutionMap.put(symbolicConstant, arguments[0]);

			Substituter substituter = new Substituter(this, substitutionMap,
					null);
			IdealExpression result = (IdealExpression) substituter
					.simplify(idealValue);

			return result;
		} else {
			int numArgs = arguments.length;
			TreeExpressionIF[] treeArgs = new TreeExpressionIF[numArgs];
			EvaluatedFunctionExpression evaluatedFunction;

			for (int i = 0; i < numArgs; i++) {
				treeArgs[i] = ((IdealExpression) arguments[i]).expression();
			}
			evaluatedFunction = evaluatedFunctionFactory.evaluatedFunction(
					((IdealExpression) function).expression(), treeArgs);
			return canonicalizeTree(evaluatedFunction);
		}
	}

	@Override
	public IdealExpression arrayLambda(SymbolicCompleteArrayTypeIF arrayType,
			SymbolicExpressionIF function) {
		TreeExpressionIF functionTree = ((IdealExpression) function)
				.expression();
		ArrayLambdaExpression lambda = arrayFactory.arrayLambdaExpression(
				arrayType, functionTree);

		return ideal(lambda);
	}

	@Override
	public IdealExpression arrayRead(SymbolicExpressionIF array,
			SymbolicExpressionIF index) {
		TreeExpressionIF arrayTree = ((IdealExpression) array).expression();

		if (arrayTree instanceof ArrayExpression) {
			ArrayExpression arrayExpression = (ArrayExpression) arrayTree;
			TreeExpressionIF[] elements = arrayExpression.elements();
			NumberIF indexNumber = extractNumber(index);

			if (indexNumber != null) {
				int indexInt = ((IntegerNumberIF) indexNumber).intValue();

				if (indexInt < elements.length) {
					TreeExpressionIF element = elements[indexInt];

					if (element != null)
						return canonicalizeTree(element);
				}
				return canonicalizeTree(arrayFactory.arrayRead(
						arrayExpression.origin(),
						((IdealExpression) index).expression()));
			}
		} else if (arrayTree instanceof ArrayLambdaExpression) {
			ArrayLambdaExpression arrayLambda = (ArrayLambdaExpression) arrayTree;
			TreeExpressionIF function = arrayLambda.function();
			SymbolicExpressionIF idealFunction = ideal(function);
			IdealExpression result = apply(idealFunction,
					new SymbolicExpressionIF[] { index });

			return result;
		}
		return canonicalizeTree(arrayFactory.arrayRead(arrayTree,
				((IdealExpression) index).expression()));
	}

	@Override
	public SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		return typeFactory.arrayType(elementType, extent);
	}

	@Override
	public IdealExpression arrayWrite(SymbolicExpressionIF array,
			SymbolicExpressionIF index, SymbolicExpressionIF value) {
		NumberIF indexNumber = extractNumber(index);
		TreeExpressionIF arrayTree = ((IdealExpression) array).expression();

		if (indexNumber != null) {
			int indexInt = ((IntegerNumberIF) indexNumber).intValue();

			if (arrayTree instanceof ArrayExpression) {
				ArrayExpression arrayExpression = (ArrayExpression) arrayTree;
				TreeExpressionIF[] elements = arrayExpression.elements();
				int length = elements.length;
				int newLength;

				if (indexInt < length) {
					newLength = length;
				} else {
					newLength = indexInt + 1;
				}

				TreeExpressionIF[] newElements = new TreeExpressionIF[newLength];

				for (int i = 0; i < length; i++)
					newElements[i] = elements[i];
				newElements[indexInt] = ((IdealExpression) value).expression();
				return otherIdeal(arrayFactory.arrayExpression(
						arrayExpression.origin(), newElements));
			} else {
				int newLength = indexInt + 1;
				TreeExpressionIF[] newElements = new TreeExpressionIF[newLength];

				newElements[indexInt] = ((IdealExpression) value).expression();
				return otherIdeal(arrayFactory.arrayExpression(arrayTree,
						newElements));
			}
		}
		return otherIdeal(arrayFactory.arrayWrite(arrayTree,
				((IdealExpression) index).expression(),
				((IdealExpression) value).expression()));
	}

	public BooleanIdealExpression booleanIdeal(CnfBooleanExpression cnf) {
		return (BooleanIdealExpression) ideal(cnf);
	}

	@Override
	public Collection<SymbolicConstant> symbolicConstants() {
		return symbolicConstantFactory.symbolicConstants();
	}

	@Override
	public SymbolicTypeIF booleanType() {
		return typeFactory.booleanType();
	}

	@Override
	public RealIdealExpression castToReal(SymbolicExpressionIF numericExpression) {
		return (RealIdealExpression) ideal(rationalFactory.rational(fpFactory
				.castToReal(((IntegerIdealExpression) numericExpression)
						.factoredPolynomial())));
	}

	public CnfFactory cnfFactory() {
		return cnfFactory;
	}

	@Override
	public BooleanIdealExpression concreteExpression(boolean value) {
		return (value ? trueIdeal : falseIdeal);
	}

	@Override
	public IntegerIdealExpression concreteExpression(int value) {
		return integerIdeal(fpFactory.factoredPolynomial(concreteFactory
				.concrete(numberFactory.integer(value))));
	}

	@Override
	public IdealExpression concreteExpression(NumberIF value) {
		if (value instanceof IntegerNumberIF) {
			return integerIdeal(fpFactory.factoredPolynomial(concreteFactory
					.concrete((IntegerNumberIF) value)));
		} else if (value instanceof RationalNumberIF) {
			return realIdeal(rationalFactory.rational(concreteFactory
					.concrete((RationalNumberIF) value)));
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ value);
		}
	}

	public ConcreteFactory concreteFactory() {
		return concreteFactory;
	}

	@Override
	public IdealExpression cond(SymbolicExpressionIF predicate,
			SymbolicExpressionIF trueValue, SymbolicExpressionIF falseValue) {
		if (predicate.equals(trueIdeal))
			return (IdealExpression) trueValue;
		if (predicate.equals(falseIdeal))
			return (IdealExpression) falseValue;
		if (trueValue.type().isBoolean()) {
			return or(and(predicate, trueValue),
					and(not(predicate), falseValue));
		} else {
			return canonicalizeTree(conditionalFactory.conditionalExpression(
					((IdealExpression) predicate).expression(),
					((IdealExpression) trueValue).expression(),
					((IdealExpression) falseValue).expression()));
		}
	}

	@Override
	public IdealExpression divide(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mismatch:\n" + arg0 + "\n"
					+ arg1);
		if (type.isInteger()) {
			return integerIdeal(fpFactory.intDivision(
					((IntegerIdealExpression) arg0).factoredPolynomial(),
					((IntegerIdealExpression) arg1).factoredPolynomial()));
		} else if (type.isReal()) {
			return realIdeal(rationalFactory.divide(
					((RealIdealExpression) arg0).rational(),
					(((RealIdealExpression) arg1)).rational()));
		} else {
			throw new IllegalArgumentException(
					"Arguments do not have numeric type:\n" + arg0 + "\n"
							+ arg1);
		}
	}

	// TODO: equals for arrays, tuples, evaluated function expression, ...
	// create new symbolic expression: equals expression?

	@Override
	public BooleanIdealExpression equals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type0 = arg0.type();

		if (type0.isNumeric())
			return equalsNumeric(arg0, arg1);
		if (type0.isBoolean())
			return equalsBoolean(arg0, arg1);
		throw new IllegalArgumentException("Equals not supported for type "
				+ type0);
	}

	private BooleanIdealExpression equalsNumeric(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();
		IdealExpression difference;
		FactoredPolynomial polynomial;
		NumericConcreteExpressionIF constant;
		TreeExpressionIF expression;

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mistmatch:\n" + arg0
					+ "\n" + arg1);
		difference = subtract(arg0, arg1);
		expression = difference.expression();
		if (type.isInteger()) {
			polynomial = (FactoredPolynomial) expression;
		} else if (type.isReal()) {
			polynomial = ((RationalExpression) expression).numerator();
		} else {
			throw new IllegalArgumentException(
					"Type cannot be used in equals comparison: " + type);
		}
		if (polynomial.isZero())
			return trueIdeal;
		constant = polynomial.factorization().constant();
		assert !constant.isZero();
		if (!constant.isOne())
			polynomial = fpFactory.divide(polynomial, constant);
		return booleanIdeal(cnfFactory.cnf(relationalFactory.relational(
				RelationKind.EQ0, polynomial)));
	}

	private BooleanIdealExpression equalsBoolean(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return or(and(arg0, arg1), and(not(arg0), not(arg1)));
	}

	@Override
	public BooleanIdealExpression exists(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate) {
		if (!predicate.type().isBoolean())
			throw new IllegalArgumentException("Expected boolean type, not:\n"
					+ predicate);
		assert predicate instanceof BooleanIdealExpression;
		return booleanIdeal(cnfFactory.exists(
				symbolicConstantFactory.expression(boundVariable),
				((BooleanIdealExpression) predicate).cnf()));
	}

	@Override
	public Collection<SymbolicExpressionIF> expressions() {
		return expressionVector;
	}

	@Override
	public IdealExpression expressionWithId(int index) {
		return (IdealExpression) expressionVector.elementAt(index);
	}

	@Override
	public Boolean extractBoolean(SymbolicExpressionIF expression) {
		if (trueIdeal.equals(expression))
			return true;
		if (falseIdeal.equals(expression))
			return false;
		return null;
	}

	@Override
	public NumberIF extractNumber(SymbolicExpressionIF expression) {
		if (expression instanceof IntegerIdealExpression) {
			FactoredPolynomial fp = ((IntegerIdealExpression) expression)
					.factoredPolynomial();

			if (fp.isZero())
				return numberFactory.zeroInteger();
			if (fp.degree().signum() == 0) {
				return fp.factorization().constant().value();
			}
		} else if (expression instanceof RealIdealExpression) {
			RationalExpression rational = ((RealIdealExpression) expression)
					.rational();

			if (rational.isZero())
				return numberFactory.zeroRational();

			FactoredPolynomial denominator = rational.denominator();

			if (denominator.isOne()) {
				FactoredPolynomial numerator = rational.numerator();

				if (numerator.degree().signum() == 0) {
					return numerator.factorization().constant().value();
				}
			}
		}
		return null;
	}

	@Override
	public SymbolicConstantIF extractSymbolicConstant(
			SymbolicExpressionIF expression) {
		if (expression instanceof BooleanIdealExpression) {
			CnfBooleanExpression cnf = ((BooleanIdealExpression) expression)
					.cnf();
			BooleanPrimitive primitive = cnfFactory.extractPrimitive(cnf);

			if (primitive instanceof SymbolicConstantExpressionIF) {
				return ((SymbolicConstantExpressionIF) primitive)
						.symbolicConstant();
			}
			return null;
		} else if (expression instanceof IntegerIdealExpression) {
			FactoredPolynomial fp = ((IntegerIdealExpression) expression)
					.factoredPolynomial();

			if (fp.degree().isOne()) {
				Polynomial polynomial = fp.polynomial();
				NumericPrimitive primitive = polynomial.extractPrimitive();

				if (primitive instanceof SymbolicConstantExpressionIF) {
					return ((SymbolicConstantExpressionIF) primitive)
							.symbolicConstant();
				}
			}
			return null;
		} else if (expression instanceof RealIdealExpression) {
			RationalExpression rational = ((RealIdealExpression) expression)
					.rational();

			FactoredPolynomial denominator = rational.denominator();

			if (denominator.isOne()) {
				FactoredPolynomial numerator = rational.numerator();
				Polynomial polynomial = numerator.polynomial();
				NumericPrimitive primitive = polynomial.extractPrimitive();

				if (primitive instanceof SymbolicConstantExpressionIF) {
					return ((SymbolicConstantExpressionIF) primitive)
							.symbolicConstant();
				}
			}
			return null;
		} else if (expression instanceof OtherIdealExpression) {
			SymbolicExpressionIF value = ((OtherIdealExpression) expression)
					.expression();

			if (value instanceof SymbolicConstantExpressionIF) {
				return ((SymbolicConstantExpressionIF) value)
						.symbolicConstant();
			}
			return null;
		} else {
			throw new RuntimeException("Unknown type of idea expression: "
					+ expression);
		}
	}

	public FactoredPolynomialFactory factoredPolynomialFactory() {
		return fpFactory;
	}

	public BooleanIdealExpression forall(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate) {
		if (!predicate.type().isBoolean())
			throw new IllegalArgumentException("Expected boolean type, not:\n"
					+ predicate);
		assert predicate instanceof BooleanIdealExpression;
		return booleanIdeal(cnfFactory.forall(
				symbolicConstantFactory.expression(boundVariable),
				((BooleanIdealExpression) predicate).cnf()));
	}

	public SymbolicFunctionTypeIF functionType(SymbolicTypeIF[] inputTypes,
			SymbolicTypeIF outputType) {
		return typeFactory.functionType(inputTypes, outputType);
	}

	public SymbolicConstantIF getOrCreateSymbolicConstant(String name,
			SymbolicTypeIF type) {
		return symbolicConstantFactory.getOrCreateSymbolicConstant(name, type);
	}

	public SymbolicConstantIF getSymbolicConstant(String name,
			SymbolicTypeIF type) {
		return symbolicConstantFactory.getSymbolicConstant(name, type);
	}

	/**
	 * Takes a tree expression that is already in canonical form and returns
	 * wrapped version.
	 */
	public IdealExpression ideal(TreeExpressionIF expression) {
		IdealExpression ideal = idealMap.get(expression);

		if (ideal == null) {
			SymbolicTypeIF type = expression.type();

			if (type.isBoolean()) {
				if (!(expression instanceof CnfBooleanExpression))
					throw new IllegalArgumentException(
							"Expression of Boolean type is not instanceof CnfBooleanExpression: "
									+ expression);
				ideal = new BooleanIdealExpression(
						(CnfBooleanExpression) expression);
			} else if (type.isInteger()) {
				if (!(expression instanceof FactoredPolynomial))
					throw new IllegalArgumentException(
							"Expression of integer type is not instanceof FactoredPolynomial: "
									+ expression);
				ideal = new IntegerIdealExpression(
						(FactoredPolynomial) expression);
			} else if (type.isReal()) {
				if (!(expression instanceof RationalExpression))
					throw new IllegalArgumentException(
							"Expression of real type is not instanceof RationalExpression: "
									+ expression);
				ideal = new RealIdealExpression((RationalExpression) expression);
			} else if (expression instanceof Tuple
					|| expression instanceof TupleRead
					|| expression instanceof TupleWrite
					|| expression instanceof ArrayRead
					|| expression instanceof ArrayWrite
					|| expression instanceof ArrayExpression
					|| expression instanceof EvaluatedFunctionExpression
					|| expression instanceof ConditionalExpression
					|| expression instanceof SymbolicConstantExpression
					|| expression instanceof LambdaExpression
					|| expression instanceof ArrayLambdaExpression) {
				ideal = new OtherIdealExpression(expression);
			} else {
				throw new SARLInternalException("Unknown kind of expression:\n"
						+ expression);
			}
			ideal.setId(expressionVector.size());
			expressionVector.add(ideal);
			idealMap.put(expression, ideal);
		}
		return ideal;
	}

	private IntegerIdealExpression integerIdeal(FactoredPolynomial fp) {
		return (IntegerIdealExpression) ideal(fp);
	}

	public SymbolicTypeIF integerType() {
		return typeFactory.integerType();
	}

	// factor out constant to make canonic.
	// cP>0 -> P>0. Use factorization.
	// better would be P>c where P has no constant term.

	// ex: 5XY+3 > 0.

	// maybe better would be P>c where P has no constant term and
	// P is monic (for real) or gcd of coeffs of P are 1 (for int).

	// plan: change GT0 to GT(P,c) of above form. this gives bound on P.
	//
	// P>c : P>c, -P<-c
	// P!>c : P<=c, -P>=-c
	// -P>c : P<-c, -P>c
	// -P!>c : P>=c, -P<=c
	//
	// note: in bounds table, only store polynomials with positive leading
	// coefficient.
	// So given P>c, if P has negative leading coefficient, this gives bound
	// -P<-c. Etc.
	//
	// would it be better to use the four relations?
	//
	// just need to convert a literal expression involving relation to a bound
	// expression.
	//
	// why not just have bounds expressions instead of inequality.
	// make new kind of basic expression: bound.
	//
	// what about = !=? e=0, e!=0. P=0. P+c=0: exact bound on P.
	// P != c. P<c || P>c. But will not reduce to a precise bound.

	// (e>0 || p) && (e>1 || q) && ...
	//

	// (e>c || e<c || e>d)

	// organize bounds as finite sequence of intervals, then have canoncial from
	// and only
	// one clause for each expression e in a single or expression.

	// need to be able to take intersection, union, complement of NumericSets.
	// (int or real type).

	// ex: (-infty, 3/2) U (3/2,4] U [4.5, 7) U [8,infty)

	// permits precise reasoning about expression e>c, e=c
	// no more e!=c

	// what about

	// e>c && e>d. these should be combined. or could just be combined during
	// simplification.

	// so two things: in affine expression, bound object, bound map, constant
	// map, must change
	// PrimitiveNumeric to FactoredPolynomial.

	// in bound extraction: for e>0, translate to form P>c, etc. to get bound on
	// polynomial. poynomial will have no constant term

	// substitution: have values for polynomial without constants terms. need to
	// find these: are they primitive? are they monic, monomial, polynomial,
	// factoredpoly?
	// ...might occur as follows:
	//
	// XY=3. 2*Z+3*XY ->... In this case, monic must be in substitution.
	// X+Z=3. X+Y+Z. might not see it. might be multiple ways to perform subs in
	// this case.

	// convert expression to be substituted away to tree form and simplify the
	// obvious parts:
	// 1*X -> X, 0+X->X, etc. Can this be done? Or bring it lowest level
	// possible first,
	// then call .tree(). If poly consists of one term, get the monomial. If
	// lead is 1,
	// get the monic. If one factor, get the primitive.

	// when simplifying, don't just substitute. also use bounds map to simplify
	// expressions of form e>0, e=0, etc. convert to P>c, look up bound.

	// generalized substitution interface: given expression on left, provide
	// method
	// that will return value to substitute.
	// this method will be: if instanceof LiteralExpression && etc. Need to
	// provide
	// a function from SymbolicExpressionIF to SymbolicExpressionIF instead of a
	// Map.

	// need function that takes literal and returns bounds object. every literal
	// involving
	// relation has one....

	// running simplify: and(p,q): parse p, use to simplify q? Store with each
	// p bounds map for expressions occuring within. x=5 && y=6 may also
	// be in p.

	// substitution:

	private CnfBooleanExpression relationExpression(RelationKind kind,
			FactoredPolynomial fp) {
		if (fp.isConstant()) {
			NumberIF value = fp.constantTerm();

			switch (kind) {
			case GT0:
				return (value.signum() > 0 ? trueCnf : falseCnf);
			case GTE0:
				return (value.signum() >= 0 ? trueCnf : falseCnf);
			case EQ0:
				return (value.signum() == 0 ? trueCnf : falseCnf);
			case NEQ0:
				return (value.signum() != 0 ? trueCnf : falseCnf);
			default:
				throw new IllegalArgumentException("unknown kind: " + kind);

			}
		} else {
			return cnfFactory.cnf(relationalFactory.relational(kind, fp));
		}

	}

	// canonical form: for integer type: p<q could be replaced by p+1<=q, i.e.,
	// q-p-1>=0, i.e., !(p-q+1>0).

	// right now: p<q => q-p>0, which is OK for reals, but not canonical for
	// ints.

	// better? maybe add to relations >, >=, =.
	// no more nots:
	// !(a>0) => -a>=0
	// !(a>=0) => -a>0

	private BooleanIdealExpression ltOrLte(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1, boolean strict) {
		SymbolicTypeIF type = arg0.type();
		SymbolicExpressionIF difference;

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mistmatch:\n" + arg0
					+ " (" + type + ")" + "\n" + arg1 + " (" + arg1.type()
					+ ")");
		if (type.isInteger()) {
			assert !strict;
		}
		difference = subtract(arg1, arg0);
		if (difference instanceof IntegerIdealExpression) {
			FactoredPolynomial polynomial = ((IntegerIdealExpression) difference)
					.factoredPolynomial();

			if (polynomial.isZero())
				return (strict ? falseIdeal : trueIdeal);

			NumericConcreteExpressionIF constant = concreteFactory
					.abs(polynomial.factorization().constant());

			assert !constant.isZero();
			if (!constant.isOne())
				polynomial = fpFactory.divide(polynomial, constant);
			return booleanIdeal(relationExpression((strict ? RelationKind.GT0
					: RelationKind.GTE0), polynomial));
		} else if (difference instanceof RealIdealExpression) {
			RationalExpression rational = ((RealIdealExpression) difference)
					.rational();

			if (rational.isZero())
				return (strict ? falseIdeal : trueIdeal);

			FactoredPolynomial numerator = rational.numerator();
			FactoredPolynomial denominator = rational.denominator();
			NumericConcreteExpressionIF constant = concreteFactory
					.abs(numerator.factorization().constant());

			assert !constant.isZero();
			if (!constant.isOne())
				numerator = fpFactory.divide(numerator, constant);

			TreeExpressionIF lhs;

			if (denominator.isOne()) {
				lhs = numerator;
			} else {
				lhs = rationalFactory.rational(numerator, denominator);
			}
			return booleanIdeal(cnfFactory.cnf(relationalFactory.relational(
					(strict ? RelationKind.GT0 : RelationKind.GTE0), lhs)));
		} else {
			throw new IllegalArgumentException(
					"Type cannot be used in equals comparison: " + type);
		}
	}

	@Override
	public IdealExpression lambda(SymbolicConstantIF symbolicConstant,
			SymbolicExpressionIF expression) {
		TreeExpressionIF treeExpression = ((IdealExpression) expression)
				.expression();
		SymbolicConstantExpression symbolicConstantExpression = symbolicConstantFactory
				.expression(symbolicConstant);
		SymbolicFunctionTypeIF functionType = typeFactory.functionType(
				new SymbolicTypeIF[] { symbolicConstant.type() },
				expression.type());
		TreeExpressionIF lambda = evaluatedFunctionFactory.lambda(functionType,
				symbolicConstantExpression, treeExpression);

		return ideal(lambda);
	}

	@Override
	public BooleanIdealExpression lessThan(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		if (arg0.type().isInteger()) {
			return ltOrLte(add(arg0, oneInt), arg1, false);
		} else {
			return ltOrLte(arg0, arg1, true);
		}
	}

	@Override
	public BooleanIdealExpression lessThanEquals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return ltOrLte(arg0, arg1, false);
	}

	@Override
	public IdealExpression minus(SymbolicExpressionIF arg) {
		if (arg instanceof IntegerIdealExpression) {
			return integerIdeal(fpFactory.negate(((IntegerIdealExpression) arg)
					.factoredPolynomial()));
		} else if (arg instanceof RealIdealExpression) {
			return realIdeal(rationalFactory.negate(((RealIdealExpression) arg)
					.rational()));
		} else {
			throw new IllegalArgumentException(
					"Argument does not have numeric type:\n" + arg);
		}
	}

	public IntegerIdealExpression modulo(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		assert arg0 instanceof IntegerIdealExpression;
		assert arg1 instanceof IntegerIdealExpression;
		return integerIdeal(fpFactory.modulo(
				((IntegerIdealExpression) arg0).factoredPolynomial(),
				((IntegerIdealExpression) arg1).factoredPolynomial()));
	}

	public IdealExpression multiply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mismatch:\n" + arg0 + "\n"
					+ arg1);
		if (type.isInteger()) {
			return integerIdeal(fpFactory.multiply(
					((IntegerIdealExpression) arg0).factoredPolynomial(),
					((IntegerIdealExpression) arg1).factoredPolynomial()));
		} else if (type.isReal()) {
			return realIdeal(rationalFactory.multiply(
					((RealIdealExpression) arg0).rational(),
					((RealIdealExpression) arg1).rational()));
		} else {
			throw new IllegalArgumentException(
					"Arguments do not have numeric type:\n" + arg0 + "\n"
							+ arg1);
		}
	}

	public SymbolicConstantIF newSymbolicConstant(String name,
			SymbolicTypeIF type) {
		return symbolicConstantFactory.newSymbolicConstant(name, type);
	}

	public BooleanIdealExpression not(SymbolicExpressionIF arg) {
		if (arg instanceof BooleanIdealExpression) {
			return booleanIdeal(cnfFactory.not(((BooleanIdealExpression) arg)
					.cnf()));
		} else {
			throw new IllegalArgumentException("Boolean type expected:\n" + arg);
		}
	}

	public int numExpression() {
		return expressionVector.size();
	}

	public IntegerIdealExpression oneInt() {
		return oneInt;
	}

	public RealIdealExpression oneReal() {
		return oneReal;
	}

	public BooleanIdealExpression or(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mismatch:\n" + arg0 + "\n"
					+ arg1);
		if (type.isBoolean()) {
			return booleanIdeal(cnfFactory.or(
					((BooleanIdealExpression) arg0).cnf(),
					((BooleanIdealExpression) arg1).cnf()));
		} else {
			throw new IllegalArgumentException(
					"Arguments do not have boolean type:\n" + arg0 + "\n"
							+ arg1);
		}
	}

	public BooleanIdealExpression or(SymbolicExpressionIF[] args) {
		CnfBooleanExpression cnf = cnfFactory.booleanExpression(false);

		for (SymbolicExpressionIF arg : args) {
			if (!(arg instanceof BooleanIdealExpression)) {
				throw new IllegalArgumentException("Expected boolean type:\n"
						+ arg);
			}
			cnf = cnfFactory.or(cnf, ((BooleanIdealExpression) arg).cnf());
		}
		return booleanIdeal(cnf);
	}

	private OtherIdealExpression otherIdeal(TreeExpressionIF expression) {
		return (OtherIdealExpression) ideal(expression);
	}

	public IdealExpression power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent) {
		IntegerNumberIF exponentNumber = (IntegerNumberIF) extractNumber(exponent);
		IdealExpression one = (base.type().isInteger() ? oneInt : oneReal);
		Exponentiator<SymbolicExpressionIF> exponentiator;

		assert exponentNumber != null;
		exponentiator = new Exponentiator<SymbolicExpressionIF>(this, one);
		return (IdealExpression) exponentiator.exp(base, exponentNumber);
	}

	private RealIdealExpression realIdeal(RationalExpression rational) {
		return (RealIdealExpression) ideal(rational);

	}

	public SymbolicTypeIF realType() {
		return typeFactory.realType();
	}

	public SimplifierIF simplifier(SymbolicExpressionIF assumption) {
		return new IdealSimplifier(this, (BooleanIdealExpression) assumption);
	}

	public IdealExpression subtract(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new IllegalArgumentException("Type mismatch:\n" + arg0 + "\n"
					+ arg1);
		if (type.isInteger()) {
			return integerIdeal(fpFactory.subtract(
					((IntegerIdealExpression) arg0).factoredPolynomial(),
					((IntegerIdealExpression) arg1).factoredPolynomial()));
		} else if (type.isReal()) {
			return realIdeal(rationalFactory.subtract(
					((RealIdealExpression) arg0).rational(),
					((RealIdealExpression) arg1).rational()));
		} else {
			throw new IllegalArgumentException(
					"Arguments do not have numeric type:\n" + arg0 + "\n"
							+ arg1);
		}
	}

	public TreeExpressionIF symbolicConstantTreeExpression(
			SymbolicConstantIF symbolicConstant) {
		return symbolicConstantFactory.expression(symbolicConstant);
	}

	public IdealExpression symbolicConstantExpression(
			SymbolicConstantIF symbolicConstant) {
		SymbolicConstantExpression primitive = symbolicConstantFactory
				.expression(symbolicConstant);
		SymbolicTypeIF type = symbolicConstant.type();
		SymbolicTypeKind kind = type.typeKind();
		TreeExpressionIF expression;

		switch (kind) {
		case BOOLEAN:
			expression = cnfFactory.cnf(primitive);
			break;
		case INTEGER:
			expression = fpFactory.factoredPolynomial(primitive);
			break;
		case REAL:
			expression = rationalFactory.rational(primitive);
			break;
		default:
			expression = primitive;
		}
		return ideal(expression);
	}

	public TreeExpressionIF tree(SymbolicExpressionIF expression) {
		return ((IdealExpression) expression).expression();
	}

	public IdealExpression tupleExpression(SymbolicTupleTypeIF type,
			SymbolicExpressionIF[] components) {
		int numComponents = components.length;
		TreeExpressionIF[] treeComponents = new TreeExpressionIF[numComponents];

		for (int i = 0; i < numComponents; i++) {
			treeComponents[i] = ((IdealExpression) components[i]).expression();
		}
		return otherIdeal(tupleFactory.tuple(treeComponents, type));
	}

	public IdealExpression arrayExpression(SymbolicExpressionIF origin,
			SymbolicExpressionIF[] elements) {
		int numElements = elements.length;

		if (numElements == 0)
			return (IdealExpression) origin;

		TreeExpressionIF[] treeElements = new TreeExpressionIF[numElements];

		for (int i = 0; i < numElements; i++) {
			SymbolicExpressionIF element = elements[i];

			if (element != null) {
				treeElements[i] = ((IdealExpression) element).expression();
			}
		}
		return otherIdeal(arrayFactory.arrayExpression(
				((IdealExpression) origin).expression(), treeElements));
	}

	public SymbolicExpressionIF tupleRead(SymbolicExpressionIF tuple, int index) {
		return tupleRead(tuple, concreteExpression(index));
	}

	public IdealExpression getArrayOrigin(SymbolicExpressionIF array) {
		TreeExpressionIF arrayTree = ((IdealExpression) array).expression();

		if (arrayTree instanceof ArrayExpression) {
			ArrayExpression arrayExpression = (ArrayExpression) arrayTree;

			return ideal(arrayExpression.origin());
		} else {
			return (IdealExpression) array;
		}
	}

	public SymbolicExpressionIF[] getArrayElements(SymbolicExpressionIF array) {
		TreeExpressionIF arrayTree = ((IdealExpression) array).expression();

		if (arrayTree instanceof ArrayExpression) {
			ArrayExpression arrayExpression = (ArrayExpression) arrayTree;
			TreeExpressionIF[] treeElements = arrayExpression.elements();
			int numElements = treeElements.length;
			SymbolicExpressionIF[] result = new SymbolicExpressionIF[numElements];

			for (int i = 0; i < numElements; i++) {
				TreeExpressionIF treeElement = treeElements[i];

				if (treeElement != null) {
					result[i] = ideal(treeElement);
				}
			}
			return result;
		} else {
			return new SymbolicExpressionIF[0];
		}
	}

	/**
	 * Returns a tuple read expressions.
	 * 
	 * @arg tuple an OtherIdealExpression of tuple type
	 * @arg index an IntegerIdealExpression which is concretizable
	 * */
	public IdealExpression tupleRead(SymbolicExpressionIF tuple,
			SymbolicExpressionIF index) {
		if (tuple instanceof OtherIdealExpression) {
			TreeExpressionIF tupleExpression = ((IdealExpression) tuple)
					.expression();
			IntegerNumberIF indexNumber = (IntegerNumberIF) extractNumber(index);
			int indexInt;

			assert indexNumber != null;
			indexInt = indexNumber.intValue();
			if (tupleExpression instanceof Tuple) {
				return canonicalizeTree(((Tuple) tupleExpression).components()[indexInt]);
			}
			return canonicalizeTree(tupleFactory.tupleRead(tupleExpression,
					concreteFactory.concrete(indexInt)));
		}
		throw new IllegalArgumentException("Not tuple expression: " + tuple);
	}

	public SymbolicTupleTypeIF tupleType(String name,
			SymbolicTypeIF[] fieldTypes) {
		return typeFactory.tupleType(name, fieldTypes);
	}

	public SymbolicExpressionIF tupleWrite(SymbolicExpressionIF tuple,
			int index, SymbolicExpressionIF value) {
		return tupleWrite(tuple, concreteExpression(index), value);
	}

	public IdealExpression tupleWrite(SymbolicExpressionIF tuple,
			SymbolicExpressionIF index, SymbolicExpressionIF value) {
		TreeExpressionIF tupleExpression = ((OtherIdealExpression) tuple)
				.expression();
		IntegerNumberIF indexNumber = (IntegerNumberIF) extractNumber(index);
		TreeExpressionIF valueExpression = ((IdealExpression) value)
				.expression();
		int indexInt;

		assert indexNumber != null;
		indexInt = indexNumber.intValue();
		if (tupleExpression instanceof Tuple) {
			TreeExpressionIF[] oldComponents = ((Tuple) tupleExpression)
					.components();
			int numComponents = oldComponents.length;
			TreeExpressionIF[] newComponents = new TreeExpressionIF[numComponents];

			for (int i = 0; i < numComponents; i++) {
				newComponents[i] = (i == indexInt ? valueExpression
						: oldComponents[i]);
			}
			return tupleExpression((SymbolicTupleTypeIF) tuple.type(),
					newComponents);
		}
		return otherIdeal(tupleFactory.tupleWrite(tupleExpression,
				concreteFactory.concrete(indexInt), valueExpression));
	}

	public IntegerIdealExpression zeroInt() {
		return zeroInt;
	}

	public RealIdealExpression zeroReal() {
		return zeroReal;
	}

	public IdealExpression make(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicExpressionIF[] arguments) {
		return (IdealExpression) super.make(operator, type, arguments);
	}

	public AffineFactory affineFactory() {
		return affineFactory;
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	@Override
	public SymbolicExpressionIF neq(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return not(equals(arg0, arg1));
	}

	private IntegerIdealExpression lengthOfTreeArray(TreeExpressionIF treeArray) {
		SymbolicTypeIF type = treeArray.type();

		if (type instanceof SymbolicCompleteArrayTypeIF) {
			// complete types specify the extent (=length)...
			return (IntegerIdealExpression) ((SymbolicCompleteArrayTypeIF) type)
					.extent();
		} else if (treeArray instanceof ArrayExpression) {
			// length his same as length of origin array...
			return lengthOfTreeArray(((ArrayExpression) treeArray).origin());
		} else if (treeArray instanceof ArrayWrite) {
			// length is same as length of original array...
			return lengthOfTreeArray(((ArrayWrite) treeArray).array());
		} else {
			return (IntegerIdealExpression) canonicalizeTree(arrayFactory
					.arrayLength(treeArray));
		}

	}

	@Override
	public SymbolicExpressionIF length(SymbolicExpressionIF array) {
		return lengthOfTreeArray(((IdealExpression) array).expression());
	}

	@Override
	public SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType) {
		return typeFactory.arrayType(elementType);
	}

	@Override
	public SymbolicUnionTypeIF unionType(String name,
			SymbolicTypeIF[] memberTypes) {
		return typeFactory.unionType(name, memberTypes);
	}

	@Override
	public SymbolicExpressionIF unionInject(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		// TODO Auto-generated method stub
		// does the argument have to be an Ideal expression?
		return unionFactory.unionInjectExpression(unionType, memberIndex,
				object);
	}

	@Override
	public SymbolicExpressionIF unionTest(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF unionExtract(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}
}
