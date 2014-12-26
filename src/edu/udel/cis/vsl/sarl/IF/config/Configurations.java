package edu.udel.cis.vsl.sarl.IF.config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.config.common.CommonSARLConfig;
import edu.udel.cis.vsl.sarl.config.common.ConfigFactory;

public class Configurations {

	public static SARLConfig CONFIG = findConfiguration();

	public static SARLConfig newConfiguration(Collection<Prover> provers) {
		return new CommonSARLConfig(provers);
	}

	public static SARLConfig newConfiguration(File configFile) {
		try {
			return ConfigFactory.fromFile(configFile);
		} catch (IOException e) {
			throw new SARLException("I/O error accessing config file "
					+ configFile + ": " + e.getMessage());
		}
	}

	public static SARLConfig findConfiguration() {
		try {
			return ConfigFactory.findConfig();
		} catch (IOException e) {
			throw new SARLException("I/O error accessing config file: "
					+ e.getMessage());
		}
	}

}
