package edu.udel.cis.vsl.sarl.util;

import java.util.Iterator;

public class EmptyIterable<T> implements Iterable<T> {

	private Iterator<T> iterator = new EmptyIterator<T>();

	@Override
	public Iterator<T> iterator() {
		return iterator;
	}

	public String toString() {
		return "{}";
	}

}
