package edu.udel.cis.vsl.sarl.IF.expr;

public interface ReferenceExpression extends SymbolicExpression {

	public enum ReferenceKind {
		NULL, IDENTITY, ARRAY_ELEMENT, TUPLE_COMPONENT, UNION_MEMBER, OFFSET
	}

	ReferenceKind referenceKind();

	/** Is this the null reference? */
	boolean isNullReference();

	/** Is this the identity reference? */
	boolean isIdentityReference();

	/** Is this an array element reference? */
	boolean isArrayElementReference();

	/** Is this a tuple component reference? */
	boolean isTupleComponentReference();

	/** Is this a union member reference? */
	boolean isUnionMemberReference();

	/** Is this an "offset reference" ? */
	boolean isOffsetReference();

}
