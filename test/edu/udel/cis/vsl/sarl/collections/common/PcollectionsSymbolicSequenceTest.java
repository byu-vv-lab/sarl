package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;

public class PcollectionsSymbolicSequenceTest {

	
	private static Collection<SymbolicExpression> listCollection;//list collection
	private static Collection<SymbolicExpression> listCollection2;
	private static Collection<SymbolicExpression> listCollection3;
	
	private static SymbolicExpression[] expressionList;//array collection
	private static SymbolicExpression[] expressionList2;
	private static SymbolicExpression[] expressionList3;
	
	private static Iterable<SymbolicExpression> iterableList;//iterable collection
	private static Iterable<SymbolicExpression> iterableList2;
	private static Iterable<SymbolicExpression> iterableList3;
	
	//symbolic expression setup
	private static SymbolicExpression empty = new ExpressionStub("");
	private static SymbolicExpression five = new ExpressionStub("5");
	private static SymbolicExpression three = new ExpressionStub("3");
	private static SymbolicExpression otherfive = new ExpressionStub("5");
	//private static SymbolicExpression nine = new ExpressionStub("9");
	private static SymbolicExpression ten = new ExpressionStub("10");
	//private static SymbolicExpression itsNull = new ExpressionStub("null");
	
	
	private PcollectionsSymbolicSequence<SymbolicExpression> listSequence;//Sequence that will take in a Collection param
	private PcollectionsSymbolicSequence<SymbolicExpression> listSequence2;
	private PcollectionsSymbolicSequence<SymbolicExpression> listSequence3;
	
	private PcollectionsSymbolicSequence<SymbolicExpression> iterableSequence;//Sequence that will take in an Iterable param
	private PcollectionsSymbolicSequence<SymbolicExpression> iterableSequence2;
	private PcollectionsSymbolicSequence<SymbolicExpression> iterableSequence3;
	
	private PcollectionsSymbolicSequence<SymbolicExpression> arraySequence;//Sequence that will take in an array param
	private PcollectionsSymbolicSequence<SymbolicExpression> arraySequence2;
	private PcollectionsSymbolicSequence<SymbolicExpression> arraySequence3;
	
	private PcollectionsSymbolicSequence<SymbolicExpression> elementSequence;//Sequence that takes in a single element param
	private PcollectionsSymbolicSequence<SymbolicExpression> elementSequence2;
	
	private PcollectionsSymbolicSequence<SymbolicExpression> plainSequence;//Sequence that takes in no param
	private PcollectionsSymbolicSequence<SymbolicExpression> plainSequence2;
	private PcollectionsSymbolicSequence<SymbolicExpression> plainSequence3;
	
	Transform<SymbolicExpression,SymbolicExpression> transform= new Transform<SymbolicExpression,SymbolicExpression>()
    {
		public ExpressionStub apply(SymbolicExpression x){
			String temp = x.toString() + "00";
			return new ExpressionStub(temp);
		}
	};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		listCollection = new LinkedList<SymbolicExpression>();
		listCollection.add(five);
		listCollection.add(three);
		listCollection.add(otherfive);
		listSequence = new PcollectionsSymbolicSequence<SymbolicExpression>(listCollection);
		listCollection2 = new LinkedList<SymbolicExpression>();
		listCollection2.add(five);
		listCollection2.add(three);
		listCollection2.add(otherfive);
		listSequence2 = new PcollectionsSymbolicSequence<SymbolicExpression>(listCollection2);
		listCollection3 = new LinkedList<SymbolicExpression>();
		listCollection3.add(five);
		listSequence3 = new PcollectionsSymbolicSequence<SymbolicExpression>(listCollection3);
		
		iterableList = new LinkedList<SymbolicExpression>();
		((LinkedList<SymbolicExpression>) iterableList).add(five);
		((LinkedList<SymbolicExpression>) iterableList).add(three);
		((LinkedList<SymbolicExpression>) iterableList).add(otherfive);
		iterableSequence = new PcollectionsSymbolicSequence<SymbolicExpression>(iterableList);
		iterableList2 = new LinkedList<SymbolicExpression>();
		((LinkedList<SymbolicExpression>) iterableList2).add(five);
		((LinkedList<SymbolicExpression>) iterableList2).add(three);
		((LinkedList<SymbolicExpression>) iterableList2).add(otherfive);
		iterableSequence2 = new PcollectionsSymbolicSequence<SymbolicExpression>(iterableList2);
		iterableList3 = new LinkedList<SymbolicExpression>();
		((LinkedList<SymbolicExpression>) iterableList3).add(otherfive);
		iterableSequence3 = new PcollectionsSymbolicSequence<SymbolicExpression>(iterableList3);
		
