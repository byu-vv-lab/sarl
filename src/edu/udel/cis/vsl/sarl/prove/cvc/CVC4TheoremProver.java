package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class CVC4TheoremProver implements TheoremProver{

	private PreUniverse model;
	private PrintStream out = System.out;
	private boolean showProverQueries = false;

	@Override
	public PreUniverse universe() {
		return model;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOutput(PrintStream out) {
		this.out = out;
		showProverQueries = out != null;
	}

}
