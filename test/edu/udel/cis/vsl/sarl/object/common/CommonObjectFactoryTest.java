package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.common.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonExpressionFactory;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealNumber;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.number.real.RealRational;
import edu.udel.cis.vsl.sarl.type.common.TypeComparator;
import edu.udel.cis.vsl.sarl.type.common.TypeSequenceComparator;

public class CommonObjectFactoryTest {

	CommonObjectFactory fac;
	
	@Before
	public void setUp() throws Exception {
		this.fac = null;
		this.fac = new CommonObjectFactory(new RealNumberFactory());
	}
	
	/**
	 * Method to test CommonObjectFactory.CommonObjectFactory
	 */
	@Test
	public void testCommonObjectFactory() {
		assertTrue(fac instanceof CommonObjectFactory);
	}

	/**
	 * Method to test CommonObjectFactory.NumberFactory
	 */
	@Test
	public void testNumberFactory() {
		assertTrue(this.fac.numberFactory() instanceof RealNumberFactory);
	}

	/**
	 * Method to test CommonObjectFactory.setExpressionComparator
	 */
	@Test
	public void testSetExpressionComparator() {
		this.fac.setExpressionComparator(new ExpressionComparatorStub());
		assertTrue(this.fac.comparator().expressionComparator() instanceof ExpressionComparatorStub);
	}

	/**
	 * Method to test CommonObjectFactory.setCollectionComparator
	 */
	@Test
	public void testSetCollectionComparator() {
		this.fac.setCollectionComparator(new CollectionComparatorStub());
		assertTrue(this.fac.comparator().collectionComparator() instanceof CollectionComparatorStub);
	}

	/**
	 * Method to test CommonObjectFactory.setTypeComparator
	 */
	@Test
	public void testSetTypeComparator() {
		this.fac.setTypeComparator(new TypeComparator());
		assertTrue(this.fac.comparator().typeComparator() instanceof TypeComparator);
	}

	/**
	 * Method to test CommonObjectFactory.setTypeSequenceComparator
	 */
	@Test
	public void testSetTypeSequenceComparator() {
		this.fac.setTypeSequenceComparator(new TypeSequenceComparator());
		assertTrue(this.fac.comparator().typeSequenceComparator() instanceof TypeSequenceComparator);
	}

	/**
	 * Method to test CommonObjectFactory.init
	 */
	@Test
	public void testInit() {
		this.fac.setExpressionComparator(new ExpressionComparatorStub());
		this.fac.setCollectionComparator(new CollectionComparatorStub());
		this.fac.setTypeComparator(new TypeComparator());
		this.fac.setTypeSequenceComparator(new TypeSequenceComparator());
		try {
			this.fac.init();
			assertTrue(true);
		}
		catch(Exception e)  {
			assertTrue(false);
		}
	}

	/**
	 * Method to test CommonObjectFactory.trueObj
	 */
	@Test
	public void testTrueObj() {
		assertEquals(true, this.fac.trueObj().getBoolean());
	}

	/**
	 * Method to test CommonObjectFactory.falseObj
	 */
	@Test
	public void testFalseObj() {
		assertEquals(false, this.fac.falseObj().getBoolean());
	}

	/**
	 * Method to test CommonObjectFactory.zeroIntObj
	 */
	@Test
	public void testZeroIntObj() {
		assertEquals(0, this.fac.zeroIntObj().getInt());
	}

	/**
	 * Method to test CommonObjectFactory.oneIntObj
	 */
	@Test
	public void testOneIntObj() {
		assertEquals(1, this.fac.oneIntObj().getInt());
	}

	/**
	 * Method to test CommonObjectFactory.zeroIntegerObj
	 */
	@Test
	public void testZeroIntegerObj() {
		assertEquals("0", this.fac.zeroIntegerObj().toString());
	}

	/**
	 * Method to test CommonObjectFactory.oneIntegerObj
	 */
	@Test
	public void testOneIntegerObj() {
		assertEquals("1", this.fac.oneIntegerObj().toString());
	}

	/**
	 * Method to test CommonObjectFactory.zeroRealObj
	 */
	@Test
	public void testZeroRealObj() {
		assertEquals("0", this.fac.zeroRealObj().toString());
	}

	/**
	 * Method to test CommonObjectFactory.oneRealObj
	 */
	@Test
	public void testOneRealObj() {
		assertEquals("1", this.fac.oneRealObj().toString());
	}

	/**
	 * Method to test CommonObjectFactory.numberObject
	 */
	@Test
	public void testNumberObject() {
		assertEquals("1", this.fac.numberObject(new RealInteger(new BigInteger("1"))).toString());
	}

	/**
	 * Method to test CommonObjectFactory.stringObject
	 */
	@Test
	public void testStringObject() {
		assertEquals("string", this.fac.stringObject("string").toString());
	}

	/**
	 * Method to test CommonObjectFactory.intObject
	 */
	@Test
	public void testIntObject() {
		assertEquals(1, this.fac.intObject(1).getInt());
	}

	/**
	 * Method to test CommonObjectFactory.charObject
	 */
	@Test
	public void testCharObject() {
		assertEquals('A', this.fac.charObject('A').getChar());
	}

	/**
	 * Method to test CommonObjectFactory.booleanObject
	 */
	@Test
	public void testBooleanObject() {
		assertEquals(true, this.fac.booleanObject(true).getBoolean());
		assertEquals(false, this.fac.booleanObject(false).getBoolean());
	}

	/**
	 * Method to test CommonObjectFactory.objectWithId
	 */
	@Test
	public void testObjectWithId() {
		IntObject tempint = this.fac.intObject(2);
		IntObject tempint2 = this.fac.intObject(2);
		this.fac.canonic(tempint);
		
		assertEquals("2", this.fac.objectWithId(8).toString());
	}

	/**
	 * Method to test CommonObjectFactory.objects() and canonic()
	 */
	@Test
	public void testObjects() {
		//make something canonic and check to make sure its added to the list
		int originalcount = this.fac.objects().size();
		IntObject tempint = this.fac.intObject(2);
		IntObject tempint2 = this.fac.intObject(2);
		this.fac.canonic(tempint);
		assertEquals(this.fac.objects().size(), originalcount+1);
	}

	/**
	 * Method to test CommonObjectFactory.numObjects
	 */
	@Test
	public void testNumObjects() {
		int originalcount = this.fac.numObjects();
		IntObject tempint = this.fac.intObject(2);
		this.fac.canonic(tempint);
		assertEquals(this.fac.numObjects(), originalcount+1);
	}

}
