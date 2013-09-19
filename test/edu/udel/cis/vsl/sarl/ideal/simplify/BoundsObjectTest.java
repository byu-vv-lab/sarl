package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class BoundsObjectTest {
	
	private static FactorySystem system;
	
	private static PrintStream out = System.out;
	
	private static SymbolicTypeFactory symbFactory;
	
	private static SymbolicExpression symbExpr_xpy;
	
	private static SymbolicExpression symbExpr_xy;
	
	private static SymbolicExpression symbExpr_xxy;
	
	private static SymbolicExpression symbExpr_xyy;
	
	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;
	
	private static NumericExpressionFactory numExprFact;
	
	private static SymbolicType realType;

	private static SymbolicType integerType;
	
	private static PreUniverse preUniv;
	
	private static NumericExpression xpy;
	
	private static NumericExpression xy;
	
	private static NumericExpression xyy;
	
	private static NumericExpression xxy;
	
	private static BoundsObject boundObj;
	
	private static BoundsObject boundObj_Integral;
	
	private static BoundsObject boundObj_xxy_L_S; //strict lower bound of -2000 for x*x*y
	
	private static BoundsObject boundObj_xxy_L_NS; //non-strict lower bound of -2000 for x*x*y
	
	private static BoundsObject boundObj_xxy_U_S; //strict upper bound
	
	private static BoundsObject boundObj_xxy_U_NS; //non-strict upper bound
	
	private static BoundsObject boundObj_xxy_U_S_0; //boundOBject used to set another's lower and then be expanded on upward
	
	private static BoundsObject boundObj_xxy_L_S_0; //boundOBject used to set another's upper and then be expanded on downward
	
	
	/*private static BoundsObject boundObj2; //strict upper and lower bounds
	
	private static BoundsObject boundObj3; //strict upper bound, non-strict lower
	
	private static BoundsObject boundObj4; //non-strict upper bound, strict lower
	
	private static BoundsObject boundObj5; //non-strict upper and lower bounds
	
	private static BoundsObject boundObj6; //strict upper bound, no lower
	
	private static BoundsObject boundObj7; //non-strict upper bound, no lower
	
	private static BoundsObject boundObj8; //no upper bound, strict lower
	
	private static BoundsObject boundObj9; //no upper bound, non-strict lower
*/	
	private static NumberFactory numFact;
	
	//private static Number numBound;
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound3;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound5;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBoundNeg2000;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBoundPos10000;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound0;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound0_int;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound_neg2000_int;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound10000_int;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound10pt5;
	
	private static NumericExpression one, two, five;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		one = preUniv.rational(1); // 1.0
		two = preUniv.rational(2); // 2.0
		five = preUniv.rational(5); // 5.0
		//system = PreUniverses.newIdealFactorySystem();
		symbFactory = system.expressionFactory().typeFactory();
		realType = preUniv.realType();
		integerType = preUniv.integerType();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType);
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType);
		xpy = preUniv.add(x, y);
		symbExpr_xpy = xpy;
		xy = preUniv.multiply(x, y);
		symbExpr_xy = xy;
		xxy = preUniv.multiply(xy, x);
		symbExpr_xxy = xxy;
		xyy = preUniv.multiply(xy, y);
		symbExpr_xyy = xyy;
		
		numFact = preUniv.numberFactory();
		//numBound = preUniv.
