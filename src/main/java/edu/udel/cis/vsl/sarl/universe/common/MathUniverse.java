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

	/*
	 * Breaking up the symbolic expression into the expressions that it is
	 * composed of by calling the method recursively. An array of all of the
	 * (separated) arguments is used to work through (continuing the recursion
	 * if necessary). By having each expression separated, we can then look for
	 * patterns to be able to break down the expression further - a more
	 * simplified manner.
	 * 
	 * EXPECTED PROBLEMS - passing in SymbolicObject arguments in the recursive
	 * call of a method taking in a SymbolicExpression. To solve this, either
	 * making new methods for each type of SymbolicObject, or distinguishing
	 * what is being passed next and deciding what to do (if something should be
	 * done) from there.
	 * 
	 * We will need to utilize the universe.make method. This will be extremely
	 * useful for us.
	 * 
	 * The first pattern we want to work on is exponentials for the
	 * representations of our sine and cosine functions in terms of i and e.
	 * This will mean checking if the expression's operator is POWER, arg0 is i,
	 * and arg1 is an int. From here, we can take the exponent (mod 4) to find
	 * what i raised to any power would equate to. (Utilizing switch statments)
	 * EXPECTED PROBLEM: i is not a real, so we need to create a new number type
	 * 
	 * Other patterns a^b * a^c = a^(b+c) (a^b)^c = a^(b*c) a^b / a^c = a^(b-c)
	 * 
	 * For our trig functions, we want to represent sine as: sine(x) = ((e^(ix)
	 * - e^(-ix)) / 2i) cosine(x) = ((e^(ix) + e^(-ix)) / 2)
	 * 
	 * Other trig functions can come from sine and cosine, so these are our two
	 * big focuses in implementing here. For example, tan(x) = sine(x)/cosine(x)
	 */

	public SymbolicExpression mathSimplify(SymbolicExpression expr) {
		/*
		 * int n = expr.numArguments(); SymbolicExpression[] newArgs = new
		 * SymbolicExpression[n]; for(int i = 0; i < n; i++){
		 * mathSimplify(expr.argument(i)); } expr =
		 * universe.make(expr.operator(), expr.type(), newArgs);
		 * 
		 * SymbolicExpression exprPrime;
		 * 
		 * if (expr.operator().equals("POWER") && expr.argument(0) == i &&
		 * isInt(expr.argument(1)){ //checking if it's i to some power //switch
		 * statement depending on which power i is being raised to % 4.
		 * //exponent = 0 --> 1 //exponent = 1 --> i //exponent = 2 --> -1
		 * //exponent = 3 --> -i
		 * 
		 * }
		 */
		return null;

	}

	// continue along these lines.

	// add tests that determine if you are using the trig identities correctly.

}
