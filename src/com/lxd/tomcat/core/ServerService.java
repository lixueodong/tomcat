package com.lxd.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import com.lxd.web.core.HttpServletRequest;
import com.lxd.web.core.HttpServletResponse;
import com.lxd.web.core.ServletRequest;
import com.lxd.web.core.ServletResponse;


/**
 * 负责处理请求的信息的类
 * @author 26872
 *
 */
public class ServerService implements Runnable{
	private Socket sk;
	private InputStream is;
	private OutputStream os;
	
	public ServerService(Socket sk) {
		this.sk = sk;
	}

	@Override
	public void run() {
		try {
			this.is = sk.getInputStream();
			this.os = sk.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//解析请求头信息
		ServletRequest request = new HttpServletRequest(is);
		
		String url = request.getUrl();
		
		try {
			url = URLDecoder.decode(url,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServletResponse response = new HttpServletResponse(os);
		response.sendRedirect(url);
	}
}
