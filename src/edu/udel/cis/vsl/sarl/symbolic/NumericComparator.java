package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface NumericComparator extends Comparator<NumericExpression> {

	void setObjectComparator(Comparator<SymbolicObject> comparator);
}
