package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;

/**
 * Walks a symbolic expression to collect all free (unbound) symbolic constants
 * occurring anywhere in that expression.
 * 
 * @author siegel
 *
 */
public class ExpressionWalker {

	/**
	 * The result of the search: the set of all symbolic constants found so far.
	 * Initially empty, it grows as the search progresses.
	 */
	private Set<SymbolicConstant> result = new HashSet<>();

	/**
	 * The stack of the bound variables. You need to keep track of this, so that
	 * when you encounter a symbolic constant you can figure out whether it is
	 * bound.
	 */
	private Deque<SymbolicConstant> boundStack = new ArrayDeque<>();

	/**
	 * Constructs new walker and does the walk, filling in <code>result</code>.
	 * 
	 * @param expr
	 *            the non-<code>null</code> symbolic expression that will be
	 *            walked
	 */
	public ExpressionWalker(SymbolicExpression expr) {
		walkExpression(expr);
	}

	/**
	 * Walks over the elements of a collection, adding free symbolic constants
	 * found to <code>result</code>.
	 * 
	 * @param collection
	 *            a non-<code>null</code> symbolic collection
	 */
	private void walkCollection(SymbolicCollection<?> collection) {
		for (SymbolicExpression expr : collection)
			walkExpression(expr);
	}

	/**
	 * Walks over a type. A type can contain expressions and therefore symbolic
	 * constants. Adds free symbolic constants discovered to <code>result</code>
	 * .
	 * 
	 * @param type
	 *            a non-<code>null</code> symbolic type
	 */
	private void walkType(SymbolicType type) {
		if (type == null)
			return;

		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
		case CHAR:
			return;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();

			if (arrayType.isComplete()) {
				NumericExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();

				walkExpression(extent);
			}
			walkType(elementType);
			return;
		}
		case FUNCTION: {
			SymbolicFunctionType functionType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicType output = functionType.outputType();

			walkTypeSequence(inputs);
			walkType(output);
			return;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence fields = tupleType.sequence();

			walkTypeSequence(fields);
			return;
		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence members = unionType.sequence();

			walkTypeSequence(members);
			return;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * Walks a type sequence, adding found free symbolic constants to
	 * <code>result</code>.
	 * 
	 * @param sequence
	 *            a non-<code>null</code> type sequence
	 */
	private void walkTypeSequence(SymbolicTypeSequence sequence) {
		for (SymbolicType t : sequence) {
			walkType(t);
		}
	}

	/**
	 * Walks a symbolic expression looking for free symbolic constants. If
	 * <code>expression</code> is a symbolic constant, looks in the
	 * <code>boundStack</code> to see if it is bound; if not, it is added to
	 * <code>result</code>. If <code>expression</code> is a binding expression
	 * (exists, forall, or lambda): the bound variable is pushed on to the
	 * <code>boundStack</code>, the body of the expression is walked, and the
	 * <code>boundStack</code> is popped. Otherwise, the arguments are walked.
	 * 
	 * @parameter expression a non-<code>null</code> symbolic expression
	 */
	private void walkExpression(SymbolicExpression expression) {
		SymbolicOperator operator = expression.operator();

		walkType(expression.type());
		// do not add bound variables to the result...
		if (operator == SymbolicOperator.SYMBOLIC_CONSTANT) {
			if (!boundStack.contains(expression))
				result.add((SymbolicConstant) expression);
			return;
		}
		if (operator == SymbolicOperator.EXISTS
				|| operator == SymbolicOperator.FORALL
				|| operator == SymbolicOperator.LAMBDA) {
			SymbolicConstant arg0 = (SymbolicConstant) expression.argument(0);
			SymbolicExpression arg1 = (SymbolicExpression) expression
					.argument(1);

			boundStack.push(arg0);
			walkExpression(arg1);
			boundStack.pop();
			return;
		} else {
			int numArgs = expression.numArguments();

			for (int i = 0; i < numArgs; i++) {
				SymbolicObject arg = expression.argument(i);

				walkObject(arg);
			}
		}
	}

	/**
	 * Walks a symbolic object, looking for free symbolic constants. For
	 * primitive objects, nothing to do. Otherwise, delegates to the appropriate
	 * method.
	 * 
	 * @param obj
	 *            a non-<code>null</code> symbolic object
	 */
	private void walkObject(SymbolicObject obj) {
		SymbolicObjectKind kind = obj.symbolicObjectKind();

		switch (kind) {
		case BOOLEAN:
		case INT:
		case NUMBER:
		case STRING:
		case CHAR:
			// no variables contained therein
			return;
		case EXPRESSION:
			walkExpression((SymbolicExpression) obj);
			return;
		case EXPRESSION_COLLECTION:
			walkCollection((SymbolicCollection<?>) obj);
			return;
		case TYPE:
			walkType((SymbolicType) obj);
			return;
		case TYPE_SEQUENCE:
			walkTypeSequence((SymbolicTypeSequence) obj);
			return;
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * Gets the result: the set of free symbolic constants occurring in the
	 * original expression that was provided at creation.
	 * 
	 * @return the result
	 */
	public Set<SymbolicConstant> getResult() {
		return result;
	}
}
