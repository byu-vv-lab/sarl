package edu.udel.cis.vsl.sarl.collections.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * Implementation of sequences based on arrays. Performance can be good for
 * small sizes.
 * 
 * @author Stephen F. Siegel
 * 
 * @param <T>
 *            the kind of symbolic expressions that will be put in the
 *            collection
 */
public class SimpleSequence<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSequence<T> {

	// Static constants...

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

	// Instances fields...

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

	// Constructors...

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
	protected SimpleSequence(int size, T[] elements, int numNull) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.size = size;
		this.elements = elements;
		this.numNull = numNull;
		for (int i = 0; i < size; i++)
			elements[i].incrementReferenceCount();
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
		for (SymbolicExpression expr : elements) {
			expr.incrementReferenceCount();
			if (expr.isNull())
				numNull++;
		}
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
		for (SymbolicExpression expr : this.elements) {
			expr.incrementReferenceCount();
			if (expr.isNull())
				numNull++;
		}
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
			element.incrementReferenceCount();
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
		element.incrementReferenceCount();
		this.size = 1;
		this.elements = newArray;
		this.numNull = element.isNull() ? 1 : 0;
	}

	// Protected methods...

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
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

	// Public methods...

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
			result.append(element.toStringBuffer(false));
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
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[newLength(size)];
		int newNumNull = element.isNull() ? numNull + 1 : numNull;

		System.arraycopy(elements, 0, newArray, 0, size);
		newArray[size] = element;
		return new SimpleSequence<T>(size + 1, newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> addMut(T element) {
		if (isImmutable())
			return add(element);
		if (size < elements.length) {
			element.incrementReferenceCount();
			elements[size] = element;
			if (element.isNull())
				numNull++;
			size++;
		} else {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[newLength(size)];
			int newNumNull = element.isNull() ? numNull + 1 : numNull;

			System.arraycopy(elements, 0, newArray, 0, size);
			newArray[size] = element;
			elements = newArray;
			numNull = newNumNull;
			element.incrementReferenceCount();
			size++;
		}
		return this;
	}

	@Override
	public SymbolicSequence<T> set(int index, T element) {
		int newNumNull = numNull;

		if (elements[index].isNull())
			newNumNull--;
		if (element.isNull())
			newNumNull++;

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size];

		System.arraycopy(elements, 0, newArray, 0, size);
		newArray[index] = element;
		return new SimpleSequence<T>(size, newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> setMut(int index, T element) {
		if (isImmutable())
			return set(index, element);
		else {
			int newNumNull = numNull;

			if (elements[index].isNull())
				newNumNull--;
			if (element.isNull())
				newNumNull++;
			elements[index].decrementReferenceCount();
			element.incrementReferenceCount();
			numNull = newNumNull;
			elements[index] = element;
			return this;
		}
	}

	@Override
	public SymbolicSequence<T> remove(int index) {
		int newNumNull = elements[index].isNull() ? numNull - 1 : numNull;

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size - 1];

		System.arraycopy(elements, 0, newArray, 0, index);
		System.arraycopy(elements, index + 1, newArray, index, size - index - 1);
		return new SimpleSequence<T>(size - 1, newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> removeMut(int index) {
		if (isImmutable())
			return remove(index);
		numNull = elements[index].isNull() ? numNull - 1 : numNull;
		elements[index].decrementReferenceCount();
		System.arraycopy(elements, index + 1, elements, index, size - index - 1);
		size--;
		return this;
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

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[newLength(index)];

		System.arraycopy(elements, 0, newArray, 0, size);
		for (int i = size; i < index; i++)
			newArray[i] = filler;
		newArray[index] = value;
		return new SimpleSequence<T>(index + 1, newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> setExtendMut(int index, T value, T filler) {
		if (isImmutable())
			return setExtend(index, value, filler);
		if (index < size)
			return setMut(index, value);
		if (filler.isNull())
			numNull += index - size;
		if (value.isNull())
			numNull++;
		if (index >= elements.length) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[newLength(index)];

			System.arraycopy(elements, 0, newArray, 0, size);
			elements = newArray;
		}
		for (int i = size; i < index; i++) {
			filler.incrementReferenceCount();
			elements[i] = filler;
		}
		value.incrementReferenceCount();
		elements[index] = value;
		size = index + 1;
		return this;
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
	public <U extends SymbolicExpression> SymbolicSequence<U> applyMut(
			Transform<T, U> transform) {
		if (isImmutable())
			return apply(transform);

		@SuppressWarnings("unchecked")
		U[] newArray = (U[]) elements;

		numNull = 0;
		for (int i = 0; i < size; i++) {
			T t = elements[i];
			U u = transform.apply(t);

			if (t != u) {
				t.decrementReferenceCount();
				u.incrementReferenceCount();
			}
			newArray[i] = u;
			if (u.isNull())
				numNull++;
		}

		@SuppressWarnings("unchecked")
		SymbolicSequence<U> result = (SymbolicSequence<U>) this;

		return result;
	}

	@Override
	protected void canonizeChildren(ObjectFactory factory) {
		if (size == elements.length) {
			for (int i = 0; i < size; i++)
				elements[i] = factory.canonic(elements[i]);
		} else {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[size];

			for (int i = 0; i < size; i++)
				newArray[i] = factory.canonic(elements[i]);
			elements = newArray;
		}
	}

	@Override
	public SymbolicSequence<T> insert(int index, T element) {
		int newNumNull = element.isNull() ? numNull + 1 : numNull;

		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[newLength(size)];

		System.arraycopy(elements, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(elements, index, newArray, index + 1, size - index);
		return new SimpleSequence<T>(size + 1, newArray, newNumNull);
	}

	@Override
	public SymbolicSequence<T> insertMut(int index, T element) {
		if (isImmutable())
			return insert(index, element);
		numNull = element.isNull() ? numNull + 1 : numNull;
		if (size == elements.length) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[newLength(size)];

			System.arraycopy(elements, 0, newArray, 0, index);
			System.arraycopy(elements, index, newArray, index + 1, size - index);
			elements = newArray;
		} else {
			System.arraycopy(elements, index, elements, index + 1, size - index);
		}
		element.incrementReferenceCount();
		elements[index] = element;
		size++;
		return this;
	}

	@Override
	public int getNumNull() {
		return numNull;
	}

	@Override
	protected Iterable<? extends SymbolicObject> getChildren() {
		return this;
	}

	@Override
	protected void nullifyFields() {
		elements = null;
	}
}