		expressionList = new SymbolicExpression[3];
		expressionList[0] = five;
		expressionList[1] = three;
		expressionList[2] = otherfive;
		arraySequence = new PcollectionsSymbolicSequence<SymbolicExpression>(expressionList);
		expressionList2 = new SymbolicExpression[3];
		expressionList2[0] = five;
		expressionList2[1] = three;
		expressionList2[2] = otherfive;
		arraySequence2 = new PcollectionsSymbolicSequence<SymbolicExpression>(expressionList2);
		expressionList3 = new SymbolicExpression[1];
		expressionList3[0] = otherfive;
		arraySequence3 = new PcollectionsSymbolicSequence<SymbolicExpression>(expressionList3);
		
		
		elementSequence = new PcollectionsSymbolicSequence<SymbolicExpression>(five);
		elementSequence2 = new PcollectionsSymbolicSequence<SymbolicExpression>(empty);
		
		plainSequence = new PcollectionsSymbolicSequence<SymbolicExpression>();
		plainSequence.add(five);
		plainSequence.add(three);
		plainSequence.add(otherfive);
		plainSequence2 = new PcollectionsSymbolicSequence<SymbolicExpression>();
		plainSequence2.add(five);
		plainSequence2.add(three);
		plainSequence2.add(otherfive);
		plainSequence3 = new PcollectionsSymbolicSequence<SymbolicExpression>();
		plainSequence3.add(empty);

	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		int testHashList = listSequence.hashCode();
		assertEquals(listSequence.hashCode(),testHashList);
		
		int testHashIterable = iterableSequence.hashCode();
		assertEquals(iterableSequence.hashCode(),testHashIterable);
		
		int testHashArray = arraySequence.hashCode();
		assertEquals(arraySequence.hashCode(),testHashArray);
		
		int testHashElement = elementSequence.hashCode();
		assertEquals(elementSequence.hashCode(),testHashElement);
		
		int testHashPlain = plainSequence.hashCode();
		assertEquals(plainSequence.hashCode(),testHashPlain);
	}
