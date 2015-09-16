package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

class SimpleEntry implements Entry<SymbolicExpression, SymbolicExpression> {
	SymbolicExpression key;
	SymbolicExpression value;

	SimpleEntry(SymbolicExpression key, SymbolicExpression value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public SymbolicExpression getKey() {
		return key;
	}

	@Override
	public SymbolicExpression getValue() {
		return value;
	}

	@Override
	public SymbolicExpression setValue(SymbolicExpression value) {
		// check committed?
		SymbolicExpression oldValue = this.value;

		this.value = value;
		return oldValue;
	}
}