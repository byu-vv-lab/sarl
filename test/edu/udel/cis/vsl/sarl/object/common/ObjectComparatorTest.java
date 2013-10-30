package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.object.common.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.object.common.ExpressionStub;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.common.BasicCollection;
import edu.udel.cis.vsl.sarl.collections.common.CommonCollectionFactory;
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
		CommonSymbolicTypeFactory typeFac = new CommonSymbolicTypeFactory(obFac);
		CommonCollectionFactory colFac = new CommonCollectionFactory(obFac);
		
		BooleanObject bool1 = obFac.booleanObject(true);
		BooleanObject bool2 = obFac.booleanObject(true);
		BooleanObject bool3 = obFac.booleanObject(false);
		BooleanObject bool4 = obFac.booleanObject(false);
		
		IntObject int1 = obFac.intObject(1);
		IntObject int0 = obFac.intObject(0);
		IntObject int2 = obFac.intObject(0);
		
		NumberObject num0 = obFac.oneIntegerObj();
		NumberObject num1 = obFac.zeroIntegerObj();
		NumberObject num2 = obFac.oneIntegerObj();
		
		StringObject string0 = obFac.stringObject("string0");
		StringObject string1 = obFac.stringObject("string0");
		StringObject string2 = obFac.stringObject("string1");
		
		SymbolicExpression exp0 = new ExpressionStub("5");
		SymbolicExpression exp1 = new ExpressionStub("5");
		SymbolicExpression exp2 = new ExpressionStub("6");
		
		SymbolicType typ0 = typeFac.characterType();
		SymbolicType typ1 = typeFac.characterType();
		SymbolicType typ2 = typeFac.booleanType();
		
		SymbolicIntegerType typ3 = typeFac.integerType();
		ArrayList<SymbolicIntegerType> typsarrlist = new ArrayList<SymbolicIntegerType>();
		SymbolicTypeSequence typs0 = typeFac.sequence(typsarrlist);
		typsarrlist.add(typ3);
		SymbolicTypeSequence typs1 = typeFac.sequence(typsarrlist);
		SymbolicTypeSequence typs2 = typeFac.sequence(typsarrlist);
		
		ArrayList<SymbolicExpression> exprarr1 = new ArrayList<SymbolicExpression>();
		ArrayList<SymbolicExpression> exprarr2 = new ArrayList<SymbolicExpression>();
		exprarr1.add(exp1);
		exprarr2.add(exp2);
		SymbolicCollection<SymbolicExpression> col0 = colFac.basicCollection(exprarr1);
		SymbolicCollection<SymbolicExpression> col1 = colFac.basicCollection(exprarr2);
		SymbolicCollection<SymbolicExpression> col2 = colFac.basicCollection(exprarr2);
		
		assertNotEquals(0, this.com.compare(string0, int0));
		assertEquals(0, this.com.compare(bool1, bool2));
		assertEquals(1, this.com.compare(bool2, bool3));
		assertEquals(-1, this.com.compare(bool3, bool2));
		assertEquals(0, this.com.compare(bool3, bool4));
		assertNotEquals(0, this.com.compare(int1, int0));
		assertEquals(0, this.com.compare(int2, int0));
		//assertEquals(0, this.com.compare(num2, num0));
		//assertNotEquals(0, this.com.compare(num1, num0));
		assertEquals(0, this.com.compare(string1, string0));
		assertNotEquals(0, this.com.compare(string2, string1));
		assertEquals(0, this.com.compare(exp0, exp1));
		assertNotEquals(0, this.com.compare(exp0, exp2));
		assertEquals(0, this.com.compare(typ0, typ1));
		assertNotEquals(0,this.com.compare(typ0, typ2));
		assertEquals(0, this.com.compare(typs1, typs2));
		assertNotEquals(0, this.com.compare(typs0, typs1));
		assertNotEquals(0, this.com.compare(col0, col1));
		assertEquals(0, this.com.compare(col1, col2));
	}

}
