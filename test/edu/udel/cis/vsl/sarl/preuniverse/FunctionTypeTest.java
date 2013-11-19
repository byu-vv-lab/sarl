/**
 * Tests the FunctionType in class preuniverse.java
 * 
 * @author Gunjan Majmudar
 * 
 */

package edu.udel.cis.vsl.sarl.preuniverse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonPreUniverse;

public class FunctionTypeTest {

	private static PreUniverse universe;

	private static SymbolicType realType, integerType;

	private static SymbolicType functionType1, functionType2;

	private static SymbolicTypeSequence sequence1, sequence2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		integerType = universe.integerType();
		realType = universe.realType();
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
	 * Tests methods typeKind() isReal() isCanonic() isInteger() for
	 * FunctionType in class preuniverse.java
	 * 
	 * @author Gunjan Majmudar
	 * 
	 */
	@Test
	public void functionKindTest() {
		SymbolicTupleType tupleType1 = universe.tupleType(
				universe.stringObject("SequenceofInteger"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						integerType }));
		SymbolicTupleType tupleType2 = universe.tupleType(universe
				.stringObject("Sequenceofreals"), Arrays
				.asList(new SymbolicType[] { realType, realType, realType }));
		sequence1 = tupleType1.sequence();
		sequence2 = tupleType2.sequence();
		functionType1 = universe.functionType(sequence1, realType);
		functionType2 = universe.functionType(sequence2, realType);

		// functionType3 = nuniverse.functionType(inputTypes, outputType)

		assertEquals(functionType1.typeKind(), functionType2.typeKind());
		assertEquals(functionType1.isReal(), false);
		assertEquals(functionType1.isCanonic(), false);
		assertEquals(functionType1.isInteger(), false);

	}

	/**
	 * Tests method compatible(functionType1, functionType2) for FunctionType in
	 * class preuniverse.java
	 * 
	 * @author Mohammad Alsulmi
	 * 
	 */
	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithFunction() {

		// here we test compatible with tuple types
		SymbolicFunctionType functionType1, functionType2;
		BooleanExpression result, expected;

		functionType1 = universe.functionType(
				Arrays.asList(new SymbolicType[] { integerType, integerType }),
				realType);
		functionType2 = universe.functionType(
				Arrays.asList(new SymbolicType[] { integerType, realType }),
				integerType);

		// here we compare two different function types (functionType1,
		// functionType2)
		// the expected compatible call should return true

		expected = universe.bool(false);
		result = universe.compatible(functionType1, functionType2);
		assertEquals(expected, result);

	}

}