/*
	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testCollectionEquals() {
		//tests if collection equals itself
		assertTrue(listSequence.equals(listSequence));
		assertTrue(iterableSequence.equals(iterableSequence));
		assertTrue(arraySequence.equals(arraySequence));
		assertTrue(elementSequence.equals(elementSequence));
		assertTrue(plainSequence.equals(plainSequence));
		
		//test if 2 collections of the same type are equal
		assertTrue(listSequence.equals(listSequence2));
		assertTrue(iterableSequence.equals(iterableSequence2));
		assertTrue(arraySequence.equals(arraySequence2));
		assertTrue(plainSequence.equals(plainSequence2));
		
		//tests if 2 collections of the same type of different sizes
		assertFalse(listSequence.equals(listSequence3));
		assertFalse(iterableSequence.equals(iterableSequence3));
		assertFalse(arraySequence.equals(arraySequence3));
		assertFalse(elementSequence.equals(elementSequence2));
		
		//tests if different 2 different types of sequences are equal
		assertTrue(listSequence.equals(iterableSequence));
		assertTrue(iterableSequence.equals(arraySequence));
		assertTrue(arraySequence.equals(listSequence));
		assertFalse(elementSequence.equals(arraySequence));
		assertFalse(plainSequence.equals(iterableSequence));
	}

	@Test
	public void testIterator() {
		java.util.Iterator<SymbolicExpression> listIterator = listSequence.iterator();
		String listString = "[";
		while(listIterator.hasNext())
		{
			listString = listString + listIterator.next().toString() + ",";			
		}
		listString = listString + "]";
		assertEquals(listString, "[5,3,5,]");
		
		java.util.Iterator<SymbolicExpression> iterableIterator = iterableSequence.iterator();
		String iterableString = "[";
		while(iterableIterator.hasNext())
		{
			iterableString = iterableString + iterableIterator.next().toString() + ",";			
		}
		iterableString = iterableString + "]";
		assertEquals(iterableString, "[5,3,5,]");
		
		java.util.Iterator<SymbolicExpression> arrayIterator = arraySequence.iterator();
		String arrayString = "[";
		while(arrayIterator.hasNext())
		{
			arrayString = arrayString + arrayIterator.next().toString() + ",";			
		}
		arrayString = arrayString + "]";
		assertEquals(arrayString, "[5,3,5,]");
		
		java.util.Iterator<SymbolicExpression> elementIterator = elementSequence.iterator();
		String elementString = "[";
		while(elementIterator.hasNext())
		{
			elementString = elementString + elementIterator.next().toString() + ",";			
		}
		elementString = elementString + "]";
		assertEquals(elementString, "[5,]");
		
		java.util.Iterator<SymbolicExpression> plainIterator = plainSequence.iterator();
		String plainString = "[";
		while(plainIterator.hasNext())
		{
			plainString = plainString + plainIterator.next().toString() + ",";			
		}
		plainString = plainString + "]";
		assertEquals(plainString, "[]");
	}

	@Test
	public void testSize() {
		assertEquals(listSequence.size(),3);
		assertEquals(iterableSequence.size(),3);
		assertEquals(arraySequence.size(),3);
		assertEquals(elementSequence.size(),1);
		assertEquals(plainSequence.size(),0);
	}

	@Test
	public void testGet() {
		/*listCollection
		iterableSequence
		arraySequence
		elementSequence
		plainSequence*/
		//int index = 0;
		//Iterable<SymbolicExpression> iterableExpression = (Iterable<SymbolicExpression>) iterableSequence.get(index);
		//System.out.println(iterableExpression.toString());
		//System.out.println(
	}

	@Test
	public void testAdd() {
		assertEquals(listSequence.size(),3);
		assertEquals(listSequence.add(ten).size(),4);
		
		assertEquals(iterableSequence.size(),3);
		assertEquals(iterableSequence.add(ten).size(),4);
		
		assertEquals(arraySequence.size(),3);
		assertEquals(arraySequence.add(ten).size(),4);
		
		assertEquals(elementSequence.size(),1);
		assertEquals(elementSequence.add(ten).size(),2);
		
		assertEquals(plainSequence.size(),0);
		assertEquals(plainSequence.add(ten).size(),1);
	}

	@Test
	public void testSet() {
		assertEquals(listSequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(listSequence.set(1, ten).toStringBuffer(true).toString(),"<5,10,5>");
		
		assertEquals(iterableSequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(iterableSequence.set(1, ten).toStringBuffer(true).toString(),"<5,10,5>");
		
		assertEquals(arraySequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(arraySequence.set(1, ten).toStringBuffer(true).toString(),"<5,10,5>");
		
		assertEquals(elementSequence.toStringBuffer(true).toString(),"<5>");
		assertEquals(elementSequence.set(0, ten).toStringBuffer(true).toString(),"<10>");
		
		assertEquals(plainSequence.add(five).toStringBuffer(true).toString(),"<5>");
		assertEquals(plainSequence.add(five).set(0, ten).toStringBuffer(true).toString(),"<10>");
	}

	@Test
	public void testRemove() {
		assertEquals(listSequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(listSequence.remove(1).toStringBuffer(true).toString(),"<5,5>");
		
		assertEquals(iterableSequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(iterableSequence.remove(1).toStringBuffer(true).toString(),"<5,5>");
		
		assertEquals(arraySequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(arraySequence.remove(1).toStringBuffer(true).toString(),"<5,5>");
		
		assertEquals(elementSequence.toStringBuffer(true).toString(),"<5>");
		assertEquals(elementSequence.remove(0).toStringBuffer(true).toString(),"<>");
		
		assertEquals(plainSequence.add(five).remove(0).toStringBuffer(true).toString(),"<>");
	}

	@Test
	public void testSubSequence() {
		assertEquals(listSequence.subSequence(0, 0).toStringBuffer(true).toString(),"<>");
		assertEquals(listSequence.subSequence(1, 3).toStringBuffer(true).toString(),"<3,5>");
		
		assertEquals(iterableSequence.subSequence(0, 0).toStringBuffer(true).toString(),"<>");
		assertEquals(iterableSequence.subSequence(1, 3).toStringBuffer(true).toString(),"<3,5>");
		
		assertEquals(arraySequence.subSequence(0, 0).toStringBuffer(true).toString(),"<>");
		assertEquals(arraySequence.subSequence(1, 3).toStringBuffer(true).toString(),"<3,5>");
		
		assertEquals(elementSequence.subSequence(0, 0).toStringBuffer(true).toString(),"<>");
		assertEquals(elementSequence.subSequence(0, 1).toStringBuffer(true).toString(),"<5>");
		
		assertEquals(elementSequence.subSequence(0, 0).toStringBuffer(true).toString(),"<>");
		assertEquals(elementSequence.add(five).subSequence(0, 1).toStringBuffer(true).toString(),"<5>");
	}

	@Test
	public void testSetExtend() {
		assertEquals(listSequence.setExtend(10, ten, empty).toStringBuffer(true).toString(),"<5,3,5,,,,,,,,10>");
		assertEquals(iterableSequence.setExtend(10, ten, empty).toStringBuffer(true).toString(),"<5,3,5,,,,,,,,10>");
		assertEquals(arraySequence.setExtend(10, ten, empty).toStringBuffer(true).toString(),"<5,3,5,,,,,,,,10>");
		assertEquals(elementSequence.setExtend(10, ten, empty).toStringBuffer(true).toString(),"<5,,,,,,,,,,10>");
		assertEquals(plainSequence.setExtend(10, ten, empty).toStringBuffer(true).toString(),"<,,,,,,,,,,10>");
		
		assertEquals(listSequence.setExtend(0, ten, empty).toStringBuffer(true).toString(),"<10,3,5>");
		assertEquals(iterableSequence.setExtend(0, ten, empty).toStringBuffer(true).toString(),"<10,3,5>");
		assertEquals(arraySequence.setExtend(0, ten, empty).toStringBuffer(true).toString(),"<10,3,5>");
		assertEquals(elementSequence.setExtend(0, ten, empty).toStringBuffer(true).toString(),"<10>");
		assertEquals(plainSequence.add(five).add(three).setExtend(0, ten, empty).toStringBuffer(true).toString(),"<10,3>");
	}

	@Test
	public void testApply() {
		assertEquals(listSequence.apply(transform).toString(),"<500,300,500>");
	}
	

	@Test
	public void testToStringBuffer() {
		assertEquals(listSequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(listSequence.toStringBuffer(false).toString(),"<5,3,5>");

		assertEquals(iterableSequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(iterableSequence.toStringBuffer(false).toString(),"<5,3,5>");
		
		assertEquals(arraySequence.toStringBuffer(true).toString(),"<5,3,5>");
		assertEquals(arraySequence.toStringBuffer(false).toString(),"<5,3,5>");
		
		assertEquals(elementSequence.toStringBuffer(true).toString(),"<5>");
		assertEquals(elementSequence.toStringBuffer(false).toString(),"<5>");
		
		assertEquals(plainSequence.toStringBuffer(true).toString(),"<>");
		assertEquals(plainSequence.toStringBuffer(false).toString(),"<>");
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals(listSequence.toStringBufferLong().toString(),"Sequence<5,3,5>");
		assertEquals(listSequence.toStringBufferLong().toString(),"Sequence<5,3,5>");
		
		assertEquals(iterableSequence.toStringBufferLong().toString(),"Sequence<5,3,5>");
		assertEquals(iterableSequence.toStringBufferLong().toString(),"Sequence<5,3,5>");
		
		assertEquals(arraySequence.toStringBufferLong().toString(),"Sequence<5,3,5>");
		assertEquals(arraySequence.toStringBufferLong().toString(),"Sequence<5,3,5>");
		
		assertEquals(elementSequence.toStringBufferLong().toString(),"Sequence<5>");
		assertEquals(elementSequence.toStringBufferLong().toString(),"Sequence<5>");
		
		assertEquals(plainSequence.toStringBufferLong().toString(),"Sequence<>");
		assertEquals(plainSequence.toStringBufferLong().toString(),"Sequence<>");

		
	}

}
