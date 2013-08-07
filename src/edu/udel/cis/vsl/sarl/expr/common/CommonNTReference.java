package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.NTReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/**
 * A standard implementation of NTReferenceExpression, used to represent a
 * non-trivial reference. A non-trivial reference is characterized by the
 * property that is has a non-null parent reference.
 * 
 * @author siegel
 * 
 */
public abstract class CommonNTReference extends CommonReferenceExpression
		implements NTReferenceExpression {

	/**
	 * Constructs a non-trivial reference expression. The cases are:
	 * <ul>
	 * <li>array element reference: function is the
	 * arrayElementReferenceFunction, parentIndexSequence is sequence of length
	 * 2 in which element 0 is the parent reference (the reference to the array)
	 * and element 1 is the index of the array element, a numeric symbolic
	 * expression of integer type.</li>
	 * <li>tuple component reference: function is the
	 * tupleComponentReferenceFunction, parentIndexSequence is sequence of
	 * length 2 in which element 0 is the parent reference (the reference to the
	 * tuple) and element 1 is the field index, a concrete numeric symbolic
	 * expression of integer type.</li>
	 * <li>union member reference: function is the unionMemberReferenceFunction,
	 * parentIndexSequence is sequence of length 2 in which element 0 is the
	 * parent reference (the reference to the expression of union type) and
	 * element 1 is the member index, a concrete numeric symbolic expression of
	 * intger type.</li>
	 * <li>offset reference: just like array element reference, but function is
	 * offsetReferenceFunction</li>
	 * </ul>
	 * 
	 * The symbolic expression has type referenceType, operator APPLY, arg0 is
	 * the function, and arg1 is the parentIndexSequence.
	 * 
	 * @param referenceType
	 *            the symbolic reference type
	 * @param function
	 *            one of the uninterpreted functions
	 * @param parentIndexSequence
	 *            sequence of length 2 in which first component is the parent
	 *            reference and second is as specified above
	 */
	public CommonNTReference(SymbolicType referenceType,
			SymbolicConstant function,
			SymbolicSequence<SymbolicExpression> parentIndexSequence) {
		super(referenceType, function, parentIndexSequence);
		assert parentIndexSequence.get(0) instanceof ReferenceExpression;
		assert parentIndexSequence.get(1).type().isInteger();
	}

	@Override
	public ReferenceExpression getParent() {
		return (ReferenceExpression) ((SymbolicSequence<?>) this.argument(1))
				.get(0);
	}

	protected NumericExpression getIndexExpression() {
		return (NumericExpression) ((SymbolicSequence<?>) this.argument(1))
				.get(1);
	}

}
