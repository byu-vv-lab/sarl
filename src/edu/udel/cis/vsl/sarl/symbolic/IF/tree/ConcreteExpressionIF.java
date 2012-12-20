package edu.udel.cis.vsl.sarl.symbolic.IF.tree;

/**
 * A concrete number (instance of NumberIF) or boolean (instance of BooleanIF)
 * value.
 * 
 * It has no arguments.
 */
public interface ConcreteExpressionIF extends
		TreeExpressionIF {

	Object value();

}
