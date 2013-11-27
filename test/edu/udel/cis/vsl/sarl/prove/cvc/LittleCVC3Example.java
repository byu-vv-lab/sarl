package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import cvc3.Expr;
import cvc3.ValidityChecker;

public class LittleCVC3Example {

	static {
		System.loadLibrary("cvc3jni");
	}

	private static PrintStream out = System.out;
	private static ValidityChecker vc = ValidityChecker.create();

	public static void main(String[] args) {

		Expr zero;
		Expr x, xeq0;

		x = vc.varExpr("x", vc.realType());
		zero = vc.ratExpr(0);
		xeq0 = vc.eqExpr(x, zero);

		out.println("Starting little CVC3 example...");
		out.flush();
		out.println("Does f(x)=0? " + vc.query(xeq0));

		out.println("Why is it invalid? " + vc.getConcreteModel());

		out.flush();
	}

}
