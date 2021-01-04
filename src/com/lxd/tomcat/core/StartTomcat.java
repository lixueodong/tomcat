package com.lxd.tomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.IIOException;

/**
 * 启动类
 * @author 26872
 *
 */
public class StartTomcat {
	public static void main(String[] args) {
		try {
			new StartTomcat().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void start() throws IOException {
		//解析读取配置web.xml
		//System.out.println(ReadConfig.getInstance().getProperty("port"));
		int port = Integer.parseInt(ReadConfig.getInstance().getProperty("port"));
		//启动一个ServerSocket
		ServerSocket ssk = new ServerSocket(port);
		System.out.println("服务器已启动，占用端口："+port);
		
		new ParseWebXml();//读取解析文件后缀对应的类型
		
		//启动一个线程或者使用线程池处理用户发过来的请求
		ExecutorService serviceThread = Executors.newFixedThreadPool(20);
		
		Socket sk = null;
		while(true) {
			sk = ssk.accept();
			serviceThread.submit(new ServerService(sk));
		}
	}
}
