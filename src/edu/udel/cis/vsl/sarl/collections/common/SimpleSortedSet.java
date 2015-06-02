package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class SimpleSortedSet<T extends SymbolicExpression> extends
		CommonSortedSet<T> {

	private final static SymbolicExpression[] emptyArray = new SymbolicExpression[0];

	int size;

	private T[] elements;

	private Comparator<T> elementComparator;

	/**
	 * Constructs new instance using the given elements array as the elements
	 * field. The array is not copied.
	 * 
	 * @param comparator
	 *            the element comparator
	 * @param elements
	 *            the array of elements that will be used as the main field
	 */
	@SuppressWarnings("unchecked")
	protected SimpleSortedSet(int size, Comparator<? super T> comparator,
			T[] elements) {
		super();
		this.size = size;
		elementComparator = (Comparator<T>) comparator;
		this.elements = elements;
		for (T element : elements)
			element.makeChild();
	}

	/**
	 * Constructs new empty set with given comparator.
	 * 
	 * @param comparator
	 *            the element comparator
	 */
	@SuppressWarnings("unchecked")
	protected SimpleSortedSet(Comparator<? super T> comparator) {
		this(0, comparator, (T[]) emptyArray);
	}

	/**
	 * Constructs new instance using the given elements array as the elements
	 * field. The array is not copied.
	 * 
	 * @param comparator
	 *            the element comparator
	 * @param elements
	 *            the array of elements that will be used as the main field
	 */
	protected SimpleSortedSet(Comparator<? super T> comparator, T[] elements) {
		this(elements.length, comparator, elements);
	}

	/**
	 * Constructs new instance by taking the elements from the given ordered
	 * list. It is assumed that the list is sorted.
	 * 
	 * @param comparator
	 *            the element comparator
	 * @param list
	 *            the sorted list of elements
	 */
	@SuppressWarnings("unchecked")
	protected SimpleSortedSet(Comparator<? super T> comparator,
			List<? extends T> list) {
		super();
		// safe since we are only restricting the elements that will be
		// compared:
		this.elementComparator = (Comparator<T>) comparator;
		// safe since this array is immutable:
		this.elements = (T[]) new SymbolicExpression[list.size()];
		list.toArray(this.elements);
		this.size = elements.length;
		for (T element : elements)
			element.makeChild();
	}

	SimpleSortedSet(Comparator<? super T> comparator, Set<? extends T> javaSet) {
		super();
		this.size = javaSet.size();

		@SuppressWarnings("unchecked")
		Comparator<T> comparator2 = (Comparator<T>) comparator;
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[this.size];

		javaSet.toArray(newArray);
		Arrays.sort(newArray, comparator);
		this.elementComparator = comparator2;
		this.elements = newArray;
		for (T element : elements)
			element.makeChild();
	}

	/**
	 * Finds the index of the given element in the array {@link #elements}. If
	 * there exists an element of the array that is equal to the specified
	 * element, there can be at most one such element of the array, and this
	 * method returns the index of that element. Otherwise, it returns -1.
	 * 
	 * @param element
	 *            any member of T
	 * @return index of the element or -1
	 */
	private int find(T element) {
		int lo = 0, hi = size - 1;

		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			T x = elements[mid];
			int compare = elementComparator.compare(x, element);

			if (compare == 0) {
				return mid;
			} else if (compare < 0) { // x<element
				lo = mid + 1;
			} else { // x>element
				hi = mid - 1;
			}
		}
		return -1;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
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
				throw new SARLException(
						"cannot remove element from a set using iterator");
			}
		};
	}

	@Override
	public boolean contains(T element) {
		return find(element) >= 0;
	}

	@Override
	public Comparator<T> comparator() {
		return elementComparator;
	}

	@Override
	public SortedSymbolicSet<T> add(T element) {
		int lo = 0, hi = size - 1;

		// loop invariant: hi-lo >= -1.
		// hi>=lo -> hi-((lo+hi)/2 + 1) >= -1.
		// hi>=lo -> ((lo+hi)/2 -1) - lo >= -1.
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			T x = elements[mid];
			int compare = elementComparator.compare(x, element);

			if (compare == 0) {
				return this;
			} else if (compare < 0) { // x<element
				lo = mid + 1;
			} else { // x>element
				hi = mid - 1;
			}
		}
		assert hi - lo == -1;
		// Example: hi=-1, lo=0
		// Example: hi=length-1, lo=length
		// lo is where element should be inserted
		if (isImmutable()) {
			@SuppressWarnings("unchecked")
			T[] newElements = (T[]) new SymbolicExpression[newLength(size)];

			System.arraycopy(elements, 0, newElements, 0, lo);
			newElements[lo] = element;
			System.arraycopy(elements, lo, newElements, lo + 1, size - lo);
			return new SimpleSortedSet<T>(size + 1, elementComparator,
					newElements);
		} else {
			if (size == elements.length) {
				@SuppressWarnings("unchecked")
				T[] newArray = (T[]) new SymbolicExpression[newLength(size)];

				System.arraycopy(elements, 0, newArray, 0, lo);
				System.arraycopy(elements, lo, newArray, lo + 1, size - lo);
				elements = newArray;
			} else {
				System.arraycopy(elements, lo, elements, lo + 1, size - lo);
			}
			element.makeChild();
			elements[lo] = element;
			size++;
			return this;
		}
	}

	@Override
	public SortedSymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		if (set instanceof SortedSymbolicSet<?>) {
			// note that addAll_helper will commit elements in set not in
			// this...
			List<T> merged = addAll_helper((SortedSymbolicSet<? extends T>) set);

			if (isImmutable()) {
				return new SimpleSortedSet<T>(elementComparator, merged);
			} else {
				size = merged.size();
				if (size > elements.length) {
					@SuppressWarnings("unchecked")
					T[] ts = (T[]) new SymbolicExpression[newLength(size)];

					elements = ts;
				}
				merged.toArray(elements);
				return this;
			}
		} else {
			// requires some work: sorting, etc.
			throw new SARLException(
					"Combining sorted and unsorted sets not yet implemented");
		}
	}

	@Override
	public SortedSymbolicSet<T> remove(T element) {
		int index = find(element);

		if (index < 0) {
			return this;
		} else if (isImmutable()) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[size - 1];

			System.arraycopy(elements, 0, newArray, 0, index);
			System.arraycopy(elements, index + 1, newArray, index, size - index
					- 1);
			return new SimpleSortedSet<T>(size - 1, elementComparator, newArray);
		} else {
			System.arraycopy(elements, index + 1, elements, index, size - index
					- 1);
			size--;
			element.release();
			return this;
		}
	}

	@Override
	public SortedSymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		if (this.size() == 0 || set.size() == 0)
			return this;
		if (set instanceof SortedSymbolicSet<?>) {
			List<T> merged = removeAll_helper((SortedSymbolicSet<? extends T>) set);

			if (isImmutable()) {
				return new SimpleSortedSet<T>(elementComparator, merged);
			} else {
				// TODO: you could probably do this in place in elements,
				// without going through a List
				merged.toArray(elements);
				size = merged.size();
				return this;
			}
		} else {
			throw new SARLException(
					"Combining sorted and unsorted sets not yet implemented");
		}
	}

	@Override
	public SortedSymbolicSet<T> keepOnly(SymbolicSet<? extends T> set) {
		if (this.size() == 0)
			return this;
		if (set.size() == 0) {
			if (isImmutable()) {
				return new SimpleSortedSet<T>(elementComparator);
			} else {
				@SuppressWarnings("unchecked")
				T[] empty = (T[]) emptyArray;

				elements = empty;
				size = 0;
				return this;
			}
		}
		if (set instanceof SortedSymbolicSet<?>) {
			List<T> merged = keepOnly_helper((SortedSymbolicSet<? extends T>) set);

			if (isImmutable()) {
				return new SimpleSortedSet<T>(elementComparator, merged);
			} else {
				merged.toArray(elements);
				size = merged.size();
				return this;
			}
		} else {
			throw new SARLException(
					"Combining sorted and unsorted sets not yet implemented");
		}
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		for (int i = 0; i < size; i++) {
			elements[i] = factory.canonic(elements[i]);
		}
	}

	@Override
	protected void commitChildren() {
		for (int i = 0; i < size; i++)
			elements[i].commit();
	}

	@Override
	public SortedSymbolicSet<T> commit() {
		if (size != elements.length) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new SymbolicExpression[size];

			System.arraycopy(elements, 0, newArray, 0, size);
			elements = newArray;
		}
		super.commit();
		return this;
	}
}
