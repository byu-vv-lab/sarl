package edu.udel.cis.vsl.sarl.symbolic.ideal.simplify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumberIF;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.MonomialFactory;

/**
 * Simplifies a constant map. This take as input a map which associates constant
 * values to factored polynomials. The factored polynomials should be in pseudo
 * primitive form. It simplifies this map by performing Gaussian elimination on
 * the coefficient matrix formed by the monic monomials. Specifically, it
 * separates out the integer and the real entries and works on each separately.
 * In each case, it constructs a matrix in which the rows correspond to map
 * entries and columns correspond to the monics (of the appropriate type) which
 * occur anywhere in the map. The entry in a column is the coefficient of that
 * monic in the factored polynomial which occurs as the key in the map entry. It
 * then performs Gaussian elimination on these matrices to reduce to reduced row
 * echelon form. It then re-constructs the maps in this reduced form.
 * 
 * If an inconsistency exists ( for example, X+Y maps to 0, X maps to 0, Y maps
 * to 1) in the map, this will be discovered in the elimination. In this case,
 * the boolean value false is returned by method reduce. True is returned if
 * there are no problems.
 */
public class LinearSolver {

	private NumberFactoryIF numberFactory;

	private FactoredPolynomialFactory fpFactory;

	private ConcreteFactory concreteFactory;

	private MonomialFactory monomialFactory;

	private RationalNumberIF[][] intMatrix, realMatrix;

	private int numIntConstraints = 0, numRealConstraints = 0;

	private Set<MonicMonomial> intMonicSet = new HashSet<MonicMonomial>();

	private Set<MonicMonomial> realMonicSet = new HashSet<MonicMonomial>();

	private Map<FactoredPolynomial, NumberIF> map;

	private MonicMonomial[] intMonics, realMonics;

	private Map<MonicMonomial, Integer> intIdMap, realIdMap;

	LinearSolver(IdealSimplifier simplifier) {
		this.numberFactory = simplifier.numberFactory();
		this.fpFactory = simplifier.fpFactory();
		this.concreteFactory = simplifier.concreteFactory();
		this.monomialFactory = simplifier.monomialFactory();
	}

	/**
	 * Extracts the monics that are used in the map and initializes data
	 * structures. The following are initialized: intMonicSec, realMonicSet,
	 * intMonics, realMonics, intIdMap, realIdMap.
	 */
	private void extractMonics() {
		int numIntMonics, numRealMonics, i;

		for (FactoredPolynomial fp : map.keySet()) {
			Set<MonicMonomial> monics;

			if (fp.type().isInteger()) {
				numIntConstraints++;
				monics = intMonicSet;

			} else {
				numRealConstraints++;
				monics = realMonicSet;
			}
			for (Monomial monomial : fp.polynomial().terms()) {
				MonicMonomial monic = monomial.monicMonomial();

				assert !monic.isOne();
				monics.add(monic);
			}
		}
		numIntMonics = intMonicSet.size();
		numRealMonics = realMonicSet.size();
		intMonics = new MonicMonomial[numIntMonics];
		realMonics = new MonicMonomial[numRealMonics];
		intIdMap = new HashMap<MonicMonomial, Integer>(numIntMonics);
		realIdMap = new HashMap<MonicMonomial, Integer>(numRealMonics);

		i = 0;
		for (MonicMonomial monic : intMonicSet)
			intMonics[i++] = monic;
		i = 0;
		for (MonicMonomial monic : realMonicSet)
			realMonics[i++] = monic;
		// sort into ascending order, i.e., highest degree first:
		Arrays.sort(intMonics);
		Arrays.sort(realMonics);
		for (i = 0; i < numIntMonics; i++)
			intIdMap.put(intMonics[i], i);
		for (i = 0; i < numRealMonics; i++)
			realIdMap.put(realMonics[i], i);
	}

