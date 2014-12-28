package edu.udel.cis.vsl.sarl.config.common;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.config.Prover;

public class CommonExternalProver implements Prover {

	private Set<String> aliases = new HashSet<>();

	private List<String> options = new LinkedList<>();

	private ProverKind kind = null;

	private File path = null;

	private String version = null;

	private double timeout = -1;

	private boolean showQueries = false;

	public CommonExternalProver() {

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
		Iterator<String> iter = aliases.iterator();

		if (iter.hasNext())
			return iter.next();
		return "Prover";
	}

	@Override
	public boolean addAlias(String value) {
		return aliases.add(value);
	}

	@Override
	public List<String> getOptions() {
		return options;
	}

	@Override
	public void addOption(String value) {
		options.add(value);
	}

	@Override
	public double getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(double value) {
		this.timeout = value;
	}

	@Override
	public void setKind(ProverKind value) {
		this.kind = value;
	}

	@Override
	public void setPath(File value) {
		this.path = value;
	}

	@Override
	public void setVersion(String value) {
		this.version = value;
	}

	@Override
	public boolean getShowQueries() {
		return showQueries;
	}

	@Override
	public void setShowQueries(boolean value) {
		this.showQueries = value;
	}

}
