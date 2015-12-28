package edu.udel.cis.vsl.sarl.simplify;

import org.junit.Test;
import org.junit.Ignore;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class SimplifyCharTest {

	public final static boolean debug = false;

	private static SymbolicUniverse universe = SARL.newStandardUniverse();

	private static SymbolicType charType = universe.characterType();

	private static SymbolicExpression a = universe.character('a');

	private static SymbolicConstant X = universe.symbolicConstant(
			universe.stringObject("X"), charType);

	@Ignore @Test
	public void simplifyChar() {
		BooleanExpression context = universe.equals(X, a);
		Reasoner reasoner = universe.reasoner(context);
		SymbolicExpression result = reasoner.simplify(X);

		if (debug)
			System.out.println(result);
		assert (result.equals(a));
	}
}
