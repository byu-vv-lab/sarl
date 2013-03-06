package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpression;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;

public class SimplifierInfo {

	/**
	 * Treat every polynomial as a linear combination of monomials, so Gaussian
	 * elimination is performed on all equalities, and not just degree 1
	 * equalities.
	 */
	public boolean linearizePolynomials = true;

	public SymbolicUniverse universe;

	public IdealFactory idealFactory;

	public NumberFactory numberFactory;

	public BooleanExpressionFactory booleanFactory;

	public AffineFactory affineFactory;

	public PrintStream out;

	public boolean verbose;

	public BooleanExpression trueExpr, falseExpr;
}
