package com.ztq.sdk.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
	private Socket socket;

	public ServerThread(Socket s) {
		this.socket = s;
	}

	@Override
	public void run() {
		if (socket == null) {
			return;
		}
		BufferedReader br = null;
		PrintStream ps = null;
		Scanner sc = null;

		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ps = new PrintStream(socket.getOutputStream());
			sc = new Scanner(System.in);
			InetAddress ia = socket.getInetAddress();
			
			while (true) {
				String str = br.readLine();
				System.out.println("服务器收到客户端"+ ia + "发来的字符串：" + str);
				if (str.equalsIgnoreCase("bye")) {
					System.out.println("客户端" + ia + "已下线！");
					break;
				}

				System.out.print("服务器请输入要发送的内容：");
				String str1 = sc.next();
				ps.println(str1);
				System.out.println("服务器发送数据内容：" + str1);
				if (str1.equalsIgnoreCase("bye")) {
					System.out.println("服务器聊天结束！");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
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
		}
	}
}