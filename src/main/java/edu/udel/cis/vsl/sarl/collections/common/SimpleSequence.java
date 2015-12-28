package edu.udel.cis.vsl.sarl.collections.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * Implementation based on arrays. The entire array is copied with every
 * operation. Performance can be good for small sized.
 * 
 * @author siegel
 * 
 * @param <T>
 *            the kind of symbolic expressions that will be put in the
 *            collection
 */
public class SimpleSequence<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSequence<T> {

	private final static int classCode = SymbolicCollectionKind.SEQUENCE
			.hashCode();

	private final static SymbolicExpression[] emptyArray = new SymbolicExpression[0];

	/**
	 * The elements of the sequence. No nulls are allowed, but NULL is OK.
	 */
	private T[] elements;

	/**
	 * The number of elements which are NULL.
	 */
	private int numNull;

	/**
	 * The numNull is NOT checked. It better be right, or all bets are off.
	 * 
	 * @param elements
	 *            the elements of the sequence; object will be used, not copied.
	 *            All elements of elements must be non-null (but NULL is OK)
	 * @param numNull
	 *            the number of elements of elements which are NULL (not null)
	 */
	SimpleSequence(T[] elements, int numNull) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.elements = elements;
		this.numNull = numNull;
	}

	/**
	 * Creates new instance from given elements. Computes how many of those
	 * elements are NULL and sets internal state accordingly.
	 * 
	 * @param elements
	 *            an array of T with no null elements (but NULL elements are OK)
	 */
	public SimpleSequence(T[] elements) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.elements = elements;

		numNull = 0;
		for (SymbolicExpression expr : elements)
			if (expr.isNull())
				numNull++;
	}

	/**
	 * Constructs empty sequence.
	 */
	@SuppressWarnings("unchecked")
	public SimpleSequence() {
		this((T[]) emptyArray, 0);
	}

	/**
	 * Constructs sequence from iterating over given elements.
	 * 
	 * @param elements
	 *            some collection of elements of T
	 */
	public SimpleSequence(Collection<? extends T> elements) {
		super(SymbolicCollectionKind.SEQUENCE);
		int size = elements.size();
		@SuppressWarnings("unchecked")
		T[] tempArray = (T[]) new SymbolicExpression[size];

		elements.toArray(tempArray);
		this.elements = tempArray;
		numNull = 0;
		for (SymbolicExpression expr : this.elements)
			if (expr.isNull())
				numNull++;
	}

	/**
	 * Constructs sequence from iterating over given elements.
	 * 
	 * @param elements
	 *            some iterable of elements of T
	 */
	public SimpleSequence(Iterable<? extends T> elements) {
		super(SymbolicCollectionKind.SEQUENCE);

		ArrayList<T> tempList = new ArrayList<T>();

		for (T element : elements) {
			if (element.isNull())
				numNull++;
			tempList.add(element);
		}

		int size = tempList.size();
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size];

		tempList.toArray(newArray);
		this.elements = newArray;
	}

	/**
	 * Constructs singleton sequence consisting of given element.
	 * 
	 * @param element
	 *            an element of T
	 */
	public SimpleSequence(T element) {
		super(SymbolicCollectionKind.SEQUENCE);

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[] { element };

		this.elements = newArray;
		this.numNull = element.isNull() ? 1 : 0;
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

	class ArrayIterator implements Iterator<T> {
		int nextIndex = 0;

		@Override
		public boolean hasNext() {
			return nextIndex < elements.length;
		}

		@Override
		public T next() {
			T result = elements[nextIndex];

			nextIndex++;
			return result;
		}

		@Override
		public void remove() {
			throw new SARLException("Cannot remove from an immutable object");
		}
	};

	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator();
	}

	@Override
	public int size() {
		return elements.length;
	}

	@Override
	public T get(int index) {
		return elements[index];
	}

	@Override
	public SymbolicSequence<T> add(T element) {
		int size = elements.length;
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size + 1];
		int newNumNull = element.isNull() ? numNull + 1 : numNull;

		System.arraycopy(elements, 0, newArray, 0, size);
		newArray[size] = element;
		return new SimpleSequence<T>(newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> set(int index, T element) {
		int size = elements.length;
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size];
		int newNumNull = numNull;

		if (elements[index].isNull())
			newNumNull--;
		if (element.isNull())
			newNumNull++;
		System.arraycopy(elements, 0, newArray, 0, size);
		newArray[index] = element;
		return new SimpleSequence<T>(newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> remove(int index) {
		int size = elements.length;
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size - 1];
		int newNumNull = elements[index].isNull() ? numNull - 1 : numNull;

		System.arraycopy(elements, 0, newArray, 0, index);
		System.arraycopy(elements, index + 1, newArray, index, size - index - 1);
		return new SimpleSequence<T>(newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> setExtend(int index, T value, T filler) {
		int size = elements.length;

		if (index < size)
			return set(index, value);
		else {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[index + 1];
			int newNumNull = numNull;

			System.arraycopy(elements, 0, newArray, 0, size);
			for (int i = size; i < index; i++)
				newArray[i] = filler;
			if (filler.isNull())
				newNumNull += index - size;
			newArray[index] = value;
			if (value.isNull())
				newNumNull++;
			return new SimpleSequence<T>(newArray, newNumNull);
		}
	}

	@Override
	public SymbolicSequence<T> subSequence(int start, int end) {
		int size = elements.length;

		if (start == 0 && end == size)
			return this;

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[end - start];

		System.arraycopy(elements, start, newArray, 0, end - start);
		return new SimpleSequence<T>(newArray);
	}

	@Override
	public <U extends SymbolicExpression> SymbolicSequence<U> apply(
			Transform<T, U> transform) {
		int size = elements.length;

		for (int i = 0; i < size; i++) {
			T t = elements[i];
			U u = transform.apply(t);

			if (t != u) {
				@SuppressWarnings("unchecked")
				U[] newArray = (U[]) new SymbolicExpression[size];

				System.arraycopy(elements, 0, newArray, 0, i);
				newArray[i] = u;
				for (i++; i < size; i++)
					newArray[i] = transform.apply(elements[i]);
				return new SimpleSequence<U>(newArray);
			}
		}

		@SuppressWarnings("unchecked")
		SymbolicSequence<U> result = (SymbolicSequence<U>) this;

		return result;
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		if (this == o)
			return true;

		SymbolicSequence<T> that = (SymbolicSequence<T>) o;

		if (this.numNull != that.getNumNull())
			return false;
		if (o instanceof SimpleSequence<?>) {
			SimpleSequence<T> simpleThat = (SimpleSequence<T>) o;

			return Arrays.equals(this.elements, simpleThat.elements);
		}

		Iterator<T> these = this.iterator();
		Iterator<T> those = that.iterator();

		while (these.hasNext()) {
			if (!these.next().equals(those.next()))
				return false;
		}
		return true;
	}

	@Override
	protected int computeHashCode() {
		int result = classCode;

		for (T element : this)
			result = result ^ element.hashCode();
		return result;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		int size = elements.length;

		for (int i = 0; i < size; i++)
			elements[i] = factory.canonic(elements[i]);
	}

	@Override
	public SymbolicSequence<T> insert(int index, T element) {
		int size = elements.length;
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size + 1];

		System.arraycopy(elements, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(elements, index, newArray, index + 1, size - index);
		return new SimpleSequence<T>(newArray, element.isNull() ? numNull + 1
				: numNull);
	}

	@Override
	public int getNumNull() {
		return numNull;
	}
}
