package com.zgy.ringforu.config;

/**
 * synchronized
 * 
 * ‘› ±≤ª”√
 * 
 * @author ZGY
 * 
 */

public class ConfigFileOpera {

	private static MainConfig mMainConfig;

	private static ConfigFileOpera mConfigFileOpera;

	private ConfigFileOpera() {

	}

	public static ConfigFileOpera getInstance() {

		if (mMainConfig == null) {
			mMainConfig = MainConfig.getInstance();
		}

		if (mConfigFileOpera == null) {
			mConfigFileOpera = new ConfigFileOpera();
		}

		return mConfigFileOpera;
	}

}
