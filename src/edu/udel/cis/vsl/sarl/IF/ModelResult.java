package edu.udel.cis.vsl.sarl.IF;

import java.util.Map;

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
public interface ModelResult extends ValidityResult {

	/**
	 * Returns the model, a map assigning a concrete symbolic expression value
	 * to each symbolic constant occurring in the query. May be null.
	 * 
	 * @return the model or null
	 */
	public Map<SymbolicConstant, SymbolicExpression> getModel();
}
