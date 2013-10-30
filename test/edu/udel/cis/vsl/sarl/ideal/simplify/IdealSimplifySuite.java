package edu.udel.cis.vsl.sarl.ideal.simplify;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SimpBoolTest.class, SimplifierIntervalTest.class, IdealSimplifierBBTest.class, SimplifyFPTest.class, IdealSimplifierTest.class,IdealSimplifierSimpExprTest.class, SimplifyEqualsZeroTest.class, DividesTest.class })
public class IdealSimplifySuite {

}
