package edu.udel.cis.vsl.sarl.ideal.simplify;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SimplifyFPTest.class, IdealSimplifierTest.class,IdealSimplifierSimpExprTest.class, SimplifyEqualsZeroTest.class })
public class IdealSimplifySuite {

}
