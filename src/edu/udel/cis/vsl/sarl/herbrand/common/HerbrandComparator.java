package edu.udel.cis.vsl.sarl.herbrand.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class HerbrandComparator implements Comparator<NumericExpression> {

	private Comparator<SymbolicObject> objectComparator;

	private Comparator<SymbolicType> typeComparator;

	HerbrandComparator(Comparator<SymbolicObject> objectComparator,
			Comparator<SymbolicType> typeComparator) {
		this.objectComparator = objectComparator;
		this.typeComparator = typeComparator;
	}

	public int compare(NumericExpression o1, NumericExpression o2) {
		SymbolicType t1 = o1.type();
		SymbolicType t2 = o2.type();

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
