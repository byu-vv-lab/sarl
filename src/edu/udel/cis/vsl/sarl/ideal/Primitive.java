package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * Wraps a numeric symbolic expression that does not fall into one of the other
 * categories in this package: symbolic constant, array read, tuple read,
 * function application.
 * 
 * @author siegel
 * 
 */
public class Primitive extends CommonSymbolicExpression {

	protected Primitive(SymbolicExpressionIF contents) {
		super(SymbolicOperator.CHOICE, contents.type(), contents);
	}

}
