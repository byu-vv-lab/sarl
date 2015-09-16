package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifierFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * Importing this class into test files stored in the same package allows for
 * the availability of common SARL objects.
 * <p>
 * Importation of this class merely provides the declaration statements. To also
 * include the initialization of the SARL objects, the setUp() method must be
 * called.
 * 
 * @see CommonObjects.setUp()
 * 
 * @author danfried
 *
 */
public class CommonObjects {

	static FactorySystem system;

	static PreUniverse preUniv;

	static PrintStream out;

	static SymbolicTypeFactory symbFactory;

	static NumberFactory numFact;

	static NumericExpressionFactory numExprFact;

	static BooleanExpressionFactory boolExprFact;

	static IdealFactory idealFactory;

	static IdealSimplifierFactory idealSimplifierFactory;

	static IdentitySimplifierFactory identitySimplifierFactory;

	static IdealSimplifier idealSimplifier, idealSimp2;

	static Simplifier simp1ifier_xeq5;

	static BooleanExpression p, q, claim1, pThanQ, xeq5, yeq6, trueExpr,
			falseExpr, assumption; // assumption is not initialized, as uses
									// will vary greatly

	static SymbolicExpression symbExpr_xpy; // x + y

	static SymbolicExpression symbExpr_xy; // x * y

	static SymbolicExpression symbExpr_xx; // x^2

	static SymbolicExpression symbExpr_xxy; // x^2 * y

	static SymbolicExpression symbExpr_xyy; // x * y^2

	static SymbolicExpression symbExpr_xpyInt; // (int)x + (int)y

	static SymbolicExpression symbExpr_xyInt; // (int)x * (int)y

	static SymbolicExpression symbExpr_xxInt; // (int)x^2

	static SymbolicExpression symbExpr_xxyInt; // (int)x^2 * (int)y

	static SymbolicExpression symbExpr_xyyInt; // (int)x * (int)y^2

	static NumericSymbolicConstant A, B, C, x, xsqd, y, z, xInt, yInt;

	/*
	 * static SymbolicExpression symbExpr
	 * 
	 * static SymbolicExpression symbExpr
	 * 
	 * static SymbolicExpression symbExpr
	 */

	static SymbolicType realType, integerType;

	static NumericExpression ratNeg1, ratNeg2, ratNeg3, ratNeg5, ratNeg25,
			ratNeg300, rat0, rat1, rat2, rat3, rat4, rat5, rat6, rat20, rat25,
			rat200; // -1.0, -2.0, -3.0, -5.0, -25.0, 0.0, 1.0, 2.0, 3.0, 4.0,
					// 5.0, 6.0, 25.0, 200.0

	static NumericExpression intNeg1, intNeg2, intNeg3, intNeg5, int0, int1,
			int2, int3, int4, int5; // -1, -2, -3, -5, 1, 2, 3, 4, 5

	static NumericExpression xNE, yNE, xpy, xy, xx, x4th, threeX4th, xxy, xyy,
			onePxPxSqdP3x4th, mixedXYTermPoly, bigMixedXYTermPoly, xSqrLess1,
			xSqrP1;

	static NumericExpression xpyInt, xyInt, xxInt, x4thInt, threeX4thInt,
			xxyInt, xyyInt;

	static BoundsObject boundObj, boundObj2, boundObj3;

	static edu.udel.cis.vsl.sarl.IF.number.Number num3, num5, numNeg2000,
			num10000, num0, num10pt5;

	static edu.udel.cis.vsl.sarl.IF.number.Number num3Int, num5Int, num0Int,
			numNeg2000Int, num10000Int, neg1Int;

