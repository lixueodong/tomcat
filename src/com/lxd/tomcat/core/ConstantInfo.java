package com.lxd.tomcat.core;

public interface ConstantInfo {
	String BASE_PATH = ReadConfig.getInstance().getProperty("path");
	String DEFAULT_RESOURCE = ReadConfig.getInstance().getProperty("default");//获取默认页面
}
