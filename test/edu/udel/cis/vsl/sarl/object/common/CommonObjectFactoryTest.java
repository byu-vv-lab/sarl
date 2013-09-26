package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealNumber;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.number.real.RealRational;

public class CommonObjectFactoryTest {

	CommonObjectFactory fac;
	
	@Before
	public void setUp() throws Exception {
		this.fac = null;
		this.fac = new CommonObjectFactory(new RealNumberFactory());
	}
	
	@Test
	public void testCommonObjectFactory() {
		assertTrue(fac instanceof CommonObjectFactory);
	}

	@Test
	public void testNumberFactory() {
		//assertTrue(this.fac.numberFactory() instanceof NumberFactory);
	}
/*
	@Test
	public void testSetExpressionComparator() {
		this.fac.setExpressionComparator(new Comparator<SymbolicExpression>());
	}

	@Test
	public void testSetCollectionComparator() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTypeComparator() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTypeSequenceComparator() {
		fail("Not yet implemented");
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testComparator() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanonic() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testTrueObj() {
		assertEquals(true, this.fac.trueObj().getBoolean());
	}

	@Test
	public void testFalseObj() {
		assertEquals(false, this.fac.falseObj().getBoolean());
	}

	@Test
	public void testZeroIntObj() {
		assertEquals(0, this.fac.zeroIntObj().getInt());
	}

	@Test
	public void testOneIntObj() {
		assertEquals(1, this.fac.oneIntObj().getInt());
	}

	@Test
	public void testZeroIntegerObj() {
		assertEquals("0", this.fac.zeroIntegerObj().toString());
	}

	@Test
	public void testOneIntegerObj() {
		assertEquals("1", this.fac.oneIntegerObj().toString());
	}

	@Test
	public void testZeroRealObj() {
		assertEquals("0", this.fac.zeroRealObj().toString());
	}

	@Test
	public void testOneRealObj() {
		assertEquals("1", this.fac.oneRealObj().toString());
	}

	@Test
	public void testNumberObject() {
		assertEquals("1", this.fac.numberObject(new RealInteger(new BigInteger("1"))).toString());
	}

	@Test
	public void testStringObject() {
		assertEquals("string", this.fac.stringObject("string").toString());
	}

	@Test
	public void testIntObject() {
		assertEquals(1, this.fac.intObject(1).getInt());
	}

	@Test
	public void testCharObject() {
		assertEquals('A', this.fac.charObject('A').getChar());
	}

	@Test
	public void testBooleanObject() {
		assertEquals(true, this.fac.booleanObject(true).getBoolean());
		assertEquals(false, this.fac.booleanObject(false).getBoolean());
	}
/*
	@Test
	public void testObjectWithId() {
		StringObject obj = this.fac.stringObject("string0");
		this.fac.canonic(obj);
		System.out.println(this.fac.objectWithId(0).toString());
	}

	@Test
	public void testObjects() {
		System.out.println(this.fac.objects());
	}

	@Test
	public void testNumObjects() {
		fail("Not yet implemented");
	}
*/
}
