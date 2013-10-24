package edu.udel.cis.vsl.sarl.collections.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.common.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.ObjectFactoryStub;

public class CollectionTest {

	private static ObjectFactory objectFactory = new ObjectFactoryStub();

	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();

	private static CollectionFactory collectionFactory = Collections
			.newCollectionFactory(objectFactory);

	private static Comparator<SymbolicCollection<? extends SymbolicExpression>> comparator;

	private static SymbolicExpression x = new ExpressionStub("x");

	private static SymbolicExpression y = new ExpressionStub("y");
	
	private static SymbolicExpression a = new ExpressionStub("5");

	private static SymbolicExpression b = new ExpressionStub("9");
 
	private static CommonCollectionFactory mapCreate;
	private static SymbolicMap<SymbolicExpression, SymbolicExpression> collectionMap;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		collectionFactory.setElementComparator(elementComparator);
		collectionFactory.init();
		comparator = collectionFactory.comparator();
		collectionMap = mapCreate.emptySortedMap();
		collectionMap = collectionMap.put(a, b);
		collectionMap = collectionMap.put(x, b);
		collectionMap = collectionMap.put(b, y);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void emptySeq() {
		SymbolicSequence<SymbolicExpression> seq0 = collectionFactory
				.emptySequence();

		assertEquals(0, seq0.size());
	}

	@Test
	public void removeUndoesAddSeq() {
		SymbolicSequence<SymbolicExpression> seq0, seq1, seq2;

		seq0 = collectionFactory.emptySequence();
		seq1 = seq0.add(x);
		seq2 = seq1.remove(0);
		assertEquals(seq0, seq2);
	}

	@Test
	public void compareSeqsEq() {
		SymbolicSequence<SymbolicExpression> seq0, seq1, seq2;

		seq0 = collectionFactory.emptySequence();
		seq1 = seq0.add(x);
		seq2 = seq1.add(y);
		System.out.println("seq2 = " + seq2);
		assertEquals(0, comparator.compare(seq0, seq0));
		assertEquals(0, comparator.compare(seq1, seq1));
		assertEquals(0, comparator.compare(seq2, seq2));
		assertNotEquals(0, comparator.compare(seq0, seq1));
		assertNotEquals(0, comparator.compare(seq0, seq2));
		assertNotEquals(0, comparator.compare(seq1, seq0));
		assertNotEquals(0, comparator.compare(seq1, seq2));
		assertEquals(-comparator.compare(seq0, seq1),
				comparator.compare(seq1, seq0));
	}

}
