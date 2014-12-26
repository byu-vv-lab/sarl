package edu.udel.cis.vsl.sarl.config.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.config.Prover;
import edu.udel.cis.vsl.sarl.IF.config.Prover.ProverKind;
import edu.udel.cis.vsl.sarl.IF.config.SARLConfig;

public class ConfigFactory {

	/**
	 * Creates a SARLConfig by parsing a SARL configuration file.
	 * 
	 * @param configFile
	 * @return
	 * @throws IOException
	 */
	public static SARLConfig fromFile(File configFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(configFile));
		int lineno = 0;
		List<Prover> provers = new LinkedList<>();

		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			// format: "aliases : kind : path : version"
			lineno++;
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#"))
				continue;

			String[] components = line.split(":");
			int numComponents = components.length;

			if (numComponents != 4) {
				reader.close();
				throw new SARLException(configFile + " line " + lineno
						+ ": expected four components, saw " + numComponents);
			}
			for (int i = 0; i < numComponents; i++)
				components[i] = components[i].trim();

			String[] aliases = components[0].split(",");

			for (int i = 0; i < aliases.length; i++) {
				aliases[i] = aliases[i].trim();
			}

			ProverKind kind;

			switch (components[1]) {
			case "CVC3":
				kind = ProverKind.CVC3;
				break;
			case "CVC3_API":
				kind = ProverKind.CVC3_API;
				break;
			case "CVC4":
				kind = ProverKind.CVC4;
				break;
			case "CVC4_API":
				kind = ProverKind.CVC4_API;
				break;
			case "Z3":
				kind = ProverKind.Z3;
				break;
			case "Z3_API":
				kind = ProverKind.Z3_API;
				break;
			default:
				reader.close();
				throw new SARLException(configFile + " line " + lineno
						+ ": unknown prover kind " + components[1]);
			}

			File path;

			if (components[1].endsWith("API")) {
				path = null;
			} else {
				path = new File(components[2]);

				if (!path.exists()) {
					reader.close();
					throw new SARLException(configFile + " line " + lineno
							+ ": " + path + " does not exist");
				}
				if (!path.isFile()) {
					reader.close();
					throw new SARLException(configFile + " line " + lineno
							+ ": " + path + " is not a file");
				}
			}

			String version = components[3];

			provers.add(new CommonExternalProver(aliases, kind, path, version));
		}
		reader.close();
		return new CommonSARLConfig(provers);
	}

	private static File findSARLConfigInDir(File dir) {
		if (dir.isDirectory()) {
			File configFile = new File(dir, ".sarl");

			if (configFile.isFile())
				return configFile;
			configFile = new File(dir, ".sarl_default");
			if (configFile.isFile())
				return configFile;
		}
		return null;
	}

	public static File findSARLConfigFile() {
		File result = findSARLConfigInDir(new File(
				System.getProperty("user.dir")));

		if (result != null)
			return result;
		result = findSARLConfigInDir(new File(System.getProperty("user.home")));
		return result;
	}

	public static SARLConfig findConfig() throws IOException {
		File configFile = findSARLConfigFile();

		if (configFile == null)
			return null;
		return fromFile(configFile);
	}
}
