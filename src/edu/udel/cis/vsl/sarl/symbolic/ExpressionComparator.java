package edu.udel.cis.vsl.sarl.symbolic;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.object.ObjectComparator;

/**
 * Comparator of symbolic expressions.
 * 
 * Creation order: first create ObjectComparator oc. Then create
 * NumericComparator nc using oc. Then create ExpressionComparator ec using oc
 * and nc.
 * 
 * @author siegel
 * 
 */
public class ExpressionComparator implements Comparator<SymbolicExpressionIF> {

	private ObjectComparator objectComparator;

	private Comparator<SymbolicTypeIF> typeComparator;

	private NumericComparator numericComparator;

	public ExpressionComparator(NumericComparator numericComparator) {
		this.numericComparator = numericComparator;
	}

	public void setObjectComparator(ObjectComparator c) {
		objectComparator = c;
		numericComparator.setObjectComparator(c);
	}

	public void setTypeComparator(Comparator<SymbolicTypeIF> c) {
		typeComparator = c;
	}

	/**
	 * Numerics first, then everything else. For everything else: first compare
	 * types, then operator, then number of arguments, the compare arguments.
	 * 
	 */
	@Override
	public int compare(SymbolicExpressionIF o1, SymbolicExpressionIF o2) {
		SymbolicTypeIF t1 = o1.type();
		SymbolicTypeIF t2 = o2.type();

		if (t1.isNumeric()) {
			if (t2.isNumeric())
				return numericComparator.compare((NumericExpression) o1,
						(NumericExpression) o2);
			else
				return -1;
		} else if (t2.isNumeric())
			return 1;
		else { // neither is numeric
			int result = typeComparator.compare(t1, t2);

			if (result != 0)
				return result;
			result = o1.operator().compareTo(o2.operator());
			if (result != 0)
				return result;
			else {
				int numArgs = o1.numArguments();

				result = numArgs - o2.numArguments();
				if (result != 0)
					return result;
				for (int i = 0; i < numArgs; i++) {
					result = objectComparator.compare(o1.argument(i),
							o2.argument(i));
					if (result != 0)
						return result;
				}
				return 0;
			}
		}
	}
}
