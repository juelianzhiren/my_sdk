package com.ztq.sdk.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {
	public static final int PORT = 8888;
	
	public static void main(String[] args) {
		ServerSocket ss = null;
		Socket s = null;
		
		try {
			 ss = new ServerSocket(PORT);
			 while(true) {
				 System.out.println("服务器等待客户端的连接请求...");
				 s = ss.accept();
				 System.out.println("客户端" + s.getInetAddress() + "连接成功！");
				 
				 new ServerThread(s).start();
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}