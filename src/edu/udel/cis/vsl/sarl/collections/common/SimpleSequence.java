package edu.udel.cis.vsl.sarl.collections.common;

import java.util.ArrayList;
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

	/**
	 * A constant used in computing the hash code for an object of this class.
	 * It is used to increase the probability that hashcodes of objects from
	 * this class will be different from hashcodes of objects from other
	 * classes.
	 */
	private final static int classCode = SymbolicCollectionKind.SEQUENCE
			.hashCode();

	/**
	 * An array of length 0, which can be re-used in multiple instances since an
	 * array of length 0 can never be modified.
	 */
	private final static SymbolicExpression[] emptyArray = new SymbolicExpression[0];

	/**
	 * The current size, or number of elements, in this sequence. Note that this
	 * is not necessarily equal to the length of array {@link #elements}. In
	 * general, the size is less or equal to the length. This is for performance
	 * reasons: it is expensive to increase the length, since that entails
	 * creating a whole new array and copying the elements from the old one to
	 * the new one. So instead whenever a new array is needed, a length that is
	 * larger than necessary is used. The method to get the new length is in the
	 * super class; see {@link #newLength(int)}.
	 */
	private int size;

	/**
	 * The array storing the elements of the sequence. No Java <code>null</code>
	 * s are allowed, but SARL's <code>NULL</code> expression is OK. The length
	 * of this array is greater than or equal to {@link #size}. The elements in
	 * position {@link size} or greater do not contain useful data; they are
	 * there only in case of future expansion.
	 */
	private T[] elements;

	/**
	 * The number of elements which are <code>NULL</code>.
	 */
	private int numNull;

	/**
	 * Creates the sequence with specified size and elements.
	 * 
	 * <p>
	 * Preconditions: <code>size</code> must be less than or equal to
	 * <code>elements.length</code>. The first <code>size</code> elements of
	 * <code>elements</code> are the elements of the new sequence (in order).
	 * Furthermore, <code>numNull</code> must equal the total number of elements
	 * of the sequence that are <code>NULL</code>. Note that none of these
	 * conditions is necessarily checked; it is up to the caller to get them
	 * right.
	 * </p>
	 * 
	 * @param size
	 *            the size of the new sequence
	 * @param elements
	 *            array containing the elements of the new sequence (and
	 *            possibly additional data which is ignored)
	 * @param numNull
	 *            the number of elements of the sequence which are
	 *            <code>NULL</code>
	 */
	SimpleSequence(int size, T[] elements, int numNull) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.size = size;
		this.elements = elements;
		this.numNull = numNull;
	}

	/**
	 * Constructs new sequence under the assumption that the size of the new
	 * sequence is exactly equal to <code>elements.length</code>, i.e., all of
	 * the members of <code>elements</code> will be used to form the new
	 * sequence.
	 * 
	 * 
	 * @param elements
	 *            the elements of the sequence; object will be used, not copied.
	 *            All elements of elements must be non-<code>null</code> (but
	 *            <code>NULL</code> is OK)
	 * @param numNull
	 *            the number of elements of elements which are <code>NULL</code>
	 * @see {@link #SimpleSequence(int, SymbolicExpression[], int)}
	 */
	SimpleSequence(T[] elements, int numNull) {
		this(elements.length, elements, numNull);
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
		size = elements.length;
	}

	/**
	 * Constructs new empty sequence.
	 */
	@SuppressWarnings("unchecked")
	public SimpleSequence() {
		this(0, (T[]) emptyArray, 0);
	}

	/**
	 * Constructs sequence from iterating over given elements.
	 * 
	 * @param elements
	 *            some collection of elements of T
	 */
	public SimpleSequence(Collection<? extends T> elements) {
		super(SymbolicCollectionKind.SEQUENCE);
		size = elements.size();

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
		size = tempList.size();

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
		T[] newArray = (T[]) new SymbolicExpression[START_LENGTH];

		newArray[0] = element;
		this.size = 1;
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

	/**
	 * A simple iterator over the elements of this sequence, in order.
	 * 
	 * @author siegel
	 */
	class ArrayIterator implements Iterator<T> {
		int nextIndex = 0;

		@Override
		public boolean hasNext() {
			return nextIndex < size;
		}

		@Override
		public T next() {
			T result = elements[nextIndex];

			nextIndex++;
			return result;
		}

		@Override
		public void remove() {
			throw new SARLException("Cannot remove from a SARL sequence using"
					+ "the iterator");
		}
	};

	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T get(int index) {
		return elements[index];
	}

	@Override
	public SymbolicSequence<T> add(T element) {
		if (!isCommitted() && size < elements.length) {
			elements[size] = element;
			if (element.isNull())
				numNull++;
			return this;
		}

		// if we are assuming committed=>size=length,
		// then at this point we know size=length.
		// Otherwise it is possible that this is committed
		// and size<length. In any case, we need to make
		// a new array, and the new length will be obtained
		// from the current size (not length).
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[newLength(size)];
		int newNumNull = element.isNull() ? numNull + 1 : numNull;

		System.arraycopy(elements, 0, newArray, 0, size);
		newArray[size] = element;
		if (isCommitted()) {
			return new SimpleSequence<T>(size + 1, newArray, newNumNull);
		} else {
			elements = newArray;
			numNull = newNumNull;
			size++;
			return this;
		}
	}

	@Override
	public SymbolicSequence<T> set(int index, T element) {
		int newNumNull = numNull;

		if (elements[index].isNull())
			newNumNull--;
		if (element.isNull())
			newNumNull++;
		if (isCommitted()) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[size];

			System.arraycopy(elements, 0, newArray, 0, size);
			newArray[index] = element;
			return new SimpleSequence<T>(size, newArray, newNumNull);
		} else {
			numNull = newNumNull;
			elements[index] = element;
			return this;
		}
	}

	@Override
	public SymbolicSequence<T> remove(int index) {
		int newNumNull = elements[index].isNull() ? numNull - 1 : numNull;

		if (isCommitted()) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[size - 1];

			System.arraycopy(elements, 0, newArray, 0, index);
			System.arraycopy(elements, index + 1, newArray, index, size - index
					- 1);
			return new SimpleSequence<T>(size - 1, newArray, newNumNull);
		} else {
			System.arraycopy(elements, index + 1, elements, index, size - index
					- 1);
			size--;
			numNull = newNumNull;
			return this;
		}
	}

	@Override
	public SymbolicSequence<T> setExtend(int index, T value, T filler) {
		if (index < size)
			return set(index, value);

		int newNumNull = numNull;

		if (filler.isNull())
			newNumNull += index - size;
		if (value.isNull())
			newNumNull++;
		if (isCommitted()) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[newLength(index)];

			System.arraycopy(elements, 0, newArray, 0, size);
			for (int i = size; i < index; i++)
				newArray[i] = filler;
			newArray[index] = value;
			return new SimpleSequence<T>(index + 1, newArray, newNumNull);
		} else {
			if (index >= elements.length) {
				@SuppressWarnings("unchecked")
				T[] newArray = (T[]) new SymbolicExpression[newLength(index)];

				System.arraycopy(elements, 0, newArray, 0, size);
				elements = newArray;
			}
			for (int i = size; i < index; i++)
				elements[i] = filler;
			elements[index] = value;
			numNull = newNumNull;
			size = index + 1;
			return this;
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
		if (isCommitted()) {
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
		} else {
			@SuppressWarnings("unchecked")
			U[] newArray = (U[]) elements;

			numNull = 0;
			for (int i = 0; i < size; i++) {
				U newElement = transform.apply(elements[i]);

				newArray[i] = newElement;
				if (newElement.isNull())
					numNull++;
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

		// we already know they have the same size since it is a
		// precondition of this method...

		if (o instanceof SimpleSequence<?>) {
			SimpleSequence<T> simpleThat = (SimpleSequence<T>) o;

			for (int i = 0; i < size; i++)
				if (!this.elements[i].equals(simpleThat.elements[i]))
					return false;
			return true;
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

		for (int i = 0; i < size; i++)
			result = result ^ elements[i].hashCode();
		return result;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		for (int i = 0; i < size; i++)
			elements[i] = factory.canonic(elements[i]);
	}

	@Override
	public SymbolicSequence<T> insert(int index, T element) {
		int newNumNull = element.isNull() ? numNull + 1 : numNull;

		if (isCommitted()) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[newLength(size)];

			System.arraycopy(elements, 0, newArray, 0, index);
			newArray[index] = element;
			System.arraycopy(elements, index, newArray, index + 1, size - index);
			return new SimpleSequence<T>(size + 1, newArray, newNumNull);
		} else {
			if (size == elements.length) {
				@SuppressWarnings("unchecked")
				T[] newArray = (T[]) new SymbolicExpression[newLength(size)];

				System.arraycopy(elements, 0, newArray, 0, index);
				System.arraycopy(elements, index, newArray, index + 1, size
						- index);
				elements = newArray;
			} else {
				System.arraycopy(elements, index, elements, index + 1, size
						- index);
			}
			elements[index] = element;
			size++;
			numNull = newNumNull;
			return this;
		}
	}

	@Override
	public int getNumNull() {
		return numNull;
	}

	@Override
	protected void commitChildren() {
		for (T element : elements)
			if (element != null)
				element.commit();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Not only does the usual commit of this object and descendants, but also
	 * trims the internal array used to store the elements to the exact size.
	 */
	@Override
	public void commit() {
		if (size != elements.length) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[size];

			System.arraycopy(elements, 0, newArray, 0, size);
			elements = newArray;
		}
		super.commit();
	}
}
