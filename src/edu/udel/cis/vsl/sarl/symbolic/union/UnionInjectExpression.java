package edu.udel.cis.vsl.sarl.symbolic.union;

import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class UnionInjectExpression extends CommonSymbolicExpression implements
		TreeExpressionIF {

	private final static int classCode = UnionInjectExpression.class.hashCode();

	private int memberIndex;

	private TreeExpressionIF object;

	public UnionInjectExpression(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		super(unionType);
		// assert object.type().equals(
		// unionType.memberType(((IntegerNumberIF) memberIndex.value())
		// .intValue()));
		this.object = object;
		this.memberIndex = memberIndex;
	}

	@Override
	public String atomString() {
		return "UnionInject(" + type().name() + ", " + object + ")";
	}

	@Override
	protected int intrinsicHashCode() {
		return classCode + memberIndex + object.hashCode();
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression object) {
		if (object instanceof UnionInjectExpression) {
			UnionInjectExpression that = (UnionInjectExpression) object;

			return object.equals(that.object)
					&& memberIndex == that.memberIndex;
		}
		return false;
	}

	@Override
	public SymbolicOperator operator() {
		return SymbolicOperator.UNION_INJECT;
	}

	@Override
	public TreeExpressionIF argument(int index) {
		if (index == 0)
			// TODO
			return null;
		else if (index == 1)
			return object;
		else
			throw new IllegalArgumentException("numArguments=" + 2 + ", index="
					+ index);
	}

	@Override
	public int numArguments() {
		return 2;
	}

	@Override
	public SymbolicUnionTypeIF type() {
		return (SymbolicUnionTypeIF) super.type();
	}

}
