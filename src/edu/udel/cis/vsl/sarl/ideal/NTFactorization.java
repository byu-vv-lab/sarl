package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A nontrivial factorization of a polynomial. Has two parts: a constant and a
 * MonicFactorization.
 * 
 * @author siegel
 * 
 */
public class NTFactorization extends CommonSymbolicExpression implements
		Factorization {

	protected NTFactorization(Constant constant,
			MonicFactorization monicFactorization) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant,
				monicFactorization);
	}

	public MonicFactorization monicFactorization(IdealFactory factory) {
		return (MonicFactorization) argument(1);
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		return (Constant) argument(0);
	}

	/**
	 * Let f1 be this factorization. Given another factorizations f2, this
	 * returns an array of length 3 containing 3 factorizations a, g1, g2 (in
	 * that order), satisfying f1=a*g1, f2=a*g2, g1 and g2 have no factors in
	 * common, a is a monic factorization.
	 */
	public Factorization[] extractCommonality(IdealFactory factory,
			Factorization that) {
		// TODO
		// just need a way to extract commonality from a 
		// monic factorization map, which is a SymbolicMap
		// in which keys are Polynomial and values PolynomialPower
		MonicFactorization[] monicTriple = null;
				//monicFactorization(factory)
				//.extractCommonality(factory, that.monicFactorization(factory));

		return new Factorization[] {
				monicTriple[0],
				factory.factorization(factorizationConstant(factory),
						monicTriple[1]),
				factory.factorization(that.factorizationConstant(factory),
						monicTriple[2]) };
	}

}
