package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
/**
 
  Testing CommonSymbolicType
  A SymbolicType could be of different kinds: 
			Real, Array, Tuple, Boolean, Char, Function,
		Union, or Integer.
	 
 * 
 * @author alali
 *
 */
public class CommonSymbolicTypeTest {
	
		/**
		 * creating different SymbolicTypes
		 */
		CommonSymbolicType realType, realType2, integerType, arrayType, 
		functionType, tupleType, booleanType;
		
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
		realType2 = new CommonSymbolicRealType(RealKind.FLOAT);
		integerType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		objectFactory = Objects.newObjectFactory(Numbers.REAL_FACTORY);
		booleanType = new CommonSymbolicPrimitiveType(SymbolicTypeKind.BOOLEAN);
		
	}

	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Tests the equality of two types by calling typeEquals()
	 * 
	 */
	@Test
	public void testIntrinsicEquals() {
		assertTrue(realType.intrinsicEquals(realType));
		assertFalse(realType.intrinsicEquals(realType2));
		assertFalse(realType.intrinsicEquals(integerType));
		assertFalse(realType.intrinsicEquals(null));
	}
	

	/**
	 * Testing the kind of a SymbolicType
	 */
	@Test
	public void testTypeKind() {
		assertEquals(realType.typeKind(), SymbolicTypeKind.REAL);
	}
	

	/**
	 * testing a type if it is INTEGER kind.
	 */
	@Test
	public void testIsInteger() {
		assertTrue(integerType.isInteger());
		assertFalse(realType.isInteger());
	}
	
	/**
	 * testing a type if it is REAL kind.
	 */
	@Test
	public void testIsReal() {
		assertTrue(realType.isReal());
		assertFalse(integerType.isReal());
	}
	
	/**
	 * testing a type if it is a BOOLEAN kind.
	 */
	@Test
	public void testIsBoolean() {
		assertTrue(booleanType.isBoolean());
		assertFalse(realType.isBoolean());
	}
	

	/**
	 * testing a type if it is numeric, i.e. INTEGER or REAL
	 */
	@Test
	public void testIsNumeric() {
		assertTrue(integerType.isNumeric());
		assertTrue(realType.isNumeric());
		assertFalse(booleanType.isNumeric());
	}
	
	/**
	 * tests if a type is HERBRAND
	 * in CommonSymbolicType, it is assumed that all types are NOT herbrand.
	 * it is overridden in the concrete classes.
	 */
	@Test
	public void testIsHerbrand() {
		assertFalse(booleanType.isHerbrand());
	}
	

	/**
	 * tests if a type is IDEAL
	 * in CommonSymbolicType, it is assumed that all types are NOT ideal
	 * so, the method must be overridden by the concrete classes that are ideal
	 */
	@Test
	public void testIsIdeal() {
		assertFalse(booleanType.isIdeal());
	}
	

	/**
	 * tests a string representation of the type
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals(realType.toStringBufferLong().toString(), "real");
	}
	
	/**
	 * tests the returned pureType of this type
	 */
	@Test
	public void testGetPureType() {
		assertTrue(realType.getPureType() instanceof CommonSymbolicRealType);
	}



/*


	
*/


}
