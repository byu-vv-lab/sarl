package edu.udel.cis.vsl.sarl.config.common;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.config.Prover;

public class CommonExternalProver implements Prover {

	private Set<String> aliases;

	private ProverKind kind;

	private File path;

	private String version;

	public CommonExternalProver(String[] aliases, ProverKind kind,
			File path, String version) {
		this.aliases = new HashSet<String>();

		for (String alias : aliases)
			this.aliases.add(alias);
		this.kind = kind;
		this.path = path;
		this.version = version;
	}

	public CommonExternalProver(Iterable<String> aliases,
			ProverKind kind, File path, String version) {
		this.aliases = new HashSet<String>();

		for (String alias : aliases)
			this.aliases.add(alias);
		this.kind = kind;
		this.path = path;
		this.version = version;
	}

	@Override
	public Set<String> getAliases() {
		return aliases;
	}

	@Override
	public ProverKind getKind() {
		return kind;
	}

	@Override
	public File getPath() {
		return path;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		String result = "Prover[";
		boolean first = true;

		for (String alias : aliases) {
			if (first)
				first = false;
			else
				result += ",";
			result += alias;
		}
		result += "; " + kind + "; " + version + "; " + path + "]";
		return result;
	}

}
