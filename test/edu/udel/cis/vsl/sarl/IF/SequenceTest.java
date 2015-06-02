package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class SequenceTest {

	private static PreUniverse universe = (PreUniverse) SARL
			.newStandardUniverse();

	private static CollectionFactory cf = universe.collectionFactory();

	private static SymbolicType intType = universe.integerType();

	private static Transform<SymbolicExpression, SymbolicExpression> trans1 = new Transform<SymbolicExpression, SymbolicExpression>() {

		@Override
		public SymbolicExpression apply(SymbolicExpression x) {
			// TODO Auto-generated method stub
			if (x.equals(universe.integer(1)))
				return universe.symbolicConstant(universe.stringObject("a"),
						intType);
			if (x.equals(universe.integer(2)))
				return universe.symbolicConstant(universe.stringObject("b"),
						intType);
			if (x.equals(universe.integer(3)))
				return universe.symbolicConstant(universe.stringObject("c"),
						intType);
			return x;
		}

	};

	// private static SymbolicExpression x1 = universe.integer(1);
	// private static SymbolicExpression x2 = universe.integer(2);
	// private static SymbolicExpression x3 = universe.integer(3);

	@Test
	public void singletonSeqGet() {
		SymbolicExpression x1 = universe.integer(27);

		assertTrue(x1.isFree());
		assertFalse(x1.isImmutable());

		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		assertFalse(x1.isFree());
		assertFalse(x1.isImmutable());
		assertEquals(1, seq.size());
		assertEquals(x1, seq.get(0));
	}

	@Test
	public void seqRemoveMut() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		assertFalse(seq.isImmutable());
		seq.remove(0);
		assertEquals(0, seq.size());
		assertTrue(x1.isFree());
	}

	@Test
	public void seqRemoveImmut() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		seq.commit();
		assertTrue(seq.isImmutable());
		SymbolicSequence<SymbolicExpression> seq2 = seq.remove(0);
		assertEquals(1, seq.size());
		assertEquals(0, seq2.size());
		assertTrue(x1.isImmutable());
		assertFalse(seq2.isImmutable());
	}

	@Test
	public void seqAdd0() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		SymbolicSequence<SymbolicExpression> seq2 = seq.add(x1);

		assertEquals(1, seq2.size());
		assertEquals(x1, seq2.get(0));
		assertFalse(x1.isFree());
		assertFalse(x1.isImmutable());
	}

	@Test
	public void seqAddMut1() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicExpression x2 = universe.integer(34);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);
		SymbolicSequence<SymbolicExpression> seq2 = seq1.add(x2);

		assertSame(seq1, seq2);
		assertEquals(2, seq2.size());
		assertEquals(x1, seq2.get(0));
		assertEquals(x2, seq2.get(1));
		assertFalse(seq2.isImmutable());
		assertFalse(x1.isFree());
		assertFalse(x1.isImmutable());
		assertFalse(x2.isFree());
		assertFalse(x2.isImmutable());
	}

	@Test
	public void seqAddImmut1() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicExpression x2 = universe.integer(34);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);

		seq1.commit();

		SymbolicSequence<SymbolicExpression> seq2 = seq1.add(x2);

		assertNotSame(seq1, seq2);
		assertEquals(2, seq2.size());
		assertEquals(x1, seq2.get(0));
		assertEquals(x2, seq2.get(1));
		assertFalse(seq2.isImmutable());
		assertTrue(x1.isImmutable());
		assertFalse(x2.isFree());
		assertFalse(x2.isImmutable());
	}

	@Test
	public void insert0() {
		SymbolicSequence<SymbolicExpression> seq0 = cf.emptySequence();
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq1 = seq0.insert(0, x1);

		assertEquals(0, seq0.size());
		assertEquals(1, seq1.size());
		assertEquals(x1, seq1.get(0));
	}

	@Test
	public void insertMut10() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);
		SymbolicExpression x2 = universe.integer(34);
		SymbolicSequence<SymbolicExpression> seq2 = seq1.insert(0, x2);

		assertSame(seq1, seq2);
		assertEquals(2, seq2.size());
		assertEquals(x2, seq2.get(0));
		assertEquals(x1, seq2.get(1));
	}

	@Test
	public void insertMut11() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);
		SymbolicExpression x2 = universe.integer(34);
		SymbolicSequence<SymbolicExpression> seq2 = seq1.insert(1, x2);

		assertSame(seq1, seq2);
		assertEquals(2, seq2.size());
		assertEquals(x1, seq2.get(0));
		assertEquals(x2, seq2.get(1));
	}

	@Test
	public void insertImmut10() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);

		seq1.commit();

		SymbolicExpression x2 = universe.integer(34);
		SymbolicSequence<SymbolicExpression> seq2 = seq1.insert(0, x2);

		assertNotSame(seq1, seq2);
		assertEquals(1, seq1.size());
		assertEquals(x1, seq1.get(0));
		assertEquals(2, seq2.size());
		assertEquals(x2, seq2.get(0));
		assertEquals(x1, seq2.get(1));
	}

	@Test
	public void insertImmut11() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);

		seq1.commit();

		SymbolicExpression x2 = universe.integer(34);
		SymbolicSequence<SymbolicExpression> seq2 = seq1.insert(1, x2);

		assertNotSame(seq1, seq2);
		assertEquals(1, seq1.size());
		assertEquals(x1, seq1.get(0));
		assertEquals(2, seq2.size());
		assertEquals(x1, seq2.get(0));
		assertEquals(x2, seq2.get(1));
	}

	@Test
	public void insertMutMid() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicExpression x2 = universe.integer(34);
		SymbolicExpression x3 = universe.integer(52);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		seq.add(x2);
		seq.insert(1, x3);
		assertEquals(3, seq.size());
		assertEquals(x1, seq.get(0));
		assertEquals(x3, seq.get(1));
		assertEquals(x2, seq.get(2));
	}

	@Test
	public void insertImmutMid() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicExpression x2 = universe.integer(34);
		SymbolicExpression x3 = universe.integer(52);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1), seq2 = seq1
				.add(x2);

		seq2.commit();

		SymbolicSequence<SymbolicExpression> seq3 = seq2.insert(1, x3);

		assertEquals(3, seq3.size());
		assertEquals(x1, seq3.get(0));
		assertEquals(x3, seq3.get(1));
		assertEquals(x2, seq3.get(2));
		assertEquals(2, seq2.size());
	}

	@Test
	public void insertBigMut() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		int n = 100;

		for (int i = 0; i < n; i++) {
			seq = seq.insert(0, universe.integer(i));
		}
		assertEquals(n, seq.size());
		for (int i = 0; i < n; i++) {
			assertEquals(universe.integer(n - i - 1), seq.get(i));
		}
		assertFalse(seq.isImmutable());
	}

	@Test
	public void insertBigImmut() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		int n = 100;

		for (int i = 0; i < n; i++) {
			seq = seq.insert(0, universe.integer(i));
			seq.commit();
		}
		assertEquals(n, seq.size());
		for (int i = 0; i < n; i++) {
			assertEquals(universe.integer(n - 1 - i), seq.get(i));
		}
		assertTrue(seq.isImmutable());
	}

	@Test
	public void insertNull() {
		SymbolicExpression x1 = universe.integer(32);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		assertEquals(0, seq.getNumNull());
		seq.insert(0, universe.nullExpression());
		assertEquals(1, seq.getNumNull());
		assertEquals(2, seq.size());
		assertEquals(x1, seq.get(1));
		assertEquals(universe.nullExpression(), seq.get(0));
	}

	@Test
	public void addBigMut() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		int n = 100;

		for (int i = 0; i < n; i++) {
			seq = seq.add(universe.integer(i));
		}
		assertEquals(n, seq.size());
		for (int i = 0; i < n; i++) {
			assertEquals(universe.integer(i), seq.get(i));
		}
		assertFalse(seq.isImmutable());
	}

	@Test
	public void addBigImmut() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		int n = 100;

		for (int i = 0; i < n; i++) {
			seq = seq.add(universe.integer(i));
			seq.commit();
		}
		assertEquals(n, seq.size());
		for (int i = 0; i < n; i++) {
			assertEquals(universe.integer(i), seq.get(i));
		}
		assertTrue(seq.isImmutable());
	}

	@Test
	public void setMut() {
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2), universe.integer(3) });
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { universe.integer(1),
						universe.integer(32), universe.integer(3) });

		seq.set(1, universe.integer(32));
		assertEquals(expected, seq);
	}

	@Test
	public void setImmut() {
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2), universe.integer(3) });
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { universe.integer(1),
						universe.integer(32), universe.integer(3) });

		seq.commit();

		SymbolicSequence<SymbolicExpression> actual = seq.set(1,
				universe.integer(32));

		assertEquals(expected, actual);
		assertEquals(universe.integer(2), seq.get(1));
	}

	@Test
	public void setNull() {
		SymbolicExpression x1 = universe.integer(10);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		assertEquals(0, seq.getNumNull());
		seq.set(0, universe.nullExpression());
		assertEquals(1, seq.getNumNull());
		seq.set(0, universe.nullExpression());
		assertEquals(1, seq.getNumNull());
	}

	@Test
	public void collection() {
		List<NumericExpression> list = Arrays.asList(universe.integer(1),
				universe.integer(2), universe.integer(3));
		SymbolicSequence<NumericExpression> seq = cf.sequence(list);
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2), universe.integer(3) });

		assertEquals(expected, seq);
	}

	@Test
	public void apply1() {
		SymbolicExpression x2 = universe.integer(2);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x2);
		SymbolicSequence<SymbolicExpression> expected = cf
				.singletonSequence((SymbolicExpression) universe
						.symbolicConstant(universe.stringObject("b"), intType));

		seq.apply(trans1);
		assertEquals(expected, seq);
	}
}
