package edu.udel.cis.vsl.sarl.symbolic.constant;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

/**
 * Represents a symbolic constant.
 * 
 * Implements the flyweight pattern using intrinsic hashCode and intrinsic
 * equals. The equals and hashCode methods come from Object and should be fast.
 * These are used by the "outside world". The factory for creating
 * SymbolicConstants, however, must compute a real hash code and use a real
 * equals method in order to determine if an equivalent object has already been
 * created. These "real" methods are implemented here as "intrinsicHashCode" and
 * "intrinsicEquals". The class SymbolicConstantKey wraps a SymbolicConstant and
 * overrides Object's equals and hashCode methods by calling the intrinsic
 * methods in this class. This Key object is used as the key in a hashtable in
 * the factory to implement the flyweight pattern.
 */
public class SymbolicConstant implements SymbolicConstantIF {

	private static int classHashCode = SymbolicConstant.class.hashCode();

	private String name;

	private SymbolicTypeIF type;

	private int id;

	SymbolicConstant(String name, SymbolicTypeIF type) {
		assert type != null;
		assert name != null;
		this.name = name;
		this.type = type;
	}

	public int id() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String name() {
		return name;
	}

	public SymbolicTypeIF type() {
		return type;
	}

	protected int intrinsicHashCode() {
		return classHashCode + name.hashCode() + type().hashCode();
	}

	protected boolean intrinsicEquals(SymbolicConstant that) {
		return name.equals(that.name) && type().equals(that.type());
	}

	public String toString() {
		return name;
	}

	public String atomString() {
		return name;
	}

}
