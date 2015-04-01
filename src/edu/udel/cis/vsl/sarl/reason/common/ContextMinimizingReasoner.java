package edu.udel.cis.vsl.sarl.reason.common;

import java.io.PrintStream;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.simplify.Simplify;
import edu.udel.cis.vsl.sarl.simplify.IF.ContextPartition;

/**
 * Strategy:
 * 
 * Right now: CommonSymbolicUniverse has mapping from boolean expressions to
 * their Reasoners
 * 
 * ContextMinimizingReasonerFactory: - has reference to the reasoner cache,
 * mapping each boolean expression to an instance of ContextMinimizingReasoner -
 * has reference to CommonReasonerFactory
 * 
 * ContextMinimizingReasoner: - has reference to ContextPartition (initialized
 * when needed) - has reference to CommonReasoner (initialized when needed) -
 * has reference to ContextMinimizingReasonerFactory
 * 
 * protocol for context minimizing reasoner valid calls:
 * <ul>
 * <li>look in cache, return if answer is there</li>
 * <li>initialize the context partition if needed</li>
 * <li>reduce the context using the context partition</li>
 * <li>get the common reasoner RR for the reduced context: if reduced context =
 * context, that would be obtained from this reasoner, else use the cache in the
 * reasoner factory to get the context minimizing reasoner and get the common
 * reasoner from it</li>
 * <li>use RR to resolve query</li>
 * <li>cache and return query result</li>
 * </ul>
 * 
 * protocol for a common reasoner valid calls </ul> <li>look in cache, return if
 * answer is there</li> <li>apply simplifier to predicate, cache and return
 * result if that resolved query</li> <li>etc. as happens now</li> </ul>
 * 
 * protocol for context minimizing reasoner simplify calls:
 * <ul>
 * ditto
 * </ul>
 * 
 * 
 * @author siegel
 *
 */
public class ContextMinimizingReasoner implements Reasoner {

	public final static boolean debug = false;

	public final static PrintStream out = System.out;

	/**
	 * The factory responsible for producing instances of
	 * {@link ContextMinimizingReasoner}, including this one.
	 */
	private ContextMinimizingReasonerFactory factory;

	/**
	 * The underlying reasoner wrapped by this reasoner.
	 */
	private Reasoner underlyingReasoner = null;

	/**
	 * The context (i.e., path condition) associated to this reasoner. All
	 * simplifications and queries are executed using this context as the
	 * underlying assumption.
	 */
	private BooleanExpression context;

	/**
	 * The partition of the set of conjunctive clauses of the context into
	 * equivalence classes. The equivalence relation is given by two clauses are
	 * equivalent if they share a common variables; complete to an equivalence
	 * relation.
	 */
	private ContextPartition partition = null;

	/**
	 * Constructs new context-minimizing reasoner with initially
	 * <code>null</code> <code>partition</code>.
	 * 
	 * @param factory
	 *            the factory used for producing this and other instances of
	 *            {@link ContextMinimizingReasoner}
	 * @param context
	 *            the context (i.e., path condition), the fixed, underlying
	 *            assumption used when processing all simplification and theorem
	 *            prover queries with this reasoner
	 */
	public ContextMinimizingReasoner(ContextMinimizingReasonerFactory factory,
			BooleanExpression context) {
		assert !context.isTrue();
		this.factory = factory;
		this.context = context;
	}

	/**
	 * Returns the underlying reasoner for the reduced context determined by the
	 * given expression. The reduced context is the conjunction of a subset of
	 * the conjunctive clauses of the context. (In particular, it is weaker than
	 * the context.)
	 * 
	 * @param expression
	 *            a symbolic expression
	 * @return the underlying reasoner associated to the reduced context
	 *         determined by the <code>expression</code> and the
	 *         {@link #context}
	 */
	private Reasoner getReducedReasoner(SymbolicExpression expression) {
		BooleanExpression reducedContext = getPartition().minimizeFor(
				expression, factory.getUniverse());
		Reasoner reasoner = factory.getReasoner(reducedContext);

		if (reasoner instanceof ContextMinimizingReasoner) {
			reasoner = ((ContextMinimizingReasoner) reasoner)
					.getUnderlyingReasoner();
		}
		return reasoner;
	}

	private ContextPartition getPartition() {
		if (partition == null) {
			partition = Simplify.newContextPartition(factory.getUniverse(),
					context);
		}
		return partition;
	}

	private Reasoner getUnderlyingReasoner() {
		if (underlyingReasoner == null) {
			underlyingReasoner = factory.getUnderlyingFactory().getReasoner(
					context);
		}
		return underlyingReasoner;
	}

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		return getUnderlyingReasoner().substitutionMap();
	}

	@Override
	public BooleanExpression getReducedContext() {
		return getUnderlyingReasoner().getReducedContext();
	}

	@Override
	public BooleanExpression getFullContext() {
		return getUnderlyingReasoner().getFullContext();
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return getUnderlyingReasoner().assumptionAsInterval(symbolicConstant);
	}

	@Override
	public SymbolicExpression simplify(SymbolicExpression expression) {
		if (debug) {
			out.println("Simplifying...");
			out.println("Context    : " + context);
			out.println("Expression : " + expression);
			out.flush();
		}

		SymbolicExpression result = getReducedReasoner(expression).simplify(
				expression);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

	@Override
	public BooleanExpression simplify(BooleanExpression expression) {
		if (debug) {
			out.println("Simplifying...");
			out.println("Context    : " + context);
			out.println("Expression : " + expression);
			out.flush();
		}

		BooleanExpression result = getReducedReasoner(expression).simplify(
				expression);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

	@Override
	public NumericExpression simplify(NumericExpression expression) {
		if (debug) {
			out.println("Simplifying...");
			out.println("Context    : " + context);
			out.println("Expression : " + expression);
			out.flush();
		}

		NumericExpression result = getReducedReasoner(expression).simplify(
				expression);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		if (debug) {
			out.println("Valid...");
			out.println("Context    : " + context);
			out.println("Predicate  : " + predicate);
			out.flush();
		}

		ValidityResult result = getReducedReasoner(predicate).valid(predicate);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		if (debug) {
			out.println("ValidOrModel...");
			out.println("Context    : " + context);
			out.println("Predicate  : " + predicate);
			out.flush();
		}

		ValidityResult result = getReducedReasoner(predicate).validOrModel(
				predicate);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

	@Override
	public boolean isValid(BooleanExpression predicate) {
		if (debug) {
			out.println("isValid...");
			out.println("Context    : " + context);
			out.println("Predicate  : " + predicate);
			out.flush();
		}

		boolean result = getReducedReasoner(predicate).isValid(predicate);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		if (debug) {
			out.println("extractNumber...");
			out.println("Context    : " + context);
			out.println("Predicate  : " + expression);
			out.flush();
		}

		Number result = getReducedReasoner(expression)
				.extractNumber(expression);

		if (debug) {
			out.println("Result     : " + result);
			out.flush();
		}
		return result;
	}

}