	/**
	 * Builds the matrix representations of the maps. For the integer
	 * constraints, there is one row for each integer entry in the map and one
	 * column for each monic of integer type, plus one additional column to hold
	 * the value associated to the constaint value associated to the map entry.
	 * The real map is similar.
	 */
	private void buildMatrices() {
		int numIntMonics = intMonics.length;
		int numRealMonics = realMonics.length;
		int intConstraintId = 0, realConstraintId = 0;

		intMatrix = new RationalNumberIF[numIntConstraints][numIntMonics + 1];
		realMatrix = new RationalNumberIF[numRealConstraints][numRealMonics + 1];
		for (int i = 0; i < numIntConstraints; i++)
			for (int j = 0; j < numIntMonics; j++)
				intMatrix[i][j] = numberFactory.zeroRational();
		for (int i = 0; i < numRealConstraints; i++)
			for (int j = 0; j < numRealMonics; j++)
				realMatrix[i][j] = numberFactory.zeroRational();
		for (Entry<FactoredPolynomial, NumberIF> entry : map.entrySet()) {
			FactoredPolynomial fp = entry.getKey();
			NumberIF value = entry.getValue();

			if (fp.type().isInteger()) {
				intMatrix[intConstraintId][numIntMonics] = numberFactory
						.rational(value);
				for (Monomial monomial : fp.polynomial().terms()) {
					MonicMonomial monic = monomial.monicMonomial();
					NumberIF coefficient = monomial.coefficient().value();

					intMatrix[intConstraintId][intIdMap.get(monic)] = numberFactory
							.rational(coefficient);
				}
				intConstraintId++;
			} else {
				realMatrix[realConstraintId][numRealMonics] = (RationalNumberIF) value;
				for (Monomial monomial : fp.polynomial().terms()) {
					MonicMonomial monic = monomial.monicMonomial();
					NumberIF coefficient = monomial.coefficient().value();

					realMatrix[realConstraintId][realIdMap.get(monic)] = (RationalNumberIF) coefficient;
				}
				realConstraintId++;
			}
		}
	}

	private boolean rebuildIntMap() {
		int numIntMonics = intMonics.length;

		for (int i = 0; i < numIntConstraints; i++) {
			FactoredPolynomial fp = fpFactory.zeroIntFactoredPolynomial();
			IntegerNumberIF lcm = numberFactory.oneInteger();

			for (int j = 0; j <= numIntMonics; j++) {
				RationalNumberIF a = intMatrix[i][j];

				if (a.signum() != 0) {
					IntegerNumberIF denominator = numberFactory.denominator(a);

					if (!denominator.isOne())
						lcm = numberFactory.lcm(lcm, denominator);
				}
			}
			for (int j = 0; j < numIntMonics; j++) {
				RationalNumberIF a = intMatrix[i][j];

				if (a.signum() != 0) {
					IntegerNumberIF coefficient = numberFactory.multiply(
							numberFactory.numerator(a), numberFactory.divide(
									lcm, numberFactory.denominator(a)));

					fp = fpFactory.addRational(fp, fpFactory
							.factoredPolynomial(monomialFactory.monomial(
									concreteFactory.concrete(coefficient),
									intMonics[j])));
				}
			}
			IntegerNumberIF value = numberFactory.multiply(numberFactory
					.numerator(intMatrix[i][numIntMonics]), numberFactory
					.divide(lcm, numberFactory
							.denominator(intMatrix[i][numIntMonics])));
			// is fp in pseudo primitive form? i think so
			map.put(fp, value);
			if (fp.isZero() && !value.isZero()) // inconsistency
				return false;
		}
		return true;
	}

	private boolean rebuildRealMap() {
		int numRealMonics = realMonics.length;

		for (int i = 0; i < numRealConstraints; i++) {
			FactoredPolynomial fp = fpFactory.zeroRealFactoredPolynomial();
			RationalNumberIF value = realMatrix[i][numRealMonics];

			for (int j = 0; j < numRealMonics; j++) {
				RationalNumberIF a = realMatrix[i][j];

				if (a.signum() != 0) {
					fp = fpFactory.addRational(fp,
							fpFactory.factoredPolynomial(monomialFactory
									.monomial(concreteFactory.concrete(a),
											realMonics[j])));
				}
			}
			map.put(fp, value);
			if (fp.isZero() && !value.isZero()) // inconsistency
				return false;
		}
		return true;
	}

	boolean reduce(Map<FactoredPolynomial, NumberIF> map) {
		this.map = map;

		// Step 1: extract monics. Uses map. Yields intIdMap, realIdMap,
		// intMonics, realMonics.

		// Step 2: build matrices. Uses intIdMap, realIdMap, intMonics,
		// realMonics, map. Yields intMatrix[][], realMatrix[][].

		// Step 3. perform gaussian elim on matrices.

		// Step 4. re-build map. Uses map, intMonics, realMonics, intMatrix,
		// realMatrix. Modifies map.

		extractMonics();
		buildMatrices();
		map.clear();
		numberFactory.gaussianElimination(intMatrix);
		numberFactory.gaussianElimination(realMatrix);
		if (!rebuildIntMap())
			return false;
		if (!rebuildRealMap())
			return false;
		return true;
	}

	public static boolean reduceConstantMap(IdealSimplifier simplifier,
			Map<FactoredPolynomial, NumberIF> map) {
		LinearSolver solver = new LinearSolver(simplifier);

		return solver.reduce(map);
	}
}
