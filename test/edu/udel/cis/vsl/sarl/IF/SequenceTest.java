package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.collections.common.CommonSymbolicCollection;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class SequenceTest {

	private static PreUniverse universe = (PreUniverse) SARL
			.newStandardUniverse();

	private static CollectionFactory cf = universe.collectionFactory();

	private static SymbolicType intType = universe.integerType();

	private static Transform<SymbolicExpression, SymbolicExpression> trans1 = new Transform<SymbolicExpression, SymbolicExpression>() {

		@Override
		public SymbolicExpression apply(SymbolicExpression x) {
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

	private static Transform<SymbolicExpression, SymbolicExpression> identity = new Transform<SymbolicExpression, SymbolicExpression>() {

		@Override
		public SymbolicExpression apply(SymbolicExpression x) {
			return x;
		}

	};

	private static Transform<SymbolicExpression, SymbolicExpression> nullTrans = new Transform<SymbolicExpression, SymbolicExpression>() {

		@Override
		public SymbolicExpression apply(SymbolicExpression x) {
			return universe.nullExpression();
		}

	};

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
	public void removeNull() {
		SymbolicSequence<SymbolicExpression> seq = cf
				.singletonSequence(universe.nullExpression());

		assertEquals(1, seq.getNumNull());
		seq.remove(0);
		assertEquals(0, seq.getNumNull());
		assertEquals(0, seq.size());
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
	public void addMutNull() {
		SymbolicExpression x1 = universe.integer(27);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x1);

		seq.add(universe.nullExpression());

		SymbolicSequence<SymbolicExpression> expected = cf.sequence(Arrays
				.asList(x1, universe.nullExpression()));

		assertEquals(expected, seq);
	}

	@Test
	public void addImmutNull() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();

		seq.commit();

		SymbolicSequence<SymbolicExpression> actual = seq.add(universe
				.nullExpression());
		SymbolicSequence<SymbolicExpression> expected = cf.sequence(Arrays
				.asList(universe.nullExpression()));

		assertEquals(expected, actual);
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
		// why not exercise the Iterable constructor while we're at it...
		Iterable<SymbolicExpression> iterable = new Iterable<SymbolicExpression>() {
			@Override
			public Iterator<SymbolicExpression> iterator() {
				return Arrays.asList(universe.integer(1),
						universe.nullExpression(), universe.integer(3))
						.iterator();
			}
		};
		SymbolicSequence<SymbolicExpression> seq = cf.sequence(iterable);
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
	public void apply1Mut() {
		SymbolicExpression x2 = universe.integer(2);
		SymbolicSequence<SymbolicExpression> seq = cf.singletonSequence(x2);
		SymbolicSequence<SymbolicExpression> expected = cf
				.singletonSequence((SymbolicExpression) universe
						.symbolicConstant(universe.stringObject("b"), intType));

		seq.apply(trans1);
		assertEquals(expected, seq);
	}

	@Test
	public void applyNullMut() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { x1, x2 });
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { universe.nullExpression(),
						universe.nullExpression() });

		seq.apply(nullTrans);
		assertEquals(expected, seq);
	}

	@Test
	public void applyIdMut() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { x1, x2 });
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { x1, x2 });

		seq.apply(identity);
		assertEquals(expected, seq);
	}

	@Test
	public void subseq03() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicExpression x3 = universe.integer(3);
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { x1, x2, x3 });
		SymbolicSequence<SymbolicExpression> subseq = seq.subSequence(0, 3);
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { x1, x2, x3 });

		assertEquals(expected, subseq);
	}

	@Test
	public void subseq12() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicExpression x3 = universe.integer(3);
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { x1, x2, x3 });
		SymbolicSequence<SymbolicExpression> subseq = seq.subSequence(1, 2);
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] { x2 });

		assertEquals(expected, subseq);
	}

	@Test
	public void subseq00() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicExpression x3 = universe.integer(3);
		SymbolicSequence<SymbolicExpression> seq = cf
				.sequence(new SymbolicExpression[] { x1, x2, x3 });
		SymbolicSequence<SymbolicExpression> subseq = seq.subSequence(0, 0);
		SymbolicSequence<SymbolicExpression> expected = cf
				.sequence(new SymbolicExpression[] {});

		assertEquals(expected, subseq);
	}

	@Test
	public void setExtend1() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicSequence<SymbolicExpression> actual = seq.setExtend(2, x2, x1);
		SymbolicSequence<SymbolicExpression> expected = cf.sequence(Arrays
				.asList(x1, x1, x2));

		assertEquals(expected, actual);
	}

	@Test
	public void setExtendNull() {
		SymbolicSequence<SymbolicExpression> seq = cf.emptySequence();
		SymbolicExpression x1 = universe.integer(1);
		SymbolicSequence<SymbolicExpression> actual = seq.setExtend(2,
				universe.nullExpression(), x1);
		SymbolicSequence<SymbolicExpression> expected = cf.sequence(Arrays
				.asList(x1, x1, universe.nullExpression()));

		assertEquals(expected, actual);
	}

	@Test
	public void eqtestFakeYes() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicExpression x3 = universe.integer(3);
		SymbolicSequence<SymbolicExpression> seq1 = cf
				.sequence(new SymbolicExpression[] { x1, x2, x3 });
		SymbolicSequence<SymbolicExpression> seq2 = new FakeSequence(seq1);

		assertTrue(seq1.equals(seq2));
	}

	@Test
	public void eqtestFakeNo() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicExpression x2 = universe.integer(2);
		SymbolicExpression x3 = universe.integer(3);
		SymbolicSequence<SymbolicExpression> seq1 = cf
				.sequence(new SymbolicExpression[] { x1, x2, x3 });
		SymbolicSequence<SymbolicExpression> seq2 = new FakeSequence(
				cf.sequence(new SymbolicExpression[] { x1, x2, x1 }));

		assertFalse(seq1.equals(seq2));
	}

	@Test
	public void eqTestYes() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1), seq2 = cf
				.singletonSequence(x1);

		assertTrue(seq1.equals(seq2));
	}

	@Test
	public void eqTestDiffNulls() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1), seq2 = cf
				.singletonSequence(universe.nullExpression());

		assertFalse(seq1.equals(seq2));
	}

	@Test
	public void string() {
		SymbolicSequence<SymbolicExpression> seq = cf.sequence(Arrays.asList(
				universe.nullExpression(), universe.integer(45)));

		assertEquals("<NULL,45>", seq.toStringBuffer(true).toString());
	}

	@Test
	public void stringBufferLong() {
		SymbolicExpression x1 = universe.integer(1);
		SymbolicSequence<SymbolicExpression> seq1 = cf.singletonSequence(x1);

		assertEquals("Sequence<1>", seq1.toStringBufferLong().toString());
	}
}

