package edu.udel.cis.vsl.sarl.IF;

import java.io.PrintStream;
import java.util.Arrays;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class SARLDemo {

	static PrintStream out = System.out;
	SymbolicUniverse universe = SARL.newStandardUniverse();
	SymbolicType realType = universe.realType();
	NumericExpression zero = universe.rational(0);
	NumericExpression x = (NumericExpression) universe.symbolicConstant(
			universe.stringObject("x"), realType);
	NumericExpression y = (NumericExpression) universe.symbolicConstant(
			universe.stringObject("y"), realType);
	NumericExpression z = (NumericExpression) universe.symbolicConstant(
			universe.stringObject("z"), realType);

	void demoPolynomials() {
		NumericExpression z1 = universe.power(universe.add(x, y), 100);
		NumericExpression z2 = universe.power(universe.add(x, y), 99);
		out.println("(x+y)^100 = " + z1);
		out.println("(x+y)^99 = " + z2);
		out.println("(x+y)^100/(x+y)^99 = " + universe.divide(z1, z2));
	}

	void simplify1() {
		// a=x*y, b=y*z, c=x*z
		// b+c>0
		// a+b=6
		// a-b=2
		// --> a=4, b=2, c>-2
		// Simplify a+b+c>0 -> 6+c>0 -> c>-6 -> true

		NumericExpression a = universe.multiply(x, y), b = universe.multiply(y,
				z), c = universe.multiply(x, z);
		BooleanExpression context = universe
				.and(Arrays.asList(
						universe.lessThan(zero, universe.add(b, c)),
						universe.equals(universe.add(a, b),
								universe.rational(6)),
						universe.equals(universe.subtract(a, b),
								universe.rational(2))));
		out.println("context = " + context);
		SymbolicExpression expr = universe.lessThan(zero,
				universe.add(Arrays.asList(a, b, c)));
		out.println("expr = " + expr);
		Reasoner reasoner = universe.reasoner(context);
		out.println("reduced context = " + reasoner.getReducedContext());
		out.println("simplified expr = " + reasoner.simplify(expr));
	}

	void reference() {
		SymbolicType real5 = universe.arrayType(realType, universe.integer(5));
		SymbolicType real55 = universe.arrayType(real5, universe.integer(5));
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), real55);
		out.println("a = " + a);
		ReferenceExpression r23 = universe.arrayElementReference(universe
				.arrayElementReference(universe.identityReference(),
						universe.integer(2)), universe.integer(3));
		out.println("r2 = " + r23);
		out.println("dereference(a,r23)=" + universe.dereference(a, r23));
		SymbolicExpression a_new = universe
				.assign(a, r23, universe.rational(7));
		out.println("a_new = " + a_new);
		out.println("a_new[2][3] = "
				+ universe.arrayRead(
						universe.arrayRead(a_new, universe.integer(2)),
						universe.integer(3)));
	}

	void reason() {
		Reasoner reasoner = universe.reasoner(universe.equals(zero,
				universe.multiply(x, y)));
		BooleanExpression assertion = universe.or(universe.equals(zero, x),
				universe.equals(zero, y));

		out.println(reasoner.isValid(assertion));
	}

	// substitution

	// quantified expression
	
	// uninterpreted functions
	
	// Herbrands

	// arrays and simplification

	public final static void main(String[] args) {
		SARLDemo demo = new SARLDemo();

		demo.demoPolynomials();
		demo.simplify1();
		demo.reference();
		demo.reason();
		out.flush();
	}

}
