package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * A substituter used to change the names of symbolic constants. It is specified
 * by a map on {@link StringObject}s (essentially the same as Strings). This
 * maps the old names to the new. The substitution is applied to all symbolic
 * constants, including bound ones.
 * 
 * @author Stephen F. Siegel
 */
public class NameSubstituter extends ExpressionSubstituter {

	/**
	 * Map from old names to new. A name which does not occur as a key in this
	 * map will not be changed.
	 */
	private Map<StringObject, StringObject> map;

	public NameSubstituter(PreUniverse universe,
			CollectionFactory collectionFactory,
			SymbolicTypeFactory typeFactory, Map<StringObject, StringObject> map) {
		super(universe, collectionFactory, typeFactory);
		this.map = map;
	}

	@Override
	protected SubstituterState newState() {
		return null;
	}

	@Override
	protected SymbolicExpression substituteQuantifiedExpression(
			SymbolicExpression expr, SubstituterState state) {
		return super.substituteNonquantifiedExpression(expr, state);
	}

	@Override
	protected SymbolicExpression substituteNonquantifiedExpression(
			SymbolicExpression expr, SubstituterState state) {
		if (expr instanceof SymbolicConstant) {
			StringObject oldName = ((SymbolicConstant) expr).name();
			StringObject newName = map.get(oldName);

			if (newName != null) {
				SymbolicType newType = substituteType(expr.type(), state);
				SymbolicConstant result = universe.symbolicConstant(newName,
						newType);

				return result;
			}
		}
		return super.substituteNonquantifiedExpression(expr, state);
	}

}
