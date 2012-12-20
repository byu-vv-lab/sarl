package edu.udel.cis.vsl.sarl.symbolic.constant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;

/**
 * Factory for producing both symbolic constants and symbolic constant
 * expressions. Uses flyweight pattern in both cases.
 * */
public class SymbolicConstantFactory {

	Map<SymbolicConstantKey, SymbolicConstant> map = new HashMap<SymbolicConstantKey, SymbolicConstant>();

	Vector<SymbolicConstant> vector = new Vector<SymbolicConstant>();

	Map<SymbolicExpressionKey<SymbolicConstantExpression>, SymbolicConstantExpression> expressionMap = new HashMap<SymbolicExpressionKey<SymbolicConstantExpression>, SymbolicConstantExpression>();

	/**
	 * Returns a flyweighted symbolic constant object with given name and type.
	 * The id number is unique to the equivalence class.
	 */
	public SymbolicConstant getOrCreateSymbolicConstant(String name,
			SymbolicTypeIF type) {
		SymbolicConstant newValue = new SymbolicConstant(name, type);
		SymbolicConstantKey key = new SymbolicConstantKey(newValue);
		SymbolicConstant old = map.get(key);

		if (old == null) {
			map.put(key, newValue);
			newValue.setId(vector.size());
			vector.add(newValue);
			return newValue;
		} else {
			return old;
		}
	}

	/** Returns null if no such symbolic constant exists. */
	public SymbolicConstant getSymbolicConstant(String name, SymbolicTypeIF type) {
		return map
				.get(new SymbolicConstantKey(new SymbolicConstant(name, type)));
	}

	/**
	 * Throws exception if symbolic constant with this name and type already
	 * exists
	 */
	public SymbolicConstant newSymbolicConstant(String name, SymbolicTypeIF type) {
		SymbolicConstant newValue = new SymbolicConstant(name, type);
		SymbolicConstantKey key = new SymbolicConstantKey(newValue);
		SymbolicConstant old = map.get(key);

		if (old == null) {
			map.put(key, newValue);
			newValue.setId(vector.size());
			vector.add(newValue);
			return newValue;
		} else {
			throw new IllegalArgumentException("Symbolic constant with name + "
					+ name + " and type " + type + " already exists");
		}
	}

	public Collection<SymbolicConstant> symbolicConstants() {
		return vector;
	}

	public int numConstants() {
		return vector.size();
	}

	public SymbolicConstant symbolicConstantWithId(int id) {
		return vector.elementAt(id);
	}

	/**
	 * Returns a flyweighted symbolic constant expression corresponding to the
	 * given symbolic constant. Like "get or create" but for symbolic constant
	 * expressions instead of symbolic constants.
	 */
	public SymbolicConstantExpression expression(SymbolicConstantIF constant) {
		return SymbolicExpression.flyweight(expressionMap,
				new SymbolicConstantExpression(constant));
	}
}
