package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/**
 * The identity reference I, which is characterized by the property that
 * dereference(value, I)==value for any symbolic expression value.
 * 
 * @author siegel
 * 
 */
public class CommonIdentityReference extends CommonReferenceExpression {

	/**
	 * Constructs the identity reference.
	 * 
	 * @param referenceType
	 *            the symbolic reference type
	 * @param oneSequence
	 *            the singleton sequence whose sole element is the concrete
	 *            integer 1
	 */
	CommonIdentityReference(SymbolicType referenceType,
			SymbolicSequence<NumericExpression> oneSequence) {
		super(referenceType, oneSequence);
	}

	/**
	 * Returns true, as this is the identity reference, overriding the default
	 * false.
	 */
	@Override
	public boolean isIdentityReference() {
		return true;
	}

	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.IDENTITY;
	}

}
