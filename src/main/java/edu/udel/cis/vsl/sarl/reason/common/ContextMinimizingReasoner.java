package edu.udel.cis.vsl.sarl.reason.common;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.simplify.Simplify;
import edu.udel.cis.vsl.sarl.simplify.IF.ContextPartition;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.util.Pair;

/**
 * <p>
 * A {@link Reasoner} based on <strong>context minimization</strong>. Given a
 * context (the boolean expression which serves as the underlying assumption)
 * and a predicate (the boolean expression to check for validity or to be
 * simplified), the context minimization algorithm produces a new context which
 * is possibly weaker than the original one, but which is guaranteed to produce
 * an equivalent result when used as the context for validity or simplification.
 * </p>
 * 
 * <p>
 * In addition, this reasoner uses simplification, caching, and calls to
 * underlying {@link TheoremProver}s as needed.
 * </p>
 * 
 * @see ContextPartition
 * 
 * @author Stephen F. Siegel
 *
 */
public class ContextMinimizingReasoner implements Reasoner {

	// Static fields...

	/**
	 * Print debugging information?
	 */
	private final static boolean debug = false;

	/**
	 * Where to print the debugging information.
	 */
	private final static PrintStream debugOut = System.out;

	/**
	 * Try renaming all symbolic constants in a canonical way, like in Green.
	 */
	private final static boolean rename = false;

	// Instance fields...

	/**
	 * The prover. Only initialized when and if it is needed, because it may be
	 * expensive and may be never necessary if all of the queries are delegated
	 * to reduced contexts.
	 */
	private TheoremProver prover = null;

	/**
	 * The simplifier. Only initialized when and if it is needed, because it may
	 * be expensive and may be never necessary if all of the simplification
	 * tasks are delegated to reduced contexts.
	 */
	private Simplifier simplifier = null;

	/**
	 * The factory responsible for producing instances of
	 * {@link ContextMinimizingReasoner}, including this one. It is needed to
	 * produce the {@link #prover} and/or {@link #simplifier}.
	 */
	private ContextMinimizingReasonerFactory factory;

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
	private ContextPartition partition;

	/**
	 * Cached results of calls to {@link #valid(BooleanExpression)}. All results
	 * are stored here (except the most trivial ones), even if they were
	 * obtained by delegation to a reduced context.
	 */
	private Map<BooleanExpression, ValidityResult> validityCache = new HashMap<>();

	// Constructors...

	/**
	 * Constructs new context-minimizing reasoner.
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
		assert context.isCanonic();
		this.factory = factory;
		this.context = context;
		this.partition = Simplify.newContextPartition(factory.getUniverse(),
				context);
	}

	// Private methods...

	private Simplifier getSimplifier() {
		if (simplifier == null)
			simplifier = factory.getSimplifierFactory().newSimplifier(context);
		return simplifier;
	}

	private TheoremProver getProver() {
		if (prover == null)
			prover = factory.getProverFactory().newProver(getReducedContext());
		return prover;
	}

	/**
	 * Use context minimization to compute a reduced context for the given
	 * expression, AND then rename all the symbolic constants in the reduced
	 * context and the expression.
	 * 
	 * @param expression
	 *            a symbolic expression that is to be simplified or validated
	 * @return a pair consisting of the {@link Reasoner} for the renamed,
	 *         reduced context and the renamed expression
	 */
	private Pair<ContextMinimizingReasoner, SymbolicExpression> reduceAndRename(
			SymbolicExpression expression) {
		BooleanExpression reducedContext = partition.minimizeFor(expression,
				factory.getUniverse());
		UnaryOperator<SymbolicExpression> renamer = factory.getUniverse()
				.canonicalRenamer("X");
		BooleanExpression renamedContext = (BooleanExpression) renamer
				.apply(reducedContext);
		SymbolicExpression renamedExpression = renamer.apply(expression);
		ContextMinimizingReasoner reasoner;

		if (renamedContext == context) {
			reasoner = this;
		} else {
			reasoner = factory.getReasoner(renamedContext);
		}
		return new Pair<ContextMinimizingReasoner, SymbolicExpression>(
				reasoner, renamedExpression);
	}

	/**
	 * Use context minimization to compute a reduced context for the given
	 * expression.
	 * 
	 * @param expression
	 *            a symbolic expression that is to be simplified or validated
	 * @return the {@link Reasoner} for the reduced context
	 */
	private ContextMinimizingReasoner getReducedReasonerFor(
			SymbolicExpression expression) {
		BooleanExpression reducedContext = partition.minimizeFor(expression,
				factory.getUniverse());
		ContextMinimizingReasoner reducedReasoner;

		if (reducedContext == context) {
			reducedReasoner = this;
		} else {
			reducedReasoner = factory.getReasoner(reducedContext);
		}
		return reducedReasoner;
	}

