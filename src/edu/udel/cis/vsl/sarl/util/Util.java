package edu.udel.cis.vsl.sarl.util;

public class Util {

	private static Iterable<?> emptyIterable = new EmptyIterable<Object>();

	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> emptyIterable() {
		return (Iterable<T>) emptyIterable;
	}

}
