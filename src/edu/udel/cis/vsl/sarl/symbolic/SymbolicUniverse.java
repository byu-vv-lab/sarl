package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.BooleanConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF.SymbolicKind;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

/**
 * This class provides partial implementation of the SymbolicUniverseIF
 * interface. Generic implementations of methods "make" and "canonicalize" are
 * given.
 * 
 * @author siegel
 */
public abstract class SymbolicUniverse implements SymbolicUniverseIF {

	/**
	 * For exists and forall, must provide an instance of
	 * SymbolicConstantExpressionIF as arg0. Cannot be applied to make concrete
	 * expressions or SymbolicConstantExpressionIF. There are separate methods
	 * for those.
	 */
	@Override
	public SymbolicExpressionIF make(SymbolicKind operator,
			SymbolicTypeIF type, SymbolicExpressionIF[] arguments) {
		int numArgs = arguments.length;

		switch (operator) {
		case ADD: {
			if (numArgs == 0) {
				return (type.isInteger() ? zeroInt() : zeroReal());
			} else {
				SymbolicExpressionIF result = arguments[0];

				for (int i = 1; i < numArgs; i++)
					result = add(result, arguments[i]);
				return result;
			}
		}
		case AND: {
			if (numArgs == 0) {
				return concreteExpression(true);
			} else {
				SymbolicExpressionIF result = arguments[0];

				for (int i = 1; i < numArgs; i++)
					result = and(result, arguments[i]);
				return result;
			}
		}
		case APPLY: {
			SymbolicExpressionIF[] idealArgs = new SymbolicExpressionIF[numArgs - 1];

			for (int i = 1; i < numArgs; i++)
				idealArgs[i - 1] = arguments[i];
			return apply(arguments[0], idealArgs);
		}
		case ARRAY_LAMBDA:
			return arrayLambda((SymbolicCompleteArrayTypeIF) type, arguments[0]);
		case ARRAY_READ:
			return arrayRead(arguments[0], arguments[1]);
		case ARRAY_WRITE:
			return arrayWrite(arguments[0], arguments[1], arguments[2]);
		case CAST:
			return castToReal(arguments[0]);
		case CONCRETE_TUPLE:
			return tupleExpression((SymbolicTupleTypeIF) type, arguments);
		case COND:
			return cond(arguments[0], arguments[1], arguments[2]);
		case DIVIDE:
			return divide(arguments[0], arguments[1]);
		case EQUALS:
			return equals(arguments[0], arguments[1]);
		case EXISTS:
			return exists(
					((SymbolicConstantExpressionIF) arguments[0])
							.symbolicConstant(),
					arguments[1]);
		case FORALL:
			return forall(
					((SymbolicConstantExpressionIF) arguments[0])
							.symbolicConstant(),
					arguments[1]);
		case INT_DIVIDE:
			return divide(arguments[0], arguments[1]);
		case LAMBDA:
			return lambda(
					((SymbolicConstantExpressionIF) arguments[0])
							.symbolicConstant(),
					arguments[1]);
		case LENGTH:
			return length(arguments[0]);
		case LESS_THAN:
			return lessThan(arguments[0], arguments[1]);
		case LESS_THAN_EQUALS:
			return lessThanEquals(arguments[0], arguments[1]);
		case MODULO:
			return modulo(arguments[0], arguments[1]);
		case MULTIPLY: {
			if (numArgs == 0) {
				return (type.isInteger() ? oneInt() : oneReal());
			} else {
				SymbolicExpressionIF result = arguments[0];

				for (int i = 1; i < numArgs; i++)
					result = multiply(result, arguments[i]);
				return result;
			}
		}
		case NEGATIVE:
			return minus(arguments[0]);
		case NEQ:
			return neq(arguments[0], arguments[1]);
		case NOT:
			return not(arguments[0]);
		case OR: {
			if (numArgs == 0) {
				return concreteExpression(false);
			} else {
				SymbolicExpressionIF result = arguments[0];

				for (int i = 1; i < numArgs; i++)
					result = or(result, arguments[i]);
				return result;
			}
		}
		case POWER:
			return power(arguments[0], arguments[1]);
		case SUBTRACT:
			return subtract(arguments[0], arguments[1]);
		case TUPLE_READ:
			return tupleRead(arguments[0], arguments[1]);
		case TUPLE_WRITE:
			return tupleWrite(arguments[0], arguments[1], arguments[2]);
		case CONCRETE_BOOLEAN:
		case CONCRETE_NUMBER:
		case SYMBOLIC_CONSTANT:
			throw new IllegalArgumentException("Cannot apply " + operator);

		default:
			throw new IllegalArgumentException("Unknown expression kind: "
					+ operator);
		}
	}

	public SymbolicExpressionIF canonicalize(TreeExpressionIF standard) {
		SymbolicKind kind = standard.kind();
		SymbolicTypeIF type = standard.type();
		int numArgs = standard.numArguments();

		switch (kind) {
		case CONCRETE_BOOLEAN:
			return concreteExpression(((BooleanConcreteExpressionIF) standard)
					.value());
		case CONCRETE_NUMBER:
			return concreteExpression(((NumericConcreteExpressionIF) standard)
					.value());
		case SYMBOLIC_CONSTANT:
			return symbolicConstantExpression(((SymbolicConstantExpressionIF) standard)
					.symbolicConstant());
		default: {
			SymbolicExpressionIF[] canonicalizedArgs = new SymbolicExpressionIF[numArgs];
			int start = 0;

			// for quantifier expressions, arg0 is a
			// SymbolicConstantExpressionIF,
			// but canonicalizing it changes it to something like an Ideal
			// expression
			if (kind == SymbolicKind.FORALL || kind == SymbolicKind.EXISTS
					|| kind == SymbolicKind.LAMBDA) {
				canonicalizedArgs[0] = standard.argument(0);
				start = 1;
			}
			for (int i = start; i < numArgs; i++)
				canonicalizedArgs[i] = canonicalize(standard.argument(i));
			return make(kind, type, canonicalizedArgs);
		}
		}
	}

}
