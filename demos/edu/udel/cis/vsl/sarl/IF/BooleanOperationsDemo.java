/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

/**
 * Demonstrate SARL boolean operations such as:
 * - NOT (!x) operation
 * - OR (x || y) operation
 * - AND (x && y) operation
 * - IMPLIES (x => y) operation
 * - EQUIVALENT (x <=> y) operation
 * - LESS THAN (x < y) operation
 * - LESS THAN EQUALS (x <= y) operation
 * - EQUALS (x = y) operation
 * - NOT EQUALS (x != y) operation
 * - combination of the above...
 * - 
 * @author mohammedalali
 *
 */
public class BooleanOperationsDemo {
	
	SymbolicUniverse myUniverse;
	
	SymbolicType booleanType, integerType;
	
	StringObject xObject, yObject;
	
	BooleanSymbolicConstant xBoolean, yBoolean;
	
	NumericSymbolicConstant xNumeric, yNumeric;
	
	BooleanExpression notExpression, orExpression, andExpression, 
	impliesExpression, equivExpression, lessThanExpression, lessThanEqualsExpression, 
	equalsExpression, neqExpression, not_lessThanExpression, not_lessThanEqualsExpression, 
	not_andExpression;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Demonstrating Boolean Operations");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		myUniverse = Universes.newIdealUniverse();
		xObject = myUniverse.stringObject("X");
		yObject = myUniverse.stringObject("Y");
		booleanType = myUniverse.booleanType();
		integerType = myUniverse.integerType();
		xBoolean = (BooleanSymbolicConstant)myUniverse.symbolicConstant(xObject, booleanType);
		yBoolean = (BooleanSymbolicConstant)myUniverse.symbolicConstant(yObject, booleanType);
		xNumeric = (NumericSymbolicConstant)myUniverse.symbolicConstant(xObject, integerType);
		yNumeric = (NumericSymbolicConstant)myUniverse.symbolicConstant(yObject, integerType);
		notExpression = myUniverse.not(xBoolean); // (!x)
		orExpression = myUniverse.or(xBoolean, yBoolean); //(x || y)
		andExpression = myUniverse.and(xBoolean, yBoolean); //(x && y)
		impliesExpression = myUniverse.implies(xBoolean, yBoolean); //(x => y)
		equivExpression = myUniverse.equiv(xBoolean, yBoolean); //(x <=> y)
		lessThanExpression = myUniverse.lessThan(xNumeric, yNumeric); //(x < y)
		lessThanEqualsExpression = myUniverse.lessThanEquals(xNumeric, yNumeric);//(x <= y)
		equalsExpression = myUniverse.equals(xBoolean, yBoolean); //(x = y)
		neqExpression = myUniverse.neq(xBoolean, yBoolean); // (x != y)
		not_lessThanExpression = myUniverse.not(lessThanExpression); // !(x < y)
		not_lessThanEqualsExpression = myUniverse.not(lessThanEqualsExpression); // !(x <= y)
		not_andExpression = myUniverse.not(andExpression); // !(x && y)

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * tests the NOT (!x) operation
	 */
	@Test
	public void testNot() {
		System.out.println("NOT operation\t(!X)");
		System.out.println(notExpression);
		
		assertTrue(notExpression.type().isBoolean());
		assertEquals(notExpression.toString(), "!X");
	}

	/**
	 * testing the OR (x || y) operation
	 */
	@Test
	public void testOr() {
		System.out.println("OR operation\t(X || Y)");
		System.out.println(orExpression);
		
		assertTrue(orExpression.type().isBoolean());
	}
	
	/**
	 * testing the AND (x && y) operation
	 */
	@Test
	public void testAnd(){		
		System.out.println("AND operation\t(X && Y)");
		System.out.println(andExpression);
	
		assertTrue(andExpression.type().isBoolean());
	}
	
	/**
	 * testing the IMPLIES (x => y) operation
	 */
	@Test
	public void testImplies(){		
		System.out.println("IMPLIES operation\t(X => Y)");
		System.out.println(impliesExpression);
		
		assertTrue(impliesExpression.type().isBoolean());
	}
	
	/**
	 * testing the EQUIVALENT (x <=> y) operation
	 */
	@Test
	public void testEquivalent(){		
		System.out.println("EQUIVALENT operation\t(X <=> Y)");
		System.out.println(equivExpression);
		
		assertTrue(equivExpression.type().isBoolean());
	}
	
	/**
	 * testing the LESS THAN (x < y) operation
	 */
	@Test
	public void testLessThan(){
		System.out.println("LESS THAN operation\t(X < Y)");
		System.out.println(lessThanExpression);
		
		assertTrue(lessThanExpression.type().isBoolean());
	}
	
	/**
	 * testing the LESS THAN EQUALS (x <= y) operation
	 */
	@Test
	public void testLessThanEquals(){		
		System.out.println("LESS THAN EQUALS operation\t(X <= Y)");
		System.out.println(lessThanEqualsExpression);
		
		assertTrue(lessThanEqualsExpression.type().isBoolean());
	}
	
	/**
	 * testing the EQUALS (x = y) operation
	 */
	@Test
	public void testEquals(){ //Is it the same as equiv()?		
		System.out.println("EQUALS operation\t(X = Y)");
		System.out.println(equalsExpression);
		
		assertTrue(equalsExpression.type().isBoolean());
	}
	
	/**
	 * testing the NOT EQUALS (x != y) operation
	 */
	@Test
	public void testNeq(){		
		System.out.println("NOT EQUALS operation\t(x != y)");
		System.out.println(neqExpression);
		
		assertTrue(neqExpression.type().isBoolean());
	}
	
	/**
	 * testing FORALL
	 */
	@Test
	public void testForall(){
		
	}
	
	/**
	 * testing combination of boolean operations
	 */
	@Test
	public void testCombination(){
		System.out.println("NOT operation\t !(x < y)");
		System.out.println(not_lessThanExpression);
		
		System.out.println("NOT operation\t!(x <= y)");
		System.out.println(not_lessThanEqualsExpression);
		
		System.out.println("NOT operation\t!(x && y)");
		System.out.println(not_andExpression);
		
		assertTrue(not_lessThanExpression.type().isBoolean());
		assertTrue(not_lessThanEqualsExpression.type().isBoolean());
		assertTrue(not_andExpression.type().isBoolean());
	}
}
