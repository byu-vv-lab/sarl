package edu.udel.cis.vsl.sarl.preuniverse;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class BooleanTest {

	private static PreUniverse universe;
	private static SymbolicType booleanType;
	private static BooleanExpressionFactory booleanFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		booleanFactory = system.booleanFactory();
		
	}
	
	/**
	 * Test for universe.bool()
	 * 
	 * @author jdimarco
	 */
	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/17/13
	public void testBoolBooleanObject() {
		BooleanObject booleanObject1;
		BooleanExpression booleanExpression1;

		booleanObject1 = universe.booleanObject(true);
		booleanExpression1 = booleanFactory.symbolic(booleanObject1);
		assertEquals(universe.bool(booleanObject1), booleanExpression1); // trivial
																			// check
																			// of
																			// return
																			// type
	}
	
	/**
	 * Test for universe.add() of Boolean List
	 * 
	 * @author jdimarco
	 */
	@Test
	// Test written Jeff DiMarco (jdimarco) 9/24/13
	public void testAndIterableOfQextendsBooleanExpression() {
		LinkedList<BooleanExpression> booleanList1;
		LinkedList<BooleanExpression> booleanList2;
		LinkedList<BooleanExpression> booleanList3;
		LinkedList<BooleanExpression> booleanEmptyList;
		BooleanExpression trueExpr;
		BooleanExpression falseExpr;

		booleanList1 = new LinkedList<BooleanExpression>();
		booleanList2 = new LinkedList<BooleanExpression>();
		booleanList3 = new LinkedList<BooleanExpression>();
		booleanEmptyList = new LinkedList<BooleanExpression>();
		trueExpr = universe.bool(true);
		falseExpr = universe.bool(false);

		booleanList1.add(trueExpr);
		booleanList1.add(trueExpr);
		booleanList1.add(trueExpr);

		booleanList2.add(trueExpr);
		booleanList2.add(trueExpr);
		booleanList2.add(falseExpr);

		booleanList3.add(falseExpr);
		booleanList3.add(falseExpr);
		booleanList3.add(falseExpr);

		assertEquals(universe.and(booleanList1), trueExpr); // test all true
		assertEquals(universe.and(booleanList2), falseExpr); // test partial
																// false
		assertEquals(universe.and(booleanList3), falseExpr); // test all false
		assertEquals(universe.and(booleanEmptyList), trueExpr); // test empty is
																// true
	}
	
	/**
	 * Test for universe.or() for Boolean List Tests cases for all false, all
	 * true, and partially true/false
	 * 
	 * @author jdimarco
	 */
	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/20/13
	public void testOrIterableOfQextendsBooleanExpression() {
		LinkedList<BooleanExpression> booleanList1;
		LinkedList<BooleanExpression> booleanList2;
		LinkedList<BooleanExpression> booleanList3;
		BooleanExpression trueExpr;
		BooleanExpression falseExpr;

		trueExpr = universe.bool(true);
		falseExpr = universe.bool(false);

		booleanList1 = new LinkedList<BooleanExpression>();
		booleanList2 = new LinkedList<BooleanExpression>();
		booleanList3 = new LinkedList<BooleanExpression>();

		booleanList1.add(universe.bool(false));
		booleanList1.add(universe.bool(false));
		booleanList1.add(universe.bool(true));
		booleanList1.add(universe.bool(false));

		booleanList2.add(universe.bool(false));
		booleanList2.add(universe.bool(false));
		booleanList2.add(universe.bool(false));
		booleanList2.add(universe.bool(false));

		booleanList3.add(universe.bool(true));
		booleanList3.add(universe.bool(true));
		booleanList3.add(universe.bool(true));
		booleanList3.add(universe.bool(true));

		assertEquals(universe.or(booleanList1), trueExpr);
		assertEquals(universe.or(booleanList2), falseExpr);
		assertEquals(universe.or(booleanList3), trueExpr);

	}
	
	/**
	 * Test for universe.extractBoolean()
	 * 
	 * @author jdimarco
	 */
	@Test
	// Test written by Jeff DiMarco(jdimarco) 9/20/13
	public void testExtractBoolean() {
		BooleanExpression trueExpression;
		BooleanExpression falseExpression;
		BooleanExpression nullExpression;

		trueExpression = universe.bool(true);
		falseExpression = universe.bool(false);
		nullExpression = null;

		assertEquals(universe.extractBoolean(trueExpression), true);
		assertEquals(universe.extractBoolean(falseExpression), false);
		assertEquals(universe.extractBoolean(nullExpression), null);

	}
}