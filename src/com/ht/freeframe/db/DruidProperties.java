package com.ht.freeframe.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DruidProperties {
	private static final Log logger = LogFactory.getLog(DruidProperties.class);

	private final String confile = "dbconfig.properties";

	public Properties getProperties() {

		Properties p = new Properties();
		InputStream inputStream = null;
		try {
			// java应用
			if (inputStream == null) {
				inputStream = this.getClass().getClassLoader()
						.getResourceAsStream(confile);
			}
			p.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
}
