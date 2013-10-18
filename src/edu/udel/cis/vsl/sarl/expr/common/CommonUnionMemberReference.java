package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.UnionMemberReference;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/**
 * Implementation of a non-trivial reference to a UnionMember
 */
public class CommonUnionMemberReference extends CommonNTReference implements
		UnionMemberReference {

	/**
	 * The fieldIndex duplicated the information in one of the arguments, but
	 * there is no obvious way to translate from a NumberObject to an IntObject
	 * so cache it here.
	 */
	private IntObject memberIndex;

	
	/**
	 * Constructor asserts that parentIndexSequnce is a valid IntegerNumber
	 * 
	 * @param referenceType
	 * @param unionMemberReferenceFunction
	 * @param parentIndexSequence
	 * @param memberIndex
	 */
	public CommonUnionMemberReference(SymbolicType referenceType,
			SymbolicConstant unionMemberReferenceFunction,
			SymbolicSequence<SymbolicExpression> parentIndexSequence,
			IntObject memberIndex) {
		super(referenceType, unionMemberReferenceFunction, parentIndexSequence);
		assert parentIndexSequence.get(1).operator() == SymbolicOperator.CONCRETE
				&& parentIndexSequence.get(1).argument(0) instanceof NumberObject
				&& ((IntegerNumber) ((NumberObject) parentIndexSequence.get(1)
						.argument(0)).getNumber()).intValue() == memberIndex
						.getInt();
		this.memberIndex = memberIndex;
	}

	/**
	 * @return memberIndex
	 */
	@Override
	public IntObject getIndex() {
		return memberIndex;
	}

	/**
	 * @return true
	 */
	@Override
	public boolean isUnionMemberReference() {
		return true;
	}

	/**
	 *@return ReferenceKind.UNION_MEMBER
	 */
	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.UNION_MEMBER;
	}
}
