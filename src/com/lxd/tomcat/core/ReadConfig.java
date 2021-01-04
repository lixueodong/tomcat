package com.lxd.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig extends Properties{
	private static final long serialVersionUID = -2469933196619036407L;
	private static ReadConfig instance = new ReadConfig();
	
	private ReadConfig() {
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("web.properties")){
			load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ReadConfig getInstance() {
		return instance;
	}
}
