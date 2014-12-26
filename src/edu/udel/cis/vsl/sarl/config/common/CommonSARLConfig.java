package edu.udel.cis.vsl.sarl.config.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.config.Prover;
import edu.udel.cis.vsl.sarl.IF.config.Prover.ProverKind;
import edu.udel.cis.vsl.sarl.IF.config.SARLConfig;

public class CommonSARLConfig implements SARLConfig {

	private Prover[] provers;

	private Map<String, Prover> aliasMap = new LinkedHashMap<>();

	public CommonSARLConfig(Collection<Prover> provers) {
		int size = provers.size();
		int count = 0;

		this.provers = new Prover[size];
		for (Prover prover : provers) {
			this.provers[count] = prover;
			count++;
			for (String alias : prover.getAliases()) {
				Prover old = aliasMap.put(alias, prover);

				if (old != null)
					throw new SARLException("Alias " + alias
							+ " used more than once:\n" + old + "\n" + prover);
			}
		}
	}

	@Override
	public int getNumProvers() {
		return provers.length;
	}

	@Override
	public Prover getProver(int index) {
		return provers[index];
	}

	@Override
	public Iterable<Prover> getProvers() {
		return Arrays.asList(provers);
	}

	@Override
	public Prover getProverWithAlias(String alias) {
		return aliasMap.get(alias);
	}

	@Override
	public Prover getProverWithKind(ProverKind kind) {
		for (Prover prover : provers) {
			if (prover.getKind() == kind)
				return prover;
		}
		return null;
	}

}
