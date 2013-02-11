package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Arrays;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.object.CommonSymbolicObject;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

/**
 * The root of the symbolic expression hierarchy. Every symbolic expression
 * extends this class.
 * 
 * 
 */
public class CommonSymbolicExpression extends CommonSymbolicObject implements
		SymbolicExpressionIF {

	private SymbolicOperator operator;

	private SymbolicTypeIF type;

	private SymbolicObject[] arguments;

	// Constructors...

	protected CommonSymbolicExpression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] arguments) {
		super(SymbolicObjectKind.EXPRESSION);
		assert operator != null;
		assert type != null;
		assert arguments != null;
		this.operator = operator;
		this.type = type;
		this.arguments = arguments;
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicTypeIF type, SymbolicObject arg0) {
		this(kind, type, new SymbolicObject[] { arg0 });
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1) {
		this(kind, type, new SymbolicObject[] { arg0, arg1 });
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		this(kind, type, new SymbolicObject[] { arg0, arg1, arg2 });
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicTypeIF type, Collection<SymbolicObject> args) {
		this(kind, type, args.toArray(new SymbolicObject[args.size()]));
	}

	/**
	 * Returns the type of this symbolic expression.
	 */
	public SymbolicTypeIF type() {
		return type;
	}

	/**
	 * Know that o has argumentKind SYMBOLIC_EXPRESSION and is not == to this.
	 */
	@Override
	protected boolean intrinsicEquals(SymbolicObject o) {
		CommonSymbolicExpression that = (CommonSymbolicExpression) o;

		return operator == that.operator && type.equals(that.type)
				&& Arrays.equals(arguments, that.arguments);
	}

	@Override
	protected int computeHashCode() {
		int result = type.hashCode() ^ operator().hashCode();
		int numArgs = this.numArguments();

		for (int i = 0; i < numArgs; i++)
			result ^= this.argument(i).hashCode();
		return result;
	}

	@Override
	public SymbolicObject argument(int index) {
		return arguments[index];
	}

	@Override
	public SymbolicOperator operator() {
		return operator;
	}

	@Override
	public int numArguments() {
		return arguments.length;
	}

	private StringBuffer toStringBuffer(SymbolicObject[] objects) {
		StringBuffer buffer = new StringBuffer("{");
		boolean first = true;

		for (SymbolicObject object : objects) {
			if (first)
				first = false;
			else
				buffer.append(",");
			if (object == null)
				buffer.append("null");
			else
				buffer.append(object.toString());
		}
		buffer.append("}");
		return buffer;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(operator.toString());

		buffer.append("[");
		buffer.append(type.toString());
		buffer.append("; ");
		buffer.append(toStringBuffer(arguments));
		buffer.append("]");
		return buffer.toString();
	}

	@Override
	public String atomString() {
		return toString();
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		int numArgs = arguments.length;

		if (!type.isCanonic())
			type = factory.canonic(type);
		for (int i = 0; i < numArgs; i++) {
			SymbolicObject arg = arguments[i];

			if (!arg.isCanonic())
				arguments[i] = factory.canonic(arg);
		}
	}
}
