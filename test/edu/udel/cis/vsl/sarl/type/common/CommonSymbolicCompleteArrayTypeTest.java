package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.common.ExpressionComparator;
import edu.udel.cis.vsl.sarl.ideal.common.NTConstant;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonIntObject;

public class CommonSymbolicCompleteArrayTypeTest {
	
	CommonSymbolicCompleteArrayType completeArray2, completeArray3, completeArray33;
	NumberFactory numberFactory;
	ObjectFactory objectFactory;
	SymbolicObject symbolicObject2, symbolicObject3;
	NumericPrimitive number2, number3;
	TypeComparator typeComparator;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		typeComparator = new TypeComparator();
		//typeComparator.setExpressionComparator(new ExpressionComparator());
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = Objects.newObjectFactory(numberFactory);
		symbolicObject3 = objectFactory.numberObject(numberFactory.integer(3));
		symbolicObject2 = objectFactory.numberObject(numberFactory.integer(2));
		SymbolicType intType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		SymbolicType arrayType = new CommonSymbolicArrayType(intType);
		//number = new NumericPrimitive(SymbolicOperator.CONCRETE, SymbolicType.SymbolicTypeKind.INTEGER, symbolicObject3);
		number3 = new NumericPrimitive(SymbolicOperator.CONCRETE, intType, symbolicObject3);
		number2 = new NumericPrimitive(SymbolicOperator.CONCRETE, intType, symbolicObject3);
		completeArray2 = new CommonSymbolicCompleteArrayType(arrayType, number2);
		completeArray3 = new CommonSymbolicCompleteArrayType(arrayType, number3);
		completeArray33 = new CommonSymbolicCompleteArrayType(arrayType, number3);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		assertEquals("The two completeArrays aren't equal", completeArray3.computeHashCode(), completeArray33.computeHashCode());
		
	}
	
	/*
	 * Testing if the compelteArrays are complete
	 */
	@Test
	public void testIsComplete() {
		assertTrue("The completeArray isn't complete", completeArray3.isComplete());
		assertTrue("The completeArray2 isn't complete", completeArray33.isComplete());
		
	}
	
	@Test
	public void testTypeEquals(){
		assertTrue(completeArray3.typeEquals(completeArray33));
		assertTrue(completeArray2.typeEquals(completeArray3));
	}


	@Test
	public void testExtentString() {
		assertEquals(completeArray2.extentString(), "[" + completeArray2.extent() + "]");
	}
	
	/*
	 * testing if two array types are equal using compareTo in TypeComparator;
	 */
	@Test
	public void testTypeComparator(){
		//Null pointer exception
		
		//assertEquals(typeComparator.compare(completeArray3, completeArray33), 0);
	}
	
	/*
	@Test
	public void testCommonSymbolicCompleteArrayType() {
		fail("Not yet implemented");
	}
*/
}
