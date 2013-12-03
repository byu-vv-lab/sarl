package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicIntegerType;


public class PcollectionsSymbolicMapTest {
	
	CommonObjectFactory fac;
	private static CommonObjectFactory objectFactory = new CommonObjectFactory(new RealNumberFactory());
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
	
	private static SymbolicExpression x = new ExpressionStub("5");

	private static SymbolicExpression y = new ExpressionStub("4");
	
	private static SymbolicExpression a = new ExpressionStub("5");
	
	private static SymbolicExpression c = new ExpressionStub("3");

	private static SymbolicExpression b = new ExpressionStub("9");
	
	private static SymbolicExpression z = new ExpressionStub("10");
	
	private static LinkedHashMap<SymbolicExpression, SymbolicExpression> map; 
	
	private static PMap<SymbolicExpression,SymbolicExpression> pmap;
	
	
	
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> javaMap;
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> pMap;
	
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> plainPMap;
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> plainPMapSame;
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> plainPMapSmaller;
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> plainEmptyPMap;
	
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> canonicMap;
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> canonicMap2;
	
	private static SymbolicExpression twenty = createExpression(20);
	private static SymbolicExpression forty = createExpression(40);
	private static SymbolicExpression sixty = createExpression(60);
	private static SymbolicExpression eighty = createExpression(80);
	private static SymbolicExpression hundred = createExpression(100);
	
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
		
				pmap = HashTreePMap.empty();
				map = new LinkedHashMap<SymbolicExpression,SymbolicExpression>();
				javaMap = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>(map);
				pMap = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>(pmap);
				
				javaMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) javaMap.put(new ExpressionStub("A^4"), new ExpressionStub("T"));
				javaMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) javaMap.put(new ExpressionStub("P^5"), new ExpressionStub("T"));
				javaMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) javaMap.put(new ExpressionStub("3G"), new ExpressionStub("T"));
				javaMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) javaMap.put(new ExpressionStub("R^2"), new ExpressionStub("T"));
				pMap.put(y, b);
				javaMap.put(y, b);
				
				plainPMap = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>();
				plainPMapSame = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>();
				plainPMapSmaller = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>();
				plainEmptyPMap = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>();
				
				plainPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMap.put(y, c);
				plainPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMap.put(b , c);
				plainPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMap.put(x, z);
				plainPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMap.put(z, b);
				plainPMapSame = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMapSame.put(y, c);
				plainPMapSame = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMapSame.put(b , c);
				plainPMapSame = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMapSame.put(x, z);
				plainPMapSame = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMapSame.put(z, b);
				
				plainPMapSmaller = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMapSmaller.put(a, c);
				plainPMapSmaller = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainPMapSmaller.put(z , y);
				
				
				canonicMap = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>(map);
				twenty = objectFactory.canonic(twenty);
				canonicMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) canonicMap.put(twenty, eighty);
				canonicMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) canonicMap.put(forty, eighty);
				canonicMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) canonicMap.put(forty, hundred);
				canonicMap2 = new PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>(map);
				
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		int hash = plainPMap.computeHashCode();
		assertEquals(plainPMap.hashCode(), hash);
		
		int javaHash = javaMap.computeHashCode();
		assertEquals(javaMap.hashCode(), javaHash);
		
		int pMapHash = pMap.computeHashCode();
		assertEquals(pMap.hashCode(), pMapHash);
	}

	@Test
	public void testCanonizeChildren() {
		assertFalse(canonicMap.isCanonic());
		canonicMap = objectFactory.canonic(canonicMap);
		assertTrue(canonicMap.isCanonic());
		
		assertFalse(canonicMap2.isCanonic());
		canonicMap2 = objectFactory.canonic(canonicMap2);
		assertTrue(canonicMap2.isCanonic());
	}
	

	@Test
	public void testGet() {
		assertEquals(plainPMap.get(y).toString(),"3");
		assertEquals(plainEmptyPMap.get(y),null);
	}

	@Test
	public void testKeys() {
		assertEquals(plainPMap.keys().toString(),"[4, 5, 9, 10]");
		assertEquals(plainEmptyPMap.keys().toString(),"[]");
	}

	@Test
	public void testValues() {
		assertEquals(plainPMap.values().toString(),"[3, 10, 3, 9]");
		assertEquals(plainEmptyPMap.values().toString(),"[]");
	}

	@Test
	public void testEntries() {
		assertEquals(plainPMap.entries().toString(),"[4=3, 5=10, 9=3, 10=9]");
		assertEquals(plainEmptyPMap.entries().toString(),"[]");
	}

	@Test
	public void testIterator() {
		Iterator<SymbolicExpression> temp = plainPMap.iterator();
		String testString = "[";
		while(temp.hasNext())
		{
			testString = testString + temp.next().toString() + ",";	
		}
		testString = testString + "]";
		assertEquals(testString, "[3,10,3,9,]");
		
		Iterator<SymbolicExpression> emptyTemp = plainEmptyPMap.iterator();
		String testEmptyString = "[";
		while(emptyTemp.hasNext())
		{
			testEmptyString = testEmptyString + emptyTemp.next().toString() + ",";	
		}
		testEmptyString = testEmptyString + "]";
		assertEquals(testEmptyString, "[]");
	}

	@Test
	public void testSize() {
		assertEquals(plainPMap.size(),4);
		assertEquals(plainEmptyPMap.size(),0);
	}

	@Test
	public void testCollectionEqualsSymbolicCollectionOfV() {
		assertFalse(plainPMap.collectionEquals(plainEmptyPMap));
		assertFalse(plainEmptyPMap.collectionEquals(plainPMap));
		assertFalse(plainPMap.collectionEquals(plainPMapSmaller));
		assertFalse(plainPMapSmaller.collectionEquals(plainPMap));
		assertTrue(plainPMap.collectionEquals(plainPMapSame));
		assertTrue(plainPMapSame.collectionEquals(plainPMap));
	}

	@Test
	public void testToStringBufferBoolean() {
		assertEquals(plainPMap.toStringBuffer(true).toString(),"{4->3, 5->10, 9->3, 10->9}");
		//assertEquals(plainPMap.toStringBuffer(true).toString(),"{4->3, 5->10, 9->3, 10->9}");
		assertEquals(plainPMap.toStringBuffer().toString(),"{4->3,5->10,9->3,10->9}");
		assertEquals(plainEmptyPMap.toStringBuffer().toString(),"{}");
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals(plainPMap.toStringBufferLong().toString(),"UnsortedMap{4->3, 5->10, 9->3, 10->9}");
		assertEquals(plainEmptyPMap.toStringBufferLong().toString(),"UnsortedMap{}");
	}

	@Test
	public void testIsEmpty() {
		assertFalse(plainPMap.isEmpty());
		assertTrue(plainEmptyPMap.isEmpty());
	}

	@Test
	public void testIsSorted() {
		assertFalse(plainPMap.isSorted());
	}

	@Test
	public void testPut() {
		assertTrue(plainEmptyPMap.isEmpty());
		plainEmptyPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainEmptyPMap.put(a,b);
		assertFalse(plainEmptyPMap.isEmpty());
	}

	@Test
	public void testRemove() {
		assertTrue(plainEmptyPMap.isEmpty());
		plainEmptyPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainEmptyPMap.put(a,b);
		assertFalse(plainEmptyPMap.isEmpty());
		plainEmptyPMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) plainEmptyPMap.remove(a);
		assertTrue(plainEmptyPMap.isEmpty());
	}

	@Test
	public void testComparator() {
		assertEquals(plainPMap.comparator(),null);
	}

}
