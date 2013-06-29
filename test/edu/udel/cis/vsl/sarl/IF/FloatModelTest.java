package edu.udel.cis.vsl.sarl.IF;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.common.CommonNumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonPreUniverse;

public class FloatModelTest {

	private SymbolicUniverse universe;

	private NumberFactory numberFactory;

	private SymbolicRealType real;

	private NumericExpression one; // real 1.0

	private NumericSymbolicConstant x1, x2, x3, x4;

	private SymbolicFunctionType unaryFunc;

	private SymbolicConstant err;

	private NumericSymbolicConstant errBound, xBound;

	private Map<SymbolicExpression, SymbolicExpression> xmap = new HashMap<SymbolicExpression, SymbolicExpression>();

	private Map<SymbolicExpression, SymbolicExpression> emap = new Map<SymbolicExpression, SymbolicExpression>() {

		@Override
		public int size() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isEmpty() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsKey(Object key) {
			if (key instanceof SymbolicExpression) {
				SymbolicExpression expr = (SymbolicExpression) key;

				if (expr.operator() == SymbolicOperator.APPLY) {
					SymbolicExpression func = (SymbolicExpression) expr
							.argument(0);

					if (func.equals(err))
						return true;
				}
			}
			return false;
		}

		@Override
		public boolean containsValue(Object value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public SymbolicExpression get(Object key) {
			if (key instanceof SymbolicExpression) {
				SymbolicExpression expr = (SymbolicExpression) key;

				if (expr.operator() == SymbolicOperator.APPLY) {
					SymbolicExpression func = (SymbolicExpression) expr
							.argument(0);

					if (func.equals(err))
						return errBound;
				}
			}
			return null;
		}

		@Override
		public SymbolicExpression put(SymbolicExpression key,
				SymbolicExpression value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public SymbolicExpression remove(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void putAll(
				Map<? extends SymbolicExpression, ? extends SymbolicExpression> m) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<SymbolicExpression> keySet() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<SymbolicExpression> values() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<java.util.Map.Entry<SymbolicExpression, SymbolicExpression>> entrySet() {
			throw new UnsupportedOperationException();
		}
	};

	@Before
	public void setUp() throws Exception {
		List<SymbolicType> listReal;
		// listRealReal;

		this.universe = SARL.newStandardUniverse();
		this.numberFactory = universe.numberFactory();
		this.real = universe.realType();
		this.one = universe.oneReal();
		listReal = Arrays.asList(new SymbolicType[] { real });
		//listRealReal = Arrays.asList(new SymbolicType[] { real, real });
		this.x1 = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x1"), real);
		this.x2 = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x2"), real);
		this.x3 = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x3"), real);
		this.x4 = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x4"), real);
		this.unaryFunc = universe.functionType(listReal, real);
		//this.binaryFunc = universe.functionType(listRealReal, real);
		this.err = universe.symbolicConstant(universe.stringObject("e"),
				unaryFunc);
		this.errBound = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("E"), real);
		this.xBound = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("M"), real);
		this.xmap.put(x1, xBound);
		this.xmap.put(x2, xBound);
		this.xmap.put(x3, xBound);
		this.xmap.put(x4, xBound);
	}

	@After
	public void tearDown() throws Exception {
	}

	private NumericExpression plus(NumericExpression x, NumericExpression y) {
		NumericExpression sum = universe.add(x, y);
		NumericExpression error = (NumericExpression) universe.apply(err,
				Arrays.asList(new SymbolicExpression[] { sum }));

		return universe.multiply(sum, universe.add(one, error));
	}

	private NumericExpression positivizeTerm(NumericExpression term) {
		SymbolicOperator op = term.operator();

		if (op == SymbolicOperator.MULTIPLY) {
			int numArgs = term.numArguments();

			if (numArgs == 2) {
				NumericExpression factor = (NumericExpression) term.argument(0);
				Number number = universe.extractNumber(factor);

				if (number != null) {
					number = numberFactory.abs(number);

					return universe.multiply(universe.number(number),
							(NumericExpression) term.argument(1));
				}
			}
		} else if (op == SymbolicOperator.CONCRETE) {
			NumberObject obj = (NumberObject) term.argument(0);
			Number number = numberFactory.abs(obj.getNumber());

			return universe.number(number);
		}
		return term;
	}

	private Iterable<? extends NumericExpression> positivizeSequence(
			Iterable<? extends NumericExpression> sequence) {
		List<NumericExpression> result = new LinkedList<NumericExpression>();

		for (NumericExpression x : sequence)
			result.add(positivize(x));
		return result;
	}

	private NumericExpression positivize(NumericExpression x) {
		SymbolicOperator op = x.operator();

		if (op == SymbolicOperator.ADD) {
			int numArgs = x.numArguments();

			if (numArgs == 2)
				return universe.add(
						positivizeTerm((NumericExpression) x.argument(0)),
						positivizeTerm((NumericExpression) x.argument(1)));
			else {
				@SuppressWarnings("unchecked")
				Iterable<? extends NumericExpression> terms = (Iterable<? extends NumericExpression>) x
						.argument(0);

				return universe.add(positivizeSequence(terms));
			}
		}
		return positivizeTerm(x);
	}

	@Test
	public void test() {
		NumericExpression result1 = plus(plus(plus(x1, x2), x3), x4);
		NumericExpression result2 = plus(plus(x1, x2), plus(x3, x4));
		NumericExpression difference = universe.subtract(result1, result2);
		NumericExpression pos, abs;
		NumericExpression factorization;
		IdealFactory idealFactory = (IdealFactory) ((CommonNumericExpressionFactory) ((CommonPreUniverse) universe)
				.numericExpressionFactory()).idealFactory();

		System.out.println("difference  = " + difference);
		pos = positivize(difference);
		System.out.println("positivized = " + pos);
		abs = (NumericExpression) universe.substitute(pos, emap);
		System.out.println("upperBound1 = " + abs);
		abs = (NumericExpression) universe.substitute(abs, xmap);
		System.out.println("upperBound2 = " + abs);
		factorization = ((Polynomial) abs).factorization(idealFactory);
		System.out.println("            = " + factorization);
	}
}
