package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.krukow.clj_ds.PersistentVector;
import com.github.krukow.clj_ds.Persistents;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * Implementation of SymbolicSequence based on Clojure clj-ds data structures.
 * There are the data structures used by Clojure that have been extracted for
 * pure Java programs. This implementation basically wraps an instance of
 * clj-ds's PersistentVector.
 * 
 * @author siegel
 * 
 * @param <T>
 *            the kind of symbolic expressions that will be put in the
 *            collection
 */
public class CljSequence<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSequence<T> {

	/**
	 * The Clojure PersistentVector which holds the data of the sequence.
	 */
	private PersistentVector<T> pvector;

	public CljSequence(PersistentVector<T> pvector) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.pvector = pvector;
	}

	/**
	 * Constructs empty sequence.
	 */
	public CljSequence() {
		this(Persistents.<T> vector());
	}

	/**
	 * Constructs sequence from iterating over given elements.
	 * 
	 * @param elements
	 *            some collection of elements of T
	 */
	public CljSequence(Collection<? extends T> elements) {
		this(Persistents.vector(elements));
	}

	/**
	 * Constructs sequence from iterating over given elements.
	 * 
	 * @param elements
	 *            some iterable of elements of T
	 */
	public CljSequence(Iterable<? extends T> elements) {
		this(Persistents.vector(elements));
	}

	/**
	 * Constructs sequence from given array of elements.
	 * 
	 * @param elements
	 *            array of elements of T
	 */
	public CljSequence(T[] elements) {
		this(Persistents.vector(elements));
	}

	/**
	 * Constructs singleton sequence consisting of given element.
	 * 
	 * @param element
	 *            an element of T
	 */
	public CljSequence(T element) {
		this(Persistents.vector(element));
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();
		boolean first = true;

		result.append("<");

		for (T element : this) {
			if (first)
				first = false;
			else
				result.append(",");
			result.append(element == null ? "null" : element
					.toStringBuffer(false));
		}
		result.append(">");
		return result;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer result = new StringBuffer("Sequence");

		result.append(toStringBuffer(true));
		return result;
	}

	@Override
	public Iterator<T> iterator() {
		return pvector.iterator();
	}

	@Override
	public int size() {
		return pvector.size();
	}

	@Override
	public T get(int index) {
		return pvector.get(index);
	}

	@Override
	public SymbolicSequence<T> add(T element) {
		return new CljSequence<T>(pvector.plus(element));
	}

	@Override
	public SymbolicSequence<T> set(int index, T element) {
		return new CljSequence<T>(pvector.plusN(index, element));
	}

	@Override
	public SymbolicSequence<T> remove(int index) {
		// not supported by PersistentVector
		int newSize = pvector.size() - 1;
		PersistentVector<T> newVector = pvector;

		for (int i = index; i < newSize; i++)
			newVector = newVector.plusN(i, newVector.get(i + 1));
		return new CljSequence<T>(newVector.minus());
	}

	@Override
	public SymbolicSequence<T> setExtend(int index, T value, T filler) {
		int size = pvector.size();

		if (index < size)
			return set(index, value);
		else {
			PersistentVector<T> newVector = pvector;

			for (int i = size; i < index; i++)
				newVector = newVector.plus(filler);
			newVector = newVector.plus(value);
			return new CljSequence<T>(newVector);
		}
	}

	@Override
	public SymbolicSequence<T> subSequence(int start, int end) {
		return new CljSequence<T>(pvector.subList(start, end));
	}

	@Override
	public <U extends SymbolicExpression> SymbolicSequence<U> apply(
			Transform<T, U> transform) {
		int count = 0;
		Iterator<T> iter = pvector.iterator();

		while (iter.hasNext()) {
			T t = iter.next();
			U u = transform.apply(t);

			if (t != u) {
				@SuppressWarnings("unchecked")
				// TODO: slow: can't we create a sublist without
				// iterating over all elements?
				PersistentVector<U> newVector = Persistents
						.vector((List<U>) pvector.subList(0, count));

				newVector = newVector.plus(u);
				while (iter.hasNext())
					newVector = newVector.plus(transform.apply(iter.next()));
				return new CljSequence<U>(newVector);
			}
			count++;
		}
		{
			@SuppressWarnings("unchecked")
			SymbolicSequence<U> result = (SymbolicSequence<U>) this;

			return result;
		}
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		if (this == o)
			return true;
		if (this.size() != o.size())
			return false;
		else {
			SymbolicSequence<T> that = (SymbolicSequence<T>) o;
			Iterator<T> these = this.iterator();
			Iterator<T> those = that.iterator();

			while (these.hasNext()) {
				// TODO this check should be unnecessary but a bug
				// in clj-ds requires that hasNext be invoked before
				// each call to next for any iterator. The bug
				// has been reported and when/if it is every fixed,
				// this can be removed.
				if (!those.hasNext())
					return false;
				if (!these.next().equals(those.next()))
					return false;
			}
			return true;
		}
	}

	@Override
	protected int computeHashCode() {
		return pvector.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<T> iter = iterator();
		int count = 0;

		while (iter.hasNext()) {
			T expr = iter.next();

			if (!expr.isCanonic()) {
				// TODO: slow. Isn't there a way to get a sublist without
				// having to iterate over everything?
				PersistentVector<T> newVector = Persistents.vector(pvector
						.subList(0, count));

				T canonic = factory.canonic(expr);

				assert canonic != null;
				newVector = newVector.plus(canonic);
				while (iter.hasNext()) {
					canonic = factory.canonic(iter.next());
					newVector = newVector.plus(canonic);
				}
				pvector = newVector;
				return;
			}
			count++;
		}
	}

	@Override
	public SymbolicSequence<T> insert(int index, T element) {
		PersistentVector<T> newVector = pvector;
		int size = newVector.size();

		if (index == size) {
			newVector = newVector.plus(element);
		} else {
			newVector = newVector.plus(newVector.get(size - 1));
			for (int i = size - 1; i > index; i--)
				newVector = newVector.plusN(i, newVector.get(i - 1));
			newVector = newVector.plusN(index, element);
		}
		return new CljSequence<T>(newVector);
	}
}
