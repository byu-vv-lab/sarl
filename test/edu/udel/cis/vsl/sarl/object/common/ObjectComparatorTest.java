package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;

/**
 * Test class for ObjectComparator
 * @author jtirrell
 *
 */
public class ObjectComparatorTest {

	/**
	 * Used for testing; instantiated during setUp
	 */
	ObjectComparator com;
	
	/**
	 * Instantiates this.com
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.com = new ObjectComparator();
	}

	/**
	 * Tests expressionComparator() and setExpressionComparator()
	 */
	@Test
	public void testExpressionComparator() {
		this.com.setExpressionComparator(new ExpressionComparatorStub());
		assertTrue(this.com.expressionComparator() instanceof ExpressionComparatorStub);
	}

	/**
	 * Tests collectionComparator() and setCollectionComparator()
	 */
	@Test
	public void testCollectionComparator() {
		this.com.setCollectionComparator(new CollectionComparatorStub());
		assertTrue(this.com.collectionComparator() instanceof CollectionComparatorStub);
	}

	/**
	 * Tests typeComparator() and setTypeComparator()
	 */
	@Test
	public void testTypeComparator() {
		this.com.setTypeComparator(new TypeComparatorStub());
		assertTrue(this.com.typeComparator() instanceof TypeComparatorStub);
	}

	/**
	 * Tests typeSequenceComparator() and setTypeSequenceComparator()
	 */
	@Test
	public void testTypeSequenceComparator() {
		this.com.setTypeSequenceComparator(new TypeSequenceComparatorStub());
		assertTrue(this.com.typeSequenceComparator() instanceof TypeSequenceComparatorStub);
	}

	/**
	 * Tests ObjectComparator.compare
	 */
	@Test
	public void testCompare() {
		this.com.setTypeSequenceComparator(new TypeSequenceComparatorStub());
		this.com.setTypeComparator(new TypeComparatorStub());
		this.com.setCollectionComparator(new CollectionComparatorStub());
		this.com.setExpressionComparator(new ExpressionComparatorStub());
		CommonObjectFactory obFac = new CommonObjectFactory(new RealNumberFactory());
		BooleanObject bool1 = obFac.booleanObject(true);
		BooleanObject bool2 = obFac.booleanObject(true);
		BooleanObject bool3 = obFac.booleanObject(false);
		IntObject int1 = obFac.intObject(1);
		IntObject int0 = obFac.intObject(0);
		NumberObject num0 = obFac.numberObject(new RealInteger(BigInteger.ONE));
		NumberObject num1 = obFac.numberObject(new RealInteger(BigInteger.ZERO));
		StringObject string0 = obFac.stringObject("string0");
		StringObject string1 = obFac.stringObject("string1");
		assertEquals(0, this.com.compare(bool1, bool2));
		assertEquals(1, this.com.compare(bool2, bool3));
		assertEquals(-1, this.com.compare(bool3, bool2));
		assertEquals(1, this.com.compare(int1, int0));
		//assertEquals(1, this.com.compare(num1, num0));
		assertEquals(1, this.com.compare(string1, string0));
	}

}
