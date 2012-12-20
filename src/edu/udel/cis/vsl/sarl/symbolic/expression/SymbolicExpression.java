package edu.udel.cis.vsl.sarl.symbolic.expression;

import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

/**
 * The root of the symbolic expression hierarchy. Every symbolic expression
 * extends this class.
 * 
 * This class is implemented using the Flyweight Pattern. Because of this, there
 * is no need to override the equals or hashCode methods of Object.
 * 
 * The intrinsicHashCode and intrinsicEquals methods are use for internal
 * purposes. They are used by the symbolic.real module to implement the
 * flyweight pattern. They are not revealed to the outside user (who will only
 * access this class through the SymbolicExpressionIF interface). The user will
 * use the standard hashCode() and equals() methods defined in Object. This is
 * OK, because of the Flyweight Pattern guarantees the user will only get one
 * unique instance from each equivalence class of objects in this class, the
 * equivalence relation being defined by intrinsicEquals.
 * 
 */
public abstract class SymbolicExpression implements SymbolicExpressionIF {

	/**
	 * This keeps track of the number of unique (up to equivalence) symbolic
	 * expressions generated. It is incremented when the flyweight method is
	 * called, if the expression does not already exist in the flyweight table.
	 */
	private static int instanceCounter = 0;

	/**
	 * Every committed symbolic expression has a non-negative id, which is
	 * unique among all symbolic expressions. This is that ID. It is set once
	 * the flyweight method is called. Until then, it is -1.
	 * 
	 */
	private int uniqueId = -1;

	/**
	 * The type of this symbolic expression.
	 */
	private SymbolicTypeIF type;

	/**
	 * Every symbolic expression also has a user-settable id, which can be used
	 * any way the user wants.
	 */
	private int id = -1;

	/**
	 * Creates a new symbolic expression constant with the given type.
	 */
	protected SymbolicExpression(SymbolicTypeIF type) {
		this.type = type;
	}

	/**
	 * Returns the id (non-unique).
	 */
	public int id() {
		return id;
	}

	/**
	 * Sets the id (non-unique).
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the type of this symbolic expression.
	 */
	public SymbolicTypeIF type() {
		return type;
	}

	/**
	 * Compares two committed symbolic expressions. There is a total order on
	 * the committed symbolic expressions given by the unique IDs.
	 * 
	 * @param x
	 *            the first symbolic expression
	 * @param y
	 *            the second symbolic expression
	 * @return 0 if they are equal, -1 if the second come before the first, +1
	 *         otherwise
	 */
	public static int compare(SymbolicExpression x, SymbolicExpression y) {
		return x.uniqueId - y.uniqueId;
	}

	/**
	 * Each concrete class must implement this method and the intrinsic equals
	 * method, which define hash codes and equals methods based on the intrinsic
	 * characteristics of the symbolic expressions in that class.
	 */
	protected abstract int intrinsicHashCode();

	protected abstract boolean intrinsicEquals(SymbolicExpression that);

	/**
	 * Implements the flyweight pattern: if there already exists a committed
	 * symbolic expression which is equivalent to the given expression
	 * (according to intrinsicEquals) returns that one, otherwise, commits
	 * expression (adds to table), assigns it a new unique id, and returns it.
	 * 
	 * @param map
	 *            the map used to record the committed expressions, one from
	 *            each equivalence class
	 * @param expression
	 *            the symbolic expression to be flyweighted
	 * @return the unique representative of the equivalence class of expression,
	 *         which may be expression itself
	 */
	public static <T extends SymbolicExpression> T flyweight(
			Map<SymbolicExpressionKey<T>, T> map, T expression) {
		SymbolicExpressionKey<T> key = new SymbolicExpressionKey<T>(expression);
		T old = map.get(key);

		if (old == null) {
			expression.uniqueId = instanceCounter++;
			map.put(key, expression);
			return expression;
		}
		return old;
	}

	/**
	 * Hash code: unique id. Note this is only effective if invoked on committed
	 * expressions.
	 */
	@Override
	public int hashCode() {
		return uniqueId;
	}
}
