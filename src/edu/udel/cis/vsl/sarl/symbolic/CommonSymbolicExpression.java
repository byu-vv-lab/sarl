package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Arrays;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;

/**
 * The root of the symbolic expression hierarchy. Every symbolic expression
 * extends this class.
 * 
 * This class is implemented using the Flyweight Pattern. Because of this, there
 * is no need to override the equals or hashCode methods of Object.
 * 
 */
public abstract class CommonSymbolicExpression extends CommonSymbolicObject
		implements SymbolicExpressionIF {

	private SymbolicOperator operator;

	private SymbolicTypeIF type;

	private SymbolicObject[] arguments;

	// Constructors...

	protected CommonSymbolicExpression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] arguments) {
		super(SymbolicObjectKind.SYMBOLIC_EXPRESSION);
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
	protected int compareLocal(SymbolicObject o) {
		CommonSymbolicExpression that = (CommonSymbolicExpression) o;
		int result = operator.compareTo(that.operator);

		if (result != 0)
			return result;
		// need types to implement comparable.
		result = type.compareTo(that.type);
		if (result != 0)
			return result;
		{
			int numArgs0 = numArguments();

			result = numArgs0 - that.numArguments();
			if (result != 0)
				return result;
			for (int i = 0; i < numArgs0; i++) {
				result = arguments[i].compareTo(that.arguments[i]);
				if (result != 0)
					return result;
			}
			return 0;
		}
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
}
