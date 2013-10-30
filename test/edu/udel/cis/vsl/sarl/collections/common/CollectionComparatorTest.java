package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;

//import java.util.Collection;
import java.util.Comparator;
/*import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;*/

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
/*import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;*/

public class CollectionComparatorTest {

	CollectionComparator collectionComparator;
	CollectionComparator testSetComparator;
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test2;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test3;
	//private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test4;
	
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetPlain;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetPlain2;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetPlain3;
	
	private static SymbolicExpression x = new ExpressionStub("5");

	private static SymbolicExpression y = new ExpressionStub("y");
	
	private static SymbolicExpression a = new ExpressionStub("5");

	private static SymbolicExpression b = new ExpressionStub("9");
	
	private static SymbolicExpression z = new ExpressionStub("10");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		collectionComparator = new CollectionComparator();
		collectionComparator.setElementComparator(elementComparator);
		test = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		test3 = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		//test4 = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		
		
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(a, b);
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(x, y);
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(y, a);
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(b, z);
		
		test2 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(a, b);
		test2 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(b, b);
		test2 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(y, b);
		
		test3 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test3.put(a, b);
		test3 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test3.put(x, y);
		test3 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test3.put(y, a);
		test3 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test3.put(b, z);
		
		
		pSetPlain = new PcollectionsSymbolicSet<SymbolicExpression>();
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(a);
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(b);
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(z);
		pSetPlain2 = new PcollectionsSymbolicSet<SymbolicExpression>();
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(a);
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(b);
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(z);
		pSetPlain3 = new PcollectionsSymbolicSet<SymbolicExpression>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetElementComparator() {
		testSetComparator = collectionComparator = new CollectionComparator();
		testSetComparator.setElementComparator(elementComparator);
		assertEquals(testSetComparator.compare(test, test2), -4);
		assertEquals(testSetComparator.compare(test2, test), 4);
		assertEquals(testSetComparator.compare(test, test3), 0);
	}

	@Test
	public void testCompare() {
		assertEquals(collectionComparator.compare(test, test2), -4);
		assertEquals(collectionComparator.compare(test2, test), 4);
		assertEquals(collectionComparator.compare(test, test3), 0);
		assertEquals(collectionComparator.compare(pSetPlain, pSetPlain2), 0);
		assertEquals(collectionComparator.compare(pSetPlain, pSetPlain3), 3);
	}
	
	
}
