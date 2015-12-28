package edu.udel.cis.vsl.sarl.util;

public class FastNode<T> {

	private T data;

	private FastNode<T> prev;

	private FastNode<T> next;

	public FastNode(T data) {
		this.data = data;
		this.prev = null;
		this.next = null;
	}

	public FastNode(T data, FastNode<T> prev, FastNode<T> next) {
		this.data = data;
		this.prev = prev;
		this.next = next;
	}

	public T getData() {
		return data;
	}

	public FastNode<T> getPrev() {
		return prev;
	}

	public void setPrev(FastNode<T> prev) {
		this.prev = prev;
	}

	public FastNode<T> getNext() {
		return next;
	}

	public void setNext(FastNode<T> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return data.toString();
	}

}
