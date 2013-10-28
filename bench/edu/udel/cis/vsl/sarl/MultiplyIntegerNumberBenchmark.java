package edu.udel.cis.vsl.sarl;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealRational;


public class MultiplyIntegerNumberBenchmark { 
	
	
	private static PrintStream out = System.out;

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigNegativeThirty = new BigInteger("-30");
	private static BigInteger bigNegativeTen = new BigInteger("-10");
	private static BigInteger bigNegativeThree = new BigInteger("-3");
	private static BigInteger bigNegativeOne = new BigInteger("-1");
	private static BigInteger bigZero = new BigInteger("0");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2"); 
	private static BigInteger bigThirty = new BigInteger("30"); 
	private static BigInteger bigTwenty = new BigInteger("20"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 
	private static BigInteger bigFifteen = new BigInteger("15"); 
	private static BigInteger bigThirtyOne = new BigInteger("31"); 
	private static BigInteger bigTen = new BigInteger("10"); 
	private static BigInteger bigA1 = new BigInteger("21220"); 
	private static BigInteger bigA2 = new BigInteger("43784"); 
	private static BigInteger bigA3 = new BigInteger("452222");  
	private static BigInteger bigA4 = new BigInteger("48273"); 




	
	
	
	public void MultiplyIntegerNumberBenchmark1() { 
		
		IntegerNumber a = factory.integer(bigZero);
		IntegerNumber b = factory.integer(bigOne); 
		long x = System.currentTimeMillis() % 1000;
		factory.multiply(a, b); 
		long y = System.currentTimeMillis() % 1000;

		System.out.println(y-x);

	}  
	
	
	
public void MultiplyIntegerNumberBenchmark2() { 
		
		IntegerNumber a = factory.integer(bigTwo);
		IntegerNumber b = factory.integer(bigTwo); 
		long x = System.currentTimeMillis() % 1000;
		factory.multiply(a, b); 
		long y = System.currentTimeMillis() % 1000;

		System.out.println(y-x);

	}  

public void MultiplyIntegerNumberBenchmark5() { 
	
	IntegerNumber a = factory.integer(bigTen);
	IntegerNumber b = factory.integer(bigTwo); 
	long x = System.currentTimeMillis() % 1000;
	factory.multiply(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

}  
public void MultiplyIntegerNumberBenchmar3() { 
	
	IntegerNumber a = factory.integer(bigTwoThousand);
	IntegerNumber b = factory.integer(bigThirtyOne); 
	long x = System.currentTimeMillis() % 1000;
	factory.multiply(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

}  



public void MultiplyIntegerNumberBenchmar4() { 
	
	IntegerNumber a = factory.integer(bigTwoThousand);
	IntegerNumber b = factory.integer(bigThousand); 
	long x = System.currentTimeMillis() % 1000;
	factory.multiply(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

}  

public void MultiplyIntegerNumberBenchmark6() { 
	
	IntegerNumber a = factory.integer(bigA1);
	IntegerNumber b = factory.integer(bigA2); 
	long x = System.currentTimeMillis() % 1000;
	factory.multiply(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

}  


public void MultiplyIntegerNumberBenchmark7() { 
	
	IntegerNumber a = factory.integer(bigA3);
	IntegerNumber b = factory.integer(bigA2); 
	long x = System.currentTimeMillis() % 1000;
	factory.multiply(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

} 


public void MultiplyIntegerNumberBenchmark8() { 
	
	IntegerNumber a = factory.integer(bigA3);
	IntegerNumber b = factory.integer(bigA4); 
	long x = System.currentTimeMillis() % 1000;
	factory.multiply(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

}  
	
	
	
}