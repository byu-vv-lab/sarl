package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;

public class CommonBooleanObjectTest {

	public CommonBooleanObject trueBooleanObject;
	public CommonBooleanObject falseBooleanObject;

	@Before
	public void setUp() throws Exception {
		this.trueBooleanObject = new CommonBooleanObject(true);
		this.falseBooleanObject = new CommonBooleanObject(false);
	}
	
	@Test
	public void testComputeHashCode() {
		CommonBooleanObject newTrueBooleanObject = new CommonBooleanObject(true);
		CommonBooleanObject newFalseBooleanObject = new CommonBooleanObject(false);
		assertTrue(this.trueBooleanObject.computeHashCode()>0);
		assertTrue(this.falseBooleanObject.computeHashCode()>0);
		assertEquals(trueBooleanObject.computeHashCode(), newTrueBooleanObject.computeHashCode());
		assertEquals(falseBooleanObject.computeHashCode(), newFalseBooleanObject.computeHashCode());
	}

	@Test
	public void testIntrinsicEquals() {
		CommonBooleanObject newTrueBooleanObject = new CommonBooleanObject(true);
		CommonBooleanObject newFalseBooleanObject = new CommonBooleanObject(false);
		assertTrue(this.trueBooleanObject.intrinsicEquals(newTrueBooleanObject));
		assertTrue(this.falseBooleanObject.intrinsicEquals(newFalseBooleanObject));
	}

//	CommonBooleanObject::canonizeChildren is empty
//	@Test
//	public void testCanonizeChildren() {
//		
//	}

	@Test
	public void testToString() {
		assertEquals("true", this.trueBooleanObject.toString());
		assertEquals("false", this.falseBooleanObject.toString());
	}

	@Test
	public void testGetBoolean() {
		assertTrue(this.trueBooleanObject.getBoolean());
		assertFalse(this.falseBooleanObject.getBoolean());
	}

	@Test
	public void testCompareTo() {
		assertEquals(1, this.trueBooleanObject.compareTo(this.falseBooleanObject));
		assertEquals(-1, this.falseBooleanObject.compareTo(this.trueBooleanObject));
		assertEquals(0, this.trueBooleanObject.compareTo(this.trueBooleanObject));
		assertEquals(0, this.falseBooleanObject.compareTo(this.falseBooleanObject));
	}

	@Test
	public void testToStringBuffer() {
		assertEquals("(false)", this.falseBooleanObject.toStringBuffer(true).toString());
		assertEquals("false", this.falseBooleanObject.toStringBuffer(false).toString());
		assertEquals("(true)", this.trueBooleanObject.toStringBuffer(true).toString());
		assertEquals("true", this.trueBooleanObject.toStringBuffer(false).toString());
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals("false", this.falseBooleanObject.toStringBufferLong().toString());
		assertEquals("true", this.trueBooleanObject.toStringBufferLong().toString());
	}

}
