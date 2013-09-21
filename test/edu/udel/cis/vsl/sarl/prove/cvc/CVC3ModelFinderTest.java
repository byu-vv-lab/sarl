package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ExprMut;
import cvc3.Op;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3ModelFinderTest {
	
	// Static fields: instantiated once and used for all tests...

	private static PrintStream out = System.out;

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	
	private static NumberFactory numFactory = factorySystem.numberFactory();

	private static SymbolicRealType realType = universe.realType();

	private static NumericExpression two = universe.rational(2);

	private static NumericExpression five = universe.rational(5);
	
	private static BooleanExpression booleanExprTrue = universe.trueExpression();
	private static BooleanExpression booleanExprFalse = universe.falseExpression();
	
	
	//Instance fields: instantiated before each test is run...
	
	private TheoremProverFactory proverFactory;

	private CVC3TheoremProver cvcProver;

	private ValidityChecker vc;
	
	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}
	
	/* Tests the constructor for CVC3ModelFinder which calls the 
	 * computeModel() method and with the case given calls 
	 * assign(Expr, SymbolicExpression) */
	
	@Test(expected=SARLInternalException.class)
	public void testCreateCVC3ModelFinder() {
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(vc.falseExpr(),vc.falseExpr());
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory.newProver(universe.bool(true));
		prover.setOutput(out);
		CVC3ModelFinder c = new CVC3ModelFinder(prover,h);
	}
	
	@Test
	public void testIsApplyExpr() {
		Expr varExpr = vc.varExpr("var", cvcProver.translateType(universe.realType()));
		Expr expr2 = cvcProver.translate(two);
		Expr expr5 = cvcProver.translate(five);
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(varExpr,vc.funExpr(vc.minusOp(), expr2, expr5));
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory.newProver(universe.bool(true));
		CVC3ModelFinder c = new CVC3ModelFinder(prover,h);
	}
	
	@Test
	public void testIsBooleanExpr() {
		Expr exprTrue = cvcProver.translate(booleanExprTrue);
		Expr exprFalse = cvcProver.translate(booleanExprFalse);
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(exprTrue,exprFalse);
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory.newProver(universe.bool(true));
		CVC3ModelFinder c = new CVC3ModelFinder(prover,h);
	}
	
	@Test
	public void testCVC3ModelFinderInvalid() {
		StringObject strX = universe.stringObject("x");
		SymbolicIntegerType sInt = universe.integerType();
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,sInt);
		NumericExpression symExprZero = universe.integer(0);
		BooleanExpression predicate = universe.equals(symConstXInt, symExprZero);
		ValidityResult result = cvcProver.validOrModel(predicate);
		
		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);
		
		ModelResult modelResult = (ModelResult) result;
		Map<SymbolicConstant, SymbolicExpression> map = modelResult.getModel();
		// Verify there is exactly 1 element in the map.
		assertEquals(1, map.size());
		
		Collection<SymbolicExpression> s = map.values();
		Number n = null;
		for(SymbolicExpression se : s) {
			if(se instanceof NumericExpression) {
				NumericExpression p = (NumericExpression) se;
				n = universe.extractNumber(p);
			}
		}
		//Verify that the result (counterExample) does not equal zero.
		assertFalse(n.equals(numFactory.integer(0)));		
	}
	
	@Test
	public void testCVC3ModelFinderApply() {
		//Give the prover the assumption that y = 0.
		CVC3TheoremProver cvcProverYIs0 = (CVC3TheoremProver) proverFactory
				.newProver(universe.equals(
						universe.symbolicConstant(universe.stringObject("y"),universe.integerType()), 
						universe.integer(0)));
		vc = cvcProverYIs0.validityChecker();
		
		StringObject strX = universe.stringObject("x");
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,universe.integerType());
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,universe.integerType());
		//Predicate is that "x = x + y" given the assumption that "y = 0".
		BooleanExpression predicate = universe.equals(symConstXInt, universe.append(symConstXInt, symConstYInt));
		ValidityResult result = cvcProverYIs0.validOrModel(predicate);
		
		ResultType resultType = result.getResultType();
		assertEquals(ResultType.YES, resultType);
	}
}

