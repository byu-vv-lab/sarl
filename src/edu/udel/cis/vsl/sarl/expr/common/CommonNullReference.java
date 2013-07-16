package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/**
 * The "null reference" is a useless reference value not equal to any of the
 * other reference values and which cannot be dereferenced. It does have
 * reference type, and can be used in places where an expression of reference
 * type is needed, but no valid reference exists.
 * 
 * @author siegel
 * 
 */
public class CommonNullReference extends CommonReferenceExpression {

	/**
	 * Constructs the null reference.
	 * 
	 * @param referenceType
	 *            the symbolic reference type
	 * @param zeroSequence
	 *            the singleton sequence whose sole element is the conrete
	 *            integer 0
	 */
	public CommonNullReference(SymbolicType referenceType,
			SymbolicSequence<NumericExpression> zeroSequence) {
		super(referenceType, zeroSequence);
	}

	/**
	 * Returns true, overriding the default false, since this is the null
	 * reference.
	 */
	@Override
	public boolean isNullReference() {
		return true;
	}

	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.NULL;
	}

}
