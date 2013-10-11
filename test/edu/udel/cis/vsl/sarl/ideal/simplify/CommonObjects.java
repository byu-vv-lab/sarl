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
 * Importing this class into test files stored in the same package 
 * allows for the availability of common SARL objects.
 * <p>
 * Importation of this class merely provides the declaration statements.
 * To also include the initialization of the SARL objects, the setUp()
 * method must be called.
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
	
	static IdealSimplifier idealSimplifier;
	
	static Simplifier simp1ifier_xeq5;
	
	static BooleanExpression xeq5, trueExpr, falseExpr, assumption; //assumption is not initialized, as uses will vary greatly
	
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
	
	static NumericSymbolicConstant x, xsqd, y, xInt, yInt;
	
	
	/*static SymbolicExpression symbExpr
	
	static SymbolicExpression symbExpr
	
	static SymbolicExpression symbExpr*/
	
	static SymbolicType realType, integerType;
		
	static NumericExpression ratNeg1, ratNeg2, ratNeg3, ratNeg5, ratNeg25, ratNeg300,
	rat0, rat1, rat2, rat3, rat4, rat5, rat6, rat20, rat25, rat200; // -1.0, -2.0, -3.0, -5.0, -25.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 25.0, 200.0
	
	static NumericExpression intNeg1, intNeg2, intNeg3, intNeg5, int0,
	int1, int2, int3, int4, int5; // -1, -2, -3, -5, 1, 2, 3, 4, 5
	
	static NumericExpression xpy, xy, xx, x4th, threeX4th, xxy,
	xyy, onePxPxSqdP3x4th, mixedXYTermPoly, bigMixedXYTermPoly, xSqrLess1, xSqrP1;
	
	static NumericExpression xpyInt, xyInt, xxInt, x4thInt, 
	threeX4thInt, xxyInt, xyyInt;
	
	static BoundsObject boundObj;
	
	static BoundsObject boundObj2;
	
	static edu.udel.cis.vsl.sarl.IF.number.Number num3,
	num5, numNeg2000, num10000, num0, num10pt5;
	
	static edu.udel.cis.vsl.sarl.IF.number.Number num3Int,
	num5Int, num0Int, numNeg2000Int, num10000Int, neg1Int;

	/**
	 * Method provides initialization of common SARL Objects declared 
	 * in the CommonObjects Class.
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
		ratNeg1 = preUniv.rational(-1);
		ratNeg2 = preUniv.rational(-2);
		ratNeg3 = preUniv.rational(-3);
		ratNeg5 = preUniv.rational(-5);
		ratNeg25 = preUniv.rational(-25);
		ratNeg300 = preUniv.rational(-300);
		rat0 = preUniv.rational(0);
		rat1 = preUniv.rational(1);
		rat2 = preUniv.rational(2);
		rat3 = preUniv.rational(3);
		rat4 = preUniv.rational(4);
		rat5 = preUniv.rational(5);
		rat6 = preUniv.rational(6);
		rat20 = preUniv.rational(20);
		rat25 = preUniv.rational(25); //25.0
		rat200 = preUniv.rational(200);
		intNeg1 = preUniv.integer(-1);
		intNeg2 = preUniv.integer(-2);
		intNeg3 = preUniv.integer(-3);
		intNeg5 = preUniv.integer(-5);
		int0 = preUniv.integer(0);
		int1 = preUniv.integer(1);
		int2 = preUniv.integer(2);
		int3 = preUniv.integer(3);
		int4 = preUniv.integer(4);
		int5 = preUniv.integer(5);
		//neg1Int = preUniv.integer(-1);
		symbFactory = system.expressionFactory().typeFactory();
		realType = preUniv.realType();
		integerType = preUniv.integerType();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType);
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType);
		xInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("xInt"), integerType);
		yInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("yInt"), integerType);
		xeq5 = preUniv.equals(x, rat5);
		simp1ifier_xeq5 = idealSimplifierFactory.newSimplifier(xeq5);
		//not sure if xsqd is necessary
		//xsqd = preUniv.multiply(x, x);
		xpy = preUniv.add(x, y);
		symbExpr_xpy = xpy;
		xy = preUniv.multiply(x, y);
		symbExpr_xy = xy;
		xx = preUniv.power(x, 2);
		x4th = preUniv.power(xx, 2);
		threeX4th = preUniv.multiply(rat3, x4th);
		xxy = preUniv.multiply(xy, x);
		symbExpr_xxy = xxy;
		xyy = preUniv.multiply(xy, y);
		symbExpr_xyy = xyy;
		onePxPxSqdP3x4th = preUniv.add(rat1, preUniv.add(x, preUniv.add(xx, threeX4th)));
		mixedXYTermPoly = preUniv.multiply(preUniv.add(onePxPxSqdP3x4th, preUniv.add(xxy, xyy)), xpy);
		bigMixedXYTermPoly = preUniv.power(mixedXYTermPoly, int3);
		xpyInt = preUniv.add(xInt, yInt);
		symbExpr_xpyInt = xpyInt;
		xxInt = preUniv.multiply(xInt, xInt);
		symbExpr_xxInt = xxInt;
		xyInt = preUniv.multiply(xInt, yInt);
		symbExpr_xyInt = xyInt;
		xxyInt = preUniv.multiply(xyInt, xInt);
		symbExpr_xxyInt = xxyInt;
		xyyInt = preUniv.multiply(xyInt, yInt);
		symbExpr_xyyInt = xyyInt;
		xSqrLess1 = preUniv.subtract(xxInt, int1);
		xSqrP1 = preUniv.add(xxInt, int1);
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
		trueExpr = preUniv.bool(true);
		falseExpr = preUniv.bool(false);
	}

}
