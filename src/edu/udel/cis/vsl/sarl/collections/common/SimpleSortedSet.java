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

	private T[] elements;

	private Comparator<T> elementComparator;

	/**
	 * Constructs new empty set with given comparator.
	 * 
	 * @param comparator
	 *            the element comparator
	 */
	@SuppressWarnings("unchecked")
	SimpleSortedSet(Comparator<? super T> comparator) {
		this.elements = (T[]) new SymbolicExpression[0];
		this.elementComparator = (Comparator<T>) comparator;
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
	@SuppressWarnings("unchecked")
	SimpleSortedSet(Comparator<? super T> comparator, T[] elements) {
		super();
		elementComparator = (Comparator<T>) comparator;
		this.elements = elements;
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
	SimpleSortedSet(Comparator<? super T> comparator, List<? extends T> list) {
		super();
		// safe since we are only restricting the elements that will be
		// compared:
		this.elementComparator = (Comparator<T>) comparator;
		// safe since this array is immutable:
		this.elements = (T[]) new SymbolicExpression[list.size()];
		list.toArray(this.elements);
	}

	SimpleSortedSet(Comparator<? super T> comparator, Set<? extends T> javaSet) {
		super();

		int size = javaSet.size();
		@SuppressWarnings("unchecked")
		Comparator<T> comparator2 = (Comparator<T>) comparator;
		@SuppressWarnings("unchecked")
		T[] newArray = (T[]) new SymbolicExpression[size];

		javaSet.toArray(newArray);
		Arrays.sort(newArray, comparator);
		this.elementComparator = comparator2;
		this.elements = newArray;
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
		int lo = 0, hi = elements.length - 1;

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
		return elements.length;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
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
				throw new SARLException(
						"cannot remove element from an immutable set");
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
		int lo = 0, hi = elements.length - 1;

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

		@SuppressWarnings("unchecked")
		T[] newElements = (T[]) new SymbolicExpression[elements.length + 1];

		System.arraycopy(elements, 0, newElements, 0, lo);
		newElements[lo] = element;
		System.arraycopy(elements, lo, newElements, lo + 1, elements.length
				- lo);
		return new SimpleSortedSet<T>(elementComparator, newElements);
	}

	/**
	 * Computes the union of this and set. Precondition: this has at least one
	 * element and set has at least one element and set is sorted.
	 * 
	 * @param set
	 *            a symbolic set whose elements belong to T and which has at
	 *            least one element
	 * @return symbolic set which is sorted and union of this and set
	 */
	@Override
	public SortedSymbolicSet<T> addAll(SymbolicSet<? extends T> set) {
		if (set instanceof SortedSymbolicSet<?>) {
			List<T> merged = addAll_helper((SortedSymbolicSet<? extends T>) set);

			return new SimpleSortedSet<T>(elementComparator, merged);
		} else {
			// SymbolicExpression[] thatArray = toArray(set);
			// // following is safe since we are only going to put elements
			// // of type T into the array:
			// @SuppressWarnings("unchecked")
			// T[] thatArrayCast = (T[]) thatArray;
			//
			// // sort the given set. or sequence of inserts
			// Arrays.sort(thatArrayCast, elementComparator);
			throw new SARLException(
					"Combining sorted and unsorted sets not yet implemented");
		}
	}

	@Override
	public SortedSymbolicSet<T> remove(T element) {
		int index = find(element);

		if (index < 0) {
			return this;
		} else {
			@SuppressWarnings("unchecked")
			T[] newElements = (T[]) new SymbolicExpression[elements.length - 1];

			System.arraycopy(elements, 0, newElements, 0, index);
			System.arraycopy(elements, index + 1, newElements, index,
					elements.length - index - 1);
			return new SimpleSortedSet<T>(elementComparator, newElements);
		}
	}

	@Override
	public SortedSymbolicSet<T> removeAll(SymbolicSet<? extends T> set) {
		if (this.size() == 0)
			return this;
		if (set.size() == 0)
			return this;
		if (set instanceof SortedSymbolicSet<?>) {
			List<T> merged = removeAll_helper((SortedSymbolicSet<? extends T>) set);

			return new SimpleSortedSet<T>(elementComparator, merged);
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
			return new SimpleSortedSet<T>(elementComparator);
		}
		if (set instanceof SortedSymbolicSet<?>) {
			List<T> merged = keepOnly_helper((SortedSymbolicSet<? extends T>) set);

			return new SimpleSortedSet<T>(elementComparator, merged);
		} else {
			throw new SARLException(
					"Combining sorted and unsorted sets not yet implemented");
		}
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		int n = elements.length;

		for (int i = 0; i < n; i++) {
			elements[i] = factory.canonic(elements[i]);
		}
	}

}
