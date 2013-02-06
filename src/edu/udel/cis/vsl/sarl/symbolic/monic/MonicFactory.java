package edu.udel.cis.vsl.sarl.symbolic.monic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.cast.CastFactory;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpression;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpressionFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class MonicFactory {

	private SymbolicTypeFactory typeFactory;

	private CastFactory castFactory;

	private PowerExpressionFactory powerExpressionFactory;

	private ConcreteFactory concreteFactory;

	private SymbolicTypeIF integerType, realType;

	private Map<SymbolicExpressionKey<MonicMonomial>, MonicMonomial> map = new HashMap<SymbolicExpressionKey<MonicMonomial>, MonicMonomial>();

	private MonicMonomial emptyIntMonic, emptyRealMonic;

	private NumericConcreteExpressionIF oneIntConcrete;

	public MonicFactory(SymbolicTypeFactory typeFactory,
			CastFactory castFactory,
			PowerExpressionFactory powerExpressionFactory,
			ConcreteFactory concreteFactory) {
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
		this.typeFactory = typeFactory;
		this.castFactory = castFactory;
		this.concreteFactory = concreteFactory;
		this.powerExpressionFactory = powerExpressionFactory;
		oneIntConcrete = concreteFactory.oneIntExpression();
		emptyIntMonic = monic(integerType, new PowerExpression[] {});
		emptyRealMonic = monic(realType, new PowerExpression[] {});
	}

	public MonicMonomial emptyIntMonic() {
		return emptyIntMonic;
	}

	public MonicMonomial emptyRealMonic() {
		return emptyRealMonic;
	}

	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public PowerExpressionFactory powerExpressionFactory() {
		return powerExpressionFactory;
	}

	public MonicMonomial monic(SymbolicTypeIF numericType,
			PowerExpression[] factorPowers) {
		return CommonSymbolicExpression.flyweight(map, new MonicMonomial(numericType,
				factorPowers));
	}

	public MonicMonomial monicMonomial(NumericPrimitive expression,
			NumericConcreteExpressionIF exponent) {
		SymbolicTypeIF type;
		PowerExpression powerExpression = powerExpressionFactory
				.powerExpression(expression, exponent);

		assert expression != null;
		type = expression.type();
		if (type.isInteger()) {
			return monic(integerType, new PowerExpression[] { powerExpression });
		} else if (type.isReal()) {
			return monic(realType, new PowerExpression[] { powerExpression });
		} else {
			throw new IllegalArgumentException(
					"Expression not of numeric type: " + expression);
		}
	}

	public MonicMonomial monicMonomial(NumericPrimitive expression) {
		return monicMonomial(expression, oneIntConcrete);
	}

	public MonicMonomial multiply(MonicMonomial monic0, MonicMonomial monic1) {
		PowerExpression[] factors0, factors1;
		int numFactors0, numFactors1, index0 = 0, index1 = 0;
		LinkedList<PowerExpression> factorList = new LinkedList<PowerExpression>();

		assert monic0 != null;
		assert monic1 != null;
		assert monic0.type().equals(monic1.type());
		factors0 = monic0.factorPowers();
		factors1 = monic1.factorPowers();
		numFactors0 = factors0.length;
		numFactors1 = factors1.length;
		while (index0 < numFactors0 && index1 < numFactors1) {
			PowerExpression factorPower0 = factors0[index0];
			PowerExpression factorPower1 = factors1[index1];
			int compare = CommonSymbolicExpression.compare(
					(CommonSymbolicExpression) factorPower0.polynomialPowerBase(),
					(CommonSymbolicExpression) factorPower1.polynomialPowerBase());

			if (compare == 0) {
				factorList.add(powerExpressionFactory.powerExpression(
						factorPower0.polynomialPowerBase(), concreteFactory.addRational(factorPower0
								.exponent(), factorPower1.exponent())));
				index0++;
				index1++;
			} else if (compare > 0) {
				factorList.add(factorPower1);
				index1++;
			} else {
				factorList.add(factorPower0);
				index0++;
			}
		}
		while (index0 < numFactors0) {
			factorList.add(factors0[index0]);
			index0++;
		}
		while (index1 < numFactors1) {
			factorList.add(factors1[index1]);
			index1++;
		}
		return monic(monic0.type(), factorList
				.toArray(new PowerExpression[factorList.size()]));
	}

	public MonicMonomial castToReal(MonicMonomial monic) {
		PowerExpression[] factorPowers = monic.factorPowers();
		int numFactors = factorPowers.length;
		MonicMonomial result = emptyRealMonic;

		for (int i = 0; i < numFactors; i++)
			result = multiply(result, monicMonomial(castFactory
					.realCast((NumericPrimitive) factorPowers[i].polynomialPowerBase()),
					factorPowers[i].exponent()));
		return result;
	}
}
