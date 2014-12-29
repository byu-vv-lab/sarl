package edu.udel.cis.vsl.sarl.config.common;

import java.io.File;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;

/**
 * Very simple implementation of {@link ProverInfo}.
 * 
 * @author siegel
 *
 */
public class CommonProverInfo implements ProverInfo {

	// Fields...

	private Set<String> aliases = new HashSet<>();

	private List<String> options = new LinkedList<>();

	private ProverKind kind = null;

	private File path = null;

	private String version = null;

	private double timeout = -1;

	private boolean showQueries = false;

	private boolean showInconclusives = false;

	private boolean showErrors = true;

	// Constructors...

	public CommonProverInfo() {
	}

	// Helper methods...

	// Exported methods...

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

	@Override
	public boolean getShowInconclusives() {
		return showInconclusives;
	}

	@Override
	public void setShowInconclusives(boolean value) {
		this.showInconclusives = value;
	}

	@Override
	public boolean getShowErrors() {
		return showErrors;
	}

	@Override
	public void setShowErrors(boolean value) {
		this.showErrors = value;
	}

	@Override
	public void print(PrintStream out) {
		out.println("prover {");

		if (!aliases.isEmpty()) {
			boolean first = true;

			out.print("  aliases = ");
			for (String alias : aliases) {
				if (first)
					first = false;
				else {
					out.print(", ");
					first = false;
				}
				out.print(alias);
			}
			out.println(";");
		}
		if (kind != null)
			out.println("  kind = " + kind + ";");
		if (version != null)
			out.println("  version = \"" + version + "\";");
		if (path != null)
			out.println("  path = \"" + path + "\";");
		if (!options.isEmpty()) {
			boolean first = true;

			out.print("  options = ");
			for (String option : options) {
				if (first)
					first = false;
				else {
					out.print(", ");
					first = false;
				}
				out.print("\"" + option + "\"");
			}
			out.println(";");
		}
		out.println("  timeout = " + timeout + ";");
		out.println("  showQueries = " + showQueries + ";");
		out.println("  showInconclusives = " + showInconclusives + ";");
		out.println("  showErrors = " + showErrors + ";");
		out.println("}");
		out.flush();
	}
}
