package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonObjects {

	static FactorySystem system;

	static PreUniverse preUniv;
	
	static PrintStream out;
	
	static SymbolicTypeFactory symbFactory;
	
	static NumberFactory numFact;
	
	static NumericExpressionFactory numExprFact;
	
	static IdealFactory idealFactory;
	
	static IdealSimplifierFactory simplifierFactory;
	
	static Simplifier simp1ifier_xeq5;
	
	static BooleanExpression xeq5;
	
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
		
	static NumericExpression ratNeg1, ratNeg2, ratNeg3, ratNeg5, 
	rat1, rat2, rat3, rat5; // -1.0, -2.0, -3.0, -5.0, 1.0, 2.0, 3.0, 5.0
	
	static NumericExpression intNeg1, intNeg2, intNeg3, intNeg5,
	int1, int2, int3, int5; // -1, -2, -3, -5, 1, 2, 3, 5
	
	static NumericExpression xpy, xy, xx, x4th, threeX4th, xxy,
	xyy, onePxPxSqdP3x4th;
	
	static NumericExpression xpyInt, xyInt, xxInt, x4thInt, 
	threeX4thInt, xxyInt, xyyInt;
	
	static BoundsObject boundObj;
	
	static edu.udel.cis.vsl.sarl.IF.number.Number num3,
	num5, numNeg2000, num10000, num0, num10pt5;
	
	static edu.udel.cis.vsl.sarl.IF.number.Number num3Int,
	num5Int, num0Int, numNeg2000Int, num10000Int;

	public static void setUp() {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		out = System.out;
		idealFactory = (IdealFactory) system.expressionFactory()
				.numericFactory();
		simplifierFactory = (IdealSimplifierFactory) Ideal
				.newIdealSimplifierFactory(idealFactory, preUniv);
		ratNeg1 = preUniv.rational(-1);
		ratNeg2 = preUniv.rational(-2);
		ratNeg3 = preUniv.rational(-3);
		ratNeg5 = preUniv.rational(-5);
		rat1 = preUniv.rational(1);
		rat2 = preUniv.rational(2);
		rat3 = preUniv.rational(3);
		rat5 = preUniv.rational(5);
		intNeg1 = preUniv.integer(-1);
		intNeg2 = preUniv.integer(-2);
		intNeg3 = preUniv.integer(-3);
		intNeg5 = preUniv.integer(-5);
		int1 = preUniv.integer(1);
		int2 = preUniv.integer(2);
		int3 = preUniv.integer(3);
		int5 = preUniv.integer(5);
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
		simp1ifier_xeq5 = simplifierFactory.newSimplifier(xeq5);
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
	}

}
