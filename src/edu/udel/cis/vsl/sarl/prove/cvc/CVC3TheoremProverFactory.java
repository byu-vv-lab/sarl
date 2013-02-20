package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintWriter;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProverIF;

public class CVC3TheoremProverFactory {
  public static TheoremProverIF newCVC3TheoremProver(
      SymbolicUniverseIF universe, PrintWriter out, boolean showProverQueries) {
    return new CVC3TheoremProver(universe, out, showProverQueries);
  }
}
