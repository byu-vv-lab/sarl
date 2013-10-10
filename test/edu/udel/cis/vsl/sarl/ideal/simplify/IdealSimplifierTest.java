package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;

public class IdealSimplifierTest {

	/*private static Map<SymbolicConstant, SymbolicExpression> substitutionMap = null;
	
	private static Map<Polynomial, BoundsObject> boundMap = new HashMap<Polynomial, BoundsObject>();
	
	private static Map<BooleanExpression, Boolean> booleanMap = new HashMap<BooleanExpression, Boolean>();
	
	private static Map<Polynomial, Number> constantMap = new HashMap<Polynomial, Number>();
	
	private static boolean intervalComputed = false;	
	
	private static SimplifierInfo simplifierInfo;*/

	private static BooleanExpression boolArg1, boolArg2;

	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CommonObjects.setUp();
		

	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void getFullContextTextTest(){
		
		idealSimplifier = idealSimplifierFactory.newSimplifier(xeq5);
		BooleanExpression boolXEq5 = idealSimplifier.getFullContext();
		assertEquals(xeq5,boolXEq5);
		 
		boolArg1 = preUniv.lessThanEquals(rat25, preUniv.multiply(x, x));
		IdealSimplifier simpEq1 =idealSimplifierFactory.newSimplifier(boolArg1);
		BooleanExpression boolSimpEq1 = simpEq1.getFullContext();
		assertEquals(preUniv.lessThanEquals(rat0, preUniv.add(ratNeg25, preUniv.multiply(x, x))), boolSimpEq1);
		
		
		boolArg2 = preUniv.lessThanEquals(rat2, preUniv.multiply(x,x));
		IdealSimplifier simpEq2 =idealSimplifierFactory.newSimplifier(boolArg2);
		BooleanExpression boolSimpEq2 = simpEq2.getFullContext();
		assertEquals(boolArg2, boolSimpEq2);
	
	}
	
	
	
	/*@Test
	public void getFullReducedQuadTest(){
		boolArg1 = preUniv.lessThanEquals(twenty_five, xpyInt);
		boolArg2 = preUniv.lessThan(five, yInt);
		
		//IdealSimplifier idealSimp1 = idealSimplifierFactory.newSimplifier(boolArg1);
		//IdealSimplifier idealSimp2 = idealSimplifierFactory.newSimplifier(boolArg2);
		
		
		//BooleanExpression boolExpr1 = idealSimp1.getReducedContext();
		//BooleanExpression boolExpr2 = idealSimp2.getReducedContext();
		
		assumption = preUniv.equals(boolArg1, boolArg2);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		BooleanExpression boolExpr = idealSimplifier.getFullContext();
		
		assertEquals(boolArg1,boolExpr);
		
	}*/
	
	@Test
	public void getReducedContextTest(){
		CommonObjects.setUp();
		idealSimplifier = idealSimplifierFactory.newSimplifier(trueExpr);
		BooleanExpression boolTrue = idealSimplifier.getReducedContext();
		assertEquals(trueExpr,boolTrue);
		
		boolArg2 = preUniv.lessThanEquals(rat2, preUniv.multiply(x,x));
		IdealSimplifier simpEq2 =idealSimplifierFactory.newSimplifier(boolArg2);
		BooleanExpression boolSimpEq2 = simpEq2.getReducedContext();
		assertEquals(boolArg2, boolSimpEq2);
		
		
	}
	
/*	@Test
	public void assumptionAsIntervalTest(){
		boolArg1 = preUniv.lessThanEquals(twenty_five, preUniv.multiply(x, x));
		boolArg2 = preUniv.lessThan(x, two_hund);
		assumption = preUniv.and(boolArg1, boolArg2);
		
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		Interval interval = idealSimplifier.assumptionAsInterval(xsqd);
		
		assertEquals(x,interval);
		
	}*/
}
