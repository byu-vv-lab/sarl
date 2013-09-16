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
	
	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;
	
	private static NumericExpressionFactory numExprFact;
	
	private static SymbolicType realType;

	private static SymbolicType integerType;
	
	private static PreUniverse preUniv;
	
	private static NumericExpression xpy;
	
	private static NumericExpression xy;
	
	private static BoundsObject boundObj;
	
	private static NumberFactory numFact;
	
	//private static Number numBound;
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound3;
	
	private static edu.udel.cis.vsl.sarl.IF.number.Number numBound5;
	
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
		numFact = preUniv.numberFactory();
		//numBound = preUniv.
//		numbFact = preUniv.  //numExprFact.numberFactory();
//		numbBound = preUniv.  //numbFact.rational("three");
		numBound3 = numFact.rational("3");
		numBound5 = numFact.rational("5");
		boundObj = BoundsObject.newTightBound(symbExpr_xpy, numBound3);
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
		
		//test on isReal and isIntegral methods
		out.println("is real?:  " + boundObj.isReal());
		assertEquals(false, boundObj.isIntegral());
		assertEquals(true, boundObj.isReal());
		
		//test on .equals method for a second object that is not an instance of BoundsObject
		assertFalse(boundObj.equals(numBound3));
		
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
		//trivial to force new report of coverage
		

	}
}
