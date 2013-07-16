package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

public abstract class CommonReferenceExpression extends
		CommonSymbolicExpression implements ReferenceExpression {

	/**
	 * Constructs a null reference or identity reference expression. The
	 * codeSingleton argument should be a sequence of length 1 whose sole
	 * element in an integer expression.
	 * 
	 * @param referenceType
	 *            the symbolic reference type
	 * @param codeSingleton
	 *            sequence of length 1 containing concrete integer 0 for null
	 *            reference or concrete integer 1 for identity reference
	 */
	public CommonReferenceExpression(SymbolicType referenceType,
			SymbolicSequence<NumericExpression> codeSingleton) {
		super(SymbolicOperator.CONCRETE, referenceType, codeSingleton);
	}

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
	 * @param referenceType
	 *            the symbolic reference type
	 * @param function
	 *            one of the uninterpreted functions
	 * @param parentIndexSequence
	 *            sequence of length 2 in which first component is the parent
	 *            reference and second is as specified above
	 */
	public CommonReferenceExpression(SymbolicType referenceType,
			SymbolicConstant function,
			SymbolicSequence<SymbolicExpression> parentIndexSequence) {
		super(SymbolicOperator.APPLY, referenceType, function,
				parentIndexSequence);
	}

	@Override
	public boolean isNullReference() {
		return false;
	}

	@Override
	public boolean isIdentityReference() {
		return false;
	}

	@Override
	public boolean isArrayElementReference() {
		return false;
	}

	@Override
	public boolean isTupleComponentReference() {
		return false;
	}

	@Override
	public boolean isUnionMemberReference() {
		return false;
	}

	@Override
	public boolean isOffsetReference() {
		return false;
	}

}
