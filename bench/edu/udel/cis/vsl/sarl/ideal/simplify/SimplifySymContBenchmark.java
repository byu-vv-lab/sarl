package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;
import java.util.ArrayList;
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
 * Benchmark for IdealSimplifier based on a symbolic constant of increasing degree
 * The assumption of a numeric substitution of a variable in the polynomial
 * is applied, and the time to apply the simplification is reported as a console
 * output.
 * 
 * @see IdealSimplifier.java
 * 
 * @author Marshilla Brahma
 *
 */
public class SimplifySymContBenchmark {
	
	static boolean iterated;
	
	static long start, time;
	
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
	
	static NumericSymbolicConstant A, B, C, x, xsqd, y, z, xInt, yInt, zInt, cInt, divInt;
	
	static SymbolicType realType, integerType;
		
	static NumericExpression ratNeg1, ratNeg2, ratNeg3, ratNeg5, ratNeg25, ratNeg300,
	rat0, rat1, rat2, rat3, rat4, rat5, rat6, rat20, rat25, rat200; // -1.0, -2.0, -3.0, -5.0, -25.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 25.0, 200.0
	
	static NumericExpression intNeg1, intNeg2, intNeg3, intNeg5, int0,
	int1, int2, int3, int4, int5; // -1, -2, -3, -5, 1, 2, 3, 4, 5
	
	static NumericExpression xNE, yNE, xpy, xy, xx, x4th, threeX4th, xxy,
	xyy, onePxPxSqdP3x4th, mixedXYTermPoly, bigMixedXYTermPoly, xSqrLess1, xSqrP1,subSymConst;
	
	static NumericExpression xpyInt, xyInt, xxInt, x4thInt, 
	threeX4thInt, xxyInt, xyyInt;
	
	static int size, n, j;
	
	static ArrayList<NumericSymbolicConstant> subCol;
	
	/**
	 * Beginning of Build up
	 * 
	 */

	public static void main(String[] args) {
		
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
		symbFactory = system.expressionFactory().typeFactory();
		realType = preUniv.realType();
		integerType = preUniv.integerType();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType);
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType);
		z = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType);
		xInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), integerType);
		yInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("yInt"), integerType);
		zInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("zInt"), integerType);
		cInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("c"), integerType);
		divInt = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("divInt"), integerType);
		xeq5 = preUniv.equals(x, rat5);
		simp1ifier_xeq5 = idealSimplifierFactory.newSimplifier(xeq5);
		xpy = preUniv.add(x, y);
		symbExpr_xpy = xpy;
		xy = preUniv.multiply(x, y);
		symbExpr_xy = xy;
		xx = preUniv.power(x, 2);
		xNE = preUniv.add(x, rat0);
		yNE = preUniv.add(y, rat0);
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
		trueExpr = preUniv.bool(true);
		falseExpr = preUniv.bool(false);
		
		subSymConst = xInt;
		assumption = preUniv.equals(preUniv.power(xInt, 6), int2);
		//out.println("assumption : " + assumption);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		//out.println(idealSimplifier);
		subCol = new ArrayList<NumericSymbolicConstant>();
		
		/**
		 * The value of <i>size</i> determines how many iterations of increasing the 
		 * symbolic constant degree are performed.
		 */
		size = 100;
		
		/**
		 * When <i>iterated</i> is set to <b>false</b>, the assumption is only applied to 
		 * the largest polynomial formed, and the time for that transform is reported.
		 * <br></br>
		 * When <i>iterated</i> is set to <b>true</b>, the assumption is applied to each
		 * iterative degree polynomial and the time for each transform is reported.
		 */
		iterated = false;
		
		for(int i = 0; i < size; i++){
			subSymConst = preUniv.multiply(subSymConst, preUniv.add(xInt, cInt));
			if(iterated)
				subCol.add((NumericSymbolicConstant) preUniv.symbolicConstant(
						preUniv.stringObject(subSymConst.toString()), realType));
			n = i+1;
		}
		
		if(!iterated){
			start = System.currentTimeMillis();
			idealSimplifier.apply(subSymConst);
			time = System.currentTimeMillis() - start;
			out.println(n + "   time: " + time);
			out.println(subSymConst);
		}
		else{
			j = 0;
			for(NumericSymbolicConstant i : subCol){
				j++;
				start = System.currentTimeMillis();
				//out.println(i);
				idealSimplifier.apply(i);
				//out.println("simplified: " + i);
				time = System.currentTimeMillis() - start;
				out.println(j + "  time: " + time);
				//out.println();
			}
			out.println(subSymConst);
		}

	}

}
