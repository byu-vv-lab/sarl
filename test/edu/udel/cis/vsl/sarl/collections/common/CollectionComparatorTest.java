package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.assertEquals;

//import java.util.Collection;
import java.util.Comparator;
/*import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;*/
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicIntegerType;

public class CollectionComparatorTest {

	CollectionComparator collectionComparator;
	CollectionComparator testSetComparator;
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
	private static CljSortedMap<SymbolicExpression,SymbolicExpression> test;
	private static CljSortedMap<SymbolicExpression,SymbolicExpression> test2;
	private static CljSortedMap<SymbolicExpression,SymbolicExpression> test3;
		
	private static PMap<SymbolicExpression,SymbolicExpression> pmap;
	private static PcollectionsHashMap<SymbolicExpression, SymbolicExpression> pMapCollection;
	private static PcollectionsHashMap<SymbolicExpression, SymbolicExpression> pMapCollection2;
	
	private static PcollectionsHashSet<SymbolicExpression> pSetPlain;
	private static PcollectionsHashSet<SymbolicExpression> pSetPlain2;
	private static PcollectionsHashSet<SymbolicExpression> pSetPlain3;
	
	private static SymbolicExpression x = new ExpressionStub("5");

	private static SymbolicExpression y = new ExpressionStub("y");
	
	private static SymbolicExpression a = new ExpressionStub("5");

	private static SymbolicExpression b = new ExpressionStub("9");
	
	private static SymbolicExpression z = new ExpressionStub("10");

	public static SymbolicExpression createExpression(int expression){
		SymbolicType symbolicType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		NumberFactory numFact = Numbers.REAL_FACTORY;
		IntegerNumber expr = numFact.integer(expression);
		ObjectFactory objFact = Objects.newObjectFactory(numFact);
		SymbolicObject symObj =  objFact.numberObject(expr);
		SymbolicTypeFactory typeFact = Types.newTypeFactory(objFact);
		CollectionFactory collectionFact = Collections.newCollectionFactory(objFact);
		ExpressionFactory exprFact = Expressions.newIdealExpressionFactory(numFact, objFact, typeFact, collectionFact);
		return exprFact.expression(SymbolicOperator.CONCRETE, symbolicType, symObj);
	}
	
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
		test = new CljSortedMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		test3 = new CljSortedMap<SymbolicExpression,SymbolicExpression>(elementComparator);

		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(a, b);
		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(x, y);
		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(y, a);
		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(b, z);
		
		test2 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(a, b);
		test2 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(b, b);
		test2 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(y, b);
		
		test3 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test3.put(a, b);
		test3 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test3.put(x, y);
		test3 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test3.put(y, a);
		test3 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test3.put(b, z);
		
		
		pSetPlain = new PcollectionsHashSet<SymbolicExpression>();
		pSetPlain = (PcollectionsHashSet<SymbolicExpression>) pSetPlain.add(a);
		pSetPlain = (PcollectionsHashSet<SymbolicExpression>) pSetPlain.add(b);
		pSetPlain = (PcollectionsHashSet<SymbolicExpression>) pSetPlain.add(z);
		pSetPlain2 = new PcollectionsHashSet<SymbolicExpression>();
		pSetPlain2 = (PcollectionsHashSet<SymbolicExpression>) pSetPlain2.add(a);
		pSetPlain2 = (PcollectionsHashSet<SymbolicExpression>) pSetPlain2.add(b);
		pSetPlain2 = (PcollectionsHashSet<SymbolicExpression>) pSetPlain2.add(z);
		pSetPlain3 = new PcollectionsHashSet<SymbolicExpression>();
		
		pmap = HashTreePMap.empty();
		pMapCollection = new PcollectionsHashMap<SymbolicExpression,SymbolicExpression>(pmap);
		pMapCollection = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection.put(a,b);
		pMapCollection = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection.put(x,y);
		pMapCollection = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection.put(y,a);
		pMapCollection = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection.put(b,z);
		
		pMapCollection2 = new PcollectionsHashMap<SymbolicExpression,SymbolicExpression>(pmap);
		pMapCollection2 = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection2.put(a,b);
		pMapCollection2 = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection2.put(x,y);
		pMapCollection2 = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection2.put(y,a);
		pMapCollection2 = (PcollectionsHashMap<SymbolicExpression, SymbolicExpression>) pMapCollection2.put(b,z);
		
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
		assertEquals(testSetComparator.compare(test,pMapCollection), -1);
		assertEquals(testSetComparator.compare(pMapCollection,test), 1);

		
		
	}

	@Test
	public void testCompare() {
		assertEquals(collectionComparator.compare(test, test2), -4);
		assertEquals(collectionComparator.compare(test2, test), 4);
		assertEquals(collectionComparator.compare(test, test3), 0);
		assertEquals(collectionComparator.compare(pSetPlain, pSetPlain3), 3);
		assertEquals(collectionComparator.compare(pSetPlain,test),-2);
		assertEquals(collectionComparator.compare(test,pSetPlain),2);
	}
	
	@Test(expected=SARLInternalException.class)
	public void testCompareException() {
		testSetComparator = collectionComparator = new CollectionComparator();
		testSetComparator.setElementComparator(elementComparator);
		testSetComparator.compare(new BasicCollection<SymbolicExpression>(new LinkedList<SymbolicExpression>()), new BasicCollection<SymbolicExpression>(new LinkedList<SymbolicExpression>()));
		
	}
	
	@Test(expected=SARLInternalException.class)
	public void testCompareExceptionSet() {
		testSetComparator = collectionComparator = new CollectionComparator();
		testSetComparator.setElementComparator(elementComparator);
		testSetComparator.compare(pSetPlain, pSetPlain2);
	}
	
	@Test(expected=SARLInternalException.class)
	public void testCompareExceptionMap() {
		testSetComparator = collectionComparator = new CollectionComparator();
		testSetComparator.setElementComparator(elementComparator);
		testSetComparator.compare(pMapCollection,pMapCollection2);
		
	}
	
	
	
}
