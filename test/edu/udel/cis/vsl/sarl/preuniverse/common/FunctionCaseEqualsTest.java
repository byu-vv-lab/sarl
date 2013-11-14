/**
 * Tests the method equals(SymbolicExpression symbolicExpression1,
 * SymbolicExpression symbolicExpression2) for case NumericExpression in
 * PreUniverse.java and method compatible(functionType functionTypeI,
 * functionType functionTypeII) in PreUniverse.java
 * 
 * @author Gunjan Majmudar
 * 
 */

package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class FunctionCaseEqualsTest {

	private static PreUniverse universe;

	private static SymbolicType realType, integerType;

	private static SymbolicType functionType1, functionType2, functionType3;

	private static SymbolicTypeSequence sequence1, sequence2, sequence3;

	private static BooleanExpression value1, value2, value, trueExpr,
			falseExpr;

	private static NumericExpression a, b, c, value3, value4;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);

		// initializing symbolic types
		integerType = universe.integerType();
		realType = universe.realType();
		trueExpr = universe.trueExpression();
		falseExpr = universe.falseExpression();

		// initializing numeric expression
		a = universe.integer(1);
		b = universe.integer(2);
		c = universe.integer(3);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the method compatible(functionType functionTypeI, functionType
	 * functionTypeII) in PreUniverse.java
	 * 
	 * @author Gunjan Majmudar
	 * 
	 */
	@Test
	public void functionTypeCompatibleTest() {
		SymbolicTupleType tupleType1 = universe.tupleType(
				universe.stringObject("SequenceofInteger"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						integerType }));
		SymbolicTupleType tupleType2 = universe.tupleType(universe
				.stringObject("Sequenceofreals"), Arrays
				.asList(new SymbolicType[] { realType, realType, realType }));
		SymbolicTupleType tupleType3 = universe.tupleType(
				universe.stringObject("SequenceofInteger"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						integerType }));
		sequence1 = tupleType1.sequence();
		sequence2 = tupleType2.sequence();
		sequence3 = tupleType3.sequence();

		functionType1 = universe.functionType(sequence1, realType);
		functionType2 = universe.functionType(sequence2, realType);
		functionType3 = universe.functionType(sequence3, realType);
		value1 = universe.compatible(functionType1, functionType2);
		value2 = universe.compatible(functionType1, functionType3);

		assertEquals(falseExpr, value1);
		assertEquals(trueExpr, value2);
	}

	/**
	 * Tests the method equals(SymbolicExpression symbolicExpression1,
	 * SymbolicExpression symbolicExpression2) for case NumericExpression in
	 * PreUniverse.java
	 * 
	 * @author Gunjan Majmudar
	 * 
	 */
	@Test
	public void equalsNumericTest() {

		value3 = universe.add(universe.add(a, b), c);
		value4 = universe.add(universe.add(a, c), b);
		value = universe.equals(value3, value4);

		assertEquals(trueExpr, value);
	}

}
