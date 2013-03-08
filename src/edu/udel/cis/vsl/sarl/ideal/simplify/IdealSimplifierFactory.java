package edu.udel.cis.vsl.sarl.ideal.simplify;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

public class IdealSimplifierFactory implements SimplifierFactory {

	private SimplifierInfo info;

	public IdealSimplifierFactory(IdealFactory idealFactory,
			SymbolicUniverse universe) {
		info = new SimplifierInfo();
		info.universe = universe;
		info.affineFactory = new AffineFactory(idealFactory);
		info.booleanFactory = idealFactory.booleanFactory();
		info.falseExpr = (BooleanExpression) universe.bool(false);
		info.trueExpr = (BooleanExpression) universe.bool(true);
		info.idealFactory = idealFactory;
		info.numberFactory = universe.numberFactory();
		info.out = System.out;
		info.verbose = true;
	}

	public IdealSimplifier newSimplifier(BooleanExpression assumption) {
		return new IdealSimplifier(info, assumption);
	}

}
