package edu.udel.cis.vsl.sarl.numbers;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.Exponentiator;

public class ExponentiatorTest {
	
	private static NumberFactory factory = Numbers.REAL_FACTORY;
	private static BigInteger bigTen = new BigInteger("10");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	/*@Test
	public void compareInts() {
		Exponentiator a = new Exponentiator();//needs params
	}*/

}
