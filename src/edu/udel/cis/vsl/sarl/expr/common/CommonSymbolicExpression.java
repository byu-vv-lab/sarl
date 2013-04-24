/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Arrays;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonSymbolicObject;

/**
 * The root of the symbolic expression hierarchy. Every symbolic expression
 * extends this class.
 * 
 * 
 */
public class CommonSymbolicExpression extends CommonSymbolicObject implements
		SymbolicExpression {

	private SymbolicOperator operator;

	private SymbolicType type;

	private SymbolicObject[] arguments;

	// Constructors...

	protected CommonSymbolicExpression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] arguments) {
		super(SymbolicObjectKind.EXPRESSION);
		assert operator != null;
		assert operator == SymbolicOperator.NULL || type != null;
		assert arguments != null;
		this.operator = operator;
		this.type = type;
		this.arguments = arguments;
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicType type, SymbolicObject arg0) {
		this(kind, type, new SymbolicObject[] { arg0 });
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1) {
		this(kind, type, new SymbolicObject[] { arg0, arg1 });
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		this(kind, type, new SymbolicObject[] { arg0, arg1, arg2 });
	}

	protected CommonSymbolicExpression(SymbolicOperator kind,
			SymbolicType type, Collection<SymbolicObject> args) {
		this(kind, type, args.toArray(new SymbolicObject[args.size()]));
	}

	/**
	 * Returns the type of this symbolic expression.
	 */
	public SymbolicType type() {
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
		int result = type == null ? operator.hashCode() : type.hashCode()
				^ operator().hashCode();
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

	private StringBuffer toStringBufferLong(SymbolicObject[] objects) {
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
				buffer.append(object.toStringBufferLong());
		}
		buffer.append("}");
		return buffer;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer buffer = new StringBuffer(operator.toString());

		buffer.append("[");
		buffer.append(type.toString());
		buffer.append("; ");
		buffer.append(toStringBufferLong(arguments));
		buffer.append("]");
		return buffer;
	}

	private void accumulate(StringBuffer buffer, String opString,
			SymbolicCollection<?> operands, boolean atomizeArgs) {
		boolean first = true;

		// opString = " " + opString + " ";
		for (SymbolicExpression arg : operands) {
			if (first)
				first = false;
			else
				buffer.append(opString);
			buffer.append(arg.toStringBuffer(atomizeArgs));
		}
	}

	private void processBinary(StringBuffer buffer, String opString,
			SymbolicObject arg0, SymbolicObject arg1, boolean atomizeArgs) {
		buffer.append(arg0.toStringBuffer(atomizeArgs));
		buffer.append(opString);
		buffer.append(arg1.toStringBuffer(atomizeArgs));
	}

	private void processFlexibleBinary(StringBuffer buffer, String opString,
			boolean atomizeArgs, boolean atomizeResult) {
		if (arguments.length == 1)
			accumulate(buffer, opString, (SymbolicCollection<?>) arguments[0],
					atomizeArgs);
		else
			processBinary(buffer, opString, arguments[0], arguments[1],
					atomizeArgs);
		if (atomizeResult) {
			buffer.insert(0, '(');
			buffer.append(')');
		}
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();

		switch (operator) {
		case ADD:
			processFlexibleBinary(result, "+", false, atomize);
			return result;
		case AND:
			processFlexibleBinary(result, " && ", false, atomize);
			return result;
		case APPLY: {
			result.append(arguments[0].toStringBuffer(true));
			result.append("(");
			accumulate(result, ",", (SymbolicCollection<?>) arguments[1], false);
			result.append(")");
			return result;
		}
		case ARRAY_LAMBDA:
			return toStringBufferLong();
		case ARRAY_READ:
			result.append(arguments[0].toStringBuffer(true));
			result.append("[");
			result.append(arguments[1].toStringBuffer(false));
			result.append("]");
			return result;
		case ARRAY_WRITE:
			result.append(arguments[0].toStringBuffer(true));
			result.append("[");
			result.append(arguments[1].toStringBuffer(false));
			result.append(":=");
			result.append(arguments[2].toStringBuffer(false));
			result.append("]");
			return result;
		case CAST:
			result.append('(');
			result.append(type.toStringBuffer(false));
			result.append(')');
			result.append(arguments[0].toStringBuffer(true));
			return result;
		case CONCRETE: {
			if (!type.isNumeric() && !type.isBoolean()) {
				result.append('(');
				result.append(type.toStringBuffer(false));
				result.append(')');
			}
			result.append(arguments[0].toStringBuffer(false));
			if (type.isHerbrand())
				result.append('h');
			if (atomize)
				atomize(result);
			return result;
		}
		case COND:
			result.append(arguments[0].toStringBuffer(true));
			result.append(" ? ");
			result.append(arguments[1].toStringBuffer(true));
			result.append(" : ");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case DENSE_ARRAY_WRITE: {
			int count = 0;

			result.append(arguments[0].toStringBuffer(true));
			result.append("[");
			for (SymbolicExpression value : (SymbolicSequence<?>) arguments[1]) {
				if (!value.isNull()) {
					if (count > 0)
						result.append(", ");
					result.append(count + ":=");
					result.append(value.toStringBuffer(false));
				}
				count++;
			}
			result.append("]");
			return result;
		}
		case DIVIDE:
			result.append(arguments[0].toStringBuffer(true));
			result.append("/");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case EQUALS:
			result.append(arguments[0].toStringBuffer(false));
			result.append(" == ");
			result.append(arguments[1].toStringBuffer(false));
			if (atomize)
				atomize(result);
			return result;
		case EXISTS:
			result.append("exists ");
			result.append(arguments[0].toStringBuffer(false));
			result.append(" : ");
			result.append(((SymbolicExpression) arguments[0]).type()
					.toStringBuffer(false));
			result.append(" . ");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case FORALL:
			result.append("forall ");
			result.append(arguments[0].toStringBuffer(false));
			result.append(" : ");
			result.append(((SymbolicExpression) arguments[0]).type()
					.toStringBuffer(false));
			result.append(" . ");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case INT_DIVIDE: {
			result.append(arguments[0].toStringBuffer(true));
			result.append("\u00F7");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		}
		case LAMBDA:
			result.append("lambda ");
			result.append(arguments[0].toStringBuffer(false));
			result.append(" : ");
			result.append(((SymbolicExpression) arguments[0]).type()
					.toStringBuffer(false));
			result.append(" . ");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case LENGTH:
			result.append("length(");
			result.append(arguments[0].toStringBuffer(false));
			result.append(")");
			return result;
		case LESS_THAN:
			result.append(arguments[0].toStringBuffer(false));
			result.append(" < ");
			result.append(arguments[1].toStringBuffer(false));
			if (atomize)
				atomize(result);
			return result;
		case LESS_THAN_EQUALS:
			result.append(arguments[0].toStringBuffer(false));
			result.append(" <= ");
			result.append(arguments[1].toStringBuffer(false));
			if (atomize)
				atomize(result);
			return result;
		case MODULO:
			result.append(arguments[0].toStringBuffer(true));
			result.append("%");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case MULTIPLY:
			result.append(arguments[0].toStringBuffer(true));
			result.append("*");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case NEGATIVE:
			result.append("-");
			result.append(arguments[0].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case NEQ:
			result.append(arguments[0].toStringBuffer(false));
			result.append(" != ");
			result.append(arguments[1].toStringBuffer(false));
			if (atomize)
				atomize(result);
			return result;
		case NOT:
			result.append("!");
			result.append(arguments[0].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case NULL:
			result.append("NULL");
			return result;
		case OR:
			processFlexibleBinary(result, " || ", false, atomize);
			if (atomize)
				atomize(result);
			return result;
		case POWER:
			result.append(arguments[0].toStringBuffer(true));
			result.append("^");
			result.append(arguments[1].toStringBuffer(true));
			if (atomize)
				atomize(result);
			return result;
		case SUBTRACT:
			processBinary(result, " - ", arguments[0], arguments[1], true);
			if (atomize)
				atomize(result);
			return result;
		case SYMBOLIC_CONSTANT:
			result.append(arguments[0].toStringBuffer(false));
			return result;
		case TUPLE_READ:
			result.append(arguments[0].toStringBuffer(true));
			result.append(".");
			result.append(arguments[1].toStringBuffer(false));
			if (atomize)
				atomize(result);
			return result;
		case TUPLE_WRITE:
			result.append(arguments[0].toStringBuffer(true));
			result.append("[.");
			result.append(arguments[1].toStringBuffer(false));
			result.append(":=");
			result.append(arguments[2].toStringBuffer(false));
			result.append("]");
			return result;
		case UNION_EXTRACT:
			result.append("extract(");
			result.append(arguments[0].toStringBuffer(false));
			result.append(",");
			result.append(arguments[1].toStringBuffer(false));
			result.append(")");
			return result;
		case UNION_INJECT:
			result.append("inject(");
			result.append(arguments[0].toStringBuffer(false));
			result.append(",");
			result.append(arguments[1].toStringBuffer(false));
			result.append(")");
			return result;
		case UNION_TEST:
			result.append("test(");
			result.append(arguments[0].toStringBuffer(false));
			result.append(",");
			result.append(arguments[1].toStringBuffer(false));
			result.append(")");
			return result;
		default:
			return toStringBufferLong();
		}
	}

	@Override
	public String atomString() {
		return toString();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		int numArgs = arguments.length;

		if (!type.isCanonic())
			type = factory.canonic(type);
		for (int i = 0; i < numArgs; i++) {
			SymbolicObject arg = arguments[i];

			if (!arg.isCanonic())
				arguments[i] = factory.canonic(arg);
		}
	}

	@Override
	public boolean isNull() {
		return operator == SymbolicOperator.NULL;
	}

	@Override
	public boolean isFalse() {
		return operator == SymbolicOperator.CONCRETE
				&& arguments[0] instanceof BooleanObject
				&& !((BooleanObject) arguments[0]).getBoolean();
	}

	@Override
	public boolean isTrue() {
		return operator == SymbolicOperator.CONCRETE
				&& arguments[0] instanceof BooleanObject
				&& ((BooleanObject) arguments[0]).getBoolean();
	}

	/**
	 * Returns false, since this will be overridden in NumericExpression.
	 */
	@Override
	public boolean isZero() {
		return false;
	}

	/**
	 * Returns false, since this will be overridden in NumericExpression.
	 */
	@Override
	public boolean isOne() {
		return false;
	}

	@Override
	public boolean isNumeric() {
		return this instanceof NumericExpression;
	}
}