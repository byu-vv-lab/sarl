package edu.udel.cis.vsl.sarl.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/** An immutable, empty set. */
public class EmptySet<E> implements Set<E> {

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new EmptyIterator<E>();
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length == 0)
			return a;
		return Arrays.copyOf(a, 0);
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("This is an immutable set");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("This is an immutable set");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return c.isEmpty();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("This is an immutable set");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("This is an immutable set");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("This is an immutable set");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("This is an immutable set");
	}

}
