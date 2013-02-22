package edu.udel.cis.vsl.sarl.IF;

/**
 * Interface for a binary operation on symbolic expressions.
 * 
 * TODO: combine with Multiplier
 * 
 * @author siegel
 * 
 */
public interface BinaryOperator<T> {

	T apply(T arg0, T arg1);

}
