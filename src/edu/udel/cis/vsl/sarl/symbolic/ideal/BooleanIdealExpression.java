package edu.udel.cis.vsl.sarl.symbolic.ideal;

import edu.udel.cis.vsl.sarl.symbolic.cnf.CnfBooleanExpression;

/**
 * The symbolic expression class in the ideal universe used to represent
 * boolean-valued symbolic expressions. It wraps an instance of
 * CnfBooleanExpression, the canonical form for booleans: Conjunctive Normal
 * Form.
 * 
 * @author siegel
 */
public class BooleanIdealExpression extends IdealExpression {

	protected BooleanIdealExpression(CnfBooleanExpression cnf) {
		super(cnf);
		assert type().isBoolean();
	}

	public CnfBooleanExpression cnf() {
		return (CnfBooleanExpression) expression;
	}
}
