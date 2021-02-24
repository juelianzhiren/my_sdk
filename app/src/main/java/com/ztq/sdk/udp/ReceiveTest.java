package com.ztq.sdk.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveTest {
	public static final int PORT = 9000;
	
	public static void main(String[] args) {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(PORT);
			
			byte[] arr = new byte[20];
			DatagramPacket dp = new DatagramPacket(arr, arr.length);
			System.out.println("等待数据的到来...");
			ds.receive(dp);
			System.out.println("接收到的数据内容：" + new String(arr, 0, dp.getLength()) + "!");
			
			byte[] arr2 = "I received!".getBytes();
			DatagramPacket dp2 = new DatagramPacket(arr2, arr2.length, dp.getAddress(), dp.getPort());
			ds.send(dp2);
			System.out.println("回发数据成功！");
			System.out.println("【接收方】接收数据报的端口为：" + PORT + "; 发送的数据报内置端口为：" + dp.getPort());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
}