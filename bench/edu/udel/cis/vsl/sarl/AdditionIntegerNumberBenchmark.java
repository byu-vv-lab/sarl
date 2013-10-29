package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;


import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class AdditionIntegerNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;


	private static BigInteger bigZero = new BigInteger("0");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 
	private static BigInteger bigThirtyOne = new BigInteger("31"); 
	private static BigInteger bigTen = new BigInteger("10"); 
	private static BigInteger bigA1 = new BigInteger("21220"); 
	private static BigInteger bigA2 = new BigInteger("43784"); 
	private static BigInteger bigA3 = new BigInteger("452222");  
	private static BigInteger bigA4 = new BigInteger("48273"); 
	private static BigInteger bigA5 = new BigInteger("48273786");  
	private static BigInteger bigA6 = new BigInteger("23456782"); 
	private static BigInteger bigA7 = new BigInteger("48456782345678"); 
	private static BigInteger bigA8 = new BigInteger("23456782345678"); 





	
	
	
	public void AddIntegerNumberBenchmark1() { 
		
		IntegerNumber a = factory.integer(bigZero);
		IntegerNumber b = factory.integer(bigOne); 
		long x = System.nanoTime();
		factory.add(a, b); 
		long y = System.nanoTime();

		System.out.println(y-x);

	}  
	
	
	
public void AddIntegerNumberBenchmark2() { 
		
		IntegerNumber a = factory.integer(bigTwo);
		IntegerNumber b = factory.integer(bigTwo); 
		long x = System.nanoTime();
		factory.add(a, b); 
		long y = System.nanoTime();

		System.out.println(y-x);

	}  

public void AddIntegerNumberBenchmark5() { 
	
	IntegerNumber a = factory.integer(bigTen);
	IntegerNumber b = factory.integer(bigTwo); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}  
public void AddIntegerNumberBenchmar3() { 
	
	IntegerNumber a = factory.integer(bigTwoThousand);
	IntegerNumber b = factory.integer(bigThirtyOne); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}  



public void AddIntegerNumberBenchmar4() { 
	
	IntegerNumber a = factory.integer(bigTwoThousand);
	IntegerNumber b = factory.integer(bigThousand); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}  

public void AddIntegerNumberBenchmark6() { 
	
	IntegerNumber a = factory.integer(bigA1);
	IntegerNumber b = factory.integer(bigA2); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}  


public void AddIntegerNumberBenchmark7() { 
	
	IntegerNumber a = factory.integer(bigA3);
	IntegerNumber b = factory.integer(bigA2); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

} 


public void AddIntegerNumberBenchmark8() { 
	
	IntegerNumber a = factory.integer(bigA3);
	IntegerNumber b = factory.integer(bigA4); 
	long x = System.nanoTime();
	factory.multiply(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}   


public void AddIntegerNumberBenchmark9() { 
	
	IntegerNumber a = factory.integer(bigA5);
	IntegerNumber b = factory.integer(bigA6); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}  

public void AddIntegerNumberBenchmark10() { 
	
	IntegerNumber a = factory.integer(bigA7);
	IntegerNumber b = factory.integer(bigA8); 
	long x = System.nanoTime();
	factory.add(a, b); 
	long y = System.nanoTime();

	System.out.println(y-x);

}  
	
	
	
	
}