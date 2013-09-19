package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
public class CommonSymbolicTypeTest {
	/*Testing CommonSymbolicType
	 * A SymbolicType could be of different kinds: 
	 * 				Real, Array, Tuple, Boolean, Char, Function,
	 *   						Union, or Integer.
	 */
		CommonSymbolicType realType, integerType, arrayType, functionType, tupleType, booleanType;
		
		ObjectFactory objectFactory;
		SymbolicTypeFactory typeFactory;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		realType = new CommonSymbolicRealType(RealKind.IDEAL);
		integerType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		objectFactory = Objects.newObjectFactory(Numbers.REAL_FACTORY);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testIntrinsicEquals() {
		//fail("Not yet implemented");
	}
/*
	@Test
	public void testCommonSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	public void testTypeEquals() {
		fail("Not yet implemented");
	}

	@Test
	public void testTypeKind() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsReal() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsNumeric() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsHerbrand() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIdeal() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringBufferLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPureType() {
		fail("Not yet implemented");
	}
*/


}
