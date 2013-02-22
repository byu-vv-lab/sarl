package edu.udel.cis.vsl.sarl.expr.ideal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;

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

	private NumberFactory numberFactory;

	private IdealFactory idealFactory;

	private RationalNumber[][] intMatrix, realMatrix;

	private int numIntConstraints = 0, numRealConstraints = 0;

	private Set<Monic> intMonicSet = new HashSet<Monic>();

	private Set<Monic> realMonicSet = new HashSet<Monic>();

	private Map<Polynomial, Number> map;

	private Monic[] intMonics, realMonics;

	private Map<Monic, Integer> intIdMap, realIdMap;

	LinearSolver(NumberFactory numberFactory) {
		this.numberFactory = numberFactory;
	}

	/**
	 * Extracts the monics that are used in the map and initializes data
	 * structures. The following are initialized: intMonicSec, realMonicSet,
	 * intMonics, realMonics, intIdMap, realIdMap.
	 */
	private void extractMonics() {
		int numIntMonics, numRealMonics, i;

		for (Polynomial fp : map.keySet()) {
			Set<Monic> monics;

			if (fp.type().isInteger()) {
				numIntConstraints++;
				monics = intMonicSet;

			} else {
				numRealConstraints++;
				monics = realMonicSet;
			}
			for (SymbolicExpression expr : fp.termMap(idealFactory).keys()) {
				Monic monic = (Monic) expr;

				assert !monic.isOne();
				monics.add(monic);
			}
		}
		numIntMonics = intMonicSet.size();
		numRealMonics = realMonicSet.size();
		intMonics = new Monic[numIntMonics];
		realMonics = new Monic[numRealMonics];
		intIdMap = new HashMap<Monic, Integer>(numIntMonics);
		realIdMap = new HashMap<Monic, Integer>(numRealMonics);

		i = 0;
		for (Monic monic : intMonicSet)
			intMonics[i++] = monic;
		i = 0;
		for (Monic monic : realMonicSet)
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

		intMatrix = new RationalNumber[numIntConstraints][numIntMonics + 1];
		realMatrix = new RationalNumber[numRealConstraints][numRealMonics + 1];
		for (int i = 0; i < numIntConstraints; i++)
			for (int j = 0; j < numIntMonics; j++)
				intMatrix[i][j] = numberFactory.zeroRational();
		for (int i = 0; i < numRealConstraints; i++)
			for (int j = 0; j < numRealMonics; j++)
				realMatrix[i][j] = numberFactory.zeroRational();
		for (Entry<Polynomial, Number> entry : map.entrySet()) {
			Polynomial fp = entry.getKey();
			Number value = entry.getValue();

			if (fp.type().isInteger()) {
				intMatrix[intConstraintId][numIntMonics] = numberFactory
						.rational(value);
				for (Entry<Monic, Monomial> term : fp.termMap(idealFactory)
						.entries()) {
					Monomial monomial = term.getValue();
					Monic monic = term.getKey();
					Number coefficient = monomial
							.monomialConstant(idealFactory).number();

					intMatrix[intConstraintId][intIdMap.get(monic)] = numberFactory
							.rational(coefficient);
				}
				intConstraintId++;
			} else {
				realMatrix[realConstraintId][numRealMonics] = (RationalNumber) value;

				for (Entry<Monic, Monomial> term : fp.termMap(idealFactory)
						.entries()) {
					Monomial monomial = term.getValue();
					Monic monic = term.getKey();
					Number coefficient = monomial
							.monomialConstant(idealFactory).number();

					realMatrix[realConstraintId][realIdMap.get(monic)] = (RationalNumber) coefficient;
				}
				realConstraintId++;
			}
		}
	}

	private boolean rebuildIntMap() {
		int numIntMonics = intMonics.length;

		for (int i = 0; i < numIntConstraints; i++) {
			Polynomial fp = idealFactory.zeroInt();
			IntegerNumber lcm = numberFactory.oneInteger();

			for (int j = 0; j <= numIntMonics; j++) {
				RationalNumber a = intMatrix[i][j];

				if (a.signum() != 0) {
					IntegerNumber denominator = numberFactory.denominator(a);

					if (!denominator.isOne())
						lcm = numberFactory.lcm(lcm, denominator);
				}
			}
			for (int j = 0; j < numIntMonics; j++) {
				RationalNumber a = intMatrix[i][j];

				if (a.signum() != 0) {
					IntegerNumber coefficient = numberFactory.multiply(
							numberFactory.numerator(a),
							numberFactory.divide(lcm,
									numberFactory.denominator(a)));

					fp = idealFactory.add(fp, idealFactory.monomial(
							idealFactory.constant(coefficient), intMonics[j]));
				}
			}
			IntegerNumber value = numberFactory.multiply(numberFactory
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
			Polynomial fp = idealFactory.zeroReal();
			RationalNumber value = realMatrix[i][numRealMonics];

			for (int j = 0; j < numRealMonics; j++) {
				RationalNumber a = realMatrix[i][j];

				if (a.signum() != 0) {
					fp = idealFactory.add(fp, idealFactory.monomial(
							idealFactory.constant(a), realMonics[j]));
				}
			}
			map.put(fp, value);
			if (fp.isZero() && !value.isZero()) // inconsistency
				return false;
		}
		return true;
	}

	boolean reduce(Map<Polynomial, Number> map) {
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

	public static boolean reduceConstantMap(NumberFactory numberFactory,
			Map<Polynomial, Number> map) {
		LinearSolver solver = new LinearSolver(numberFactory);

		return solver.reduce(map);
	}
}
