package edu.udel.cis.vsl.sarl.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class SingletonList<E> implements List<E> {

	private E element;

	public SingletonList(E element) {
		this.element = element;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return element.equals(o);
	}

	@Override
	public Iterator<E> iterator() {
		return new SingletonIterator<E>(element);
	}

	@Override
	public Object[] toArray() {
		return new Object[] { element };
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		a[0] = (T) element;
		return a;
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!(element.equals(o)))
				return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public E get(int index) {
		if (index == 0)
			return element;
		throw new NoSuchElementException();
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("SingletonList is immutable");
	}

	@Override
	public int indexOf(Object o) {
		if (element.equals(o))
			return 0;
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		if (element.equals(o))
			return 0;
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

}
