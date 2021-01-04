package com.lxd.web.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.lxd.tomcat.core.ConstantInfo;
import com.lxd.tomcat.core.ParseWebXml;
import com.lxd.tomcat.util.StringUtil;

public class HttpServletResponse implements ServletResponse{
	private OutputStream os = null;
	private String basepath = ConstantInfo.BASE_PATH;
	private String projectName;
	
	public HttpServletResponse(OutputStream os,String projectName) {
		this.os = os;
		this.projectName = projectName;
	}
	
	
	@Override
	public void sendRedirect(String url) {
		if(StringUtil.checkNull(url)) {
			//报错404
			error404(url);
			return;
		}
		//   /jiaju/login
		if(url.startsWith("/") && url.indexOf("/") == url.lastIndexOf("/")) {// /jiaju
			send302(url);
			return;
		}else {//  /jiaju/index.html	/jiaju/		/jiaju/back/index.html
			if(url.endsWith("/")) {
				//说明没有指定具体资源
				String defaultPath = ConstantInfo.DEFAULT_RESOURCE;//获取默认页面
				
				File f1 = new File(basepath,url.substring(1).replace("/", "\\") + defaultPath);
				if(!f1.exists() || !f1.isFile()) {
					//报错
					error404(url);
					return;
				}
				send200(readFile(f1),url.substring(url.lastIndexOf(".")+1).toLowerCase());
				return;
			}
			File f1 = new File(basepath,url.substring(1).replace("/", "\\"));
			
			if(!f1.exists() || !f1.isFile()) {
				//报错
				error404(url);
				return;
			}
			send200(readFile(f1),url.substring(url.lastIndexOf(".")+1).toLowerCase());
		}
	}

	private void send200(byte[] bt, String extension) {
		String contentType = "text/html;charset=utf-8";
		String type = ParseWebXml.getContentType(extension);
		if(StringUtil.checkNull(type)) {
			contentType = type;
		}
		
		String responseHeader =  "HTTP/1.1 200 OK\r\nContent-Type: "+ contentType+"\r\nContent-Length:"+bt.length+"\r\n\r\n";
		try {
			os.write(responseHeader.getBytes());
			os.write(bt);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

	/**
	 * 读取指定的文件
	 * @param f1
	 * @return
	 */
	private byte[] readFile(File f1) {
		byte[] bt = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f1);
			bt = new byte[fis.available()];
			fis.read(bt);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fis !=null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return bt;
	}


	private void send302(String url) {
		try {
			String responseHeader = "HTTP/1.1 302 Moved Temporarily\r\nContent-Type: text/html;charset=utf-8\r\nLocation:"+ url +"\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 404报错
	 * @param url
	 */
	private void error404(String url) {
		try {
			String data = "<h1>HTTP Status 404 -"+url+"</h1>";
			String responseHeader = "HTTP/1.1 404 File Not Found\r\nContent-Type:text/html;charset=utf-8\r\nContent-Length:"+data.length()+"\r\n\r\n";
			os.write(responseHeader.getBytes());
			os.write(data.getBytes());
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(os !=null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}


	@Override
	public PrintWriter getWriter() throws IOException {
		String responseHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8\r\n\r\n";
		os.write(responseHeader.getBytes());
		os.flush();
		return new PrintWriter(os);
	}
	
}
