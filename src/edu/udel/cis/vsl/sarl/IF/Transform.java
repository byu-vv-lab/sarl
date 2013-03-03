package edu.udel.cis.vsl.sarl.IF;

public interface Transform<S, T> {

	T apply(S x);

}