	/**
	 * Attempts to determine validity of <code>predicate</code> without printing
	 * anything. Uses context-reduction, caching, simplification, and
	 * theorem-provers as needed.
	 * 
	 * @param predicate
	 *            non-<code>null</code> boolean expression whose validity under
	 *            this context is to be determined
	 * @param getModel
	 *            if <code>true</code>, try to find a model (concrete
	 *            counterexample) if the result is not valid, i.e., return an
	 *            instance of {@link ModelResult}.
	 * @return a non-<code>null</code> validity result
	 */
	private ValidityResult valid1(BooleanExpression predicate, boolean getModel) {
		if (predicate.isTrue())
			return Prove.RESULT_YES;
		if (predicate.isFalse())
			return Prove.RESULT_NO;

		ValidityResult result = validCheckCache(predicate, getModel);

		if (result != null)
			return result;

		ContextMinimizingReasoner reducedReasoner;
		BooleanExpression transformedPredicate;

		if (rename) {
			// note: for now, getModel won't work with renamed predicates; you
			// need a way to get the map between the old and new names in the
			// predicate
			Pair<ContextMinimizingReasoner, SymbolicExpression> pair = reduceAndRename(predicate);

			reducedReasoner = pair.left;
			transformedPredicate = (BooleanExpression) pair.right;
		} else {
			reducedReasoner = getReducedReasonerFor(predicate);
			transformedPredicate = predicate;
		}

		if (debug) {
			debugOut.println("Reduced context       : "
					+ reducedReasoner.context);
			debugOut.println("Transformed predicate : " + transformedPredicate);
		}

		if (reducedReasoner != this) {
			result = reducedReasoner.validCacheNoReduce(transformedPredicate,
					getModel);
		} else {
			result = this.validNoCacheNoReduce(transformedPredicate, getModel);
		}
		updateCache(predicate, result);
		return result;
	}

	/**
	 * <p>
	 * Attempts to determine the validity of <code>predicate</code>, without
	 * printing anything and without using context-reduction. May check the
	 * cache(s) for previous results on <code>predicate</code>; may use
	 * simplification; may use the theorem prover.
	 * </p>
	 * 
	 * <p>
	 * Precondition: the <code>context</code> is already reduced for
	 * <code>predicate</code>.
	 * </p>
	 * 
	 * @param predicate
	 *            non-<code>null</code> boolean expression whose validity under
	 *            this context is to be determined
	 * @param getModel
	 *            if <code>true</code>, try to find a model (concrete
	 *            counterexample) if the result is not valid, i.e., return an
	 *            instance of {@link ModelResult}.
	 * @return a non-<code>null</code> validity result
	 */
	private ValidityResult validCacheNoReduce(BooleanExpression predicate,
			boolean getModel) {
		ValidityResult result = validCheckCache(predicate, getModel);

		if (result != null)
			return result;
		result = validNoCacheNoReduce(predicate, getModel);
		updateCache(predicate, result);
		return result;
	}

	/**
	 * <p>
	 * Attempts to determine the validity of <code>predicate</code>, without
	 * printing anything, without using context-reduction, and without checking
	 * the cache(s) for previous results on <code>predicate</code>. May use
	 * simplification and the theorem prover.
	 * </p>
	 * 
	 * <p>
	 * Precondition: the <code>context</code> is already reduced for
	 * <code>predicate</code>.
	 * </p>
	 * 
	 * @param predicate
	 *            non-<code>null</code> boolean expression whose validity under
	 *            this context is to be determined
	 * @param getModel
	 *            if <code>true</code>, try to find a model (concrete
	 *            counterexample) if the result is not valid, i.e., return an
	 *            instance of {@link ModelResult}.
	 * @return a non-<code>null</code> validity result
	 */
	private ValidityResult validNoCacheNoReduce(BooleanExpression predicate,
			boolean getModel) {
		BooleanExpression newContext = getSimplifier().getReducedContext();
		BooleanExpression newPredicate = (BooleanExpression) simplifier
				.apply(predicate);
		ValidityResult result;
		ContextMinimizingReasoner newReasoner; // may be same as old

		if (newContext == context) {
			newReasoner = this;
		} else {
			newReasoner = factory.getReasoner(newContext);
		}
		if (newPredicate != predicate || newContext != context) {
			// the predicate or context got simpler, so start over again with
			// checks of trivial cases, cache, etc...
			if (debug) {
				debugOut.println("Context              : " + context);
				debugOut.println("Simplified context   : " + newContext);
				debugOut.println("Predicate            : " + predicate);
				debugOut.println("Simplified predicate : " + newPredicate);
				debugOut.flush();
			}
			result = newReasoner.valid1(newPredicate, getModel);
		} else if (getModel) {
			result = getProver().validOrModel(newPredicate);
		} else {
			result = getProver().valid(newPredicate);
		}
		return result;
	}

