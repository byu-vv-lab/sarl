package edu.udel.cis.vsl.sarl.expr.cnf;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;

public class CnfBooleanExpression extends CommonSymbolicExpression implements
		BooleanExpression {

	protected CnfBooleanExpression(SymbolicOperator kind, SymbolicType type,
			Collection<SymbolicObject> args) {
		super(kind, type, args);
		assert type.isBoolean();
	}

	protected CnfBooleanExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject[] args) {
		super(kind, type, args);
		assert type.isBoolean();
	}

	protected CnfBooleanExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg) {
		super(kind, type, arg);
		assert type.isBoolean();
	}

	protected CnfBooleanExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(kind, type, arg0, arg1);
		assert type.isBoolean();
	}

	protected CnfBooleanExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(kind, type, arg0, arg1, arg2);
		assert type.isBoolean();
	}

	@Override
	public BooleanExpression booleanArg(int i) {
		return (BooleanExpression) argument(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SymbolicCollection<BooleanExpression> booleanCollectionArg(int i) {
		return (SymbolicCollection<BooleanExpression>) argument(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SymbolicSet<BooleanExpression> booleanSetArg(int i) {
		return (SymbolicSet<BooleanExpression>) argument(i);
	}

}
