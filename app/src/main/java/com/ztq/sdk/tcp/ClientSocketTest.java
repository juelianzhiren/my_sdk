package com.ztq.sdk.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSocketTest {
	public static void main(String[] args) {
		Socket s = null;
		PrintStream ps = null;
		Scanner sc = null;
		BufferedReader br = null;
		
		try {
			s = new Socket("127.0.0.1", ServerSocketTest.PORT);
			System.out.println("客户端连接服务器成功！");
			
			sc = new Scanner(System.in);
			ps = new PrintStream(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while(true) {
				System.out.print("客户端请输入要发送的内容：");
				String str = sc.next();
				ps.println(str);
				System.out.println("客户端发送数据内容：" + str);
				if (str.equalsIgnoreCase("bye")) {
					System.out.println("客户端聊天结束！");
					break;
				}
				
				String str1 = br.readLine();
				System.out.println("客户端收到服务器的信息：" + str1);
				if (str1.equalsIgnoreCase("bye")) {
					 System.out.println("服务器已下线！");
					 break;
				 }
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			if (e.getMessage() != null && e.getMessage().equals("Connection reset")) {
				System.out.println("服务器已断开连接");
			} else {
				e.printStackTrace();
			}
		}  catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				ps.close();
			}
			if (sc != null) {
				sc.close();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}