class FakeSequence extends CommonSymbolicCollection<SymbolicExpression>
		implements SymbolicSequence<SymbolicExpression> {

	private SymbolicSequence<SymbolicExpression> seq;

	public FakeSequence(SymbolicSequence<SymbolicExpression> seq) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.seq = seq;
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		return seq.toStringBuffer(atomize);
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return seq.toStringBufferLong();
	}

	@Override
	public Iterator<SymbolicExpression> iterator() {
		return seq.iterator();
	}

	@Override
	public int size() {
		return seq.size();
	}

	@Override
	public SymbolicExpression get(int index) {
		return seq.get(index);
	}

	@Override
	public SymbolicSequence<SymbolicExpression> add(SymbolicExpression element) {
		return seq.add(element);
	}

	@Override
	public SymbolicSequence<SymbolicExpression> set(int index,
			SymbolicExpression element) {
		return seq.set(index, element);
	}

	@Override
	public SymbolicSequence<SymbolicExpression> remove(int index) {
		return seq.remove(index);
	}

	@Override
	public SymbolicSequence<SymbolicExpression> insert(int index,
			SymbolicExpression element) {
		return seq.insert(index, element);
	}

	@Override
	public SymbolicSequence<SymbolicExpression> setExtend(int index,
			SymbolicExpression value, SymbolicExpression filler) {
		return seq.setExtend(index, value, filler);
	}

	@Override
	public SymbolicSequence<SymbolicExpression> subSequence(int start, int end) {
		return seq.subSequence(start, end);
	}

	@Override
	public <U extends SymbolicExpression> SymbolicSequence<U> apply(
			Transform<SymbolicExpression, U> transform) {
		return seq.apply(transform);
	}

	@Override
	public int getNumNull() {
		return seq.getNumNull();
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<SymbolicExpression> o) {
		return false;
	}

	@Override
	protected void commitChildren() {
	}

	@Override
	protected int computeHashCode() {
		return seq.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

}
