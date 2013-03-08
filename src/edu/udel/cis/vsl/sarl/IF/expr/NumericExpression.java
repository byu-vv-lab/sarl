package edu.udel.cis.vsl.sarl.IF.expr;


/**
 * Marker interface for an expression of numeric type (integer or real type).
 * 
 * Note that class of this type should override the isZero and isOne methods in
 * CommonSymbolicExpression, if they extend CommonSymbolicExpression.
 * 
 * @author siegel
 * 
 */
public interface NumericExpression extends SymbolicExpression {

}
