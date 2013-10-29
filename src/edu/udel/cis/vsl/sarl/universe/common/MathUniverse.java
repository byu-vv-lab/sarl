package edu.udel.cis.vsl.sarl.universe.common;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class MathUniverse extends CommonSymbolicUniverse implements PreUniverse {

	private SymbolicRealType realType;

	private SymbolicFunctionType realToRealFunctionType;

	private SymbolicExpression sinFunction;

	private SymbolicExpression cosFunction;

	private NumericExpression pi;

	private NumericExpression e;
	
	private NumericExpression i;

	public MathUniverse(FactorySystem system) {
		super(system);

		SymbolicTypeSequence realSingleton;

		this.realType = realType();
		realSingleton = typeSequence(new SymbolicType[] { realType });
		this.realToRealFunctionType = functionType(realSingleton, realType);
		this.sinFunction = canonic(symbolicConstant(stringObject("sin"),
				realToRealFunctionType));
		this.cosFunction = canonic(symbolicConstant(stringObject("cos"),
				realToRealFunctionType));
		this.pi = (NumericExpression) canonic(symbolicConstant(
				stringObject("pi"), realType));
		this.e = (NumericExpression) canonic(symbolicConstant(
				stringObject("e"), realType));
		this.i = (NumericExpression) canonic(symbolicConstant(
				stringObject("i"), realType));
	}

	public NumericExpression sin(NumericExpression arg) {
		NumericExpression result = (NumericExpression) this.apply(sinFunction,
				Arrays.asList(arg));

		// do work here to put into canonical form

		return result;
	}

	public NumericExpression cos(NumericExpression arg) {
		NumericExpression result = (NumericExpression) this.apply(cosFunction,
				Arrays.asList(arg));

		// do work here to put into canonical form

		return result;
	}

	public NumericExpression pi() {
		return pi;
	}

	public NumericExpression e() {
		return e;
	}
	
	public NumericExpression i() {
		return i;
	}
	
	public SymbolicExpression mathSimplify(SymbolicExpression expr) {
		// TODO
		
		return null;
	}
	
	// continue along these lines.
	
	// add tests that determine if you are using the trig identities correctly.

}
