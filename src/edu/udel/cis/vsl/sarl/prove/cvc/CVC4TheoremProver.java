package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class CVC4TheoremProver implements TheoremProver{

	private PreUniverse model;
	private BooleanExpression context;
	private PrintStream out = System.out;
	private boolean showProverQueries = false;
	private ExprManager em = new ExprManager();
	private SmtEngine smt = new SmtEngine(em);
	private Expr cvcAssumption;

	CVC4TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.model = universe;
		this.context = context;
		cvcAssumption = translate(context);
		smt.assertFormula(cvcAssumption);
	}
	 
	private Expr translate(BooleanExpression context) {
		// TODO Auto-generated method stub
		return null;
	}

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
