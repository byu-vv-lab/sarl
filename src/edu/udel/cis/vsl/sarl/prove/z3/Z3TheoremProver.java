package edu.udel.cis.vsl.sarl.prove.z3;

import java.util.ArrayDeque;
import java.util.Deque;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.IntSort;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.TupleSort;
import com.microsoft.z3.Z3Exception;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

/*
 * Under construction.
 * 
 * Some notes on Z3.
 * 
 * To create a forall expression: create a symbol from a string. Create an
 * expression from the symbol using mkConst. Use the expression to form the
 * body.
 * 
 * Need patterns and weights.
 * 
 * Arrays have to be modeled as ordered pairs, like in CVC. Why not make every
 * array value a big array. That will simplify everything.
 * 
 * Use a stack to translate quantifiers. Push onto this stack translations for
 * each bound variable only. no need for cleaning, hopefully.
 * 
 * Functions: create a FuncDecl. The apply the function to some arguments, use
 * method apply in the FuncDecl. Also: mkApp.
 * 
 * Tuples: make the tuple sort. Get the constructor from the sort. Get the
 * fields from the sort: each field is a FuncDecl. I guess this func decl is the
 * extraction function (projection).
 */

public class Z3TheoremProver implements TheoremProver {

	public final static String bigArrayName = "bigArray";

	/**
	 * A stack is used to deal with nested scopes from quantifier expressions.
	 * An entry is pushed when entering a new quantified scope and popped when
	 * exiting. Each frame on the stack contains a map from symbolic expressions
	 * to Z3 Expr, giving the translation of each. This serves as a translation
	 * cast. Whenever you want to translate a symbolic expressions, search
	 * through this stack starting from the top (most recently added frame) and
	 * going down. Note that the top of the stack is considered the "first"
	 * element and the iterator goes from the top of the stack down.
	 * 
	 */
	private Deque<TranslationFrame> stack = new ArrayDeque<TranslationFrame>();

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * The Z3 object used to create expressions.
	 */
	private Context ctx;

	/**
	 * The Z3 object used to solve queries.
	 */
	private Solver solver;

	private BoolExpr z3Assumption;

	private IntSort intSort;

	/**
	 * The Z3 integer constant 0.
	 */
	private IntNum z3_0;

	/**
	 * The CVC4 integer constant 1.
	 */
	private IntNum z3_1;

	/**
	 * The CVC4 boolean constant "true".
	 */
	private BoolExpr z3_true;

	/**
	 * The CVC4 boolean constant "false".
	 */
	private BoolExpr z3_false;

	/**
	 * The number of auxiliary Z3 variables created. These are the variables
	 * that do not correspond to any SARL variable but are needed for some
	 * reason to translate an expression. Includes both ordinary and bound Z3
	 * variables.
	 */
	private int auxVarCount = 0;

	private Symbol bigArraySymbol;

	// *************************** Constructors *************************** //

	/**
	 * Constructs new CVC4 theorem prover with given symbolic universe.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param context
	 *            the assumption(s) the prover will use for queries
	 */
	Z3TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		try {
			ctx = new Context();
			solver = ctx.mkSolver();
			intSort = ctx.getIntSort();
			z3_0 = ctx.mkInt(0);
			z3_1 = ctx.mkInt(1);
			z3_true = ctx.mkTrue();
			z3_false = ctx.mkFalse();
			bigArraySymbol = ctx.mkSymbol(bigArrayName);
			if (context.isTrue()) {
				z3Assumption = z3_true;
			} else {
				z3Assumption = (BoolExpr) translate(context);
				solver.add(z3Assumption);
			}
		} catch (Z3Exception e) {
			throw new SARLInternalException("Unable to construct Z3 solver: "
					+ e);
		}
	}

	// ************************* Private Methods ************************** //

	/**
	 * Creates a new Z3 (ordinary) variable of given type with unique name;
	 * increments {@link #auxVarCount}.
	 * 
	 * @param sort
	 *            a Z3 SORT
	 * @return the new CVC4 variable
	 */
	private Expr newAuxVar(Sort sort) throws Z3Exception {
		Expr result = ctx.mkFreshConst("aux", sort);

		return result;
	}

	/**
	 * Creates a new CVC4 symbol of given type with unique name to be used in a
	 * quantifier expression; increments {@link #auxVarCount}
	 * 
	 * @param sort
	 *            a Z3 sort
	 * @return the new symbol
	 */
	private Symbol newAuxBoundSymbol(Sort sort) throws Z3Exception {
		Symbol symbol = ctx.mkSymbol("_i" + auxVarCount);

		auxVarCount++;
		return symbol;
	}

	/**
	 * This method takes in the length and value of an array and wraps them in
	 * an ordered pair.
	 * 
	 * @param length
	 *            Z3 Expr of length
	 * @param value
	 *            Z3 Expr of value
	 * @return Z3 tuple of an ordered pair (length, value)
	 * @throws Z3Exception
	 */
	private Expr bigArray(TupleSort bigArraySort, IntExpr length,
			ArrayExpr value) throws Z3Exception {
		// is there a way to get the constructor rather than make it?
		// could keep mappings from the array types to these constructors
		FuncDecl constructor = bigArraySort.mkDecl();
		Expr result = constructor.apply(length, value);

		return result;
	}

	/**
	 * Given any CVC Expr which has type (Integer, array-of-T), returns the
	 * first component, i.e., the component of integer type.
	 * 
	 * @param bigArray
	 *            any CVC Expr of type (Integer, array-of-T) for some T
	 * @return the first component of that expression
	 * @throws Z3Exception
	 */
	private IntExpr bigArrayLength(Expr bigArray) throws Z3Exception {
		IntExpr length;

		if (bigArrayName.equals(bigArray.getFuncDecl().getName().toString())) {
			assert bigArray.getNumArgs() == 2;
			length = (IntExpr) bigArray.getArgs()[0];
		} else {
			DatatypeSort datatypeSort = (DatatypeSort) bigArray.getSort();
			FuncDecl lengthAccessor = datatypeSort.getAccessors()[0][0];

			// can I do this conversion?
			length = (IntExpr) ctx.mkApp(lengthAccessor, bigArray);
		}
		return length;
	}

	/**
	 * Given any CVC Expr which has type (Integer, array-of-T), returns the
	 * second component, i.e., the component of array type.
	 * 
	 * @param bigArray
	 *            any CVC Expr of type (Integer, array-of-T) for some T
	 * @return the second component of that expression
	 * @throws Z3Exception
	 */
	private ArrayExpr bigArrayValue(Expr bigArray) throws Z3Exception {
		ArrayExpr value;

		if (bigArrayName.equals(bigArray.getFuncDecl().getName().toString())) {
			assert bigArray.getNumArgs() == 2;
			value = (ArrayExpr) bigArray.getArgs()[1];
		} else {
			DatatypeSort datatypeSort = (DatatypeSort) bigArray.getSort();
			FuncDecl valueAccessor = datatypeSort.getAccessors()[0][1];

			// can I do this conversion?
			value = (ArrayExpr) ctx.mkApp(valueAccessor, bigArray);
		}
		return value;
	}

	private Expr translate(SymbolicExpression expression) throws Z3Exception {

		return null;
	}

	@Override
	public PreUniverse universe() {
		return universe;
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

}