	/**
	 * Method provides initialization of common SARL Objects declared in the
	 * CommonObjects Class.
	 * <p>
	 * This method is only accessible within the
	 * edu.udel.cis.vsl.sarl.ideal.simplify package.
	 * 
	 * @see CommonObjects
	 */
	static void setUp() {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		out = System.out;
		idealFactory = (IdealFactory) system.expressionFactory()
				.numericFactory();
		idealSimplifierFactory = (IdealSimplifierFactory) Ideal
				.newIdealSimplifierFactory(idealFactory, preUniv);
		boolExprFact = PreUniverses.newIdealFactorySystem().booleanFactory();
		ratNeg1 = (NumericExpression) preUniv.rational(-1).commit();
		ratNeg2 = (NumericExpression) preUniv.rational(-2).commit();
		ratNeg3 = (NumericExpression) preUniv.rational(-3).commit();
		ratNeg5 = (NumericExpression) preUniv.rational(-5).commit();
		ratNeg25 = (NumericExpression) preUniv.rational(-25).commit();
		ratNeg300 = (NumericExpression) preUniv.rational(-300).commit();
		rat0 = (NumericExpression) preUniv.rational(0).commit();
		rat1 = (NumericExpression) preUniv.rational(1).commit();
		rat2 = (NumericExpression) preUniv.rational(2).commit();
		rat3 = (NumericExpression) preUniv.rational(3).commit();
		rat4 = (NumericExpression) preUniv.rational(4).commit();
		rat5 = (NumericExpression) preUniv.rational(5).commit();
		rat6 = (NumericExpression) preUniv.rational(6).commit();
		rat20 = (NumericExpression) preUniv.rational(20).commit();
		rat25 = (NumericExpression) preUniv.rational(25).commit(); // 25.0
		rat200 = (NumericExpression) preUniv.rational(200).commit();
		intNeg1 = (NumericExpression) preUniv.integer(-1).commit();
		intNeg2 = (NumericExpression) preUniv.integer(-2).commit();
		intNeg3 = (NumericExpression) preUniv.integer(-3).commit();
		intNeg5 = (NumericExpression) preUniv.integer(-5).commit();
		int0 = (NumericExpression) preUniv.integer(0).commit();
		int1 = (NumericExpression) preUniv.integer(1).commit();
		int2 = (NumericExpression) preUniv.integer(2).commit();
		int3 = (NumericExpression) preUniv.integer(3).commit();
		int4 = (NumericExpression) preUniv.integer(4).commit();
		int5 = (NumericExpression) preUniv.integer(5).commit();
		// neg1Int = preUniv.integer(-1);
		symbFactory = system.expressionFactory().typeFactory();
		realType = preUniv.realType();
		integerType = preUniv.integerType();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType).commit();
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType).commit();
		z = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType).commit();
		xInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("xInt"), integerType).commit();
		yInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("yInt"), integerType).commit();
		xeq5 = (BooleanExpression) preUniv.equals(x, rat5).commit();
		yeq6 = (BooleanExpression) preUniv.equals(y, rat6).commit();
		simp1ifier_xeq5 = idealSimplifierFactory.newSimplifier(xeq5);
		// not sure if xsqd is necessary
		// xsqd = preUniv.multiply(x, x);
		xpy = (NumericExpression) preUniv.add(x, y).commit();
		symbExpr_xpy = xpy;
		xy = (NumericExpression) preUniv.multiply(x, y).commit();
		symbExpr_xy = xy;
		xx = (NumericExpression) preUniv.power(x, 2).commit();
		xNE = (NumericExpression) preUniv.add(x, rat0).commit();
		yNE = (NumericExpression) preUniv.add(y, rat0).commit();
		x4th = (NumericExpression) preUniv.power(xx, 2).commit();
		threeX4th = (NumericExpression) preUniv.multiply(rat3, x4th).commit();
		xxy = (NumericExpression) preUniv.multiply(xy, x).commit();
		symbExpr_xxy = (NumericExpression) xxy.commit();
		xyy = (NumericExpression) preUniv.multiply(xy, y).commit();
		symbExpr_xyy = (NumericExpression) xyy.commit();
		onePxPxSqdP3x4th = (NumericExpression) preUniv.add(rat1,
				preUniv.add(x, preUniv.add(xx, threeX4th))).commit();
		mixedXYTermPoly = (NumericExpression) preUniv.multiply(
				preUniv.add(onePxPxSqdP3x4th, preUniv.add(xxy, xyy)), xpy)
				.commit();
		bigMixedXYTermPoly = (NumericExpression) preUniv.power(mixedXYTermPoly,
				int3).commit();
		xpyInt = (NumericExpression) preUniv.add(xInt, yInt).commit();
		symbExpr_xpyInt = (NumericExpression) xpyInt.commit();
		xxInt = (NumericExpression) preUniv.multiply(xInt, xInt).commit();
		symbExpr_xxInt = (NumericExpression) xxInt.commit();
		xyInt = (NumericExpression) preUniv.multiply(xInt, yInt).commit();
		symbExpr_xyInt = xyInt;
		xxyInt = (NumericExpression) preUniv.multiply(xyInt, xInt).commit();
		symbExpr_xxyInt = (NumericExpression) xxyInt.commit();
		xyyInt = (NumericExpression) preUniv.multiply(xyInt, yInt).commit();
		symbExpr_xyyInt = (NumericExpression) xyyInt.commit();
		xSqrLess1 = (NumericExpression) preUniv.subtract(xxInt, int1).commit();
		xSqrP1 = (NumericExpression) preUniv.add(xxInt, int1).commit();
		numFact = preUniv.numberFactory();
		num3 = numFact.rational("3");
		num5 = numFact.rational("5");
		numNeg2000 = numFact.rational("-2000");
		num10000 = numFact.rational("10000");
		num0 = numFact.rational("0");
		num10pt5 = numFact.rational("10.5");
		num3Int = numFact.integer("3");
		num0Int = numFact.integer("0");
		num5Int = numFact.integer("5");
		numNeg2000Int = numFact.integer("-2000");
		num10000Int = numFact.integer("10000");
		trueExpr = (BooleanExpression) preUniv.bool(true).commit();
		falseExpr = (BooleanExpression) preUniv.bool(false).commit();
		p = (BooleanExpression) preUniv.falseExpression().commit();
		q = (BooleanExpression) preUniv.falseExpression().commit();
		pThanQ = (BooleanExpression) preUniv.implies(p, q).commit();
	}

}
