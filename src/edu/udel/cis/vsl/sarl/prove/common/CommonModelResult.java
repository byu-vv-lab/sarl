package edu.udel.cis.vsl.sarl.prove.common;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * A result to a validity query which also requested a model in case the answer
 * was "NO", and for which the answer was "NO". The model may nevertheless be
 * null, indicating the model could not be constructed for some reason (even
 * though the query is known to be invalid).
 * 
 * @author siegel
 * 
 */
public class CommonModelResult extends CommonValidityResult implements
		ModelResult {

	/**
	 * The model: assigns a concrete symbolic expression value to each symbolic
	 * constant occurring in the query.
	 */
	private Map<SymbolicConstant, SymbolicExpression> model;

	/**
	 * Constructs new ModelResult with given model. The result type will be
	 * {@link ResultType.NO}.
	 * 
	 * @param model
	 *            the model, a map assigning a concrete symbolic expression
	 *            value to each symbolic constant occurring in the query; may be
	 *            null
	 */
	public CommonModelResult(Map<SymbolicConstant, SymbolicExpression> model) {
		super(ResultType.NO);
		this.model = model;
	}

	/**
	 * Returns the model, a map assigning a concrete symbolic expression value
	 * to each symbolic constant occurring in the query. May be null.
	 * 
	 * @return the model or null
	 */
	@Override
	public Map<SymbolicConstant, SymbolicExpression> getModel() {
		return model;
	}
}