//		numbFact = preUniv.  //numExprFact.numberFactory();
//		numbBound = preUniv.  //numbFact.rational("three");
		numBound3 = numFact.rational("3");
		numBound5 = numFact.rational("5");
		numBoundNeg2000 = numFact.rational("-2000");
		numBoundPos10000 = numFact.rational("10000");
		numBound0 = numFact.rational("0");
		numBound0_int = numFact.integer("0");
		numBound_neg2000_int = numFact.integer("-2000");
		numBound10000_int = numFact.integer("10000");
		numBound10pt5 =numFact.rational("10.5");
		boundObj = BoundsObject.newTightBound(symbExpr_xpy, numBound3);
		boundObj_xxy_L_S = BoundsObject.newLowerBound(symbExpr_xxy, numBoundNeg2000, true);
		boundObj_xxy_L_NS = BoundsObject.newLowerBound(symbExpr_xxy, numBoundNeg2000, false);
		boundObj_xxy_U_S = BoundsObject.newUpperBound(symbExpr_xxy, numBoundPos10000, true);
		boundObj_xxy_U_NS = BoundsObject.newUpperBound(symbExpr_xxy, numBoundPos10000, false);
		boundObj_Integral= BoundsObject.newLowerBound(symbExpr_xxy, numBoundNeg2000, true); //begins with strict lower
		boundObj_xxy_U_S_0 = BoundsObject.newUpperBound(symbExpr_xxy, numBound0, true);
		boundObj_xxy_L_S_0 = BoundsObject.newLowerBound(symbExpr_xxy, numBound0, true);
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void equals(){
		//test on .equals for equal objects
		BoundsObject boundObjEquals = boundObj;
		assertEquals(boundObj, boundObjEquals); //expected to pass; both objects are equal
		assertTrue(boundObj.equals(boundObjEquals));
		out.println("boundObj = " + boundObj.toString() + " boundObjEquals = " + boundObjEquals.toString());
		out.println("boundObj(xpy,3) equals? boundObjEquals(xpy, 3): " + boundObj.equals(boundObjEquals));
		assertEquals(numBound3, boundObj.constant());
		
		//test on .equals for unequal objects, on basis of strict vs non-strict (lower bound)
		assertThat(boundObj_xxy_L_S, is(not(boundObj_xxy_L_NS))); //expected to pass, as these objects are not equal
		assertFalse(boundObj_xxy_L_S.equals(boundObj_xxy_L_NS));
		BoundsObject bObjEq_L_S = boundObj_xxy_L_S;
		BoundsObject bObjEq_L_NS = boundObj_xxy_L_NS;
		//equals l-bound, Strict
		assertTrue(boundObj_xxy_L_S.equals(bObjEq_L_S));
		assertTrue(boundObj_xxy_L_NS.equals(bObjEq_L_NS));
		
		//test on .equals for unequal objects, on basis of expr
		boundObjEquals = BoundsObject.newTightBound(symbExpr_xy, numBound3);
		assertThat(boundObj, is(not(boundObjEquals))); //expected to pass, as these objects are not equal
		assertFalse(boundObj.equals(boundObjEquals));
		out.println("boundObj = " + boundObj.toString() + " boundObjEquals = " + boundObjEquals.toString());
		out.println("boundObj(xpy,3) equals? boundObjEquals(xy, 3): " + boundObj.equals(boundObjEquals));
		
		//test on .equals for unequal objects, on basis of bound
		boundObjEquals = BoundsObject.newTightBound(symbExpr_xpy, numBound5);
		assertThat(boundObj, is(not(boundObjEquals))); //expected to pass, as these objects are not equal
		assertFalse(boundObj.equals(boundObjEquals));
		assertFalse(boundObjEquals.equals(boundObj));
		out.println("boundObj = " + boundObj.toString() + " boundObjEquals = " + boundObjEquals.toString());
		out.println("boundObj(xpy,3) equals? boundObjEquals(xpy, 5): " + boundObj.equals(boundObjEquals));
		assertEquals(boundObj.symbolicConstant(), boundObjEquals.symbolicConstant());
		
		//test on .equals for unequal objects, on basis of bound and expr
		boundObjEquals = BoundsObject.newTightBound(symbExpr_xy, numBound5);
		assertThat(boundObj, is(not(boundObjEquals))); //expected to pass, as these objects are not equal
		assertFalse(boundObj.equals(boundObjEquals));
		assertFalse(boundObjEquals.equals(boundObj));
		
		//test on isConsistent() method 
		assertEquals(true, boundObj.isConsistent());
		assertEquals(true, boundObjEquals.isConsistent());
		assertEquals(true, boundObj_xxy_L_S.isConsistent());
		assertEquals(true, boundObj_xxy_L_NS.isConsistent());
		out.println("lower, strict is constant???: " + boundObj_xxy_L_S.isConsistent());
		out.println("lower, non-strict is constant???: " + boundObj_xxy_L_NS.isConsistent());
		
		//test on isReal and isIntegral methods
		out.println("is real?:  " + boundObj.isReal());
		assertEquals(false, boundObj.isIntegral());
		assertEquals(true, boundObj.isReal());
		out.println("is real?:  " + boundObj_xxy_L_S.isReal());
		assertEquals(false, boundObj_xxy_L_S.isIntegral());
		assertEquals(true, boundObj_xxy_L_S.isReal());
		//BoundsObject boundObjIntegral = BoundsObject.
		//boundObj_Integral.
		
		//test on .equals method for a second object that is not an instance of BoundsObject
		assertEquals(false, boundObj.equals(numBound3));
		
	//tests to alter bounds for coverage of setting/restricting upper/lower
		
	    //boundObj_Integral.enlargeTo(boundObj_xxy_U_S);
		//out.println("EXPECTED UPPER: " + boundObj_xxy_U_S.upper);
		//out.println("NEW UPPER: " + boundObj_Integral.upper);
		boundObj_Integral.restrictUpper(numBoundPos10000, true);
			//unsure why the below test fails is asserting Equals
			//good place for javadoc update!!!
				//assertNotEquals(boundObj_Integral.upper(), boundObj_xxy_U_S.upper());
		assertEquals(boundObj_Integral.upper(), boundObj_xxy_U_S.upper());
		
		//trying to get an integral boundObj
		BoundsObject boundObjInt = BoundsObject.newUpperBound(xpy, numBound10000_int, true);
		BoundsObject boundObjInt2 = boundObjInt.clone();
		assertEquals(boundObjInt, boundObjInt2);
		boundObjInt.restrictLower(numBound_neg2000_int, true);
			//numBound0_int = numFact.integer("0");
			//numBound_neg2000_int = numFact.integer("-2000");
			//numBound10000_int = numFact.integer("10000");
		//boundObjInt.restrictLower(numBound_neg2000_int, true);
		//boundObjInt.restrictUpper(numBound10000_int, true);
		assertEquals(true, boundObjInt.isReal());
		
		//testing coverage for enlargeLower() and restrictLower()
		BoundsObject boundObjEL = BoundsObject.newLowerBound(xpy, numBound0_int, true);
		BoundsObject boundObjELower = BoundsObject.newLowerBound(xpy, numBound_neg2000_int, true);
		out.println("Current boundObjEL bounds: " + boundObjEL.toString());
		boundObjEL.enlargeTo(boundObjELower);
		out.println("boundObjEL bounds after enlargement: " + boundObjEL.toString());
		boundObjEL.restrictLower(numBound0_int, true);
		out.println("boundObjEL bounds after restriction back to 0: " + boundObjEL.toString());
		boundObjEL.restrictLower(numBound10000_int, true);
		out.println("boundObjEL bounds after restriction up to 10000: " + boundObjEL.toString());
		boundObjEL.makeConstant(numBound0_int);
		out.println("boundObjEL bounds after making const @ (int)0: " + boundObjEL.toString());
		boundObjEL.makeConstant(numBound0); //redundant, see above statement
		out.println("boundObjEL bounds after making const @ (real/rational)0: " + boundObjEL.toString());
		boundObjEL.makeConstant(numBound10pt5);
		out.println("boundObjEL bounds after making const @ 10.5: " + boundObjEL.toString());
		
		//testing coverage for enlargeUpper()
		BoundsObject boundObjEU = BoundsObject.newUpperBound(xpy, numBound0_int, true);
		BoundsObject boundObjEUpper = BoundsObject.newUpperBound(xpy, numBound10000_int, true);
		out.println();
		out.println("Current boundObjEU bounds: " + boundObjEU.toString());
		boundObjEU.enlargeTo(boundObjEUpper);
		out.println("boundObjEU bounds after enlargement: " + boundObjEU.toString());
		out.println();
		
		//further toString() testing
		BoundsObject boundObjL = BoundsObject.newLowerBound(xpy, numBound0, false);
		out.println("Expecting null upper bound.....: " + boundObjL.toString());
		
		//assertEquals(true, boundObj_Integral.isIntegral());
		out.println("Current integral bounds: " + boundObj_Integral.toString());
		boundObj_Integral.restrictTo(boundObj_xxy_U_S_0);
		out.println("Attempted integral bounds (after using restrictTo(~U=0): " + boundObj_Integral.toString());
		boundObj_Integral.enlargeTo(boundObj_xxy_U_S);
		
		//boundObj_Integral.restrictTo(boundObj_xxy_U_S);
		out.println("Attempted integral bounds (after using restrictTo(): " + boundObj_Integral.toString());
		
		//test on Assertions in BoundsObject, when the constructor receives a null expr
		boolean gotError = false;
		try{
			boundObjEquals = BoundsObject.newTightBound(null, numBound5);
		}catch(AssertionError e){
			//expected error
			//e.addSuppressed(e);
			out.println("Expected error of null value: " + e.getMessage());
			gotError = true;
		}
		assertEquals(true, gotError); 
		
		//further test on Assertions in BoundsObject, when the constructor receives a null expr
		boolean gotError2 = false;
		try{
			boundObjEquals.makeConstant(null);
		}catch(RuntimeException e){
			//expected error
			//e.addSuppressed(e);
			out.println("Expected error about tight bound wrongly set to null: " + e.getMessage());
			gotError2 = true;
		}
		assertEquals(true, gotError2); 
		
		//tests on getter methods for bounds and their respective strictness
		out.println("lower: " + boundObj.lower());
		assertEquals(numBound3, boundObj.lower());
		out.println("lower: " + boundObj.strictLower());
		assertEquals(false, boundObj.strictLower());
		out.println("lower: " + boundObj.upper());
		assertEquals(numBound3, boundObj.upper());
		out.println("lower: " + boundObj.strictUpper());
		assertEquals(false, boundObj.strictUpper());
		assertEquals(true, boundObj_xxy_L_S.strictLower());
		assertEquals(true, boundObj_xxy_U_S.strictUpper());
		//trivial to force new report of coverage
		

	}
	@Ignore
	@Test
	public void broadBoundTest(){
		
	}
	
}

