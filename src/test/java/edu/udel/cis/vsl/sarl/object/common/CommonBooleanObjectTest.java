package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for CommonBooleanObject
 * @author jtirrell
 *
 */
public class CommonBooleanObjectTest {

	/**
	 * Set a true CommonBooleanObject during setUp()
	 */
	public CommonBooleanObject trueBooleanObject;
	/**
	 * Set to a false CommonBooleanObject during setUp()
	 */
	public CommonBooleanObject falseBooleanObject;

	/**
	 * Instantiates trueBooleanObject and falseBooleanObject
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.trueBooleanObject = new CommonBooleanObject(true);
		this.falseBooleanObject = new CommonBooleanObject(false);
	}
	
	/**
	 * Verifies that two true CommonBooleanObject or two false CommonBooleanObjects return the same hash
	 */
	@Test
	public void testComputeHashCode() {
		CommonBooleanObject newTrueBooleanObject = new CommonBooleanObject(true);
		CommonBooleanObject newFalseBooleanObject = new CommonBooleanObject(false);
		
		assertTrue(this.trueBooleanObject.computeHashCode()>0);
		assertTrue(this.falseBooleanObject.computeHashCode()>0);
		assertEquals(trueBooleanObject.computeHashCode(), newTrueBooleanObject.computeHashCode());
		assertEquals(falseBooleanObject.computeHashCode(), newFalseBooleanObject.computeHashCode());
	}

	/**
	 * Verifies that CommonBooleanObjects of the same value are intrinsically equal to each other
	 */
	@Test
	public void testIntrinsicEquals() {
		CommonBooleanObject newTrueBooleanObject = new CommonBooleanObject(true);
		CommonBooleanObject newFalseBooleanObject = new CommonBooleanObject(false);
		
		assertTrue(this.trueBooleanObject.intrinsicEquals(newTrueBooleanObject));
		assertTrue(this.falseBooleanObject.intrinsicEquals(newFalseBooleanObject));
		assertFalse(this.trueBooleanObject.intrinsicEquals(newFalseBooleanObject));
	}

//	CommonBooleanObject::canonizeChildren is empty
//	@Test
//	public void testCanonizeChildren() {
//		
//	}

	/**
	 * Verifies that calling toString() on a CommonBooleanObject returns the string "true" or "false"
	 */
	@Test
	public void testToString() {
		assertEquals("true", this.trueBooleanObject.toString());
		assertEquals("false", this.falseBooleanObject.toString());
	}

	/**
	 * Verifies that calling getBoolean() on CommonBooleanObjects returns their boolean values
	 */
	@Test
	public void testGetBoolean() {
		assertTrue(this.trueBooleanObject.getBoolean());
		assertFalse(this.falseBooleanObject.getBoolean());
	}

	/**
	 * Verifies that calling compareTo() on CommonBooleanObjects returns 1, -1, or 0 accordingly.
	 */
	@Test
	public void testCompareTo() {
		assertEquals(1, this.trueBooleanObject.compareTo(this.falseBooleanObject));
		assertEquals(-1, this.falseBooleanObject.compareTo(this.trueBooleanObject));
		assertEquals(0, this.trueBooleanObject.compareTo(this.trueBooleanObject));
		assertEquals(0, this.falseBooleanObject.compareTo(this.falseBooleanObject));
	}

	/**
	 * Verifies that calling toStringBuffer() on a CommonBooleanObject returns a string buffer
	 * containing "true" or "false"
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals("false", this.falseBooleanObject.toStringBuffer(false).toString());
		assertEquals("true", this.trueBooleanObject.toStringBuffer(false).toString());
	}

	/**
	 * Verifies that calling toStringBuffer() on a CommonBooleanObject returns a string buffer 
	 * containing "true" or "false"
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals("false", this.falseBooleanObject.toStringBufferLong().toString());
		assertEquals("true", this.trueBooleanObject.toStringBufferLong().toString());
	}

}
