package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonObjects {

	static FactorySystem system;

	static PreUniverse preUniv;
	
	static PrintStream out;
	
	static SymbolicTypeFactory symbFactory;
	
	static NumberFactory numFact;
	
	static NumericExpressionFactory numExprFact;
	
	static SymbolicExpression symbExpr_xpy; // x + y
	
	static SymbolicExpression symbExpr_xy; // x * y
	
	static SymbolicExpression symbExpr_xxy; // x^2 * y
	
	static SymbolicExpression symbExpr_xyy; // x * y^2
	
	static SymbolicExpression symbExpr_xpyInt; // (int)x + (int)y
	
	static SymbolicExpression symbExpr_xyInt; // (int)x * (int)y
	
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
	xyy;
	
	static NumericExpression xpyInt, xyInt, xxInt, x4thInt, 
	threeX4thInt, xxyInt, xyyInt, onePxPxSqdP3x4th;
	
	static BoundsObject boundObj;
	
	static edu.udel.cis.vsl.sarl.IF.number.Number num3,
	num5, numNeg2000, num10000, num0, num10pt5;
	
	static edu.udel.cis.vsl.sarl.IF.number.Number num3Int,
	num0Int, numNeg2000Int, num10000Int;

	static void setUp() {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		out = System.out;
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
	}

}
