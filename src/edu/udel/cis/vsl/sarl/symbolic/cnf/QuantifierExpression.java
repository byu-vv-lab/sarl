package edu.udel.cis.vsl.sarl.symbolic.cnf;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

public class QuantifierExpression extends SymbolicExpression implements
		BasicExpression {

	public enum Quantifier {
		FORALL, EXISTS
	};

	private SymbolicConstantExpression variable;

	private CnfBooleanExpression predicate;

	private Quantifier quantifier;

	protected QuantifierExpression(Quantifier quantifier,
			SymbolicConstantExpression variable, CnfBooleanExpression predicate) {
		super(predicate.type());
		this.quantifier = quantifier;
		this.variable = variable;
		this.predicate = predicate;
	}

	public SymbolicConstantExpression variable() {
		return variable;
	}

	public CnfBooleanExpression predicate() {
		return predicate;
	}

	public Quantifier quantifier() {
		return quantifier;
	}

	@Override
	protected boolean intrinsicEquals(SymbolicExpression that) {
		return that instanceof QuantifierExpression
				&& variable.equals(((QuantifierExpression) that).variable)
				&& predicate.equals(((QuantifierExpression) that).predicate);
	}

	@Override
	protected int intrinsicHashCode() {
		return QuantifierExpression.class.hashCode() + variable.hashCode()
				+ predicate.hashCode();
	}

	public String toString() {
		return quantifier + " " + variable() + "." + predicate().atomString();

	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return variable;
		case 1:
			return predicate;
		default:
			throw new RuntimeException("numArguments=" + 2 + ", index=" + index);
		}
	}

	public SymbolicKind kind() {
		if (quantifier == Quantifier.EXISTS)
			return SymbolicKind.EXISTS;
		else
			return SymbolicKind.FORALL;
	}

	public int numArguments() {
		return 2;
	}

}