	/**
	 * Looks for cached result of validity check on predicate. For the context
	 * "true", results are cached directly in the predicate. Otherwise, look in
	 * the map {@link #validityCache}.
	 * 
	 * @param predicate
	 *            boolean expression whose validity is being checked
	 * @return cached result from previous check on this predicate or
	 *         <code>null</code> if no such result is cached
	 */
	private ValidityResult validCheckCache(BooleanExpression predicate,
			boolean getModel) {
		BooleanExpression fullContext = getFullContext();
		ValidityResult result;

		if (fullContext.isTrue()) {
			ResultType resultType = predicate.getValidity();

			if (resultType != null) {
				switch (resultType) {
				case MAYBE:
					result = Prove.RESULT_MAYBE;
					break;
				case NO:
					if (getModel)
						result = validityCache.get(predicate);
					else
						result = Prove.RESULT_NO;
					break;
				case YES:
					result = Prove.RESULT_YES;
					break;
				default:
					throw new SARLInternalException("unrechable");
				}
			} else {
				result = null;
			}
		} else {
			result = validityCache.get(predicate);
		}
		return result;
	}

	/**
	 * Updates the validity cache with the specified result.
	 * 
	 * @param predicate
	 *            boolean expression whose validity was checked
	 * @param result
	 *            the (non-<code>null</code>) result of the validity check on
	 *            <code>predicate</code>
	 */
	private void updateCache(BooleanExpression predicate, ValidityResult result) {
		BooleanExpression fullContext = getFullContext();

		if (fullContext.isTrue()) {
			predicate.setValidity(result.getResultType());
			if (result instanceof ModelResult)
				validityCache.put(predicate, result);
		} else {
			validityCache.put(predicate, result);
		}
	}

	/**
	 * Attempts to reduce the given <code>expression</code> to a concrete
	 * {@link Number}, without using context-reduction.
	 * 
	 * Precondition: this <code>context</code> is already the reduced context
	 * for <code>expression</code>.
	 * 
	 * @param expression
	 *            a non-<code>null</code> numeric expression
	 * @return <code>null</code> or concrete {@link Number}
	 */
	private Number extractNumberNoReduce(NumericExpression expression) {
		NumericExpression simple = (NumericExpression) simplify(expression);

		return factory.getUniverse().extractNumber(simple);
	}

	// Public methods...

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		return getSimplifier().substitutionMap();
	}

	@Override
	public BooleanExpression getReducedContext() {
		return getSimplifier().getReducedContext();
	}

	@Override
	public BooleanExpression getFullContext() {
		return getSimplifier().getFullContext();
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return getSimplifier().assumptionAsInterval(symbolicConstant);
	}

	@Override
	public SymbolicExpression simplify(SymbolicExpression expression) {

		if (debug) {
			debugOut.println("Simplifying            :" + expression + " ("
					+ expression.type() + ")");
			debugOut.println("Simplification context : " + context);
		}

		ContextMinimizingReasoner reducedReasoner = getReducedReasonerFor(expression);
		SymbolicExpression result = reducedReasoner.getSimplifier().apply(
				expression);

		if (debug) {
			debugOut.println("Simplification result  : " + result + " ("
					+ result.type() + ")");
		}
		return result;
	}

	@Override
	public BooleanExpression simplify(BooleanExpression expression) {
		return (BooleanExpression) simplify((SymbolicExpression) expression);
	}

	@Override
	public NumericExpression simplify(NumericExpression expression) {
		return (NumericExpression) simplify((SymbolicExpression) expression);
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		PreUniverse universe = factory.getUniverse();
		boolean showQuery = universe.getShowQueries();

		predicate = (BooleanExpression) universe.canonic(predicate);
		if (showQuery) {
			PrintStream out = universe.getOutputStream();
			int id = universe.numValidCalls();

			out.println("Query " + id + " context        : " + context);
			out.println("Query " + id + " assertion      : " + predicate);
			out.flush();
		}

		ValidityResult result = valid1(predicate, false);

		if (showQuery) {
			PrintStream out = universe.getOutputStream();
			int id = universe.numValidCalls();

			out.println("Query " + id + " result         : " + result);
			out.flush();
		}
		universe.incrementValidCount();
		return result;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		PreUniverse universe = factory.getUniverse();
		boolean showQuery = universe.getShowQueries();

		if (showQuery) {
			PrintStream out = universe.getOutputStream();
			int id = universe.numValidCalls();

			out.println("ModelQuery " + id + " context   : " + context);
			out.println("ModelQuery " + id + " assertion : " + predicate);
			out.flush();
		}

		ValidityResult result = valid1(predicate, true);

		if (showQuery) {
			PrintStream out = universe.getOutputStream();
			int id = universe.numValidCalls();

			out.println("ModelQuery " + id + " result    : " + result);
			out.flush();
		}
		universe.incrementValidCount();
		return result;
	}

	@Override
	public boolean isValid(BooleanExpression predicate) {
		return valid(predicate).getResultType() == ResultType.YES;
	}

	@Override
	public Number extractNumber(NumericExpression expression) {
		return getReducedReasonerFor(expression).extractNumberNoReduce(
				expression);
	}
}
