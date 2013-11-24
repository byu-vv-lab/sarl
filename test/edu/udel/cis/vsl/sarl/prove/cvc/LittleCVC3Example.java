package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

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

