package edu.udel.cis.vsl.sarl.preuniverse;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * This class tests Type functionality in the PreUniverse package.
 * The majority of these tests test methods by causing exceptions
 * to be thrown when given bad input.
 * 
 * @author jsaints
 */
public class TypeTest {

	// Universe
	private static PreUniverse universe;
	// SymbolicExpressions
	private static NumericExpression two, four, oneReal, oneInt;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Instantiate universe
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		
		// Instantiate NumberExpressions
		two = universe.integer(2);
		four = universe.integer(4);
		oneReal = universe.oneReal();
		oneInt = universe.oneInt();
	}
	
	/**
	 * Tests that you are unable to add two SymExprs of different types.
	 * Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
	 */
	@Test(expected=SARLException.class)
	public void testCheckSameType1() {
		universe.add(two, four); //should complete successfully
		universe.add(oneReal, oneInt); //should throw an exception b/c not same type
	}
	
	/**
	 * Tests that you are unable to multiply two SymExprs of different types.
	 * Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
	 */
	@Test(expected=SARLException.class)
	public void testCheckSameType2() {
		universe.multiply(two, four); //should complete successfully
		universe.multiply(oneReal, oneInt); //should throw an exception b/c not same type
	}
	
	/**
	 * Tests that you are unable to divide two SymExprs of different types.
	 * Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
	 */
	@Test(expected=SARLException.class)
	public void testCheckSameType3() {
		universe.divide(two, four); //should complete successfully
		universe.divide(oneReal, oneInt); //should throw an exception b/c not same type
	}
	
	/**
	 * Tests that you are unable to subtract two SymExprs of different types.
	 * Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
	 */
	@Test(expected=SARLException.class)
	public void testCheckSameType4() {
		universe.subtract(two, four); //should complete successfully
		universe.subtract(oneReal, oneInt); //should throw an exception b/c not same type
	}
	
	/**
	 * Tests that you are unable to modulo two SymExprs of different types.
	 * Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
	 */
	@Test(expected=SARLException.class)
	public void testCheckSameType5() {
		universe.modulo(two, four); //should complete successfully
		universe.modulo(oneReal, oneInt); //should throw an exception b/c not same type
	}
}
	