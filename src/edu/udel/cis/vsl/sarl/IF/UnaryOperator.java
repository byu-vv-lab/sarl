package edu.udel.cis.vsl.sarl.IF;

/**
 * A UnaryOperator on a type T is an object which provides a method "apply" that
 * takes an element of T and returns an element of T. It is a Transform<T,T>.
 * 
 * @author siegel
 * 
 */
public interface UnaryOperator<T> extends Transform<T, T> {
}
